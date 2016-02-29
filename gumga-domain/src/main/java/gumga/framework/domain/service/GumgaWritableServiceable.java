package gumga.framework.domain.service;

/**
 * Service com a operação salvar
 */
public interface GumgaWritableServiceable<T> extends GumgaReadableServiceable<T> {

    public T save(T resource);

}
