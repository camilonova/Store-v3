package com.nova.log.input.validator;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.JComponent;
import javax.swing.JTextField;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;

/**
 * Provee una caja de texto la cual valida que la entrada sea de tipo moneda.
 * Para obtener el valor formateado a texto se debe llamar al metodo getText().
 * Para obtener el valor numerico se debe llamar al metodo getDouble().
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class MoneyValidatorField extends AbstractValidatorField {

	/**
	 * Formateador de la entrada
	 */
	final NumberFormat formatter = NumberFormat.getCurrencyInstance();

	/**
	 * Construye una caja de texto con un maximo de digitos de 18
	 * 
	 * @since 1.0
	 */
	public MoneyValidatorField() {
		this(18);
	}

	/**
	 * Construye una caja de texto con un maximo de digitos determinados por el
	 * parametro
	 * 
	 * @param maxDigits
	 *            Digitos maximos de la parte entera
	 * @since 1.0
	 */
	public MoneyValidatorField(int maxDigits) {
		this(maxDigits, new JTextField(), new JTextField());
	}

	/**
	 * Construye una caja de texto con un maximo de 18 digitos y componentes
	 * determinados por los parametros
	 * 
	 * @param nextElement
	 *            Componente que se activa al presionar 'enter'
	 * @param exitElement
	 *            Componente que se activa al presionar 'esc'
	 * @since 1.0
	 */
	public MoneyValidatorField(JComponent nextElement, JComponent exitElement) {
		this(18, nextElement, exitElement);
	}

	/**
	 * Construye la caja de texto para el uso de moneda.
	 * 
	 * @param maxDigits
	 *            Cantidad de digitos maximos en la parte entera
	 * @param nextElement
	 *            Componente que se activa al presionar 'enter'
	 * @param exitElement
	 *            Componente que se activa al presionar 'esc'
	 * @since 1.0
	 */
	public MoneyValidatorField(int maxDigits, JComponent nextElement,
			JComponent exitElement) {
		super(nextElement, exitElement);

		setMaxInput(maxDigits);
		setHorizontalAlignment(RIGHT);
		try {
			formatter.setMaximumFractionDigits(Integer.parseInt(StoreCore
					.getProperty("FRACTIONS")));
		} catch (Exception e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}

		addKeyListener(this);
		addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				if (getText().length() > 0) {
					try {
						setText(formatter.parse(getText()).toString());
					} catch (ParseException e1) {
						ErrorLogLoader.addErrorEntry(e1);
						e1.printStackTrace();
					}
					selectAll();
				}
			}

			public void focusLost(FocusEvent e) {
				if (getText().length() > 0)
					setText(formatter.format(Double.parseDouble(getText())));
			}
		});
	}

	@Override
	public void setMaxInput(int maxDigits) {
		formatter.setMaximumIntegerDigits(maxDigits);
	}

	/**
	 * Retorna el valor double del contenido de la caja de texto.
	 * 
	 * @return Valor double del contenido
	 * @throws ParseException
	 *             Se lanza cuando la caja de texto esta vacia
	 * @since 1.0
	 */
	public double getDouble() throws ParseException {
		return formatter.parse(getText()).doubleValue();
	}

	/**
	 * Muestra en la caja de texto el valor pasado por parametro
	 * 
	 * @param money
	 *            Cantidad de dinero a mostrar
	 * @since 1.0
	 */
	public void setDouble(double money) {
		setText(formatter.format(money));
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
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			nextElement.requestFocus();
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			exitElement.requestFocus();
	}
}
