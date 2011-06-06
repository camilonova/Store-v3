package com.nova.modules.kardex.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import com.nova.dat.parameter.GlobalConstants;
import com.nova.gui.shared.statusBar.StoreStatusBar;
import com.nova.modules.Kardex;
import com.nova.modules.ModuleBuilder;

/**
 * Construye el modulo del kardex.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class KardexBuilder implements ModuleBuilder {

	KardexMainPanel mainPanel;

	KardexToolBar toolBar;

	StoreStatusBar statusBar;

	/**
	 * Construimos los componentes pertenecientes al modulo
	 * <p>
	 * Creation date 18/05/2006 - 09:37:15 AM
	 * 
	 * @param kardex
	 *            Modulo principal
	 * 
	 * @since 1.0
	 */
	public KardexBuilder(Kardex kardex) {
		mainPanel = new KardexMainPanel(this);
		toolBar = new KardexToolBar(mainPanel);
		statusBar = new StoreStatusBar("Modulo de kardex cargado!!!");

		kardex.add(toolBar, BorderLayout.NORTH);
		kardex.add(mainPanel, BorderLayout.CENTER);
		kardex.add(statusBar, BorderLayout.SOUTH);
	}

	/**
	 * Constructor compatible con el modulo de pruebas
	 * <p>
	 * Creation date 18/05/2006 - 11:04:33 AM
	 * 
	 * @param testing
	 *            Modulo de pruebas
	 * 
	 * @since 1.0
	 */
	public KardexBuilder(JFrame testing) {
		mainPanel = new KardexMainPanel(this);
		toolBar = new KardexToolBar(mainPanel);
		statusBar = new StoreStatusBar("Modulo de kardex cargado!!!");

		testing.add(toolBar, BorderLayout.NORTH);
		testing.add(mainPanel, BorderLayout.CENTER);
		testing.add(statusBar, BorderLayout.SOUTH);
	}

	public String getRelatedTableName() {
		return "kardex";
	}

	public String getOrderColumn() {
		return "Proceso";
	}

	public String getOrderType() {
		return GlobalConstants.ORDER_DESCENDANT;
	}

	public void setStatusBarText(String message) {
		statusBar.setText(message);
	}

	/**
	 * Determina si la barra de herramientas esta habilitada
	 * <p>
	 * Creation date 25/05/2006 - 06:20:14 PM
	 * 
	 * @param enabled
	 *            True para activarlo, False de lo contrario
	 * @since 1.0
	 */
	public void setToolbarEnabled(boolean enabled) {
		toolBar.setEnabled(enabled);
	}

}
