package gumga.framework.presentation;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

public abstract class GumgaCRUDController extends GumgaController {
	
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

}
