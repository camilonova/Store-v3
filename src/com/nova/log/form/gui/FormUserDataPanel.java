package com.nova.log.form.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.log.input.special.AutoCompleteJComboBox;
import com.nova.modules.invoice.gui.InvoiceMainPanel;
import com.nova.modules.purchase.gui.PurchaseMainPanel;
import com.nova.modules.quotation.gui.QuoteMainPanel;
import com.toedter.calendar.JDateChooser;

/**
 * Esta clase representa panel del usuario que contiene inicialmente el nombre y
 * el consecutivo del formato.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class FormUserDataPanel extends JPanel {

	JLabel userLbl;

	AutoCompleteJComboBox userFld;

	JLabel dateLbl;

	JDateChooser dateFld;

	JLabel consecutiveLbl;

	JTextField consecutiveFld;

	String user;

	ArrayList<String> data;

	String consecutive;

	String consecutiveValue;

	private String consecutiveTable;

	private String userTable;

	/**
	 * Construye los componentes y los agrega al panel, inicialmente solo se
	 * tiene en cuenta el nombre del usuario y el numero del formato.
	 * <p>
	 * Creation date 20/04/2006 - 09:35:57 PM
	 * 
	 * @param form
	 *            Formato principal
	 * 
	 * @since 1.0
	 */
	public FormUserDataPanel(Form form) {
		if (form.getModuleForm() instanceof QuoteMainPanel)
			loadQuotation();
		else if (form.getModuleForm() instanceof InvoiceMainPanel)
			loadInvoice();
		else if (form.getModuleForm() instanceof PurchaseMainPanel)
			loadPurchase();

		userLbl = new JLabel(user, SwingConstants.CENTER);
		userFld = new AutoCompleteJComboBox(data);
		dateLbl = new JLabel("Fecha", SwingConstants.CENTER);
		dateFld = new JDateChooser(new Date());
		consecutiveLbl = new JLabel(consecutive, SwingConstants.CENTER);
		consecutiveFld = new JTextField(consecutiveValue);

		userLbl.setPreferredSize(new Dimension(60, 20));
		userFld.setPreferredSize(new Dimension(350, 20));
		dateLbl.setPreferredSize(new Dimension(50, 20));
		dateFld.setPreferredSize(new Dimension(120, 20));
		consecutiveLbl.setPreferredSize(new Dimension(60, 20));
		consecutiveFld.setPreferredSize(new Dimension(100, 20));

		userFld.setToolTipText(user + " del formato");
		dateFld.setToolTipText("Fecha");
		consecutiveFld.setToolTipText("Numero de " + consecutive);

		dateFld.setEnabled(false);
		consecutiveFld.setEditable(false);
		consecutiveFld.setHorizontalAlignment(SwingConstants.RIGHT);
		consecutiveFld.setFont(new Font("Arial", Font.BOLD, 16));
		consecutiveFld.setForeground(form.getFormColor());

		add(userLbl);
		add(userFld);
		add(dateLbl);
		add(dateFld);
		add(consecutiveLbl);
		add(consecutiveFld);

		setBorder(BorderFactory.createTitledBorder("Informacion General"));
		setPreferredSize(new Dimension(790, 65));
	}

	/**
	 * Prepara el formato para mostrar una cotizacion
	 * <p>
	 * Creation date 4/05/2006 - 08:04:10 AM
	 * 
	 * @since 1.0
	 */
	private void loadQuotation() {
		userTable = "clientes";
		user = "Cliente";
		data = StoreCore.getAllColumnData(userTable, user);
		consecutiveTable = "cotizacion";
		consecutive = "Cotizacion";
		consecutiveValue = StoreCore.getNextId(consecutiveTable, consecutive);
		try {
			if (consecutiveValue.equals("1"))
				consecutiveValue = StoreCore.getProperty("QUOTATION_INDEX");
		} catch (Exception e) {
			// No debe pasar
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
	}

	/**
	 * Prepara el formato para mostrar una factura
	 * <p>
	 * Creation date 17/05/2006 - 03:44:31 PM
	 * 
	 * @since 1.0
	 */
	private void loadInvoice() {
		userTable = "clientes";
		user = "Cliente";
		data = StoreCore.getAllColumnData(userTable, user);
		consecutiveTable = "facturacion";
		consecutive = "Factura";
		consecutiveValue = StoreCore.getNextId(consecutiveTable, consecutive);
		try {
			if (consecutiveValue.equals("1"))
				consecutiveValue = StoreCore.getProperty("INVOICE_INDEX");
		} catch (Exception e) {
			// No debe pasar
			e.printStackTrace();
		}
	}

	private void loadPurchase() {
		userTable = "proveedores";
		user = "Proveedor";
		data = StoreCore.getAllColumnData(userTable, user);
		consecutiveTable = "compra";
		consecutive = "Compra";
		consecutiveValue = StoreCore.getNextId(consecutiveTable, consecutive);
		try {
			if (consecutiveValue.equals("1"))
				consecutiveValue = StoreCore.getProperty("PURCHASE_INDEX");
		} catch (Exception e) {
			// No debe pasar
			e.printStackTrace();
		}
	}

	/**
	 * Retorna el numero del formato
	 * <p>
	 * Creation date 20/04/2006 - 09:35:07 PM
	 * 
	 * @return Consecutivo del formato
	 * @since 1.0
	 */
	String getFormNumber() {
		return consecutiveFld.getText();
	}

	/**
	 * Determina el numero del formato
	 * <p>
	 * Creation date 4/05/2006 - 07:56:34 AM
	 * 
	 * @param formNumber
	 *            Numero del formato
	 * @since 1.0
	 */
	void setFormNumber(String formNumber) {
		consecutiveFld.setText(formNumber);
	}

	/**
	 * Retorna el nombre del usuario del formulario
	 * <p>
	 * Creation date 20/04/2006 - 09:35:19 PM
	 * 
	 * @return Usuario del formato
	 * @since 1.0
	 */
	String getSelectedUser() {
		return (String) userFld.getSelectedItem();
	}

	/**
	 * Determina el usuario del formato
	 * <p>
	 * Creation date 4/05/2006 - 07:58:52 AM
	 * 
	 * @param user
	 *            Usuario del formato
	 * @since 1.0
	 */
	void setSelectedUser(String user) {
		userFld.setSelectedItem(user);
	}

	/**
	 * Determina el estado del componente
	 * <p>
	 * Creation date 4/05/2006 - 10:02:40 AM
	 * 
	 * @param enable
	 *            True para habilitar el componente, false de lo contrario
	 * @since 1.0
	 */
	void setPanelEnabled(boolean enable) {
		userFld.setEnabled(enable);
		dateFld.setEnabled(enable);
		consecutiveFld.setEnabled(enable);
	}

	/**
	 * Transfiere el focus al componente del usuario del formato
	 * <p>
	 * Creation date 20/04/2006 - 10:09:34 PM
	 * 
	 * @since 1.0
	 */
	void setFocus() {
		userFld.requestFocus();
	}

	/**
	 * Recarga la informacion del panel
	 * <p>
	 * Creation date 3/05/2006 - 10:07:45 AM
	 * 
	 * @since 1.0
	 */
	public void reload() {
		dateFld.setDate(new Date());
		userFld.setModel(new DefaultComboBoxModel(StoreCore.getAllColumnData(
				userTable, user).toArray()));
		userFld.setSelectedItem("");
		consecutiveValue = StoreCore.getNextId(consecutiveTable, consecutive);
		consecutiveFld.setText(consecutiveValue);
	}

}
