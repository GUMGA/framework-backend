package gumga.framework.domain.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;

@NoRepositoryBean
public interface GumgaQueryDSLRepository<T, ID extends Serializable> extends GumgaCrudRepository<T, ID>, QueryDslPredicateExecutor<T> {
	
	List<T> findAll(Predicate predicate);
	
	List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders);

}
