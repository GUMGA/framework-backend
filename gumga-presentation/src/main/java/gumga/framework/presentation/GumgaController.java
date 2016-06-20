package gumga.framework.presentation;


import gumga.framework.core.service.GumgaSecurityService;
import gumga.framework.presentation.menu.GumgaMenuService;
import gumga.framework.presentation.menu.Menu;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;


/**
 * 
 * A classe GumgaController representa uma unidade básica para ser utilizada
 * como controller web. Existem métodos auxiliares necessários para uma
 * página web via JSTL. Como por exemplo: menu e username.
 *
 */
@Controller
public abstract class GumgaController {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired(required = false)
	private GumgaMenuService menuService;
	
	@Autowired(required = false)
	private GumgaSecurityService securityService;
	
	@ModelAttribute("menu")
	public Menu loadMenu() throws IOException {
		if (menuService == null) {
			logger.warn("No implementation for GumgaMenuService found. Menu will NOT be loaded.");
			return null;
		}
		
		return menuService.getMenu();
	}
	
	@ModelAttribute("username")
	public String username() throws IOException {
		if (securityService == null) {
			logger.warn("No implementation for GumgaSecurityService found. Username will NOT be loaded.");
			return null;
		}
		
		return securityService.getUsername();
	}
	
}
