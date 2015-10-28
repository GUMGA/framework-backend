package gumga.framework.application.customfields;

import gumga.framework.domain.customfields.GumgaCustomField;
import gumga.framework.domain.customfields.GumgaCustomFieldValue;
import gumga.framework.domain.repository.GumgaCrudRepository;

public interface GumgaCustomFieldValueRepository extends GumgaCrudRepository<GumgaCustomFieldValue, Long> {

    GumgaCustomFieldValue findByFieldAndGumgaModelId(GumgaCustomField field, Long gumgaModelId);

}
