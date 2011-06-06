package com.nova.modules.purchase.log;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.gui.StoreSession;
import com.nova.log.form.dat.FormRegister;
import com.nova.log.form.gui.Form;
import com.nova.log.form.log.FormPrint;
import com.nova.modules.kardex.log.KardexDataManager;

/**
 * Clase encargada del registro de una compra en el sistema
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class RegisterPurchase extends FormRegister {

	private String purchaseNumber;

	/**
	 * Registra una compra en el sistema
	 * <p>
	 * Creation date 27/05/2006 - 02:08:39 PM
	 * 
	 * @param form
	 *            Formato con datos a registrar
	 * 
	 * @since 1.0
	 */
	public RegisterPurchase(Form form) {
		super(form);
		purchaseNumber = form.getConsecutiveNumber();

		register();
		if (JOptionPane.showConfirmDialog(form, "Desea imprimir la compra?",
				"Imprimir", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

			ArrayList<String> userFormData = new ArrayList<String>();
			ArrayList<String> rowData = StoreCore.getAllRowData("proveedores",
					"Proveedor", form.getFormUser());
			userFormData.add(rowData.get(2));
			userFormData.add(rowData.get(3));
			userFormData.add(rowData.get(4));
			userFormData.add(rowData.get(7));

			new FormPrint(form, userFormData);
		}

		form.reloadForm();
	}

	@Override
	protected void showGUI() {
		// Sin implementacion momentanea
	}

	@Override
	protected void register() {
		ArrayList<String> data = new ArrayList<String>();
		ArrayList<String[]> tableData = form.getTableModel().getTableData();

		data.add(purchaseNumber);
		data.add(form.getFormUser());
		data.add(form.getFormSQLDate());
		data.add(form.getExempt());
		data.add(form.getBase());
		data.add(form.getTax());
		data.add(form.getTotal());
		data.add(StoreSession.getUserName());

		// Kardex
		for (String[] row : tableData) {
			String barCode = StoreCore.getStockBarCode(row[1]);
			boolean b = KardexDataManager.buyStockItem(purchaseNumber, barCode,
					Integer.parseInt(row[0]), Double.parseDouble(row[2]));
			if (b == false)
				System.err.println("Error en articulo " + barCode);
		}
		try {
			// Elementos
			StoreCore.registerFormMetaData("compra", purchaseNumber, tableData);
			// Compra
			StoreCore.newData("compra", data);
			form.updateMainPanel("Compra " + purchaseNumber + " agregada");
		} catch (SQLException e) {
			form.updateMainPanel("Error. Verifique los datos");
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
	}
}
