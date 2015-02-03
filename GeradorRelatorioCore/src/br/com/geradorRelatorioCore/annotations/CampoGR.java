package br.com.geradorRelatorioCore.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import br.com.geradorRelatorioCore.mascaras.CampoMascara;

@Retention(RetentionPolicy.RUNTIME)
public @interface CampoGR {
	
	String label();
	Class<? extends CampoMascara> mask() default CampoMascara.class;
	
}
