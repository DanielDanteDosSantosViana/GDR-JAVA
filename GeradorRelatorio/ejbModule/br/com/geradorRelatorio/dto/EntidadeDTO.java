package br.com.geradorRelatorio.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import br.com.geradorRelatorio.interfaces.ICreatAlias;
import br.com.geradorRelatorioCore.annotations.EntidadeGR;
import br.com.geradorRelatorioCore.interfaces.ICampoDTO;
import br.com.geradorRelatorioCore.interfaces.IEntidadeDTO;
import br.com.geradorRelatorioCore.interfaces.ISelectItemWrapper;

public class EntidadeDTO implements IEntidadeDTO, ICreatAlias{
	
	private final Class<?> clazz;
	private final EntidadeGR entidadeGr;
	private final Entity entity;
	private final List<ICampoDTO> campos;
	private final List<IEntidadeDTO> relacionamentos;
	
	private List<ISelectItemWrapper> relacionamentosView;
	private List<ISelectItemWrapper> camposView;
	private List<ISelectItemWrapper> camposNumericosView;
	
	private IEntidadeDTO entidadeOrigem;
	private CampoDTO campoRelacionamento;
	
	private Integer tentativasGeracaoAlias = 0;
	
	public EntidadeDTO(Class<?> clazz) {
		this.clazz = clazz;
		
		this.entidadeGr = clazz.getAnnotation(EntidadeGR.class);
		this.entity = clazz.getAnnotation(Entity.class);
		this.campos = new ArrayList<ICampoDTO>();
		this.relacionamentos = new ArrayList<IEntidadeDTO>();
	}
	
	@Override
	public Class<?> getClazz() {
		return clazz;
	}
	
	public EntidadeGR getEntidadeGr() {
		return entidadeGr;
	}

	public Entity getEntity() {
		return entity;
	}
	
	@Override
	public List<ICampoDTO> getCampos() {
		return campos;
	}
	
	@Override
	public List<IEntidadeDTO> getRelacionamentos() {
		return relacionamentos;
	}

	@Override
	public boolean hasRelacionamento() {
		return !relacionamentos.isEmpty();
	}

	@Override
	public IEntidadeDTO getEntidadeOrigem() {
		return entidadeOrigem;
	}

	public void setEntidadeOrigem(IEntidadeDTO entidadeOrigem) {
		this.entidadeOrigem = entidadeOrigem;
	}
	
	@Override
	public List<ISelectItemWrapper> getRelacionamentosView() {
		return relacionamentosView;
	}
	
	@Override
	public boolean equals(Object obj) {
		return this.clazz.getName().equals(((EntidadeDTO)obj).clazz.getName()) &&  this.getLabel().equals(((EntidadeDTO)obj).getLabel());
	}
	
	@Override
	public void setRelacionamentosView(List<ISelectItemWrapper> relacionamentosView) {
		this.relacionamentosView = relacionamentosView;
	}
	
	@Override
	public List<ISelectItemWrapper> getCamposView() {
		return camposView;
	}
	
	@Override
	public void setCamposView(List<ISelectItemWrapper> camposView) {
		this.camposView = camposView;
	}
	
	@Override
	public List<ISelectItemWrapper> getCamposNumericosView() {
		return camposNumericosView;
	}
	
	@Override
	public void setCamposNumericosView(List<ISelectItemWrapper> camposNumericosView) {
		this.camposNumericosView = camposNumericosView;
	}
	
	@Override
	public String getLabel() {
		return isRelacionamento() ?  getCampoRelacionamento().getLabelSimples() : getEntidadeGr().label();
	}
	
	private boolean isRelacionamento() {
		return this.entidadeOrigem != null;
	}

	private String getFullPathEnityClass(){
		return getClazz().getName();
	}
	
	public String getKeyAlias(){
		return getFullPathEnityClass() + "." + getLabel();
	}

	public String generateAlias( ) {
		StringBuilder alias = new StringBuilder(getClazz().getSimpleName());
		
		if(tentativasGeracaoAlias > 0){
			alias.append("_");
			alias.append(tentativasGeracaoAlias);
		}
		
		tentativasGeracaoAlias++;
		
		return alias.toString().toLowerCase();
	}
	
	public String getEntidade(){
		return isRelacionamento() ? campoRelacionamento.getNomeCampo() : getClazz().getSimpleName();
	}
	
	public void resetContatorAlias(){
		tentativasGeracaoAlias = 0;
	}

	public CampoDTO getCampoRelacionamento() {
		return campoRelacionamento;
	}

	public void setCampoRelacionamento(CampoDTO campoRelacionamento) {
		this.campoRelacionamento = campoRelacionamento;
	}
	
}
