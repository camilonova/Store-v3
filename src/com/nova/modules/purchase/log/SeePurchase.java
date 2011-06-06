package com.nova.modules.purchase.log;

import javax.swing.JOptionPane;

import com.nova.log.form.gui.Form;
import com.nova.modules.purchase.gui.PurchaseMainPanel;

/**
 * Esta clase permite la visualizacion de una compra registrada en el sistema
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class SeePurchase extends Form {

	/**
	 * Carga los datos de la compra seleccionada en el formato
	 * <p>
	 * Creation date 4/05/2006 - 08:22:24 AM
	 * 
	 * @param mainPanel
	 *            Panel principal
	 * 
	 * @since 1.0
	 */
	public SeePurchase(PurchaseMainPanel mainPanel) {
		super(mainPanel);
		String selectedPurchase = mainPanel.getSelectedID();
		if (selectedPurchase == null) {
			JOptionPane.showMessageDialog(null, "Debe seleccionar una compra");
			return;
		}
		loadForm(mainPanel.getTableName(), selectedPurchase);

		setVisible(true);
	}

}
