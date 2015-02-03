package br.com.geradorRelatorioCore.interfaces;

import java.util.List;

public interface IEntidadeDTO {

	Class<?> getClazz();

	boolean hasRelacionamento();

	List<IEntidadeDTO> getRelacionamentos();

	List<ICampoDTO> getCampos();

	IEntidadeDTO getEntidadeOrigem();

	List<ISelectItemWrapper> getRelacionamentosView();

	void setRelacionamentosView(List<ISelectItemWrapper> relacionamentosView);

	void setCamposView(List<ISelectItemWrapper> camposView);

	List<ISelectItemWrapper> getCamposView();

	List<ISelectItemWrapper> getCamposNumericosView();

	void setCamposNumericosView(List<ISelectItemWrapper> camposNumericosView);

	String getLabel();

}
