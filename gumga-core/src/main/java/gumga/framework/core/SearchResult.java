package gumga.framework.core;

import java.util.List;

public class SearchResult<T> {

	private final int pageSize;
	private final Long count;
	private final int start;
	private final List<T> values;
	
	public SearchResult(QueryObject query, Long count, List<T> data) {
		this.pageSize = query.getPageSize();
		this.count = count;
		this.start = query.getStart();
		this.values = data;
	}

	public int getPageSize() {
		return pageSize;
	}

	public Long getCount() {
		return count;
	}

	public int getStart() {
		return start;
	}

	public List<T> getValues() {
		return values;
	}

}
