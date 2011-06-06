package com.nova.modules;

import javax.swing.JInternalFrame;

import com.nova.modules.invoice.gui.InvoiceBuilder;

/**
 * Representa el modulo de facturacion.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class Invoice extends JInternalFrame implements Module {

	/**
	 * Construye una ventana de visualizacion del modulo.
	 * <p>
	 * Creation date 15/04/2006 - 04:37:35 PM
	 * 
	 * @since 1.0
	 */
	public Invoice() {
		super("Facturacion", true, true, true, true);
		new InvoiceBuilder(this);

		setSize(800, 600);
		setAutoscrolls(true);
		setResizable(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
