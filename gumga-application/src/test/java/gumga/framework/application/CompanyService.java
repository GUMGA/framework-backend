package gumga.framework.application;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService extends GumgaService<Company, Long> {

	@Autowired
	public CompanyService(CompanyRepository repository) {
		super(repository);
	}

}
