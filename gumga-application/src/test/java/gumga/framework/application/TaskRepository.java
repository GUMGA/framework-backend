/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.application;

import gumga.framework.domain.domains.GumgaOi;
import gumga.framework.domain.repository.GumgaCrudAndQueryNotOnlyTypedRepository;
import gumga.framework.domain.repository.GumgaCrudRepository;
import java.io.Serializable;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author wlademir
 */
public interface TaskRepository extends GumgaCrudAndQueryNotOnlyTypedRepository<Task, Long>{
    
    @Query("select obj from Task obj where obj.name like(?1) and obj.oi in(?2)")
    public List<Task>getTaskDescriptionAndOi(String description, List<GumgaOi>ois);
    
}
