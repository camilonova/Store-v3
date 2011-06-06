package com.nova.modules.provider.log;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.modules.provider.gui.ProviderMainPanel;

/**
 * Esta clase facilita la logica para editar la informacion de un proveedor
 * registrado en el sistema
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class EditProvider extends ProviderDataGUI {

	final String selectedID;

	/**
	 * Construye la interfaz de usuario y agrega funcionalidad de editar la
	 * informacion de un proveedor del sistema
	 * 
	 * @param mainPanel
	 *            Panel del modulo
	 * @since 1.0
	 */
	public EditProvider(final ProviderMainPanel mainPanel) {
		super();
		selectedID = mainPanel.getSelectedID();
		if (selectedID == null) {
			JOptionPane
					.showMessageDialog(null, "Debe seleccionar un proveedor");
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
		productsFld.setText(arrayList.get(8));
		contactNameFld.setText(arrayList.get(9));
		contactNumberFld.setText(arrayList.get(10));
		creditoFld.setDouble(Double.parseDouble(arrayList.get(11)));

		aceptarBtn.setToolTipText("Actualizar la informacion del proveedor");
		aceptarBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (isValidData()) {
					try {
						ArrayList<String> userData = getUserData(selectedID);
						StoreCore.updateData(mainPanel.getTableName(), userData);
						mainPanel.updateData("Proveedor actualizado correctamente");
						dispose();
					} catch (SQLException e1) {
						mainPanel.updateData("Error. Verifique los datos");
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
