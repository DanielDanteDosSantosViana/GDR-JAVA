package br.com.geradorRelatorioWeb.dto;

import java.io.Serializable;

public class ConfigDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String entidadesProperties;
	private String callBack;
	
	public String getEntidadesProperties() {
		return entidadesProperties;
	}
	public void setEntidadesProperties(String entidadesProperties) {
		this.entidadesProperties = entidadesProperties;
	}
	public String getCallBack() {
		return callBack;
	}
	public void setCallBack(String callBack) {
		this.callBack = callBack;
	}

}
