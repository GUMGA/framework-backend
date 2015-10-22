package gumga.framework.application.customfields;

import gumga.framework.domain.customfields.GumgaCustomField;
import gumga.framework.domain.repository.GumgaCrudRepository;
import java.util.List;

public interface GumgaCustomFieldRepository extends GumgaCrudRepository<GumgaCustomField, Long> {

    List<GumgaCustomField> findByClazz(String clazz);

}
