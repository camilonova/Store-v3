package com.nova.modules;

import javax.swing.JInternalFrame;

import com.nova.modules.provider.gui.ProviderBuilder;

/**
 * Representa el modulo de manejo de proveedores.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class Provider extends JInternalFrame implements Module {

	/**
	 * Construye una ventana de visualizacion del modulo
	 * 
	 * @since 1.0
	 */
	public Provider() {
		super("Manejo de Proveedores", true, true, true, true);
		new ProviderBuilder(this);

		setSize(600, 400);
		setAutoscrolls(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
