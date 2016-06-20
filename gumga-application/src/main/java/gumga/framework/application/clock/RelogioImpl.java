package gumga.framework.application.clock;

import gumga.framework.core.Relogio;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class RelogioImpl implements Relogio {

	@Override
	public Date now() {
		return new Date();
	}
	
}
