package com.nova.log.input.validator;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;

/**
 * Esta clase representa una caja de entrada de texto, formateada
 * especificamente para convertir nit y CC al formato especificado por la
 * aplicacion, el cual debe llevar los puntos de separacion y la barra seguida
 * de un numero para el nit.
 * <p>
 * El numero de identificacion mas largo contiene 9 digitos y el del NIT tiene
 * un tamaño maximo de 11 caracteres.
 * <p>
 * 
 * @author Camilo Nova
 */
public class IdentificationValidatorField extends AbstractValidatorField {

	/**
	 * Constructor predeterminado, construye la caja de texto.
	 */
	public IdentificationValidatorField() {
		this(new JButton(), new JButton());
	}

	/**
	 * Constructor, construye la caja de texto y los parametros recibidos como
	 * botones de utilidad de formulario
	 * 
	 * @param nextElement
	 *            Componente que se activa al presionar 'enter'
	 * @param exitElement
	 *            Componente que se activa al presionar 'esc'
	 */
	public IdentificationValidatorField(JComponent nextElement,
			JComponent exitElement) {
		super(nextElement, exitElement);
		setHorizontalAlignment(RIGHT);

		addKeyListener(this);
		addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				setText(getText().replace(".", ""));
				selectAll();
			}

			public void focusLost(FocusEvent e) {
				setText(getText());
			}
		});
	}

	@Override
	public void setMaxInput(int maxDigits) {
		// Sin implementacion
	}

	/**
	 * Retorna el texto formateado como numero de identificacion
	 */
	@Override
	public String getText() {
		String text = super.getText().replace(".", "");

		if (text.endsWith("-") || !text.contains("-") && text.length() < 7
				|| text.contains("-") && text.length() < 9)
			requestFocus();
		else {
			String string = text;
			String end = new String();

			if (text.split("-").length == 2) {
				string = text.split("-")[0];
				end = "-" + text.split("-")[1];
			}
			text = new String();

			for (int i = 0; i < string.length(); i++) {
				if ((string.length() - i) % 3 == 0 && i != 0)
					text += ".";
				text += string.charAt(i);
			}
			text += end;
		}
		return text;
	}

	/**
	 * Restinge los siguientes eventos:
	 * <p> - Que sea una letra
	 * <p> - Que contenga solamente un caracter '-'
	 * <p> - Que no comience con '-'
	 * <p> - Que la cadena sea de longitud 9 sin el caracter '-'
	 * <p> - Que exista solamente un numero luego del caracer '-'
	 */
	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();
		String text = getText().replace(".", "");

		if ((!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE
				|| c == KeyEvent.VK_DELETE || c == KeyEvent.VK_MINUS))
				|| text.contains("-")
				&& c == KeyEvent.VK_MINUS
				|| text.startsWith("-")
				|| (!text.contains("-") && text.length() == 9 && c != KeyEvent.VK_MINUS)
				|| (text.split("-").length == 2))

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
