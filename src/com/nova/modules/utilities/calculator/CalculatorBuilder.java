package com.nova.modules.utilities.calculator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.nova.log.input.validator.MoneyValidatorField;
import com.nova.log.input.validator.PercentValidatorField;
import com.nova.log.math.StoreMath;
import com.nova.modules.ModuleBuilder;

/**
 * Esta clase se encarga de proveer una interfaz para el calculo de impuestos la
 * cual es muy util para fijar precios de venta en base al impuesto.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class CalculatorBuilder extends JPanel implements ModuleBuilder {

	/**
	 * Entrada numerica del precio
	 */
	MoneyValidatorField precioFld;

	/**
	 * Entrada numerica del impuesto
	 */
	PercentValidatorField impuestoFld;

	/**
	 * Seleccion de precio con iva
	 */
	private JRadioButton precioConImpuesto = new JRadioButton(
			"Precio con impuesto", true);

	/**
	 * Seleccion de precio sin iva
	 */
	private JRadioButton precioSinImpuesto = new JRadioButton(
			"Precio sin impuesto");

	/**
	 * Grupo de botones que permiten una unica eleccion
	 */
	private ButtonGroup grupoBtn = new ButtonGroup();

	/**
	 * Salida de total
	 */
	private MoneyValidatorField precioTFld;

	/**
	 * Salida de impuesto
	 */
	private MoneyValidatorField impuestoTFld;

	/**
	 * Constructor de la clase, construye la interfaz de usuario.
	 * 
	 * @since 1.0
	 */
	public CalculatorBuilder() {
		JPanel upperPanel = new JPanel();
		JPanel middlePanel = new JPanel();
		JPanel lowerPanel = new JPanel();

		precioFld = new MoneyValidatorField();
		impuestoFld = new PercentValidatorField();
		precioTFld = new MoneyValidatorField();
		impuestoTFld = new MoneyValidatorField();

		precioFld.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					double precio = Double.parseDouble(precioFld.getText());
					double impuesto = impuestoFld.getDouble() + 1;
					calcular(precio, impuesto);
				} catch (Exception e1) {
					// Esta vacia la caja de texto
				}
				super.keyReleased(e);
			}

		});
		impuestoFld.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					double precio = precioFld.getDouble();
					double impuesto = Double.parseDouble(impuestoFld.getText()) + 1;
					calcular(precio, impuesto);
				} catch (Exception e1) {
					// Esta vacia la caja de texto
				}
				super.keyReleased(e);
			}

		});
		precioSinImpuesto.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					double precio = precioFld.getDouble();
					double impuesto = impuestoFld.getDouble() + 1;
					calcular(precio, impuesto);
				} catch (Exception e1) {
					// Esta vacia la caja de texto
				}
			}

		});
		precioConImpuesto.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					double precio = precioFld.getDouble();
					double impuesto = impuestoFld.getDouble() + 1;
					calcular(precio, impuesto);
				} catch (Exception e1) {
					// Esta vacia la caja de texto
				}
			}

		});

		precioFld.setToolTipText("Precio a calcular impuesto");
		impuestoFld.setToolTipText("Porcentaje de impuesto a calcular");
		precioSinImpuesto
				.setToolTipText("Seleccione si el precio no tiene impuesto");
		precioConImpuesto
				.setToolTipText("Seleccione si el precio tiene impuesto");

		precioFld.setPreferredSize(new Dimension(110, 20));
		precioTFld.setPreferredSize(new Dimension(110, 20));
		impuestoTFld.setPreferredSize(new Dimension(60, 20));

		upperPanel.add(new JLabel("Precio"));
		upperPanel.add(precioFld);
		upperPanel.add(new JLabel("Impuesto"));
		upperPanel.add(impuestoFld);

		grupoBtn.add(precioConImpuesto);
		grupoBtn.add(precioSinImpuesto);
		middlePanel.add(precioConImpuesto);
		middlePanel.add(precioSinImpuesto);

		precioTFld.setEditable(false);
		impuestoTFld.setEditable(false);
		impuestoFld.setText("16%");

		lowerPanel.add(new JLabel("Total"));
		lowerPanel.add(precioTFld);
		lowerPanel.add(new JLabel("Impuesto"));
		lowerPanel.add(impuestoTFld);

		add(upperPanel, BorderLayout.NORTH);
		add(middlePanel, BorderLayout.CENTER);
		add(lowerPanel, BorderLayout.SOUTH);

	}

	/**
	 * Calcula el valor del impuesto y el precio con o sin impuesto
	 * 
	 * @param precio
	 *            Precio a calcular
	 * @param impuesto
	 *            Impuesto del precio
	 * @since 1.0
	 */
	void calcular(double precio, double impuesto) {
		if (precioConImpuesto.isSelected()) {
			double precioCalculado = StoreMath.divide(precio, impuesto, false);
			precioTFld.setText(StoreMath.parseDouble(precioCalculado));
			impuestoTFld.setText(StoreMath.parseDouble(StoreMath.subtract(
					precio, precioCalculado, true)));
		} else {
			double precioCalculado = StoreMath
					.multiply(precio, impuesto, false);
			precioTFld.setText(StoreMath.parseDouble(precioCalculado));
			impuestoTFld.setText(StoreMath.parseDouble(StoreMath.subtract(
					precioCalculado, precio, true)));
		}
	}

	public String getRelatedTableName() {
		// Sin Implementacion
		return null;
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
