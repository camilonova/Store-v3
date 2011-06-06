package com.nova.modules.quotation.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ImageLoader;
import com.nova.gui.shared.table.StoreTableRemoveRow;
import com.nova.gui.shared.table.StoreTableSearch;
import com.nova.modules.quotation.log.AddQuote;
import com.nova.modules.quotation.log.SeeQuote;

/**
 * Esta clase provee el acceso a los comandos del modulo de usuarios mediante
 * una barra de herramientas
 * 
 * @author Camilo Nova
 * @version 1.0
 */
class QuoteToolBar extends JToolBar {

	private final StoreTableSearch search;
	private final ImageLoader imageLoader;

	/**
	 * Construye una barra de herramientas y le agrega funcionalidad
	 * 
	 * @param mainPanel
	 *            Panel principal de usuarios
	 * @since 1.0
	 */
	public QuoteToolBar(final QuoteMainPanel mainPanel) {
		search = new StoreTableSearch(mainPanel);
		imageLoader = ImageLoader.getInstance();

		if (StoreCore.getAccess("AgregarCotizaciones")) {
			JButton newBtn = new JButton("Crear", imageLoader
					.getImage("new.png"));
			newBtn.setMnemonic('c');
			newBtn.setToolTipText("Crear una nueva cotizacion");
			newBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new AddQuote(mainPanel);
				}

			});
			add(newBtn);
		}
		if (StoreCore.getAccess("VerCotizaciones")) {
			JButton seeBtn = new JButton("Ver", imageLoader
					.getImage("see.png"));
			seeBtn.setMnemonic('v');
			seeBtn.setToolTipText("Ver la cotizacion seleccionada");
			seeBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new SeeQuote(mainPanel);
				}

			});
			add(seeBtn);
		}
		if (StoreCore.getAccess("BorrarCotizaciones")) {
			JButton delBtn = new JButton("Eliminar", imageLoader
					.getImage("del.png"));
			delBtn.setMnemonic('l');
			delBtn.setToolTipText("Borrar la cotizacion seleccionada");
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
