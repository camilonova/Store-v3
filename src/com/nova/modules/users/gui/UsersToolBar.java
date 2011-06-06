package com.nova.modules.users.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ImageLoader;
import com.nova.gui.shared.table.StoreTableRemoveRow;
import com.nova.gui.shared.table.StoreTableSearch;
import com.nova.modules.users.log.AddUser;
import com.nova.modules.users.log.ChangeUserPassword;
import com.nova.modules.users.log.EditUser;

/**
 * Esta clase provee el acceso a los comandos del modulo de usuarios mediante
 * una barra de herramientas, validando los respectivos accesos a los comandos
 * 
 * @author Camilo Nova
 * @version 1.0
 */
class UsersToolBar extends JToolBar {

	private final StoreTableSearch search;
	private final ImageLoader imageLoader;

	/**
	 * Construye una barra de herramientas y le agrega funcionalidad
	 * 
	 * @param mainPanel
	 *            Panel principal de usuarios
	 * @since 1.0
	 */
	public UsersToolBar(final UsersMainPanel mainPanel) {
		search = new StoreTableSearch(mainPanel);
		imageLoader = ImageLoader.getInstance();

		if (StoreCore.getAccess("AgregarUsuarios")) {
			JButton newBtn = new JButton("Agregar", imageLoader
					.getImage("User.png"));
			newBtn.setMnemonic('a');
			newBtn.setToolTipText("Agregar un nuevo usuario al sistema");
			newBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new AddUser(mainPanel);
				}

			});
			add(newBtn);
		}

		if (StoreCore.getAccess("EditarUsuarios")) {
			JButton editBtn = new JButton("Editar", imageLoader
					.getImage("woman2.png"));
			editBtn.setMnemonic('e');
			editBtn.setToolTipText("Editar el usuario seleccionado");
			editBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new EditUser(mainPanel);
				}

			});
			add(editBtn);
		}

		if (StoreCore.getAccess("BorrarUsuarios")) {
			JButton delBtn = new JButton("Eliminar", imageLoader
					.getImage("DeleteRed.png"));
			delBtn.setMnemonic('l');
			delBtn.setToolTipText("Borrar el usuario seleccionado del sistema");
			delBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new StoreTableRemoveRow(mainPanel);
				}

			});
			add(delBtn);
		}

		addSeparator();

		if (StoreCore.getAccess("CambiarPassword")) {
			JButton changeBtn = new JButton("Cambiar Password", 
					imageLoader.getImage("UserAdmin.png"));
			changeBtn.setMnemonic('c');
			changeBtn
					.setToolTipText("Cambiar la contraseña del usuario seleccionado");
			changeBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new ChangeUserPassword(mainPanel);
				}

			});
			add(changeBtn);
		}

		addSeparator();
		add(search.getSearchBtn());
		add(search.getBackBtn());
		add(search.getNextBtn());

		setFloatable(false);
	}
}
