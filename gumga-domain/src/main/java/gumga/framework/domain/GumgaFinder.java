package gumga.framework.domain;

import gumga.framework.core.GumgaIdable;
import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;

import com.mysema.query.jpa.hibernate.HibernateQuery;

public interface GumgaFinder<T extends GumgaIdable> extends Inferable<T> {
	
	T first();

	T last();

	T find(Long id);

	List<T> all();

	List<T> find(Long... id);

	List<T> limit(int limit);

	List<T> offset(int limit, int offset);
	
	SearchResult<T> pesquisa(QueryObject query);
	
	Pesquisa<T> pesquisa();
	
	Query createQuery(String query);
	
	SQLQuery createSQLQuery(String query);
	
	HibernateQuery queryDSL();
	
	Long count();

}
