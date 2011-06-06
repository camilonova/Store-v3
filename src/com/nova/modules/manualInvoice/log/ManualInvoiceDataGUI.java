package com.nova.modules.manualInvoice.log;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.dat.parameter.GlobalConstants;
import com.nova.gui.StoreSession;
import com.nova.log.input.special.AutoCompleteJComboBox;
import com.nova.log.input.special.DateUtils;
import com.nova.log.input.validator.MoneyValidatorField;
import com.nova.log.input.validator.NumberValidatorField;
import com.nova.log.math.StoreMath;
import com.nova.modules.customer.log.AddCustomer;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

/**
 * Esta clase provee la interfaz de usuario del modulo de facturacion manual, es
 * abstracta para que las clases encargadas de utilizar la interfaz la hereden
 * para su uso, implica que estas clases se encuentren en en mismo paquete.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
abstract class ManualInvoiceDataGUI extends JDialog {

	JLabel facturaLbl;

	NumberValidatorField facturaFld;

	JLabel fechaLbl;

	JDateChooser fechaFld;

	JLabel clienteLbl;

	AutoCompleteJComboBox clienteCbx;

	JLabel excluidoLbl;

	MoneyValidatorField excluidoFld;

	JLabel gravadoLbl;

	MoneyValidatorField gravadoFld;

	JLabel impuestoLbl;

	JComboBox impuestoCbx;

	JLabel totalLbl;

	MoneyValidatorField totalFdl;

	JRadioButton contadoRBtn;

	JRadioButton creditoRBtn;

	ButtonGroup grupoGrp;

	JCheckBox anuladaCBox;

	JCheckBox seguirAgregandoCBox;

	protected JButton aceptarBtn;

	protected JButton cancelarBtn;

	double taxValue;

	/**
	 * Prepara y agrega los componentes de la interfaz de usuario para agregar
	 * facturas manuales al sistema, permitiendo agregar facturas anuladas y
	 * clientes no registrados.
	 * 
	 * @since 1.0
	 */
	public ManualInvoiceDataGUI() {
		JPanel dataPnl = new JPanel();
		JPanel optionsPnl = new JPanel();
		JPanel buttonsPnl = new JPanel();

		aceptarBtn = new JButton("Aceptar");
		cancelarBtn = new JButton("Cancelar");

		facturaLbl = new JLabel("Factura");
		facturaFld = new NumberValidatorField(6, aceptarBtn, cancelarBtn);
		fechaLbl = new JLabel("Fecha");
		fechaFld = new JDateChooser(new Date());
		clienteLbl = new JLabel("Cliente");
		clienteCbx = new AutoCompleteJComboBox();
		excluidoLbl = new JLabel("Excluido");
		excluidoFld = new MoneyValidatorField(25, aceptarBtn, cancelarBtn);
		gravadoLbl = new JLabel("Gravado");
		gravadoFld = new MoneyValidatorField(25, aceptarBtn, cancelarBtn);
		impuestoLbl = new JLabel("Impuesto");
		impuestoCbx = new JComboBox();
		totalLbl = new JLabel("Total");
		totalFdl = new MoneyValidatorField();
		contadoRBtn = new JRadioButton("Contado", true);
		creditoRBtn = new JRadioButton("Credito", false);
		grupoGrp = new ButtonGroup();
		anuladaCBox = new JCheckBox("ANULADA");
		seguirAgregandoCBox = new JCheckBox("Continuar Agregando");
		
		Thread thread = new Thread(new Runnable() {
		
			public void run() {
				try {
					if (facturaFld.isEnabled()) {
						facturaFld.setText(StoreCore.getNextId(
								"facturacion_manual", "Factura"));
					}
					clienteCbx.setItems(StoreCore.getAllColumnData("clientes",
							"Cliente"));
					impuestoCbx.setModel(new DefaultComboBoxModel(StoreCore
							.getAllColumnData("impuestos", "Impuesto")
							.toArray()));
					impuestoCbx.setSelectedItem(StoreCore
							.getProperty("DEFAULT_TAX"));
					aceptarBtn.setEnabled(true);
				} catch (Exception e) {
					ErrorLogLoader.addErrorEntry(e);
					e.printStackTrace();
				}
			}
		
		}, "Load Manual Invoice Data");
		thread.start();

		grupoGrp.add(contadoRBtn);
		grupoGrp.add(creditoRBtn);

		dataPnl.setPreferredSize(new Dimension(390, 150));
		optionsPnl.setPreferredSize(new Dimension(390, 20));

		facturaLbl.setPreferredSize(new Dimension(60, 20));
		facturaFld.setPreferredSize(new Dimension(50, 20));
		fechaLbl.setPreferredSize(new Dimension(50, 20));
		fechaFld.setPreferredSize(new Dimension(160, 20));
		clienteLbl.setPreferredSize(new Dimension(60, 20));
		clienteCbx.setPreferredSize(new Dimension(270, 20));
		excluidoLbl.setPreferredSize(new Dimension(60, 20));
		excluidoFld.setPreferredSize(new Dimension(100, 20));
		gravadoLbl.setPreferredSize(new Dimension(60, 20));
		gravadoFld.setPreferredSize(new Dimension(100, 20));
		impuestoLbl.setPreferredSize(new Dimension(60, 20));
		impuestoCbx.setPreferredSize(new Dimension(100, 20));
		totalLbl.setPreferredSize(new Dimension(60, 20));
		totalFdl.setPreferredSize(new Dimension(100, 20));

		contadoRBtn.setPreferredSize(new Dimension(150, 20));
		creditoRBtn.setPreferredSize(new Dimension(150, 20));
		anuladaCBox.setPreferredSize(new Dimension(150, 20));
		seguirAgregandoCBox.setPreferredSize(new Dimension(150, 20));

		facturaFld.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				String string = facturaFld.getText();
				if (StoreCore.isRegisteredData("facturacion_manual", "Factura",
						string))
					facturaFld.setForeground(Color.RED);
				else
					facturaFld.setForeground(Color.GREEN);
				facturaFld.repaint();
				super.focusLost(e);
			}

		});
		excluidoFld.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					double excluido = Double.parseDouble(excluidoFld.getText());
					double gravado = gravadoFld.getDouble();
					updateTotal(excluido, gravado);
				} catch (Exception e1) {
					// Ocurre pero no importa
				}
				super.keyReleased(e);
			}

		});
		gravadoFld.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				try {
					double excluido = excluidoFld.getDouble();
					double gravado = Double.parseDouble(gravadoFld.getText());
					updateTotal(excluido, gravado);
				} catch (Exception e1) {
					// Ocurre pero no importa
				}
				super.keyReleased(e);
			}

		});
		impuestoCbx.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					taxValue = Double.parseDouble(
							StoreCore.getDataAt("impuestos", "Impuesto",
									(String) impuestoCbx.getSelectedItem(), "Valor"));
					double excluido = excluidoFld.getDouble();
					double gravado = gravadoFld.getDouble();
					updateTotal(excluido, gravado);
				} catch (Exception e1) {
					// Ocurre pero no importa
				}
			}

		});

		dataPnl.setBorder(BorderFactory.createTitledBorder("Informacion"));
		optionsPnl.setBorder(BorderFactory.createTitledBorder("Opciones"));

		cancelarBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}

		});
		anuladaCBox.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				boolean status = !anuladaCBox.isSelected();
				clienteCbx.setEnabled(status);
				excluidoFld.setEnabled(status);
				gravadoFld.setEnabled(status);
				impuestoCbx.setEnabled(status);
				contadoRBtn.setEnabled(status);
				creditoRBtn.setEnabled(status);
			}

		});

		excluidoFld.setDouble(0);
		gravadoFld.setDouble(0);
		totalFdl.setDouble(0);
		totalFdl.setEnabled(false);
		aceptarBtn.setEnabled(false);
		fechaLbl.setHorizontalAlignment(SwingConstants.CENTER);

		dataPnl.add(facturaLbl);
		dataPnl.add(facturaFld);
		dataPnl.add(fechaLbl);
		dataPnl.add(fechaFld);
		dataPnl.add(clienteLbl);
		dataPnl.add(clienteCbx);
		dataPnl.add(excluidoLbl);
		dataPnl.add(excluidoFld);
		dataPnl.add(gravadoLbl);
		dataPnl.add(gravadoFld);
		dataPnl.add(impuestoLbl);
		dataPnl.add(impuestoCbx);
		dataPnl.add(totalLbl);
		dataPnl.add(totalFdl);

		optionsPnl.add(contadoRBtn);
		optionsPnl.add(creditoRBtn);
		optionsPnl.add(anuladaCBox);
		optionsPnl.add(seguirAgregandoCBox);

		buttonsPnl.add(aceptarBtn);
		buttonsPnl.add(cancelarBtn);

		add(dataPnl, BorderLayout.NORTH);
		add(optionsPnl, BorderLayout.CENTER);
		add(buttonsPnl, BorderLayout.SOUTH);

		setComponentsTooltip();
		setSize(380, 300);
		setLocation((GlobalConstants.SCREEN_SIZE_WIDTH - getWidth()) / 2,
				(GlobalConstants.SCREEN_SIZE_HEIGHT - getHeight()) / 2);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		setResizable(false);
	}

	/**
	 * Metodo que actualiza el total del formato
	 * <p>
	 * Creation date 30/03/2006 - 10:57:59 PM
	 * 
	 * @param excluido
	 *            Valor excluido
	 * @param gravado
	 *            Valor gravado, antes de impuesto
	 * @since 1.0
	 */
	void updateTotal(double excluido, double gravado) {
		excluido += gravado * (1 + taxValue);
		totalFdl.setText(StoreMath.parseDouble(excluido));
	}

	/**
	 * Metodo que agrega los tooltips a los componentes del modulo
	 * <p>
	 * Creation date 30/03/2006 - 10:59:26 PM
	 * 
	 * @since 1.0
	 */
	private void setComponentsTooltip() {
		cancelarBtn.setToolTipText("Cancelar y cerrar la ventana");
		facturaFld
				.setToolTipText("Numero de la factura. En rojo, si ya se encuentra registrada");
		((JTextFieldDateEditor) fechaFld.getDateEditor())
				.setToolTipText("Fecha en que fue hecha la factura");
		clienteCbx.setToolTipText("Cliente de la factura");
		excluidoFld.setToolTipText("Valor excento de impuesto");
		gravadoFld
				.setToolTipText("Valor gravado con impuesto. Base (antes de impuesto)");
		impuestoCbx.setToolTipText("Impuesto a facturar");
		totalFdl
				.setToolTipText("Total facturado. (Excento + Gravado + Impuesto), impuesto calculado en base al valor gravado");
		contadoRBtn
				.setToolTipText("Seleccione si la facturacion fue de contado");
		creditoRBtn
				.setToolTipText("Seleccione si la facturacion fua a credito");
		anuladaCBox.setToolTipText("Seleccione si la factura fue anulada");
		seguirAgregandoCBox
				.setToolTipText("Seleccione si quiere continuar agregando");
		
		fechaFld.getCalendarButton().setMnemonic(KeyEvent.VK_F);
		aceptarBtn.setMnemonic(KeyEvent.VK_A);
		cancelarBtn.setMnemonic(KeyEvent.VK_C);
		contadoRBtn.setMnemonic(KeyEvent.VK_O);
		creditoRBtn.setMnemonic(KeyEvent.VK_E);
		anuladaCBox.setMnemonic(KeyEvent.VK_N);
		seguirAgregandoCBox.setMnemonic(KeyEvent.VK_G);
	}

	/**
	 * Metodo que valida que los datos del modulo esten bien ingresados
	 * <p>
	 * Creation date 30/03/2006 - 10:59:44 PM
	 * 
	 * @param isUpdate
	 *            True si se estan actualizando los datos, false de lo contrario
	 * @return True, si la informacion es correcta, false de lo contrario
	 * @since 1.0
	 */
	protected boolean isValidData(boolean isUpdate) {
		// Validamos el numero de factura
		if (!isUpdate && StoreCore.isRegisteredData("facturacion_manual", "Factura",
				facturaFld.getText())) {
			JOptionPane.showMessageDialog(this,
					"El numero de factura se encuentra registrado");
			facturaFld.requestFocus();
			return false;
		}
		// Validamos el cliente
		String cliente = clienteCbx.getSelectedItem().toString();
		if (!anuladaCBox.isSelected()
				&& !StoreCore.isRegisteredData("clientes", "Cliente", cliente)) {
			int choice = JOptionPane
					.showConfirmDialog(this,
							"El cliente que ingreso no existe.\nDesea ingresarlo al sistema?");
			if (choice == JOptionPane.YES_OPTION) {
				// Ingresa el cliente al sistema
				new AddCustomer(cliente);
			}
			if (choice == JOptionPane.CANCEL_OPTION) {
				// Retorna a cambiar el cliente
				clienteCbx.requestFocus();
				clienteCbx.getEditor().selectAll();
				return false;
			}
		}

		try {
			double total = totalFdl.getDouble();
			if (!anuladaCBox.isSelected()) {
				// Validamos que el total sea mayor a cero
				if (total == 0) {
					excluidoFld.requestFocus();
					return false;
				}
				// Validamos el credito
				if (creditoRBtn.isSelected()) {
					ArrayList<String> datosCliente = StoreCore.getAllRowData(
							"clientes", "Cliente", cliente);
					// Clientes sin registrar
					if (datosCliente.size() == 0) {
						JOptionPane
								.showMessageDialog(
										this,
										"Los clientes sin registrar no tienen acceso a credito.\nIntente registrarlo primero en el sistema.");
						return false;
					}
					// Verificamos que el saldo sea menor que el valor maximo
					// del credito
					if (datosCliente.get(8) != null) {
						String credito = datosCliente.get(8);
						double maximo = Double
								.parseDouble(datosCliente.get(10));
						double saldo = total - Double.parseDouble(credito);
						if (saldo > maximo) {
							JOptionPane
									.showMessageDialog(this,
											"El credito sobrepasa el valor maximo permitido");
							return false;
						}
					} else {
						JOptionPane.showMessageDialog(this,
								"El cliente no tiene acceso a credito");
						return false;
					}
				}
			}
		} catch (ParseException e) {
			return false;
		}

		return true;
	}

	/**
	 * Retorna los datos del formulario para agregarlo a la base de datos
	 * <p>
	 * Creation date 30/03/2006 - 11:00:21 PM
	 * 
	 * @return Datos listos para agregar a la base de datos
	 * @since 1.0
	 */
	protected ArrayList<String> getData() {
		String factura = facturaFld.getText();
		String fecha = DateUtils.CalendarToSQL(fechaFld.getCalendar());
		String cliente = clienteCbx.getSelectedItem().toString();
		String tipo = null;
		double excluido = 0;
		double gravado = 0;
		double tax = 0;
		double total = 0;

		try {
			excluido = excluidoFld.getDouble();
			gravado = gravadoFld.getDouble();
			total = totalFdl.getDouble();
			tax = StoreMath.subtract(total, StoreMath.add(excluido, gravado, true), true);
		} catch (ParseException e) {
			// No deberia pasar
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		String cancelDate = null;
		String cancelValue = null;

		if (anuladaCBox.isSelected()) {
			cliente = "ANULADA";
			excluido = 0;
			gravado = 0;
			tax = 0;
			total = 0;
		} else {
			if (contadoRBtn.isEnabled() && contadoRBtn.isSelected()) {
				tipo = "Contado";
				cancelDate = DateUtils.CalendarToSQL(fechaFld.getCalendar());
				try {
					cancelValue = String.valueOf(totalFdl.getDouble());
				} catch (ParseException e) {
					cancelValue = null;
				}
			} else {
				tipo = "Credito";
			}
		}

		ArrayList<String> data = new ArrayList<String>();
		data.add(factura);
		data.add(fecha);
		data.add(cliente);
		data.add(tipo);
		data.add(String.valueOf(excluido));
		data.add(String.valueOf(gravado));
		data.add(String.valueOf(tax));
		data.add(String.valueOf(total));
		data.add(cancelDate);
		data.add(cancelValue);
		data.add(StoreSession.getUserName());
		data.add("NOW()");

		return data;
	}
}
