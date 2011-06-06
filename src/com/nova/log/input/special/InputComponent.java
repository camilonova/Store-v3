package com.nova.log.input.special;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * Clase que representa un componente de entrada de datos que permite la
 * funcionalidad de hacer un filtrado de datos mientras se escribe en el
 * componente.
 * <p>
 * Hasta el momento el desarrollo mas elaborado de toda la aplicacion :)
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class InputComponent extends JTextField {

	ArrayList<String> data;

	JScrollPane scrollPane;

	JList list;

	Thread grepThread;

	Thread windowThread;

	JPopupMenu popupMenu;

	private ActionListener noItemAction;

	/**
	 * Constructor, agrega la funcionalidad y las caracteristicas especiales al
	 * componente para obtener el comportamiento deseado.
	 * 
	 * @param dat
	 *            Datos a filtrar
	 * 
	 * @since 1.0
	 */
	public InputComponent(ArrayList<String> dat) {
		this.data = dat;

		list = new JList(data.toArray());
		scrollPane = new JScrollPane(list);
		popupMenu = new JPopupMenu() {
			@Override
			public boolean isFocusable() {
				return false;
			}
		};
		popupMenu.add(scrollPane);

		/*
		 * Listeners del componente
		 */
		addKeyListener(new KeyListener() {

			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!(Character.isLetterOrDigit(c)
						|| c == KeyEvent.VK_BACK_SPACE
						|| c == KeyEvent.VK_DELETE || c == KeyEvent.VK_MINUS || c == KeyEvent.VK_SPACE))
					e.consume();
			}

			@SuppressWarnings("synthetic-access")
			public void keyReleased(KeyEvent e) {
				if (!popupMenu.isShowing()) {
					grepThread.run();
					windowThread.run();
				}
				if (Character.isLetterOrDigit(e.getKeyChar())
						|| e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					grepThread.run();
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					popupMenu.setVisible(false);
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN
						&& popupMenu.isShowing()) {
					list.setSelectedIndex(list.getSelectedIndex() + 1);
					list.scrollRectToVisible(list.getCellBounds(list
							.getFirstVisibleIndex() + 2, list
							.getSelectedIndex()));
				} else if (e.getKeyCode() == KeyEvent.VK_UP
						&& popupMenu.isShowing()) {
					// FIXME problemas cuando solamente hay un elemento
					list.setSelectedIndex(list.getSelectedIndex() - 1);
					list.scrollRectToVisible(list
							.getCellBounds(list.getSelectedIndex(), list
									.getLastVisibleIndex() - 2));
				}
				requestFocus();
			}

			public void keyPressed(KeyEvent e) {
				// FIXME al mantener presionado debe continuar con el scroll
				// Poner en foro como copiar el listener en el componente
			}

		});
		addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				Component oppositeComponent = e.getOppositeComponent();
				if (oppositeComponent == null
						|| (!oppositeComponent.equals(list) && oppositeComponent instanceof JComponent))
					popupMenu.setVisible(false);
			}

		});

		/*
		 * Listeners del dialogo
		 */
		list.addMouseListener(new MouseAdapter() {
			@Override
			@SuppressWarnings("synthetic-access")
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2)
					fireActionPerformed();
				super.mouseClicked(e);
			}
		});
		list.addKeyListener(new KeyAdapter() {
			@Override
			@SuppressWarnings("synthetic-access")
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER)
					fireActionPerformed();
				super.keyReleased(e);
			}

		});

		/*
		 * Hilos del dialogo y de filtrado
		 */
		grepThread = new Thread(new Runnable() {

			public void run() {
				ArrayList<String> temp = new ArrayList<String>();
				for (int i = 0; i < data.size(); i++) {
					String string = data.get(i);
					if (evaluate(string, getText()))
						temp.add(string);
				}
				updateList(temp.toArray());
			}

		}, "UpdateWindowData");
		windowThread = new Thread(new Runnable() {

			public void run() {
				if (list.getModel().getSize() > 1) {
					popupMenu.setPopupSize(getWidth(), 150);
					popupMenu.show(InputComponent.this, 0, getHeight());
				}
			}

		}, "WindowThread");

	}

	@Override
	protected void fireActionPerformed() {
		Object selectedValue = list.getSelectedValue();
		if (selectedValue != null && noItemAction != null) {
			setText((String) selectedValue);
			popupMenu.setVisible(false);
			requestFocus();
			super.fireActionPerformed();
		}
		else
			noItemAction.actionPerformed(new ActionEvent(this, 0, ""));
	}

	/**
	 * Actualiza los datos en la lista
	 * 
	 * @param newData
	 *            Datos a mostrar en la lista
	 * @since 1.0
	 */
	void updateList(Object[] newData) {
		list.removeAll();
		list.setListData(newData);
	}

	/**
	 * Actualiza los datos a filtrar
	 * <p>
	 * Creation date 3/05/2006 - 10:22:35 AM
	 * @param dat Datos nuevos a utilizar en el componente
	 * 
	 * @since 1.0
	 */
	public void updateData(ArrayList<String> dat) {
		setText(null);
		data = dat;
	}

	/**
	 * Evalua las cadenas recibidas por parametro, buscando que una este
	 * contenida en la otra
	 * 
	 * @param originalStr
	 *            Cadena principal
	 * @param obtainedStr
	 *            Cadena obtenida
	 * @return True si la cadena obtenida se encuentra en la cadena principal
	 * @since 1.0
	 */
	boolean evaluate(String originalStr, String obtainedStr) {
		String a = originalStr.toLowerCase();
		String b = obtainedStr.toLowerCase();
		return a.contains(b);
	}
	
	/**
	 * Determina el tipo de accion que debe ejecutar en componente
	 * en caso de que no encuentre el elemento ingresado en la lista
	 * <p>Creation date 16/06/2006 - 11:47:47 AM
	 *
	 * @param e			Accion a ejecutar cuando no encuentre el elemento
	 * @since 1.0
	 */
	public void setNoItemAction(ActionListener e) {
		this.noItemAction = e;
	}
}
