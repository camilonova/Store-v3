package com.nova.modules;

import javax.swing.JInternalFrame;

import com.nova.modules.utilities.tax.TaxBuilder;

/**
 * Representa el modulo de manejo de impuestos.
 * 
 * @author Camilo Nova
 * @since 1.0
 */
public class Tax extends JInternalFrame implements Module {

	/**
	 * Construye una ventana de visualizacion del modulo
	 * 
	 * @since 1.0
	 */
	public Tax() {
		super("Manejo de Impuestos", false, true, false, true);

		add(new TaxBuilder());
		setSize(320, 160);
		setAutoscrolls(true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
