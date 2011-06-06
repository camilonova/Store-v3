package com.nova.modules.kardex.dat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.rowset.WebRowSet;
import javax.swing.table.AbstractTableModel;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;

/**
 * Modelo de datos para la tabla de kardex, no utiliza la general debido a que
 * tiene que filtrar los datos dependiendo el codigo del articulo que se pasa
 * por parametro a updateDataTable();
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class KardexTableModel extends AbstractTableModel {

	protected ArrayList<String> header;

	protected WebRowSet data;

	/**
	 * Constructor de la clase.
	 * <p>
	 * Creation date 18/05/2006 - 12:29:32 PM
	 * 
	 * @since 1.0
	 */
	public KardexTableModel() {
		data = null;
		header = new ArrayList<String>();
		header.add("Fecha");
		header.add("Operacion");
		header.add("Tipo");
		header.add("Origen");
		header.add("Unidades");
		header.add("Costo");
		header.add("Total");
		header.add("SaldoUnidades");
		header.add("SaldoCosto");
		header.add("SaldoTotal");

		fireTableStructureChanged();
	}

	/**
	 * Actualiza los datos de la tabla, filtrando los datos con el parametro
	 * recibido
	 * <p>
	 * Creation date 18/05/2006 - 12:30:45 PM
	 * 
	 * @param barCode
	 *            Codigo del articulo a mostrar
	 * @since 1.0
	 */
	public void updateTableData(String barCode) {
		try {
			data = StoreCore.getRowSet();
			data.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
			data.setConcurrency(ResultSet.CONCUR_UPDATABLE);

			data.setCommand("SELECT `Fecha` , `Operacion` , `Tipo` , "
					+ "`Origen` , `Unidades` , `Costo` , `Total` , "
					+ "`SaldoUnidades` , `SaldoCosto` , `SaldoTotal` "
					+ "FROM `kardex` WHERE `Codigo` = '" + barCode + "' "
					+ "ORDER BY `Proceso` DESC");
			data.execute();
			fireTableDataChanged();
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
	}

	public int getRowCount() {
		if (data != null) {
			try {
				data.last();
				return data.getRow();
			} catch (SQLException e) {
				ErrorLogLoader.addErrorEntry(e);
				e.printStackTrace();
			}
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
