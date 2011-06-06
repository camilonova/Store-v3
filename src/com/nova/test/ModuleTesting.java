package com.nova.test;

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.nova.dat.parameter.GlobalConstants;
import com.nova.gui.StoreSession;
import com.nova.modules.utilities.reports.ReportBuilder;

/**
 * Clase que ejecuta las pruebas de los modulos de la aplicacion
 * @author Camilo Nova
 * @version 1.0
 */
public class ModuleTesting extends JFrame {

	/**
	 * Constructor, llama al constructor del modulo apropiado
	 * <p>Creation date 23/06/2006 - 05:54:51 PM
	 *
	 * @since 1.0
	 */
	public ModuleTesting() {		
		StoreSession.setTestingUser(this);
		
		// comentariar los constructores de los modulos
		// new ManualInvoiceBuilder(this);
		// new StockBuilder(this);
		new ReportBuilder(this);
		
		setSize(300,150);
		//setSize(800, 600);
		setTitle("Testing Module. powered by NoVa");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * Modulo de pruebas
	 * <p>Creation date 23/06/2006 - 05:55:31 PM
	 *
	 * @param args	Sin uso
	 * @since 1.0
	 */
	public static void main(String[] args) {
		try {
			Class.forName(GlobalConstants.DB_DRIVER).newInstance();
			UIManager.setLookAndFeel(GlobalConstants.LOOK_AND_FEEL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		new ModuleTesting();
	}

	/**
	 * Retorna el ID del usuario de prueba
	 * <p>Creation date 23/06/2006 - 05:51:51 PM
	 *
	 * @return		ID del usuario de prueba
	 * @since 1.0
	 */
	public String getTestingID() {
		return "1";
	}

	/**
	 * Retorna el nombre del usuario de prueba
	 * <p>Creation date 23/06/2006 - 05:52:55 PM
	 *
	 * @return		Nombre del usuario de prueba
	 * @since 1.0
	 */
	public String getTestingUser() {
		return "Testing";
	}
}
