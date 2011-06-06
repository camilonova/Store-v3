package com.nova.modules.provider.log;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.modules.provider.gui.ProviderMainPanel;

/**
 * Esta clase facilita la logica para la creacion de un nuevo proveedor en el
 * sistema
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class AddProvider extends ProviderDataGUI {

	/**
	 * Construye la interfaz de usuario y agrega funcionalidad de agregar un
	 * nuevo proveedor al sistema
	 * 
	 * @param mainPanel
	 *            Panel del modulo
	 * @since 1.0
	 */
	public AddProvider(final ProviderMainPanel mainPanel) {
		super();
		aceptarBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (isValidData()) {
					try {
						String tableName = mainPanel.getTableName();
						StoreCore.newData(tableName, getUserData(StoreCore
								.getNextId(tableName, "ID")));
						mainPanel.updateData("Proveedor creado correctamente");
						dispose();
					} catch (SQLException e1) {
						mainPanel.updateData("Error. Verifique los datos");
						ErrorLogLoader.addErrorEntry(e1);
						e1.printStackTrace();
					}
				}
			}
		});
		aceptarBtn.setToolTipText("Ingresar datos y crear el proveedor");
		setTitle("Creacion de nuevo proveedor");
		setVisible(true);
	}

	/**
	 * Constructor creado para dar posibilidad de crear un proveedor rapidamente
	 * sin utilizar el listado principal
	 * 
	 * @param provider
	 *            Nombre del proveedor que se quiere agregar
	 * @since 1.0
	 */
	public AddProvider(String provider) {
		super();
		nombreFld.setText(provider);
		aceptarBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (isValidData()) {
					try {
						String tableName = "proveedores";
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
		aceptarBtn.setToolTipText("Ingresar datos y crear el proveedor");
		setTitle("Creacion de nuevo proveedor");
		setVisible(true);
	}
}
