package com.nova.modules.manualInvoice.gui;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.nova.gui.shared.table.StoreTablePanel;
import com.nova.modules.ManualInvoice;
import com.nova.modules.ModuleBuilder;

/**
 * Esta clase representa el panel donde se muestran los datos de las facturas
 * manuales ingresadas al sistema.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class ManualInvoiceMainPanel extends StoreTablePanel {

	/**
	 * Constructor. Llama a la superclase para construir la interfaz y le da el
	 * tamaño a la ventana
	 * 
	 * @param moduleBuilder
	 *            Constructor del modulo
	 * @param manualInvoice
	 *            Ventana interna del modulo
	 * @since 1.0
	 */
	public ManualInvoiceMainPanel(ModuleBuilder moduleBuilder,
			ManualInvoice manualInvoice) {
		super(moduleBuilder, manualInvoice);
	}

	@Override
	protected void setColumnsProperties() {
		cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

		tableColumn = table.getColumn("Cliente");
		tableColumn.setPreferredWidth(320);

		tableColumn = table.getColumn("Exento");
		tableColumn.setCellRenderer(cellRenderer);
		tableColumn.setPreferredWidth(100);

		tableColumn = table.getColumn("Gravado");
		tableColumn.setCellRenderer(cellRenderer);
		tableColumn.setPreferredWidth(100);

		tableColumn = table.getColumn("Impuesto");
		tableColumn.setCellRenderer(cellRenderer);
		tableColumn.setPreferredWidth(100);

		tableColumn = table.getColumn("Total");
		tableColumn.setCellRenderer(cellRenderer);
		tableColumn.setPreferredWidth(100);

		tableColumn = table.getColumn("FechaCancelacion");
		tableColumn.setCellRenderer(cellRenderer);
		tableColumn.setPreferredWidth(120);

		tableColumn = table.getColumn("ValorCancelado");
		tableColumn.setCellRenderer(cellRenderer);
		tableColumn.setPreferredWidth(100);

		tableColumn = table.getColumn("UltimaModificacion");
		tableColumn.setCellRenderer(cellRenderer);
		tableColumn.setPreferredWidth(150);
	}
}
