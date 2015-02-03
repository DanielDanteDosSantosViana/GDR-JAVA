package br.com.geradorRelatorioCore.mascaras;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeMask extends CampoMascara{

	public DateTimeMask() {
		super("dd/mm/aaaa hh:mm");
	}

	@Override
	public String getMascaraJS() {
		return "setMask('dateTime')";
	}

	@Override
	public String processar(Object o) {
		if(o instanceof Date){
			Date data = (Date) o;
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");
			
			return df.format(data);
		}
		
		return null;
	}

}
