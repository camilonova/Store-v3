package com.nova.modules.users.gui;

import java.awt.Dimension;

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
public class UsersMainPanel extends StoreTablePanel {

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
	public UsersMainPanel(ModuleBuilder usersBuilder,
			JInternalFrame internalFrame) {
		super(usersBuilder, internalFrame);
		setPreferredSize(new Dimension(590, 300));
	}

	@Override
	protected void setColumnsProperties() {
		cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

		tableColumn = table.getColumn("ID");
		tableColumn.setPreferredWidth(40);
		tableColumn.setCellRenderer(cellRenderer);

		tableColumn = table.getColumn("Usuario");
		tableColumn.setPreferredWidth(100);

		tableColumn = table.getColumn("Password");
		tableColumn.setPreferredWidth(250);

		tableColumn = table.getColumn("Nombre");
		tableColumn.setPreferredWidth(150);

		tableColumn = table.getColumn("UltimoAcceso");
		tableColumn.setPreferredWidth(150);
	}
}
