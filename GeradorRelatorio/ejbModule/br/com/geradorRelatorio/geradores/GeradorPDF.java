package br.com.geradorRelatorio.geradores;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import br.com.geradorRelatorio.builders.HtmlBuilder;
import br.com.geradorRelatorio.dto.CampoDTO;
import br.com.geradorRelatorioCore.exception.GeradorArquivoRelatorioException;
import br.com.geradorRelatorioCore.interfaces.ICampoViewDTO;
import br.com.geradorRelatorioCore.interfaces.IGeradorArquivoRelatorio;
import br.com.geradorRelatorioCore.mascaras.CampoMascara;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;

@SuppressWarnings("deprecation")
public class GeradorPDF implements IGeradorArquivoRelatorio{

	@Override
	public byte[] gerar(List<ICampoViewDTO> camposConsulta, List<Object> valores, String nomeArquivo) throws GeradorArquivoRelatorioException {
		
		Document doc = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		try {
			doc = new Document();

			try {
				PdfWriter.getInstance(doc, bos);
			} catch (DocumentException e) {
				throw new GeradorArquivoRelatorioException(e);
			}
			
			doc.setPageSize(PageSize.A4_LANDSCAPE);
			doc.addCreationDate();
			doc.open();
			
			HtmlBuilder htmlBuilder = new HtmlBuilder().criar().criarCorpo()
													   .criarTabela().criarLinhaTabela();
			
			for (ICampoViewDTO campo : camposConsulta) {
				htmlBuilder = htmlBuilder.criarColunaTabela().addValor(campo.getApelido()).fecharColunaTabela();
			}
			
			
			for (Object valor : valores) {
				
				htmlBuilder = htmlBuilder.criarLinhaTabela();
				
				if(valor.getClass().isArray()){
					
					Object[] valorArray = (Object[]) valor;
					
					for (int i = 0; i < valorArray.length; i++) {
						Object v  = valorArray[i]; 
						CampoDTO campo = (CampoDTO) camposConsulta.get(i).getCamposSelecionado();
						
						htmlBuilder = addValor(htmlBuilder, v, campo, camposConsulta.get(i).getMascara());
					}
				}
				else{
					htmlBuilder = addValor(htmlBuilder, valor, (CampoDTO)camposConsulta.get(0).getCamposSelecionado(), camposConsulta.get(0).getMascara());
				}
				
				htmlBuilder = htmlBuilder.fecharLinhaTabela();
			}
			
			htmlBuilder = htmlBuilder
							.fecharTabela()
							.fecharCorpo()
							.fechar();
			
	        try {
				new HTMLWorker(doc).parse(htmlBuilder.construir());
			} catch (IOException e) {
				throw new GeradorArquivoRelatorioException(e);
			}
			
		} finally {
			if (doc != null) {
 				doc.close();
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e) {
					throw new GeradorArquivoRelatorioException(e);
				}
			}
		}
		
		return bos.toByteArray();
	}
	
	
	private HtmlBuilder addValor(HtmlBuilder xlsBuilder, Object valor, CampoDTO campo, CampoMascara campoMascara) {
		xlsBuilder = xlsBuilder
				.criarColunaTabela()
				.addValor(campoMascara == null ? campo.getMascara().processar(valor) : campoMascara.processar(valor))
				.fecharColunaTabela();

		return xlsBuilder;
	}


	@Override
	public String getExtensao() {
		return "pdf";
	}

}
