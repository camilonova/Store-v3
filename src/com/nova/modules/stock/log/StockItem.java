package com.nova.modules.stock.log;

import java.util.ArrayList;

import com.nova.dat.db.StoreCore;
import com.nova.log.math.StoreMath;

/**
 * Esta es una clase de prueba que pretende ser la version inicial de la
 * migracion de los items del inventario a clases. <b>Es importante crear este
 * tipo de clases ya que a futuro se debe investigar sobre hibernate y la
 * posibilidad de migrar la aplicacion a este estandar en conjunto con el J2EE.
 * Just Do It</b>
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class StockItem {
	// TODO Investigar como implementar un ENUM en esta clase

	private int barCode = 0;

	private int group = 1;

	private int quantity = 2;

	private int minimum = 3;

	private int description = 4;

	private int type = 5;

	private int brand = 6;

	private int packing = 7;

	private int tax = 8;

	private int sellPrice = 9;

	private int provider = 10;

	private int cost = 11;

	private int itemPlace = 12;

	private int image = 13;

	private int modifiedBy = 14;

	private int lastModification = 15;

	private ArrayList<String> stockItemData;

	private double priceBase;

	private double priceTax;

	/**
	 * Representa el identificador de codigo de barras
	 */
	public static int BARCODE = 1111;

	/**
	 * Representa el identificador de descripcion
	 */
	public static int DESCRIPTION = 2222;

	/**
	 * Representa el identificador de descripcion + marca
	 */
	public static int DESCRIPTION_BRAND = 3333;

	/**
	 * Obtiene los datos del articulo en base a los parametros y calcula las
	 * propiedades a retornar por los demas metodos
	 * <p>
	 * Creation date 23/04/2006 - 04:58:01 PM
	 * 
	 * @param identifier
	 *            Identificador del articulo
	 * @param identifierType
	 *            Tipo de identificador
	 * @throws Exception
	 *             En caso de que no exista el articulo
	 * 
	 * @since 1.0
	 */
	public StockItem(String identifier, int identifierType) throws Exception {
		String keyColumn;
		if (identifierType == BARCODE) {
			keyColumn = "Codigo";
		} else if (identifierType == DESCRIPTION) {
			keyColumn = "Descripcion";
		} else if (identifierType == DESCRIPTION_BRAND) {
			identifier = StoreCore.getStockBarCode(identifier);
			keyColumn = "Codigo";
		} else
			throw new Exception(
					"Ese tipo de identificador no valido o no existe");

		stockItemData = StoreCore.getAllRowData("inventario", keyColumn,
				identifier);
		if (stockItemData.size() == 0)
			throw new Exception("El producto " + identifier + " no existe!!!");

		double getTax = Double.valueOf(getTax());
		double getSell = Double.valueOf(getSellPrice());

		if (getTax > 0) {
			// Si el articulo tiene impuesto
			priceBase = StoreMath.divide(getSell, getTax + 1, true);
			priceTax = StoreMath.subtract(getSell, priceBase, true);
		} else {
			// Si no lo tiene
			priceBase = getSell;
			priceTax = 0;
		}
	}

	/**
	 * Crea un articulo de inventario en memoria y obtiene los datos para que
	 * sean manejados por la clase
	 * <p>
	 * Creation date 28/04/2006 - 12:01:18 PM
	 * 
	 * @param data
	 *            Datos del articulo a crear
	 * @throws Exception
	 *             Si los datos son incompatibles
	 * 
	 * @since 1.0
	 */
	@SuppressWarnings("unchecked")
	public StockItem(ArrayList<String> data) throws Exception {
		if (data.size() - 1 != lastModification)
			throw new Exception("Los datos no son compatibles!!!");

		// Clonamos los datos
		stockItemData = (ArrayList) data.clone();

		double getTax = Double.valueOf(getTax());
		double getSell = Double.valueOf(getSellPrice());

		if (getTax > 0) {
			// Si el articulo tiene impuesto
			priceBase = StoreMath.divide(getSell, getTax + 1, true);
			priceTax = StoreMath.subtract(getSell, priceBase, true);
		} else {
			// Si no lo tiene
			priceBase = getSell;
			priceTax = 0;
		}
	}

	/**
	 * Retorna el codigo de barras del articulo
	 * <p>
	 * Creation date 23/04/2006 - 05:09:41 PM
	 * 
	 * @return Codigo de barras
	 * @since 1.0
	 */
	public String getBarCode() {
		return stockItemData.get(barCode);
	}

	/**
	 * Retorna la categoria del articulo
	 * <p>
	 * Creation date 23/04/2006 - 05:10:00 PM
	 * 
	 * @return Categoria
	 * @since 1.0
	 */
	public String getGroup() {
		return stockItemData.get(group);
	}

	/**
	 * Retorna la cantidad de elementos del articulo
	 * <p>
	 * Creation date 23/04/2006 - 05:10:20 PM
	 * 
	 * @return Cantidad del articulo
	 * @since 1.0
	 */
	public String getQuantity() {
		return stockItemData.get(quantity);
	}

	/**
	 * Retorna el valor minimo de items del articulo
	 * <p>
	 * Creation date 23/04/2006 - 05:17:23 PM
	 * 
	 * @return Valor minimo de items
	 * @since 1.0
	 */
	public String getMinimum() {
		return stockItemData.get(minimum);
	}

	/**
	 * Retorna la descripcion del articulo
	 * <p>
	 * Creation date 23/04/2006 - 05:16:57 PM
	 * 
	 * @return Descripcion del articulo
	 * @since 1.0
	 */
	public String getDescription() {
		return stockItemData.get(description);
	}

	/**
	 * Retorna el tipo de articulo (Producto o Servicio)
	 * <p>
	 * Creation date 23/04/2006 - 05:16:36 PM
	 * 
	 * @return Tipo de articulo
	 * @since 1.0
	 */
	public String getType() {
		return stockItemData.get(type);
	}

	/**
	 * Retorna la marca del articulo
	 * <p>
	 * Creation date 23/04/2006 - 05:16:22 PM
	 * 
	 * @return Marca del articulo <b>Puede ser NULL</b>
	 * @since 1.0
	 */
	public String getBrand() {
		return stockItemData.get(brand);
	}

	/**
	 * Retorna el empaque del articulo
	 * <p>
	 * Creation date 23/04/2006 - 05:16:06 PM
	 * 
	 * @return Empaque del articulo <b>Puede ser NULL</b>
	 * @since 1.0
	 */
	public String getPacking() {
		return stockItemData.get(packing);
	}

	/**
	 * Retorna el valor del impuesto del articulo
	 * <p>
	 * Creation date 23/04/2006 - 05:15:21 PM
	 * 
	 * @return Valor del impuesto
	 * @since 1.0
	 */
	public String getTax() {
		return stockItemData.get(tax);
	}

	/**
	 * Retorna el precio de venta del articulo
	 * <p>
	 * Creation date 23/04/2006 - 05:14:38 PM
	 * 
	 * @return Precio de venta
	 * @since 1.0
	 */
	public String getSellPrice() {
		return stockItemData.get(sellPrice);
	}

	/**
	 * Retorna el nombre del proveedor del articulo
	 * <p>
	 * Creation date 23/04/2006 - 05:14:13 PM
	 * 
	 * @return Proveedor del articulo <b>Puede ser NULL</b>
	 * @since 1.0
	 */
	public String getProvider() {
		return stockItemData.get(provider);
	}

	/**
	 * Retorna el costo del articulo
	 * <p>
	 * Creation date 23/04/2006 - 05:13:58 PM
	 * 
	 * @return Costo del articulo
	 * @since 1.0
	 */
	public String getCost() {
		return stockItemData.get(cost);
	}

	/**
	 * Retorna la ubicacion fisica del elemento
	 * <p>
	 * Creation date 23/04/2006 - 05:13:34 PM
	 * 
	 * @return Ubicacion fisica del elemento
	 * @since 1.0
	 */
	public String getItemPlace() {
		return stockItemData.get(itemPlace);
	}

	/**
	 * Retorna la ruta de la imagen
	 * <p>
	 * Creation date 23/04/2006 - 05:13:21 PM
	 * 
	 * @return Ruta de la imagen <b>Puede ser NULL</b>
	 * @since 1.0
	 */
	public String getImage() {
		return stockItemData.get(image);
	}

	/**
	 * Retorna el usuario que modifico el articulo
	 * <p>
	 * Creation date 2/05/2006 - 06:32:47 PM
	 * 
	 * @return Usuario que modifico el articulo
	 * @since 1.0
	 */
	public String getModifiedBy() {
		return stockItemData.get(modifiedBy);
	}

	/**
	 * Retorna la fecha de ultima modificacion
	 * <p>
	 * Creation date 23/04/2006 - 05:13:03 PM
	 * 
	 * @return Fecha de ultima modificacion
	 * @since 1.0
	 */
	public String getLastModification() {
		return stockItemData.get(lastModification);
	}

	/**
	 * Retorna el valor neto del precio del articulo. P.E: Precio publico=2900
	 * con impuesto del 16%. Retorna 2500
	 * <p>
	 * Creation date 23/04/2006 - 05:12:17 PM
	 * 
	 * @return Valor neto del articulo
	 * @since 1.0
	 */
	public double getPriceBase() {
		return priceBase;
	}

	/**
	 * Retorna el valor del impuesto en el precio del articulo P.E: Precio
	 * publico=2900 con impuesto del 16%. Retorna 400
	 * <p>
	 * Creation date 23/04/2006 - 05:10:49 PM
	 * 
	 * @return Valor del impuesto en el precio de venta
	 * @since 1.0
	 */
	public double getPriceTax() {
		return priceTax;
	}

}
