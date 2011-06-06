package com.nova.modules.stock.gui;

import javax.swing.JInternalFrame;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.nova.gui.shared.table.StoreTablePanel;
import com.nova.modules.ModuleBuilder;

/**
 * Esta clase representa el panel central del modulo donde se muestra en una
 * tabla los datos de los usuarios registrados en el sistema
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class StockMainPanel extends StoreTablePanel {

	/**
	 * Constructor. Llama a la superclase para construir la interfaz.
	 * 
	 * @param usersBuilder
	 *            Constructor del modulo
	 * @param internalFrame
	 *            Ventana interna del modulo
	 * @since 1.0
	 */
	public StockMainPanel(ModuleBuilder usersBuilder,
			JInternalFrame internalFrame) {
		super(usersBuilder, internalFrame);
	}

	@Override
	protected void setColumnsProperties() {
		cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

		tableColumn = table.getColumn("Codigo");
		tableColumn.setCellRenderer(cellRenderer);
		tableColumn.setPreferredWidth(120);

		tableColumn = table.getColumn("Categoria");
		tableColumn.setPreferredWidth(100);

		tableColumn = table.getColumn("Cantidad");
		tableColumn.setCellRenderer(cellRenderer);

		tableColumn = table.getColumn("Minimo");
		tableColumn.setCellRenderer(cellRenderer);

		tableColumn = table.getColumn("Descripcion");
		tableColumn.setPreferredWidth(400);

		tableColumn = table.getColumn("Marca");
		tableColumn.setPreferredWidth(120);

		tableColumn = table.getColumn("Impuesto");
		tableColumn.setCellRenderer(cellRenderer);

		tableColumn = table.getColumn("PrecioPublico");
		tableColumn.setCellRenderer(cellRenderer);
		tableColumn.setPreferredWidth(100);

		tableColumn = table.getColumn("Proveedor");
		tableColumn.setPreferredWidth(200);

		tableColumn = table.getColumn("Costo");
		tableColumn.setCellRenderer(cellRenderer);
		tableColumn.setPreferredWidth(100);

		tableColumn = table.getColumn("Ubicacion");
		tableColumn.setPreferredWidth(100);

		tableColumn = table.getColumn("Imagen");
		tableColumn.setPreferredWidth(200);

		tableColumn = table.getColumn("UltimaModificacion");
		tableColumn.setPreferredWidth(150);

	}
}
