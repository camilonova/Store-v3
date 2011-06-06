package com.nova.modules.users.log;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.modules.users.gui.UsersMainPanel;

/**
 * Esta clase facilita la logica para la creacion de un nuevo usuario en el
 * sistema
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class AddUser extends UsersDataGUI {

	/**
	 * Construye la interfaz de usuario y agrega funcionalidad de agregar un
	 * nuevo usuario al sistema
	 * 
	 * @param mainPanel
	 *            Panel del modulo
	 * @since 1.0
	 */
	public AddUser(final UsersMainPanel mainPanel) {
		super();
		aceptarBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					StoreCore.newData(mainPanel.getTableName(), getUserData(
							StoreCore.getNextId(mainPanel.getTableName(), "ID"),
							false));
					mainPanel.updateData("Usuario creado correctamente");
					dispose();
				} catch (SQLException e1) {
					mainPanel.updateData("Error. Verifique los datos");
					ErrorLogLoader.addErrorEntry(e1);
					e1.printStackTrace();
				}
			}

		});
		aceptarBtn.setToolTipText("Ingresar datos y crear el usuario");
		setTitle("Creacion de nuevo usuario");
		setVisible(true);
	}

}
