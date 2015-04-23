/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.domain.repository;

import com.fasterxml.classmate.GenericType;
import com.mysema.query.jpa.impl.JPAQuery;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author wlademir
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
public interface GumgaCrudAndQueryNotOnlyTypedRepository <T, ID extends Serializable> extends GumgaCrudRepository<T, ID>{
    
    /**
     * 
     * Query hql não tipada para consultas complexas. <br/>
     * Retorna apenas um registro. <br/>
     * Não passa pelo multi tenancy, caso seja necessario deve ser feito <br/>
     * manualmente no hql implementado.
     * @param <E>
     * @param hql
     * @param type
     * @param param
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <E> E getUniqueResult(String hql, Class<E> type, Map<String,Object> param);
    
    /**
     *
     * Query hql não tipada para consultas complexas. <br/>
     * Não passa pelo multi tenancy, caso seja necessario deve ser feito <br/>
     * manualmente no hql implementado.
     * @param <E>
     * @param hql
     * @param type
     * @param param
     * @return
     */
    public List getResultList(String hql, Map<String,Object> param);
    
    /**
     *
     * Query hql não tipada para consultas complexas. <br/>
     * Não passa pelo multi tenancy, caso seja necessario deve ser feito <br/>
     * manualmente no hql implementado.
     * @param <E>
     * @param hql
     * @param type
     * @param param
     * @param maxResult
     * @return
     * 
     */
    public List getResultList(String hql, Map<String,Object> param, int maxResult);
    
    /**
     *
      * Query hql não tipada para consultas complexas. <br/>
     * Não passa pelo multi tenancy, caso seja necessario deve ser feito <br/>
     * manualmente no hql implementado.
     * @param <E>
     * @param hql
     * @param type
     * @param param
     * @param firstresult
     * @param maxResult
     * @return 
     */
    public List getResultList(String hql, Map<String,Object> param,int firstresult, int maxResult);
    
   
    
    
    /**
     *
     * JPAQuery instanciado para consultas complexas.
     * @return
     */

    public JPAQuery getJPAQuerydsl();
    
    
}
