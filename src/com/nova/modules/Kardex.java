package com.nova.modules;

import javax.swing.JInternalFrame;

import com.nova.modules.kardex.gui.KardexBuilder;

/**
 * Representa el modulo de kardex. Este modulo es el encargado de manejar los
 * procesos de entrada y salida de los productos registrados en el inventario,
 * ademas de llevar el control del costo de los articulos cada vez que se
 * ingresa o se egresa cantidades del producto.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class Kardex extends JInternalFrame implements Module {

	/**
	 * Construye una ventana de visualizacion del modulo
	 * 
	 * @since 1.0
	 */
	public Kardex() {
		super("Kardex de los productos", false, true, false, true);
		new KardexBuilder(this);

		setSize(600, 400);
		setAutoscrolls(true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
