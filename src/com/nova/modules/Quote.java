package com.nova.modules;

import javax.swing.JInternalFrame;

import com.nova.modules.quotation.gui.QuoteBuilder;

/**
 * Representa el modulo de cotizaciones.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class Quote extends JInternalFrame implements Module {

	/**
	 * Construye una ventana de visualizacion del modulo.
	 * <p>
	 * Creation date 15/04/2006 - 04:37:35 PM
	 * 
	 * @since 1.0
	 */
	public Quote() {
		super("Cotizaciones", true, true, true, true);
		new QuoteBuilder(this);

		setSize(800, 600);
		setAutoscrolls(true);
		setResizable(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
