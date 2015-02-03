package br.com.geradorRelatorio.builders;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.ativaEmprestimo.util.Util;
import br.com.geradorRelatorio.dto.CampoDTO;
import br.com.geradorRelatorio.util.CastUtil;
import br.com.geradorRelatorioCore.enumeration.EOperador;
import br.com.geradorRelatorioCore.interfaces.ICampoViewDTO;
import br.com.geradorRelatorioCore.interfaces.IItemFormulaDTO;

public class QueryBuilder {
	
	private final EntityManager em;
	private Query quey;
	private Integer indexParameter;
	
	public QueryBuilder(EntityManager em) {
		this.em = em;
	}

	
	public QueryBuilder adicionarJpql(StringBuilder jpql){
		this.quey = em.createQuery(jpql.toString());
		this.indexParameter = 1;
		
		return this;
	}


	public QueryBuilder adicionarParametros(List<ICampoViewDTO> camposConsulta,	List<IItemFormulaDTO> formulaCondicaoRetorno) throws ParseException {
		adicionarParametrosConsulta(camposConsulta, true);
		adicionarParametrosCondicaoRetorno(formulaCondicaoRetorno, false);
		
		return this;
	}


	private void adicionarParametrosConsulta(List<ICampoViewDTO> camposConsulta, boolean parseToNumber) throws ParseException {
		if(camposConsulta != null){
			for (ICampoViewDTO campo : camposConsulta) {
				if(campo.isAvancado()){
					adicionarParametros(campo.getFormulaFinal(), parseToNumber);
				}
			}
		}
	}


	private void adicionarParametros(List<IItemFormulaDTO> list, boolean parseToLong) throws ParseException {
		for (int i = 0; i < list.size(); i++) {
			IItemFormulaDTO item = list.get(i);
			if(item.isValorLivre()){
				if(parseToLong){
 					Class<?>[] classesNumericas = new Class[]{BigDecimal.class, Integer.class, Double.class};
					
 					for (Class<?> classe : classesNumericas) {
 						try {
 							if(classe.equals(Integer.class)){
 								this.quey.setParameter(indexParameter, Double.valueOf(item.getValor().toString()).intValue());
 							}
 							else if(classe.equals(Double.class)){
 								this.quey.setParameter(indexParameter, Double.valueOf(item.getValor().toString()));
 							}
 							else if(classe.equals(BigDecimal.class)){
 								this.quey.setParameter(indexParameter, new BigDecimal(Double.valueOf(item.getValor().toString())));
 							}
						} catch (IllegalArgumentException e) {
							continue;
						}
 						break;
					}
 					
 					indexParameter++;
				}
				else{
					CampoDTO campo = (CampoDTO) list.get(i - 2).getValor();
					EOperador operador = (EOperador) list.get(i - 1).getValor();
					
					Class<?> type = campo.getField().getType(); 
					
					if(EOperador.CONTEM.equals(operador)){
						this.quey.setParameter(indexParameter++, "%"+CastUtil.cast(item.getValor(), type)+"%");
					}
					else{
						this.quey.setParameter(indexParameter++, CastUtil.cast(item.getValor(), type));
					}
					
				}
			}
		} 
		
	}
	
	private void adicionarParametrosCondicaoRetorno(List<IItemFormulaDTO> formulaCondicaoRetorno, boolean parseToLong) throws ParseException {
		if(formulaCondicaoRetorno != null){
			this.adicionarParametros(formulaCondicaoRetorno, parseToLong);
		}
	}


	public Query construir() {
		return this.quey;
	}
	
}
