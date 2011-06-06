package com.nova.dat.loader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * Esta clase se encarga de cargar el archivo de parametros para la clase
 * GlobalConstants
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class ParameterLoader {

	/**
	 * Ruta del archivo que contiene las propiedades
	 */
	public static final String PROPERTIES_FILE = System
			.getProperty("user.home")
			+ System.getProperty("file.separator")
			+ ".store"
			+ System.getProperty("file.separator") + "store.properties";

	/**
	 * Contiene las propiedades de la aplicacion
	 */
	private static Properties properties;

	static {
		try {
			properties = new Properties();
			properties.loadFromXML(new FileInputStream(PROPERTIES_FILE));
		} catch (InvalidPropertiesFormatException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		} catch (IOException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
	}

	private ParameterLoader() {
		// Nadie puede instanciar
	}

	/**
	 * Obtiene la propiedad identificada por el parametro
	 * 
	 * @param key
	 *            Propiedad a retornar
	 * @return Valor de la propiedad
	 * @since 1.0
	 */
	public static String getProperty(String key) {
		return properties.getProperty(key);
	}
}