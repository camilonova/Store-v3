package com.nova.modules.customer.log;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.modules.customer.gui.CustomerMainPanel;

/**
 * Esta clase facilita la logica para la creacion de un nuevo cliente en el
 * sistema
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class AddCustomer extends CustomerDataGUI {

	/**
	 * Construye la interfaz de usuario y agrega funcionalidad de agregar un
	 * nuevo cliente al sistema
	 * 
	 * @param mainPanel
	 *            Panel del modulo
	 * @since 1.0
	 */
	public AddCustomer(final CustomerMainPanel mainPanel) {
		super();
		aceptarBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (isValidData(false)) {
					try {
						String tableName = mainPanel.getTableName();
						StoreCore.newData(tableName, getUserData(StoreCore
								.getNextId(tableName, "ID")));
						mainPanel.updateData("Cliente creado correctamente");
						dispose();
					} catch (SQLException e1) {
						mainPanel.updateData("Error en los datos. Verifique los datos nuevamente.");
						ErrorLogLoader.addErrorEntry(e1);
						e1.printStackTrace();
					}
				}
			}
		});
		aceptarBtn.setToolTipText("Ingresar datos y crear el cliente");
		setTitle("Creacion de nuevo cliente");
		setVisible(true);
	}

	/**
	 * Constructor creado para dar posibilidad de crear un cliente rapidamente
	 * sin utilizar el listado principal
	 * 
	 * @param customer
	 *            Nombre del cliente que se quiere agregar
	 * @since 1.0
	 */
	public AddCustomer(String customer) {
		super();
		nombreFld.setText(customer);
		aceptarBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (isValidData(false)) {
					try {
						String tableName = "clientes";
						StoreCore.newData(tableName, getUserData(StoreCore
								.getNextId(tableName, "ID")));
						dispose();
					} catch (SQLException e1) {
						ErrorLogLoader.addErrorEntry(e1);
						e1.printStackTrace();
					}
				}
			}

		});
		aceptarBtn.setToolTipText("Ingresar datos y crear el cliente");
		setTitle("Creacion rapida de nuevo cliente");
		setVisible(true);
	}
}
