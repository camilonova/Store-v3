package com.nova.modules.users.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.nova.dat.parameter.GlobalConstants;
import com.nova.gui.shared.statusBar.StoreStatusBar;
import com.nova.modules.ModuleBuilder;
import com.nova.modules.Users;

/**
 * Esta clase se encarga de construir el modulo de manejo de usuarios
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class UsersBuilder implements ModuleBuilder {

	UsersToolBar toolBar;

	UsersMainPanel mainPanel;

	StoreStatusBar statusBar;

	/**
	 * Construye la interfaz grafica del modulo.
	 * 
	 * @param users
	 *            Modulo propietario
	 * @since 1.0
	 */
	public UsersBuilder(Users users) {
		mainPanel = new UsersMainPanel(this, users);
		toolBar = new UsersToolBar(mainPanel);
		statusBar = new StoreStatusBar("Modulo de usuarios cargado!!!");

		users.add(toolBar, BorderLayout.NORTH);
		users.add(mainPanel, BorderLayout.CENTER);
		users.add(statusBar, BorderLayout.SOUTH);
	}

	/**
	 * Constructor compatible con el modulo de pruebas
	 * <p>
	 * Creation date 2/05/2006 - 05:32:25 PM
	 * 
	 * @param testing
	 *            Modulo de pruebas
	 * 
	 * @since 1.0
	 */
	public UsersBuilder(JFrame testing) {
		mainPanel = new UsersMainPanel(this, null);
		toolBar = new UsersToolBar(mainPanel);
		statusBar = new StoreStatusBar("Modulo de usuarios cargado!!!");

		testing.add(toolBar, BorderLayout.NORTH);
		testing.add(mainPanel, BorderLayout.CENTER);
		testing.add(statusBar, BorderLayout.SOUTH);
	}

	public String getRelatedTableName() {
		return "usuarios";
	}

	public String getOrderColumn() {
		return "ID";
	}

	public String getOrderType() {
		return GlobalConstants.ORDER_ASCENDANT;
	}

	public void setStatusBarText(String message) {
		statusBar.setText(message);
	}
}
