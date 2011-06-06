package com.nova.dat.loader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Properties;

import javax.swing.JOptionPane;

/**
 * Representa el flujo de error de la aplicacion, el cual almacena mensajes de
 * error en un archivo en el home del usuario
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class ErrorLogLoader {

	/**
	 * Ruta del archivo que contiene las propiedades
	 */
	public static final String ERR_LOG_FILE = System.getProperty("user.home")
			+ System.getProperty("file.separator") + ".store"
			+ System.getProperty("file.separator") + "store.err";

	private static Properties properties;

	static {
		try {
			properties = new Properties();
			properties.loadFromXML(new FileInputStream(ERR_LOG_FILE));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private ErrorLogLoader() {
		// Nadie la instancia
	}

	/**
	 * Agrega una entrada al log de error de la aplicacion
	 * <p>
	 * Creation date 5/06/2006 - 08:40:36 AM
	 * 
	 * @param error
	 *            Error a agregar al log
	 * @since 1.0
	 */
	public static void addErrorEntry(Exception error) {
		try {
			String timeStamp = DateFormat.getDateTimeInstance().format(
					new Date());
			String errDescription = error.getMessage() + "\nCaused by:\n" + error.getCause();
			properties.setProperty(timeStamp, errDescription);
			properties.storeToXML(new FileOutputStream(ERR_LOG_FILE),
					"Error Log - Store - Camilo Nova");
			JOptionPane.showMessageDialog(null, "Se ha producido un error grave\nDebe informar al administrador", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}