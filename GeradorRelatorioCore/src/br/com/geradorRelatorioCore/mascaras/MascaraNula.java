package br.com.geradorRelatorioCore.mascaras;

public class MascaraNula extends CampoMascara {

	public MascaraNula() {
		super("");
	}

	@Override
	public String getMascaraJS() {
		return null;
	}

	@Override
	public String processar(Object o) {
		return o != null ? o.toString() : "";
	}

}
