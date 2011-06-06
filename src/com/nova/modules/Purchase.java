package com.nova.modules;

import javax.swing.JInternalFrame;

import com.nova.modules.purchase.gui.PurchaseBuilder;

/**
 * Representa el modulo de compras.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class Purchase extends JInternalFrame implements Module {

	/**
	 * Construye una ventana de visualizacion del modulo.
	 * <p>
	 * Creation date 15/04/2006 - 04:37:35 PM
	 * 
	 * @since 1.0
	 */
	public Purchase() {
		super("Compras", true, true, true, true);
		new PurchaseBuilder(this);

		setSize(800, 600);
		setAutoscrolls(true);
		setResizable(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
