package br.com.geradorRelatorioCore.enumeration;

public enum EOperadorAritmetico {
	
	MAIS("+"),
	MENOS("-"),
	VEZES("*"),
	DIVISAO("/");
	
	private final String simbolo;

	private EOperadorAritmetico(String simbolo) {
		this.simbolo = simbolo;
	}

	public String getSimbolo() {
		return simbolo;
	}
	
}
