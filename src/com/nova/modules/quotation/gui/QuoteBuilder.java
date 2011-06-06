package com.nova.modules.quotation.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.nova.dat.parameter.GlobalConstants;
import com.nova.gui.shared.statusBar.StoreStatusBar;
import com.nova.modules.ModuleBuilder;
import com.nova.modules.Quote;

/**
 * Esta clase se encarga de construir la interfaz del modulo de cotizaciones.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class QuoteBuilder implements ModuleBuilder {

	QuoteToolBar toolBar;

	QuoteMainPanel mainPanel;

	StoreStatusBar statusBar;

	/**
	 * Construye la interfaz grafica del modulo.
	 * <p>
	 * Creation date 15/04/2006 - 05:20:04 PM
	 * 
	 * @param quote
	 *            Modulo propietario
	 * 
	 * @since 1.0
	 */
	public QuoteBuilder(Quote quote) {
		mainPanel = new QuoteMainPanel(this, quote);
		toolBar = new QuoteToolBar(mainPanel);
		statusBar = new StoreStatusBar("Modulo de Cotizaciones cargado!!!");

		quote.add(toolBar, BorderLayout.NORTH);
		quote.add(mainPanel, BorderLayout.CENTER);
		quote.add(statusBar, BorderLayout.SOUTH);
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
	public QuoteBuilder(JFrame testing) {
		mainPanel = new QuoteMainPanel(this, null);
		toolBar = new QuoteToolBar(mainPanel);
		statusBar = new StoreStatusBar("Modulo de Cotizaciones cargado!!!");

		testing.add(toolBar, BorderLayout.NORTH);
		testing.add(mainPanel, BorderLayout.CENTER);
		testing.add(statusBar, BorderLayout.SOUTH);
	}

	public String getRelatedTableName() {
		return "cotizacion";
	}

	public String getOrderColumn() {
		return "Cotizacion";
	}

	public String getOrderType() {
		return GlobalConstants.ORDER_DESCENDANT;
	}

	public void setStatusBarText(String message) {
		statusBar.setText(message);
	}
}
