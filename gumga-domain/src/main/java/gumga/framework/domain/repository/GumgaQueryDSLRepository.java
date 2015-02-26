package gumga.framework.domain.repository;

import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import gumga.framework.core.SearchResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface GumgaQueryDSLRepository<T, ID extends Serializable> extends GumgaCrudRepository<T, ID>, QueryDslPredicateExecutor<T> {
	
	List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders);

    <A> List<A> findAll(ISpecification specification, Expression<A> projection);

    List<T> findAll(ISpecification specification);

    SearchResult<T> search(Predicate predicate, Pageable page);

    <A> SearchResult<A> search(Predicate predicate, Pageable page, Expression<A> projection);

    <A> A findOne(ISpecification query, Expression<A> projection);

}
