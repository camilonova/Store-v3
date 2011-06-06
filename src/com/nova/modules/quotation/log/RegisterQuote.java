package com.nova.modules.quotation.log;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.gui.StoreSession;
import com.nova.log.form.dat.FormRegister;
import com.nova.log.form.gui.Form;
import com.nova.log.form.log.FormPrint;

/**
 * Clase encargada del registro de una cotizacion en el sistema
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class RegisterQuote extends FormRegister {

	/**
	 * Registra la cotizacion y pregunta si desea imprimir
	 * <p>
	 * Creation date 27/05/2006 - 11:15:43 AM
	 * 
	 * @param form
	 *            Formato con datos a registrar
	 * 
	 * @since 1.0
	 */
	public RegisterQuote(Form form) {
		super(form);
		register();

		if (JOptionPane.showConfirmDialog(form,
				"Desea imprimir la cotizacion?", "Imprimir",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

			ArrayList<String> userFormData = new ArrayList<String>();
			ArrayList<String> rowData = StoreCore.getAllRowData("clientes",
					"Cliente", form.getFormUser());
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
		// Sin implementacion
	}

	@Override
	protected void register() {
		ArrayList<String> data = new ArrayList<String>();

		data.add(form.getConsecutiveNumber());
		data.add(form.getFormUser());
		data.add(form.getFormSQLDate());
		data.add(form.getExempt());
		data.add(form.getBase());
		data.add(form.getTax());
		data.add(form.getTotal());
		data.add(StoreSession.getUserName());

		try {
			StoreCore.registerFormMetaData("cotizacion", form
					.getConsecutiveNumber(), form.getTableModel().getTableData());
			StoreCore.newData("cotizacion", data);
			form.updateMainPanel("Cotizacion " + form.getConsecutiveNumber()
					+ " agregada");
		} catch (SQLException e) {
			form.updateMainPanel("Error. Verifique los datos");
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
	}

}
