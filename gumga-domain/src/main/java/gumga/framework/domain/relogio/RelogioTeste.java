package gumga.framework.domain.relogio;

import gumga.framework.domain.config.Dev;

import java.util.Date;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Dev 
@Primary
@Component 
public class RelogioTeste extends RelogioImpl {

	private Date date;
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public Date now() {
		return date == null ? super.now() : date;
	}

}
