package com.nova.modules.invoice.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.nova.dat.parameter.GlobalConstants;
import com.nova.gui.shared.statusBar.StoreStatusBar;
import com.nova.modules.Invoice;
import com.nova.modules.ModuleBuilder;

/**
 * Esta clase se encarga de construir la interfaz del modulo de facturacion.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class InvoiceBuilder implements ModuleBuilder {

	InvoiceToolBar toolBar;

	InvoiceMainPanel mainPanel;

	StoreStatusBar statusBar;

	/**
	 * Construye la interfaz grafica del modulo.
	 * <p>
	 * Creation date 15/04/2006 - 05:20:04 PM
	 * 
	 * @param invoice
	 *            Modulo propietario
	 * 
	 * @since 1.0
	 */
	public InvoiceBuilder(Invoice invoice) {
		mainPanel = new InvoiceMainPanel(this, invoice);
		toolBar = new InvoiceToolBar(mainPanel);
		statusBar = new StoreStatusBar("Modulo de Facturacion cargado!!!");

		invoice.add(toolBar, BorderLayout.NORTH);
		invoice.add(mainPanel, BorderLayout.CENTER);
		invoice.add(statusBar, BorderLayout.SOUTH);
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
	public InvoiceBuilder(JFrame testing) {
		mainPanel = new InvoiceMainPanel(this, null);
		toolBar = new InvoiceToolBar(mainPanel);
		statusBar = new StoreStatusBar("Modulo de Facturacion cargado!!!");

		testing.add(toolBar, BorderLayout.NORTH);
		testing.add(mainPanel, BorderLayout.CENTER);
		testing.add(statusBar, BorderLayout.SOUTH);
	}

	public String getRelatedTableName() {
		return "facturacion";
	}

	public String getOrderColumn() {
		return "Factura";
	}

	public String getOrderType() {
		return GlobalConstants.ORDER_DESCENDANT;
	}

	public void setStatusBarText(String message) {
		statusBar.setText(message);
	}
}
