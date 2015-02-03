package br.com.geradorRelatorioCore.interfaces;

import java.lang.reflect.Field;

import br.com.geradorRelatorioCore.mascaras.CampoMascara;

public interface ICampoDTO {

	String getLabel();

	boolean isRelacionamento();

	CampoMascara getMascara();

	Field getField();

	String getLabelSimples();

}
