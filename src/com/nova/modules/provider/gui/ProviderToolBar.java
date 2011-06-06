package com.nova.modules.provider.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ImageLoader;
import com.nova.gui.shared.table.StoreTableRemoveRow;
import com.nova.gui.shared.table.StoreTableSearch;
import com.nova.modules.provider.log.AddProvider;
import com.nova.modules.provider.log.EditProvider;

/**
 * Esta clase provee el acceso a los comandos del modulo de proveedores mediante
 * una barra de herramientas, validando los respectivos accesos a los comandos
 * 
 * @author Camilo Nova
 * @version 1.0
 */
class ProviderToolBar extends JToolBar {

	private final StoreTableSearch search;
	private final ImageLoader imageLoader;

	/**
	 * Construye una barra de herramientas y le agrega funcionalidad
	 * 
	 * @param mainPanel
	 *            Panel principal
	 * @since 1.0
	 */
	public ProviderToolBar(final ProviderMainPanel mainPanel) {
		search = new StoreTableSearch(mainPanel);
		imageLoader = ImageLoader.getInstance();

		if (StoreCore.getAccess("AgregarProveedores")) {
			JButton newBtn = new JButton("Agregar", imageLoader
					.getImage("woman3.png"));
			newBtn.setMnemonic('a');
			newBtn.setToolTipText("Agregar un nuevo proveedor al sistema");
			newBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new AddProvider(mainPanel);
				}

			});
			add(newBtn);
		}

		if (StoreCore.getAccess("EditarProveedores")) {
			JButton editBtn = new JButton("Editar", imageLoader
					.getImage("woman1.png"));
			editBtn.setMnemonic('e');
			editBtn.setToolTipText("Editar el proveedor seleccionado");
			editBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new EditProvider(mainPanel);
				}

			});
			add(editBtn);
		}

		if (StoreCore.getAccess("BorrarProveedores")) {
			JButton delBtn = new JButton("Eliminar", imageLoader
					.getImage("DeleteRed.png"));
			delBtn.setMnemonic('l');
			delBtn
					.setToolTipText("Borrar el proveedor seleccionado del sistema");
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
