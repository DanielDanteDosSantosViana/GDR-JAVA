package br.com.geradorRelatorio.dto;

import java.io.Serializable;

import br.com.geradorRelatorioCore.interfaces.IRelatorioDTO;

public class RelatorioDTO implements Serializable, IRelatorioDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final byte[] arquivo;
	private final String extensao;
	
	
	public RelatorioDTO(byte[] arquivo, String extensao) {
		this.arquivo = arquivo;
		this.extensao = extensao;
	}
	
	@Override
	public byte[] getArquivo() {
		return arquivo;
	}

	@Override
	public String getExtensao() {
		return extensao;
	}
	
	
	
}
