package gumga.framework.domain.service;

/**
 * Service com a operação de delete
 */
public interface GumgaDeletableServiceable<T> {

    public void delete(T resource);

}
