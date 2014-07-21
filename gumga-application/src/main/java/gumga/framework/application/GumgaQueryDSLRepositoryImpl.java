package gumga.framework.application;

import gumga.framework.domain.repository.GumgaQueryDSLRepository;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.querydsl.QSort;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.repository.NoRepositoryBean;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.PathBuilder;

@NoRepositoryBean
public class GumgaQueryDSLRepositoryImpl<T, ID extends Serializable> extends GumgaGenericRepository<T, ID> implements GumgaQueryDSLRepository<T, ID> {
	
	private final EntityPath<T> path;
	private final PathBuilder<T> builder;
	private final Querydsl querydsl;
	
	public GumgaQueryDSLRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		
		this.path = SimpleEntityPathResolver.INSTANCE.createPath(entityInformation.getJavaType());
		this.builder = new PathBuilder<T>(path.getType(), path.getMetadata());
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

	public Page<T> findAll(Predicate predicate, Pageable pageable) {
		JPQLQuery countQuery = createQuery(predicate);
		JPQLQuery query = querydsl.applyPagination(pageable, createQuery(predicate));

		Long total = countQuery.count();
		List<T> content = total > pageable.getOffset() ? query.list(path) : Collections.<T> emptyList();

		return new PageImpl<T>(content, pageable, total);
	}

	public long count(Predicate predicate) {
		return createQuery(predicate).count();
	}

	public JPQLQuery createQuery(Predicate... predicate) {
		return querydsl.createQuery(path).where(predicate);
	}

}
