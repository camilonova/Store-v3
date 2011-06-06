package com.nova.modules.manualInvoice.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.nova.dat.parameter.GlobalConstants;
import com.nova.gui.shared.statusBar.StoreStatusBar;
import com.nova.modules.ManualInvoice;
import com.nova.modules.ModuleBuilder;

/**
 * Esta clase se encarga de construir el modulo de manejo de la facturacion
 * manual.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class ManualInvoiceBuilder implements ModuleBuilder {

	ManualInvoiceToolBar toolBar;

	ManualInvoiceMainPanel mainPanel;

	StoreStatusBar statusBar;

	/**
	 * Construye la interfaz grafica del modulo. Permite que el modulo se reciba
	 * por parametro con el fin de que la ventana tenga un tamaño variable.
	 * 
	 * @param invoice
	 *            Modulo de facturacion manual
	 * @since 1.0
	 */
	public ManualInvoiceBuilder(ManualInvoice invoice) {
		mainPanel = new ManualInvoiceMainPanel(this, invoice);
		toolBar = new ManualInvoiceToolBar(mainPanel);
		statusBar = new StoreStatusBar(
				"Modulo de Facturacion Manual cargado!!!");

		invoice.add(toolBar, BorderLayout.NORTH);
		invoice.add(mainPanel, BorderLayout.CENTER);
		invoice.add(statusBar, BorderLayout.SOUTH);
	}

	/**
	 * Constructor compatible con el modulo de pruebas
	 * <p>
	 * Creation date 2/05/2006 - 05:27:23 PM
	 * 
	 * @param testing
	 *            Modulo de pruebas
	 * 
	 * @since 1.0
	 */
	public ManualInvoiceBuilder(JFrame testing) {
		mainPanel = new ManualInvoiceMainPanel(this, null);
		toolBar = new ManualInvoiceToolBar(mainPanel);
		statusBar = new StoreStatusBar(
				"Modulo de Facturacion Manual cargado!!!");

		testing.add(toolBar, BorderLayout.NORTH);
		testing.add(mainPanel, BorderLayout.CENTER);
		testing.add(statusBar, BorderLayout.SOUTH);
	}

	public String getRelatedTableName() {
		return "facturacion_manual";
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
