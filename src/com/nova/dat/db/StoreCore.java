package com.nova.dat.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Random;

import javax.sql.rowset.WebRowSet;

import com.nova.dat.loader.ErrorLogLoader;
import com.nova.dat.parameter.GlobalConstants;
import com.nova.gui.StoreSession;
import com.nova.log.math.StoreMath;
import com.sun.rowset.WebRowSetImpl;

/**
 * Clase interfaz de la aplicacion y el servidor de datos, provee todos los
 * metodos de recuperacion y actualizacion de datos en el servidor.
 * <p>
 * Los datos de moneda se deben almacenar en la base de datos en forma numerica,
 * libre de nomenclaturas, para ofrecer compatibilidad con Locales.
 * <p>
 * Creation date 30/03/2006 - 10:32:36 AM
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public final class StoreCore {

	private StoreCore() {
		// Nadie instancia la clase
	}

	/**
	 * Retorna una conexion con la base de datos. <b>Se debe tener en cuenta
	 * cerrar la conexion y asignar null a la instancia retornada</b>
	 * <p>
	 * Creation date 9/05/2006 - 10:01:19 PM
	 * 
	 * @return Conexion con la base de datos
	 * @throws SQLException
	 * @since 1.0
	 */
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(GlobalConstants.DB_SERVER,
				GlobalConstants.DB_USER, GlobalConstants.DB_PASS);
	}

	/**
	 * Retorna una instancia de una conexion con la base de datos. Esto para
	 * ofrecer la facilidad de ejecutar comandos a la medida para una solucion
	 * en especial. Es importante que se cierre la conexion apenas se termine de
	 * procesar la informacion y asignar <i>null</i> al objeto retornado por
	 * este metodo
	 * <p>
	 * Creation date 26/05/2006 - 02:46:18 PM
	 * 
	 * @return Instancia de la conexion con la base de datos
	 * @throws SQLException
	 * @since 1.0
	 */
	public static WebRowSet getRowSet() throws SQLException {
		WebRowSetImpl rowSetImpl = new WebRowSetImpl();
		rowSetImpl.setUsername(GlobalConstants.DB_USER);
		rowSetImpl.setPassword(GlobalConstants.DB_PASS);
		rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

		return rowSetImpl;
	}

	/**
	 * Inicia la sesion del usuario en la DB
	 * <p>
	 * Creation date 9/05/2006 - 09:24:51 PM
	 * 
	 * @param user
	 *            Usuario
	 * @param pass
	 *            Contraseña
	 * @return ID unico del usuario
	 * @since 1.0
	 */
	public static String startSession(String user, String pass) {
		String userID = null;
		try {
			WebRowSetImpl rowSetImpl = new WebRowSetImpl();
			rowSetImpl.setUsername(GlobalConstants.DB_USER);
			rowSetImpl.setPassword(GlobalConstants.DB_PASS);
			rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

			rowSetImpl
					.setCommand("SELECT `ID`, `UltimoAcceso` FROM `usuarios` "
							+ "WHERE 1 AND `Usuario` = '" + user + "' AND "
							+ "`Password` = '" + pass + "'");
			rowSetImpl.execute();

			if (rowSetImpl.next()) {
				System.out.println("Servidor Encontrado\t"+GlobalConstants.DB_SERVER);
				userID = rowSetImpl.getString(1);
				rowSetImpl.updateTimestamp(2, new Timestamp(System
						.currentTimeMillis()));
				rowSetImpl.updateRow();
				rowSetImpl.acceptChanges();
			}

			rowSetImpl.close();
			rowSetImpl = null;
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}

		return userID;
	}

	/**
	 * Optimiza todas las tablas de la base de datos. Este metodo se debe
	 * ejecutar al cerrar la aplicacion.
	 * <p>
	 * Creation date 30/03/2006 - 11:47:55 AM
	 * 
	 * @since 1.0
	 */
	public static void optimizeTables() {
		try {
			System.out.println("Optimizando tablas...");

			Connection connection = getConnection();
			Statement operation = connection.createStatement();
			ResultSet set = operation.executeQuery("SHOW TABLES");

			// Leemos las tablas en la base de datos
			String sqlSentence = "OPTIMIZE TABLE ";
			while (set.next()) {
				sqlSentence += "`" + set.getString(1) + "` ";
				if (!set.isLast())
					sqlSentence += ", ";
			}

			// Optimizamos las tablas
			operation.execute(sqlSentence);

			set.close();
			operation.close();
			connection.close();

			set = null;
			operation = null;
			connection = null;

			System.out.println("Tablas optimizadas");
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
	}

	/**
	 * Retorna el valor de la propiedad recibida por parametro, estas
	 * propiedades se almacenan en la base de datos para que su modificacion sea
	 * mas facil de hacer.
	 * <p>
	 * Creation date 30/03/2006 - 10:37:19 AM
	 * 
	 * @param property
	 *            Propiedad a retornar valor
	 * @return Valor de la propiedad
	 * @throws Exception
	 *             Lanzado si la propiedad no existe
	 * @since 1.0
	 */
	public static String getProperty(String property) throws Exception {
		String result = null;
		try {
			WebRowSetImpl rowSetImpl = new WebRowSetImpl();
			rowSetImpl.setUsername(GlobalConstants.DB_USER);
			rowSetImpl.setPassword(GlobalConstants.DB_PASS);
			rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

			rowSetImpl.setCommand("SELECT `Valor` FROM `propiedades` "
					+ "WHERE 1 AND `Propiedad` = '" + property + "'");
			rowSetImpl.execute();

			if (rowSetImpl.next()) {
				result = rowSetImpl.getString(1);
				rowSetImpl.close();
				rowSetImpl = null;
				return result;
			}
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		throw new Exception("Propiedad "+property+" no encontrada");
	}

	/**
	 * Retorna un tip aleatorio para mostrar.
	 * <p>
	 * Creation date 30/03/2006 - 10:38:55 AM
	 * 
	 * @return Consejo aleatorio de Store
	 * @since 1.0
	 */
	public static String getTip() {
		try {
			WebRowSetImpl rowSetImpl = new WebRowSetImpl();
			rowSetImpl.setUsername(GlobalConstants.DB_USER);
			rowSetImpl.setPassword(GlobalConstants.DB_PASS);
			rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

			Random rand = new Random();
			int number = rand.nextInt(getRowsCount("tips")) + 1;
			rowSetImpl.setCommand("SELECT `Consejo` FROM `tips` "
					+ "WHERE 1 AND `ID` = '" + String.valueOf(number) + "'");
			rowSetImpl.execute();

			if (rowSetImpl.next())
				return rowSetImpl.getString(1);
			rowSetImpl.close();

			rand = null;
			rowSetImpl = null;
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Retorna si el usuario tiene acceso a un modulo en particular.
	 * <p>
	 * La codificacion de los permisos en la DB se hace asi, "accion-modulo"
	 * para ejecutar comandos sobre el modulo. "modulo", para el acceso al
	 * modulo sin tener en cuenta los comandos
	 * <p>
	 * Creation date 30/03/2006 - 10:39:43 AM
	 * 
	 * @param type
	 *            Tipo de acceso a verificar permiso
	 * @return True si el usuario tiene acceso valido
	 * @since 1.0
	 */
	public static boolean getAccess(String type) {
		boolean result = false;
		try {
			WebRowSetImpl rowSetImpl = new WebRowSetImpl();
			rowSetImpl.setUsername(GlobalConstants.DB_USER);
			rowSetImpl.setPassword(GlobalConstants.DB_PASS);
			rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

			rowSetImpl.setCommand("SELECT `" + type + "` FROM `usuarios` "
					+ "WHERE 1 AND `ID` = " + StoreSession.getUserID());
			rowSetImpl.execute();

			if (rowSetImpl.next())
				result = rowSetImpl.getString(1).equals("Si");
			rowSetImpl.close();
			rowSetImpl = null;
		} catch (SQLException e) {
			Exception err = new Exception("Permiso '" + type
					+ "' no encontrado!!!");
			ErrorLogLoader.addErrorEntry(err);
			ErrorLogLoader.addErrorEntry(e);
			err.printStackTrace();
		}
		return result;
	}

	/**
	 * Retorna los nombres de las columnas de la tabla recibida por parametro
	 * Creation date 30/03/2006 - 10:40:31 AM
	 * 
	 * @param table
	 *            Tabla a retornar nombres
	 * @return Nombre de las columnas en la tabla
	 * @since 1.0
	 */
	public static ArrayList<String> getColumnNames(String table) {
		ArrayList<String> datos = new ArrayList<String>();
		try {
			WebRowSetImpl rowSetImpl = new WebRowSetImpl();
			rowSetImpl.setUsername(GlobalConstants.DB_USER);
			rowSetImpl.setPassword(GlobalConstants.DB_PASS);
			rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

			rowSetImpl.setCommand("SELECT * FROM `" + table + "` LIMIT 0,1");
			rowSetImpl.execute();
			ResultSetMetaData metaData = rowSetImpl.getMetaData();
			for (int i = 0; i < metaData.getColumnCount(); i++)
				datos.add(metaData.getColumnLabel(i + 1));
			rowSetImpl.close();
			rowSetImpl = null;
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		return datos;
	}

	/**
	 * Retorna el indice de la columna en la tabla. Utilizado para la
	 * funcionalidad de ordenar los datos de la tabla. Creation date 30/03/2006 -
	 * 11:30:30 AM
	 * 
	 * @param table
	 *            Tabla de la base de datos
	 * @param columnName
	 *            Columna a retornar el indice
	 * @return Indice de la columna (Iniciando desde cero, para la primera
	 *         columna)
	 * @since 1.0
	 */
	public static int getColumnIndex(String table, String columnName) {
		int index = -1;
		try {
			WebRowSetImpl rowSetImpl = new WebRowSetImpl();
			rowSetImpl.setUsername(GlobalConstants.DB_USER);
			rowSetImpl.setPassword(GlobalConstants.DB_PASS);
			rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

			rowSetImpl.setCommand("SELECT * FROM `" + table + "` LIMIT 0,1");
			rowSetImpl.execute();
			index = rowSetImpl.findColumn(columnName) - 1;
			rowSetImpl.close();
			rowSetImpl = null;
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		return index;
	}

	/**
	 * Retorna la cantidad de filas (registros) en la tabla
	 * <p>
	 * Creation date 30/03/2006 - 10:41:21 AM
	 * 
	 * @param table
	 *            Tabla de la base de datos
	 * @return Numero de filas (registros) en la tabla
	 * @since 1.0
	 */
	public static int getRowsCount(String table) {
		int rowCount = 0;
		try {
			WebRowSetImpl rowSetImpl = new WebRowSetImpl();
			rowSetImpl.setUsername(GlobalConstants.DB_USER);
			rowSetImpl.setPassword(GlobalConstants.DB_PASS);
			rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

			rowSetImpl.setCommand("SELECT COUNT(*) AS rowcount FROM `" + table
					+ "`");
			rowSetImpl.execute();

			if (rowSetImpl.next())
				rowCount = rowSetImpl.getInt("rowcount");
			rowSetImpl.close();
			rowSetImpl = null;
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		return rowCount;
	}

	/**
	 * Metodo que permite agregar un registro con la informacion del parametro
	 * <i>data</i> en la tabla <i>table</i> dinamicamente utilizando el tamaño
	 * del arreglo y leyendo dinamicamente la informacion de la tabla.
	 * Actualizado a ArrayList. Optimizado para datos NULL y funciones.
	 * <p>
	 * Creation date 30/03/2006 - 10:43:11 AM
	 * 
	 * @param table
	 *            Tabla de la base de datos
	 * @param data
	 *            Array de datos a insertar en la tabla
	 * @throws SQLException Se lanza cuando ocurre un error en el ingreso de los datos
	 * @since 1.0
	 */
	public static void newData(String table, ArrayList<String> data) throws SQLException {
		String sqlSentence = "INSERT INTO `" + table + "` VALUES (";

		// Leemos la informacion
		for (int i = 0; i < data.size(); i++) {
			// Si el dato en null en la base de datos
			if (data.get(i) == null)
				sqlSentence += "NULL ";
			// Si se esta llamando a una funcion
			else if (data.get(i).contains("()"))
				sqlSentence += data.get(i);
			else
				sqlSentence += "'" + data.get(i) + "' ";

			if (i < data.size() - 1)
				sqlSentence += ", ";
		}
		sqlSentence += ");";

		WebRowSetImpl rowSetImpl = new WebRowSetImpl();
		rowSetImpl.setUsername(GlobalConstants.DB_USER);
		rowSetImpl.setPassword(GlobalConstants.DB_PASS);
		rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

		rowSetImpl.setCommand(sqlSentence);
		rowSetImpl.execute();
		rowSetImpl.close();
		rowSetImpl = null;
	}

	/**
	 * Actualiza dinamicamente un registro en la tabla pasada por parametro,
	 * utilizando la misma metodologia del metodo <i>newData()</i>, busca el
	 * primer elemento del array <i>data</i> y lo ubica como el indice de la
	 * fila a actualizar, esto debido a que por definicion la columna de indice
	 * es la primera en la tabla y es numerica. Actualizado a ArrayList.
	 * Optimizado para datos NULL y funciones.
	 * <p>
	 * Creation date 30/03/2006 - 10:50:04 AM
	 * 
	 * @param table
	 *            Tabla de la base de datos
	 * @param data
	 *            ArrayList de datos a insertar en la tabla
	 * @throws SQLException
	 *             Se lanza cuando existe un error en la actualizacion
	 * @since 1.0
	 */
	public static void updateData(String table, ArrayList<String> data) throws SQLException {
		String sqlSentence = "UPDATE `" + table + "` SET ";
		ArrayList<String> columnNames = getColumnNames(table);
		for (int i = 1; i < columnNames.size(); i++) {
			sqlSentence += "`" + columnNames.get(i) + "` = ";
			// Si el dato en null en la base de datos
			if (data.get(i) == null)
				sqlSentence += "NULL ";
			// Si se esta llamando a una funcion
			else if (data.get(i).contains("()"))
				sqlSentence += data.get(i);
			else
				sqlSentence += "'" + data.get(i) + "' ";

			if (i < columnNames.size() - 1)
				sqlSentence += ", ";
		}
		sqlSentence += "WHERE `" + columnNames.get(0) + "` = '"
				+ data.get(0) + "' LIMIT 1;";

		WebRowSetImpl rowSetImpl = new WebRowSetImpl();
		rowSetImpl.setUsername(GlobalConstants.DB_USER);
		rowSetImpl.setPassword(GlobalConstants.DB_PASS);
		rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

		rowSetImpl.setCommand(sqlSentence);
		rowSetImpl.execute();
		rowSetImpl.close();
		rowSetImpl = null;
	}

	/**
	 * Retorna toda la informacion de la columna ubicada en la tabla, es decir,
	 * retorna el valor de todas las filas en la columna indicada. Creation date
	 * 30/03/2006 - 10:58:05 AM
	 * 
	 * @param table
	 *            Tabla en la base de datos
	 * @param columnName
	 *            Columna a obtener la informacion
	 * @return Toda la informacion de la columna en la tabla
	 * @since 1.0
	 */
	public static ArrayList<String> getAllColumnData(String table,
			String columnName) {
		ArrayList<String> data = new ArrayList<String>();
		try {
			WebRowSetImpl rowSetImpl = new WebRowSetImpl();
			rowSetImpl.setUsername(GlobalConstants.DB_USER);
			rowSetImpl.setPassword(GlobalConstants.DB_PASS);
			rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

			rowSetImpl.setCommand("SELECT `" + columnName + "` FROM `" + table
					+ "` ORDER BY `" + columnName + "` ASC");
			rowSetImpl.execute();

			while (rowSetImpl.next()) {
				data.add(rowSetImpl.getString(columnName));
			}
			rowSetImpl.close();
			rowSetImpl = null;
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * Retorna toda la informacion de la fila (registro) ubicado por los
	 * parametros, puede retornar elementos null si los valores son tambien
	 * null.
	 * <p>
	 * Creation date 30/03/2006 - 11:05:39 AM
	 * 
	 * @param table
	 *            Tabla en la base de datos
	 * @param keyColumn
	 *            Columna a ubicar la fila
	 * @param keyValue
	 *            Dato en la columna perteneciente a la fila (Este dato sera
	 *            retornado)
	 * @return Toda la informacion de la fila ubicada en la tabla
	 * @since 1.0
	 */
	public static ArrayList<String> getAllRowData(String table,
			String keyColumn, String keyValue) {
		ArrayList<String> datos = new ArrayList<String>();
		try {
			WebRowSetImpl rowSetImpl = new WebRowSetImpl();
			rowSetImpl.setUsername(GlobalConstants.DB_USER);
			rowSetImpl.setPassword(GlobalConstants.DB_PASS);
			rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

			rowSetImpl.setCommand("SELECT * FROM `" + table + "` WHERE 1 AND `"
					+ keyColumn + "` = '" + keyValue + "'");
			rowSetImpl.execute();

			if (rowSetImpl.next()) {
				for (int i = 0; i < rowSetImpl.getMetaData().getColumnCount(); i++)
					datos.add(rowSetImpl.getString(i + 1));
			}
			rowSetImpl.close();
			rowSetImpl = null;
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		return datos;
	}

	/**
	 * Retorna el dato ubicado en la tabla <i>table</i>, ubicando la fila por
	 * la columna <i>keyColumn</i> y el valor <i>keyValue</i>, a la fila
	 * encontrada se retorna el dato ubicado en la columna <i>columnData</i>.
	 * <p>
	 * Creation date 30/03/2006 - 10:59:34 AM
	 * 
	 * @param table
	 *            Tabla de la base de datos
	 * @param keyColumn
	 *            Columna a ubicar la fila
	 * @param keyValue
	 *            Valor de la columna a ubicar la fila
	 * @param columnData
	 *            Columna de quien se quiere obtener el valor
	 * @return Valor ubicado por los parametros en la columna <i>columnData</i>.
	 *         Si no encuentra nada retorna null.
	 * @since 1.0
	 */
	public static String getDataAt(String table, String keyColumn,
			String keyValue, String columnData) {
		String dato = null;
		try {
			WebRowSetImpl rowSetImpl = new WebRowSetImpl();
			rowSetImpl.setUsername(GlobalConstants.DB_USER);
			rowSetImpl.setPassword(GlobalConstants.DB_PASS);
			rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

			rowSetImpl.setCommand("SELECT `" + columnData + "` FROM `" + table
					+ "` WHERE 1 AND `" + keyColumn + "` = '" + keyValue + "'");
			rowSetImpl.execute();

			if (rowSetImpl.next())
				dato = rowSetImpl.getString(1);
			rowSetImpl.close();
			rowSetImpl = null;
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		return dato;
	}

	/**
	 * Evalua si el dato existe en la ubicacion dada por los parametros
	 * <p>
	 * Creation date 30/03/2006 - 11:07:40 AM
	 * 
	 * @param table
	 *            Tabla en la base de datos
	 * @param keyColumn
	 *            Columna llave
	 * @param keyValue
	 *            Valor llave
	 * @return True, si existe una fila con el valor llave en la columna llave
	 * @since 1.0
	 */
	public static boolean isRegisteredData(String table, String keyColumn,
			String keyValue) {
		boolean isRegistered = false;
		try {
			WebRowSetImpl rowSetImpl = new WebRowSetImpl();
			rowSetImpl.setUsername(GlobalConstants.DB_USER);
			rowSetImpl.setPassword(GlobalConstants.DB_PASS);
			rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

			rowSetImpl.setCommand("SELECT `" + keyColumn + "` FROM `" + table
					+ "` WHERE 1 AND `" + keyColumn + "` = '" + keyValue + "'");
			rowSetImpl.execute();

			if (rowSetImpl.next())
				isRegistered = true;
			rowSetImpl.close();
			rowSetImpl = null;
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		return isRegistered;
	}

	/**
	 * Elimina registros en la base de datos, ubicandolos buscando el valor en
	 * la columna pasada por parametro. La columna debe ser el indice de la
	 * tabla para eliminar un solo registro. De no ser asi, se eliminaran todas
	 * las filas donde <i>keyValue</i> se encuentre en <i>KeyColumn</i>. Para
	 * asegurar que solamente se eliminara una fila <i>keyColumn</i> debe ser
	 * la columna indice de la tabla
	 * <p>
	 * Creation date 30/03/2006 - 10:54:41 AM
	 * 
	 * @param table
	 *            Tabla en la base de datos
	 * @param keyColumn
	 *            Nombre de la columna donde esta el siguiente parametro (Indice
	 *            para eliminar un solo registro)
	 * @param keyValue
	 *            Valor en la columna de la fila a eliminar
	 * @since 1.0
	 */
	public static void removeRow(String table, String keyColumn, String keyValue) {
		try {
			WebRowSetImpl rowSetImpl = new WebRowSetImpl();
			rowSetImpl.setUsername(GlobalConstants.DB_USER);
			rowSetImpl.setPassword(GlobalConstants.DB_PASS);
			rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

			rowSetImpl.setCommand("DELETE FROM `" + table + "` WHERE `"
					+ keyColumn + "` = '" + keyValue + "' ;");
			rowSetImpl.execute();
			rowSetImpl.close();
			rowSetImpl = null;
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
	}

	/**
	 * Actualiza el valor ubicado por los parametros
	 * <p>
	 * Creation date 30/03/2006 - 11:21:43 AM
	 * 
	 * @param table
	 *            Tabla en la base de datos
	 * @param keyColumn
	 *            Columna llave
	 * @param keyValue
	 *            Dato llave
	 * @param dataColumn
	 *            Columna a actualizar
	 * @param dataValue
	 *            Dato nuevo
	 * @since 1.0
	 */
	public static void setDataAt(String table, String keyColumn,
			String keyValue, String dataColumn, String dataValue) {
		try {
			WebRowSetImpl rowSetImpl = new WebRowSetImpl();
			rowSetImpl.setUsername(GlobalConstants.DB_USER);
			rowSetImpl.setPassword(GlobalConstants.DB_PASS);
			rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

			rowSetImpl.setCommand("UPDATE `" + table + "` SET " + "`"
					+ dataColumn + "` = '" + dataValue + "' WHERE 1 AND `"
					+ keyColumn + "` = '" + keyValue + "' ;");
			rowSetImpl.execute();
			rowSetImpl.close();
			rowSetImpl = null;
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
	}

	/**
	 * Retorna todos los datos de la tabla indicada por el parametro, ordenada
	 * por los parametros <i>orderBy</i> y <i>type</i>. Esta consulta retorna
	 * el ResultSet sensible al scroll, implicando que debe utilizarse
	 * unicamente para el modelo de la tabla.
	 * <p>
	 * Creation date 30/03/2006 - 11:37:25 AM
	 * 
	 * @param table
	 *            Tabla en la base de datos
	 * @param orderBy
	 *            Columna a ordenar la tabla
	 * @param type
	 *            GlobalConstants.ORDER_ASCENDANT o
	 *            GlobalConstants.ORDER_DESCENTANT
	 * @return Todos los datos de la tabla ordenados
	 * @since 1.0
	 */
	public static WebRowSet getTableData(String table, String orderBy,
			String type) {
		try {
			WebRowSetImpl webRowSetImpl = new WebRowSetImpl();
			webRowSetImpl.setUsername(GlobalConstants.DB_USER);
			webRowSetImpl.setPassword(GlobalConstants.DB_PASS);
			webRowSetImpl.setUrl(GlobalConstants.DB_SERVER);
			webRowSetImpl.setType(ResultSet.TYPE_SCROLL_SENSITIVE);
			webRowSetImpl.setConcurrency(ResultSet.CONCUR_UPDATABLE);

			webRowSetImpl.setCommand("SELECT * FROM `" + table + "` ORDER BY `"
					+ orderBy + "` " + type);
			webRowSetImpl.execute();

			return webRowSetImpl;
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Retorna el ID de la tabla para el proximo registro a ingresar
	 * <p>
	 * Creation date 30/03/2006 - 11:42:14 AM
	 * 
	 * @param table
	 *            Tabla en la base de datos
	 * @param column
	 *            Columna Indice de la tabla
	 * @return Entero indice del proximo registro a ingresar
	 * @since 1.0
	 */
	public static String getNextId(String table, String column) {
		long id = 1;
		try {
			WebRowSetImpl rowSetImpl = new WebRowSetImpl();
			rowSetImpl.setUsername(GlobalConstants.DB_USER);
			rowSetImpl.setPassword(GlobalConstants.DB_PASS);
			rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

			rowSetImpl.setCommand("SELECT MAX(`" + column
					+ "`) AS lastID FROM `" + table + "`");
			rowSetImpl.execute();
			if (rowSetImpl.next())
				id += Long.parseLong(rowSetImpl.getString("lastID"));
			rowSetImpl.close();
			rowSetImpl = null;
		} catch (NumberFormatException e) {
			return "1";
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		return String.valueOf(id);
	}

	/**
	 * Retorna la descripcion y la marca de todos los elementos del inventario,
	 * separados por el identificador general de la aplicacion. <b>Este metodo
	 * unicamente debe ser utilizado por InputComponent</b>
	 * <p>
	 * Mejorado para presentar los servicios sin separador
	 * <p>
	 * Creation date 30/03/2006 - 11:44:20 AM
	 * 
	 * @return Todos los registros del inventario (descripcion + marca)
	 * @since 1.0
	 */
	public static ArrayList<String> getStockAllItems() {
		ArrayList<String> datos = new ArrayList<String>();
		try {
			WebRowSetImpl rowSetImpl = new WebRowSetImpl();
			rowSetImpl.setUsername(GlobalConstants.DB_USER);
			rowSetImpl.setPassword(GlobalConstants.DB_PASS);
			rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

			rowSetImpl.setCommand("SELECT `Descripcion` , `Marca` "
					+ "FROM `inventario` ORDER BY `Descripcion` ASC ");
			rowSetImpl.execute();
			while (rowSetImpl.next()) {
				String description = rowSetImpl.getString(1);
				String brand = rowSetImpl.getString(2);

				if (brand == null)
					datos.add(description);
				else
					datos.add(description
							+ GlobalConstants.DESCRIPTION_BRAND_SEPARATOR
							+ brand);
			}
			rowSetImpl.close();
			rowSetImpl = null;
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		return datos;
	}

	/**
	 * Retorna los productos del inventario que tienen cantidad mayor a cero y
	 * los servicios, separados por el identificador general de la aplicacion.
	 * <b>Este metodo unicamente debe ser utilizado por InputComponent</b>
	 * <p>
	 * Creation date 31/05/2006 - 09:33:15 PM
	 * 
	 * @return Productos del inventario con cantidad mayor a cero y servicios
	 * @since 1.0
	 */
	public static ArrayList<String> getStockSaleItems() {
		ArrayList<String> datos = new ArrayList<String>();
		try {
			WebRowSetImpl rowSetImpl = new WebRowSetImpl();
			rowSetImpl.setUsername(GlobalConstants.DB_USER);
			rowSetImpl.setPassword(GlobalConstants.DB_PASS);
			rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

			rowSetImpl
					.setCommand("SELECT `Descripcion` , `Marca` "
							+ "FROM `inventario` WHERE `Cantidad` >0 OR `Cantidad` IS NULL "
							+ "ORDER BY `Descripcion` ASC ");
			rowSetImpl.execute();
			while (rowSetImpl.next()) {
				String description = rowSetImpl.getString(1);
				String brand = rowSetImpl.getString(2);

				if (brand == null)
					datos.add(description);
				else
					datos.add(description
							+ GlobalConstants.DESCRIPTION_BRAND_SEPARATOR
							+ brand);
			}
			rowSetImpl.close();
			rowSetImpl = null;
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		return datos;
	}

	/**
	 * Retorna solamente los productos del inventario. <b>Este metodo unicamente
	 * debe ser utilizado por InputComponent</b>
	 * <p>
	 * Creation date 31/05/2006 - 09:36:07 PM
	 * 
	 * @return Productos del inventario unicamente
	 * @since 1.0
	 */
	public static ArrayList<String> getStockOnlyProducts() {
		ArrayList<String> datos = new ArrayList<String>();
		try {
			WebRowSetImpl rowSetImpl = new WebRowSetImpl();
			rowSetImpl.setUsername(GlobalConstants.DB_USER);
			rowSetImpl.setPassword(GlobalConstants.DB_PASS);
			rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

			rowSetImpl.setCommand("SELECT `Descripcion` , `Marca` "
					+ "FROM `inventario` WHERE `Marca` IS NOT NULL "
					+ "ORDER BY `Descripcion` ASC ");
			rowSetImpl.execute();
			while (rowSetImpl.next()) {
				String description = rowSetImpl.getString(1);
				String brand = rowSetImpl.getString(2);

				datos.add(description
						+ GlobalConstants.DESCRIPTION_BRAND_SEPARATOR + brand);
			}
			rowSetImpl.close();
			rowSetImpl = null;
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		return datos;
	}

	/**
	 * Retorna solamente los productos del inventario que tenga registro del
	 * SICE y CUBS
	 * <p>
	 * Creation date 31/05/2006 - 09:37:18 PM
	 * 
	 * @return Productos del inventario registrados en el SICE
	 * @since 1.0
	 */
	public static ArrayList<String> getStockSICEItems() {
		// TODO Funcionalidad SICE
		return null;
	}

	/**
	 * Retorna la descripcion y la marca del elemento identificado por el
	 * parametro, separados por el identificador general de la aplicacion
	 * <p>
	 * Mejorado para presentar los servicios sin separador
	 * <p>
	 * Creation date 30/03/2006 - 11:45:38 AM
	 * 
	 * @param barCode
	 *            Codigo de barras del articulo en el inventario
	 * @return (descripcion + marca) del articulo, null si no existe
	 * @since 1.0
	 */
	public static String getStockDescriptionAndBrand(String barCode) {
		String descriptionAndBrand = null;
		try {
			WebRowSetImpl rowSetImpl = new WebRowSetImpl();
			rowSetImpl.setUsername(GlobalConstants.DB_USER);
			rowSetImpl.setPassword(GlobalConstants.DB_PASS);
			rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

			rowSetImpl
					.setCommand("SELECT `Descripcion` , `Marca` FROM `inventario`"
							+ "WHERE `Codigo` = '" + barCode + "'");
			rowSetImpl.execute();
			if (rowSetImpl.next()) {
				String description = rowSetImpl.getString(1);
				String brand = rowSetImpl.getString(2);

				if (brand == null)
					descriptionAndBrand = description;
				else
					descriptionAndBrand = description
							+ GlobalConstants.DESCRIPTION_BRAND_SEPARATOR
							+ brand;
			}
			rowSetImpl.close();
			rowSetImpl = null;
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		return descriptionAndBrand;
	}

	/**
	 * Retorna el codigo de barras del articulo identificado con el parametro
	 * <p>
	 * Creation date 30/03/2006 - 11:49:37 AM
	 * 
	 * @param identifier
	 *            (descripcion + marca) del tipo "getStockDescriptionAndBrand()"
	 * @return Codigo de barras del articulo, null si no existe
	 * @since 1.0
	 */
	public static String getStockBarCode(String identifier) {
		String stockBarCode = null;
		if (identifier.contains(GlobalConstants.DESCRIPTION_BRAND_SEPARATOR)) {
			// Producto
			try {
				String split[] = identifier
						.split(GlobalConstants.DESCRIPTION_BRAND_SEPARATOR);

				WebRowSetImpl rowSetImpl = new WebRowSetImpl();
				rowSetImpl.setUsername(GlobalConstants.DB_USER);
				rowSetImpl.setPassword(GlobalConstants.DB_PASS);
				rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

				rowSetImpl.setCommand("SELECT `Codigo` FROM `inventario`"
						+ " WHERE `Descripcion` = '" + split[0]
						+ "' AND `Marca` = '" + split[1] + "' ");
				rowSetImpl.execute();
				if (rowSetImpl.next())
					stockBarCode = rowSetImpl.getString(1);
				rowSetImpl.close();
				rowSetImpl = null;
			} catch (SQLException e) {
				ErrorLogLoader.addErrorEntry(e);
				e.printStackTrace();
			}
		} else {
			// Servicio
			try {
				WebRowSetImpl rowSetImpl = new WebRowSetImpl();
				rowSetImpl.setUsername(GlobalConstants.DB_USER);
				rowSetImpl.setPassword(GlobalConstants.DB_PASS);
				rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

				rowSetImpl.setCommand("SELECT `Codigo` FROM `inventario`"
						+ " WHERE `Descripcion` = '" + identifier
						+ "' AND `Marca` IS NULL ");
				rowSetImpl.execute();
				if (rowSetImpl.next())
					stockBarCode = rowSetImpl.getString("Codigo");
				rowSetImpl.close();
				rowSetImpl = null;
			} catch (SQLException e) {
				ErrorLogLoader.addErrorEntry(e);
				e.printStackTrace();
			}
		}

		return stockBarCode;
	}

	/**
	 * Evalua si existe un articulo que tenga los parametros recibidos
	 * <p>
	 * Creation date 10/05/2006 - 02:40:48 PM
	 * 
	 * @param description
	 *            Descripcion del articulo
	 * @param brand
	 *            Marca del articulo
	 * @return True si existe un articulo con los mismos parametros
	 * @since 1.0
	 */
	public static boolean isStockRegistered(String description, String brand) {
		boolean isRegistered = false;
		try {
			WebRowSetImpl rowSetImpl = new WebRowSetImpl();
			rowSetImpl.setUsername(GlobalConstants.DB_USER);
			rowSetImpl.setPassword(GlobalConstants.DB_PASS);
			rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

			rowSetImpl
					.setCommand("SELECT `Codigo` FROM `inventario` WHERE `Descripcion` = '"
							+ description + "' AND `Marca` = '" + brand + "'");
			rowSetImpl.execute();

			if (rowSetImpl.next())
				isRegistered = true;
			rowSetImpl.close();
			rowSetImpl = null;
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		return isRegistered;
	}

	/**
	 * Registra los datos de los elementos del formato
	 * <p>
	 * Creation date 10/05/2006 - 03:46:31 PM
	 * 
	 * @param table
	 *            Tabla del modulo
	 * @param consecutiveNumber
	 *            Numero del consecutivo del formato
	 * @param tableData
	 *            Datos del formato
	 * @since 1.0
	 */
	public static void registerFormMetaData(String table,
			String consecutiveNumber, ArrayList<String[]> tableData) {
		try {
			int process = Integer.valueOf(StoreCore.getNextId(table + "_items",
					"Proceso"));
			Connection connection = getConnection();
			PreparedStatement statement = connection
					.prepareStatement("INSERT INTO `" + table
							+ "_items` VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			connection.setAutoCommit(false);

			for (String[] row : tableData) {
				// Calculamos Totales
				double excento = 0;
				double gravado = StoreMath.parseString(row[3]);
				double tax = StoreMath.parseString(row[4]);
				double total = StoreMath.parseString(row[5]);
				if (tax == 0) {
					// Exento
					excento = gravado;
					gravado = 0;
				}

				// Proceso
				statement.setInt(1, process++);
				// Cotizacion
				statement.setInt(2, Integer.valueOf(consecutiveNumber));
				// Cantidad
				statement.setInt(3, Integer.valueOf(row[0]));
				// Descripcion
				statement.setString(4, row[1]);
				// Precio publico
				statement.setDouble(5, Double.valueOf(row[2]));
				// Excento
				statement.setDouble(6, excento);
				// Gravado
				statement.setDouble(7, gravado);
				// Impuesto
				statement.setDouble(8, tax);
				// Total
				statement.setDouble(9, total);
				// Adicionamos la sentencia
				statement.addBatch();
			}

			statement.executeBatch();
			connection.commit();
			connection.setAutoCommit(true);

			connection.close();
			statement = null;
			connection = null;
		} catch (NumberFormatException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
	}

	/**
	 * Carga los datos de los elementos del formato
	 * <p>
	 * Creation date 10/05/2006 - 04:08:18 PM
	 * 
	 * @param table
	 *            Tabla del modulo
	 * @param keyColumn
	 *            Columna que contiene el consecutivo
	 * @param consecutive
	 *            Numero del consecutivo del formato
	 * @return Datos para ingresar en la tabla del formato
	 * @since 1.0
	 */
	public static ArrayList<String[]> getFormMetaData(String table,
			String keyColumn, String consecutive) {
		ArrayList<String[]> metaData = new ArrayList<String[]>();
		try {
			WebRowSetImpl rowSetImpl = new WebRowSetImpl();
			rowSetImpl.setUsername(GlobalConstants.DB_USER);
			rowSetImpl.setPassword(GlobalConstants.DB_PASS);
			rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

			rowSetImpl.setCommand("SELECT * FROM `" + table + "_items` WHERE `"
					+ keyColumn + "` = " + consecutive);
			rowSetImpl.execute();

			while (rowSetImpl.next()) {
				ArrayList<String> arrayList = new ArrayList<String>();
				int columnCount = rowSetImpl.getMetaData().getColumnCount();
				for (int j = 0; j < columnCount; j++) {
					arrayList.add(rowSetImpl.getString(j + 1));
				}

				String[] rowData = new String[columnCount - 3];
				rowData[0] = arrayList.get(2);
				rowData[1] = arrayList.get(3);
				rowData[2] = arrayList.get(4);
				double ex = Double.parseDouble(arrayList.get(5));
				double gr = Double.parseDouble(arrayList.get(6));
				rowData[3] = StoreMath.parseDouble(StoreMath.add(ex, gr, true));
				rowData[4] = StoreMath.parseDouble(Double.parseDouble(arrayList
						.get(7)));
				rowData[5] = StoreMath.parseDouble(Double.parseDouble(arrayList
						.get(8)));
				metaData.add(rowData);
			}
			rowSetImpl.close();
			rowSetImpl = null;
		} catch (NumberFormatException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}

		return metaData;
	}
	
	/**
	 * Retorna el total del credito de un cliente en el modulo de facturacion
	 * manual.
	 * <p>Creation date 14/06/2006 - 03:36:59 PM
	 *
	 * @param clientName		Nombre del cliente registrado en el sistema
	 * @return		Total deuda del cliente en el modulo de facturacion manual
	 * @since 1.0
	 */
	public static double getManualInvoiceCreditValue(String clientName) {
		double credit = 0;
		try {
			WebRowSetImpl rowSetImpl = new WebRowSetImpl();
			rowSetImpl.setUsername(GlobalConstants.DB_USER);
			rowSetImpl.setPassword(GlobalConstants.DB_PASS);
			rowSetImpl.setUrl(GlobalConstants.DB_SERVER);

			rowSetImpl.setCommand("SELECT `Total` "
					+ "FROM `facturacion_manual` WHERE `Cliente` = '"
					+ clientName + "'AND `FechaCancelacion` IS NULL");
			rowSetImpl.execute();

			while (rowSetImpl.next())
				credit = StoreMath.subtract(credit, rowSetImpl
						.getDouble("Total"), true);

			rowSetImpl.close();
			rowSetImpl = null;
		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		return credit;
	}

}