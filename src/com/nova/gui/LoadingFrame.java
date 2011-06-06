package com.nova.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Window;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

import com.nova.dat.loader.ErrorLogLoader;

/**
 * Esta clase provee los metodos para mostrar en pantalla una imagen de
 * carga mientras se ejecuta un proceso.
 * @author Camilo Nova
 * @version 1.0
 */
public class LoadingFrame {

	private static JDialog window;

	/**
	 * Muestra la ventana de carga que bloquea mientras se ejecuta el proceso
	 * <p>Creation date 20/06/2006 - 04:31:46 PM
	 *
	 * @param owner		Ventana generadora del evento
	 * @since 1.0
	 */
	public synchronized static void show(final Window owner) {
		if (window == null && owner != null) {
			Thread showThread = new Thread(new Runnable() {

				@SuppressWarnings("synthetic-access")
				public void run() {
					if (owner instanceof Frame) {
						window = new JDialog((Frame) owner);
					} else if (owner instanceof Dialog) {
						window = new JDialog((Dialog) owner);
					} else {
						window = new JDialog();
					}
					window.setFocusableWindowState(false);
					window.setUndecorated(true);

					URLClassLoader urlLoader = (URLClassLoader) owner
							.getClass().getClassLoader();
					URL fileLoc = urlLoader.findResource("images/loading.gif");
					Image img = owner.getToolkit().createImage(fileLoc);

					MediaTracker tracker = new MediaTracker(owner);
					tracker.addImage(img, 0);
					try {
						tracker.waitForID(0);
						if (tracker.isErrorAny()) {
							return;
						}
					} catch (Exception ex) {
						ErrorLogLoader.addErrorEntry(ex);
						ex.printStackTrace();
					}
					JLabel l = new JLabel(new ImageIcon(img));

					window.add(l, BorderLayout.CENTER);
					window.pack();

					Dimension screenSize = Toolkit.getDefaultToolkit()
							.getScreenSize();
					Dimension labelSize = l.getPreferredSize();
					window.setLocation(screenSize.width / 2
							- (labelSize.width / 2), screenSize.height / 2
							- (labelSize.height / 2));

					window.setAlwaysOnTop(true);
					window.setVisible(true);
				}

			}, "SplashShowThread");
			showThread.start();
		}
	}

	/**
	 * Cierra la ventana de carga. Este metodo debera ser llamado
	 * cuando se termine de realizar el proceso.
	 * <p>Creation date 20/06/2006 - 04:32:40 PM
	 *
	 * @since 1.0
	 */
	public static void hide() {
		if (window != null) {
			window.dispose();
			window = null;
		}
	}
}