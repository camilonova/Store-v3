package com.nova.modules.kardex.log;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.nova.dat.loader.ErrorLogLoader;
import com.nova.dat.parameter.GlobalConstants;
import com.nova.log.input.validator.MoneyValidatorField;
import com.nova.log.input.validator.NumberValidatorField;
import com.nova.log.input.validator.TextValidatorField;
import com.nova.modules.kardex.gui.KardexMainPanel;

/**
 * Representa el dialogo de ajuste de las cantidades de los productos del
 * kardex, el cual ofrece la facilidad de ver el saldo de las unidades
 * resultantes
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class AddKardex extends JDialog {

	TextValidatorField operacionFld;

	ButtonGroup tipoGrp;

	JRadioButton entradaRbtn;

	JRadioButton salidaRbtn;

	NumberValidatorField unidadesFld;

	MoneyValidatorField costoFld;

	NumberValidatorField saldoFld;

	private JButton aceptarBtn;

	private JButton cancelarBtn;

	/**
	 * Construye el panel y le da las caracteristicas
	 * <p>
	 * Creation date 25/05/2006 - 06:48:26 PM
	 * 
	 * @param mainPanel
	 *            Panel principal del modulo
	 * 
	 * @since 1.0
	 */
	public AddKardex(final KardexMainPanel mainPanel) {
		aceptarBtn = new JButton("Aceptar");
		cancelarBtn = new JButton("Cancelar");

		operacionFld = new TextValidatorField(aceptarBtn, cancelarBtn);
		tipoGrp = new ButtonGroup();
		entradaRbtn = new JRadioButton("Entrada", true);
		salidaRbtn = new JRadioButton("Salida", false);
		costoFld = new MoneyValidatorField(aceptarBtn, cancelarBtn);
		unidadesFld = new NumberValidatorField(12, aceptarBtn, cancelarBtn);
		saldoFld = new NumberValidatorField();

		operacionFld.setPreferredSize(new Dimension(310, 20));
		unidadesFld.setPreferredSize(new Dimension(100, 20));
		costoFld.setPreferredSize(new Dimension(100, 20));
		saldoFld.setPreferredSize(new Dimension(80, 20));

		saldoFld.setEnabled(false);
		tipoGrp.add(entradaRbtn);
		tipoGrp.add(salidaRbtn);

		JPanel upPanel = new JPanel();
		JPanel midPanel = new JPanel();
		JPanel izqMidPanel = new JPanel();
		JPanel derMidPanel = new JPanel();
		JPanel lowPanel = new JPanel();

		upPanel.setPreferredSize(new Dimension(390, 30));
		upPanel.add(new JLabel("Operacion"));
		upPanel.add(operacionFld);

		izqMidPanel.setPreferredSize(new Dimension(180, 85));
		izqMidPanel.setBorder(BorderFactory.createTitledBorder("Tipo"));
		izqMidPanel.add(entradaRbtn);
		izqMidPanel.add(salidaRbtn);
		izqMidPanel.add(new JLabel("Saldo"));
		izqMidPanel.add(Box.createHorizontalStrut(10));
		izqMidPanel.add(saldoFld);

		derMidPanel.setPreferredSize(new Dimension(200, 85));
		derMidPanel.setBorder(BorderFactory.createTitledBorder("Cantidades"));
		derMidPanel.add(new JLabel("Unidades"));
		derMidPanel.add(unidadesFld);
		derMidPanel.add(new JLabel("Costo"));
		derMidPanel.add(Box.createHorizontalStrut(16));
		derMidPanel.add(costoFld);

		midPanel.setPreferredSize(new Dimension(390, 90));
		midPanel.add(izqMidPanel);
		midPanel.add(derMidPanel);

		lowPanel.add(aceptarBtn);
		lowPanel.add(cancelarBtn);

		add(upPanel, BorderLayout.NORTH);
		add(midPanel, BorderLayout.CENTER);
		add(lowPanel, BorderLayout.SOUTH);

		unidadesFld.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					int units = Integer.parseInt(mainPanel
							.getStockItemQuantity());
					int newUnits = Integer.parseInt(unidadesFld.getText());

					if (entradaRbtn.isSelected())
						saldoFld.setText(String.valueOf(units + newUnits));
					else
						saldoFld.setText(String.valueOf(units - newUnits));
				} catch (NumberFormatException e1) {
					// Caja vacia. No pasa nada
				}

				super.keyReleased(e);
			}
		});
		entradaRbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int units = Integer.parseInt(mainPanel
							.getStockItemQuantity());
					int newUnits = Integer.parseInt(unidadesFld.getText());
					saldoFld.setText(String.valueOf(units + newUnits));
				} catch (NumberFormatException e1) {
					// Caja Vacia, no importa
				}

				costoFld.setText(null);
				costoFld.setEnabled(true);
			}
		});
		salidaRbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int units = Integer.parseInt(mainPanel
							.getStockItemQuantity());
					int newUnits = Integer.parseInt(unidadesFld.getText());
					saldoFld.setText(String.valueOf(units - newUnits));
				} catch (NumberFormatException e1) {
					// Caja Vacia, no importa
				}

				costoFld.setDouble(Double.parseDouble(mainPanel
						.getStockItemCost()));
				costoFld.setEnabled(false);
			}
		});
		aceptarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isValidData()) {
					try {
						ArrayList<String> operationData = new ArrayList<String>();
						operationData.add(mainPanel.getSelectedBarCode());
						operationData.add(operacionFld.getText());
						operationData.add(entradaRbtn.isSelected() ? "E" : "S");
						operationData.add(unidadesFld.getText());
						operationData.add(String.valueOf(costoFld.getDouble()));

						KardexDataManager.insertKardexOperation(operationData);
						mainPanel.run();
						dispose();
					} catch (ParseException e1) {
						// No deberia pasar
						ErrorLogLoader.addErrorEntry(e1);
						e1.printStackTrace();
					}
				}
			}
		});
		cancelarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		setTitle("Agregar Registro");

		pack();
		setComponentsTooltip();
		setLocation((GlobalConstants.SCREEN_SIZE_WIDTH - getWidth()) / 2,
				(GlobalConstants.SCREEN_SIZE_HEIGHT - getHeight()) / 2);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		setModal(true);
		setVisible(true);
	}

	private void setComponentsTooltip() {
		operacionFld
				.setToolTipText("Descripcion de la operacion que quiere ingresar");
		entradaRbtn
				.setToolTipText("Seleccione si va a ingresar articulos al inventario");
		salidaRbtn
				.setToolTipText("Seleccione si va a egresar articulos del inventario");
		saldoFld
				.setToolTipText("Cantidad del producto luego de efectuada la operacion");
		unidadesFld.setToolTipText("Cantidad de unidades a procesar");
		costoFld.setToolTipText("Costo de las unidades a procesar");
		aceptarBtn.setToolTipText("Ingresar operacion");
		cancelarBtn.setToolTipText("Cancelar operacion");
	}

	/**
	 * Valida que los datos sean correctos
	 * <p>
	 * Creation date 25/05/2006 - 07:06:30 PM
	 * 
	 * @return True, si los datos a ingresar son correctos
	 * @since 1.0
	 */
	boolean isValidData() {
		if (operacionFld.getText().length() < 5) {
			JOptionPane.showMessageDialog(this,
					"Debe ingresar una buena descripcion de la operacion");
			operacionFld.requestFocus();
			return false;
		}
		if (unidadesFld.getText().length() == 0) {
			JOptionPane.showMessageDialog(this,
					"Debe ingresar una cantidad de unidades");
			unidadesFld.requestFocus();
			return false;
		}
		if (costoFld.getText().length() == 0) {
			JOptionPane.showMessageDialog(this,
					"Debe ingresar un costo para las unidades");
			costoFld.requestFocus();
			return false;
		}
		if (saldoFld.getText().length() > 0
				&& Integer.parseInt(saldoFld.getText()) < 0) {
			JOptionPane.showMessageDialog(this,
					"Las unidades finales deben ser positivas");
			unidadesFld.requestFocus();
			return false;
		}
		return true;
	}

}
