package gumga.framework.domain.seed;

import gumga.framework.domain.GumgaIdable;
import gumga.framework.domain.GumgaRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractSeed<T extends GumgaIdable> implements AppSeed {
	
	@Transactional
	public void loadSeed() throws IOException {
		URL url = Thread.currentThread().getContextClassLoader().getResource(pathFile());
		
		if (url == null)
			throw new FileNotFoundException();
		
		File file = new File(url.getFile());
		
		BufferedReader source = new BufferedReader(new FileReader(file));
		String line = null;
		int count = 0;

		while ((line = source.readLine()) != null) {
			String[] parts = line.split(";");
			
			repository().saveOrUpdate(createObject(parts));
			count++;
			
			if (count == 50) {
				repository().session().flush();
				repository().session().clear();
				count = 0;
			}
		}

		source.close();
	}
	
	public abstract GumgaRepository<T> repository();
	
	public abstract T createObject(String[] args);

	public abstract String pathFile();
	
}
