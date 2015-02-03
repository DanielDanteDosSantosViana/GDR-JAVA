package br.com.geradorRelatorio.builders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import br.com.geradorRelatorio.util.ExecelStyleUtil;
import br.com.geradorRelatorio.util.ExecelStyleUtil.EEstilo;

public class ExecelBuilder {
	
	private final Workbook wb;
	private Sheet planilha;
	
	private int indexLinha;
	private Row linha;
	
	private int indexCelula;
	private Cell cedula;
	
	private final ExecelStyleUtil styleUtil;
	
	public enum EnumTipoXml{
		XLS, XLMX
	}
	
	public ExecelBuilder(EnumTipoXml tipo) {
		this.wb = EnumTipoXml.XLS.equals(tipo) ? new HSSFWorkbook() : new XSSFWorkbook();
		this.styleUtil = new ExecelStyleUtil(this.wb);
	}
	
	public ExecelBuilder construirPlanilha(String label) {
		planilha = wb.createSheet(label);
		
		planilha.setDisplayGridlines(true);
		planilha.setPrintGridlines(false);
		
		return this;
	}

	public ExecelBuilder criarLinha() {
		this.linha = planilha.createRow(indexLinha);
		
		this.indexLinha++;
		this.indexCelula = 0;

		return this;
	}
	
	public ExecelBuilder criarCedula() {
		this.cedula = linha.createCell(indexCelula);
		
		return this;
	}
	
	public ExecelBuilder addValor(Object o) {
		
		if(o == null){
			this.cedula.setCellValue("");
		}
		else if(o instanceof Boolean){
			this.cedula.setCellValue((Boolean)o);
		}
		else if(o instanceof Calendar){
			this.cedula.setCellValue((Calendar)o);
		}
		else if(o instanceof Date){
			this.cedula.setCellValue((Date)o);
		}
		else if(o instanceof Double){
			this.cedula.setCellValue((Double)o);
		}
		else {
			this.cedula.setCellValue(o.toString().trim());
		}
		
		this.planilha.autoSizeColumn(indexCelula);
		indexCelula++;
		
		return this;
	}
	
	public ExecelBuilder addEstilo(EEstilo estilo) {
		this.cedula.setCellStyle(this.styleUtil.getStyle(estilo));
		
		return this;
	}
	
	public byte[] gerar() throws IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		try {
		    this.wb.write(bos);
		} finally {
		    bos.close();
		}
		
		return bos.toByteArray();
	}
	
}
