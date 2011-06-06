package com.nova.log.form.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.log.form.dat.FormTableModel;
import com.nova.log.input.special.InputComponent;
import com.nova.log.input.validator.BarCodeValidatorField;
import com.nova.log.input.validator.MoneyValidatorField;
import com.nova.log.input.validator.NumberValidatorField;
import com.nova.modules.purchase.gui.PurchaseMainPanel;
import com.nova.modules.stock.log.AddStockItem;
import com.nova.modules.stock.log.StockItem;

/**
 * Representa el panel de ingreso de articulos al formato
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class FormInsertPanel extends JPanel {

	JLabel quantityLbl;

	NumberValidatorField quantityFld;

	JLabel barCodeLbl;

	BarCodeValidatorField barCodeFld;

	JLabel descriptionLbl;

	InputComponent descriptionFld;

	JLabel sellPriceLbl;

	MoneyValidatorField sellPriceFld;

	FormTableModel tableModel;

	/**
	 * Construye el panel y le agrega funcionalidad
	 * <p>
	 * Creation date 2/05/2006 - 10:34:56 PM
	 * 
	 * @param form
	 *            Formato
	 * 
	 * @since 1.0
	 */
	public FormInsertPanel(final Form form) {
		tableModel = form.getTableModel();
		quantityLbl = new JLabel("Cantidad", SwingConstants.CENTER);
		quantityFld = new NumberValidatorField(5);
		barCodeLbl = new JLabel("Codigo", SwingConstants.CENTER);
		barCodeFld = new BarCodeValidatorField();
		descriptionLbl = new JLabel("Descripcion", SwingConstants.CENTER);
		descriptionFld = new InputComponent(form.getFormItems());
		sellPriceLbl = new JLabel("Precio", SwingConstants.CENTER);
		sellPriceFld = new MoneyValidatorField();

		quantityLbl.setPreferredSize(new Dimension(50, 20));
		quantityFld.setPreferredSize(new Dimension(50, 20));
		barCodeLbl.setPreferredSize(new Dimension(50, 20));
		barCodeFld.setPreferredSize(new Dimension(100, 20));
		descriptionLbl.setPreferredSize(new Dimension(70, 20));
		descriptionFld.setPreferredSize(new Dimension(280, 20));
		sellPriceLbl.setPreferredSize(new Dimension(50, 20));
		sellPriceFld.setPreferredSize(new Dimension(80, 20));

		quantityFld.setToolTipText("Cantidad de items del articulo");
		barCodeFld.setToolTipText("Codigo de referencia del articulo");
		descriptionFld.setToolTipText("Descripcion del articulo");
		sellPriceFld.setToolTipText("Precio publico unitario");

		quantityFld.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				descriptionFld.requestFocus();
			}

		});
		barCodeFld.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					String barCode = barCodeFld.getText();
					StockItem stockItem = new StockItem(barCode,
							StockItem.BARCODE);

					String cant = quantityFld.getText();
					String quantity = cant.length() > 0 ? cant : "1";
					String description = StoreCore
							.getStockDescriptionAndBrand(barCode);

					String price;
					if (form.getModuleForm() instanceof PurchaseMainPanel)
						price = stockItem.getCost();
					else
						price = stockItem.getSellPrice();

					tableModel.insertRow(tableModel.calculateTotals(stockItem,
							quantity, description, price));
					quantityFld.setText(null);
					barCodeFld.setText(null);
				} catch (Exception e1) {
					barCodeFld.selectAll();
				}
			}
		});
		descriptionFld.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					StockItem stockItem = new StockItem(descriptionFld
							.getText(), StockItem.DESCRIPTION_BRAND);

					String price;
					if (form.getModuleForm() instanceof PurchaseMainPanel)
						price = stockItem.getCost();
					else
						price = stockItem.getSellPrice();

					sellPriceFld.setDouble(Double.valueOf(price));
					sellPriceFld.requestFocus();
				} catch (Exception e1) {
					ErrorLogLoader.addErrorEntry(e1);
					e1.printStackTrace();
				}
			}

		});
		descriptionFld.setNoItemAction(new ActionListener() {
		
			public void actionPerformed(ActionEvent arg0) {
				Toolkit.getDefaultToolkit().beep();
				if(JOptionPane.showConfirmDialog(form, "Elemento no existente. Desea ingresarlo?",
						"Articulo no registrado", JOptionPane.YES_NO_OPTION)
						== JOptionPane.YES_OPTION) {
					String newItemDescription = descriptionFld.getText();
					new AddStockItem(newItemDescription);
					descriptionFld.updateData(form.getFormItems());
				}
				else
					descriptionFld.selectAll();
			}
		
		});
		sellPriceFld.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					StockItem stockItem = new StockItem(descriptionFld
							.getText(), StockItem.DESCRIPTION_BRAND);

					String cant = quantityFld.getText();
					String quantity = cant.length() > 0 ? cant : "1";
					String description = descriptionFld.getText();
					String price = sellPriceFld.getText();

					tableModel.insertRow(tableModel.calculateTotals(stockItem,
							quantity, description, price));
					quantityFld.setText(null);
					sellPriceFld.setText(null);
					descriptionFld.setText(null);
				} catch (Exception e1) {
					descriptionFld.selectAll();
				}
				descriptionFld.requestFocus();
			}

		});

		add(quantityLbl);
		add(quantityFld);
		add(barCodeLbl);
		add(barCodeFld);
		add(descriptionLbl);
		add(descriptionFld);
		add(sellPriceLbl);
		add(sellPriceFld);

		setBorder(BorderFactory.createTitledBorder("Ingresar Elementos"));
		setPreferredSize(new Dimension(790, 60));
	}

	/**
	 * Determina el estado del panel
	 * <p>
	 * Creation date 4/05/2006 - 10:05:26 AM
	 * 
	 * @param enable
	 *            True para habilitar el panel, false de lo contrario
	 * @since 1.0
	 */
	void setPanelEnabled(boolean enable) {
		quantityFld.setEnabled(enable);
		barCodeFld.setEnabled(enable);
		descriptionFld.setEnabled(enable);
		sellPriceFld.setEnabled(enable);
	}
}
