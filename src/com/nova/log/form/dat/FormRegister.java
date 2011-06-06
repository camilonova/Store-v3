package com.nova.log.form.dat;

import javax.swing.JDialog;

import com.nova.log.form.gui.Form;

/**
 * Esta clase abstrae las clases que deben registrar datos provenientes de un
 * formato por lo cual los clientes de Form deben crear una clase que herede de
 * esta para registar los datos en la base de datos
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public abstract class FormRegister extends JDialog {

	protected final Form form;

	/**
	 * Constructor. Almacena una instancia del formulario
	 * <p>
	 * Creation date 27/05/2006 - 10:04:10 AM
	 * 
	 * @param form
	 *            Formulario, se obtienen los datos a partir de el
	 * 
	 * @since 1.0
	 */
	public FormRegister(Form form) {
		super(form);
		this.form = form;
	}

	/**
	 * Muestra la ventana de registro en caso de tener una. La ventana y sus
	 * caracteristicas deben ser procesadas en este metodo
	 * <p>
	 * Creation date 27/05/2006 - 09:53:51 AM
	 * 
	 * @since 1.0
	 */
	protected abstract void showGUI();

	/**
	 * Registra los datos del formulario en la Base de Datos
	 * <p>
	 * Creation date 27/05/2006 - 09:55:11 AM
	 * 
	 * @since 1.0
	 */
	protected abstract void register();

}
