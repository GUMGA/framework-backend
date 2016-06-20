package gumga.framework.domain;

import java.io.Serializable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/**
 * Repositório padrão GUMGA
 *
 */
@NoRepositoryBean
public interface GumgaRepository<T, ID extends Serializable> extends Repository<T, ID> {

}
