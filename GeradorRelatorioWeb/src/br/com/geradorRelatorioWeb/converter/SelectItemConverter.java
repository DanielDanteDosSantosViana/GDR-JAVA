package br.com.geradorRelatorioWeb.converter;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItems;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;

import br.com.geradorRelatorioCore.enumeration.EFuncoes;
import br.com.geradorRelatorioCore.interfaces.ICampoDTO;
import br.com.geradorRelatorioCore.interfaces.IEntidadeDTO;
import br.com.geradorRelatorioCore.mascaras.CampoMascara;

@FacesConverter("selectItemConversorGR")
public class SelectItemConverter implements Converter{

	@Override
	@SuppressWarnings("unchecked")
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		List<SelectItem> itens = (List<SelectItem>)((UISelectItems)arg1.getChildren().get(0)).getValue();
		
		for (SelectItem selectItem : itens) {
			if(arg2.equals(selectItem.getLabel())){
				return selectItem.getValue();
			}
		}
		
		return null;
	}

	@Override
	public String getAsString(FacesContext faces, UIComponent ui, Object o) {
		String ret = null;
		
		if(o instanceof IEntidadeDTO) {
			ret = ((IEntidadeDTO)o).getLabel();
		}
		else if(o instanceof ICampoDTO){
			ret = ((ICampoDTO)o).getLabel();
		}
		else if(o instanceof CampoMascara ){
			ret = ((CampoMascara)o).getLabel();
		}
		else if(o instanceof EFuncoes){
			ret = ((EFuncoes)o).getLabel();
		}
		
		return ret;
	}

}
