package br.com.geradorRelatorioWeb.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.geradorRelatorioCore.enumeration.EFuncoes;
import br.com.geradorRelatorioCore.enumeration.EOperadorAritmetico;
import br.com.geradorRelatorioCore.interfaces.ICampoDTO;
import br.com.geradorRelatorioCore.interfaces.ICampoViewDTO;
import br.com.geradorRelatorioCore.interfaces.IItemFormulaDTO;
import br.com.geradorRelatorioCore.mascaras.CampoMascara;
import br.com.geradorRelatorioWeb.util.Util;

public class CampoViewDTO implements Serializable, ICampoViewDTO{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean avancado;
	
	private ICampoDTO camposSelecionado;
	private String apelido;
	private CampoMascara mascara;
	
	private List<ItemFormulaDTO> formula;
	private List<ItemFormulaDTO> formulaDesfazer;
	
	private String msgErroFormulaConsultaAvancado;
	
	private ICampoDTO campoConsultaAvancada;
	private Double valorMutavelConsultaAvancado;
	private EOperadorAritmetico operadorCaonsultaAvancado;
	private boolean abrirParenteses;
	private boolean fecharParenteses;
	private EFuncoes funcaoConsultaAvancado;
	
	public CampoViewDTO() {	}
	
	public CampoViewDTO(ICampoDTO camposSelecionado, String apelido, CampoMascara mascara) {
		super();
		this.camposSelecionado = camposSelecionado;
		this.apelido = apelido;
		this.mascara = mascara;
	}

	public CampoViewDTO(boolean avancado) {
		this.avancado = avancado;

		if(this.avancado){
			this.formula = new ArrayList<ItemFormulaDTO>();
			this.formulaDesfazer = new ArrayList<ItemFormulaDTO>();
		}
	}
	
	@Override
	public ICampoDTO getCamposSelecionado() {
		return camposSelecionado;
	}
	public void setCamposSelecionado(ICampoDTO camposSelecionado) {
		this.camposSelecionado = camposSelecionado;
	}
	
	@Override
	public String getApelido() {
		return apelido;
	}
	public void setApelido(String apelido) {
		this.apelido = apelido;
	}
	
	@Override
	public CampoMascara getMascara() {
		return mascara;
	}
	public void setMascara(CampoMascara mascara) {
		this.mascara = mascara;
	}
	
	@Override
	public boolean isAvancado() {
		return avancado;
	}
	
	public void setAvancado(boolean avancado) {
		this.avancado = avancado;
	}

	public List<ItemFormulaDTO> getFormula() {
		return formula;
	}

	public void setFormula(List<ItemFormulaDTO> formula) {
		this.formula = formula;
	}

	public String getMsgErroFormulaConsultaAvancado() {
		return msgErroFormulaConsultaAvancado;
	}

	public void setMsgErroFormulaConsultaAvancado(
			String msgErroFormulaConsultaAvancado) {
		this.msgErroFormulaConsultaAvancado = msgErroFormulaConsultaAvancado;
	}

	public List<ItemFormulaDTO> getFormulaDesfazer() {
		return formulaDesfazer;
	}

	public void setFormulaDesfazer(List<ItemFormulaDTO> formulaDesfazer) {
		this.formulaDesfazer = formulaDesfazer;
	}
	
	public ICampoDTO getCampoConsultaAvancada() {
		return campoConsultaAvancada;
	}

	public void setCampoConsultaAvancada(ICampoDTO campoConsultaAvancada) {
		this.campoConsultaAvancada = campoConsultaAvancada;
	}

	public Double getValorMutavelConsultaAvancado() {
		return valorMutavelConsultaAvancado;
	}

	public void setValorMutavelConsultaAvancado(Double valorMutavelConsultaAvancado) {
		if(valorMutavelConsultaAvancado != null && valorMutavelConsultaAvancado == 0){
			this.valorMutavelConsultaAvancado = null;
		}
		else{
			this.valorMutavelConsultaAvancado = valorMutavelConsultaAvancado;
		}
	}

	public EOperadorAritmetico getOperadorCaonsultaAvancado() {
		return operadorCaonsultaAvancado;
	}

	public void setOperadorCaonsultaAvancado(EOperadorAritmetico operadorCaonsultaAvancado) {
		this.operadorCaonsultaAvancado = operadorCaonsultaAvancado;
	}
	
	public EFuncoes getFuncaoConsultaAvancado() {
		return funcaoConsultaAvancado;
	}

	public void setFuncaoConsultaAvancado(EFuncoes funcaoConsultaAvancado) {
		this.funcaoConsultaAvancado = funcaoConsultaAvancado;
	}

	public boolean isAbrirParenteses() {
		return abrirParenteses;
	}

	public void setAbrirParenteses(boolean abrirParenteses) {
		this.abrirParenteses = abrirParenteses;
	}

	public boolean isFecharParenteses() {
		return fecharParenteses;
	}

	public void setFecharParenteses(boolean fecharParenteses) {
		this.fecharParenteses = fecharParenteses;
	}
	
	@Override
	public List<IItemFormulaDTO> getFormulaFinal() {
		return Util.castList(formula, IItemFormulaDTO.class);
	}
}
