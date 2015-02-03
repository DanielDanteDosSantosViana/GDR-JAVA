package br.com.geradorRelatorioCore.mascaras;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MoneyMask extends CampoMascara{

	private static final Locale BRAZIL = new Locale("pt","BR"); 
    private static final DecimalFormatSymbols REAL = new DecimalFormatSymbols(BRAZIL);  

	public MoneyMask() {
		super("R$");
	}

	@Override
	public String getMascaraJS() {
		return "setMask('money')";
	}

	@Override
	public String processar(Object o) {
	    DecimalFormat mask = new DecimalFormat("¤ ###,###,###,###,###,###,##0.00",REAL); 
	    
		return o != null ? mask.format(o) : null;
	}

}
