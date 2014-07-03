package gumga.framework.domain;

import gumga.framework.domain.service.GumgaDeletableServiceable;
import gumga.framework.domain.service.GumgaReadableServiceable;
import gumga.framework.domain.service.GumgaWritableServiceable;

public interface GumgaServiceable<T> extends GumgaReadableServiceable<T>, GumgaWritableServiceable<T>, GumgaDeletableServiceable<T> {
	
}
