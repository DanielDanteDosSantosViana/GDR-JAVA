package br.com.geradorRelatorioCore.enumeration;

public enum EOperador {
	
	IGUAL("=", "="),
	DIFERENTE("<>", "<>"),
	MAIOR_IGUAL(">=", ">="),
	MENOR_IGUAL("<=", "<="),
	CONTEM("LIKE", "contŽm");
	
	private final String simbolo;
	private final String label;

	private EOperador(String simbolo, String label) {
		this.simbolo = simbolo;
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public String getSimbolo() {
		return simbolo;
	}
	
}
