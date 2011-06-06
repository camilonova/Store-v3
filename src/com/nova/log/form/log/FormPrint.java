package com.nova.log.form.log;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.nova.dat.loader.ErrorLogLoader;
import com.nova.dat.parameter.GlobalConstants;
import com.nova.gui.shared.converter.NumberToLetterConverter;
import com.nova.log.form.gui.Form;
import com.nova.log.input.special.DateUtils;
import com.nova.log.math.StoreMath;
import com.nova.modules.stock.log.StockItem;

/**
 * Esta clase se encarga de manejar la impresion del formato en sus diferentes
 * tipos
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class FormPrint extends JFrame implements Runnable {

	private PrintJob printJob;

	private final Form form;

	private Graphics page;

	private String consecutive;

	private String date;

	private String user;

	private String identification;

	private String city;

	private String address;

	private String phone;

	private FontMetrics fontMetrics;

	private static final int ROWS_PER_PAGE = 25;

	private static int FINAL_PAGE;

	private static int CURRENT_PAGE = 0;

	private final ArrayList<String> userFormData;

	/**
	 * Imprime el formato. Inicialmente solo compatible con un tipo de impresion
	 * <p>
	 * Creation date 5/05/2006 - 08:37:16 AM
	 * 
	 * @param form
	 *            Formato a imprimir
	 * @param userFormData
	 *            Datos del usuario del formato, debe contener en estricto orden
	 *            (Identificacion, Ciudad, Telefono, Direccion)
	 * 
	 * @since 1.0
	 */
	public FormPrint(Form form, ArrayList<String> userFormData) {
		this.form = form;
		this.userFormData = userFormData;

		FINAL_PAGE = (form.getTable().getRowCount() - 1) / ROWS_PER_PAGE + 1;
		setUndecorated(true);

		// Preparar ventana
		JLabel label = new JLabel("Imprimiendo el formato...",
				SwingConstants.CENTER);
		label.setPreferredSize(new Dimension(200, 25));
		label.setForeground(Color.RED);

		add(label, BorderLayout.CENTER);
		pack();
		setLocation((GlobalConstants.SCREEN_SIZE_WIDTH - getWidth()) / 2,
				(GlobalConstants.SCREEN_SIZE_HEIGHT - getHeight()) / 4);
		setLayout(new FlowLayout());
		setResizable(false);
		setAlwaysOnTop(true);
		form.setAlwaysOnTop(false);

		Thread thread = new Thread(this, "PrintingProcess");
		thread.start();

		printJob = Toolkit.getDefaultToolkit().getPrintJob(this,
				"STORE_FORMAT", null);
		if (printJob != null)
			printForm();

		// Cerramos la ventana
		form.setAlwaysOnTop(true);
		dispose();
	}

	public void run() {
		setVisible(true);
	}

	/**
	 * Imprime el formato desde un hilo
	 * <p>
	 * Creation date 5/05/2006 - 08:38:53 AM
	 * 
	 * @since 1.0
	 */
	private void printForm() {
		consecutive = form.getConsecutiveNumber();
		Calendar calendar = DateUtils.SQLtoCalendar(form.getFormSQLDate());
		date = DateFormat.getDateInstance().format(calendar.getTime());
		user = form.getFormUser();
		identification = userFormData.get(0);
		city = userFormData.get(1);
		phone = userFormData.get(2);
		address = userFormData.get(3);

		printHeader();
		printBody();
		printFooter();
	}

	/**
	 * Imprime la parte inicial
	 * <p>
	 * Creation date 5/05/2006 - 08:38:44 AM
	 * 
	 * @since 1.0
	 */
	private void printHeader() {
		page = printJob.getGraphics();
		CURRENT_PAGE++;
		page.setFont(new Font("Dialog", Font.BOLD, 16));
		page.setColor(Color.BLACK);

		page.drawString(consecutive, 500, 180);
		page.setFont(new Font("Dialog", Font.PLAIN, 12));
		page.drawString(date, 100, 180);
		page.drawString(user, 100, 215);
		page.drawString(identification, 450, 215);
		page.drawString(city, 100, 245);
		page.drawString(address, 240, 245);
		page.drawString(phone, 470, 245);

		// Numero de la pagina
		page.setFont(new Font("Arial", Font.PLAIN, 8));
		page.drawString("Pagina " + String.valueOf(CURRENT_PAGE) + " de "
				+ FINAL_PAGE, 520, 160);

	}

	/**
	 * Imprime la parte central
	 * <p>
	 * Creation date 5/05/2006 - 08:38:36 AM
	 * 
	 * @since 1.0
	 */
	private void printBody() {
		page.setFont(new Font("Arial", Font.PLAIN, 8));

		ArrayList<String[]> data = form.getTableModel().getTableData();
		for (int i = 0, space = 0; i < data.size(); i++, space++) {
			int yCordinate = (13 * space) + 290;
			String[] rowData = data.get(i);
			StockItem stockItem;
			try {
				stockItem = new StockItem(rowData[1],
						StockItem.DESCRIPTION_BRAND);
			} catch (Exception e) {
				stockItem = null;
				ErrorLogLoader.addErrorEntry(e);
				e.printStackTrace();
			}

			String item = String.valueOf(i + 1);
			String description = rowData[1];
			String packing = stockItem.getPacking() != null ? stockItem
					.getPacking() : "";
			String quantity = rowData[0];
			double bs = StoreMath.parseString(rowData[3]);
			double tx = StoreMath.parseString(rowData[4]);
			double unitValue = StoreMath.divide(bs, Double
					.parseDouble(quantity), true);
			String exemptUnit;
			String baseUnit;
			if (tx == 0) {
				exemptUnit = String.valueOf(unitValue);
				baseUnit = new String();
			} else {
				exemptUnit = new String();
				baseUnit = String.valueOf(unitValue);
			}
			String totalNoTax = String.valueOf(bs);

			drawStringToRight(item, 50, yCordinate);
			if (fontMetrics.stringWidth(description) < 250) {
				page.drawString(description, 65, yCordinate);
			} else {
				page.drawString(description.substring(0, 60), 65, yCordinate);
				yCordinate = (13 * space++) + 290;
				page.drawString(description.substring(60), 65, yCordinate);
			}
			page.drawString(packing, 315, yCordinate);
			drawStringToRight(quantity, 380, yCordinate);
			drawStringToRight(baseUnit, 450, yCordinate);
			drawStringToRight(exemptUnit, 510, yCordinate);
			drawStringToRight(totalNoTax, 570, yCordinate);

			if (Integer.parseInt(item) % ROWS_PER_PAGE == 0
					&& CURRENT_PAGE < FINAL_PAGE) {
				page.setFont(new Font("Dialog", Font.PLAIN, 12));
				page.drawString("Continua...", 500, 734);
				space = 0;
				printHeader();
			}
		}
	}

	/**
	 * Imprime la parte final
	 * <p>
	 * Creation date 5/05/2006 - 08:38:10 AM
	 * 
	 * @since 1.0
	 */
	private void printFooter() {
		String exempt = StoreMath.parseDouble(Double.parseDouble(form
				.getExempt()));
		String base = StoreMath.parseDouble(Double.parseDouble(form.getBase()));
		String tax = StoreMath.parseDouble(Double.parseDouble(form.getTax()));
		double add = StoreMath.add(Double.parseDouble(form.getExempt()), Double
				.parseDouble(form.getBase()), true);
		double totalDouble = Double.parseDouble(form.getTotal());
		String subTotal = StoreMath.parseDouble(add);
		String total = StoreMath.parseDouble(totalDouble);

		// Total en letras
		page.setFont(new Font("Arial", Font.PLAIN, 6));
		page.drawString(NumberToLetterConverter.convertNumberToLetter(StoreMath
				.parseString(total)), 80, 655);

		// Totales
		page.setFont(new Font("Dialog", Font.PLAIN, 12));
		drawStringToRight(subTotal, 570, 636);
		drawStringToRight(exempt, 570, 660);
		drawStringToRight(base, 570, 682);
		drawStringToRight(tax, 570, 705);

		// Gran Total
		page.setFont(new Font("Dialog", Font.BOLD, 14));
		drawStringToRight(total, 570, 734);

		printJob.end();
	}

	/**
	 * Imprime una cadena alineada a la derecha
	 * <p>
	 * Creation date 5/05/2006 - 08:36:12 AM
	 * 
	 * @param string
	 *            Cadena a imprimir
	 * @param x
	 *            Coordenada horizontal
	 * @param y
	 *            Coordenada vertical
	 * @since 1.0
	 */
	private void drawStringToRight(String string, int x, int y) {
		fontMetrics = page.getFontMetrics();
		page.drawString(string, x - fontMetrics.stringWidth(string), y);
	}

}
