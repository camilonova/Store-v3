package com.nova.modules.users.log;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.modules.users.gui.UsersMainPanel;

/**
 * Esta clase facilita la logica para editar la informacion de un usuario
 * registrado en el sistema
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class EditUser extends UsersDataGUI {

	final String selectedID;

	/**
	 * Construye la interfaz de usuario y agrega funcionalidad de editar la
	 * informacion de un usuario del sistema
	 * 
	 * @param mainPanel
	 *            Panel del modulo
	 * @since 1.0
	 */
	public EditUser(final UsersMainPanel mainPanel) {
		super();
		selectedID = mainPanel.getSelectedID();
		if (selectedID == null) {
			JOptionPane.showMessageDialog(null, "Debe seleccionar un usuario");
			return;
		}

		String userName = StoreCore.getDataAt(mainPanel.getTableName(), "ID",
				selectedID, "Usuario");
		components.get(1).setEnabled(false);

		ArrayList<String> allRowData = StoreCore.getAllRowData(mainPanel
				.getTableName(), "ID", selectedID);
		allRowData.remove(4);
		allRowData.remove(0);

		for (int i = 0; i < components.size(); i++) {
			if (components.get(i) instanceof JTextField) {
				JTextField field = (JTextField) components.get(i);
				field.setText(allRowData.get(i));
			} else if (components.get(i) instanceof JRadioButton) {
				JRadioButton radioButton = (JRadioButton) components.get(i);
				radioButton.setSelected(allRowData.get(i).equals("Si"));
			}
		}

		aceptarBtn.setToolTipText("Actualizar la informacion del usuario");
		aceptarBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					StoreCore.updateData(mainPanel.getTableName(), getUserData(
							selectedID, true));
					mainPanel.updateData("Usuario actualizado correctamente");
					dispose();
				} catch (SQLException e1) {
					mainPanel.updateData("Error. Verifique los datos");
					ErrorLogLoader.addErrorEntry(e1);
					e1.printStackTrace();
				}
			}

		});

		setTitle("Editar informacion de " + userName);
		setVisible(true);
	}

}
