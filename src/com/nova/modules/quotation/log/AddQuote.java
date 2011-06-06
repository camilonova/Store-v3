package com.nova.modules.quotation.log;

import com.nova.log.form.gui.Form;
import com.nova.modules.quotation.gui.QuoteMainPanel;

/**
 * Esta clase facilita la logica para el ingreso de una cotizacion
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class AddQuote extends Form {

	/**
	 * Muestra el formulario para agregar una cotizacion
	 * <p>
	 * Creation date 20/04/2006 - 08:51:36 PM
	 * 
	 * @param mainPanel
	 *            Panel principal
	 * 
	 * @since 1.0
	 */
	public AddQuote(QuoteMainPanel mainPanel) {
		super(mainPanel);
		setVisible(true);
	}

}
