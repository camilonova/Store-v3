package com.nova.modules.users.log;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.nova.dat.db.StoreCore;
import com.nova.dat.parameter.GlobalConstants;
import com.nova.log.cryptoStore.CryptoStore;
import com.nova.log.input.validator.TextValidatorField;

/**
 * Esta abstraccion crea un GUI general para el manejo de la informacion de los
 * usuarios, inicialmente es utilizado por el comando de crear y editar usuario.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
abstract class UsersDataGUI extends JDialog {

	protected JButton accTotalBtn;

	protected JButton aceptarBtn;

	protected JButton cancelarBtn;

	protected ArrayList<JComponent> components;

	private int componente;

	/**
	 * Construye una interfaz para el manejo de la informacion de los usuarios.
	 * Los herederos de esta clase deben determinar el comportamiento del boton
	 * <i>aceptarBtn</i> y deben hacer visible la ventana.
	 * 
	 * @since 1.0
	 */
	public UsersDataGUI() {
		components = new ArrayList<JComponent>();
		JPanel infoPnl = new JPanel();
		JPanel permisosPnl = new JPanel();
		JScrollPane scrollPane = new JScrollPane(permisosPnl);
		JPanel buttonsPnl = new JPanel();
		accTotalBtn = new JButton("Acceso Total");
		aceptarBtn = new JButton("Aceptar");
		cancelarBtn = new JButton("Cancelar");

		JLabel userLbl = new JLabel("Usuario");
		userLbl.setPreferredSize(new Dimension(180, 20));
		infoPnl.add(userLbl);
		TextValidatorField userFld = new TextValidatorField(20, aceptarBtn,
				cancelarBtn);
		userFld.setPreferredSize(new Dimension(150, 20));
		infoPnl.add(userFld);
		components.add(userFld);

		JLabel passwordLbl = new JLabel("Password");
		passwordLbl.setPreferredSize(new Dimension(180, 20));
		infoPnl.add(passwordLbl);
		JPasswordField passwordFld = new JPasswordField();
		passwordFld.setPreferredSize(new Dimension(150, 20));
		infoPnl.add(passwordFld);
		components.add(passwordFld);

		JLabel nombreLbl = new JLabel("Nombre Completo");
		nombreLbl.setPreferredSize(new Dimension(120, 20));
		infoPnl.add(nombreLbl);
		TextValidatorField nombreFld = new TextValidatorField(aceptarBtn,
				cancelarBtn);
		nombreFld.setPreferredSize(new Dimension(210, 20));
		infoPnl.add(nombreFld);
		components.add(nombreFld);

		ArrayList<String> columnNames = StoreCore.getColumnNames("usuarios");
		for (componente = 5; componente < columnNames.size(); componente++) {
			JLabel label = new JLabel(columnNames.get(componente));
			label.setPreferredSize(new Dimension(180, 20));
			permisosPnl.add(label);

			JRadioButton radioButton1 = new JRadioButton("Si");
			radioButton1.setPreferredSize(new Dimension(150 / 2, 20));
			JRadioButton radioButton2 = new JRadioButton("No", true);
			radioButton2.setPreferredSize(new Dimension(150 / 2, 20));
			ButtonGroup group = new ButtonGroup();
			group.add(radioButton1);
			group.add(radioButton2);

			permisosPnl.add(radioButton1);
			permisosPnl.add(radioButton2);
			components.add(radioButton1);
		}

		infoPnl.setPreferredSize(new Dimension(390, 110));
		permisosPnl.setPreferredSize(new Dimension(350, componente * 23));
		infoPnl.setBorder(BorderFactory
				.createTitledBorder("Informacion Basica"));
		scrollPane.setBorder(BorderFactory
				.createTitledBorder("Permisos de acceso"));

		accTotalBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < components.size(); i++) {
					if (components.get(i) instanceof JRadioButton) {
						JRadioButton radioButton = (JRadioButton) components
								.get(i);
						radioButton.setSelected(true);
					}
				}
			}

		});
		cancelarBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}

		});
		buttonsPnl.add(accTotalBtn);
		buttonsPnl.add(aceptarBtn);
		buttonsPnl.add(cancelarBtn);

		add(infoPnl, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(buttonsPnl, BorderLayout.SOUTH);

		setTooltips();
		setSize(400, 400);
		setLocation((GlobalConstants.SCREEN_SIZE_WIDTH - getWidth()) / 2,
				(GlobalConstants.SCREEN_SIZE_HEIGHT - getHeight()) / 2);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setAlwaysOnTop(true);
		setResizable(false);
	}

	/**
	 * Determina los tooltips de los componentes en el formulario
	 * <p>
	 * Creation date 15/04/2006 - 09:53:45 AM
	 * 
	 * @since 1.0
	 */
	private void setTooltips() {
		accTotalBtn
				.setToolTipText("Dar todos los permisos de acceso al usuario");
		cancelarBtn.setToolTipText("Cancelar y cerrar la ventana");
	}

	/**
	 * Retorna los datos listos para enviar a la base de datos
	 * 
	 * @param newUserID
	 *            ID de la fila a la que pertenecen los datos
	 * @param isUpdate
	 *            True si es una actualizacion de datos.
	 * 
	 * @return Datos listos para enviar a DB
	 * @since 1.0
	 */
	protected ArrayList<String> getUserData(String newUserID, boolean isUpdate) {
		// Ultimo Acceso
		components.add(3, new JTextField("NOW()"));

		ArrayList<String> data = new ArrayList<String>();
		data.add(newUserID);
		for (int i = 0; i < components.size(); i++) {
			JComponent component = components.get(i);
			if (component instanceof JPasswordField) {
				JPasswordField passwordFld = (JPasswordField) component;
				String cadena = String.valueOf(passwordFld.getPassword());
				data.add(isUpdate ? cadena : CryptoStore.encriptar(cadena));
			} else if (component instanceof JTextField) {
				JTextField textField = (JTextField) component;
				data.add(textField.getText());
			} else if (component instanceof JRadioButton) {
				JRadioButton radioBtn = (JRadioButton) component;
				data.add(radioBtn.isSelected() ? "Si" : "No");
			}
		}

		return data;
	}
}
