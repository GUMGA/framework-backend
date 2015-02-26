package gumga.framework.domain.repository;

import com.mysema.query.jpa.JPQLQuery;

/**
 * Interface para consultas mais complexas
 */
public interface ISpecification {

    JPQLQuery createQuery(JPQLQuery query);

}
