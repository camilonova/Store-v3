package com.nova.log.form.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.DefaultCellEditor;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import com.nova.log.form.dat.FormTableModel;
import com.nova.log.input.validator.DoubleValidatorField;
import com.nova.log.input.validator.NumberValidatorField;
import com.nova.modules.purchase.gui.PurchaseMainPanel;

/**
 * Representa la tabla de datos del formato.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class FormTableDataPanel extends JScrollPane {

	JTable table;

	FormTableModel model;

	DefaultTableCellRenderer cellRenderer;

	TableColumn tableColumn;

	/**
	 * Crea el panel con la tabla de datos
	 * <p>
	 * Creation date 20/04/2006 - 09:56:30 PM
	 * 
	 * @param form
	 *            Formato
	 * 
	 * @since 1.0
	 */
	public FormTableDataPanel(final Form form) {
		model = new FormTableModel(form);
		table = new JTable(model);

		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setSurrendersFocusOnKeystroke(true);

		model.addTableModelListener(new TableModelListener() {

			public void tableChanged(TableModelEvent e) {
				ArrayList<String[]> data = model.getTableData();
				// Actualizamos el total del formato
				form.updateTotalPanel(data);
			}

		});

		cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

		tableColumn = table.getColumn("Cantidad");
		tableColumn.setCellRenderer(cellRenderer);
		tableColumn.setPreferredWidth(60);
		tableColumn.setCellEditor(new DefaultCellEditor(
				new NumberValidatorField()));

		tableColumn = table.getColumn("Descripcion");
		tableColumn.setPreferredWidth(300);

		if (form.getModuleForm() instanceof PurchaseMainPanel)
			tableColumn = table.getColumn("Costo");
		else
			tableColumn = table.getColumn("Precio");

		tableColumn.setCellRenderer(cellRenderer);
		tableColumn.setPreferredWidth(90);
		tableColumn.setCellEditor(new DefaultCellEditor(
				new DoubleValidatorField()));

		cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setForeground(Color.LIGHT_GRAY);
		cellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

		tableColumn = table.getColumn("Base");
		tableColumn.setCellRenderer(cellRenderer);
		tableColumn.setPreferredWidth(100);

		tableColumn = table.getColumn("Impuesto");
		tableColumn.setCellRenderer(cellRenderer);
		tableColumn.setPreferredWidth(95);

		cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setForeground(form.getFormColor());
		cellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

		tableColumn = table.getColumn("Total");
		tableColumn.setCellRenderer(cellRenderer);
		tableColumn.setPreferredWidth(120);

		setViewportView(table);
		setPreferredSize(new Dimension(790, 300));
	}

	/**
	 * Determina la celda que debe estar seleccionada
	 * <p>
	 * Creation date 20/04/2006 - 09:56:00 PM
	 * 
	 * @param row
	 *            Fila a seleccionar
	 * @param col
	 *            Columna a seleccionar
	 * @since 1.0
	 */
	public void setSelectedCell(int row, int col) {
		if (row > -1 && col > -1) {
			table.setColumnSelectionInterval(col, col);
			table.setRowSelectionInterval(row, row);
			table.scrollRectToVisible(table.getCellRect(row, col, true));
		}
	}

	/**
	 * Determina el estado del panel
	 * <p>
	 * Creation date 4/05/2006 - 10:09:26 AM
	 * 
	 * @param enable
	 *            True para habilitar el panel, false de lo contrario
	 * @since 1.0
	 */
	void setPanelEnabled(boolean enable) {
		table.setEnabled(enable);
	}
}
