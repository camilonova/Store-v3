package com.nova.modules.invoice.log;

import com.nova.log.form.gui.Form;
import com.nova.modules.invoice.gui.InvoiceMainPanel;

/**
 * Esta clase facilita la logica para el ingreso de una factura
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class AddInvoice extends Form {

	/**
	 * Da algunas propiedades de la ventana y la muestra
	 * <p>
	 * Creation date 20/04/2006 - 08:51:36 PM
	 * 
	 * @param mainPanel
	 *            Tabla de datos
	 * 
	 * @since 1.0
	 */
	public AddInvoice(InvoiceMainPanel mainPanel) {
		super(mainPanel);
		setVisible(true);
	}

}
