package br.com.geradorRelatorio.session;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.com.geradorRelatorio.builders.JpqlBuilder;
import br.com.geradorRelatorio.builders.QueryBuilder;
import br.com.geradorRelatorio.core.EntidadesConsumer;
import br.com.geradorRelatorio.dto.RelatorioDTO;
import br.com.geradorRelatorioCore.exception.EntidadeConsumerException;
import br.com.geradorRelatorioCore.exception.GeracaoException;
import br.com.geradorRelatorioCore.interfaces.ICampoViewDTO;
import br.com.geradorRelatorioCore.interfaces.IEntidadeDTO;
import br.com.geradorRelatorioCore.interfaces.IEntidadeVIiewDTO;
import br.com.geradorRelatorioCore.interfaces.IGeradorArquivoRelatorio;
import br.com.geradorRelatorioCore.interfaces.IItemFormulaDTO;
import br.com.geradorRelatorioCore.interfaces.IRelatorioDTO;
import br.com.geradorRelatorioCore.service.IGeradorDeRelatorioServiceLocal;

@Stateless
public class GeradorDeRelatorioServiceBeanBean implements IGeradorDeRelatorioServiceLocal{
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Map<String, IEntidadeDTO> loadEntidades(String urlProperties) throws EntidadeConsumerException{
		EntidadesConsumer entidadesConsumer = new EntidadesConsumer(urlProperties);
		return entidadesConsumer.consumer();
	}

	@Override
	@SuppressWarnings("unchecked")
	public IRelatorioDTO gerarRelatorio(IEntidadeDTO entidadeOrigem, List<IEntidadeVIiewDTO> entidadesView, List<ICampoViewDTO> camposConsulta,
			List<IItemFormulaDTO> formulaCondicaoRetorno, boolean agruparResultadoRetorno, String nomeArquivo, String fullPathClassGerador) throws GeracaoException {
		
		try{
			StringBuilder jpql = generarJpql(entidadeOrigem, entidadesView, camposConsulta, formulaCondicaoRetorno, agruparResultadoRetorno);
			Query query = gerarQuery(camposConsulta, formulaCondicaoRetorno, jpql);
			
			List<Object> resultados =	query.getResultList();
			
			IGeradorArquivoRelatorio gerador = (IGeradorArquivoRelatorio) Class.forName(fullPathClassGerador).newInstance();
			 
			return new RelatorioDTO( gerador.gerar(camposConsulta, resultados, nomeArquivo), gerador.getExtensao());
		}
		catch(Exception e){
			throw new GeracaoException();
		}
	}

	private Query gerarQuery(List<ICampoViewDTO> camposConsulta, List<IItemFormulaDTO> formulaCondicaoRetorno, StringBuilder jpql) throws ParseException {
		Query query = new QueryBuilder(em)
							.adicionarJpql(jpql)
							.adicionarParametros(camposConsulta, formulaCondicaoRetorno)
							.construir();
		return query;
	}

	private StringBuilder generarJpql(IEntidadeDTO entidadeOrigem,	List<IEntidadeVIiewDTO> entidadesView, List<ICampoViewDTO> camposConsulta, 
			List<IItemFormulaDTO> formulaCondicaoRetorno, boolean agruparResultadoRetorno) {
		
		return new JpqlBuilder()
						.appendSelect().novaLinha()
						.appendCamposRetorno(camposConsulta).novaLinha()
						.appendFrom().novaLinha()
						.appendEntidades(entidadeOrigem, entidadesView).novaLinha()
						.appendWhere(formulaCondicaoRetorno).novaLinha()
						.appendCondicoes(formulaCondicaoRetorno).novaLinha()
						.construir();
		
	}
}
