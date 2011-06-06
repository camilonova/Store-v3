package com.nova.modules.purchase.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ImageLoader;
import com.nova.gui.shared.table.StoreTableSearch;
import com.nova.modules.purchase.log.AddPurchase;
import com.nova.modules.purchase.log.SeePurchase;

/**
 * Esta clase provee el acceso a los comandos del modulo de compras mediante una
 * barra de herramientas
 * 
 * @author Camilo Nova
 * @version 1.0
 */
class PurchaseToolBar extends JToolBar {

	private final StoreTableSearch search;
	private final ImageLoader imageLoader;

	/**
	 * Construye una barra de herramientas y le agrega funcionalidad
	 * 
	 * @param mainPanel
	 *            Panel principal de compras
	 * @since 1.0
	 */
	public PurchaseToolBar(final PurchaseMainPanel mainPanel) {
		search = new StoreTableSearch(mainPanel);
		imageLoader = ImageLoader.getInstance();

		if (StoreCore.getAccess("AgregarCompra")) {
			JButton newBtn = new JButton("Crear", imageLoader
					.getImage("new.png"));
			newBtn.setMnemonic('c');
			newBtn.setToolTipText("Crear una nueva compra");
			newBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new AddPurchase(mainPanel);
				}

			});
			add(newBtn);
		}
		if (StoreCore.getAccess("VerCompra")) {
			JButton seeBtn = new JButton("Ver", imageLoader
					.getImage("see.png"));
			seeBtn.setMnemonic('v');
			seeBtn.setToolTipText("Ver la compra seleccionada");
			seeBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new SeePurchase(mainPanel);
				}

			});
			add(seeBtn);
		}

		addSeparator();
		add(search.getSearchBtn());
		add(search.getBackBtn());
		add(search.getNextBtn());

		setFloatable(false);
	}
}
