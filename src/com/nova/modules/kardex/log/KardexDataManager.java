package com.nova.modules.kardex.log;

import java.awt.Window;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.rowset.WebRowSet;
import javax.swing.JOptionPane;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.log.math.StoreMath;
import com.nova.modules.stock.log.StockItem;

/**
 * Clase que concentra toda la logica para el manejo del kardex de la
 * aplicacion, cualquier operacion con los elementos del inventario debe hacerse
 * por medio de esta clase.
 * <p>
 * Se aclara que los servicios no tiene procesos en el kardex asi que solamente
 * se maneja para los productos.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class KardexDataManager {

	private static final String inOperation = "Entrada";

	private static final String outOperation = "Salida";

	private static final String manualOperation = "Manual";

	private static final String autoOperation = "Automatico";

	private static final String kardexTable = "kardex";

	private static final String stockTable = "inventario";

	private static String process;

	private static String barCode;

	private static String date;

	private static String operation;

	private static String type;

	private static String origin;

	private static String localUnits;

	private static String localCost;

	private static String localTotal;

	private static String globalUnits;

	private static String globalCost;

	private static String globalTotal;

	/**
	 * Crea un nuevo elemento en el inventario
	 * <p>
	 * Creation date 28/04/2006 - 07:24:16 PM
	 * 
	 * @param owner
	 *            Ventana que llama al metodo (para mostrar mensajes de error)
	 * @param data
	 *            Datos del elemento a ingresar
	 * @return True, si el proceso se concreto con exito
	 * @since 1.0
	 */
	public static boolean createStockItem(Window owner, ArrayList<String> data) {
		try {
			// Tomamos los datos
			StockItem stockItem = new StockItem(data);

			// Los servicios no tienen procesos en el kardex
			if (stockItem.getType().equals("Servicio")) {
				// Solamente ingresamos los datos al inventario
				StoreCore.newData(stockTable, data);
				return true;
			}

			process = StoreCore.getNextId(kardexTable, "Proceso");
			barCode = stockItem.getBarCode();
			date = "NOW()";
			operation = "Inventario Inicial";
			type = inOperation;
			origin = autoOperation;
			localUnits = stockItem.getQuantity();
			localCost = stockItem.getCost();
			localTotal = String.valueOf(StoreMath.multiply(Double
					.parseDouble(localUnits), Double.parseDouble(localCost),
					true));
			globalUnits = localUnits;
			globalCost = localCost;
			globalTotal = localTotal;

			// Validamos que no existan registros anteriores
			if (StoreCore.isRegisteredData(kardexTable, "Codigo", barCode)) {
				JOptionPane.showMessageDialog(owner,
						"El producto tiene registros vigentes en kardex\n"
								+ "Debe comprobar el kardex inmediatamente",
						"Error", JOptionPane.ERROR_MESSAGE);
				owner.dispose();
			} else {
				// Registramos la operacion
				StoreCore.newData(stockTable, data);
				StoreCore.newData(kardexTable, getKardexTableData());
				return true;
			}
		} catch (Exception e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
			return false;
		}

		return false;
	}

	/**
	 * Elimina los registros del articulo identificado por el parametro tanto
	 * del inventario como del kardex
	 * <p>
	 * Creation date 29/04/2006 - 04:13:59 PM
	 * 
	 * @param itemBarCode
	 *            Codigo de barras del articulo
	 * @return True, si la operacion fue existosa
	 * @since 1.0
	 */
	public static boolean deleteStockItem(String itemBarCode) {
		// Verificamos si esta seguro de eliminarlo
		int seleccion = JOptionPane
				.showConfirmDialog(
						null,
						"Esta seguro de eliminar el articulo "
								+ itemBarCode
								+ "\nEsto eliminara todos los procesos relacionados con el ",
						"Eliminar...", JOptionPane.YES_NO_OPTION);

		if (seleccion == JOptionPane.YES_OPTION) {
			// Eliminamos los registros
			StoreCore.removeRow(kardexTable, "Codigo", itemBarCode);
			StoreCore.removeRow(stockTable, "Codigo", itemBarCode);
			return true;
		}
		return false;
	}

	/**
	 * Prepara los datos del kardex para ser enviados a la base de datos
	 * <p>
	 * Creation date 29/04/2006 - 04:44:34 PM
	 * 
	 * @return Datos del kardex para enviar a la base de datos
	 * @since 1.0
	 */
	private static ArrayList<String> getKardexTableData() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(process);
		arrayList.add(barCode);
		arrayList.add(date);
		arrayList.add(operation);
		arrayList.add(type);
		arrayList.add(origin);
		arrayList.add(localUnits);
		arrayList.add(localCost);
		arrayList.add(localTotal);
		arrayList.add(globalUnits);
		arrayList.add(globalCost);
		arrayList.add(globalTotal);

		return arrayList;
	}

	/**
	 * Retorna las unidades que existiran despues de efectuada la operacion
	 * <p>
	 * Creation date 25/05/2006 - 07:38:28 PM
	 * 
	 * @return Unidades despues de efectuada la operacion
	 * @since 1.0
	 */
	private synchronized static int getUnitsAfter() {
		int unitsBefore = Integer.parseInt(StoreCore.getDataAt(stockTable,
				"Codigo", barCode, "Cantidad"));
		int operatedUnits = Integer.parseInt(localUnits);

		if (type.equals(inOperation))
			return unitsBefore + operatedUnits;
		return unitsBefore - operatedUnits;
	}

	/**
	 * Retorna el costo total de la operacion a realizar. El costo unitario se
	 * toma de dividir el valor retornado por la cantidad total de elementos del
	 * producto
	 * <p>
	 * Creation date 26/05/2006 - 07:31:47 AM
	 * 
	 * @return Costo total de la operacion
	 * @since 1.0
	 */
	private synchronized static double getCalculatedTotalCost() {
		double local = 0;
		double global = 0;

		try {
			if (type.equals(inOperation))
				local = Double.parseDouble(localTotal);
			else
				local = -Double.parseDouble(localTotal);

			// Buscamos el ultimo valor del saldo total
			WebRowSet rowSet = StoreCore.getRowSet();
			rowSet.setType(ResultSet.TYPE_SCROLL_INSENSITIVE);
			rowSet.setConcurrency(ResultSet.CONCUR_READ_ONLY);

			rowSet.setCommand("SELECT `SaldoTotal` "
					+ "FROM `kardex` WHERE `Codigo` = '" + barCode + "' "
					+ "ORDER BY `Proceso` DESC LIMIT 0,1");
			rowSet.execute();

			if (rowSet.next())
				global = Double.parseDouble(rowSet.getString(1));

			rowSet.close();
			rowSet = null;
		} catch (SQLException e) {
			// No deberia pasar
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}

		return StoreMath.add(global, local, true);
	}

	/**
	 * Ingresa una operacion manual del kardex.
	 * <p>
	 * El parametro debe ser de la forma: (codigo, descripcion de la operacion,
	 * tipo E o S, unidades, costo)
	 * <p>
	 * Creation date 26/05/2006 - 07:35:24 AM
	 * 
	 * @param operationData
	 *            Datos basicos de la operacion
	 * @since 1.0
	 */
	public static void insertKardexOperation(ArrayList<String> operationData) {
		process = StoreCore.getNextId(kardexTable, "Proceso");
		barCode = operationData.get(0);
		date = "NOW()";
		operation = operationData.get(1);
		type = operationData.get(2).equals("E") ? inOperation : outOperation;
		origin = manualOperation;
		localUnits = operationData.get(3);
		localCost = operationData.get(4);
		localTotal = String.valueOf(StoreMath.multiply(Double
				.parseDouble(localUnits), Double.parseDouble(localCost), true));

		// Calculamos los totales
		int unitsAfter = getUnitsAfter();
		double calculatedTotalCost = getCalculatedTotalCost();
		double calculatedCost;
		if (type.equals(inOperation))
			calculatedCost = StoreMath.divide(calculatedTotalCost, unitsAfter,
					true);
		else
			calculatedCost = Double.parseDouble(localCost);

		globalUnits = String.valueOf(unitsAfter);
		globalCost = String.valueOf(calculatedCost);
		globalTotal = String.valueOf(calculatedTotalCost);

		// Actualizamos e ingresamos los datos
		try {
			StoreCore.newData(kardexTable, getKardexTableData());
			StoreCore.setDataAt(stockTable, "Codigo", barCode, "Cantidad",
					globalUnits);
			StoreCore.setDataAt(stockTable, "Codigo", barCode, "Costo", globalCost);
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}

	}

	/**
	 * Verifica los procesos del kardex para el articulo identificado por el
	 * parametro.
	 * <p>
	 * Creation date 26/05/2006 - 07:57:51 AM
	 * 
	 * @param itemBarCode
	 *            Codigo del producto a verificar
	 * @since 1.0
	 */
	public synchronized static void checkStockItem(String itemBarCode) {
		try {
			WebRowSet rowSet = StoreCore.getRowSet();
			rowSet
					.setCommand("SELECT `Tipo` , `Unidades` , `Costo` , `Total` , "
							+ "`SaldoUnidades` , `SaldoCosto` , `SaldoTotal` "
							+ "FROM `"
							+ kardexTable
							+ "` WHERE `Codigo` = '"
							+ itemBarCode + "' " + "ORDER BY `Proceso` ASC");
			rowSet.execute();

			int unitsBefore = 0;
			double cost = 0;
			double totalBefore = 0;
			while (rowSet.next()) {
				int units = rowSet.getInt("Unidades");
				cost = rowSet.getDouble("Costo");
				double total = StoreMath.multiply(units, cost, true);

				if (rowSet.isFirst()) {
					rowSet.updateDouble("Total", total);
					rowSet.updateInt("SaldoUnidades", units);
					rowSet.updateDouble("SaldoCosto", cost);
					rowSet.updateDouble("SaldoTotal", total);
					unitsBefore = units;
					totalBefore = total;
				} else {
					if (rowSet.getString("Tipo").equals(inOperation)) {
						unitsBefore += units;
						totalBefore = StoreMath.add(totalBefore, total, true);
						cost = StoreMath.divide(totalBefore, unitsBefore, true);
					} else {
						unitsBefore -= units;
						totalBefore = StoreMath.subtract(totalBefore, total,
								true);
					}

					rowSet.updateDouble("Total", total);
					rowSet.updateInt("SaldoUnidades", unitsBefore);
					rowSet.updateDouble("SaldoCosto", cost);
					rowSet.updateDouble("SaldoTotal", totalBefore);
				}

				rowSet.updateRow();
			}
			rowSet.acceptChanges();
			StoreCore.setDataAt(stockTable, "Codigo", itemBarCode, "Cantidad",
					String.valueOf(unitsBefore));
			StoreCore.setDataAt(stockTable, "Codigo", itemBarCode, "Costo",
					String.valueOf(cost));

			rowSet.close();
			rowSet = null;
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
	}

	/**
	 * Registra la venta de unidades de un producto del inventario. Valida si es
	 * un servicio, en caso de serlo retorna false
	 * <p>
	 * Creation date 26/05/2006 - 04:28:00 PM
	 * 
	 * @param invoiceNumber
	 *            Numero de factura de venta
	 * @param itemBarCode
	 *            Codigo del producto a vender
	 * @param itemUnits
	 *            Unidades del producto a vender
	 * @return True, si el proceso fue correcto. False de lo contrario
	 * @since 1.0
	 */
	public static boolean sellStockItem(String invoiceNumber,
			String itemBarCode, int itemUnits) {
		if (StoreCore.getDataAt(stockTable, "Codigo", itemBarCode, "Tipo")
				.equals("Servicio"))
			return false;

		try {
			process = StoreCore.getNextId(kardexTable, "Proceso");
			barCode = itemBarCode;
			date = "NOW()";
			operation = "Venta Factura No " + invoiceNumber;
			type = outOperation;
			origin = autoOperation;
			localUnits = String.valueOf(itemUnits);
			localCost = StoreCore.getDataAt(stockTable, "Codigo", barCode,
					"Costo");
			localTotal = String.valueOf(StoreMath.multiply(itemUnits, Double
					.parseDouble(localCost), true));
			globalUnits = String.valueOf(getUnitsAfter());
			globalCost = localCost;
			globalTotal = String.valueOf(getCalculatedTotalCost());

			try {
				// Actualizamos e ingresamos los datos
				StoreCore.newData(kardexTable, getKardexTableData());
				StoreCore.setDataAt(stockTable, "Codigo", barCode, "Cantidad",
						globalUnits);
			} catch (SQLException e) {
				ErrorLogLoader.addErrorEntry(e);
				e.printStackTrace();
				return false;
			}

			return true;
		} catch (NumberFormatException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Registra la compra de unidades de un producto del inventario Valida si es
	 * un servicio, en caso de serlo retorna false
	 * <p>
	 * Creation date 26/05/2006 - 04:58:40 PM
	 * 
	 * @param purchaseNumber
	 *            Numero de factura de compra
	 * @param itemBarCode
	 *            Codigo del producto a comprar
	 * @param itemUnits
	 *            Unidades del producto a comprar
	 * @param itemCost
	 *            Costo de las unidades a comprar
	 * @return True, si el proceso fue correcto. False de lo contrario
	 * @since 1.0
	 */
	public static boolean buyStockItem(String purchaseNumber,
			String itemBarCode, int itemUnits, double itemCost) {
		if (StoreCore.getDataAt(stockTable, "Codigo", itemBarCode, "Tipo")
				.equals("Servicio"))
			return false;

		try {
			process = StoreCore.getNextId(kardexTable, "Proceso");
			barCode = itemBarCode;
			date = "NOW()";
			operation = "Compra Factura No " + purchaseNumber;
			type = inOperation;
			origin = autoOperation;
			localUnits = String.valueOf(itemUnits);
			localCost = String.valueOf(itemCost);
			localTotal = String.valueOf(StoreMath.multiply(itemUnits, Double
					.parseDouble(localCost), true));

			// Calculamos los totales
			int unitsAfter = getUnitsAfter();
			double calculatedTotalCost = getCalculatedTotalCost();
			double calculatedCost = StoreMath.divide(calculatedTotalCost,
					unitsAfter, true);

			globalUnits = String.valueOf(unitsAfter);
			globalCost = String.valueOf(calculatedCost);
			globalTotal = String.valueOf(calculatedTotalCost);

			try {
				// Actualizamos e ingresamos los datos
				StoreCore.newData(kardexTable, getKardexTableData());
				StoreCore.setDataAt(stockTable, "Codigo", barCode, "Cantidad",
						globalUnits);
				StoreCore.setDataAt(stockTable, "Codigo", barCode, "Costo",
						globalCost);
			} catch (SQLException e) {
				ErrorLogLoader.addErrorEntry(e);
				e.printStackTrace();
				return false;
			}

			return true;
		} catch (NumberFormatException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}

		return false;
	}

}