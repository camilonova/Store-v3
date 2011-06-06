package com.nova.gui.shared.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.nova.dat.parameter.GlobalConstants;

/**
 * Esta clase representa la funcionalidad de ordenamiento de las columnas de la
 * tabla, usando para esto, cambios en la interfaz de las cabeceras de las
 * columnas y conexion a la base de datos.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
class StoreTableSorter {

	/**
	 * Representa el encambezamiento de la tabla
	 */
	JTableHeader tableHeader;

	/**
	 * Representa el modelo de la tabla
	 */
	private StoreTableModel model;

	/**
	 * Determina si ascienden o descienden los datos
	 */
	boolean ascendente = true;

	/**
	 * Determina la columna actualmente ordenada
	 */
	int columnaSeleccionada = -1;

	/**
	 * Determina que las columnas no se puedan reorganizar, implementa el
	 * ordenamiento basado en el indizado de las columnas.
	 * 
	 * @param table
	 *            Tabla donde se muestran los datos
	 * @since 1.0
	 */
	public StoreTableSorter(JTable table) {
		tableHeader = table.getTableHeader();
		model = (StoreTableModel) table.getModel();

		tableHeader.setReorderingAllowed(false);
		tableHeader.setToolTipText("Click para ordenar");

		for (int i = 0; i < model.getColumnCount(); i++)
			paintArrow(i);

		tableHeader.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTableHeader h = (JTableHeader) e.getSource();
				TableColumnModel columnModel = h.getColumnModel();
				int viewColumn = columnModel.getColumnIndexAtX(e.getX());
				int column = columnModel.getColumn(viewColumn).getModelIndex();

				updateData(column);
				paintArrow(column);
				columnaSeleccionada = column;
				if (ascendente)
					ascendente = false;
				else
					ascendente = true;
			}
		});
	}

	/**
	 * Actualiza los datos de la tabla
	 * 
	 * @param column
	 *            Columna a ordenar
	 */
	void updateData(int column) {
		model.changeUpdateData(model.getColumnName(column),
				ascendente ? GlobalConstants.ORDER_ASCENDANT
						: GlobalConstants.ORDER_DESCENDANT);
		model.updateTableData();

	}

	/**
	 * Metodo que dibuja una flecha dependiento el ordenamiento
	 * 
	 * @param columna
	 *            Columna a dibujar la flecha
	 * @since 1.0
	 */
	void paintArrow(int columna) {
		TableColumn tableColumn = tableHeader.getColumnModel().getColumn(
				columna);

		tableColumn.setHeaderRenderer(new TableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {

				Component c = tableHeader.getDefaultRenderer()
						.getTableCellRendererComponent(table, value,
								isSelected, hasFocus, row, column);
				if (c instanceof JLabel) {
					JLabel l = (JLabel) c;
					l.setHorizontalTextPosition(SwingConstants.LEFT);
					l.setIcon(getIcon(column, l.getFont().getSize()));
				}
				return c;
			}
		});
		tableHeader.repaint();
	}

	/**
	 * Metodo que retorna el icono para mostrar en la cabecera de la columna
	 * 
	 * @param column
	 *            Columna a retornar icono
	 * @param size
	 *            Tamaño del icono
	 * @return Icono a mostrar
	 */
	Icon getIcon(int column, int size) {
		if (column != columnaSeleccionada)
			return null;
		return new Arrow(!ascendente, size);
	}

	/**
	 * Metodo que retorna la columna actualmente ordenada
	 * 
	 * @return Indice de la columna seleccionada
	 */
	public int getSelectedColumnIndex() {
		return columnaSeleccionada;
	}

	/**
	 * Metodo que retorna la orientacion de la ordenacion
	 * 
	 * @return Ordenacion ascendente o descendente
	 */
	public String getOrientaton() {
		if (!ascendente || columnaSeleccionada == -1)
			return GlobalConstants.ORDER_ASCENDANT;
		return GlobalConstants.ORDER_DESCENDANT;
	}

	/**
	 * Clase que dibuja un triangulo.
	 * 
	 * @author java examples
	 * @author Camilo Nova
	 * @version 1.0
	 */
	private class Arrow implements Icon {

		/**
		 * Direccion de la flecha
		 */
		private boolean descending;

		/**
		 * Tamaño, depende del texto de la columna
		 */
		private int size;

		/**
		 * Recibe los parametros necesarios para dibujar la flecha
		 * 
		 * @param descending
		 *            Direccion
		 * @param size
		 *            Tamaño
		 */
		public Arrow(boolean descending, int size) {
			this.descending = descending;
			this.size = size;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
			Color color = c.getBackground();
			int dx = 3 * size / 4;
			int dy = descending ? dx : -dx;
			// Align icon (roughly) with font baseline.
			y = y + 5 * size / 6 + (descending ? -dy : 0);
			int shift = descending ? 1 : -1;
			g.translate(x, y);

			// Right diagonal.
			g.setColor(color.darker());
			g.drawLine(dx / 2, dy, 0, 0);
			g.drawLine(dx / 2, dy + shift, 0, shift);

			// Left diagonal.
			g.setColor(color.brighter());
			g.drawLine(dx / 2, dy, dx, 0);
			g.drawLine(dx / 2, dy + shift, dx, shift);

			// Horizontal line.
			if (descending)
				g.setColor(color.darker().darker());
			else
				g.setColor(color.brighter().brighter());

			g.drawLine(dx, 0, 0, 0);

			g.setColor(color);
			g.translate(-x, -y);
		}

		public int getIconWidth() {
			return size;
		}

		public int getIconHeight() {
			return size;
		}
	}
}