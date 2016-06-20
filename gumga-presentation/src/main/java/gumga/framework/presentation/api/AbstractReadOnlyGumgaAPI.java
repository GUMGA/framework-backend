package gumga.framework.presentation.api;

import com.wordnik.swagger.annotations.ApiOperation;
import gumga.framework.annotations.GumgaSwagger;
import gumga.framework.application.GumgaUserDataService;
import gumga.framework.application.tag.GumgaTagDefinitionService;
import gumga.framework.application.tag.GumgaTagService;
import gumga.framework.core.GumgaThreadScope;
import gumga.framework.core.QueryObject;
import gumga.framework.core.QueryToSave;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.GumgaObjectAndRevision;
import gumga.framework.domain.GumgaServiceable;
import gumga.framework.domain.GumgaUserData;
import gumga.framework.domain.service.GumgaReadableServiceable;
import gumga.framework.domain.tag.GumgaTag;
import gumga.framework.domain.tag.GumgaTagDefinition;
import java.util.List;
import javax.validation.Valid;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class AbstractReadOnlyGumgaAPI<T> extends AbstractProtoGumgaAPI<T> {

    protected GumgaReadableServiceable<T> service;
    @Autowired
    protected GumgaUserDataService guds;
    @Autowired
    protected GumgaTagDefinitionService gtds;
    @Autowired
    protected GumgaTagService gts;

    public AbstractReadOnlyGumgaAPI(GumgaReadableServiceable<T> service) {
        this.service = service;
    }

    @GumgaSwagger
    @Transactional
    @ApiOperation(value = "search", notes = "Faz uma pesquisa pela query informada através do objeto QueryObjet, os atributos são aq, q, start, pageSize, sortField, sortDir e searchFields.")
    @RequestMapping(method = RequestMethod.GET)
    public SearchResult<T> pesquisa(QueryObject query) {
        SearchResult<T> pesquisa = service.pesquisa(query);
        return new SearchResult<>(query, pesquisa.getCount(), pesquisa.getValues());
    }

    @Transactional
    @ApiOperation(value = "saveQuery", notes = "Salva a consulta avançada.")
    @RequestMapping(value = "saq", method = RequestMethod.POST)
    public String save(@RequestBody @Valid QueryToSave qts,
            BindingResult result) {
        String key = "aq;" + qts.getPage() + ";" + qts.getName();
        String userLogin = GumgaThreadScope.login.get();
        GumgaUserData gud = guds.findByUserLoginAndKey(userLogin, key);
        if (gud == null) {
            gud = new GumgaUserData();
            gud.setUserLogin(userLogin);
            gud.setKey(key);
            gud.setDescription(qts.getName());
        }
        gud.setValue(qts.getData());
        guds.save(gud);
        return "OK";
    }

    @GumgaSwagger
    @Transactional
    @ApiOperation(value = "load", notes = "Carrega entidade pelo id informado.")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public T load(@PathVariable Long id) {
        return service.view(id);
    }

    @Transactional
    @ApiOperation(value = "listOldVersions", notes = "Mostra versões anteriores do objeto.")
    @RequestMapping(value = "listoldversions/{id}", method = RequestMethod.GET)
    public List<GumgaObjectAndRevision> listOldVersions(@PathVariable Long id) {
        return service.listOldVersions(id);
    }

    public void setService(GumgaServiceable<T> service) {
        this.service = service;
    }

    @ApiOperation(value = "queryByKeyPrefix", notes = "Retorna os associados do usuário a uma chave.")
    @RequestMapping(value = "gumgauserdata/{prefix}", method = RequestMethod.GET)
    public SearchResult<GumgaUserData> queryByKeyPrefix(@PathVariable String prefix) {
        return ((GumgaUserDataService) guds).searchByKeyPrefix(prefix);

    }

    @Transactional
    @RequestMapping(method = RequestMethod.GET, value = "tags")
    public SearchResult<GumgaTagDefinition> listAllTags(QueryObject query) {
        SearchResult<GumgaTagDefinition> pesquisa = gtds.pesquisa(query);
        return new SearchResult<>(query, pesquisa.getCount(), pesquisa.getValues());
    }

    @Transactional
    @RequestMapping(method = RequestMethod.GET, value = "tags/{objectId}")
    public List<GumgaTag> listTagsOfEspecificObject(@PathVariable("objectId") Long objectId) {

        List<GumgaTag> tags = gts.findByObjectTypeAndObjectId(clazz().getCanonicalName(), objectId);
        for (GumgaTag tag : tags) {
            Hibernate.initialize(tag.getValues());
        }
        return tags;
    }

}
