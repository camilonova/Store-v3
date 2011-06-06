package com.nova.modules.provider.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.nova.dat.parameter.GlobalConstants;
import com.nova.gui.shared.statusBar.StoreStatusBar;
import com.nova.modules.ModuleBuilder;
import com.nova.modules.Provider;

/**
 * Esta clase se encarga de construir el modulo de manejo de proveedores
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class ProviderBuilder implements ModuleBuilder {

	ProviderToolBar toolBar;

	ProviderMainPanel mainPanel;

	StoreStatusBar statusBar;

	/**
	 * Construye la interfaz grafica del modulo.
	 * 
	 * @param customer
	 *            Modulo propietario
	 * @since 1.0
	 */
	public ProviderBuilder(Provider customer) {
		mainPanel = new ProviderMainPanel(this, customer);
		toolBar = new ProviderToolBar(mainPanel);
		statusBar = new StoreStatusBar("Modulo de proveedores cargado!!!");

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
	public ProviderBuilder(JFrame testing) {
		mainPanel = new ProviderMainPanel(this, null);
		toolBar = new ProviderToolBar(mainPanel);
		statusBar = new StoreStatusBar("Modulo de proveedores cargado!!!");

		testing.add(toolBar, BorderLayout.NORTH);
		testing.add(mainPanel, BorderLayout.CENTER);
		testing.add(statusBar, BorderLayout.SOUTH);
	}

	public String getRelatedTableName() {
		return "proveedores";
	}

	public String getOrderColumn() {
		return "Proveedor";
	}

	public String getOrderType() {
		return GlobalConstants.ORDER_ASCENDANT;
	}

	public void setStatusBarText(String message) {
		statusBar.setText(message);
	}
}
