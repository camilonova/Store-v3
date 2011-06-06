package com.nova.modules.stock.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ImageLoader;
import com.nova.gui.shared.table.StoreTableSearch;
import com.nova.modules.kardex.log.KardexDataManager;
import com.nova.modules.stock.log.AddStockItem;
import com.nova.modules.stock.log.EditStockItem;

/**
 * Esta clase provee el acceso a los comandos del modulo de usuarios mediante
 * una barra de herramientas.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
class StockToolBar extends JToolBar {

	private final StoreTableSearch search;
	private final ImageLoader imageLoader;

	/**
	 * Construye una barra de herramientas y le agrega funcionalidad.
	 * 
	 * @param mainPanel
	 *            Panel principal de usuarios
	 * @since 1.0
	 */
	public StockToolBar(final StockMainPanel mainPanel) {
		search = new StoreTableSearch(mainPanel);
		imageLoader = ImageLoader.getInstance();

		if (StoreCore.getAccess("AgregarInventario")) {
			JButton newBtn = new JButton("Agregar", imageLoader
					.getImage("bookmark.png"));
			newBtn.setMnemonic('a');
			newBtn.setToolTipText("Agregar un nuevo articulo al inventario");
			newBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new AddStockItem(mainPanel);
				}

			});
			add(newBtn);
		}

		if (StoreCore.getAccess("EditarInventario")) {
			JButton editBtn = new JButton("Editar", imageLoader
					.getImage("bookmark_add.png"));
			editBtn.setMnemonic('e');
			editBtn.setToolTipText("Editar el articulo seleccionado");
			editBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new EditStockItem(mainPanel);
				}

			});
			add(editBtn);
		}

		if (StoreCore.getAccess("BorrarInventario")) {
			JButton delBtn = new JButton("Eliminar", imageLoader
					.getImage("button_cancel.png"));
			delBtn.setMnemonic('l');
			delBtn.setToolTipText("Borrar el articulo seleccionado");
			delBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (KardexDataManager.deleteStockItem(mainPanel
							.getSelectedID()))
						mainPanel.updateData("Articulo borrado exitosamente");
					else
						mainPanel
								.updateData("El articulo no se pudo eliminar. Verifique el kardex");
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
