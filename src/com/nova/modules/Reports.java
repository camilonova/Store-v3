package com.nova.modules;

import javax.swing.JInternalFrame;

import com.nova.modules.utilities.reports.ReportBuilder;

/**
 * Representa el modulo de reportes del sistema.
 * 
 * @author Camilo Nova
 * @since 1.0
 */
public class Reports extends JInternalFrame implements Module {

	/**
	 * Construye una ventana de visualizacion del modulo
	 * 
	 * @since 1.0
	 */
	public Reports() {
		super("Reportes", false, true, false, true);

		new ReportBuilder(this);
		setSize(300, 150);
		setAutoscrolls(true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
