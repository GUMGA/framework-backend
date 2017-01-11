package gumga.framework.application;

import gumga.framework.core.GumgaThreadScope;
import gumga.framework.core.QueryObject;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.GumgaUserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GumgaUserDataService extends GumgaService<GumgaUserData, Long> {

    private GumgaUserDataRepository repository;

    @Autowired
    public GumgaUserDataService(GumgaUserDataRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public void beforeSave(GumgaUserData entity) {
        entity.setUserLogin(GumgaThreadScope.login.get());
    }

    /**
     * Encontrar GumgaUserData baseado no atributo key da classe @{@link GumgaUserData}
     * @param prefix
     * @return
     */
    public SearchResult<GumgaUserData> searchByKeyPrefix(String prefix) {
        QueryObject qo = new QueryObject();
        qo.setAq(String.format("obj.userLogin='%s' and obj.key like '%s%%'", GumgaThreadScope.login.get(), prefix));
        return super.pesquisa(qo);
    }

    /**
     * Encontrar GumgaUserData baseado no atributo userLogin e key da classe @{@link GumgaUserData}
     * @param userLogin
     * @param key
     * @return
     */
    public GumgaUserData findByUserLoginAndKey(String userLogin, String key) {
        return repository.findByUserLoginAndKey(userLogin,key);
    }

}
