package gumga.framework.presentation.api;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/metadata/")
public class MetadataAPI {

    @RequestMapping(value="describe/{classe}",method = RequestMethod.GET)
    public GumgaEntityMetadata describe(@PathVariable String classe) {
        try {
            Class clazz = Class.forName(classe);
            GumgaEntityMetadata gem = new GumgaEntityMetadata(clazz);
            return gem;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MetadataAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new RuntimeException("Entity not present");
    }

}
