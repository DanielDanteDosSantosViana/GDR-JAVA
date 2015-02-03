package br.com.geradorRelatorioCore.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EntidadeGR {
	
	String label();
	
}
