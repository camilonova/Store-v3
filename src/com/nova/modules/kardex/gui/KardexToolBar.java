package com.nova.modules.kardex.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ImageLoader;
import com.nova.modules.kardex.log.AddKardex;
import com.nova.modules.kardex.log.KardexDataManager;

/**
 * Crea la barra de herramientas del modulo la cual permite el acceso a los
 * comandos propios del modulo
 * 
 * @author Camilo Nova
 * @version 1.0
 */
class KardexToolBar extends JToolBar {

	private final ImageLoader imageLoader;

	/**
	 * Agrega los botones y las funcionalidades respectivas a la barra de
	 * herramientas
	 * <p>
	 * Creation date 19/05/2006 - 10:10:06 AM
	 * 
	 * @param mainPanel
	 *            Panel principal del modulo
	 * 
	 * @since 1.0
	 */
	public KardexToolBar(final KardexMainPanel mainPanel) {
		imageLoader = ImageLoader.getInstance();

		if (StoreCore.getAccess("KardexAjustarCantidades")) {
			JButton newBtn = new JButton("Ajustar Cantidades", 
					imageLoader.getImage("ajust.png"));
			newBtn.setMnemonic('a');
			newBtn.setToolTipText("Agregar un nuevo cliente al sistema");
			newBtn.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					new AddKardex(mainPanel);
				}

			});
			add(newBtn);
		}

		JButton verifyBtn = new JButton("Verificar", imageLoader
				.getImage("verify.png"));
		verifyBtn.setMnemonic('v');
		verifyBtn.setToolTipText("Verifica los registros del elemento");
		verifyBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				KardexDataManager
						.checkStockItem(mainPanel.getSelectedBarCode());
				mainPanel.run();
				JOptionPane.showMessageDialog(mainPanel,
						"Producto verificado correctamente");
			}

		});
		add(verifyBtn);

		setFloatable(false);
		setEnabled(false);
	}

	@Override
	public void setEnabled(boolean enabled) {
		for (int i = 0; i < getComponentCount(); i++) {
			getComponent(i).setEnabled(enabled);
		}
		super.setEnabled(enabled);
	}

}
