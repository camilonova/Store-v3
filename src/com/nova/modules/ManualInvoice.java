package com.nova.modules;

import javax.swing.JInternalFrame;

import com.nova.modules.manualInvoice.gui.ManualInvoiceBuilder;

/**
 * Representa el modulo de manejo de facturacion manual.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class ManualInvoice extends JInternalFrame implements Module {

	/**
	 * Construye una ventana de visualizacion del modulo
	 * 
	 * @since 1.0
	 */
	public ManualInvoice() {
		super("Facturacion Manual", true, true, true, true);
		new ManualInvoiceBuilder(this);

		setSize(800, 600);
		setAutoscrolls(true);
		setResizable(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
