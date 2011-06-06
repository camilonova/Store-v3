package com.nova.log.input.validator;

import java.awt.event.KeyEvent;

import javax.swing.JTextField;

/**
 * Componente que verifica la entrada de un valor tipo double.
 * <p>
 * Es homologo a MoneyValidatorField(), con la restriccion que este no aplica
 * las locales al texto, unicamente deja el valor numerico.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class DoubleValidatorField extends AbstractValidatorField {

	/**
	 * Crea el componente basico para que unicamente permita el ingreso de texto
	 * numerico
	 * <p>
	 * Creation date 23/04/2006 - 11:55:19 AM
	 * 
	 * @since 1.0
	 */
	public DoubleValidatorField() {
		super(new JTextField(), new JTextField());
		setHorizontalAlignment(RIGHT);
		addKeyListener(this);
	}

	@Override
	public void setMaxInput(int maxDigits) {
		// Nothing
	}

	/**
	 * Valida los siguientes eventos:
	 * <p> - Que no sea una letra
	 * <p> - Que contenga solamente un caracter '.'
	 * 
	 * @since 1.0
	 */
	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();
		if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE
				|| c == KeyEvent.VK_DELETE || c == KeyEvent.VK_PERIOD)
				|| super.getText().contains(".") && c == KeyEvent.VK_PERIOD)
			e.consume();
	}

	public void keyReleased(KeyEvent e) {
		// Nothing
	}
}
