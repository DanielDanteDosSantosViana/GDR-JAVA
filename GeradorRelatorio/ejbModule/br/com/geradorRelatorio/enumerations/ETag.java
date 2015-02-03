package br.com.geradorRelatorio.enumerations;

public enum ETag {
	
	HTML("html"),
	TABLE("table"),
	TR("tr"),
	TD("td"),
	BODY("body");
	
	private final String tag;

	private ETag(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	
	
}
