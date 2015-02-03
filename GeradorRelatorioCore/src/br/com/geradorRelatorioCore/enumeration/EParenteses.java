package br.com.geradorRelatorioCore.enumeration;

public enum EParenteses {
	
	ABRIR("("),
	FECHAR(")");
	
	private final String simbolo;

	private EParenteses(String simbolo) {
		this.simbolo = simbolo;
	}

	public String getSimbolo() {
		return simbolo;
	}
	
	
}
