package gumga.framework.presentation.api;

import com.wordnik.swagger.annotations.ApiOperation;
import gumga.framework.application.GumgaUserDataService;
import gumga.framework.core.GumgaThreadScope;
import gumga.framework.core.QueryObject;
import gumga.framework.core.QueryToSave;
import gumga.framework.core.SearchResult;
import gumga.framework.domain.GumgaObjectAndRevision;
import gumga.framework.domain.GumgaServiceable;
import gumga.framework.domain.GumgaUserData;
import gumga.framework.domain.service.GumgaReadableServiceable;
import java.util.List;
import javax.validation.Valid;
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

    public AbstractReadOnlyGumgaAPI(GumgaReadableServiceable<T> service) {
        this.service = service;
    }

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
        GumgaUserData gud = new GumgaUserData();
        gud.setUserLogin(GumgaThreadScope.login.get());
        gud.setKey("aq;" + qts.getPage());
        gud.setDescription(qts.getName());
        gud.setValue(qts.getData());
        guds.save(gud);
        return "OK";
    }

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

}
