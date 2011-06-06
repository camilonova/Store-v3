package com.nova.modules.purchase.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.nova.dat.parameter.GlobalConstants;
import com.nova.gui.shared.statusBar.StoreStatusBar;
import com.nova.modules.ModuleBuilder;
import com.nova.modules.Purchase;

/**
 * Esta clase se encarga de construir la interfaz del modulo de compras.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class PurchaseBuilder implements ModuleBuilder {

	PurchaseToolBar toolBar;

	PurchaseMainPanel mainPanel;

	StoreStatusBar statusBar;

	/**
	 * Construye la interfaz grafica del modulo.
	 * <p>
	 * Creation date 15/04/2006 - 05:20:04 PM
	 * 
	 * @param purchase
	 *            Modulo propietario
	 * 
	 * @since 1.0
	 */
	public PurchaseBuilder(Purchase purchase) {
		mainPanel = new PurchaseMainPanel(this, purchase);
		toolBar = new PurchaseToolBar(mainPanel);
		statusBar = new StoreStatusBar("Modulo de Compras cargado!!!");

		purchase.add(toolBar, BorderLayout.NORTH);
		purchase.add(mainPanel, BorderLayout.CENTER);
		purchase.add(statusBar, BorderLayout.SOUTH);
	}

	/**
	 * Constructor compatible con el modulo de pruebas
	 * <p>
	 * Creation date 2/05/2006 - 05:28:25 PM
	 * 
	 * @param testing
	 *            Modulo de pruebas
	 * 
	 * @since 1.0
	 */
	public PurchaseBuilder(JFrame testing) {
		mainPanel = new PurchaseMainPanel(this, null);
		toolBar = new PurchaseToolBar(mainPanel);
		statusBar = new StoreStatusBar("Modulo de Compras cargado!!!");

		testing.add(toolBar, BorderLayout.NORTH);
		testing.add(mainPanel, BorderLayout.CENTER);
		testing.add(statusBar, BorderLayout.SOUTH);
	}

	public String getRelatedTableName() {
		return "compra";
	}

	public String getOrderColumn() {
		return "Compra";
	}

	public String getOrderType() {
		return GlobalConstants.ORDER_DESCENDANT;
	}

	public void setStatusBarText(String message) {
		statusBar.setText(message);
	}
}
