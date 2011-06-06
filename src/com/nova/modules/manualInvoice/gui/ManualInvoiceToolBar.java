package com.nova.modules.manualInvoice.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ImageLoader;
import com.nova.gui.shared.table.StoreTableRemoveRow;
import com.nova.gui.shared.table.StoreTableSearch;
import com.nova.modules.manualInvoice.log.AddManualInvoice;
import com.nova.modules.manualInvoice.log.CancelManualInvoice;
import com.nova.modules.manualInvoice.log.EditManualInvoice;

/**
 * Esta clase provee el acceso a los comandos del modulo de usuarios mediante
 * una barra de herramientas
 * 
 * @author Camilo Nova
 * @version 1.0
 */
class ManualInvoiceToolBar extends JToolBar {

	private final StoreTableSearch search;
	private final ImageLoader imageLoader;

	/**
	 * Construye una barra de herramientas y le agrega funcionalidad
	 * 
	 * @param mainPanel
	 *            Panel principal de usuarios
	 * @since 1.0
	 */
	public ManualInvoiceToolBar(final ManualInvoiceMainPanel mainPanel) {
		search = new StoreTableSearch(mainPanel);
		imageLoader = ImageLoader.getInstance();

		if (StoreCore.getAccess("AgregarFacturacionManual")) {
			JButton newBtn = new JButton("Agregar", imageLoader
					.getImage("new.png"));
			newBtn.setMnemonic('a');
			newBtn.setToolTipText("Agregar una nueva factura");
			newBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new AddManualInvoice(mainPanel);
				}

			});
			add(newBtn);
		}

		if (StoreCore.getAccess("EditarFacturacionManual")) {
			JButton editBtn = new JButton("Editar", imageLoader
					.getImage("edit.png"));
			editBtn.setMnemonic('e');
			editBtn.setToolTipText("Editar la factura seleccionada");
			editBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new EditManualInvoice(mainPanel);
				}

			});
			add(editBtn);
		}

		if (StoreCore.getAccess("BorrarFacturacionManual")) {
			JButton delBtn = new JButton("Eliminar", imageLoader
					.getImage("del.png"));
			delBtn.setMnemonic('l');
			delBtn.setToolTipText("Borrar la factura seleccionada");
			delBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					String cliente = StoreCore.getDataAt(mainPanel.getTableName(), "Factura", mainPanel.getSelectedID(), "Cliente");
					// Eliminamos el registro de la factura manual
					new StoreTableRemoveRow(mainPanel);
					// Actualizamos el credito del cliente
					double credito = StoreCore.getManualInvoiceCreditValue(cliente);
					StoreCore.setDataAt("clientes", "Cliente", cliente, "Credito",
							String.valueOf(credito));

				}

			});
			add(delBtn);
		}

		addSeparator();
		if (StoreCore.getAccess("CancelarFacturacionManual")) {
			JButton delBtn = new JButton("Cancelar Factura", 
					imageLoader.getImage("cancel.png"));
			delBtn.setMnemonic('c');
			delBtn.setToolTipText("Cancelar la factura a credito seleccionada");
			delBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new CancelManualInvoice(mainPanel);
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
