package com.nova.log.form.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JDialog;
import javax.swing.JTable;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.dat.parameter.GlobalConstants;
import com.nova.log.form.dat.FormTableModel;
import com.nova.log.form.dat.ModuleForm;
import com.nova.log.form.log.FormCommand;
import com.nova.log.input.special.DateUtils;
import com.nova.log.math.StoreMath;
import com.nova.modules.invoice.gui.InvoiceMainPanel;
import com.nova.modules.purchase.gui.PurchaseMainPanel;
import com.nova.modules.quotation.gui.QuoteMainPanel;

/**
 * Esta clase abstrae toda la interfaz grafica relacionada con el formato.
 * <p>
 * Permitiendo ademas los metodos necesarios para obtener y modificar los datos
 * provenientes del mismo.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public abstract class Form extends JDialog {

	FormCommand command;

	FormToolBar toolBar;

	FormUserDataPanel userPanel;

	FormInsertPanel insertPanel;

	FormTableDataPanel tablePanel;

	FormTotalsPanel totalPanel;

	private final ModuleForm moduleForm;

	private final String identifier;

	private final Color foreground;

	/**
	 * Construye el formato dandole todas las caracteristicas y personalizandolo
	 * de acuerdo a la clase que registra los datos
	 * <p>
	 * Creation date 27/05/2006 - 09:59:17 AM
	 * 
	 * @param mainPanel
	 *            Panel principal del modulo
	 * 
	 * @since 1.0
	 */
	public Form(ModuleForm mainPanel) {
		this.moduleForm = mainPanel;
		if (mainPanel instanceof QuoteMainPanel) {
			identifier = "Cotizacion";
			foreground = Color.GREEN;
		} else if (mainPanel instanceof InvoiceMainPanel) {
			identifier = "Factura";
			foreground = Color.RED;
		} else if (mainPanel instanceof PurchaseMainPanel) {
			identifier = "Compra";
			foreground = Color.BLUE;
		} else {
			identifier = null;
			foreground = null;
		}

		userPanel = new FormUserDataPanel(this);
		tablePanel = new FormTableDataPanel(this);
		insertPanel = new FormInsertPanel(this);
		command = new FormCommand(this);
		totalPanel = new FormTotalsPanel(this);
		toolBar = new FormToolBar(command);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowOpened(WindowEvent e) {
				userPanel.setFocus();
				super.windowOpened(e);
			}

			@Override
			public void windowClosing(WindowEvent e) {
				toolBar.exit.doClick();
				super.windowClosing(e);
			}

		});

		add(toolBar);
		add(userPanel);
		add(insertPanel);
		add(tablePanel);
		add(totalPanel);

		setSize(800, 575);
		setTitle("Creacion de nueva " + this.identifier);
		setLocation((GlobalConstants.SCREEN_SIZE_WIDTH - getWidth()) / 2,
				(GlobalConstants.SCREEN_SIZE_HEIGHT - getHeight()) / 2);
		setLayout(new FlowLayout());
		setResizable(false);
		setAlwaysOnTop(true);
	}

	/**
	 * Actualiza el panel de total
	 * <p>
	 * Creation date 23/04/2006 - 06:29:35 PM
	 * 
	 * @param data
	 *            Datos de la tabla
	 * @since 1.0
	 */
	public void updateTotalPanel(ArrayList<String[]> data) {
		double excluido = 0;
		double gravado = 0;
		double impuesto = 0;

		for (int i = 0; i < data.size(); i++) {
			String[] row = data.get(i);
			if (row[3] != null && row[4] != null) {
				double parseDouble = StoreMath.parseString(row[4]);
				if (parseDouble > 0) {
					gravado += StoreMath.parseString(row[3]);
					impuesto += parseDouble;
				} else
					excluido += StoreMath.parseString(row[3]);
			}
		}
		totalPanel.setTotals(excluido, gravado, impuesto);
	}

	/**
	 * Retorna el modelo de la tabla
	 * <p>
	 * Creation date 24/04/2006 - 08:40:31 AM
	 * 
	 * @return Modelo de la tabla
	 * @since 1.0
	 */
	public FormTableModel getTableModel() {
		return tablePanel.model;
	}

	/**
	 * Retorna la tabla
	 * <p>
	 * Creation date 24/04/2006 - 08:47:57 AM
	 * 
	 * @return Tabla de datos
	 * @since 1.0
	 */
	public JTable getTable() {
		return tablePanel.table;
	}

	/**
	 * Selecciona la celda ubicada por los parametros
	 * <p>
	 * Creation date 24/04/2006 - 08:53:48 AM
	 * 
	 * @param row
	 *            Fila a seleccionar
	 * @param col
	 *            Columna a seleccionar
	 * @since 1.0
	 */
	public void setSelectedCell(int row, int col) {
		tablePanel.setSelectedCell(row, col);
	}

	/**
	 * Retorna el numero del formato
	 * <p>
	 * Creation date 2/05/2006 - 09:30:56 AM
	 * 
	 * @return Numero del formato
	 * @since 1.0
	 */
	public String getConsecutiveNumber() {
		return userPanel.getFormNumber();
	}

	/**
	 * Determina el numero del formato
	 * <p>
	 * Creation date 4/05/2006 - 07:57:00 AM
	 * 
	 * @param consecutive
	 *            Numero del formato
	 * @since 1.0
	 */
	public void setConsecutiveNumber(String consecutive) {
		userPanel.setFormNumber(consecutive);
	}

	/**
	 * Retorna el usuario del formato
	 * <p>
	 * Creation date 2/05/2006 - 09:34:06 AM
	 * 
	 * @return Usuario del formato
	 * @since 1.0
	 */
	public String getFormUser() {
		return userPanel.getSelectedUser();
	}

	/**
	 * Determina el usuario del formato
	 * <p>
	 * Creation date 4/05/2006 - 08:00:05 AM
	 * 
	 * @param user
	 *            Usuario del formato
	 * @since 1.0
	 */
	public void setFormUser(String user) {
		userPanel.setSelectedUser(user);
	}

	/**
	 * Determina la fecha del formato
	 * <p>
	 * Creation date 4/05/2006 - 08:02:57 AM
	 * 
	 * @param calendar
	 *            Fecha del formato
	 * @since 1.0
	 */
	public void setFormDate(Calendar calendar) {
		userPanel.dateFld.setCalendar(calendar);
	}

	/**
	 * Retorna la fecha SQL del formato
	 * <p>
	 * Creation date 5/05/2006 - 09:30:40 AM
	 * 
	 * @return Fecha SLQ del formato
	 * @since 1.0
	 */
	public String getFormSQLDate() {
		return DateUtils.CalendarToSQL(userPanel.dateFld.getCalendar());
	}

	/**
	 * Retorna el valor excento del formato
	 * <p>
	 * Creation date 2/05/2006 - 10:01:00 AM
	 * 
	 * @return Valor excento
	 * @since 1.0
	 */
	public String getExempt() {
		String exempt;
		try {
			exempt = String.valueOf(totalPanel.getExempt());
		} catch (ParseException e) {
			exempt = null;
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		return exempt;
	}

	/**
	 * Determina el valor excluido del formato
	 * <p>
	 * Creation date 4/05/2006 - 08:12:02 AM
	 * 
	 * @param exempt
	 *            Valor excluido
	 * @since 1.0
	 */
	public void setExempt(String exempt) {
		totalPanel.setExempt(Double.valueOf(exempt));
	}

	/**
	 * Retorna el valor gravado del formato
	 * <p>
	 * Creation date 2/05/2006 - 10:02:24 AM
	 * 
	 * @return Valor gravado
	 * @since 1.0
	 */
	public String getBase() {
		String base;
		try {
			base = String.valueOf(totalPanel.getBase());
		} catch (ParseException e) {
			base = null;
			e.printStackTrace();
		}
		return base;
	}

	/**
	 * Determina el valor gravado del formato
	 * <p>
	 * Creation date 4/05/2006 - 08:13:36 AM
	 * 
	 * @param base
	 *            Valor gravado del formato
	 * @since 1.0
	 */
	public void setBase(String base) {
		totalPanel.setBase(Double.valueOf(base));
	}

	/**
	 * Retorna el valor del impuesto del formato
	 * <p>
	 * Creation date 2/05/2006 - 10:03:19 AM
	 * 
	 * @return Impuesto del formato
	 * @since 1.0
	 */
	public String getTax() {
		String tax;
		try {
			tax = String.valueOf(totalPanel.getTax());
		} catch (ParseException e) {
			tax = null;
			e.printStackTrace();
		}
		return tax;
	}

	/**
	 * Determina el valor del impuesto del formato
	 * <p>
	 * Creation date 4/05/2006 - 08:14:37 AM
	 * 
	 * @param tax
	 *            Valor del impuesto
	 * @since 1.0
	 */
	public void setTax(String tax) {
		totalPanel.setTax(Double.parseDouble(tax));
	}

	/**
	 * Retorna el total del formato
	 * <p>
	 * Creation date 2/05/2006 - 10:04:01 AM
	 * 
	 * @return Total del formato
	 * @since 1.0
	 */
	public String getTotal() {
		String total;
		try {
			total = String.valueOf(totalPanel.getTotal());
		} catch (ParseException e) {
			total = null;
			e.printStackTrace();
		}
		return total;
	}

	/**
	 * Determina el total del formato
	 * <p>
	 * Creation date 4/05/2006 - 08:15:56 AM
	 * 
	 * @param total
	 *            Total del formato
	 * @since 1.0
	 */
	public void setTotal(String total) {
		totalPanel.setTotal(Double.parseDouble(total));
	}

	/**
	 * Recarga los datos del formato
	 * <p>
	 * Creation date 3/05/2006 - 10:08:00 AM
	 * 
	 * @since 1.0
	 */
	public void reloadForm() {
		userPanel.reload();
		userPanel.setFocus();
		insertPanel.descriptionFld.updateData(getFormItems());
		tablePanel.model.removeAllRows();
	}

	/**
	 * Carga los datos en el formato. Desarrollado para el comando ver
	 * <p>
	 * Creation date 4/05/2006 - 09:59:09 AM
	 * 
	 * @param table
	 *            Tabla del panel principal
	 * @param consecutive
	 *            Numero del formato consecutivo a cargar
	 * @since 1.0
	 */
	public void loadForm(String table, String consecutive) {
		String keyColumn = StoreCore.getColumnNames(table).get(0);
		ArrayList<String> quoteData = StoreCore.getAllRowData(table, keyColumn,
				consecutive);

		setConsecutiveNumber(quoteData.get(0));
		setFormUser(quoteData.get(1));
		setFormDate(DateUtils.SQLtoCalendar(quoteData.get(2)));

		ArrayList<String[]> formMetaData = StoreCore.getFormMetaData(table,
				keyColumn, consecutive);
		tablePanel.model.setTableData(formMetaData);

		setTitle("Ver " + identifier + " No " + consecutive);
		toolBar.loadSeeToolbar();
		userPanel.setPanelEnabled(false);
		insertPanel.setPanelEnabled(false);
		tablePanel.setPanelEnabled(false);

	}

	/**
	 * Actualiza los datos del modulo
	 * <p>
	 * Creation date 5/05/2006 - 09:11:30 AM
	 * 
	 * @param message
	 *            Mensaje a mostrar
	 * @since 1.0
	 */
	public void updateMainPanel(String message) {
		if (moduleForm != null)
			moduleForm.updateData(message);
	}

	/**
	 * Convierte el formato de tipo ver a tipo agregar manteniendo los datos
	 * existentes en el formato. Desarrollado para el comando copy
	 * <p>
	 * Creation date 17/05/2006 - 08:00:07 AM
	 * 
	 * @since 1.0
	 */
	public void transferSeeToAdd() {
		remove(toolBar);
		toolBar = new FormToolBar(command);
		add(toolBar, 0);

		userPanel.reload();
		userPanel.setFocus();
		insertPanel.descriptionFld.updateData(getFormItems());

		setTitle("Creacion de nueva " + this.identifier);
		userPanel.setPanelEnabled(true);
		insertPanel.setPanelEnabled(true);
		tablePanel.setPanelEnabled(true);
	}

	/**
	 * Retorna el modulo propietario del formulario.
	 * <p>
	 * Creation date 27/05/2006 - 10:37:38 AM
	 * 
	 * @return Modulo propietario del formulario
	 * @since 1.0
	 */
	public ModuleForm getModuleForm() {
		return moduleForm;
	}

	/**
	 * Retorna el color significativo del formato. Para diferenciar de los
	 * diferentes tipos de implementacion
	 * <p>
	 * Creation date 28/05/2006 - 11:15:04 AM
	 * 
	 * @return Color significativo del formato
	 * @since 1.0
	 */
	public Color getFormColor() {
		return foreground;
	}

	/**
	 * Retorna los elementos a mostrar en el panel de ingreso
	 * <p>
	 * Creation date 31/05/2006 - 09:22:04 PM
	 * 
	 * @return Elementos a mostrar en el panel de ingreso
	 * @since 1.0
	 */
	public ArrayList<String> getFormItems() {
		return moduleForm.getFormItems();
	}

}
