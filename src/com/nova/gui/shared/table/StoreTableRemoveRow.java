package com.nova.gui.shared.table;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import com.nova.dat.db.StoreCore;

/**
 * Esta clase elimina el registro seleccionado de la tabla.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class StoreTableRemoveRow extends JDialog {

	private String ID;

	private String tableName;

	/**
	 * Borra la fila seleccionada de la tabla, preguntando antes si esta seguro
	 * de hacerlo, si lo esta la elimina y actualiza los datos.
	 * 
	 * @param tablePanel
	 *            Panel de la tabla
	 * @since 1.0
	 */
	public StoreTableRemoveRow(StoreTablePanel tablePanel) {
		ID = tablePanel.getSelectedID();
		tableName = tablePanel.getTableName();

		if (ID == null)
			JOptionPane.showMessageDialog(tablePanel,
					"Debe seleccionar un registro!!!", "Error",
					JOptionPane.WARNING_MESSAGE);
		else {
			int seleccion = JOptionPane.showConfirmDialog(tablePanel,
					"Esta seguro de eliminar el registro " + ID, "Eliminar...",
					JOptionPane.YES_NO_OPTION);
			if (seleccion == JOptionPane.YES_OPTION) {
				String colName = StoreCore.getColumnNames(tableName).get(0);
				StoreCore.removeRow(tableName, colName, ID);
				tablePanel.updateData("Registro " + ID + " Eliminado!!!");
			}
		}
	}
}
