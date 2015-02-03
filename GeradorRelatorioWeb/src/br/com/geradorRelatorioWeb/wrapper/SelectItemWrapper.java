package br.com.geradorRelatorioWeb.wrapper;

import javax.faces.model.SelectItem;

import br.com.geradorRelatorioCore.interfaces.ISelectItemWrapper;


public class SelectItemWrapper  extends SelectItem implements ISelectItemWrapper{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8977279276399818577L;
	
	public SelectItemWrapper(Object value, String label) {
		super(value, label);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (getValue() == null) {
			if (obj!= null)
				return false;
		} else if (!getValue().equals(obj))
			return false;
		return true;
	}
}
