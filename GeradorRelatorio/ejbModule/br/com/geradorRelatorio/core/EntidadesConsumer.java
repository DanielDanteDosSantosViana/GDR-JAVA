package br.com.geradorRelatorio.core;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.persistence.Entity;

import br.com.geradorRelatorio.dto.CampoDTO;
import br.com.geradorRelatorio.dto.EntidadeDTO;
import br.com.geradorRelatorio.util.Util;
import br.com.geradorRelatorioCore.annotations.CampoGR;
import br.com.geradorRelatorioCore.annotations.EntidadeGR;
import br.com.geradorRelatorioCore.exception.EntidadeConsumerException;
import br.com.geradorRelatorioCore.exception.EntidadeConsumerException.EError;
import br.com.geradorRelatorioCore.interfaces.IEntidadeDTO;


public class EntidadesConsumer {
	
	private final Properties properties;
	private final Map<String, IEntidadeDTO> entidades;
	
	public EntidadesConsumer(String urlProperties) throws EntidadeConsumerException {
		properties = new Properties();
		entidades = new HashMap<String, IEntidadeDTO>();
		
		try {
			loadProperties(urlProperties);
		} catch (IOException e) {
			throw new EntidadeConsumerException(e, EError.PROPERTIES_NOT_FOUND);
		}
	}
	
	private void loadProperties(String urlProperties) throws IOException {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		properties.load(loader.getResourceAsStream(urlProperties));
	}

	public Map<String, IEntidadeDTO> consumer() throws EntidadeConsumerException {
		Set<Object> keys = properties.keySet();
		
		for (Object key : keys) {

			Class<?> clazz = loadClass(key);
				
			EntidadeDTO entidade = creatEntidade(clazz);	
			
			entidades.put(entidade.getClazz().getName(), entidade);
		}
		
		return entidades;
	}

	private EntidadeDTO creatEntidade(Class<?> clazz) throws EntidadeConsumerException {
		
		EntidadeDTO entidade = new EntidadeDTO(clazz);
		
		loadCampos(entidade, clazz);
		
		return entidade;
	}

	private void loadCampos(EntidadeDTO entidade, Class<?> clazz) throws EntidadeConsumerException {
		if(!clazz.getSuperclass().equals(Object.class)){
			loadCampos(entidade, clazz.getSuperclass());
		}
		
		Field[] fields = clazz.getDeclaredFields();
		
		for (Field field : fields) {
			if(field.isAnnotationPresent(CampoGR.class)){
				CampoDTO campo  = creatCampo(field, entidade);
				entidade.getCampos().add(campo);
			}
		}
		
	}

	private CampoDTO creatCampo(Field field, EntidadeDTO entidade) throws EntidadeConsumerException {
		CampoDTO campo = new CampoDTO(field, Util.hasRelacionamento(field), entidade);
		
		validarCampo(campo);

		if(campo.isRelacionamento()){
			loadRelacionamentos(entidade, campo);
		}
		
		return campo;
	}

	private void loadRelacionamentos(EntidadeDTO entidade, CampoDTO campo) throws EntidadeConsumerException {
		Class<?> type = typeOfRelacionamento(campo.getField());
		EntidadeDTO relacionamento = creatEntidade(type);
		relacionamento.setEntidadeOrigem(entidade);
		relacionamento.setCampoRelacionamento(campo);
		entidade.getRelacionamentos().add(relacionamento);
		
	}

	private Class<?> typeOfRelacionamento(Field field) {
		Class<?> ret = field.getType();
		
		if(Util.isCollecion(ret)){
			ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
			ret = (Class<?>) stringListType.getActualTypeArguments()[0];
		}
		
		return ret;
	}

	private void validarCampo(CampoDTO campo) throws EntidadeConsumerException {
		Class<?> type = campo.getField().getType(); 
		
		if(!type.isPrimitive() && !type.isEnum() && !Util.isWrapper(type) && !campo.isRelacionamento() ){
			throw new EntidadeConsumerException(EError.RELACIONAMENTO_IVALIDO);
		}
		
	}

	private void validarClasse(Class<?> clazz) throws EntidadeConsumerException {
		if(!clazz.isAnnotationPresent(EntidadeGR.class) || !clazz.isAnnotationPresent(Entity.class)){
			throw new EntidadeConsumerException(EError.CLASS_NOT_VALID);
		}
		
	}

	private Class<?> loadClass(Object key) throws EntidadeConsumerException {
		String classFullPath = properties.getProperty((String) key);
		
		try {
			Class<?> clazz = Class.forName(classFullPath);
			
			validarClasse(clazz);
			
			return clazz;
		} catch (ClassNotFoundException e) {
			throw new EntidadeConsumerException(e, EError.CLASS_NOT_FOUND);
		}
	}

}
