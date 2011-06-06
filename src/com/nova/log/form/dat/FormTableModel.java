package com.nova.log.form.dat;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.nova.log.form.gui.Form;
import com.nova.log.math.StoreMath;
import com.nova.modules.purchase.gui.PurchaseMainPanel;
import com.nova.modules.stock.log.StockItem;

/**
 * Representa el modelo de la tabla del formulario
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class FormTableModel extends AbstractTableModel {

	ArrayList<String> headers;

	ArrayList<String[]> data;

	/**
	 * Instancia los tipos de datos necesarios para la tabla, incluyendo los
	 * encabezados
	 * <p>
	 * Creation date 23/04/2006 - 05:22:10 PM
	 * 
	 * @param form
	 *            Formato
	 * 
	 * @since 1.0
	 */
	public FormTableModel(Form form) {
		headers = new ArrayList<String>();
		data = new ArrayList<String[]>();

		headers.add("Cantidad");
		headers.add("Descripcion");
		if (form.getModuleForm() instanceof PurchaseMainPanel)
			headers.add("Costo");
		else
			headers.add("Precio");
		headers.add("Base");
		headers.add("Impuesto");
		headers.add("Total");

		fireTableStructureChanged();
	}

	public int getRowCount() {
		return data.size();
	}

	public int getColumnCount() {
		return headers.size();
	}

	@Override
	public String getColumnName(int column) {
		return headers.get(column);
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == 0 || columnIndex == 2)
			return true;
		return false;
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		String[] rowData = data.get(rowIndex);
		StockItem stockItem;
		try {
			stockItem = new StockItem(rowData[1], StockItem.DESCRIPTION_BRAND);
		} catch (Exception e1) {
			stockItem = null;
		}

		String quantity = rowData[0];
		String description = rowData[1];
		String price = rowData[2];
		String base = rowData[3];
		String taxValue = rowData[4];
		String total = rowData[5];

		if (columnIndex == 0) {
			quantity = (String) aValue;
		} else if (columnIndex == 2) {
			price = (String) aValue;
		}

		// Verificamos si los datos son validos
		if (quantity != null && price != null && stockItem != null)
			rowData = calculateTotals(stockItem, quantity, description, price);
		else {
			rowData[0] = quantity;
			rowData[1] = description;
			rowData[2] = price;
			rowData[3] = base;
			rowData[4] = taxValue;
			rowData[5] = total;
		}

		data.set(rowIndex, rowData);
		fireTableDataChanged();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return data.get(rowIndex)[columnIndex];
	}

	/**
	 * Calcula los totales en la tabla
	 * <p>
	 * Creation date 2/05/2006 - 10:32:15 PM
	 * 
	 * @param stockItem
	 *            Articulo del stock a calcular
	 * @param quantity
	 *            Cantidad
	 * @param description
	 *            Descripcion y marca
	 * @param price
	 *            Precio
	 * @return Array con los datos para ingresar a la tabla
	 * @since 1.0
	 */
	public String[] calculateTotals(StockItem stockItem, String quantity,
			String description, String price) {
		String[] rowData = new String[headers.size()];

		String base = null;
		String taxValue = null;
		String total = null;

		// Subtotal
		double precio = StoreMath.multiply(Double.parseDouble(price), Double
				.parseDouble(quantity), true);
		double tax = Double.parseDouble(stockItem.getTax()) + 1;

		// Impuesto
		if (tax > 0) {
			// Si el articulo tiene impuesto
			double precioCalculado = StoreMath.divide(precio, tax, true);
			base = StoreMath.parseDouble(precioCalculado);
			taxValue = StoreMath.parseDouble(StoreMath.subtract(precio,
					precioCalculado, true));
		} else {
			// Si no lo tiene
			base = StoreMath.parseDouble(precio);
			taxValue = "0";
		}

		// Total
		total = StoreMath.parseDouble(StoreMath.add(
				StoreMath.parseString(base), StoreMath.parseString(taxValue),
				true));

		rowData[0] = quantity;
		rowData[1] = description;
		rowData[2] = price;
		rowData[3] = base;
		rowData[4] = taxValue;
		rowData[5] = total;

		return rowData;
	}

	/**
	 * Inserta los datos pasados por parametro al final de la tabla
	 * <p>
	 * Creation date 2/05/2006 - 10:01:52 PM
	 * 
	 * @param rowData
	 *            Datos a insertar al final de la tabla
	 * @since 1.0
	 */
	public void insertRow(String[] rowData) {
		data.add(rowData);
		fireTableDataChanged();
	}

	/**
	 * Elimina de la tabla la fila pasada por parametro
	 * <p>
	 * Creation date 23/04/2006 - 05:21:19 PM
	 * 
	 * @param rowIndex
	 *            Fila a eliminar
	 * @since 1.0
	 */
	public void removeRow(int rowIndex) {
		data.remove(rowIndex);
		fireTableDataChanged();
	}

	/**
	 * Elimina todas las filas de la tabla
	 * <p>
	 * Creation date 23/04/2006 - 05:21:43 PM
	 * 
	 * @since 1.0
	 */
	public void removeAllRows() {
		data = new ArrayList<String[]>();
		fireTableDataChanged();
	}

	/**
	 * Mueve una posicion hacia arriba la fila indicada
	 * <p>
	 * Creation date 23/04/2006 - 05:20:48 PM
	 * 
	 * @param rowIndex
	 *            Fila a mover arriba
	 * @return True si fue posible la operacion
	 * @since 1.0
	 */
	public boolean moveRowUp(int rowIndex) {
		if (rowIndex > 0) {
			String[] dat = data.remove(rowIndex);
			data.add(rowIndex - 1, dat);
			fireTableDataChanged();
			return true;
		}
		return false;
	}

	/**
	 * Mueve una posicion hacia abajo la fila indicada
	 * <p>
	 * Creation date 23/04/2006 - 05:20:06 PM
	 * 
	 * @param rowIndex
	 *            Fila a mover abajo
	 * @return True si fue posible la operacion
	 * @since 1.0
	 */
	public boolean moveRowDown(int rowIndex) {
		if (rowIndex < data.size() - 1) {
			String[] dat = data.remove(rowIndex);
			data.add(rowIndex + 1, dat);
			fireTableDataChanged();
			return true;
		}
		return false;
	}

	/**
	 * Retorna los datos de la tabla contenida en el formato.
	 * <p>
	 * Creation date 23/04/2006 - 05:19:32 PM
	 * 
	 * @return Datos de la tabla
	 * @since 1.0
	 */
	public ArrayList<String[]> getTableData() {
		return data;
	}

	/**
	 * Determina los datos de la tabla
	 * <p>
	 * Creation date 10/05/2006 - 04:10:10 PM
	 * 
	 * @param newData
	 *            Datos nuevos a cargar en la tabla
	 * @since 1.0
	 */
	public void setTableData(ArrayList<String[]> newData) {
		data = newData;
		fireTableDataChanged();
	}

}
