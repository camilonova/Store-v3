package com.nova.log.form.log;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.nova.dat.db.StoreCore;
import com.nova.log.form.dat.FormTableModel;
import com.nova.log.form.gui.Form;
import com.nova.modules.customer.log.AddCustomer;
import com.nova.modules.provider.log.AddProvider;
import com.nova.modules.purchase.gui.PurchaseMainPanel;

/**
 * Esta clase provee los metodos para ejecutar comandos sobre el formulario
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class FormCommand {

	private final Form form;

	private final FormTableModel tableModel;

	private JTable table;

	/**
	 * Almacena la instancia principal para la ejecucion de los comandos
	 * <p>
	 * Creation date 20/04/2006 - 07:34:06 PM
	 * 
	 * @param form
	 *            Formato
	 * 
	 * @since 1.0
	 */
	public FormCommand(Form form) {
		this.form = form;
		this.tableModel = form.getTableModel();
		this.table = form.getTable();
	}

	/**
	 * Registra el formulario
	 * <p>
	 * Creation date 20/04/2006 - 07:53:14 PM
	 * 
	 * @since 1.0
	 */
	public void register() {
		// Validamos el usuario del formato
		String formUser = form.getFormUser();
		if (form.getModuleForm() instanceof PurchaseMainPanel) {
			if (!StoreCore.isRegisteredData("proveedores", "Proveedor",
					formUser)) {
				int choice = JOptionPane
						.showConfirmDialog(form,
								"El proveedor que ingreso no existe.\nDesea ingresarlo al sistema?");
				if (choice == JOptionPane.YES_OPTION) {
					// Ingresa el proveedor al sistema
					new AddProvider(formUser);
				}
				if (choice == JOptionPane.CANCEL_OPTION) {
					// Retorna a cambiar el proveedor
					return;
				}
			}
		} else {
			if (!StoreCore.isRegisteredData("clientes", "Cliente", formUser)) {
				int choice = JOptionPane
						.showConfirmDialog(form,
								"El cliente que ingreso no existe.\nDesea ingresarlo al sistema?");
				if (choice == JOptionPane.YES_OPTION) {
					// Ingresa el cliente al sistema
					new AddCustomer(formUser);
				}
				if (choice == JOptionPane.CANCEL_OPTION) {
					// Retorna a cambiar el cliente
					return;
				}
			}
		}
		// Validamos que existan datos a registrar
		if (table.getRowCount() == 0)
			JOptionPane.showMessageDialog(form,
					"No existen datos para registrar");
		else if (JOptionPane.showConfirmDialog(form,
				"Esta seguro de registrar los datos en el sistema?",
				"Registrar datos", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			form.getModuleForm().registerData(form);
		}
	}

	/**
	 * Sube una posicion la fila seleccionada
	 * <p>
	 * Creation date 20/04/2006 - 07:53:31 PM
	 * 
	 * @since 1.0
	 */
	public void upRow() {
		int selectedRow = table.getSelectedRow();
		int selectedColumn = table.getSelectedColumn();
		if (tableModel.moveRowUp(selectedRow))
			form.setSelectedCell(selectedRow - 1, selectedColumn);
		table.requestFocus();
	}

	/**
	 * Baja una posicion la fila seleccionada
	 * <p>
	 * Creation date 20/04/2006 - 07:53:42 PM
	 * 
	 * @since 1.0
	 */
	public void downRow() {
		int selectedRow = table.getSelectedRow();
		int selectedColumn = table.getSelectedColumn();
		if (tableModel.moveRowDown(selectedRow))
			form.setSelectedCell(selectedRow + 1, selectedColumn);
		table.requestFocus();
	}

	/**
	 * Elimina la fila seleccionada
	 * <p>
	 * Creation date 20/04/2006 - 07:54:18 PM
	 * 
	 * @since 1.0
	 */
	public void delRow() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow != -1
				&& JOptionPane.showConfirmDialog(form,
						"Esta seguro de eliminar la fila seleccionada?",
						"Eliminar fila", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			tableModel.removeRow(selectedRow);
		}
	}

	/**
	 * Elimina todas las filas del formulario
	 * <p>
	 * Creation date 20/04/2006 - 07:54:42 PM
	 * 
	 * @since 1.0
	 */
	public void delAllRows() {
		if (JOptionPane.showConfirmDialog(form,
				"Esta seguro de eliminar todas las filas?",
				"Eliminar todos los datos", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			tableModel.removeAllRows();
		}
	}

	/**
	 * Sale del formulario, validando si existen datos sin registrar
	 * <p>
	 * Creation date 20/04/2006 - 07:54:31 PM
	 * 
	 * @since 1.0
	 */
	public void exitForm() {
		if (table.getRowCount() > 0
				&& JOptionPane.showConfirmDialog(form,
						"Existen datos en el formato. Desea descartarlos?",
						"Datos sin registrar", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
			return;
		form.dispose();
	}

	/**
	 * Imprime el formato
	 * <p>
	 * Creation date 4/05/2006 - 10:28:34 AM
	 * 
	 * @since 1.0
	 */
	public void print() {
		String formUser = form.getFormUser();
		if (StoreCore.isRegisteredData("clientes", "Cliente", formUser)) {
			ArrayList<String> userFormData = new ArrayList<String>();
			ArrayList<String> rowData = StoreCore.getAllRowData("clientes",
					"Cliente", form.getFormUser());
			userFormData.add(rowData.get(2));
			userFormData.add(rowData.get(3));
			userFormData.add(rowData.get(4));
			userFormData.add(rowData.get(7));

			new FormPrint(form, userFormData);
		} else if (StoreCore.isRegisteredData("clientes", "Cliente", formUser)) {
			ArrayList<String> userFormData = new ArrayList<String>();
			ArrayList<String> rowData = StoreCore.getAllRowData("proveedores",
					"Proveedor", form.getFormUser());
			userFormData.add(rowData.get(2));
			userFormData.add(rowData.get(3));
			userFormData.add(rowData.get(4));
			userFormData.add(rowData.get(7));

			new FormPrint(form, userFormData);
		}
	}

	/**
	 * Copia los datos de la tabla en un formato nuevo
	 * <p>
	 * Creation date 4/05/2006 - 10:28:52 AM
	 * 
	 * @since 1.0
	 */
	public void copyDataInNewForm() {
		form.transferSeeToAdd();
	}

	/**
	 * Sale del formato sin validar los datos para registrar
	 * <p>
	 * Creation date 4/05/2006 - 10:14:06 AM
	 * 
	 * @since 1.0
	 */
	public void exitWithOutSave() {
		form.dispose();
	}
}
