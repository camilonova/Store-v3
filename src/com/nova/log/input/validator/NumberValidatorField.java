package com.nova.log.input.validator;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 * Esta clase representa una caja de entrada de texto, formateada
 * especificamente para convertir numericamente la entrada al formato
 * especificado por la aplicacion, el cual debe constar unicamente por numeros.
 * <p>
 * Reemplaza los listener de teclado de la version anterior :)
 * 
 * @author Camilo Nova
 */
public class NumberValidatorField extends AbstractValidatorField {

	/**
	 * Constructor por defecto de la clase, construye una caja de texto con un
	 * maximo de numeros de 12
	 */
	public NumberValidatorField() {
		this(12);
	}

	/**
	 * Constructor, crea una caja de texto con un numero maximo de digitos
	 * establecidos por el parametro
	 * 
	 * @param maxNumber
	 *            Numero maximo de numeros a recibir
	 */
	public NumberValidatorField(int maxNumber) {
		this(maxNumber, new JTextField(), new JTextField());
	}

	/**
	 * Constructor de la clase, asigna los parametros recibidos a las
	 * caracteristicas de la clase
	 * 
	 * @param maxNumber
	 *            Numero maximo de numeros a recibir
	 * @param nextElement
	 *            Componente que se activa al presionar 'enter'
	 * @param exitElement
	 *            Componente que se activa al presionar 'esc'
	 */
	public NumberValidatorField(int maxNumber, JComponent nextElement,
			JComponent exitElement) {
		super(nextElement, exitElement);

		maxInput = maxNumber;
		setHorizontalAlignment(RIGHT);

		addKeyListener(this);
		addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				selectAll();
				super.focusGained(e);
			}

		});
	}

	@Override
	public void setMaxInput(int maxDigits) {
		this.maxInput = maxDigits;
	}

	/**
	 * Restringe los siguientes eventos:
	 * <p> - Que sea una letra
	 * <p> - Que la longitud sea menor a la indicada
	 */
	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();

		if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)
				|| getText().length() == maxInput)
			e.consume();
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			if (nextElement instanceof JButton) {
				JButton nextBtn = (JButton) nextElement;
				nextBtn.doClick();
			} else
				nextElement.requestFocus();
		} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (exitElement instanceof JButton) {
				JButton exitBtn = (JButton) exitElement;
				exitBtn.doClick();
			} else
				exitElement.requestFocus();
		}
	}
}
