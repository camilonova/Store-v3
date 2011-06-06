package com.nova.modules.provider.log;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.dat.parameter.GlobalConstants;
import com.nova.gui.StoreSession;
import com.nova.log.input.special.AutoCompleteJComboBox;
import com.nova.log.input.validator.IdentificationValidatorField;
import com.nova.log.input.validator.MoneyValidatorField;
import com.nova.log.input.validator.NumberValidatorField;
import com.nova.log.input.validator.TextValidatorField;

/**
 * Esta clase se encarga de construir y dar las caracteristicas de la interfaz
 * de usuario que permite agregar o editar un proveedor del sistema.
 * <p>
 * Agregada funcionalidad de creditos.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
abstract class ProviderDataGUI extends JDialog {

	TextValidatorField nombreFld;

	IdentificationValidatorField identificationFld;

	AutoCompleteJComboBox ciudadCbx;

	NumberValidatorField telefonoFld;

	NumberValidatorField faxFld;

	NumberValidatorField movilFld;

	TextValidatorField direccionFld;

	TextValidatorField contactNameFld;

	NumberValidatorField contactNumberFld;

	TextValidatorField productsFld;

	MoneyValidatorField creditoFld;

	protected JButton aceptarBtn;

	protected JButton cancelarBtn;

	/**
	 * Construye la interfaz de usuario, dando las carateristicas a los
	 * componentes que la componen, permitiendo el ingreso de los datos del
	 * proveedor.
	 * 
	 * @since 1.0
	 */
	public ProviderDataGUI() {
		JPanel infoPanel = new JPanel();
		JPanel dataPanel = new JPanel();
		JPanel contactPanel = new JPanel();
		JPanel moreInfoPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();

		aceptarBtn = new JButton("Aceptar");
		cancelarBtn = new JButton("Cancelar");
		nombreFld = new TextValidatorField(aceptarBtn, cancelarBtn);
		identificationFld = new IdentificationValidatorField(aceptarBtn,
				cancelarBtn);
		ciudadCbx = new AutoCompleteJComboBox(StoreCore.getAllColumnData(
				"ciudades", "Ciudad"));
		telefonoFld = new NumberValidatorField(10, aceptarBtn, cancelarBtn);
		faxFld = new NumberValidatorField(10, aceptarBtn, cancelarBtn);
		movilFld = new NumberValidatorField(10, aceptarBtn, cancelarBtn);
		direccionFld = new TextValidatorField(100, aceptarBtn, cancelarBtn);
		contactNameFld = new TextValidatorField(aceptarBtn, cancelarBtn);
		contactNumberFld = new NumberValidatorField(10, aceptarBtn, cancelarBtn);
		productsFld = new TextValidatorField(aceptarBtn, cancelarBtn);
		creditoFld = new MoneyValidatorField();

		creditoFld.setDouble(0);
		creditoFld.setEnabled(false);

		infoPanel.setPreferredSize(new Dimension(390, 90));
		dataPanel.setPreferredSize(new Dimension(390, 90));
		contactPanel.setPreferredSize(new Dimension(390, 90));
		moreInfoPanel.setPreferredSize(new Dimension(390, 90));

		nombreFld.setPreferredSize(new Dimension(310, 20));
		identificationFld.setPreferredSize(new Dimension(90, 20));
		ciudadCbx.setPreferredSize(new Dimension(170, 20));
		telefonoFld.setPreferredSize(new Dimension(80, 20));
		faxFld.setPreferredSize(new Dimension(80, 20));
		movilFld.setPreferredSize(new Dimension(80, 20));
		direccionFld.setPreferredSize(new Dimension(310, 20));
		contactNameFld.setPreferredSize(new Dimension(310, 20));
		contactNumberFld.setPreferredSize(new Dimension(80, 20));
		productsFld.setPreferredSize(new Dimension(310, 20));
		creditoFld.setPreferredSize(new Dimension(80, 20));

		infoPanel.setBorder(BorderFactory
				.createTitledBorder("Informacion General"));
		dataPanel.setBorder(BorderFactory
				.createTitledBorder("Datos de Contacto"));
		contactPanel.setBorder(BorderFactory
				.createTitledBorder("Informacion del Contacto"));
		moreInfoPanel.setBorder(BorderFactory
				.createTitledBorder("Informacion Adicional"));

		try {
			ciudadCbx.setSelectedItem(StoreCore.getProperty("DEFAULT_CITY"));
		} catch (Exception e1) {
			ErrorLogLoader.addErrorEntry(e1);
			e1.printStackTrace();
		}

		cancelarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		infoPanel.add(new JLabel("Nombre"));
		infoPanel.add(nombreFld);
		infoPanel.add(new JLabel("NIT/CC"));
		infoPanel.add(identificationFld);
		infoPanel.add(new JLabel("Ciudad"));
		infoPanel.add(ciudadCbx);

		dataPanel.add(new JLabel("Telefono"));
		dataPanel.add(telefonoFld);
		dataPanel.add(new JLabel("Fax"));
		dataPanel.add(faxFld);
		dataPanel.add(new JLabel("Movil"));
		dataPanel.add(movilFld);
		dataPanel.add(new JLabel("Direccion"));
		dataPanel.add(direccionFld);

		contactPanel.add(new JLabel("Nombre"));
		contactPanel.add(contactNameFld);
		contactPanel.add(new JLabel("Telefono"));
		contactPanel.add(contactNumberFld);
		contactPanel.add(Box.createHorizontalStrut(230));

		moreInfoPanel.add(new JLabel("Productos"));
		moreInfoPanel.add(productsFld);
		moreInfoPanel.add(Box.createHorizontalStrut(200));
		moreInfoPanel.add(new JLabel("Estado Credito"));
		moreInfoPanel.add(creditoFld);

		buttonsPanel.add(aceptarBtn);
		buttonsPanel.add(cancelarBtn);

		add(infoPanel);
		add(dataPanel);
		add(contactPanel);
		add(moreInfoPanel);
		add(buttonsPanel);

		setSize(800, 260);
		setLayout(new FlowLayout());
		setComponentsTooltip();
		setLocation((GlobalConstants.SCREEN_SIZE_WIDTH - getWidth()) / 2,
				(GlobalConstants.SCREEN_SIZE_HEIGHT - getHeight()) / 2);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setModal(true);
		setAlwaysOnTop(true);
	}

	/**
	 * Da una descripcion a cada componente de la interfaz
	 * 
	 * @since 1.0
	 */
	private void setComponentsTooltip() {
		nombreFld.setToolTipText("Nombre del proveedor de la organizacion");
		identificationFld
				.setToolTipText("NIT, Cedula o Numero de identificacion");
		ciudadCbx
				.setToolTipText("Ciudad donde el proveedor tiene su centro de operaciones");
		telefonoFld.setToolTipText("Telefono de contacto directo");
		faxFld.setToolTipText("Fax o Telefax");
		movilFld.setToolTipText("Celular del contacto principal");
		direccionFld.setToolTipText("Direccion fisica del proveedor");
		contactNameFld.setToolTipText("Nombre del contacto principal");
		contactNumberFld.setToolTipText("Telefono del contacto principal");
		productsFld.setToolTipText("Descripcion de los productos distribuidos");
		creditoFld.setToolTipText("Estado de credito");
		cancelarBtn.setToolTipText("Descartar datos y volver");
	}

	/**
	 * Verifica si los datos son validos para ser ingresados al sistema
	 * 
	 * @return True si los datos son validos
	 * @since 1.0
	 */
	protected boolean isValidData() {
		if (nombreFld.getText().length() == 0) {
			nombreFld.requestFocus();
			return false;
		}
		if (identificationFld.getText().length() == 0) {
			identificationFld.requestFocus();
			return false;
		}

		return true;
	}

	/**
	 * Retorna los datos del formato listos para ser ingresados a la base de
	 * datos.
	 * 
	 * @param providerID
	 *            ID del proveedor, nuevo o actualizando datos
	 * @return Datos listos para ingresar a la base de datos
	 * @since 1.0
	 */
	protected ArrayList<String> getUserData(String providerID) {
		ArrayList<String> data = new ArrayList<String>();
		data.add(providerID);
		data.add(nombreFld.getText());
		data.add(identificationFld.getText());
		data.add((String) ciudadCbx.getSelectedItem());
		data.add(telefonoFld.getText());
		data.add(faxFld.getText());
		data.add(movilFld.getText());
		data.add(direccionFld.getText());
		data.add(productsFld.getText());
		data.add(contactNameFld.getText());
		data.add(contactNumberFld.getText());
		try {
			data.add(String.valueOf(creditoFld.getDouble()));
		} catch (ParseException e) {
			data.add("0");
		}
		data.add(StoreSession.getUserName());
		data.add("NOW()");

		return data;
	}
}