package gumga.framework.application.sharedmodel;

import gumga.framework.application.Bus;
import gumga.framework.application.BusRepository;
import gumga.framework.application.BusService;
import gumga.framework.application.Car;
import gumga.framework.application.CarRepository;
import gumga.framework.application.Company;
import gumga.framework.application.CompanyService;
import gumga.framework.application.SpringConfig;
import gumga.framework.core.GumgaThreadScope;
import gumga.framework.core.QueryObject; 
import gumga.framework.core.SearchResult;
import gumga.framework.domain.customfields.CustomFieldType;
import gumga.framework.domain.customfields.GumgaCustomField;
import gumga.framework.domain.customfields.GumgaCustomFieldValue;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class SharedModelTest {

    @Autowired
    private BusService busService;

    @Autowired
    private BusRepository busRepository;

    public SharedModelTest() {
        GumgaThreadScope.organizationCode.set("1.");
        GumgaThreadScope.login.set("munif@gumga.com.br");
    }

    @Test
    public void injectionSanityCheck() {
        assertNotNull(busService);
        assertNotNull(busRepository);
    }

    @Before
    public void insertData() {
        if (busRepository.findAll().size() > 0) {
            return;
        }

        Bus linha103 = new Bus("103");
        linha103.addOrganization("3.1.");
        linha103.addUser("munif@gumga.com.br");
        Bus linha104 = new Bus("104");
        linha104.addOrganization("2.1.");
        linha104.addOrganization("5.");
        busRepository.save(linha103);
        busRepository.save(linha104);
    }

    @Test
    @Transactional
    public void listBusQ() {
        GumgaThreadScope.organizationCode.set("2.1.");
        QueryObject qo = new QueryObject();
        SearchResult<Bus> pesquisa = busService.pesquisa(qo);
        assertEquals(2l, pesquisa.getCount().longValue());
    }

    @Test
    @Transactional
    public void listBusAQ() {
        GumgaThreadScope.organizationCode.set("2.1.");

        QueryObject qo = new QueryObject();
        qo.setAq("obj.line like '%'");
        SearchResult<Bus> pesquisa = busService.pesquisa(qo);
        assertEquals(2l, pesquisa.getCount().longValue());
    }

}
