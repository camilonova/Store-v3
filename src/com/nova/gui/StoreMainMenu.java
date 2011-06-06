package com.nova.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.dat.loader.ImageLoader;
import com.nova.gui.util.StoreTip;
import com.nova.modules.Calculator;
import com.nova.modules.City;
import com.nova.modules.Customer;
import com.nova.modules.Groups;
import com.nova.modules.Invoice;
import com.nova.modules.Kardex;
import com.nova.modules.ManualInvoice;
import com.nova.modules.Module;
import com.nova.modules.PriceRead;
import com.nova.modules.Provider;
import com.nova.modules.Purchase;
import com.nova.modules.Quote;
import com.nova.modules.Reports;
import com.nova.modules.Stock;
import com.nova.modules.Tax;
import com.nova.modules.Users;
import com.nova.modules.customer.log.AddCustomer;
import com.nova.modules.provider.log.AddProvider;
import com.nova.modules.stock.log.AddStockItem;

/**
 * Esta clase representa el menu de la aplicacion que es generalizado, este menu
 * carga dinamicamente los modulos a los que el usuario tiene acceso.
 * <p>
 * Ademas es el punto de partida para la creacion de un nuevo modulo en el
 * sistema.
 * 
 * <p>
 * Las teclas de acceso rapido se definen asi: F1 Ayuda F2 Inventario (Asignado)
 * F3 Facturacion Manual (Asignado) F4 Cotizaciones (Asignado) F5 Compras
 * (Asignado) F6 Ventas (Asignado) F7 Usuarios (Asignado) F8 Clientes (Asignado)
 * F9 Proveedores (Asignado)
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class StoreMainMenu extends JMenuBar {

	final JDesktopPane desktopPane;
	final ImageLoader imageLoader;

	/**
	 * Prepara el menu principal
	 * 
	 * @param desktopPane
	 *            Frame principal
	 * @since 1.0
	 */
	public StoreMainMenu(JDesktopPane desktopPane) {
		this.desktopPane = desktopPane;
		this.imageLoader = ImageLoader.getInstance();
		makeStockMenu();
		makePOSMenu();
		makeThirdPartyMenu();
		makeUtilitiesMenu();

		makeToolsMenu();
		makeHelpMenu();
	}

	/**
	 * Prepara el menu de inventario, el cual contiene las facilidades para el
	 * acceso al inventario y al kardex. En general todo lo que tenga que ver
	 * con el inventario tiene lugar aqui
	 * <p>
	 * Creation date 15/04/2006 - 11:54:11 AM
	 * 
	 * @since 1.0
	 */
	private void makeStockMenu() {
		JMenu stockMnu = new JMenu("Inventario");
		boolean isAccesible = false;

		if (StoreCore.getAccess("Inventario")) {
			isAccesible = true;

			if (StoreCore.getAccess("AgregarInventario")) {
				JMenuItem addItem = new JMenuItem("Agregar Articulo");
				addItem.setMnemonic('a');
				addItem.setToolTipText("Agregar rapidamente un nuevo articulo");
				addItem.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						new AddStockItem("");
					}

				});
				stockMnu.add(addItem);
			}

			JMenuItem menuItem = new JMenuItem("Listar Inventario",
					imageLoader.getImage("world.png"));
			setMenuItemSpecialFeatures(menuItem, 'v', "Ver el inventario",
					KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), Stock.class);
			stockMnu.add(menuItem);
			stockMnu.addSeparator();
		}
		if (StoreCore.getAccess("Kardex")) {
			isAccesible = true;
			JMenuItem menuItem = new JMenuItem("Ver Kardex", 
					imageLoader.getImage("kardexSingle.png"));
			setMenuItemSpecialFeatures(menuItem, 'k', "Ver el kardex", null,
					Kardex.class);
			stockMnu.add(menuItem);
		}
		if (isAccesible) {
			stockMnu.setMnemonic('i');
			add(stockMnu);
		}
	}

	/**
	 * Prepara el menu de movimiento de mercancia, contiene las facilidades para
	 * cotizar, comprar y vender elementos del inventario.
	 * <p>
	 * Creation date 10/05/2006 - 06:49:28 PM
	 * 
	 * @since 1.0
	 */
	private void makePOSMenu() {
		JMenu POSMnu = new JMenu("Punto de Venta");
		boolean isAccesible = false;

		if (StoreCore.getAccess("Cotizaciones")) {
			isAccesible = true;
			JMenuItem menuItem = new JMenuItem("Cotizaciones", 
					imageLoader.getImage("quote.png"));
			setMenuItemSpecialFeatures(menuItem, 't',
					"Ver el listado de cotizaciones", KeyStroke.getKeyStroke(
							KeyEvent.VK_F4, 0), Quote.class);
			POSMnu.add(menuItem);
		}
		if (StoreCore.getAccess("Compra")) {
			isAccesible = true;
			JMenuItem menuItem = new JMenuItem("Compras", 
					imageLoader.getImage("purchase.png"));
			setMenuItemSpecialFeatures(menuItem, 'c',
					"Ver el listado de facturas", KeyStroke.getKeyStroke(
							KeyEvent.VK_F5, 0), Purchase.class);

			POSMnu.add(menuItem);
		}
		if (StoreCore.getAccess("Facturacion")) {
			isAccesible = true;
			JMenuItem menuItem = new JMenuItem("Facturacion", 
					imageLoader.getImage("invoice.png"));
			setMenuItemSpecialFeatures(menuItem, 'f',
					"Ver el listado de facturas", KeyStroke.getKeyStroke(
							KeyEvent.VK_F6, 0), Invoice.class);

			POSMnu.add(menuItem);
		}
		if (isAccesible) {
			POSMnu.setMnemonic('p');
			add(POSMnu);
		}
	}

	/**
	 * Prepara el menu de terceros, que son las personas ajenas a la empresa,
	 * como los clientes, proveedores y empleados.
	 * <p>
	 * Creation date 6/04/2006 - 09:05:00 AM
	 * 
	 * @since 1.0
	 */
	private void makeThirdPartyMenu() {
		JMenu thirdPartyMnu = new JMenu("Terceros");
		boolean isAccesible = false;

		if (StoreCore.getAccess("Clientes")) {
			isAccesible = true;
			JMenu customerMnu = new JMenu("Clientes");

			if (StoreCore.getAccess("AgregarClientes")) {
				JMenuItem addItem = new JMenuItem("Agregar Cliente");
				addItem.setMnemonic('c');
				addItem.setToolTipText("Agregar rapidamente un nuevo cliente");
				addItem.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						new AddCustomer("");
					}

				});
				customerMnu.add(addItem);
			}

			JMenuItem menuItem = new JMenuItem("Listar Clientes",
					imageLoader.getImage("customer.png"));
			setMenuItemSpecialFeatures(menuItem, 'l',
					"Ver el listado de clientes", KeyStroke.getKeyStroke(
							KeyEvent.VK_F8, 0), Customer.class);
			customerMnu.add(menuItem);

			customerMnu.setMnemonic('c');
			thirdPartyMnu.add(customerMnu);
		}
		if (StoreCore.getAccess("Proveedores")) {
			isAccesible = true;
			JMenu customerMnu = new JMenu("Proveedores");

			if (StoreCore.getAccess("AgregarProveedores")) {
				JMenuItem addItem = new JMenuItem("Agregar Proveedor");
				addItem.setMnemonic('p');
				addItem
						.setToolTipText("Agregar rapidamente un nuevo proveedor");
				addItem.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e) {
						new AddProvider("");
					}

				});
				customerMnu.add(addItem);
			}

			JMenuItem menuItem = new JMenuItem("Listar Proveedores",
					imageLoader.getImage("provider.png"));
			setMenuItemSpecialFeatures(menuItem, 'l',
					"Ver el listado de proveedores", KeyStroke.getKeyStroke(
							KeyEvent.VK_F9, 0), Provider.class);
			customerMnu.add(menuItem);

			customerMnu.setMnemonic('p');
			thirdPartyMnu.add(customerMnu);
		}
		if (isAccesible) {
			thirdPartyMnu.setMnemonic('t');
			add(thirdPartyMnu);
		}
	}

	/**
	 * Prepara el menu de utilidades. Las utilidades generales de la aplicacion
	 * que permitan alguna facilidad independiente de los modulos principales
	 * tiene lugar aqui.
	 * 
	 * @since 1.0
	 */
	private void makeUtilitiesMenu() {
		JMenu utilitiesMnu = new JMenu("Utilidades");
		boolean isAccesible = false;

		if (StoreCore.getAccess("Reportes")) {
			isAccesible = true;
			JMenuItem menuItem = new JMenuItem("Reportes",
					imageLoader.getImage("reports.png"));
			setMenuItemSpecialFeatures(menuItem, 'r',
					"Ver los reportes del sistema", null,
					Reports.class);
			utilitiesMnu.add(menuItem);
		}
		if (StoreCore.getAccess("ConsultarPrecios")) {
			isAccesible = true;
			JMenuItem menuItem = new JMenuItem("Consultar Precios",
					imageLoader.getImage("money.gif"));
			setMenuItemSpecialFeatures(menuItem, 'p',
					"Consultar los precios de los productos", null,
					PriceRead.class);
			utilitiesMnu.add(menuItem);
		}
		if (StoreCore.getAccess("Calculadora")) {
			isAccesible = true;
			JMenuItem menuItem = new JMenuItem("Calculadora Impuesto",
					imageLoader.getImage("pc.gif"));
			setMenuItemSpecialFeatures(menuItem, 'c',
					"Calculadora de impuesto", null, Calculator.class);
			utilitiesMnu.add(menuItem);
		}
		utilitiesMnu.addSeparator();
		if (StoreCore.getAccess("Categorias")) {
			isAccesible = true;
			JMenuItem menuItem = new JMenuItem("Categorias Inventario",
					imageLoader.getImage("group.gif"));
			setMenuItemSpecialFeatures(menuItem, 'i',
					"Manejar las categorias del inventario", null, Groups.class);
			utilitiesMnu.add(menuItem);
		}
		if (StoreCore.getAccess("Impuestos")) {
			isAccesible = true;
			JMenuItem menuItem = new JMenuItem("Impuestos");
			setMenuItemSpecialFeatures(menuItem, 't',
					"Manejar el listado de impuestos", null, Tax.class);
			utilitiesMnu.add(menuItem);
		}
		if (StoreCore.getAccess("Ciudades")) {
			isAccesible = true;
			JMenuItem menuItem = new JMenuItem("Ciudades");
			setMenuItemSpecialFeatures(menuItem, 'u',
					"Manejar el listado de ciudades", null, City.class);
			utilitiesMnu.add(menuItem);
		}
		if (isAccesible) {
			utilitiesMnu.setMnemonic('u');
			add(utilitiesMnu);
		}
	}

	/**
	 * Prepara el menu de herramientas. Los modulos que no tienen mucha
	 * frecuencia de uso tienen lugar aqui, manejo de usuarios y facturacion
	 * manual.
	 * 
	 * @since 1.0
	 */
	private void makeToolsMenu() {
		JMenu toolsMnu = new JMenu("Herramientas");
		boolean isAccesible = false;

		if (StoreCore.getAccess("Usuarios")) {
			isAccesible = true;
			JMenuItem menuItem = new JMenuItem("Manejo de Usuarios",
					imageLoader.getImage("users.png"));

			setMenuItemSpecialFeatures(menuItem, 'm',
					"Manejar los usuarios del sistema", KeyStroke.getKeyStroke(
							KeyEvent.VK_F7, 0), Users.class);

			toolsMnu.add(menuItem);
		}
		if (StoreCore.getAccess("FacturacionManual")) {
			isAccesible = true;
			JMenuItem menuItem = new JMenuItem("Facturacion Manual",
					imageLoader.getImage("kwrite.png"));

			setMenuItemSpecialFeatures(menuItem, 'f',
					"Manejar la Facturacion Manual", KeyStroke.getKeyStroke(
							KeyEvent.VK_F3, 0), ManualInvoice.class);

			toolsMnu.add(menuItem);
		}
		if (isAccesible) {
			toolsMnu.setMnemonic('h');
			add(toolsMnu);
		}
	}

	/**
	 * Prepara el menu de ayuda. El cual no tiene verificacion de permisos
	 * 
	 * @since 1.0
	 */
	private void makeHelpMenu() {
		JMenu helpMnu = new JMenu("Ayuda");

		JMenuItem tipMenuItem = new JMenuItem("Tips...");
		tipMenuItem.setMnemonic('t');
		tipMenuItem.setToolTipText("Muestra los consejos de Store");
		tipMenuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new StoreTip();
			}

		});

		helpMnu.add(tipMenuItem);
		helpMnu.setMnemonic('y');
		add(helpMnu);
	}

	/**
	 * Da al menu pasado por parametro las caracteristicas indicadas.
	 * 
	 * @param menuItem
	 *            Item a agregar las caracteristicas
	 * @param mNemonic
	 *            Mnemonic del item
	 * @param toolTip
	 *            Tooltip del item
	 * @param keyStroke
	 *            Tecla de acceso rapido
	 * @param module
	 *            Modulo a ejecutar por el item
	 * @since 1.0
	 */
	private void setMenuItemSpecialFeatures(JMenuItem menuItem, char mNemonic,
			String toolTip, KeyStroke keyStroke, Class module) {
		menuItem.setMnemonic(mNemonic);
		menuItem.setToolTipText(toolTip);
		menuItem.setAccelerator(keyStroke);
		menuItem.addActionListener(new CustomActionListener(module));
	}

	/**
	 * Esta clase provee un hilo para la carga de los modulos, esto permite que
	 * mientras se carga un modulo el usuario pueda hacer otras cosas.
	 * 
	 * @author Camilo Nova
	 * @version 1.0
	 */
	private class CustomActionListener implements ActionListener, Runnable {

		private final Class moduleClass;

		/**
		 * Constructor de la clase. Almacena el modulo a cargar
		 * 
		 * @param moduleClass
		 *            Modulo a cargar con el hilo
		 * @since 1.0
		 */
		public CustomActionListener(Class moduleClass) {
			this.moduleClass = moduleClass;
		}

		/**
		 * Arranca el hilo de carga de modulo
		 * 
		 * @since 1.0
		 */
		public void actionPerformed(ActionEvent e) {
			LoadingFrame.show(Store.getStoreMainFrame());
			Thread thread = new Thread(this, "LoadModule");
			thread.start();
		}

		/**
		 * Carga el modulo y lo agrega al frame principal en paralelo. Verifica
		 * que solo exista un modulo abierto.
		 * 
		 * @since 1.0
		 */
		public void run() {
			Module module = null;
			try {
				// Verificamos si el modulo ya esta abierto
				for (int i = 0; i < desktopPane.getComponentCount(); i++) {
					Component component2 = desktopPane.getComponent(i);
					if (moduleClass.isInstance(component2)) {
						// Si lo esta, lo seleccionamos y retornamos
						LoadingFrame.hide();
						((JInternalFrame) component2).setSelected(true);
						return;
					}
				}
				module = (Module) moduleClass.newInstance();
				desktopPane.add((Component) module);
				LoadingFrame.hide();
				((JInternalFrame) module).setSelected(true);
			} catch (InstantiationException e) {
				ErrorLogLoader.addErrorEntry(e);
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				ErrorLogLoader.addErrorEntry(e);
				e.printStackTrace();
			} catch (PropertyVetoException e) {
				ErrorLogLoader.addErrorEntry(e);
				e.printStackTrace();
			}
		}
	}
}
