package gumga.framework.presentation.api;

import gumga.framework.domain.GumgaModel;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metadata/")
public class MetadataAPI {

    @RequestMapping("describe/{classe}")
    public GumgaEntityMetadata describe(@PathVariable String classe) {
        try {
            Class clazz = Class.forName("br.com.empresa.piloto2.domain.model." + classe);
            GumgaEntityMetadata gem = new GumgaEntityMetadata(clazz);
            return gem;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MetadataAPI.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new RuntimeException("Entity not present");
    }

    class GumgaAtributeMetadata {

        private String name;
        private String javaType;
        private String type;

        public GumgaAtributeMetadata(String name, String javaType) {
            this.name = name;
            this.javaType = javaType;
            this.type = javaScriptType(javaType);
        }

        public String getName() {
            return name;
        }

        public String getJavaType() {
            return javaType;
        }

        public String getType() {
            return type;
        }

        private String javaScriptType(String javaType) {
            switch (javaType) {
                case "String":
                    return "TEXT";
                case "BigDecimal":
                case "Integer":
                case "int":
                case "Float":
                case "float":
                case "Double":
                case "double":
                case "Long":
                case "long":
                    return "NUMBER";
                case "boolean":
                case "Boolean":
                    return "BOOLEAN";
                default:
                    return javaType;
            }
        }

    }

    class GumgaEntityMetadata {

        private String name;
        private String pack;
        private List<GumgaAtributeMetadata> atributes;

        public GumgaEntityMetadata(Class clazz) {
            name = clazz.getSimpleName();
            pack = clazz.getCanonicalName().substring(0, clazz.getCanonicalName().lastIndexOf('.'));
            atributes = new ArrayList<>();
            List<Class> classes = new ArrayList<>();
            while (!(clazz.equals(Object.class) || clazz.equals(GumgaModel.class))) {
                classes.add(0, clazz);
                clazz = clazz.getSuperclass();
            }
            for (Class c : classes) {
                for (Field f : c.getDeclaredFields()) {
                    atributes.add(new GumgaAtributeMetadata(f.getName(), f.getType().getSimpleName().toString()));
                }
            }
        }

        public List<GumgaAtributeMetadata> getAtributes() {
            return atributes;
        }

        public String getName() {
            return name;
        }

        public String getPack() {
            return pack;
        }

    }

}
