/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.application;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

}
