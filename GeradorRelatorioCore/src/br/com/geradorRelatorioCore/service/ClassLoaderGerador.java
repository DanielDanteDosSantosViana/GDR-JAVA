package br.com.geradorRelatorioCore.service;

import java.io.Serializable;

public class ClassLoaderGerador extends ClassLoader implements Serializable{
	
	public ClassLoaderGerador(ClassLoader classLoader) {
		super(classLoader);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Class<?> loadClass(String name, boolean resolve)	throws ClassNotFoundException {
		return super.loadClass(name, resolve);
	}
}
