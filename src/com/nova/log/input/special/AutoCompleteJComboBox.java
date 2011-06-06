package com.nova.log.input.special;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JComboBox.KeySelectionManager;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Componente que hereda de JComboBox y que posee las mismas caracteristicas, se
 * ha implementado la caracteristica de autocompletar el texto ingresado
 * 
 * @author Camilo Nova
 * @since 1.0
 */
public class AutoCompleteJComboBox extends JComboBox implements
		KeySelectionManager, ActionListener {

	private String searchFor;

	private long lap;
	
	/**
	 * Crea el componente con los elementos indicados por el parametro y agrega
	 * las caracteristicas para autocompletar el texto
	 * <p>
	 * Creation date 17/05/2006 - 09:41:39 AM
	 * 
	 * @param items		Elementos a autocompletar
	 * 
	 * @since 1.0
	 */
	public AutoCompleteJComboBox(ArrayList<String> items) {
		this();
		setModel(new DefaultComboBoxModel(items.toArray()));
	}
	
	/**
	 * Crea el componente de autocompletado sin elementos. Es necesario que
	 * se llame al metodo setItems(), para definir los elementos.
	 * <p>Creation date 18/06/2006 - 12:34:19 PM
	 *
	 * @since 1.0
	 */
	public AutoCompleteJComboBox() {
		lap = System.currentTimeMillis();
		setKeySelectionManager(this);
		setEditable(true);

		if (getEditor() != null) {
			JTextField fieldEditor = (JTextField) getEditor()
					.getEditorComponent();
			if (fieldEditor != null) {
				addActionListener(this);
				fieldEditor.setDocument(new CBDocument());
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JTextField textField = (JTextField) getEditor().getEditorComponent();
		String text = textField.getText();
		ComboBoxModel aModel = getModel();
		String current;
		for (int i = 0; i < aModel.getSize(); i++) {
			current = aModel.getElementAt(i).toString();
			if (current.toLowerCase().startsWith(text.toLowerCase())) {
				textField.setText(current);
				textField.setSelectionStart(text.length());
				textField.setSelectionEnd(current.length());
				break;
			}
		}
	}

	public int selectionForKey(char aKey, ComboBoxModel aModel) {
		long now = System.currentTimeMillis();
		if (searchFor != null && aKey == KeyEvent.VK_BACK_SPACE
				&& searchFor.length() > 0) {
			searchFor = searchFor.substring(0, searchFor.length() - 1);
		} else {
			if (lap + 1000 < now)
				searchFor = "" + aKey;
			else
				searchFor = searchFor + aKey;
		}
		lap = now;
		for (int i = 0; i < aModel.getSize(); i++) {
			String current = aModel.getElementAt(i).toString().toLowerCase();
			if (current.toLowerCase().startsWith(searchFor.toLowerCase()))
				return i;
		}
		return -1;
	}

	/**
	 * Metodo que convierte toda la entrada del campo de texto en mayusculas.
	 * <p>
	 * Creation date 12/04/2006 - 01:46:58 PM
	 * 
	 * @since 1.0
	 */
	public void convertToUpperCase() {
		((JTextField) getEditor().getEditorComponent())
				.addKeyListener(new KeyAdapter() {

					@Override
					public void keyTyped(KeyEvent e) {
						e.setKeyChar(Character.toUpperCase(e.getKeyChar()));
						super.keyReleased(e);
					}

				});
	}

	/**
	 * Retorna el valor que se encuentra en la caja de texto
	 * <p>
	 * Creation date 12/04/2006 - 01:46:58 PM
	 * 
	 * @since 1.0
	 */
	@Override
	public Object getSelectedItem() {
		return super.getEditor().getItem();
	}

	/**
	 * Selecciona lo que debe mostrar la caja de texto, independiente de si esta
	 * en el listado o no
	 * <p>
	 * Creation date 12/04/2006 - 01:46:58 PM
	 * 
	 * @since 1.0
	 */
	@Override
	public void setSelectedItem(Object anObject) {
		super.getEditor().setItem(anObject);
	}

	/**
	 * Actualiza los elementos en la lista.
	 * <p>
	 * Creation date 17/05/2006 - 09:30:09 AM
	 * 
	 * @param items
	 *            Elementos nuevos
	 * @since 1.0
	 */
	public void setItems(ArrayList<String> items) {
		super.setModel(new DefaultComboBoxModel(items.toArray()));
	}

	/**
	 * Esta clase es la encargada de ingresar el texto faltante para
	 * autocompletar el texto ingresado
	 * 
	 * @author Camilo Nova
	 * @version 1.0
	 */
	class CBDocument extends PlainDocument {
		@SuppressWarnings("synthetic-access")
		@Override
		public void insertString(int offset, String str, AttributeSet a)
				throws BadLocationException {
			if (str != null) {
				super.insertString(offset, str, a);
				if (!isPopupVisible() && str.length() != 0)
					fireActionEvent();
			}
		}
	}
}
