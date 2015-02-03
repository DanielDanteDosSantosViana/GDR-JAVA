package br.com.geradorRelatorio.dto;

import java.lang.reflect.Field;

import br.com.geradorRelatorio.interfaces.ICreatAlias;
import br.com.geradorRelatorio.util.Util;
import br.com.geradorRelatorioCore.annotations.CampoGR;
import br.com.geradorRelatorioCore.exception.EntidadeConsumerException;
import br.com.geradorRelatorioCore.interfaces.ICampoDTO;
import br.com.geradorRelatorioCore.interfaces.IEntidadeDTO;
import br.com.geradorRelatorioCore.mascaras.CampoMascara;

public class CampoDTO implements ICampoDTO, ICreatAlias{
	
	private final Field field;
	private final boolean relacionamento;
	private final CampoGR campoGR;
	private final IEntidadeDTO entidade;
	private final CampoMascara mascara;
	
	public CampoDTO(Field field, boolean relacionamento, IEntidadeDTO entidade) throws EntidadeConsumerException {
		this.field = field;
		this.relacionamento = relacionamento;
		this.campoGR = this.field.getAnnotation(CampoGR.class);
		this.entidade = entidade;
		this.mascara = Util.getMask(this.campoGR, field);
	}
	
	@Override
	public Field getField() {
		return field;
	}
	
	@Override
	public boolean isRelacionamento() {
		return relacionamento;
	}

	public CampoGR getCampoGR() {
		return campoGR;
	}
	
	@Override
	public String getLabel(){
		return getEntidadeDTO().getLabel() + "." + this.campoGR.label();
	}

	@Override
	public CampoMascara getMascara() {
		return mascara;
	}

	public String getKeyAlias() {
		return getEntidadeDTO().getKeyAlias();
	}
	
	public String generateAlias(){
		return getEntidadeDTO().generateAlias();
	}

	private EntidadeDTO getEntidadeDTO() {
		return (EntidadeDTO)this.entidade;
	}
	
	public String getNomeCampo(){
		return this.field.getName();
	}
	
	@Override
	public String getLabelSimples(){
		return this.campoGR.label();
	}
	
	public void resetContatorAlias(){
		getEntidadeDTO().resetContatorAlias();
	}
}
