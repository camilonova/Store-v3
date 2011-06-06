package com.nova.log.form.dat;

import java.util.ArrayList;

import javax.swing.JInternalFrame;

import com.nova.gui.shared.table.StoreTablePanel;
import com.nova.log.form.gui.Form;
import com.nova.modules.ModuleBuilder;

/**
 * Clase que abstrae los modulos que requieren de la funcionalidad de formato.
 * Todos los modulos que deseen utilizar el formato y registrar la informacion
 * que este les facilita deben heredar de esta clase.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public abstract class ModuleForm extends StoreTablePanel {

	protected FormRegister register;

	/**
	 * Constructor encargado de llamar al constructor de StoreTablePanel
	 * utilizando los mismos parametros
	 * <p>
	 * Creation date 27/05/2006 - 10:41:37 AM
	 * 
	 * @param moduleBuilder
	 *            Modulo constructor
	 * @param internalFrame
	 *            Ventana interna del modulo
	 * 
	 * @since 1.0
	 */
	public ModuleForm(ModuleBuilder moduleBuilder, JInternalFrame internalFrame) {
		super(moduleBuilder, internalFrame);
	}

	/**
	 * Retorna la instancia de la clase encargada del registro de la informacion
	 * del formato en basado en el modulo
	 * <p>
	 * Creation date 27/05/2006 - 10:38:30 AM
	 * 
	 * @param form
	 *            Formato con datos a registrar
	 * @since 1.0
	 */
	public abstract void registerData(Form form);

	/**
	 * Retorna los elementos que permite operar el modulo. Estos varian
	 * dependiendo las caracteristicas basicas.
	 * <p>
	 * El modulo de facturacion, solamente permite productos y servicios con
	 * cantidad mayor a cero.
	 * <p>
	 * El modulo de compras, solamente permite productos.
	 * <p>
	 * Creation date 31/05/2006 - 09:23:46 PM
	 * 
	 * @return Elementos a mostrar en el formato
	 * @since 1.0
	 */
	public abstract ArrayList<String> getFormItems();

}
