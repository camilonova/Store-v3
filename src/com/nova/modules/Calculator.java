package com.nova.modules;

import javax.swing.JInternalFrame;

import com.nova.modules.utilities.calculator.CalculatorBuilder;

/**
 * Representa el modulo de la calculadora de impuestos.
 * 
 * @author Camilo Nova
 * @since 1.0
 */
public class Calculator extends JInternalFrame implements Module {

	/**
	 * Construye una ventana de visualizacion de la calculadora
	 * 
	 * @since 1.0
	 */
	public Calculator() {
		super("Calculadora de Impuesto", false, true, false, true);

		add(new CalculatorBuilder());
		setSize(300, 150);
		setAutoscrolls(true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
