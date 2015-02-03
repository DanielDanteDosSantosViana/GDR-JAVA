package br.com.geradorRelatorioCore.enumeration;

public enum EOperadorLogico {
	
	AND("E"),
	OR("OU");
	
	private final String label;

	private EOperadorLogico(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
	
}
