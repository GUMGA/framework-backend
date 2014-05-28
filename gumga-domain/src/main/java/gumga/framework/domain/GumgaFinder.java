package gumga.framework.domain;

import java.util.List;

public interface GumgaFinder<T extends GumgaIdable> {
	
	T first();
	
	T last();
	
	T find(Long id);
	
	List<T> all();
	
	List<T> find(Long... id);
	
	List<T> limit(Long limit);
	
	List<T> offset(Long limit, Long offset);
	
}
