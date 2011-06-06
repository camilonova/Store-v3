package com.nova.log.input.validator;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;

/**
 * Esta clase representa una caja de entrada de texto, formateada
 * especificamente para validar la entrada a texto para el formato especificado
 * por la aplicacion, el cual debe constar unicamente por letras y numeros, sin
 * caracteres.
 * <p>
 * Reemplaza los listener de teclado de la version anterior :)
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class TextValidatorField extends AbstractValidatorField {

	boolean mayusOnly;

	/**
	 * Constructor por defecto de la clase, construye la caja de texto con
	 * botones fantasma conpatibles con el formulario
	 */
	public TextValidatorField() {
		this(new JButton(), new JButton());
	}

	/**
	 * Constructor, construye la caja de texto y asigna los botones recibidos
	 * por parametro con un maximo de 100 letras de entrada
	 * 
	 * @param nextElement
	 *            Componente que se activa al presionar 'enter'
	 * @param exitElement
	 *            Componente que se activa al presionar 'esc'
	 */
	public TextValidatorField(JComponent nextElement, JComponent exitElement) {
		this(100, nextElement, exitElement);
	}

	/**
	 * Constructor, construye la caja de texto con los parametros recibidos
	 * 
	 * @param maxDigits
	 *            Cantidad maxima de letras a recibir
	 * @param nextElement
	 *            Componente que se activa al presionar 'enter'
	 * @param exitElement
	 *            Componente que se activa al presionar 'esc'
	 */
	public TextValidatorField(int maxDigits, JComponent nextElement,
			JComponent exitElement) {
		super(nextElement, exitElement);

		setMaxInput(maxDigits);
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
	public void setMaxInput(int maxInput) {
		this.maxInput = maxInput;
	}

	/**
	 * Determina si el texto que recibe debe estar en mayusculas
	 * 
	 * @param isAlwaysMayus
	 *            True para recibir solamente mayusculas
	 */
	public void setMayusOnly(boolean isAlwaysMayus) {
		mayusOnly = isAlwaysMayus;
	}

	/**
	 * Valida los siguientes eventos:
	 * <p> - Que sea unicamente letras o digitos, ningun caracter especial es
	 * permitido
	 */
	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();

		if (mayusOnly)
			e.setKeyChar(Character.toUpperCase(c));

		if (!(Character.isLetterOrDigit(c) || c == KeyEvent.VK_BACK_SPACE
				|| c == KeyEvent.VK_DELETE || c == KeyEvent.VK_MINUS || c == KeyEvent.VK_SPACE)
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
