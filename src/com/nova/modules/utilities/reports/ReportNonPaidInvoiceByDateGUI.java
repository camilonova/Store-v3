package com.nova.modules.utilities.reports;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.dat.parameter.GlobalConstants;
import com.nova.dat.reports.ReportNonPaidInvoiceByDate;
import com.toedter.calendar.JDateChooser;

/**
 * Esta clase representa la interfaz grafica de la generacion del reporte de
 * facturas manuales sin cancelar por fechas
 * @author Camilo Nova
 * @version 1.0
 */
public class ReportNonPaidInvoiceByDateGUI extends JDialog {
	
	JDateChooser beginDate;
	
	JDateChooser endDate;
	
	JButton generarBtn;
	
	JButton cancelarBtn;
	
	/**
	 * Construye la interfaz grafica que permite la entrada
	 * de las fechas para la generacion del reporte
	 * <p>Creation date 21/07/2006 - 09:37:43 PM
	 *
	 * @since 1.0
	 */
	public ReportNonPaidInvoiceByDateGUI() {
		beginDate = new JDateChooser();
		endDate = new JDateChooser();
		generarBtn = new JButton("Generar");
		cancelarBtn = new JButton("Cancelar");
		
		JPanel upPnl = new JPanel();
		JPanel lowPnl = new JPanel();
		
		beginDate.setPreferredSize(new Dimension(90,20));
		endDate.setPreferredSize(new Dimension(90,20));
		
		upPnl.add(new JLabel("Desde"));
		upPnl.add(beginDate);
		upPnl.add(new JLabel("Hasta"));
		upPnl.add(endDate);
		
		lowPnl.add(generarBtn);
		lowPnl.add(cancelarBtn);

		generarBtn.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
				Calendar begin = beginDate.getCalendar();
				Calendar end = endDate.getCalendar();

				try {
					if(begin == null || end == null) {
						JOptionPane.showMessageDialog(ReportNonPaidInvoiceByDateGUI.this,
						"Tiene que ingresar ambas fechas");
						beginDate.requestFocus();
					}
					else if(!begin.equals(end) && end.after(begin)) {
						new ReportNonPaidInvoiceByDate(begin, end, StoreCore.getProperty("OWNER"));
						dispose();
					} else
						JOptionPane.showMessageDialog(ReportNonPaidInvoiceByDateGUI.this,
								"La fecha de inicio debe ser anterior a la fecha final");
				} catch (HeadlessException e1) {
					ErrorLogLoader.addErrorEntry(e1);
					e1.printStackTrace();
				} catch (Exception e1) {
					ErrorLogLoader.addErrorEntry(e1);
					e1.printStackTrace();
				}
		
			}
		
		});
		cancelarBtn.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		
		});
		
		add(upPnl, BorderLayout.CENTER);
		add(lowPnl, BorderLayout.SOUTH);
		
		setSize(300, 100);
		setLocation((GlobalConstants.SCREEN_SIZE_WIDTH - getWidth()) / 2,
				(GlobalConstants.SCREEN_SIZE_HEIGHT - getHeight()) / 3);
		setTitle("Reporte de facturas sin cancelar");
		setResizable(false);
		setModal(true);
		setAlwaysOnTop(true);
		setVisible(true);
	}

}
