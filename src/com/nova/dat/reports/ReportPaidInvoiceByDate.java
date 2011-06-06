package com.nova.dat.reports;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.log.input.special.DateUtils;

/**
 * Representa la logica de la generacion del reporte de facturas manuales
 * canceladas por fecha utilizando JasperReports
 * @author Camilo Nova
 * @version 1.0
 */
public class ReportPaidInvoiceByDate {

	private String reportPath = "/reports/facturasCanceladasxFechas.jasper";

	/**
	 * Hace el llamado al reporte previamente configurado enviando los parametros
	 * necesarios
	 * <p>Creation date 21/07/2006 - 09:39:46 PM
	 * @param beginDate			Fecha inicial del reporte
	 * @param endDate			Fecha final del reporte
	 * @param owner				Propietario del reporte
	 *
	 * @since 1.0
	 */
	public ReportPaidInvoiceByDate(Calendar beginDate, Calendar endDate, String owner) {
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("BEGIN_DATE", DateUtils.CalendarToSQL(beginDate));
			parameters.put("END_DATE", DateUtils.CalendarToSQL(endDate));
			parameters.put("OWNER", owner);
			
			InputStream inputStream = getClass().getResourceAsStream(reportPath);
			
			Connection connection = StoreCore.getConnection();
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					inputStream, parameters, connection);

			JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
			jasperViewer.setVisible(true);
			connection.close();
		} catch (JRException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
	}
}
