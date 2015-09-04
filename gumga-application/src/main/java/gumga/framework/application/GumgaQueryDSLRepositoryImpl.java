package gumga.framework.application;

import com.google.common.base.Strings;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.*;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.ComparablePath;
import com.mysema.query.types.path.PathBuilder;
import gumga.framework.core.GumgaThreadScope;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.domains.GumgaOi;
import gumga.framework.domain.repository.GumgaQueryDSLRepository;
import gumga.framework.domain.repository.ISpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.QSort;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;


@NoRepositoryBean
public class GumgaQueryDSLRepositoryImpl<T, ID extends Serializable> extends GumgaGenericRepository<T, ID> implements GumgaQueryDSLRepository<T, ID> {

    private final EntityPath<T> path;
    private final Querydsl querydsl;

    public GumgaQueryDSLRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);

        this.path = SimpleEntityPathResolver.INSTANCE.createPath(entityInformation.getJavaType());
        PathBuilder<T> builder = new PathBuilder<>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(entityManager, builder);
    }

    public T findOne(Predicate predicate) {
        return createQuery(predicate).uniqueResult(path);
    }

    public List<T> findAll() {
        return createQuery().list(path);
    }

    public List<T> findAll(Predicate predicate) {
        return findAll(toSpecification(predicate), path);
    }

    public List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {
        JPQLQuery query = createQuery(predicate);
        query = querydsl.applySorting(new QSort(orders), query);
        return query.list(path);
    }

    @Override
    public List<T> findAll(ISpecification specification) {
        return findAll(specification, path);
    }

    @Override
    public <A> List<A> findAll(ISpecification specification, Expression<A> projection) {
        return createQuery(specification).list(projection);
    }

    @Override
    public Page<T> findAll(ISpecification specification, Pageable page) {
        return findAll(specification, page, path);
    }

    @Override
    public <A> Page<A> findAll(ISpecification specification, Pageable page, Expression<A> projection) {
        long total = createQuery(specification).count();

        JPQLQuery query = createQuery(specification);
        query = querydsl.applyPagination(page, query);
        List<A> content = total > page.getOffset() ? query.list(projection) : Collections.<A>emptyList();

        return new PageImpl<>(content, page, total);
    }

    @Override
    public SearchResult<T> search(Predicate predicate, Pageable page) {
        return search(toSpecification(predicate), page);
    }

    @Override
    public <A> SearchResult<A> search(Predicate predicate, Pageable page, Expression<A> projection) {
        return search(toSpecification(predicate), page, projection);
    }

    @Override
    public SearchResult<T> search(ISpecification specification, Pageable page) {
        return search(specification, page, path);
    }

    @Override
    public <A> SearchResult<A> search(ISpecification specification, Pageable page, Expression<A> projection) {
        return createResultFromPageResult(page, this.findAll(specification, page, projection));
    }

    @Override
    public <A> A findOne(ISpecification specification, Expression<A> projection) {
        return specification.createQuery(querydsl.createQuery(path)).uniqueResult(projection);
    }

    @Override
    public Page<T> findAll(Predicate predicate, Pageable pageable) {
        return findAll(toSpecification(predicate), pageable, path);
    }

    @Override
    public long count(Predicate predicate) {
        return createQuery(predicate).count();
    }

    private <A> SearchResult<A> createResultFromPageResult(Pageable page, Page<A> result) {
        return new SearchResult<>(page.getOffset(), page.getPageSize(), result.getTotalElements(), result.getContent());
    }

    public JPQLQuery createQuery(Predicate... predicate) {
        return createQuery(toSpecification(predicate));
    }

    private ISpecification toSpecification(Predicate... predicate) {
        return query -> query.where(predicate);
    }

    private JPQLQuery createQuery(ISpecification specification) {
        JPQLQuery query = specification.createQuery(querydsl.createQuery(path));

        if (this.hasMultitenancy()) {
            query.where(getOiExpression());
        }

        return query;
    }

    private BooleanExpression getOiExpression() {
        String result = GumgaThreadScope.organizationCode.get();
        result = Strings.isNullOrEmpty(result) ? "" : result;
        ComparablePath<GumgaOi> oi = new ComparablePath<>(GumgaOi.class, PathMetadataFactory.forProperty(this.path, "oi"));
        return oi.stringValue().startsWith(result).or(oi.isNull());
    }

}
