package br.com.geradorRelatorioCore.enumeration;

public enum EContextParam {
	
	ENTIDADES("br.com.geradorRelatorios.entidades"), 
	CALL_BACK("br.com.geradorRelatorios.callBack"), 
	PATH_CONFIG("br.com.geradorRelatorios.config");
	
	private final String param;

	private EContextParam(String param) {
		this.param = param;
	}

	public String getParam() {
		return param;
	}
	
}
