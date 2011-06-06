package com.nova.log.input.validator;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * Abstraccion que permite generalizar los <i>ChackerField</i> de la aplicacion
 * 
 * @author Camilo Nova
 * @version 1.0
 */
abstract class AbstractValidatorField extends JTextField implements KeyListener {

	/**
	 * Componente siguiente
	 */
	final JComponent nextElement;

	/**
	 * Componente de salida
	 */
	final JComponent exitElement;

	/**
	 * Representa la cantidad maxima de numeros
	 */
	int maxInput;

	/**
	 * Constructor. Determina los componentes que se activan con 'enter' y 'esc'
	 * 
	 * @param nextElement
	 *            Componente que se activa al presionar 'enter'
	 * @param exitElement
	 *            Componente que se activa al presionar 'esc'
	 * @since 1.0
	 */
	AbstractValidatorField(JComponent nextElement, JComponent exitElement) {
		this.nextElement = nextElement;
		this.exitElement = exitElement;
	}

	/**
	 * Determina el numero maximo de digitos que recibe la caja de texto
	 * 
	 * @param maxInput
	 *            Numero maximo de digitos
	 * @since 1.0
	 */
	public abstract void setMaxInput(int maxInput);

	@Override
	public void paste() {
		// No se puede pegar texto en los validadores
	}

	public void keyPressed(KeyEvent e) {
		// Sin Implementacion
	}

}