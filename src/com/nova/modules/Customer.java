package com.nova.modules;

import javax.swing.JInternalFrame;

import com.nova.modules.customer.gui.CustomerBuilder;

/**
 * Representa el modulo de manejo de clientes.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class Customer extends JInternalFrame implements Module {

	/**
	 * Construye una ventana de visualizacion del modulo
	 * 
	 * @since 1.0
	 */
	public Customer() {
		super("Manejo de Clientes", true, true, true, true);
		new CustomerBuilder(this);

		setSize(600, 400);
		setAutoscrolls(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
