package com.nova.log.math;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;

/**
 * Esta clase provee los metodos para hacer operaciones entre los numeros de la
 * aplicacion
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class StoreMath {

	private static final NumberFormat numberFormat;

	private static final int PRESICION;

	private static BigDecimal extendedNumber;

	private StoreMath() {
		// Nadie instancia la clase por ser estatica
	}

	/**
	 * Metodo estatico que se ejecuta al momento de instanciar la clase
	 */
	static {
		int a = 0;
		try {
			a = Integer.parseInt(StoreCore.getProperty("FRACTIONS"));
		} catch (Exception e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		numberFormat = NumberFormat.getCurrencyInstance();
		PRESICION = a;
	}

	/**
	 * Suma los numeros recibidos por parametro
	 * <p>
	 * Creation date 28/04/2006 - 12:30:21 PM
	 * 
	 * @param aNumber
	 *            Numero inicial
	 * @param bNumber
	 *            Numero a sumar
	 * @param round
	 *            True, si quiere redondear el resultado
	 * @return Numero operado
	 * @since 1.0
	 */
	public static double add(double aNumber, double bNumber, boolean round) {
		numberFormat.setMaximumFractionDigits(PRESICION);
		extendedNumber = new BigDecimal(aNumber);
		BigDecimal bigDecimal = new BigDecimal(bNumber);

		extendedNumber = extendedNumber.add(bigDecimal);

		if (round)
			return round(extendedNumber.doubleValue());
		return extendedNumber.doubleValue();

	}

	/**
	 * Resta los numeros recibidos por parametro
	 * <p>
	 * Creation date 28/04/2006 - 12:31:53 PM
	 * 
	 * @param aNumber
	 *            Numero inicial
	 * @param bNumber
	 *            Numero a restar
	 * @param round
	 *            True, si quiere redondear el resultado
	 * @return Numero operado
	 * @since 1.0
	 */
	public static double subtract(double aNumber, double bNumber, boolean round) {
		numberFormat.setMaximumFractionDigits(PRESICION);
		extendedNumber = new BigDecimal(aNumber);
		BigDecimal bigDecimal = new BigDecimal(bNumber);

		extendedNumber = extendedNumber.subtract(bigDecimal);

		if (round)
			return round(extendedNumber.doubleValue());
		return extendedNumber.doubleValue();

	}

	/**
	 * Multiplica los numeros recibidos por parametro
	 * <p>
	 * Creation date 28/04/2006 - 12:32:36 PM
	 * 
	 * @param aNumber
	 *            Numero inicial
	 * @param bNumber
	 *            Numero a multiplicar
	 * @param round
	 *            True, si quiere redondear el resultado
	 * @return Numero operado
	 * @since 1.0
	 */
	public static double multiply(double aNumber, double bNumber, boolean round) {
		numberFormat.setMaximumFractionDigits(PRESICION);
		extendedNumber = new BigDecimal(aNumber);
		BigDecimal bigDecimal = new BigDecimal(bNumber);

		extendedNumber = extendedNumber.multiply(bigDecimal);

		if (round)
			return round(extendedNumber.doubleValue());
		return extendedNumber.doubleValue();

	}

	/**
	 * Divide los numeros recibidos por parametro
	 * <p>
	 * Creation date 28/04/2006 - 12:33:14 PM
	 * 
	 * @param aNumber
	 *            Numero inicial
	 * @param bNumber
	 *            Dividendo
	 * @param round
	 *            True, si quiere redondear el resultado
	 * @return Numero operado
	 * @since 1.0
	 */
	public static double divide(double aNumber, double bNumber, boolean round) {
		numberFormat.setMaximumFractionDigits(PRESICION);
		extendedNumber = new BigDecimal(aNumber);
		BigDecimal bigDecimal = new BigDecimal(bNumber);

		extendedNumber = extendedNumber.divide(bigDecimal, 10,
				BigDecimal.ROUND_FLOOR);

		if (round)
			return round(extendedNumber.doubleValue());
		return extendedNumber.doubleValue();

	}

	/**
	 * Convierte un numero en formato texto en uno numerico
	 * 
	 * @param number
	 *            Numero en formato texto
	 * @return Numero en formato numerico
	 * @since 1.0
	 */
	public static double parseString(String number) {
		try {
			return numberFormat.parse(number).doubleValue();
		} catch (ParseException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Convierte un numero en formato numerico en uno texto
	 * 
	 * @param number
	 *            Numero en formato numerico
	 * @return Numero en formato texto
	 * @since 1.0
	 */
	public static String parseDouble(double number) {
		return numberFormat.format(number);
	}

	/**
	 * Redondea el numero en formato numerico pasado por parametro
	 * 
	 * @param number
	 *            Numero a redondear
	 * @return Numero redondeado en formato numerico
	 * @since 1.0
	 */
	public static double round(double number) {
		return parseString(numberFormat.format(number));
	}

	/**
	 * Retorna la cantidad de numeros decimales
	 * <p>
	 * Creation date 28/04/2006 - 12:36:57 PM
	 * 
	 * @return Cantidad de numeros decimales
	 * @since 1.0
	 */
	public static int getRoundPresicion() {
		return PRESICION;
	}
}