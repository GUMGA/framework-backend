package gumga.framework.application;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.PathBuilder;
import gumga.framework.core.SearchResult;
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

	public List<T> findAll(Predicate predicate) {
		return createQuery(predicate).list(path);
	}

	public List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {

        JPQLQuery query = createQuery(predicate);
		query = querydsl.applySorting(new QSort(orders), query);
		return query.list(path);
	}

    @Override
    public <A> List<A> findAll(ISpecification specification, Expression<A> projection) {
        JPQLQuery query = specification.createQuery(querydsl.createQuery(path));
        return query.list(projection);
    }

    @Override
    public <A> Page<A> findAll(ISpecification specification, Pageable page, Expression<A> projection) {
        return null;
    }


    @Override
    public List<T> findAll(ISpecification specification) {
        JPQLQuery query = specification.createQuery(querydsl.createQuery(path));
        return query.list(this.path);
    }

    @Override
    public Page<T> findAll(ISpecification specification, Pageable page) {
        return null;
    }

    @Override
    public SearchResult<T> search(Predicate predicate, Pageable page) {
        return createResultFromPageResult(page, this.findAll(predicate, page));
    }

    @Override
    public <A> SearchResult<A> search(Predicate predicate, Pageable page, Expression<A> projection) {
        return createResultFromPageResult(page, this.findAll(predicate, page, projection));
    }

    @Override
    public SearchResult<T> search(ISpecification specification, Pageable page) {
        return createResultFromPageResult(page, this.findAll(specification, page));
    }

    @Override
    public <A> SearchResult<A> search(ISpecification specification, Pageable page, Expression<A> projection) {
        return createResultFromPageResult(page, this.findAll(specification, page, projection));
    }

    public <A> A findOne(ISpecification specification, Expression<A> projection) {
        return specification.createQuery(querydsl.createQuery(path)).uniqueResult(projection);
    }

    public Page<T> findAll(Predicate predicate, Pageable pageable) {
		JPQLQuery countQuery = createQuery(predicate);
		JPQLQuery query = querydsl.applyPagination(pageable, createQuery(predicate));

		Long total = countQuery.count();
		List<T> content = total > pageable.getOffset() ? query.list(path) : Collections.<T> emptyList();

		return new PageImpl<>(content, pageable, total);
	}


    private <A> SearchResult<A> createResultFromPageResult(Pageable page, Page<A> result) {
        return new SearchResult<>(page.getOffset(), page.getPageSize(), result.getTotalElements(), result.getContent());
    }


    public long count(Predicate predicate) {
		return createQuery(predicate).count();
	}

	public JPQLQuery createQuery(Predicate... predicate) {
		return querydsl.createQuery(path).where(predicate);
	}

    private <A> Page<A> findAll(Predicate predicate, Pageable pageable, Expression<A> projection) {

        JPQLQuery countQuery = createQuery(predicate);
        JPQLQuery query = querydsl.applyPagination(pageable, createQuery(predicate));

        Long total = countQuery.count();
        List<A> content = total > pageable.getOffset() ? query.list(projection) : Collections.<A> emptyList();

        return new PageImpl<>(content, pageable, total);
    }


}
