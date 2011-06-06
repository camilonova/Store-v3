package com.nova.modules;

import javax.swing.JInternalFrame;

import com.nova.modules.utilities.groups.GroupsBuilder;

/**
 * Representa el modulo de manejo de categorias de inventario.
 * 
 * @author Camilo Nova
 * @since 1.0
 */
public class Groups extends JInternalFrame implements Module {

	/**
	 * Construye una ventana de visualizacion del modulo
	 * 
	 * @since 1.0
	 */
	public Groups() {
		super("Manejo de Categorias", false, true, false, true);

		add(new GroupsBuilder());
		setSize(300, 200);
		setAutoscrolls(true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
