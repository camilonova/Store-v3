package com.nova.modules.utilities.priceRead;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.nova.dat.db.StoreCore;
import com.nova.log.input.special.InputComponent;
import com.nova.log.input.validator.BarCodeValidatorField;
import com.nova.log.input.validator.MoneyValidatorField;
import com.nova.log.math.StoreMath;
import com.nova.modules.ModuleBuilder;

/**
 * Esta clase es la encargada de contruir la interfaz grafica de usuario para el
 * modulo de lectura de precios, el cual recibe o el codigo o la descripcion de
 * un elemento y muestra su precio de venta
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class PriceReadBuilder extends JPanel implements ModuleBuilder {

	BarCodeValidatorField codigoFld;

	InputComponent descripcionFld;

	MoneyValidatorField precioFld;

	/**
	 * Construye los componentes y crea la ventana.
	 * <p>
	 * Creation date 22/04/2006 - 10:00:43 AM
	 * 
	 * @since 1.0
	 */
	public PriceReadBuilder() {
		JLabel label1 = new JLabel("Codigo");
		JLabel label2 = new JLabel("Descripcion");
		JLabel label3 = new JLabel("Precio");

		codigoFld = new BarCodeValidatorField();
		descripcionFld = new InputComponent(StoreCore.getStockAllItems());
		precioFld = new MoneyValidatorField();

		label1.setPreferredSize(new Dimension(230, 20));
		label2.setPreferredSize(new Dimension(80, 20));
		label3.setPreferredSize(new Dimension(230, 20));
		codigoFld.setPreferredSize(new Dimension(150, 20));
		descripcionFld.setPreferredSize(new Dimension(300, 20));
		precioFld.setPreferredSize(new Dimension(150, 20));

		codigoFld.setToolTipText("Codigo del articulo. Enter para actualizar");
		descripcionFld
				.setToolTipText("Descripcion del articulo. Enter para actualizar");
		precioFld.setToolTipText("Precio de venta del articulo");

		codigoFld.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String barCode = codigoFld.getText();
				descripcionFld.setText(StoreCore
						.getStockDescriptionAndBrand(barCode));
				precioFld.setText(getSellPrice(barCode));
				codigoFld.selectAll();
			}

		});
		descripcionFld.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String barCode = StoreCore.getStockBarCode(descripcionFld
						.getText());
				codigoFld.setText(barCode);
				precioFld.setText(getSellPrice(barCode));
				descripcionFld.selectAll();
			}

		});
		precioFld.setEditable(false);

		add(label1);
		add(codigoFld);
		add(label2);
		add(descripcionFld);
		add(label3);
		add(precioFld);
	}

	/**
	 * Retorna el precio de venta de un elemento
	 * 
	 * @param itemBarCode
	 *            Codigo del elemento
	 * @return Precio del elemento
	 * @since 1.0
	 */
	String getSellPrice(String itemBarCode) {
		String price = StoreCore.getDataAt(getRelatedTableName(), "Codigo",
				itemBarCode, "PrecioPublico");
		return StoreMath.parseDouble(Double.valueOf(price));
	}

	public String getRelatedTableName() {
		return "inventario";
	}

	public String getOrderColumn() {
		// Sin Implementacion
		return null;
	}

	public String getOrderType() {
		// Sin Implementacion
		return null;
	}

	public void setStatusBarText(String message) {
		// Sin Implementacion
	}
}
