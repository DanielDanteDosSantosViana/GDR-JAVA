package br.com.geradorRelatorioCore.interfaces;

import java.util.List;

import br.com.geradorRelatorioCore.mascaras.CampoMascara;

public interface ICampoViewDTO {

	boolean isAvancado();

	ICampoDTO getCamposSelecionado();

	String getApelido();

	List<IItemFormulaDTO> getFormulaFinal();

	CampoMascara getMascara();

}
