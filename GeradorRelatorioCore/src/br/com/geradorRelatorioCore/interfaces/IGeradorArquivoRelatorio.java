package br.com.geradorRelatorioCore.interfaces;

import java.util.List;

import br.com.geradorRelatorioCore.exception.GeradorArquivoRelatorioException;

public interface IGeradorArquivoRelatorio {

	byte[] gerar(List<ICampoViewDTO> camposConsulta, List<Object> valores, String nomeArquivo) throws GeradorArquivoRelatorioException;

	String getExtensao();

}
