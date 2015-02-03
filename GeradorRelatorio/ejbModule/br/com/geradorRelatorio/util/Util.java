package br.com.geradorRelatorio.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import br.com.geradorRelatorioCore.annotations.CampoGR;
import br.com.geradorRelatorioCore.exception.EntidadeConsumerException;
import br.com.geradorRelatorioCore.exception.EntidadeConsumerException.EError;
import br.com.geradorRelatorioCore.mascaras.CampoMascara;
import br.com.geradorRelatorioCore.mascaras.DateMask;
import br.com.geradorRelatorioCore.mascaras.MascaraNula;
import br.com.geradorRelatorioCore.mascaras.MoneyMask;

@SuppressWarnings("rawtypes")
public class Util {
	
	private static List<Object> TIPO_SIMPLES = new ArrayList<Object>();
	private static List<Object> TIPO_COMPLEXOS = new ArrayList<Object>();
	private static List<Class> RELACIONAMENTOS = new ArrayList<Class>();

	static{
		TIPO_SIMPLES.add(Integer.class);
		TIPO_SIMPLES.add(Long.class);
		TIPO_SIMPLES.add(Float.class);
		TIPO_SIMPLES.add(Boolean.class);
		TIPO_SIMPLES.add(Byte.class);
		TIPO_SIMPLES.add(Double.class);
		TIPO_SIMPLES.add(Short.class);
		TIPO_SIMPLES.add(BigDecimal.class);
		TIPO_SIMPLES.add(String.class);
		TIPO_SIMPLES.add(Date.class);
		
		RELACIONAMENTOS.add(OneToOne.class);
		RELACIONAMENTOS.add(OneToMany.class);
		RELACIONAMENTOS.add(ManyToOne.class);
		RELACIONAMENTOS.add(JoinTable.class);
		
		TIPO_COMPLEXOS.add(List.class);
		TIPO_COMPLEXOS.add(Set.class);
		TIPO_COMPLEXOS.add(Map.class);
	}
	
	public static boolean isWrapper(Class<?> type) {
		return TIPO_SIMPLES.contains(type);
	}

	@SuppressWarnings("unchecked")
	public static boolean hasRelacionamento(Field field) {
		for (Class relacionamento : RELACIONAMENTOS) {
			if(field.isAnnotationPresent(relacionamento)){
				return true;
			}
		}
		return false;
	}

	public static boolean isCollecion(Class<?> type) {
		return TIPO_COMPLEXOS.contains(type);
	}
	
	public static CampoMascara getMask(CampoGR campo, Field field) throws EntidadeConsumerException{
		
		try {
			if(!campo.mask().equals(CampoMascara.class)){
				return campo.mask().newInstance();
			}
			else{
				Class<?> type = field.getType(); 
				
				if(type.equals(Date.class)){
					return new DateMask();
				}
				else if(type.equals(BigDecimal.class)){
					return new MoneyMask();
				}
			}
		} catch (Exception e) {
			throw new EntidadeConsumerException(e, EError.MASCARA_INVALIDA);
		}
		
		return new MascaraNula();
	}
}
