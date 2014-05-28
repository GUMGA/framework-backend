package gumga.framework.presentation.io;

import gumga.framework.presentation.menu.Menu;
import gumga.framework.presentation.menu.MenuComponent;
import gumga.framework.presentation.menu.MenuGrupo;
import gumga.framework.presentation.menu.MenuItem;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuReader {
	
	private static Logger logger = Logger.getLogger(MenuReader.class.getCanonicalName());
	
	public static Menu loadMenu() throws IOException {
		return digesterMenu(loadMenuFile());
	}
	
	private static List<String> loadMenuFile() throws IOException {
		URL url = Thread.currentThread().getContextClassLoader().getResource("menu.config");
		
		if (url == null) {
			logger.warning("menu.config não encontrado, o menu não será carregado.");
			return Collections.emptyList();
		}
		
		String resource = url.getPath().replace("/C:/","C:/");
		return Files.readAllLines(Paths.get(resource), Charset.defaultCharset());
	}
	
	public static Menu digesterMenu(List<String> menuOptions) {
		Menu menu = new Menu();
		
        for (String menuOption : menuOptions) {
        	MenuComponent item = createMenuItem(menuOption);
            menu.addItem(item, item.getLevel());
        }
        
        return menu;
    }
	
	private static MenuComponent createMenuItem(String menuOption) {
        Map<String, String> properties = getMenuItemProperties(menuOption);
        
        String replaced = menuOption.replace("\t", "");
        int level = menuOption.length() - replaced.length();
        
    	String label = replaced.contains("{") ? replaced.substring(0, replaced.indexOf("{") - 1) : replaced;
        String url = properties.get("url");
        String id = properties.get("id");
        String clazz = properties.get("class");
        
        MenuComponent item = (url != null) ? new MenuItem(label, url) : new MenuGrupo(label);  
        
        item.setLevel(level);
        if (id != null) item.setId(id);
        if (clazz != null) item.setClazz(clazz);
        
        return item;
	}
	
	private static Map<String, String> getMenuItemProperties(String menuOption) {
		Pattern pattern = Pattern.compile("\\{(.*?)\\}");
		Matcher matchPattern = pattern.matcher(menuOption);

		Map<String, String> properties = new HashMap<String, String>();

        while (matchPattern.find()) {
            String propertiesInline = matchPattern.group(1).trim();
            String[] propertiesSplited = propertiesInline.split(" ");
            
            for (String property : propertiesSplited) {
            	String[] splited = property.split("=");
            	properties.put(splited[0], splited[1].replaceAll("\"", ""));
            }
        }
        
        return properties;
	}

}
