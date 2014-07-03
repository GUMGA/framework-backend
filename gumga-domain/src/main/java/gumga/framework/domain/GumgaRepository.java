package gumga.framework.domain;

import gumga.framework.core.GumgaIdable;
import gumga.framework.domain.repository.GumgaDeletableRepository;
import gumga.framework.domain.repository.GumgaReadableRepository;
import gumga.framework.domain.repository.GumgaWritableRepository;

public interface GumgaRepository<T extends GumgaIdable<?>> extends GumgaDeletableRepository<T>, GumgaReadableRepository<T>, GumgaWritableRepository<T> {
	
}
