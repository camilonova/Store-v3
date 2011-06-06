package com.nova.modules.quotation.log;

import javax.swing.JOptionPane;

import com.nova.log.form.gui.Form;
import com.nova.modules.quotation.gui.QuoteMainPanel;

/**
 * Esta clase permite la visualizacion de una cotizacion en el sistema
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class SeeQuote extends Form {

	/**
	 * Carga los datos de la cotizacion seleccionada en el formato
	 * <p>
	 * Creation date 4/05/2006 - 08:22:24 AM
	 * 
	 * @param mainPanel
	 *            Panel principal
	 * 
	 * @since 1.0
	 */
	public SeeQuote(QuoteMainPanel mainPanel) {
		super(mainPanel);
		String selectedQuote = mainPanel.getSelectedID();
		if (selectedQuote == null) {
			JOptionPane.showMessageDialog(null,
					"Debe seleccionar una cotizacion");
			return;
		}
		loadForm(mainPanel.getTableName(), selectedQuote);

		setVisible(true);
	}

}
