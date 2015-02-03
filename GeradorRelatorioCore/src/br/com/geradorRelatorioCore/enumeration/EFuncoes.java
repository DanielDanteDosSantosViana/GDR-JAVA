package br.com.geradorRelatorioCore.enumeration;


public enum EFuncoes {
	
	SUM("Somar todos os valores dos registros de", "sum(#)"),
	MIN("Menor valor dos registros de", "min(#)"),
	MAX("Maior valor dos registros de", "max(#)");
	
	private final String  label;
	private final String funcao;
	
	private EFuncoes(String label, String funcao) {
		this.label = label;
		this.funcao = funcao;
	}

	public String getLabel() {
		return label;
	}

	public String getFuncao() {
		return funcao;
	} 
	
	public String buildFuncao(String campo){
		return this.funcao.replace("#", campo);
	}
}
