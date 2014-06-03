package gumga.framework.domain;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringConfig.class })
public class GumgaFinderTest {
	
	@Autowired
	private GumgaRepository<Car> carRepository;
	
	@Autowired
	private GumgaService<Company> service;
	
	@Test
	public void testFeliz() {
		assertNotNull(carRepository);
		assertNotNull(service);
	}
	
}
