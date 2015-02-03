package br.com.geradorRelatorioCore.mascaras;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateMask extends CampoMascara{

	public DateMask() {
		super("dd/mm/aaaa");
	}

	@Override
	public String getMascaraJS() {
		return "setMask('date')";
	}

	@Override
	public String processar(Object o) {
		if(o instanceof Date){
			Date data = (Date) o;
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			
			return df.format(data);
		}
		
		return null;
	}

}
