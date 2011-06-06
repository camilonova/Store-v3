package com.nova.modules;

/**
 * Interface que abstrae los constructores de modulos, para generalizarlos en un
 * solo grupo y poder darle caracteristicas generales por igual.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public interface ModuleBuilder {

	/**
	 * Retorna el nombre de la tabla relacionada con el modulo Creation date
	 * 30/03/2006 - 11:57:22 AM
	 * 
	 * @return Nombre de la tabla relacionada con el modulo
	 * @since 1.0
	 */
	public String getRelatedTableName();

	/**
	 * Retorna la columna por la cual deben ser ordenados los datos Creation
	 * date 30/03/2006 - 11:57:30 AM
	 * 
	 * @return Columna de ordenacion
	 * @since 1.0
	 */
	public String getOrderColumn();

	/**
	 * Retorna el tipo de orden de los datos Creation date 30/03/2006 - 11:58:32
	 * AM
	 * 
	 * @return Tipo de orden de los datos
	 * @since 1.0
	 */
	public String getOrderType();

	/**
	 * Determina el mensaje a mostrar en la barra de estado del modulo Creation
	 * date 30/03/2006 - 11:59:02 AM
	 * 
	 * @param message
	 *            Mensaje a mostrar en la barra de estado
	 * @since 1.0
	 */
	public void setStatusBarText(String message);
}
