package com.nova.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.dat.parameter.GlobalConstants;
import com.nova.log.cryptoStore.CryptoStore;
import com.nova.log.input.validator.TextValidatorField;
import com.nova.test.ModuleTesting;

/**
 * Esta clase se encarga de proveer la interfaz de validacion de usuario de la
 * aplicacion. Para saber si el usuario se ha validado correctamente se debe
 * llamar al metodo <i>IsUserValid()</i> que retorna un booleano con el
 * resultado de la validacion.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class StoreSession extends JDialog implements Runnable {

	TextValidatorField usuarioFld;

	JPasswordField passwordFld;

	JButton entrarBtn;

	JButton cancelarBtn;

	boolean flag = false;

	private final long tiempoInicial;

	private static String userID;

	private static String userName;

	private static String userLongName;

	/**
	 * Inicia una sesion en la aplicacion validando el usuario.
	 * <p>
	 * El boton de entrar permanece desabilitado mientras que la conexion con la
	 * base de datos no se haya realizado
	 * 
	 * @param tiempoInicial
	 *            Tiempo en que la aplicacion inicio
	 * 
	 * @since 1.0
	 */
	protected StoreSession(long tiempoInicial) {
		this.tiempoInicial = tiempoInicial;
		setLayout(new GridLayout(3, 2));

		entrarBtn = new JButton("Entrar");
		cancelarBtn = new JButton("Cancelar");
		passwordFld = new JPasswordField();
		usuarioFld = new TextValidatorField(passwordFld, cancelarBtn);

		entrarBtn.setEnabled(false);
		Thread conexionDB = new Thread(this, "ConexionDB");
		conexionDB.start();

		passwordFld.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					entrarBtn.doClick();
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					cancelarBtn.doClick();
			}
		});

		entrarBtn.setToolTipText("Validar datos y entrar");
		cancelarBtn.setToolTipText("Salir de la aplicacion");

		entrarBtn.addActionListener(new ActionListener() {

			@SuppressWarnings("synthetic-access")
			public void actionPerformed(ActionEvent e) {
				String user = usuarioFld.getText();
				String pass = CryptoStore.encriptar(String
						.copyValueOf(passwordFld.getPassword()));

				userID = StoreCore.startSession(user, pass);
				if (userID != null) {
					LoadingFrame.show(StoreSession.this);
					userName = StoreCore.getDataAt("usuarios", "ID", userID,
							"Usuario");
					userLongName = StoreCore.getDataAt("usuarios", "ID",
							userID, "Nombre");
					flag = true;
					dispose();
				} else {
					usuarioFld.setText(null);
					passwordFld.setText(null);
					usuarioFld.requestFocus();
				}
			}
		});

		cancelarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				System.exit(0);
			}
		});

		add(new JLabel("Usuario:"));
		add(usuarioFld);
		add(new JLabel("Password:"));
		add(passwordFld);
		add(entrarBtn);
		add(cancelarBtn);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cancelarBtn.doClick();
			}
		});

		setSize(200, 100);
		setLocation((GlobalConstants.SCREEN_SIZE_WIDTH - getWidth()) / 2,
				(GlobalConstants.SCREEN_SIZE_HEIGHT - getHeight()) / 2);
		setTitle("Valide su Acceso");
		setResizable(false);
		setModal(true);
		setAlwaysOnTop(true);
		LoadingFrame.hide();
		setVisible(true);
	}

	/**
	 * Inicializa las clases estaticas de la aplicacion, luego habilita el boton
	 * de entrar.
	 * <p>
	 * Creation date 4/06/2006 - 12:54:51 PM
	 * 
	 * @since 1.0
	 */
	public void run() {
		try {
			//TODO Crear una conexion que permanesca abierta mientras el usuario intenta logearse
			Class.forName(GlobalConstants.DB_DRIVER).newInstance();
			entrarBtn.setEnabled(true);
			System.out.println("Store cargado en "
					+ ((System.currentTimeMillis() - tiempoInicial) / 1000.00)
					+ " segundos\n");
		} catch (Exception e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * Retorna el resultado de la validacion del usuario
	 * <p>
	 * Creation date 4/06/2006 - 12:54:51 PM
	 * 
	 * @return Resultado de la validacion
	 * @since 1.0
	 */
	protected boolean isUserValid() {
		return flag;
	}

	/**
	 * Retorna el ID del usuario
	 * <p>
	 * Creation date 4/06/2006 - 12:54:51 PM
	 * 
	 * @return ID del usuario
	 * @since 1.0
	 */
	public static String getUserID() {
		return userID;
	}

	/**
	 * Retorna el nombre del usuario
	 * <p>
	 * Creation date 4/06/2006 - 12:54:51 PM
	 * 
	 * @return Nombre del usuario
	 * @since 1.0
	 */
	public static String getUserName() {
		return userName;
	}

	/**
	 * Retorna el nombre largo del usuario
	 * <p>
	 * Creation date 4/06/2006 - 12:54:51 PM
	 * 
	 * @return Nombre largo
	 * @since 1.0
	 */
	public static String getUserLongName() {
		return userLongName;
	}
	
	/**
	 * Determina el acceso con el modulo de pruebas
	 * <p>Creation date 23/06/2006 - 05:57:24 PM
	 *
	 * @param testing		Modulo de pruebas
	 * @since 1.0
	 */
	public static void setTestingUser(ModuleTesting testing) {
		userID = testing.getTestingID();
		userName = testing.getTestingUser();
	}

}
