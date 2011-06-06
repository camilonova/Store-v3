package com.nova.modules.customer.gui;

import javax.swing.JInternalFrame;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.nova.gui.shared.table.StoreTablePanel;
import com.nova.modules.ModuleBuilder;

/**
 * Esta clase representa el panel central del modulo donde se muestra en una
 * tabla los datos de los clientes registrados en el sistema
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class CustomerMainPanel extends StoreTablePanel {

	/**
	 * Constructor. Llama a la superclase para construir la interfaz y le da el
	 * tamaño a la ventana
	 * 
	 * @param usersBuilder
	 *            Constructor del modulo
	 * @param internalFrame
	 *            Ventana interna del modulo
	 * @since 1.0
	 */
	public CustomerMainPanel(ModuleBuilder usersBuilder,
			JInternalFrame internalFrame) {
		super(usersBuilder, internalFrame);
	}

	@Override
	protected void setColumnsProperties() {
		cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

		tableColumn = table.getColumn("ID");
		tableColumn.setPreferredWidth(30);
		tableColumn.setCellRenderer(cellRenderer);

		tableColumn = table.getColumn("Cliente");
		tableColumn.setPreferredWidth(250);

		tableColumn = table.getColumn("Identificacion");
		tableColumn.setPreferredWidth(100);

		tableColumn = table.getColumn("Ciudad");
		tableColumn.setPreferredWidth(80);

		tableColumn = table.getColumn("Direccion");
		tableColumn.setPreferredWidth(250);

		tableColumn = table.getColumn("Credito");
		tableColumn.setCellRenderer(cellRenderer);

		tableColumn = table.getColumn("Calificacion");
		tableColumn.setCellRenderer(cellRenderer);

		tableColumn = table.getColumn("CreditoMaximo");
		tableColumn.setCellRenderer(cellRenderer);

		tableColumn = table.getColumn("UltimaModificacion");
		tableColumn.setPreferredWidth(150);
	}

}
