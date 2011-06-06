package com.nova.modules.invoice.log;

import javax.swing.JOptionPane;

import com.nova.log.form.gui.Form;
import com.nova.modules.invoice.gui.InvoiceMainPanel;

/**
 * Esta clase permite la visualizacion de una factura registrada en el sistema
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class SeeInvoice extends Form {

	/**
	 * Carga los datos de la factura seleccionada en el formato
	 * <p>
	 * Creation date 4/05/2006 - 08:22:24 AM
	 * 
	 * @param mainPanel
	 *            Panel principal
	 * 
	 * @since 1.0
	 */
	public SeeInvoice(InvoiceMainPanel mainPanel) {
		super(mainPanel);
		String selectedInvoice = mainPanel.getSelectedID();
		if (selectedInvoice == null) {
			JOptionPane.showMessageDialog(null, "Debe seleccionar una factura");
			return;
		}
		loadForm(mainPanel.getTableName(), selectedInvoice);

		setVisible(true);
	}

}
