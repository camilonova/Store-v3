package com.nova.modules.users.log;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import com.nova.dat.db.StoreCore;
import com.nova.dat.parameter.GlobalConstants;
import com.nova.log.cryptoStore.CryptoStore;
import com.nova.modules.users.gui.UsersMainPanel;

/**
 * Esta clase permite el cambio de contraseña para un usuario registrado en el
 * sistema
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class ChangeUserPassword extends JDialog {

	String selectedID;

	String userName;

	JPasswordField nuevoFld;

	JPasswordField confirmadoFld;

	/**
	 * Construye la interfaz de usuario y da la funcionalidad de cambio de
	 * contraseña para el usuario seleccionado en la tabla
	 * 
	 * @param mainPanel
	 *            Panel principal de usuarios
	 * @version 1.0
	 */
	public ChangeUserPassword(final UsersMainPanel mainPanel) {
		selectedID = mainPanel.getSelectedID();
		if (selectedID == null) {
			JOptionPane.showMessageDialog(null, "Debe seleccionar un usuario");
			return;
		}

		userName = StoreCore.getDataAt("usuarios", "ID", selectedID, "Usuario");
		JPanel dataPnl = new JPanel();
		JPanel buttonsPnl = new JPanel();

		JLabel label1 = new JLabel("Nueva Contraseña:");
		label1.setPreferredSize(new Dimension(130, 20));
		nuevoFld = new JPasswordField();
		nuevoFld.setPreferredSize(new Dimension(100, 20));

		JLabel label2 = new JLabel("Confirme Contraseña:");
		label2.setPreferredSize(new Dimension(130, 20));
		confirmadoFld = new JPasswordField();
		confirmadoFld.setPreferredSize(new Dimension(100, 20));

		dataPnl.add(label1);
		dataPnl.add(nuevoFld);
		dataPnl.add(label2);
		dataPnl.add(confirmadoFld);

		JButton aceptarBtn = new JButton("Cambiar");
		aceptarBtn
				.setToolTipText("Cambia la contraseña actual por la digitada arriba");
		aceptarBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String cad1 = String.valueOf(nuevoFld.getPassword());
				String cad2 = String.valueOf(confirmadoFld.getPassword());
				if (cad1.equals(cad2)) {
					StoreCore.setDataAt("usuarios", "ID", selectedID,
							"Password", CryptoStore.encriptar(cad1));
					dispose();
					mainPanel
							.updateData("Contraseña cambiada para " + userName);
				} else {
					JOptionPane.showMessageDialog(null,
							"Las contraseñas no coinciden");
					nuevoFld.setText("");
					confirmadoFld.setText("");
					nuevoFld.requestFocus();
				}
			}

		});
		JButton cancelarBtn = new JButton("Cancelar");
		cancelarBtn.setToolTipText("Cancelar y cerrar la ventana");
		cancelarBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}

		});
		buttonsPnl.add(aceptarBtn);
		buttonsPnl.add(cancelarBtn);

		add(dataPnl, BorderLayout.CENTER);
		add(buttonsPnl, BorderLayout.SOUTH);

		setTitle("Cambio de contraseña para " + userName);
		setSize(280, 120);
		setLocation((GlobalConstants.SCREEN_SIZE_WIDTH - getWidth()) / 2,
				(GlobalConstants.SCREEN_SIZE_HEIGHT - getHeight()) / 2);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setAlwaysOnTop(true);
		setResizable(false);
		setVisible(true);
	}
}
