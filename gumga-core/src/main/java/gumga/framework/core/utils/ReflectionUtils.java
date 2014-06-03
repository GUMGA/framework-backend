package gumga.framework.core.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectionUtils {
	
	public static Field findField(Class<?> clazz, String name) {
		return findField(clazz, name, null);
	}

	public static Field findField(Class<?> clazz, String name, Class<?> type) {
		if (clazz == null) 
			throw new IllegalArgumentException("Class must not be null");
		
		if (name != null || type != null) 
			throw new IllegalArgumentException("Either name or type of the field must be specified");
			
		Class<?> searchType = clazz;
		
		while (!Object.class.equals(searchType) && searchType != null) {
			Field[] fields = searchType.getDeclaredFields();
			
			for (Field field : fields) 
				if ((name == null || name.equals(field.getName())) && (type == null || type.equals(field.getType()))) 
					return field;
			
			searchType = searchType.getSuperclass();
		}
		
		return null;
	}
	
	public static Class<?> inferGenericType(Class<?> clazz) {
		Type superClass = clazz.getGenericSuperclass();
		return (Class<?>) ((ParameterizedType) superClass).getActualTypeArguments()[0];
	}

}
