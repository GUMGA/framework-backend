/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.application;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import org.apache.lucene.search.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author wlademir
 */
@Service
public class GumgaUntypedRepository {

    @PersistenceContext
    private EntityManager em;

    public GumgaUntypedRepository() {

    }

    @Transactional
    public void save(Object obj) {
        em.persist(obj);
    }

    public List<Object> fullTextSearchaaa(String text, Class entidade, String... atributos) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(entidade).get();
        Query query = qb.keyword().onFields(atributos).matching(text).createQuery();
        return fullTextEntityManager.createFullTextQuery(query, entidade).getResultList();
    }

    public List<Object> fullTextSearch(String text) {
        List aRetornar = new ArrayList();

        for (Class entidade : getAllIndexedEntities()) {
            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
            QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(entidade).get();
            String atributos = "";
            List<Field> todosAtributos = getTodosAtributos(entidade);
            for (Field f : todosAtributos) {
                if (f.isAnnotationPresent(org.hibernate.search.annotations.Field.class)) {
                    atributos += f.getName() + ",";
                }
            }
            if (!atributos.isEmpty()) {
                atributos = atributos.substring(0, atributos.length() - 1);
                Query query = qb.keyword().onFields(atributos.split(",")).matching(text).createQuery();
                aRetornar.addAll(fullTextEntityManager.createFullTextQuery(query, entidade).getResultList());
            }
        }
        return aRetornar;
    }

    public static List<Field> getTodosAtributos(Class classe) throws SecurityException {
        List<Field> aRetornar = new ArrayList<Field>();
        if (!classe.getSuperclass().equals(Object.class)) {
            aRetornar.addAll(getTodosAtributos(classe.getSuperclass()));
        }
        aRetornar.addAll(Arrays.asList(classe.getDeclaredFields()));
        return aRetornar;
    }

    private List<Class> getAllIndexedEntities() {
        List<Class> aRetornar = new ArrayList<>();
        Session session = em.unwrap(Session.class);
        SessionFactory sessionFactory = session.getSessionFactory();
        Map<String, ClassMetadata> map = (Map<String, ClassMetadata>) sessionFactory.getAllClassMetadata();
        for (String k : map.keySet()) {
            Class mappedClass = map.get(k).getMappedClass();
            if (mappedClass.isAnnotationPresent(Entity.class) && mappedClass.isAnnotationPresent(Indexed.class)) {
                aRetornar.add(mappedClass);
            }
        }
        return aRetornar;
    }

}
