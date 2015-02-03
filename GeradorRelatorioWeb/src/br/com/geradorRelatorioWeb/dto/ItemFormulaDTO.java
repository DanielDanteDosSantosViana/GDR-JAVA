package br.com.geradorRelatorioWeb.dto;

import java.io.Serializable;

import br.com.geradorRelatorioCore.enumeration.EFuncoes;
import br.com.geradorRelatorioCore.enumeration.EOperador;
import br.com.geradorRelatorioCore.enumeration.EOperadorAritmetico;
import br.com.geradorRelatorioCore.enumeration.EOperadorLogico;
import br.com.geradorRelatorioCore.enumeration.EParenteses;
import br.com.geradorRelatorioCore.interfaces.ICampoDTO;
import br.com.geradorRelatorioCore.interfaces.IItemFormulaDTO;


public class ItemFormulaDTO implements Serializable, IItemFormulaDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Object valor;
	private boolean valorLivre;

	public ItemFormulaDTO(Object valor) {
		this.valor = valor;
	}
	
	public ItemFormulaDTO(Object valor, boolean valorLivre) {
		this.valor = valor;
		this.valorLivre = valorLivre;
	}

	@Override
	public Object getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
	
	@Override
	public String toString() {
		if(valor != null ){
			if(valor instanceof ICampoDTO){
				return addEspaco(((ICampoDTO)valor).getLabel());
			}
			else if(valor instanceof EOperadorAritmetico){
				return addEspaco(((EOperadorAritmetico)valor).getSimbolo());
			}
			else if(valor instanceof EParenteses){
				return addEspaco(((EParenteses)valor).getSimbolo());
			}
			else if(valor instanceof EFuncoes){
				return addEspaco(((EFuncoes)valor).getLabel());
			}
			else if(valor instanceof EOperadorLogico){
				return addEspaco(((EOperadorLogico)valor).getLabel());
			}
			else if(valor instanceof EOperador){
				return addEspaco(((EOperador)valor).getLabel());
			}
			else{
				return addEspaco(valor.toString());
			}
		}
		
		return null;
	}
	
	private String addEspaco(String str){
		StringBuilder sb = new StringBuilder(str);
		sb.append(" ");
		
		return sb.toString();
	}
	
	@Override
	public boolean isValorLivre() {
		return valorLivre;
	}

	public void setValorLivre(boolean valorLivre) {
		this.valorLivre = valorLivre;
	}
	
}
