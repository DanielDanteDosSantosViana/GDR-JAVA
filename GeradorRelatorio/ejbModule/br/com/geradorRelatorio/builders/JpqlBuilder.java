package br.com.geradorRelatorio.builders;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.geradorRelatorio.dto.CampoDTO;
import br.com.geradorRelatorio.dto.EntidadeDTO;
import br.com.geradorRelatorio.interfaces.ICreatAlias;
import br.com.geradorRelatorioCore.enumeration.EFuncoes;
import br.com.geradorRelatorioCore.enumeration.EOperador;
import br.com.geradorRelatorioCore.enumeration.EOperadorAritmetico;
import br.com.geradorRelatorioCore.enumeration.EOperadorLogico;
import br.com.geradorRelatorioCore.enumeration.EParenteses;
import br.com.geradorRelatorioCore.interfaces.ICampoDTO;
import br.com.geradorRelatorioCore.interfaces.ICampoViewDTO;
import br.com.geradorRelatorioCore.interfaces.IEntidadeDTO;
import br.com.geradorRelatorioCore.interfaces.IEntidadeVIiewDTO;
import br.com.geradorRelatorioCore.interfaces.IItemFormulaDTO;

public class JpqlBuilder {
	
	private final StringBuilder jpql;
	private final Map<String, String> mapAlis;
	
	private static final String ESPACO = " ";
	
	public JpqlBuilder() {
		this.jpql = new StringBuilder();
		this.mapAlis = new HashMap<String, String>();
	}
	
	public JpqlBuilder appendSelect(){
		return append("SELECT");
	}
	
	private JpqlBuilder append(String str) {
		this.jpql.append(str);
		this.jpql.append(ESPACO);
		return this;
	}

	public StringBuilder construir(){
		return this.jpql;
	}

	public JpqlBuilder appendCamposRetorno(List<ICampoViewDTO> camposConsulta) {
		
		Iterator<ICampoViewDTO> it = camposConsulta.iterator();
		
		while (it.hasNext()) {
			ICampoViewDTO campo = (ICampoViewDTO) it.next();
			
			if(!campo.isAvancado()){
				appendCampoSimples(campo);
			}
			else{
				appendCampoAvancado(campo);
			}
			
			appendVirgula(it);
		}
		
		
		return this;
	}

	private void appendCampoAvancado(ICampoViewDTO campo) {
		Iterator<IItemFormulaDTO> it = campo.getFormulaFinal().iterator();
		appendItemFormula(it);
	}

	private void appendItemFormula(Iterator<IItemFormulaDTO> it) {
		identar();
		
		while (it.hasNext()) {
			IItemFormulaDTO item = (IItemFormulaDTO) it.next();
			String itemStr = this.builderItemFormula(item);
			
			if(item.getValor() instanceof EFuncoes){
				item = (IItemFormulaDTO) it.next();
				itemStr = itemStr.replace("#", builderItemFormula(item));
			}
			else if(item.getValor() instanceof EOperadorLogico){
				novaLinha();
				identar();
			}
			
			this.append(itemStr);
			
		}
	}

	private String builderItemFormula(IItemFormulaDTO item) {
		Object valor = item.getValor();
		if(valor instanceof ICampoDTO){
			StringBuilder campoStr = new StringBuilder("");
			campoStr.append(creatAlias((CampoDTO) item.getValor()));
			campoStr.append(".");
			campoStr.append(((CampoDTO)valor).getNomeCampo());
			
			return campoStr.toString();
		}
		else if(valor instanceof EOperadorAritmetico){
			return  ((EOperadorAritmetico)valor).getSimbolo();
		}
		else if(valor instanceof EParenteses){
			return  ((EParenteses)valor).getSimbolo();
		}
		else if(valor instanceof EFuncoes){
			return ((EFuncoes)valor).getFuncao();
		}
		else if(valor instanceof EOperador){
			return  ((EOperador)valor).getSimbolo();
		}
		else if(valor instanceof EOperadorLogico){
			return valor.toString();
		}
		else{
			return "?";
		}
	}

	private void appendVirgula(Iterator<?> it) {
		if(it.hasNext()){
			this.append(",");
			novaLinha();
		}
	}

	private void appendCampoSimples(ICampoViewDTO campoView) {
		CampoDTO campo = (CampoDTO) campoView.getCamposSelecionado();
		identar();
		appendAlias(campo);
		appendCampo(campo);
	}

	private void appendCampo(CampoDTO campo) {
		this.append(campo.getNomeCampo());
	}

	private void appendAlias(CampoDTO campo) {
		this.jpql.append(creatAlias(campo));
		appendPonto();
	}

	private void appendPonto() {
		this.jpql.append(".");
	}

	private String creatAlias(ICreatAlias creatCampo) {
		String key = creatCampo.getKeyAlias();
		
		if(!this.mapAlis.containsKey(key)){
			
			String alias = creatCampo.generateAlias();
			
			while (this.mapAlis.containsValue(alias)) {
				alias = creatCampo.generateAlias();
			}
			
			creatCampo.resetContatorAlias();
			
			this.mapAlis.put(key, alias);
		}
		
		return this.mapAlis.get(key);
	}
	
	public JpqlBuilder novaLinha(){
		this.jpql.append("\n");
		return this;
	}
	
	private void identar(){
		this.jpql.append("\t");
	}

	public JpqlBuilder appendFrom() {
		return append("FROM");
	}

	public JpqlBuilder appendEntidades(IEntidadeDTO entidadeOrigem,	List<IEntidadeVIiewDTO> relacionamentos) {
		identar();
		this.appendEntidade((EntidadeDTO) entidadeOrigem);
		this.appendRelacionamentos(relacionamentos);

		return this;
	}

	private void appendRelacionamentos(List<IEntidadeVIiewDTO> relacionamentos) {
		if(relacionamentos != null){
			
			Iterator<IEntidadeVIiewDTO> it = relacionamentos.iterator();
			
			while (it.hasNext()) {
				IEntidadeVIiewDTO relacionamento = (IEntidadeVIiewDTO) it.next();
				if(relacionamento.getEntidadeSelecionada() != null){
					novaLinha();
					identar();
					this.appendEntidade((EntidadeDTO) relacionamento.getEntidadeSelecionada());
				}
				
			}
		}
	}

	private void appendEntidade(EntidadeDTO entidadeOrigem) {
		
		if(entidadeOrigem.getEntidadeOrigem() != null){
			this.append("LEFT JOIN");
			this.jpql.append(creatAlias(((EntidadeDTO)entidadeOrigem.getEntidadeOrigem())));
			this.appendPonto();
		}
		
		this.append(entidadeOrigem.getEntidade());
		this.append(creatAlias(entidadeOrigem));
	}

	public JpqlBuilder appendWhere(List<IItemFormulaDTO> formulaCondicaoRetorno) {
		if(formulaCondicaoRetorno != null){
			return append("WHERE");
		}
		
		return this;
	}

	public JpqlBuilder appendCondicoes(List<IItemFormulaDTO> formulaCondicaoRetorno) {
		if(formulaCondicaoRetorno != null){
			Iterator<IItemFormulaDTO> it = formulaCondicaoRetorno.iterator();
			this.appendItemFormula(it);
		}
		
		return this;
	}
	
	
	
}
