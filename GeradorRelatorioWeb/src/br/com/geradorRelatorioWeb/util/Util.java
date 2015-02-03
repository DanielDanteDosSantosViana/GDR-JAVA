package br.com.geradorRelatorioWeb.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import br.com.geradorRelatorioCore.enumeration.EContextParam;
import br.com.geradorRelatorioCore.interfaces.ICampoDTO;
import br.com.geradorRelatorioCore.interfaces.IEntidadeDTO;
import br.com.geradorRelatorioCore.interfaces.ISelectItemWrapper;
import br.com.geradorRelatorioCore.mascaras.CampoMascara;
import br.com.geradorRelatorioCore.mascaras.DateMask;
import br.com.geradorRelatorioCore.mascaras.DateTimeMask;
import br.com.geradorRelatorioCore.mascaras.MoneyMask;
import br.com.geradorRelatorioWeb.wrapper.SelectItemWrapper;

public class Util {
	
	public static final List<SelectItem> MASCARAS = populaMascaras();
	
	public static String recuperaContextParam(EContextParam param){
		FacesContext faces = FacesContext.getCurrentInstance();
		return faces.getExternalContext().getInitParameter(param.getParam()); 
	}
	
	private static List<SelectItem> populaMascaras() {
		List<SelectItem> ret = new ArrayList<SelectItem>();
		
		ret.add(new SelectItem(null, ""));
		ret.add(createSelectItem(new DateMask()));
		ret.add(createSelectItem(new DateTimeMask()));
		ret.add(createSelectItem(new MoneyMask()));
		
		return ret;
	}
	
	private static SelectItem createSelectItem(CampoMascara mascara) {
		return new SelectItem(mascara, mascara.getLabel());
	}
	
	public static List<SelectItemWrapper> converteRelacionamentos(IEntidadeDTO entidadeOrigem){
		if(entidadeOrigem.getRelacionamentosView() == null){
			
			entidadeOrigem.setRelacionamentosView(new ArrayList<ISelectItemWrapper>());

			for (IEntidadeDTO relacionamento : entidadeOrigem.getRelacionamentos()) {
				entidadeOrigem.getRelacionamentosView().add(new SelectItemWrapper(relacionamento, relacionamento.getLabel()));
			}
		}
		
		return castList(entidadeOrigem.getRelacionamentosView(), SelectItemWrapper.class);
	}
	
	public static List<SelectItemWrapper> converteCamposNumericos(IEntidadeDTO entidadeOrigem){
		if(entidadeOrigem.getCamposNumericosView() == null){
			
			entidadeOrigem.setCamposNumericosView(new ArrayList<ISelectItemWrapper>());
			
			if(entidadeOrigem.getCampos() != null){
				for (ICampoDTO campo : entidadeOrigem.getCampos()) {
					if(!campo.isRelacionamento() && isNumeric(campo.getField().getType())){
						entidadeOrigem.getCamposNumericosView().add(new SelectItemWrapper(campo, campo.getLabel()));
					}
				}
			}
			
		}
		
		return castList(entidadeOrigem.getCamposNumericosView(), SelectItemWrapper.class);
	}
	
	public static List<SelectItemWrapper> converteCampos(IEntidadeDTO entidadeOrigem){
		if(entidadeOrigem.getCamposView() == null){
			
			entidadeOrigem.setCamposView(new ArrayList<ISelectItemWrapper>());
			
			if(entidadeOrigem.getCampos() != null){
				for (ICampoDTO campo : entidadeOrigem.getCampos()) {
					if(!campo.isRelacionamento()){
						entidadeOrigem.getCamposView().add(new SelectItemWrapper(campo, campo.getLabel()));
					}
				}
			}
			
		}
		
		return castList(entidadeOrigem.getCamposView(), SelectItemWrapper.class);
	}
	
	public static boolean isNumeric(Class<?> valor){
		boolean retorno = false;
		
		if(valor.equals(BigDecimal.class) || valor.equals(Integer.class) || 
		   valor.equals(Double.class) || valor.equals(Long.class) || valor.equals(Short.class)){
			retorno = true;
		}
		
		return retorno;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T> castList(List<?> lst, Class<T> clazz){
		return (List<T>)(Object) lst;
	}

	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}
	
}
