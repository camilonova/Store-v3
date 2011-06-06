package com.nova.log.input.special;

import java.util.Calendar;

/**
 * Provee los metodos para la conversion de fechas en el manejo de la fecha que
 * utiliza la base de datos
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public abstract class DateUtils {

	/**
	 * Convierte la fecha recibida en formato Calendar
	 * <p>
	 * Creation date 4/06/2006 - 04:09:59 PM
	 * 
	 * @param sqlDate
	 *            Fecha en formato YYYY-MM-DD
	 * @return Calendario con la fecha convertida
	 * @since 1.0
	 */
	public static Calendar SQLtoCalendar(String sqlDate) {
		String data[] = sqlDate.split("-");
		int year = Integer.parseInt(data[0]);
		int month = Integer.parseInt(data[1]) - 1;
		int day = Integer.parseInt(data[2]);

		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day, 0, 0, 0);

		return calendar;
	}

	/**
	 * Convierte la fecha recibida en formato SQL
	 * <p>
	 * Creation date 4/06/2006 - 04:15:10 PM
	 * 
	 * @param calendar
	 *            Calendario con la fecha
	 * @return Fecha en formato YYYY-MM-DD
	 * @since 1.0
	 */
	public static String CalendarToSQL(Calendar calendar) {
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		return year + "-" + month + "-" + day;
	}

}
