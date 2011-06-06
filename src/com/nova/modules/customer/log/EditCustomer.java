package com.nova.modules.customer.log;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.modules.customer.gui.CustomerMainPanel;

/**
 * Esta clase facilita la logica para editar la informacion de un usuario
 * registrado en el sistema
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class EditCustomer extends CustomerDataGUI {

	final String selectedID;

	/**
	 * Construye la interfaz de usuario y agrega funcionalidad de editar la
	 * informacion de un usuario del sistema
	 * 
	 * @param mainPanel
	 *            Panel del modulo
	 * @since 1.0
	 */
	public EditCustomer(final CustomerMainPanel mainPanel) {
		super();
		selectedID = mainPanel.getSelectedID();
		if (selectedID == null) {
			JOptionPane.showMessageDialog(null, "Debe seleccionar un cliente");
			return;
		}

		ArrayList<String> arrayList = StoreCore.getAllRowData(mainPanel
				.getTableName(), "ID", selectedID);
		nombreFld.setText(arrayList.get(1));
		identificationFld.setText(arrayList.get(2));
		ciudadCbx.setSelectedItem(arrayList.get(3));
		telefonoFld.setText(arrayList.get(4));
		faxFld.setText(arrayList.get(5));
		movilFld.setText(arrayList.get(6));
		direccionFld.setText(arrayList.get(7));
		String credito = arrayList.get(8);
		if (credito != null) {
			nombreFld.setEnabled(false);
			creditoHabilitadoCbx.doClick();
			creditoSaldoFld.setDouble(Double.parseDouble(credito));
			calificacionFld.setText(arrayList.get(9));
			creditoMaxFld.setDouble(Double.parseDouble(arrayList.get(10)));
		}

		aceptarBtn.setToolTipText("Actualizar la informacion del cliente");
		aceptarBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (isValidData(true)) {
					try {
						ArrayList<String> userData = getUserData(selectedID);
						StoreCore.updateData(mainPanel.getTableName(), userData);
						mainPanel.updateData("Cliente actualizado correctamente");
						dispose();
					} catch (SQLException e1) {
						mainPanel.updateData("Error en la actualizacion. Verifique los datos");
						ErrorLogLoader.addErrorEntry(e1);
						e1.printStackTrace();
					}
				}
			}

		});

		setTitle("Editar informacion de " + nombreFld.getText());
		setVisible(true);
	}

}
