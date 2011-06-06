package com.nova.modules;

import javax.swing.JInternalFrame;

import com.nova.modules.stock.gui.StockBuilder;

/**
 * Representa el modulo de manejo del inventario.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class Stock extends JInternalFrame implements Module {

	/**
	 * Construye una ventana de visualizacion del modulo
	 * 
	 * @since 1.0
	 */
	public Stock() {
		super("Inventario", true, true, true, true);
		new StockBuilder(this);

		setSize(800, 600);
		setAutoscrolls(true);
		setResizable(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
