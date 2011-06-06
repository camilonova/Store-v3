package com.nova.modules;

import javax.swing.JInternalFrame;

import com.nova.modules.users.gui.UsersBuilder;

/**
 * Representa el modulo de manejo de usuarios.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class Users extends JInternalFrame implements Module {

	/**
	 * Construye una ventana de visualizacion del modulo
	 * 
	 * @since 1.0
	 */
	public Users() {
		super("Manejo de Usuarios", false, true, false, true);
		new UsersBuilder(this);

		setSize(600, 400);
		setAutoscrolls(true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
