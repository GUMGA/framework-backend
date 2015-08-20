package gumga.framework.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import gumga.framework.core.GumgaThreadScope;
import static org.hibernate.criterion.Order.asc;
import static org.hibernate.criterion.Order.desc;
import static org.hibernate.criterion.Projections.rowCount;
import static org.hibernate.criterion.Restrictions.or;
import static org.hibernate.criterion.Restrictions.like;
import gumga.framework.core.QueryObject;
import gumga.framework.core.QueryObjectElement;
import gumga.framework.core.SearchResult;
import gumga.framework.core.utils.ReflectionUtils;
import gumga.framework.domain.*;
import gumga.framework.domain.domains.GumgaMoney;
import gumga.framework.domain.repository.GumgaCrudRepository;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.CrudMethodMetadata;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public class GumgaGenericRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements GumgaCrudRepository<T, ID> {

    protected final JpaEntityInformation<T, ID> entityInformation;
    protected final EntityManager entityManager;

    public GumgaGenericRepository(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.entityInformation = entityInformation;
    }

    @Override
    public SearchResult<T> search(QueryObject query) {

        if (query.isAdvanced()) {
            return advancedSearch(query);
        }

        Long count = count(query);
        List<T> data = getOrdered(query);

        return new SearchResult<T>(query, count, data);
    }

    private List<T> getOrdered(QueryObject query) {
        Pesquisa<T> pesquisa = getPesquisa(query);
        String sortField = query.getSortField();
        String sortType = query.getSortDir();

        if (!sortField.isEmpty()) {
            createAliasIfNecessary(pesquisa, sortField);
            pesquisa.addOrder(sortType.equals("asc") ? asc(sortField).ignoreCase() : desc(sortField).ignoreCase());
        }

        return pesquisa.setFirstResult(query.getStart()).setMaxResults(query.getPageSize()).list();
    }

    private Long count(QueryObject query) {
        Object uniqueResult = getPesquisa(query).setProjection(rowCount()).uniqueResult();
        return uniqueResult == null ? 0L : ((Number) uniqueResult).longValue();
    }

    private Pesquisa<T> getPesquisa(QueryObject query) {
        if (query.getSearchFields() != null && query.getSearchFields().length == 0) {
            throw new IllegalArgumentException("Para realizar a search deve se informar pelo menos um campo a ser pesquisado.");
        }

        Criterion[] fieldsCriterions = new HibernateQueryObject(query).getCriterions(entityInformation.getJavaType());
        Pesquisa<T> pesquisa = search().add(or(fieldsCriterions));

        if (hasMultitenancy()) {
            String oiValue = (GumgaThreadScope.organizationCode.get() == null) ? "" : GumgaThreadScope.organizationCode.get();
            Criterion multitenancyCriterion = or(like("oi", oiValue, MatchMode.START), Restrictions.isNull("oi"));
            pesquisa.add(multitenancyCriterion);
        }

        if (query.getSearchFields() != null) {
            for (String field : query.getSearchFields()) {
                createAliasIfNecessary(pesquisa, field);
            }
        }

        return pesquisa;
    }

    public boolean hasMultitenancy() {
        return entityInformation.getJavaType().isAnnotationPresent(GumgaMultitenancy.class);
    }

    private void createAliasIfNecessary(Pesquisa<T> pesquisa, String field) {
        String[] chain = field.split("\\.");

        if (chain.length <= 1) {
            return;
        }
        if (pesquisa.getAliases().contains(chain[0])) {
            return;
        }

        pesquisa.createAlias(chain[0], chain[0]);
        pesquisa.addAlias(chain[0]);
    }

    @Override
    public Pesquisa<T> search() {
        return Pesquisa.createCriteria(session(), entityInformation.getJavaType());
    }

    @Override
    public T findOne(ID id) {
        T resource = super.findOne(id);

        if (resource == null) {
            throw new EntityNotFoundException("cannot find " + entityInformation.getJavaType() + " with id: " + id);
        }
        checkOwnership(resource);
        return resource;
    }

    private Session session() {
        return entityManager.unwrap(Session.class);
    }

    private SearchResult<T> advancedSearch(QueryObject query) {

        System.out.println("----------------ADVANCED SEARCH-AQ------------>" + query.getAq());
        List<QueryObjectElement> qoeFromString = qoeFromString(query.getAqo());
        String hqlFromQes = hqlFromQoes(qoeFromString);
        System.out.println("----------------ADVANCED SEARCH-hqL from QES-->" + hqlFromQes);

        if (!QueryObject.EMPTY.equals(query.getAqo())) {
            query.setAq(hqlFromQes);
        }

        String modelo = "from %s obj WHERE %s";
        if (hasMultitenancy()) {
            modelo = "from %s obj WHERE (obj.oi is null OR obj.oi like '" + GumgaThreadScope.organizationCode.get() + "%%')  AND (%s) ";
        }

        String hqlConsulta = "";
        if (query.getSortField().isEmpty()) {
            hqlConsulta = String.format(modelo + " ORDER BY obj.id ", entityInformation.getEntityName(), query.getAq());
        } else {
            hqlConsulta = String.format(modelo + " ORDER BY %s %s, obj.id", entityInformation.getEntityName(), query.getAq(), query.getSortField(), query.getSortDir());
        }
        String hqlConta = String.format("SELECT count(obj) " + modelo, entityInformation.getEntityName(), query.getAq());
        Query qConta = entityManager.createQuery(hqlConta);
        Query qConsulta = entityManager.createQuery(hqlConsulta);
        Long total = (Long) qConta.getSingleResult();
        qConsulta.setMaxResults(query.getPageSize());
        qConsulta.setFirstResult(query.getStart());
        List resultList = qConsulta.getResultList();
        return new SearchResult<T>(query, total, resultList);
    }

    @Override
    public <A> SearchResult<A> advancedSearch(String selectQueryWithoutWhere, String countQuery, String ordenationId, QueryObject whereQuery) {
        if (Strings.isNullOrEmpty(ordenationId)) {
            throw new IllegalArgumentException("Para realizar a search deve se informar um OrdenationId para complementar a ordenação");
        }

        String modelo = selectQueryWithoutWhere + " WHERE %s";
        String hqlConsulta;
        if (whereQuery.getSortField().isEmpty()) {
            hqlConsulta = String.format(modelo, whereQuery.getAq());
        } else {
            hqlConsulta = String.format(modelo + " ORDER BY %s %s, %s", whereQuery.getAq(), whereQuery.getSortField(), whereQuery.getSortDir(), ordenationId);
        }
        String hqlConta = countQuery + " WHERE " + whereQuery.getAq();
        Query qConta = entityManager.createQuery(hqlConta);
        Query qConsulta = entityManager.createQuery(hqlConsulta);
        Long total = (Long) qConta.getSingleResult();
        qConsulta.setMaxResults(whereQuery.getPageSize());
        qConsulta.setFirstResult(whereQuery.getStart());
        List resultList = qConsulta.getResultList();

        return new SearchResult<A>(whereQuery, total, resultList);
    }

    public SearchResult<T> search(String hql, Map<String, Object> params) {
        Query query = entityManager.createQuery(hql);
        if (params != null) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        List<T> result = query.getResultList();
        int total = result.size();
        return new SearchResult<T>(0, total, total, result);
    }

    @Override
    protected TypedQuery<Long> getCountQuery(Specification<T> spec) {
        return super.getCountQuery(spec); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected TypedQuery<T> getQuery(Specification<T> spec, Sort sort) {
        return super.getQuery(spec, sort); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected TypedQuery<T> getQuery(Specification<T> spec, Pageable pageable) {
        return super.getQuery(spec, pageable); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Page<T> readPage(TypedQuery<T> query, Pageable pageable, Specification<T> spec) {
        return super.readPage(query, pageable, spec); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void flush() {
        super.flush(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <S extends T> List<S> save(Iterable<S> entities) {
        return super.save(entities); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <S extends T> S saveAndFlush(S entity) {
        return super.saveAndFlush(entity); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <S extends T> S save(S entity) {
        //setOi(entity);
        return super.save(entity); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long count(Specification<T> spec) {
        return super.count(spec); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long count() {
        return super.count(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<T> findAll(Specification<T> spec, Sort sort) {
        return super.findAll(spec, sort); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return super.findAll(spec, pageable); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<T> findAll(Specification<T> spec) {
        return super.findAll(spec); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T findOne(Specification<T> spec) {
        return super.findOne(spec); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return super.findAll(pageable); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<T> findAll(Sort sort) {
        return super.findAll(sort); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<T> findAll(Iterable<ID> ids) {
        return super.findAll(ids); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<T> findAll() {
        return super.findAll(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean exists(ID id) {
        return super.exists(id); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T getOne(ID id) {
        return super.getOne(id); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteAllInBatch() {
        super.deleteAllInBatch(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteAll() {
        super.deleteAll(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteInBatch(Iterable<T> entities) {
        super.deleteInBatch(entities); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Iterable<? extends T> entities) {
        super.delete(entities); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(T entity) {
        super.delete(entity); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(ID id) {
        super.delete(id); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Class<T> getDomainClass() {
        return super.getDomainClass(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRepositoryMethodMetadata(CrudMethodMetadata crudMethodMetadata) {
        super.setRepositoryMethodMetadata(crudMethodMetadata); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<GumgaObjectAndRevision> listOldVersions(ID id) {
        List<GumgaObjectAndRevision> aRetornar = new ArrayList<>();
        AuditReader ar = AuditReaderFactory.get(entityManager);
        List<Number> revisoes = ar.getRevisions(entityInformation.getJavaType(), id);
        for (Number n : revisoes) {
            GumgaRevisionEntity gumgaRevisionEntity = entityManager.find(GumgaRevisionEntity.class, n.longValue());
            T object = ar.find(entityInformation.getJavaType(), id, n.longValue());
            aRetornar.add(new GumgaObjectAndRevision(gumgaRevisionEntity, object));
        }
        return aRetornar;
    }

    private void checkOwnership(T o) throws EntityNotFoundException {
        if (GumgaThreadScope.ignoreCheckOwnership.get() != null && GumgaThreadScope.ignoreCheckOwnership.get()) {
            return;
        }
        if (!o.getClass().isAnnotationPresent(GumgaMultitenancy.class)) {
            return;
        }
        GumgaModel object = (GumgaModel) o;
        if (hasMultitenancy()) {
            if (object.getOi() != null) {
                if (GumgaThreadScope.organizationCode.get() == null || !object.getOi().getValue().startsWith(GumgaThreadScope.organizationCode.get())) {
                    throw new EntityNotFoundException("cannot find object of " + entityInformation.getJavaType() + " with id: " + object.getId() + " in your organization");
                }
            }
        }
    }

    public List<QueryObjectElement> qoeFromString(String s) {
        List<QueryObjectElement> aRetornar = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode mainNode = mapper.readTree(s);
            Iterator<JsonNode> elements = mainNode.elements();
            while (elements.hasNext()) {
                QueryObjectElement qoe = new QueryObjectElement();
                aRetornar.add(qoe);
                JsonNode node = elements.next();
                if (node.has("attribute")) {
                    qoe.setAttribute(node.get("attribute").get("name").asText());
                }
                if (node.has("hql")) {
                    qoe.setHql(node.get("hql").get("hql").asText());
                }
                qoe.setValue(node.get("value").asText());
            }
        } catch (Exception ex) {
            System.out.println("--------Jackson -->" + ex.toString());
        }
        return aRetornar;

    }

    public String hqlFromQoes(List<QueryObjectElement> qoes) {
        // ----------------ADVANCED SEARCH-AQ------------>obj.nome like '%a%' AND obj.quantidade=3
        //----------------ADVANCED SEARCH-AQO----------->[{"attribute":{"name":"nome","type":"string","$$hashKey":"object:69"},"hql":{"hql":"contains","label":"contém","before":" like '%","after":"%'","$$hashKey":"object:134"},"value":"a","$$hashKey":"object:149"},{"value":"AND","$$hashKey":"object:165"},{"attribute":{"name":"quantidade","type":"number","$$hashKey":"object:71"},"hql":{"hql":"eq","label":"igual","before":"=","after":"","$$hashKey":"object:153"},"value":"3","$$hashKey":"object:166"}]
        String aRetornar = "";
        try {
            for (QueryObjectElement qoe : qoes) {
                if ("NO_ATTRIBUTE".equals(qoe.getAttribute())) {
                    aRetornar += " " + qoe.getValue() + " ";
                } else {
                    aRetornar += "obj." + qoe.getAttribute();
                    Class type = String.class;
                    Field field = ReflectionUtils.findField(entityInformation.getJavaType(), qoe.getAttribute());
                    if (field != null) {
                        type = field.getType();

                    }

                    FieldStereotype fieldStereotype = getFieldStereotype(type);

                    HqlEntry het = new HqlEntry(fieldStereotype, qoe.getHql());
                    HqlElement hel = getHqlConverter().get(het);
                    aRetornar += hel.before + qoe.getValue() + hel.after;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return aRetornar;
    }

    class HqlEntry {

        public final FieldStereotype type;
        public final String operator;

        public HqlEntry(FieldStereotype type, String operator) {
            this.type = type;
            this.operator = operator;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 59 * hash + Objects.hashCode(this.type);
            hash = 59 * hash + Objects.hashCode(this.operator);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final HqlEntry other = (HqlEntry) obj;
            if (!Objects.equals(this.type, other.type)) {
                return false;
            }
            if (!Objects.equals(this.operator, other.operator)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "HqlEntry{" + "type=" + type + ", operator=" + operator + '}';
        }

    }

    class HqlElement {

        public final String before;
        public final String after;

        public HqlElement(String before, String after) {
            this.before = before;
            this.after = after;
        }

        @Override
        public String toString() {
            return "HqlElement{" + "before=" + before + ", after=" + after + '}';
        }

    }

    enum FieldStereotype {

        TEXT,
        NUMBER,
        LOGIC,
        DATE,
        DEFAULT

    }

    private Map<HqlEntry, HqlElement> hqlConverter;

    public Map<HqlEntry, HqlElement> getHqlConverter() {
        if (hqlConverter == null) {
            hqlConverter = new HashMap<>();

            hqlConverter.put(new HqlEntry(FieldStereotype.DEFAULT, "eq"), new HqlElement("='", "'"));
            hqlConverter.put(new HqlEntry(FieldStereotype.DEFAULT, "ne"), new HqlElement("!='", "'"));
            hqlConverter.put(new HqlEntry(FieldStereotype.DEFAULT, "ge"), new HqlElement(">='", "'"));
            hqlConverter.put(new HqlEntry(FieldStereotype.DEFAULT, "le"), new HqlElement("<='", "'"));
            hqlConverter.put(new HqlEntry(FieldStereotype.DEFAULT, "gt"), new HqlElement(">'", "'"));
            hqlConverter.put(new HqlEntry(FieldStereotype.DEFAULT, "lt"), new HqlElement("<'", "'"));
            hqlConverter.put(new HqlEntry(FieldStereotype.DEFAULT, "contains"), new HqlElement(" like '%", "%'"));
            hqlConverter.put(new HqlEntry(FieldStereotype.DEFAULT, "not_contains"), new HqlElement("not like '%", "%'"));
            hqlConverter.put(new HqlEntry(FieldStereotype.DEFAULT, "starts_with"), new HqlElement("like '", "%'"));
            hqlConverter.put(new HqlEntry(FieldStereotype.DEFAULT, "ends_with"), new HqlElement("like '%", "'"));

            hqlConverter.put(new HqlEntry(FieldStereotype.NUMBER, "eq"), new HqlElement("=", ""));
            hqlConverter.put(new HqlEntry(FieldStereotype.NUMBER, "ne"), new HqlElement("!=", ""));
            hqlConverter.put(new HqlEntry(FieldStereotype.NUMBER, "ge"), new HqlElement(">=", ""));
            hqlConverter.put(new HqlEntry(FieldStereotype.NUMBER, "le"), new HqlElement("<=", ""));
            hqlConverter.put(new HqlEntry(FieldStereotype.NUMBER, "gt"), new HqlElement(">", ""));
            hqlConverter.put(new HqlEntry(FieldStereotype.NUMBER, "lt"), new HqlElement("<", ""));

        }
        return hqlConverter;
    }

    public FieldStereotype getFieldStereotype(Class type) {
        if (java.lang.Byte.class.equals(type)) {
            return FieldStereotype.NUMBER;
        }
        if (java.lang.Double.class.equals(type)) {
            return FieldStereotype.NUMBER;
        }
        if (java.lang.Float.class.equals(type)) {
            return FieldStereotype.NUMBER;
        }
        if (java.lang.Integer.class.equals(type)) {
            return FieldStereotype.NUMBER;
        }
        if (java.lang.Long.class.equals(type)) {
            return FieldStereotype.NUMBER;
        }
        if (java.lang.Number.class.equals(type)) {
            return FieldStereotype.NUMBER;
        }
        if (java.lang.Short.class.equals(type)) {
            return FieldStereotype.NUMBER;
        }
        if (GumgaMoney.class.equals(type)) {
            return FieldStereotype.NUMBER;
        }

        return FieldStereotype.DEFAULT;
    }

}

/*
 hqlConverter.put(new HqlEntry(FieldStereotype.TEXT, "eq"), new HqlElement("='", "'"));
 hqlConverter.put(new HqlEntry(FieldStereotype.TEXT, "ne"), new HqlElement("!='", "'"));
 hqlConverter.put(new HqlEntry(FieldStereotype.TEXT, "contains"), new HqlElement(" like '%", "%'"));
 hqlConverter.put(new HqlEntry(FieldStereotype.TEXT, "not_contains"), new HqlElement("not like '%", "%'"));
 hqlConverter.put(new HqlEntry(FieldStereotype.TEXT, "starts_with"), new HqlElement("like '", "%'"));
 hqlConverter.put(new HqlEntry(FieldStereotype.TEXT, "ends_with"), new HqlElement("like '%", "'"));
 hqlConverter.put(new HqlEntry(FieldStereotype.TEXT, "ge"), new HqlElement(">='", "'"));
 hqlConverter.put(new HqlEntry(FieldStereotype.TEXT, "le"), new HqlElement("<='", "'"));
 hqlConverter.put(new HqlEntry(FieldStereotype.TEXT, "gt"), new HqlElement(">'", "'"));
 hqlConverter.put(new HqlEntry(FieldStereotype.TEXT, "lt"), new HqlElement("<'", "'"));

 hqlConverter.put(new HqlEntry(FieldStereotype.NUMBER, "eq"), new HqlElement("='", "'"));
 hqlConverter.put(new HqlEntry(FieldStereotype.NUMBER, "ne"), new HqlElement("!='", "'"));
 hqlConverter.put(new HqlEntry(FieldStereotype.NUMBER, "ge"), new HqlElement(">='", "'"));
 hqlConverter.put(new HqlEntry(FieldStereotype.NUMBER, "le"), new HqlElement("<='", "'"));
 hqlConverter.put(new HqlEntry(FieldStereotype.NUMBER, "gt"), new HqlElement(">'", "'"));
 hqlConverter.put(new HqlEntry(FieldStereotype.NUMBER, "lt"), new HqlElement("<'", "'"));

 hqlConverter.put(new HqlEntry(FieldStereotype.LOGIC, "eq"), new HqlElement("='", "'"));
 hqlConverter.put(new HqlEntry(FieldStereotype.LOGIC, "ne"), new HqlElement("!='", "'"));

 hqlConverter.put(new HqlEntry(FieldStereotype.DATE, "eq"), new HqlElement("='", "'"));
 hqlConverter.put(new HqlEntry(FieldStereotype.DATE, "ne"), new HqlElement("!='", "'"));
 hqlConverter.put(new HqlEntry(FieldStereotype.DATE, "ge"), new HqlElement(">='", "'"));
 hqlConverter.put(new HqlEntry(FieldStereotype.DATE, "le"), new HqlElement("<='", "'"));
 hqlConverter.put(new HqlEntry(FieldStereotype.DATE, "gt"), new HqlElement(">'", "'"));
 hqlConverter.put(new HqlEntry(FieldStereotype.DATE, "lt"), new HqlElement("<'", "'"));

 if (java.lang.Boolean.class.equals(type)) {
 fieldStereotype = FieldStereotype.LOGIC;
 }

 if (java.lang.String.class.equals(type)) {
 fieldStereotype = FieldStereotype.TEXT;
 }
 if (java.lang.Character.class.equals(type)) {
 fieldStereotype = FieldStereotype.TEXT;
 }
 if (java.lang.Enum.class.equals(type)) {
 fieldStereotype = FieldStereotype.TEXT;
 }

 if (java.lang.Byte.class.equals(type)) {
 fieldStereotype = FieldStereotype.NUMBER;
 }
 if (java.lang.Double.class.equals(type)) {
 fieldStereotype = FieldStereotype.NUMBER;
 }
 if (java.lang.Float.class.equals(type)) {
 fieldStereotype = FieldStereotype.NUMBER;
 }
 if (java.lang.Integer.class.equals(type)) {
 fieldStereotype = FieldStereotype.NUMBER;
 }
 if (java.lang.Long.class.equals(type)) {
 fieldStereotype = FieldStereotype.NUMBER;
 }
 if (java.lang.Number.class.equals(type)) {
 fieldStereotype = FieldStereotype.NUMBER;
 }
 if (java.lang.Short.class.equals(type)) {
 fieldStereotype = FieldStereotype.NUMBER;
 }



 */
