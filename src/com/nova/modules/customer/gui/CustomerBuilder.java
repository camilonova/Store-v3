package com.nova.modules.customer.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.nova.dat.parameter.GlobalConstants;
import com.nova.gui.shared.statusBar.StoreStatusBar;
import com.nova.modules.Customer;
import com.nova.modules.ModuleBuilder;

/**
 * Esta clase se encarga de construir el modulo de manejo de clientes
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class CustomerBuilder implements ModuleBuilder {

	CustomerToolBar toolBar;

	CustomerMainPanel mainPanel;

	StoreStatusBar statusBar;

	/**
	 * Construye la interfaz grafica del modulo.
	 * 
	 * @param customer
	 *            Modulo propietario
	 * @since 1.0
	 */
	public CustomerBuilder(Customer customer) {
		mainPanel = new CustomerMainPanel(this, customer);
		toolBar = new CustomerToolBar(mainPanel);
		statusBar = new StoreStatusBar("Modulo de clientes cargado!!!");

		customer.add(toolBar, BorderLayout.NORTH);
		customer.add(mainPanel, BorderLayout.CENTER);
		customer.add(statusBar, BorderLayout.SOUTH);
	}

	/**
	 * Constructor compatible con el modulo de pruebas
	 * <p>
	 * Creation date 2/05/2006 - 05:22:53 PM
	 * 
	 * @param testing
	 *            Modulo de pruebas
	 * 
	 * @since 1.0
	 */
	public CustomerBuilder(JFrame testing) {
		mainPanel = new CustomerMainPanel(this, null);
		toolBar = new CustomerToolBar(mainPanel);
		statusBar = new StoreStatusBar("Modulo de clientes cargado!!!");

		testing.add(toolBar, BorderLayout.NORTH);
		testing.add(mainPanel, BorderLayout.CENTER);
		testing.add(statusBar, BorderLayout.SOUTH);
	}

	public String getRelatedTableName() {
		return "clientes";
	}

	public String getOrderColumn() {
		return "Cliente";
	}

	public String getOrderType() {
		return GlobalConstants.ORDER_ASCENDANT;
	}

	public void setStatusBarText(String message) {
		statusBar.setText(message);
	}
}
