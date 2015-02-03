package br.com.geradorRelatorio.geradores;

import java.io.IOException;
import java.util.List;

import br.com.geradorRelatorio.builders.ExecelBuilder;
import br.com.geradorRelatorio.builders.ExecelBuilder.EnumTipoXml;
import br.com.geradorRelatorio.dto.CampoDTO;
import br.com.geradorRelatorio.util.ExecelStyleUtil.EEstilo;
import br.com.geradorRelatorioCore.exception.GeradorArquivoRelatorioException;
import br.com.geradorRelatorioCore.interfaces.ICampoViewDTO;
import br.com.geradorRelatorioCore.interfaces.IGeradorArquivoRelatorio;
import br.com.geradorRelatorioCore.mascaras.CampoMascara;

public class GeradorXls implements IGeradorArquivoRelatorio{
	
	@Override
	public byte[] gerar(List<ICampoViewDTO> camposConsulta, List<Object> valores, String nomeArquivo) throws GeradorArquivoRelatorioException{
		
		ExecelBuilder xlsBuilder = new ExecelBuilder(EnumTipoXml.XLS)
										.construirPlanilha(nomeArquivo)
										.criarLinha();
		
		for (ICampoViewDTO coluna : camposConsulta) {
			xlsBuilder = xlsBuilder
							.criarCedula()
							.addValor(coluna.getApelido())
							.addEstilo(EEstilo.TITULO);
		}
		
		for (Object valor : valores) {
			
			xlsBuilder = xlsBuilder.criarLinha();
			if(valor != null){
				if(valor.getClass().isArray()){
					
					Object[] valorArray = (Object[]) valor;
					
					for (int i = 0; i < valorArray.length; i++) {
						Object v  = valorArray[i]; 
						CampoDTO campo = (CampoDTO) camposConsulta.get(i).getCamposSelecionado();
						
						xlsBuilder = addValor(xlsBuilder, v, campo, camposConsulta.get(i).getMascara());
					}
				}
				else{
					xlsBuilder = addValor(xlsBuilder, valor, (CampoDTO)camposConsulta.get(0).getCamposSelecionado(), camposConsulta.get(0).getMascara());
				}
			}
		}
		
		try {
			return xlsBuilder.gerar();
		} catch (IOException e) {
			throw new GeradorArquivoRelatorioException(e);
		}
	}

	private ExecelBuilder addValor(ExecelBuilder xlsBuilder, Object valor, CampoDTO campo, CampoMascara campoMascara) {
		xlsBuilder = xlsBuilder
				.criarCedula()
				.addValor(campoMascara != null ? campoMascara.processar(valor) : campo != null ? campo.getMascara().processar(valor) : valor);

		return xlsBuilder;
	}

	@Override
	public String getExtensao() {
		return "xls";
	}
	
}
