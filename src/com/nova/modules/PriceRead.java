package com.nova.modules;

import javax.swing.JInternalFrame;

import com.nova.modules.utilities.priceRead.PriceReadBuilder;

/**
 * Representa el modulo del lector de precios
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class PriceRead extends JInternalFrame implements Module {

	/**
	 * Construye una ventana de visualizacion del lector
	 * 
	 * @since 1.0
	 */
	public PriceRead() {
		super("Consultar Precios", false, true, false, true);
		add(new PriceReadBuilder());

		setSize(400, 120);
		setAutoscrolls(true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
