package com.nova.modules.manualInvoice.log;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.modules.manualInvoice.gui.ManualInvoiceMainPanel;

/**
 * Esta clase facilita la logica para el ingreso de una factura manual
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class AddManualInvoice extends ManualInvoiceDataGUI {

	/**
	 * Construye la interfaz de usuario y agrega funcionalidad de agregar una
	 * factura manual
	 * 
	 * @param mainPanel
	 *            Panel del modulo
	 * @since 1.0
	 */
	public AddManualInvoice(final ManualInvoiceMainPanel mainPanel) {
		super();
		aceptarBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (isValidData(false)) {
					try {
						StoreCore.newData(mainPanel.getTableName(), getData());
						Thread thread = new Thread(new Runnable() {
						
							public void run() {
								// Actualizamos el credito del cliente
								String client = (String) clienteCbx.getSelectedItem();
								double saldoCredito = StoreCore.getManualInvoiceCreditValue(client);
								StoreCore.setDataAt("clientes", "Cliente", client, "Credito",
										String.valueOf(saldoCredito));
							}
						
						}, "Update Customer Credit Value");
						thread.start();

						mainPanel.updateData("Factura N°" + facturaFld.getText()
								+ " agregada correctamente");

						if (seguirAgregandoCBox.isSelected()) {
							if (anuladaCBox.isSelected())
								anuladaCBox.doClick();

							int factNumber = 1 + Integer.parseInt(facturaFld
									.getText());
							facturaFld.setText(String.valueOf(factNumber));
							clienteCbx.setItems(StoreCore.getAllColumnData(
									"clientes", "Cliente"));
							clienteCbx.setSelectedItem(null);
							excluidoFld.setDouble(0);
							gravadoFld.setDouble(0);
							totalFdl.setDouble(0);
							clienteCbx.requestFocus();

						} else
							dispose();
					} catch (Exception e1) {
						mainPanel.updateData("Error. Verifique los datos");
						ErrorLogLoader.addErrorEntry(e1);
						e1.printStackTrace();
					}
				}
			}

		});
		aceptarBtn.setToolTipText("Ingresar datos");
		setTitle("Ingreso Factura Manual");
		setVisible(true);
	}

}
