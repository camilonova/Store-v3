package com.nova.modules.invoice.gui;

import java.util.ArrayList;

import javax.swing.JInternalFrame;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.nova.dat.db.StoreCore;
import com.nova.log.form.dat.ModuleForm;
import com.nova.log.form.gui.Form;
import com.nova.modules.ModuleBuilder;
import com.nova.modules.invoice.log.RegisterInvoice;

/**
 * Esta clase representa la tabla donde se muestran las facturas ingresadas al
 * sistema.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class InvoiceMainPanel extends ModuleForm {

	/**
	 * Constructor. Llama a la superclase para construir la interfaz y le da el
	 * tamaño a la ventana
	 * 
	 * @param moduleBuilder
	 *            Constructor del modulo
	 * @param internalFrame
	 *            Ventana interna del modulo
	 * @since 1.0
	 */
	public InvoiceMainPanel(ModuleBuilder moduleBuilder,
			JInternalFrame internalFrame) {
		super(moduleBuilder, internalFrame);
	}

	@Override
	protected void setColumnsProperties() {
		cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

		tableColumn = table.getColumn("Factura");
		tableColumn.setCellRenderer(cellRenderer);

		tableColumn = table.getColumn("Cliente");
		tableColumn.setPreferredWidth(200);

		tableColumn = table.getColumn("Exento");
		tableColumn.setCellRenderer(cellRenderer);

		tableColumn = table.getColumn("Gravado");
		tableColumn.setCellRenderer(cellRenderer);

		tableColumn = table.getColumn("Impuesto");
		tableColumn.setCellRenderer(cellRenderer);

		tableColumn = table.getColumn("Total");
		tableColumn.setCellRenderer(cellRenderer);

	}

	@Override
	public void registerData(Form form) {
		register = new RegisterInvoice(form);
	}

	@Override
	public ArrayList<String> getFormItems() {
		return StoreCore.getStockSaleItems();
	}

}
