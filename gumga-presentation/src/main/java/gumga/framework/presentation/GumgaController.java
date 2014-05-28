package gumga.framework.presentation;


import gumga.framework.core.service.GumgaSecurityService;
import gumga.framework.presentation.menu.GumgaMenuService;
import gumga.framework.presentation.menu.Menu;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public abstract class GumgaController {
	
	protected Logger logger = Logger.getLogger(getClass().getSimpleName());
	
	@Autowired(required = false)
	private GumgaMenuService menuService;
	
	@Autowired(required = false)
	private GumgaSecurityService securityService;
	
	public abstract String path();
	
	@RequestMapping
	public String index() {
		return path() + "/base";
	}

	@RequestMapping("/grid")
	public String grid(Model model) {
		return path() + "/grid";
	}

	@RequestMapping("/form")
	public String form(Model model) {
		return path() + "/form";
	}
	
	@ModelAttribute("menu")
	public Menu loadMenu() throws IOException {
		if (menuService == null) {
			logger.warning("No implementation for GumgaMenuService found. Menu will NOT be loaded.");
			return null;
		}
		
		return menuService.getMenu();
	}
	
	@ModelAttribute("username")
	public String username() throws IOException {
		if (securityService == null) {
			logger.warning("No implementation for GumgaSecurityService found. Username will NOT be loaded.");
			return null;
		}
		
		return securityService.getUsername();
	}
	
}
