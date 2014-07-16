package gumga.framework.domain.repository;

import java.io.Serializable;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GumgaQueryDSLRepository<T, ID extends Serializable> extends GumgaCrudRepository<T, ID>, QueryDslPredicateExecutor<T> {

}
