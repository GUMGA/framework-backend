package gumga.framework.core;

import static java.util.Collections.reverse;
import static java.util.Collections.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class SearchUtils {

	public static <E extends Enum<E>> SearchResult<E> filterEnum(Class<E> enumClass, QueryObject query) {
		List<E> filtered = new ArrayList<E>();
		
		for (E value : enumClass.getEnumConstants())
			if (StringUtils.startsWithIgnoreCase(value.name(), query.getQ()))
				filtered.add(value);
		
		sortEnum(query.getSortDir(), filtered);
		List<E> paginated = paginate(query.getStart(), query.getPageSize(), filtered);
		return new SearchResult<E>(query, filtered.size(), paginated);
	}

	private static <T> List<T> paginate(int start, int pageSize, List<T> filtered) {
		int paginationStart = Math.min(start, filtered.size());
		int paginationEnd = Math.min(start + pageSize, filtered.size());
		return filtered.subList(paginationStart, paginationEnd);
	}

	private static <E extends Enum<E>> void sortEnum(String direction, List<E> filtered) {
		if ("asc".equalsIgnoreCase(direction))
			sort(filtered, new EnumNameComparator<E>()); 
		else if ("desc".equalsIgnoreCase(direction)) {
			sort(filtered, new EnumNameComparator<E>()); 
			reverse(filtered);
		}
	}

	
	private static class EnumNameComparator<E extends Enum<E>> implements Comparator<E> {
		@Override public int compare(E o1, E o2) {
			return o1.name().compareTo(o2.name());
		}
	}
}
