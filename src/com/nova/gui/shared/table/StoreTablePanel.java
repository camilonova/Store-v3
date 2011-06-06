package com.nova.gui.shared.table;

import java.text.DateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.sql.rowset.WebRowSet;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.modules.ModuleBuilder;

/**
 * Esta clase representa el panel donde se muestra la tabla con los datos
 * indicados en el constructor de la clase.
 * <p>
 * Es una mejora del sistema de paneles de la version anterior, en esta clase se
 * fusiona la funcionalidad de los paneles y se pone a disposicion de todos los
 * modulos de la aplicacion.
 * <p>
 * Se ha implementado la actualizacion periodica de la informacion mostrada
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public abstract class StoreTablePanel extends JScrollPane {

	protected JTable table;

	protected StoreTableModel model;

	protected DefaultTableCellRenderer cellRenderer;

	protected TableColumn tableColumn;

	protected StoreTableSorter sorter;

	private final ModuleBuilder moduleBuilder;

	private final String tableName;

	private final String orderColumn;

	private final String orderType;

	/**
	 * Construye la interfaz de la tabla y le agrega sus propiedades
	 * <p>
	 * Creation date 31/03/2006 - 08:03:03 AM
	 * 
	 * @param moduleBuilder
	 *            Modulo constructor
	 * @param internalFrame
	 *            Ventana interna del modulo
	 * 
	 * @since 1.0
	 */
	public StoreTablePanel(ModuleBuilder moduleBuilder,
			JInternalFrame internalFrame) {
		this.moduleBuilder = moduleBuilder;
		this.tableName = moduleBuilder.getRelatedTableName();
		this.orderColumn = moduleBuilder.getOrderColumn();
		this.orderType = moduleBuilder.getOrderType();

		model = new StoreTableModel(tableName, orderColumn, orderType);
		table = new JTable(model);
		sorter = new StoreTableSorter(table);

		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		installUpdateDaemon(internalFrame);
		setColumnsProperties();
		setViewportView(table);
	}

	/**
	 * Instala en la ventana recibida por parametro un demonio de actualizacion
	 * de datos
	 * <p>
	 * Creation date 10/05/2006 - 10:49:08 AM
	 * 
	 * @param internalFrame
	 *            Ventana interna a instalar el demonio
	 * @since 1.0
	 */
	private void installUpdateDaemon(JInternalFrame internalFrame) {
		if (internalFrame == null)
			return;

		int updateTimeRate;
		try {
			updateTimeRate = 1000 * Integer.parseInt(StoreCore
					.getProperty("UPDATE_RATE_TIME"));
		} catch (Exception e1) {
			ErrorLogLoader.addErrorEntry(e1);
			e1.printStackTrace();
			return;
		}
		final Timer timer = new Timer(tableName + " update");
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				updateData("Datos Actualizados - "
						+ DateFormat.getTimeInstance().format(
								new Date(System.currentTimeMillis()))
						+ " - Filas leidas " + table.getRowCount());
			}

		}, updateTimeRate, updateTimeRate);

		internalFrame.addInternalFrameListener(new InternalFrameAdapter() {

			@Override
			@SuppressWarnings("synthetic-access")
			public void internalFrameClosing(InternalFrameEvent e) {
				timer.cancel();
				super.internalFrameClosed(e);
			}

		});
	}

	/**
	 * Da las propiedades de alineacion y tamaño a las columnas
	 * <p>
	 * Creation date 31/03/2006 - 07:58:48 AM
	 * 
	 * @since 1.0
	 */
	protected abstract void setColumnsProperties();

	/**
	 * Actualiza los datos de la tabla y muestra la cadena recibida por
	 * parametro
	 * <p>
	 * Creation date 31/03/2006 - 07:59:44 AM
	 * 
	 * @param message
	 *            Mensaje a mostrar, puede ser null
	 * @since 1.0
	 */
	public void updateData(String message) {
		int row = table.getSelectedRow();
		int col = table.getSelectedColumn();

		model.updateTableData();
		table.repaint();
		setSelectedCell(row, col);

		if (message == null)
			setText("Datos Cargados, Leidos "
					+ model.getRowCount() + " registros.");
		else
			setText(message);
	}

	/**
	 * Retorna el valor de la primera columna de la fila seleccionada
	 * <p>
	 * Creation date 31/03/2006 - 08:00:26 AM
	 * 
	 * @return Valor de la primera columna, null si no hay columna seleccionada
	 * @since 1.0
	 */
	public String getSelectedID() {
		if (table.getSelectedRow() != -1)
			return (String) model.getValueAt(table.getSelectedRow(), 0);
		return null;
	}

	/**
	 * Retorna el nombre de la tabla en la base de datos mostrada
	 * <p>
	 * Creation date 31/03/2006 - 08:01:40 AM
	 * 
	 * @return Nombre de la tabla en la base de datos
	 * @since 1.0
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * Selecciona la celda indicada por los parametros
	 * <p>
	 * Creation date 31/03/2006 - 08:02:16 AM
	 * 
	 * @param row
	 *            Fila a seleccionar
	 * @param col
	 *            Columna a seleccionar
	 * @since 1.0
	 */
	public void setSelectedCell(int row, int col) {
		if (row > -1 && row < table.getRowCount() && col > -1
				&& col < table.getColumnCount()) {
			table.setColumnSelectionInterval(col, col);
			table.setRowSelectionInterval(row, row);
			table.scrollRectToVisible(table.getCellRect(row, col, true));
		}
	}

	/**
	 * Retorna el conjunto de datos mostrados por la tabla
	 * <p>
	 * Creation date 10/05/2006 - 12:10:28 PM
	 * 
	 * @return Datos de la tabla
	 * @since 1.0
	 */
	public WebRowSet getTableResultSet() {
		return model.data;
	}

	/**
	 * Retorna la fila seleccionada en la tabla
	 * <p>
	 * Creation date 10/05/2006 - 12:10:52 PM
	 * 
	 * @return Fila seleccionada, -1 si no hay seleccion
	 * @since 1.0
	 */
	public int getSelectedRow() {
		return table.getSelectedRow();
	}
	
	/**
	 * Determina el texto a mostrar en la barra de estado
	 * <p>Creation date 8/06/2006 - 08:53:29 AM
	 *
	 * @param message		Mensaje a mostrar
	 * @since 1.0
	 */
	public void setText(String message) {
		moduleBuilder.setStatusBarText(message);
	}
	
}