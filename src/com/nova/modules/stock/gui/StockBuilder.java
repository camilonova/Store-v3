package com.nova.modules.stock.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.nova.dat.parameter.GlobalConstants;
import com.nova.gui.shared.statusBar.StoreStatusBar;
import com.nova.modules.ModuleBuilder;
import com.nova.modules.Stock;

/**
 * Esta clase se encarga de construir el modulo del inventario.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class StockBuilder implements ModuleBuilder {

	StockToolBar toolBar;

	StockMainPanel mainPanel;

	StoreStatusBar statusBar;

	/**
	 * Construye la interfaz grafica del modulo. Permite que el modulo se reciba
	 * por parametro con el fin de que la ventana tenga un tamaño variable.
	 * 
	 * @param stock
	 *            Modulo de inventario
	 * 
	 * @since 1.0
	 */
	public StockBuilder(Stock stock) {
		mainPanel = new StockMainPanel(this, stock);
		toolBar = new StockToolBar(mainPanel);
		statusBar = new StoreStatusBar("Modulo de Inventario cargado!!!");

		stock.add(toolBar, BorderLayout.NORTH);
		stock.add(mainPanel, BorderLayout.CENTER);
		stock.add(statusBar, BorderLayout.SOUTH);
	}

	/**
	 * Constructor para el modulo de pruebas
	 * <p>
	 * Creation date 29/04/2006 - 03:08:52 PM
	 * 
	 * @param testing
	 *            Modulo de pruebas
	 * 
	 * @since 1.0
	 */
	public StockBuilder(JFrame testing) {
		mainPanel = new StockMainPanel(this, null);
		toolBar = new StockToolBar(mainPanel);
		statusBar = new StoreStatusBar("Modulo de Inventario cargado!!!");

		testing.add(toolBar, BorderLayout.NORTH);
		testing.add(mainPanel, BorderLayout.CENTER);
		testing.add(statusBar, BorderLayout.SOUTH);
	}

	public String getRelatedTableName() {
		return "inventario";
	}

	public String getOrderColumn() {
		return "Descripcion";
	}

	public String getOrderType() {
		return GlobalConstants.ORDER_ASCENDANT;
	}

	public void setStatusBarText(String message) {
		statusBar.setText(message);
	}
}
