package com.nova.modules.manualInvoice.log;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.dat.parameter.GlobalConstants;
import com.nova.gui.StoreSession;
import com.nova.log.input.special.DateUtils;
import com.nova.log.input.validator.MoneyValidatorField;
import com.nova.log.math.StoreMath;
import com.nova.modules.manualInvoice.gui.ManualInvoiceMainPanel;
import com.toedter.calendar.JDateChooser;

/**
 * Esta clase provee la interfaz de usuario para cancelar una factura credito
 * del modulo de facturacion manual
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class CancelManualInvoice extends JDialog {

	JLabel fechaFactLbl;

	JDateChooser fechaFactFld;

	JLabel facturaLbl;

	JTextField facturaFld;

	JLabel fechaCancLbl;

	JDateChooser fechaCancFld;

	JLabel valorLbl;

	MoneyValidatorField valorFld;

	JButton aceptarBtn;

	JButton cancelarBtn;

	ArrayList<String> rowData;

	private String selectedInvoice;

	/**
	 * Crea la interfaz grafica y da las caracteristicas de los componentes.
	 * <p>
	 * Creation date 5/04/2006 - 04:37:20 PM
	 * 
	 * @param mainPanel
	 *            Panel del modulo principal
	 * 
	 * @since 1.0
	 */
	public CancelManualInvoice(final ManualInvoiceMainPanel mainPanel) {
		selectedInvoice = mainPanel.getSelectedID();
		if (selectedInvoice == null) {
			JOptionPane.showMessageDialog(mainPanel,
					"Debe seleccionar una factura");
			return;
		}
		rowData = StoreCore.getAllRowData(mainPanel.getTableName(), "Factura",
				selectedInvoice);
		if (rowData.get(2).equals("ANULADA")
				|| rowData.get(3).equals("Contado")) {
			JOptionPane.showMessageDialog(mainPanel,
					"Debe seleccionar una factura a credito");
			return;
		}
		if (rowData.get(8) != null) {
			JOptionPane.showMessageDialog(mainPanel,
					"La factura fue cancelada el " + rowData.get(8));
			return;
		}

		JPanel upPnl = new JPanel();
		JPanel midPnl = new JPanel();
		JPanel lowPnl = new JPanel();

		upPnl.setBorder(BorderFactory
						.createTitledBorder("Informacion Factura"));
		midPnl.setBorder(BorderFactory
				.createTitledBorder("Informacion Cancelacion"));

		aceptarBtn = new JButton("Aceptar");
		cancelarBtn = new JButton("Cancelar");

		fechaFactLbl = new JLabel("Fecha Factura");
		fechaFactFld = new JDateChooser(new Date());
		facturaLbl = new JLabel("Factura");
		facturaFld = new JTextField(rowData.get(0));
		fechaCancLbl = new JLabel("Fecha Cancelacion");
		fechaCancFld = new JDateChooser(new Date());
		valorLbl = new JLabel("Valor Cancelado");
		valorFld = new MoneyValidatorField(25, aceptarBtn, cancelarBtn);

		fechaFactLbl.setPreferredSize(new Dimension(110, 20));
		fechaFactFld.setPreferredSize(new Dimension(120, 20));
		facturaLbl.setPreferredSize(new Dimension(100, 20));
		facturaFld.setPreferredSize(new Dimension(100, 20));

		fechaCancLbl.setPreferredSize(new Dimension(110, 20));
		fechaCancFld.setPreferredSize(new Dimension(120, 20));
		valorLbl.setPreferredSize(new Dimension(100, 20));
		valorFld.setPreferredSize(new Dimension(100, 20));

		fechaFactFld.setCalendar(DateUtils.SQLtoCalendar(rowData.get(1)));
		valorFld.setDouble(Double.parseDouble(rowData.get(7)));
		fechaFactFld.setEnabled(false);
		facturaFld.setEditable(false);
		facturaFld.setHorizontalAlignment(SwingConstants.RIGHT);

		aceptarBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// Validamos las fechas
				Calendar cancelCal = fechaCancFld.getCalendar();
				if (cancelCal.compareTo(fechaFactFld.getCalendar()) < 0) {
					JOptionPane
							.showMessageDialog(CancelManualInvoice.this,
									"La fecha de cancelacion no puede ser anterior a la de facturacion");
					fechaCancFld.requestFocus();
					return;
				}

				try {
					// Restamos el valor de la factura del credito del cliente
					ArrayList<String> datosCliente = StoreCore.getAllRowData(
							"clientes", "Cliente", rowData.get(2));
					double valorCancelado = Double.parseDouble(rowData.get(7));
					double saldo = StoreMath.add(Double.parseDouble(datosCliente.get(8)), valorCancelado, true);
					
					datosCliente.set(8, String.valueOf(saldo));
					StoreCore.updateData("clientes", datosCliente);

					// Cancelamos la factura
					rowData.set(8, DateUtils.CalendarToSQL(cancelCal));
					rowData.set(9, String.valueOf(valorFld.getDouble()));
					rowData.set(10, StoreSession.getUserName());
					rowData.set(11, "NOW()");
					StoreCore.updateData(mainPanel.getTableName(), rowData);
					mainPanel.updateData("Factura No "+facturaFld.getText()+" Cancelada!!!");
					dispose();
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

		upPnl.add(fechaFactLbl);
		upPnl.add(fechaFactFld);
		upPnl.add(facturaLbl);
		upPnl.add(facturaFld);

		midPnl.add(fechaCancLbl);
		midPnl.add(fechaCancFld);
		midPnl.add(valorLbl);
		midPnl.add(valorFld);

		lowPnl.add(aceptarBtn);
		lowPnl.add(cancelarBtn);

		add(upPnl, BorderLayout.NORTH);
		add(midPnl, BorderLayout.CENTER);
		add(lowPnl, BorderLayout.SOUTH);

		pack();
		setComponentsTooltip();
		setTitle("Cancelar Factura");
		setLocation((GlobalConstants.SCREEN_SIZE_WIDTH - getWidth()) / 2,
				(GlobalConstants.SCREEN_SIZE_HEIGHT - getHeight()) / 2);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setAlwaysOnTop(true);
		setResizable(false);
		setVisible(true);
	}

	/**
	 * Determina los tooltips de los componentes
	 * <p>
	 * Creation date 6/04/2006 - 08:19:19 AM
	 * 
	 * @since 1.0
	 */
	private void setComponentsTooltip() {
		fechaFactFld.setToolTipText("Fecha de facturacion");
		facturaFld.setToolTipText("Numero de factura");
		fechaCancFld.setToolTipText("Fecha en la que se cancela la factura");
		valorFld.setToolTipText("Valor a cancelar");
	}
}
