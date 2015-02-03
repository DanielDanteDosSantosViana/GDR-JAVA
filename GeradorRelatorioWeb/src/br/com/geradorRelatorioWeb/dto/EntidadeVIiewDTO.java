package br.com.geradorRelatorioWeb.dto;

import java.io.Serializable;
import java.util.List;

import br.com.geradorRelatorioCore.interfaces.IEntidadeDTO;
import br.com.geradorRelatorioCore.interfaces.IEntidadeVIiewDTO;
import br.com.geradorRelatorioCore.interfaces.ISelectItemWrapper;

public class EntidadeVIiewDTO implements Serializable, IEntidadeVIiewDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private IEntidadeDTO entidadeSelecionada;
	
	private List<ISelectItemWrapper> entidades;
	
	@Override
	public IEntidadeDTO getEntidadeSelecionada() {
		return entidadeSelecionada;
	}
	
	public void setEntidadeSelecionada(IEntidadeDTO entidadeSelecionada) {
		this.entidadeSelecionada = entidadeSelecionada;
	}
	public List<ISelectItemWrapper> getEntidades() {
		return entidades;
	}
	public void setEntidades(List<ISelectItemWrapper> entidades) {
		this.entidades = entidades;
	}
	
	
}
