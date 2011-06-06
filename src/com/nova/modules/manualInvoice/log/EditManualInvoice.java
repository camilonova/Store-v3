package com.nova.modules.manualInvoice.log;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.log.input.special.DateUtils;
import com.nova.log.math.StoreMath;
import com.nova.modules.manualInvoice.gui.ManualInvoiceMainPanel;

/**
 * Esta clase facilita la logica para editar una factura manual ingresada en el
 * sistema
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class EditManualInvoice extends ManualInvoiceDataGUI {

	final String selectedInvoice;

	/**
	 * Construye la interfaz de usuario y agrega funcionalidad de editar una
	 * factura manual
	 * 
	 * @param mainPanel
	 *            Panel del modulo
	 * @since 1.0
	 */
	public EditManualInvoice(final ManualInvoiceMainPanel mainPanel) {
		super();
		selectedInvoice = mainPanel.getSelectedID();
		if (selectedInvoice == null) {
			JOptionPane.showMessageDialog(null, "Debe seleccionar una factura");
			return;
		}

		ArrayList<String> invoiceData = StoreCore.getAllRowData(mainPanel
				.getTableName(), "Factura", selectedInvoice);
		facturaFld.setText(selectedInvoice);
		facturaFld.setEnabled(false);
		seguirAgregandoCBox.setEnabled(false);
		String cliente = invoiceData.get(2);

		if (cliente.equals("ANULADA"))
			anuladaCBox.doClick();
		else {
			fechaFld.setCalendar(DateUtils.SQLtoCalendar(invoiceData.get(1)));
			clienteCbx.setSelectedItem(cliente);
			creditoRBtn.setSelected(invoiceData.get(3).equals("Credito"));
			excluidoFld.setDouble(Double.parseDouble(invoiceData.get(4)));
			gravadoFld.setDouble(Double.parseDouble(invoiceData.get(5)));
			double impu = Double.parseDouble(invoiceData.get(6));
			double grav = Double.parseDouble(invoiceData.get(5));
			if (grav > 0) {
				double percent = StoreMath.divide(impu, grav, true);
				impuestoCbx.setSelectedItem(StoreCore.getDataAt("impuestos",
						"Valor", String.valueOf(percent), "Impuesto"));
			}
			totalFdl.setDouble(Double.parseDouble(invoiceData.get(7)));
		}

		aceptarBtn.setToolTipText("Actualizar la informacion");
		aceptarBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (isValidData(true)) {
					try {
						// Actualizamos los datos de la factura
						StoreCore.updateData(mainPanel.getTableName(), getData());
						// Actualizamos el credito del cliente
						String client = (String) clienteCbx.getSelectedItem();
						double saldoCredito = StoreCore.getManualInvoiceCreditValue(client);
						StoreCore.setDataAt("clientes", "Cliente", client, "Credito",
								String.valueOf(saldoCredito));

						mainPanel.updateData("Factura N°" + facturaFld.getText()
								+ " actualizada correctamente");
						dispose();
					} catch (SQLException e1) {
						mainPanel.updateData("Error. Verifique los datos");
						ErrorLogLoader.addErrorEntry(e1);
						e1.printStackTrace();
					}
				}
			}

		});

		setTitle("Editar informacion de factura N°" + selectedInvoice);
		setVisible(true);
	}

}
