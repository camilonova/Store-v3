package com.nova.modules;

import javax.swing.JInternalFrame;

import com.nova.modules.utilities.city.CityBuilder;

/**
 * Representa el modulo de manejo de ciudades.
 * 
 * @author Camilo Nova
 * @since 1.0
 */
public class City extends JInternalFrame implements Module {

	/**
	 * Construye una ventana de visualizacion del modulo
	 * 
	 * @since 1.0
	 */
	public City() {
		super("Manejo de Ciudades", false, true, false, true);

		add(new CityBuilder());
		setSize(300, 200);
		setAutoscrolls(true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
