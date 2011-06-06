package com.nova.modules.invoice.log;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.dat.parameter.GlobalConstants;
import com.nova.gui.StoreSession;
import com.nova.log.form.dat.FormRegister;
import com.nova.log.form.gui.Form;
import com.nova.log.form.log.FormPrint;
import com.nova.log.input.validator.MoneyValidatorField;
import com.nova.modules.kardex.log.KardexDataManager;

/**
 * Clase encargada del registro de una factura en el sistema
 * TODO Agregar ventas a credito
 * @author Camilo Nova
 * @version 1.0
 */
public class RegisterInvoice extends FormRegister {

	private double total;

	private JLabel efectivoLbl;

	private MoneyValidatorField efectivoFld;
	
	private JRadioButton contadoRBtn;
	
	private JRadioButton creditoRBtn;
	
	private ButtonGroup tipoGrp;

	private JButton okBtn;

	private JButton cancelBtn;

	private String invoiceNumber;

	/**
	 * Registra una factura en el sistema
	 * <p>
	 * Creation date 27/05/2006 - 02:08:39 PM
	 * 
	 * @param form
	 *            Formato con datos a registrar
	 * 
	 * @since 1.0
	 */
	public RegisterInvoice(Form form) {
		super(form);
		invoiceNumber = form.getConsecutiveNumber();
		total = Double.parseDouble(form.getTotal());

		showGUI();
	}

	@Override
	protected void showGUI() {
		JPanel upPnl = new JPanel();
		JPanel midPnl = new JPanel();
		JPanel lowPnl = new JPanel();

		okBtn = new JButton("Aceptar");
		cancelBtn = new JButton("Cancelar");

		efectivoLbl = new JLabel("EFECTIVO");
		efectivoFld = new MoneyValidatorField(okBtn, cancelBtn);
		contadoRBtn = new JRadioButton("Contado", true);
		creditoRBtn = new JRadioButton("Credito");
		tipoGrp = new ButtonGroup();
		
		tipoGrp.add(contadoRBtn);
		tipoGrp.add(creditoRBtn);

		efectivoFld.setDouble(total);
		efectivoFld.setPreferredSize(new Dimension(170, 25));
		efectivoLbl.setFont(new Font("Arial", Font.PLAIN, 20));
		efectivoFld.setFont(new Font("Arial", Font.BOLD, 20));
		
		upPnl.setBorder(BorderFactory.createTitledBorder("Total a cancelar"));
		midPnl.setBorder(BorderFactory.createTitledBorder("Tipo de cancelacion"));

		okBtn.addActionListener(new ActionListener() {

			@SuppressWarnings("synthetic-access")
			public void actionPerformed(ActionEvent e) {
				if (isValidData()) {
					register();
					try {
						efectivoFld.setDouble(efectivoFld.getDouble() - total);
						JOptionPane.showMessageDialog(RegisterInvoice.this,
								"<html><center><h1>Cambio "
										+ efectivoFld.getText()
										+ "</h1></center></html>");
					} catch (ParseException e1) {
						// Nunca deberia pasar
						ErrorLogLoader.addErrorEntry(e1);
						e1.printStackTrace();
					}
					dispose();

					if (JOptionPane.showConfirmDialog(form,
							"Desea imprimir la factura?", "Imprimir",
							JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

						ArrayList<String> userFormData = new ArrayList<String>();
						ArrayList<String> rowData = StoreCore.getAllRowData(
								"clientes", "Cliente", form.getFormUser());
						userFormData.add(rowData.get(2));
						userFormData.add(rowData.get(3));
						userFormData.add(rowData.get(4));
						userFormData.add(rowData.get(7));

						new FormPrint(form, userFormData);
					}

					form.reloadForm();
				} else {
					JOptionPane.showMessageDialog(RegisterInvoice.this,
							"Vefifique el valor ingresado");
					efectivoFld.requestFocus();
					efectivoFld.selectAll();
				}
			}

		});
		cancelBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}

		});

		upPnl.add(efectivoLbl);
		upPnl.add(efectivoFld);
		
		midPnl.add(contadoRBtn);
		midPnl.add(creditoRBtn);

		lowPnl.add(okBtn);
		lowPnl.add(cancelBtn);

		add(upPnl, BorderLayout.NORTH);
		add(midPnl, BorderLayout.CENTER);
		add(lowPnl, BorderLayout.SOUTH);

		setSize(300, 180);
		setTitle("Registrar...");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocation((GlobalConstants.SCREEN_SIZE_WIDTH - getWidth()) / 2,
				(GlobalConstants.SCREEN_SIZE_HEIGHT - getHeight()) / 2);
		setModal(true);
		setResizable(false);
		setVisible(true);
	}

	@Override
	protected void register() {
		ArrayList<String> data = new ArrayList<String>();
		ArrayList<String[]> tableData = form.getTableModel().getTableData();

		data.add(invoiceNumber);
		data.add(form.getFormUser());
		data.add(form.getFormSQLDate());
		data.add(form.getExempt());
		data.add(form.getBase());
		data.add(form.getTax());
		data.add(form.getTotal());
		data.add(StoreSession.getUserName());

		// Kardex
		for (String[] row : tableData) {
			String barCode = StoreCore.getStockBarCode(row[1]);
			boolean b = KardexDataManager.sellStockItem(invoiceNumber, barCode,
					Integer.parseInt(row[0]));
			if (b == false)
				System.err.println("Error en articulo " + barCode);
		}
		// Elementos
		StoreCore.registerFormMetaData("facturacion", invoiceNumber, tableData);
		// Factura
		try {
			StoreCore.newData("facturacion", data);
			form.updateMainPanel("Factura " + invoiceNumber + " agregada");
		} catch (SQLException e) {
			form.updateMainPanel("Error. Verifique los datos");
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
	}

	/**
	 * Verifica que la cantidad ingresada sea mayor o igual al total
	 * <p>
	 * Creation date 27/05/2006 - 01:51:56 PM
	 * 
	 * @return True, si la cantidad ingresada es mayor que el total
	 * @since 1.0
	 */
	private boolean isValidData() {
		try {
			if (efectivoFld.getDouble() < total)
				return false;
		} catch (Exception e) {
			// No deberia pasar
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		return true;
	}
}
