package br.com.geradorRelatorioCore.service;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import br.com.geradorRelatorioCore.exception.EntidadeConsumerException;
import br.com.geradorRelatorioCore.exception.GeracaoException;
import br.com.geradorRelatorioCore.interfaces.ICampoViewDTO;
import br.com.geradorRelatorioCore.interfaces.IEntidadeDTO;
import br.com.geradorRelatorioCore.interfaces.IEntidadeVIiewDTO;
import br.com.geradorRelatorioCore.interfaces.IItemFormulaDTO;
import br.com.geradorRelatorioCore.interfaces.IRelatorioDTO;

@Local
public interface IGeradorDeRelatorioServiceLocal {

	Map<String, IEntidadeDTO> loadEntidades(String urlProperties) throws EntidadeConsumerException;

	IRelatorioDTO gerarRelatorio(IEntidadeDTO entidadeOrigem, List<IEntidadeVIiewDTO> entidadesView,	List<ICampoViewDTO> camposConsulta,
						List<IItemFormulaDTO> formulaCondicaoRetorno,boolean agruparResultadoRetorno, String string, String fullPathClassGerador) throws GeracaoException;
	
}
