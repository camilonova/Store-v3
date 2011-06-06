package com.nova.gui.shared.statusBar;

import javax.swing.JLabel;

/**
 * Clase compartida para todas las GUI de la aplicacion, crea una barra de
 * estado la cual permite informar al usuario de mensajes del sistema.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class StoreStatusBar extends JLabel {

	/**
	 * Crea una barra de estado sin texto inicial
	 * <p>
	 * Creation date 6/04/2006 - 08:26:39 AM
	 * 
	 * @since 1.0
	 */
	public StoreStatusBar() {
		this(null);
	}

	/**
	 * Crea una barra de estado para agregar en la parte baja de la ventana.
	 * <p>
	 * Creation date 6/04/2006 - 08:24:10 AM
	 * 
	 * @param text
	 *            Texto inicial
	 * 
	 * @since 1.0
	 */
	public StoreStatusBar(String text) {
		super(" " + text);
	}

	@Override
	public void setText(String text) {
		super.setText("  " + text);
	}
}
