package com.nova.modules.customer.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ImageLoader;
import com.nova.gui.shared.table.StoreTableRemoveRow;
import com.nova.gui.shared.table.StoreTableSearch;
import com.nova.modules.customer.log.AddCustomer;
import com.nova.modules.customer.log.EditCustomer;

/**
 * Esta clase provee el acceso a los comandos del modulo de clientes mediante
 * una barra de herramientas, validando los respectivos accesos a los comandos
 * 
 * @author Camilo Nova
 * @version 1.0
 */
class CustomerToolBar extends JToolBar {

	private final StoreTableSearch search;
	private final ImageLoader imageLoader;

	/**
	 * Construye una barra de herramientas y le agrega funcionalidad
	 * 
	 * @param mainPanel
	 *            Panel principal de usuarios
	 * @since 1.0
	 */
	public CustomerToolBar(final CustomerMainPanel mainPanel) {
		search = new StoreTableSearch(mainPanel);
		imageLoader = ImageLoader.getInstance();

		if (StoreCore.getAccess("AgregarClientes")) {
			JButton newBtn = new JButton("Agregar", imageLoader
					.getImage("User.png"));
			newBtn.setMnemonic('a');
			newBtn.setToolTipText("Agregar un nuevo cliente al sistema");
			newBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new AddCustomer(mainPanel);
				}

			});
			add(newBtn);
		}
		if (StoreCore.getAccess("EditarClientes")) {
			JButton editBtn = new JButton("Editar", imageLoader
					.getImage("woman2.png"));
			editBtn.setMnemonic('e');
			editBtn.setToolTipText("Editar el cliente seleccionado");
			editBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new EditCustomer(mainPanel);
				}

			});
			add(editBtn);
		}

		if (StoreCore.getAccess("BorrarClientes")) {
			JButton delBtn = new JButton("Eliminar", imageLoader
					.getImage("DeleteRed.png"));
			delBtn.setMnemonic('l');
			delBtn.setToolTipText("Borrar el cliente seleccionado del sistema");
			delBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new StoreTableRemoveRow(mainPanel);
				}

			});
			add(delBtn);
		}

		addSeparator();
		add(search.getSearchBtn());
		add(search.getBackBtn());
		add(search.getNextBtn());

		setFloatable(false);
	}
}
