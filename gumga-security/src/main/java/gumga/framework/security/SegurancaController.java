package gumga.framework.security;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SegurancaController {

	@RequestMapping("/security")
	public String seguranca() {
		return "security/login";
	}
	
}
