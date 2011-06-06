package com.nova.modules.stock.log;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import com.nova.modules.kardex.log.KardexDataManager;
import com.nova.modules.stock.gui.StockMainPanel;

/**
 * Esta clase facilita la logica para la creacion de un nuevo articulo en el
 * inventario.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class AddStockItem extends StockDataGUI {

	/**
	 * Construye la interfaz de usuario y agrega funcionalidad de agregar un
	 * articulo al inventario
	 * 
	 * @param mainPanel
	 *            Panel del modulo
	 * @since 1.0
	 */
	public AddStockItem(final StockMainPanel mainPanel) {
		super();
		aceptarBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (isValidData(false)) {
					ArrayList<String> data = getData();
					if (KardexDataManager.createStockItem(AddStockItem.this,
							data)) {
						mainPanel.updateData("Articulo " + data.get(4)
								+ " creado correctamente!!!");
						if (continuarAgregandoChbx.isSelected()) {
							cleanGUI();
							barCodeFld.requestFocus();
						} else
							dispose();
					} else
						mainPanel
								.updateData("Ha ocurrido un error en la creacion del articulo");
				}
			}

		});
		aceptarBtn.setToolTipText("Ingresar datos y crear articulo");
		setTitle("Creacion de articulo nuevo");
		setVisible(true);
	}

	/**
	 * Constructor para creado rapido de un elemento del inventario. Debe
	 * llamarse externamente del modulo.
	 * <p>
	 * Creation date 13/04/2006 - 08:37:46 AM
	 * @param description Descripcion del articulo a agregar rapidamente
	 * 
	 * @since 1.0
	 */
	public AddStockItem(String description) {
		super();
		
		descriptionFdl.setText(description);
		continuarAgregandoChbx.setEnabled(false);
		
		aceptarBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (isValidData(false)) {
					if (KardexDataManager.createStockItem(AddStockItem.this,
							getData())) {
						if (continuarAgregandoChbx.isSelected()) {
							cleanGUI();
							barCodeFld.requestFocus();
						} else
							dispose();
					}
				}
			}

		});
		aceptarBtn.setToolTipText("Ingresar datos y crear articulo");
		setTitle("Creacion de articulo nuevo");
		setVisible(true);
	}
}
