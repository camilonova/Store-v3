package com.nova.dat.parameter;

import java.awt.Toolkit;

import com.nova.dat.loader.ParameterLoader;

/**
 * Interface que representa los parametros globales y generales de la
 * aplicacion, los cuales son cargados desde un archivo externo.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public interface GlobalConstants {

	/**
	 * Puente de conexion con la DB
	 */
	public static final String CORE = ParameterLoader.getProperty("core");

	/**
	 * Driver de conexion con la DB
	 */
	public static final String DB_DRIVER = ParameterLoader
			.getProperty("driver");

	/**
	 * Nombre de usuario de la base de datos
	 */
	public static final String DB_USER = ParameterLoader.getProperty("parm1");

	/**
	 * Password del usuario en la base de datos
	 */
	public static final String DB_PASS = ParameterLoader.getProperty("parm2");

	/**
	 * Servidor primario de datos
	 */
	public static final String DB_SERVER = "jdbc:mysql://"
			+ ParameterLoader.getProperty("addr") + "/store";

	/**
	 * Orden ascendente de los datos
	 */
	public static final String ORDER_ASCENDANT = "ASC";

	/**
	 * Orden descendente de los datos
	 */
	public static final String ORDER_DESCENDANT = "DESC";

	/**
	 * Look and Feel de la aplicacion
	 */
	public static final String LOOK_AND_FEEL = ParameterLoader
			.getProperty("look");

	/**
	 * Ancho de la pantalla
	 */
	public static final int SCREEN_SIZE_WIDTH = Toolkit.getDefaultToolkit()
			.getScreenSize().width;

	/**
	 * Alto de la pantalla
	 */
	public static final int SCREEN_SIZE_HEIGHT = Toolkit.getDefaultToolkit()
			.getScreenSize().height;

	/**
	 * Separador general de la descripcion del articulo y su marca
	 */
	public static final String DESCRIPTION_BRAND_SEPARATOR = " .:. ";

}