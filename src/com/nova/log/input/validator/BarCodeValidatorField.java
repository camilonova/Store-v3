package com.nova.log.input.validator;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTextField;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;

/**
 * Representa el campo de ingreso de un codigo para los elementos de la aplicacion
 * @author Camilo Nova
 * @version 1.0
 */
public class BarCodeValidatorField extends AbstractValidatorField {
	
	/**
	 * Construye el componente que permite validar la entrada de un
	 * codigo para los elementos del inventario
	 * <p>Creation date 11/06/2006 - 08:09:04 AM
	 *
	 * @since 1.0
	 */
	public BarCodeValidatorField() {
		this(new JTextField(), new JTextField());
	}

	/**
	 * Construye el componente que permite validar la entrada de un
	 * codigo para los elementos del inventario
	 * <p>Creation date 11/06/2006 - 08:05:06 AM
	 * @param nextElement		Componente activado con 'enter'
	 * @param exitElement		Componente activado con 'esc'
	 *
	 * @since 1.0
	 */
	public BarCodeValidatorField(JComponent nextElement,
			JComponent exitElement) {
		super(nextElement, exitElement);

		try {
			setMaxInput(Integer.parseInt(StoreCore.getProperty("BARCODE_LENGTH")));
		} catch (Exception e1) {
			ErrorLogLoader.addErrorEntry(e1);
			e1.printStackTrace();
		}
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

	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();
		e.setKeyChar(Character.toUpperCase(c));

		if (!(Character.isLetterOrDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)
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
