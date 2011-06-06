package com.nova.modules.customer.log;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import com.nova.log.input.validator.PercentValidatorField;
import com.nova.log.input.validator.TextValidatorField;

/**
 * Esta clase se encarga de construir y dar las caracteristicas de la interfaz
 * de usuario que permite agregar o editar un cliente del sistema.
 * <p>
 * Agregada funcionalidad de creditos.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
abstract class CustomerDataGUI extends JDialog {

	TextValidatorField nombreFld;

	IdentificationValidatorField identificationFld;

	AutoCompleteJComboBox ciudadCbx;

	NumberValidatorField telefonoFld;

	NumberValidatorField faxFld;

	NumberValidatorField movilFld;

	TextValidatorField direccionFld;

	JCheckBox creditoHabilitadoCbx;

	MoneyValidatorField creditoMaxFld;

	MoneyValidatorField creditoSaldoFld;

	PercentValidatorField calificacionFld;

	protected JButton aceptarBtn;

	protected JButton cancelarBtn;

	/**
	 * Construye la interfaz de usuario, dando las carateristicas a los
	 * componentes que la componen, permitiendo el ingreso de los datos del
	 * cliente.
	 * 
	 * @since 1.0
	 */
	public CustomerDataGUI() {
		JPanel infoPanel = new JPanel();
		JPanel dataPanel = new JPanel();
		JPanel creditPanel = new JPanel();
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

		creditoHabilitadoCbx = new JCheckBox("Habilitar Credito");
		creditoMaxFld = new MoneyValidatorField(25, aceptarBtn, cancelarBtn);
		creditoSaldoFld = new MoneyValidatorField(25);
		calificacionFld = new PercentValidatorField(3);

		creditoMaxFld.setDouble(0);
		calificacionFld.setText("0%");
		creditoSaldoFld.setDouble(0);
		creditoMaxFld.setEnabled(false);
		calificacionFld.setEnabled(false);
		creditoSaldoFld.setEnabled(false);

		infoPanel.setPreferredSize(new Dimension(390, 90));
		dataPanel.setPreferredSize(new Dimension(390, 90));
		creditPanel.setPreferredSize(new Dimension(390, 90));

		nombreFld.setPreferredSize(new Dimension(310, 20));
		identificationFld.setPreferredSize(new Dimension(90, 20));
		ciudadCbx.setPreferredSize(new Dimension(170, 20));
		telefonoFld.setPreferredSize(new Dimension(80, 20));
		faxFld.setPreferredSize(new Dimension(80, 20));
		movilFld.setPreferredSize(new Dimension(80, 20));
		direccionFld.setPreferredSize(new Dimension(310, 20));
		creditoHabilitadoCbx.setPreferredSize(new Dimension(150, 20));
		creditoMaxFld.setPreferredSize(new Dimension(120, 20));
		calificacionFld.setPreferredSize(new Dimension(70, 20));
		creditoSaldoFld.setPreferredSize(new Dimension(120, 20));

		infoPanel.setBorder(BorderFactory
				.createTitledBorder("Informacion General"));
		dataPanel.setBorder(BorderFactory
				.createTitledBorder("Datos de Contacto"));
		creditPanel.setBorder(BorderFactory
				.createTitledBorder("Informacion Financiera"));

		try {
			ciudadCbx.setSelectedItem(StoreCore.getProperty("DEFAULT_CITY"));
		} catch (Exception e1) {
			ErrorLogLoader.addErrorEntry(e1);
			e1.printStackTrace();
		}

		creditoHabilitadoCbx.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				boolean b = creditoHabilitadoCbx.isSelected();
				creditoMaxFld.setEnabled(b);
				calificacionFld.setEnabled(b);
			}

		});
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

		creditPanel.add(creditoHabilitadoCbx);
		creditPanel.add(new JLabel("Cupo Maximo"));
		creditPanel.add(creditoMaxFld);
		creditPanel.add(new JLabel("Calificacion"));
		creditPanel.add(calificacionFld);
		creditPanel.add(new JLabel("Estado Actual"));
		creditPanel.add(creditoSaldoFld);

		buttonsPanel.add(aceptarBtn);
		buttonsPanel.add(cancelarBtn);

		add(infoPanel);
		add(dataPanel);
		add(creditPanel);
		add(buttonsPanel);

		setSize(400, 360);
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
		nombreFld
				.setToolTipText("Nombre de la persona o Empresa cliente de la organizacion");
		identificationFld
				.setToolTipText("NIT, Cedula o Numero de identificacion");
		ciudadCbx
				.setToolTipText("Ciudad donde el cliente tiene su centro de operaciones");
		telefonoFld.setToolTipText("Telefono de contacto directo");
		faxFld.setToolTipText("Fax o Telefax");
		movilFld.setToolTipText("Celular del contacto principal o cliente");
		direccionFld.setToolTipText("Direccion fisica del cliente");
		cancelarBtn.setToolTipText("Descartar datos y volver");
	}

	/**
	 * Verifica si los datos son validos para ser ingresados al sistema
	 * 
	 * @return True si los datos son validos
	 * @since 1.0
	 */
	protected boolean isValidData(boolean isUpdate) {
		if (nombreFld.getText().length() == 0) {
			nombreFld.requestFocus();
			return false;
		}
		if (identificationFld.getText().length() == 0) {
			identificationFld.requestFocus();
			return false;
		}
		if (!isUpdate) {
			if (StoreCore.isRegisteredData("clientes", "Cliente", nombreFld.getText())) {
				nombreFld.requestFocus();
				return false;
			}
			if (StoreCore.isRegisteredData("clientes", "Identificacion", identificationFld.getText())) {
				identificationFld.requestFocus();
				return false;
			}
		}

		return true;
	}

	/**
	 * Retorna los datos del formato listos para ser ingresados a la base de
	 * datos. Si el cliente no tiene habilitado el credito, los datos de los
	 * campos relacionados con credito tienen el valor NULL.
	 * 
	 * @param customerID
	 *            ID del cliente, nuevo o actualizando datos
	 * @return Datos listos para ingresar a la base de datos
	 * @since 1.0
	 */
	protected ArrayList<String> getUserData(String customerID) {
		ArrayList<String> data = new ArrayList<String>();
		data.add(customerID);
		data.add(nombreFld.getText());
		data.add(identificationFld.getText());
		data.add((String) ciudadCbx.getSelectedItem());
		data.add(telefonoFld.getText());
		data.add(faxFld.getText());
		data.add(movilFld.getText());
		data.add(direccionFld.getText());
		if (!creditoHabilitadoCbx.isSelected()) {
			data.add(null);
			data.add(null);
			data.add(null);
		} else {
			try {
				data.add(String.valueOf(creditoSaldoFld.getDouble()));
				data.add(calificacionFld.getText());
				data.add(String.valueOf(creditoMaxFld.getDouble()));
			} catch (ParseException e) {
				// No deberia pasar
				ErrorLogLoader.addErrorEntry(e);
				e.printStackTrace();
			}
		}
		data.add(StoreSession.getUserName());
		data.add("NOW()");

		return data;
	}
}