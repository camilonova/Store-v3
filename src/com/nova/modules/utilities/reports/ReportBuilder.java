package com.nova.modules.utilities.reports;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JList;

import com.nova.modules.ModuleBuilder;
import com.nova.modules.Reports;

/**
 * Representa el constructor del modulo de reportes
 * @author Camilo Nova
 * @version 1.0
 */
public class ReportBuilder implements ModuleBuilder {

	/**
	 * Construye una lista con los reportes disponibles
	 * <p>Creation date 21/07/2006 - 10:53:01 PM
	 * @param reports		Modulo de reportes
	 *
	 * @since 1.0
	 */
	public ReportBuilder(Reports reports) {
		reports.add(prepareReportList());
	}

	/**
	 * Construye una lista con los reportes disponibles
	 * <p>Creation date 21/07/2006 - 09:47:42 PM
	 * @param testing		Modulo de pruebas
	 *
	 * @since 1.0
	 */
	public ReportBuilder(JFrame testing) {
		testing.add(prepareReportList());
	}
	
	private JList prepareReportList() {
		final ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("Reporte de facturas canceladas por fecha");
		arrayList.add("Reporte de facturas sin cancelar por fecha");
		
		final JList listado = new JList(arrayList.toArray());
		
		listado.setToolTipText("Haga click sobre el reporte que quiera generar");
		
		listado.addMouseListener(new MouseAdapter() {
		
			@Override
			@SuppressWarnings("synthetic-access")
			public void mouseClicked(MouseEvent e) {
				switch (listado.getSelectedIndex()) {
				case 0:
					new ReportPaidInvoiceByDateGUI();
					break;
				case 1:
					new ReportNonPaidInvoiceByDateGUI();
					break;

				default:
					break;
				}
				
				super.mouseClicked(e);
			}
		
		});
		
		return listado;
	}

	public String getRelatedTableName() {
		// Sin implementacion
		return null;
	}

	public String getOrderColumn() {
		// Sin implementacion
		return null;
	}

	public String getOrderType() {
		// Sin implementacion
		return null;
	}

	public void setStatusBarText(String message) {
		// Sin implementacion
	}

}
