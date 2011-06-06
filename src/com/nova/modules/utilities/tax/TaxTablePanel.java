package com.nova.modules.utilities.tax;

import java.awt.Dimension;

import com.nova.gui.shared.table.StoreTablePanel;
import com.nova.modules.ModuleBuilder;

/**
 * Esta clase representa el panel central del modulo donde se muestra en una
 * tabla los impuestos y sus respectivos valores
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class TaxTablePanel extends StoreTablePanel {

	/**
	 * Constructor. Llama a la superclase para construir la interfaz y le da el
	 * tamaño a la ventana
	 * 
	 * @param usersBuilder
	 *            Constructor del modulo
	 * @since 1.0
	 */
	public TaxTablePanel(ModuleBuilder usersBuilder) {
		super(usersBuilder, null);
		setPreferredSize(new Dimension(170, 100));
		setToolTipText("Impuestos registrados en el sistema");
	}

	@Override
	protected void setColumnsProperties() {
		// Sin implementacion
	}

}
