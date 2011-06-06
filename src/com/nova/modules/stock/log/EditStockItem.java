package com.nova.modules.stock.log;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.dat.loader.ImageLoader;
import com.nova.log.math.StoreMath;
import com.nova.modules.stock.gui.StockMainPanel;

/**
 * Esta clase facilita la logica para editar un articulo del inventario. Con la
 * restriccion de no editar el codigo ni cambiar su tipo.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class EditStockItem extends StockDataGUI {

	final String selectedItem;

	/**
	 * Construye la interfaz de usuario y agrega funcionalidad de editar un
	 * articulo del inventario
	 * 
	 * @param mainPanel
	 *            Panel del modulo
	 * @since 1.0
	 */
	public EditStockItem(final StockMainPanel mainPanel) {
		super();
		selectedItem = mainPanel.getSelectedID();
		if (selectedItem == null) {
			JOptionPane.showMessageDialog(null, "Debe seleccionar un articulo");
			return;
		}

		// Leemos datos
		try {
			StockItem stockItem = new StockItem(StoreCore.getAllRowData(
					mainPanel.getTableName(), "Codigo", selectedItem));
			if (stockItem.getType().equals("Producto"))
				productRbtn.doClick();
			else
				serviceRbtn.doClick();

			barCodeFld.setText(stockItem.getBarCode());
			groupCbx.setSelectedItem(stockItem.getGroup());
			quantityFld.setText(stockItem.getQuantity());
			minimumFld.setText(stockItem.getMinimum());
			descriptionFdl.setText(stockItem.getDescription());
			brandCbx.setSelectedItem(stockItem.getBrand());
			packingCbx.setSelectedItem(stockItem.getPacking());
			taxCbx.setSelectedItem(StoreCore.getDataAt("impuestos", "Valor",
					stockItem.getTax(), "Impuesto"));
			sellPriceFld
					.setDouble(Double.parseDouble(stockItem.getSellPrice()));
			providerCbx.setSelectedItem(stockItem.getProvider());
			costFld.setDouble(Double.parseDouble(stockItem.getCost()));
			itemPlaceFld.setText(stockItem.getItemPlace());
			imageUrl = stockItem.getImage();

			barCodeFld.setEnabled(false);
			quantityFld.setEnabled(false);
			costFld.setEnabled(false);
			productRbtn.setEnabled(false);
			serviceRbtn.setEnabled(false);
			continuarAgregandoChbx.setEnabled(false);

			if (imageUrl != null && imageUrl.length() > 5)
				imagenLbl.setIcon(new ImageIcon(imageUrl));
			else
				imagenLbl.setIcon(ImageLoader.getInstance().getImage(
						"no-image.jpg"));

			utilidadFld.setDouble(StoreMath.divide(sellPriceFld.getDouble(),
					costFld.getDouble(), true));

			aceptarBtn.setToolTipText("Actualizar la informacion");
			aceptarBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (isValidData(true)) {
						try {
							ArrayList<String> data = getData();
							StoreCore.updateData(mainPanel.getTableName(), data);
							mainPanel.updateData("Articulo " + data.get(4)
									+ " actualizado correctamente!!!");
							if (continuarAgregandoChbx.isSelected()) {
								cleanGUI();
								barCodeFld.requestFocus();
							} else
								dispose();
						} catch (SQLException e1) {
							mainPanel.updateData("Error. Verifique los datos");
							ErrorLogLoader.addErrorEntry(e1);
							e1.printStackTrace();
						}
					}
				}

			});
		} catch (Exception e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}

		setTitle("Editar el articulo " + selectedItem);
		setVisible(true);
	}

}
