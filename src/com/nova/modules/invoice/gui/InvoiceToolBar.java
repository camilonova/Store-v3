package com.nova.modules.invoice.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ImageLoader;
import com.nova.gui.shared.table.StoreTableSearch;
import com.nova.modules.invoice.log.AddInvoice;
import com.nova.modules.invoice.log.SeeInvoice;

/**
 * Esta clase provee el acceso a los comandos del modulo de facturacion mediante
 * una barra de herramientas
 * 
 * @author Camilo Nova
 * @version 1.0
 */
class InvoiceToolBar extends JToolBar {

	private final StoreTableSearch search;
	private final ImageLoader imageLoader;

	/**
	 * Construye una barra de herramientas y le agrega funcionalidad
	 * 
	 * @param mainPanel
	 *            Panel principal de usuarios
	 * @since 1.0
	 */
	public InvoiceToolBar(final InvoiceMainPanel mainPanel) {
		search = new StoreTableSearch(mainPanel);
		imageLoader = ImageLoader.getInstance();

		if (StoreCore.getAccess("AgregarFacturacion")) {
			JButton newBtn = new JButton("Crear", imageLoader
					.getImage("new.png"));
			newBtn.setMnemonic('c');
			newBtn.setToolTipText("Crear una nueva factura");
			newBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new AddInvoice(mainPanel);
				}

			});
			add(newBtn);
		}
		if (StoreCore.getAccess("VerFacturacion")) {
			JButton seeBtn = new JButton("Ver", imageLoader
					.getImage("see.png"));
			seeBtn.setMnemonic('v');
			seeBtn.setToolTipText("Ver la factura seleccionada");
			seeBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new SeeInvoice(mainPanel);
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
