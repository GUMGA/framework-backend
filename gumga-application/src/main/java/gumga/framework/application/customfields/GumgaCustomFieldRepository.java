package gumga.framework.application.customfields;

import gumga.framework.domain.customfields.GumgaCustomField;
import gumga.framework.domain.repository.GumgaCrudRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface GumgaCustomFieldRepository extends GumgaCrudRepository<GumgaCustomField, Long> {

    //@Query("from GumgaCustomField obj where obj.clazz =(?1) and obj.oi.value like (?2)")
    List<GumgaCustomField> findByClazz(String clazz);

}
