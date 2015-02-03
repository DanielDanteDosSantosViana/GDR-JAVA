package br.com.geradorRelatorio.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class CastUtil {
	
	private final static String  DATE_PATTERN = "dd/MM/yyyy";
	
	@SuppressWarnings("unchecked")
	public static <T> T cast(Object obj, Class<T> type) throws ParseException{
		
		if(type.equals(Integer.class)){
			return (T) Integer.valueOf(obj.toString());
		}
		else if(type.equals(Long.class)){
			return (T) Integer.valueOf(obj.toString());
		}
		else if(type.equals(Short.class)){
			return (T) Integer.valueOf(obj.toString());
		}
		else if(type.equals(Double.class)){
			return (T) Integer.valueOf(obj.toString());
		}
		else if(type.equals(Date.class)){
			return (T) castDate(obj);
		}
		else{
			return (T) obj.toString();
		}
		
	}

	private static Date castDate(Object obj) throws ParseException {
		DateFormat df = new SimpleDateFormat(DATE_PATTERN);
		return df.parse(obj.toString());
	}
}
