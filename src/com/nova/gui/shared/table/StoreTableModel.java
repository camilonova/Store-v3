package com.nova.gui.shared.table;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.rowset.WebRowSet;
import javax.swing.table.AbstractTableModel;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;

/**
 * Modelo de la tabla, con lo cual se convierte en el patron a seguir de la
 * tabla, si se quisiera modificar alguna caracteristica de la tabla, se debe
 * hacer desde aqui.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class StoreTableModel extends AbstractTableModel {

	protected ArrayList<String> header;

	protected WebRowSet data;

	private final String tableName;

	private String orderColumn;

	private String orderType;

	/**
	 * Inicializa el modelo con los datos pasados por parametro
	 * 
	 * @param tableName
	 *            Nombre de la tabla a leer
	 * @param orderColumn
	 *            Columna de la tabla por la cual ordenar los datos
	 * @param orderType
	 *            GlobalConstants.ORDER_ASCENDANT o
	 *            GlobalConstants.ORDER_DESCENDANT
	 * @since 1.0
	 */
	public StoreTableModel(String tableName, String orderColumn,
			String orderType) {
		this.tableName = tableName;
		this.orderColumn = orderColumn;
		this.orderType = orderType;
		header = StoreCore.getColumnNames(tableName);
		updateTableData();
	}

	/**
	 * Cambia los parametros de orden de los datos de la tabla
	 * 
	 * @param orderColumn
	 *            Columna de la tabla por la cual ordenar los datos
	 * @param orderType
	 *            GlobalConstants.ORDER_ASCENDANT o
	 *            GlobalConstants.ORDER_DESCENDANT
	 * @since 1.0
	 */
	@SuppressWarnings("hiding")
	public void changeUpdateData(String orderColumn, String orderType) {
		this.orderColumn = orderColumn;
		this.orderType = orderType;

	}

	/**
	 * Actualiza los datos de la tabla utilizando los datos de actualizacion. Si
	 * se desean cambiar dichos datos es necesario llamar a
	 * <i>changeUpdateData()</i>.
	 * 
	 * @since 1.0
	 */
	public void updateTableData() {
		data = StoreCore.getTableData(tableName, orderColumn, orderType);
		fireTableDataChanged();
	}

	public int getRowCount() {
		try {
			data.last();
			return data.getRow();
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		return 0;
	}

	public int getColumnCount() {
		return header.size();
	}

	@Override
	public String getColumnName(int column) {
		return header.get(column);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			data.absolute(rowIndex + 1);
			return data.getString(columnIndex + 1);
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		return null;
	}
}
