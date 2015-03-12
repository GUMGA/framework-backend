/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.application;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
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

    public List<Object> fullTextSearch(String text, Class entidade, String... atributos) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(entidade).get();
        Query query = qb.keyword().onFields(atributos).matching(text).createQuery();
        return fullTextEntityManager.createFullTextQuery(query, entidade).getResultList();
    }

}
