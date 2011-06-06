package com.nova.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.UIManager;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.dat.loader.ImageLoader;
import com.nova.dat.parameter.GlobalConstants;
import com.nova.gui.util.StoreTip;

/**
 * Clase principal de la aplicacion, se encarga de inicializar la interfaz
 * grafica y de llamar a los metodos que hacen posible la ejecucion correcta de
 * Store
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class Store extends JFrame {

	private JDesktopPane desktop;

	private StoreMainMenu mainMenu;

	long tiempoInicial;

	private static Store store;

	/**
	 * Carga la aplicacion principal, validando previamente el usuario y
	 * cargando la clase que maneja las imagenes de la aplicacion
	 * <p>
	 * Creation date 15/04/2006 - 12:19:42 PM
	 * 
	 * @since 1.0
	 */
	public Store() {
		System.out.println("Inicializando store");
		LoadingFrame.show(this);
		tiempoInicial = System.currentTimeMillis();

		setLookAndFeel();
		if (validateUser()) {
			loadMainWindow(StoreSession.getUserLongName());
		}
	}

	/**
	 * Determina el look and feel de la aplicacion
	 * <p>
	 * Creation date 15/04/2006 - 12:19:53 PM
	 * 
	 * @since 1.0
	 */
	private void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(GlobalConstants.LOOK_AND_FEEL);
		} catch (Exception e) {
			ErrorLogLoader.addErrorEntry(e);
			System.out
					.println("Ejecutando Look & Feel Alternativo Store GUI Lite");
		}
	}

	/**
	 * Carga la ventana de validacion de usuario
	 * 
	 * @return True, si la validacion fue correcta
	 * @since 1.0
	 */
	private boolean validateUser() {
		StoreSession session = new StoreSession(tiempoInicial);
		return session.isUserValid();
	}

	/**
	 * Carga la ventana principal de la aplicacion
	 * 
	 * @param userName
	 *            Nombre del usuario validado
	 * @since 1.0
	 */
	private void loadMainWindow(String userName) {
		desktop = new JDesktopPane();
		mainMenu = new StoreMainMenu(desktop);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				StoreCore.optimizeTables();
				System.exit(0);
			}

			@Override
			public void windowOpened(WindowEvent e) {
				try {
					if (Boolean.parseBoolean(StoreCore.getProperty("SHOW_TIP"))) {
						new StoreTip();
					}
				} catch (Exception e1) {
					ErrorLogLoader.addErrorEntry(e1);
					e1.printStackTrace();
				}
				super.windowOpened(e);
			}
		});

		setContentPane(desktop);
		setJMenuBar(mainMenu);
		try {
			setTitle("Store v" + StoreCore.getProperty("VERSION") + " - "
					+ userName + " - " + StoreCore.getProperty("OWNER")
					+ " - developed by Camilo Nova.");
		} catch (Exception e1) {
			ErrorLogLoader.addErrorEntry(e1);
			e1.printStackTrace();
		}
		setSize(800, 575);
		setExtendedState(MAXIMIZED_BOTH);
		setIconImage(ImageLoader.getInstance().getImage("Store.gif").getImage());
		LoadingFrame.hide();
		setVisible(true);
	}

	/**
	 * Metodo principal de la aplicacion ejecuta esta clase
	 * 
	 * @param args	Sin uso
	 * @since 1.0
	 */
	public static void main(String[] args) {
		// TODO Cargar una sola instancia utilizando el ClassLoader
		store = new Store();
	}
	
	/**
	 * Retorna el frame principal de la aplicacion
	 * <p>Creation date 20/06/2006 - 05:58:26 PM
	 *
	 * @return		Frame principal de la aplicacion
	 * @since 1.0
	 */
	public static Store getStoreMainFrame() {
		return store;
	}
}
