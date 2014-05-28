package gumga.framework.core;


public class QueryObject {

	private String q = "";
	private int start = 0;
	private int pageSize = 10;
	private String sortField = "";
	private String sortDir = "asc";
	private String[] searchFields;

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortDir() {
		return sortDir;
	}

	public void setSortDir(String sortDir) {
		this.sortDir = sortDir;
	}

	public String[] getSearchFields() {
		return searchFields;
	}

	public void setSearchFields(String searchFields) {
		this.searchFields = searchFields.split(",");
	}

	public void setSearchFields(String... fields) {
		this.searchFields = fields;
	}
	
	public int getCurrentPage() {
		if (start == 0) return 1;
		return start / pageSize + 1;
	}

}
