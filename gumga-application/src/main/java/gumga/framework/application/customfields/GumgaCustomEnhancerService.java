package gumga.framework.application.customfields;

import gumga.framework.domain.GumgaModel;
import gumga.framework.domain.customfields.GumgaCustomField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.beans.BeanMap;
import net.sf.cglib.proxy.Enhancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author munif
 */
@Service
public class GumgaCustomEnhancerService {
    
    @Autowired
    private GumgaCustomFieldService customFieldService;
    
    @Autowired
    private GumgaCustomFieldValueService customFieldValueService;
    
    public Map loadCustomFields(GumgaModel obj){
        Map toReturn=new HashMap();
        toReturn.putAll(BeanMap.create(obj));
        List<GumgaCustomField> customFields = customFieldService.findByClass(obj.getClass());
        for (GumgaCustomField cf:customFields){
            Object value=customFieldValueService.getValue(cf,obj);
            toReturn.put(cf.getName(), value);
        }
        return toReturn;
    }
    
    
    
}
