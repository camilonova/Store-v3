package com.nova.modules.utilities.tax;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.sql.rowset.WebRowSet;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.dat.loader.ImageLoader;
import com.nova.dat.parameter.GlobalConstants;
import com.nova.gui.shared.table.StoreTableRemoveRow;
import com.nova.log.input.validator.PercentValidatorField;
import com.nova.log.input.validator.TextValidatorField;
import com.nova.modules.ModuleBuilder;

/**
 * Esta clase brinda la interfaz grafica para la manipulacion de los impuestos
 * de la aplicacion
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class TaxBuilder extends JPanel implements ModuleBuilder {

	TaxTablePanel mainPanel;
	private final ImageLoader imageLoader;

	/**
	 * Crea la interfaz grafica y agrega las funcionalidades correspondientes
	 * <p>
	 * Creation date 2/05/2006 - 04:37:05 PM
	 * 
	 * @since 1.0
	 */
	public TaxBuilder() {
		mainPanel = new TaxTablePanel(this);
		imageLoader = ImageLoader.getInstance();
		
		JPanel buttonsPnl = new JPanel();
		JButton addBtn = new JButton("Agregar", imageLoader
				.getImage("gold_new.png"));
		JButton editBtn = new JButton("Modificar", imageLoader
				.getImage("gold_add.png"));
		JButton delBtn = new JButton("Eliminar", imageLoader
				.getImage("gold_del.png"));

		buttonsPnl.setPreferredSize(new Dimension(120, 120));

		addBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				final String title = "Agregar nuevo impuesto";
				final JButton aceptarBtn = new JButton("Aceptar");
				final JButton cancelarBtn = new JButton("Cancelar");
				final PercentValidatorField percentField = new PercentValidatorField(
						2, aceptarBtn, cancelarBtn);
				final TextValidatorField textField = new TextValidatorField(10,
						percentField, cancelarBtn);

				aceptarBtn.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e1) {
						try {
							ArrayList<String> data = new ArrayList<String>();
							data.add(textField.getText());
							data.add(String.valueOf(percentField.getDouble()));
							StoreCore.newData(getRelatedTableName(), data);
							mainPanel.updateData("Impuesto Agregado");
							cancelarBtn.doClick();
						} catch (Exception e2) {
							mainPanel.updateData("Error. Verifique los datos");
							ErrorLogLoader.addErrorEntry(e2);
							e2.printStackTrace();
						}
					}
				});

				textField.setPreferredSize(new Dimension(80, 20));
				percentField.setPreferredSize(new Dimension(40, 20));

				showDataDialog(title, textField, percentField, aceptarBtn,
						cancelarBtn);
			}

		});
		editBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				final int row = mainPanel.getSelectedRow() + 1;
				if (row == 0) {
					JOptionPane.showMessageDialog(null,
							"Deber seleccionar un impuesto");
					return;
				}

				final WebRowSet tableResultSet = mainPanel.getTableResultSet();
				final String title = "Agregar nuevo impuesto";
				final JButton aceptarBtn = new JButton("Aceptar");
				final JButton cancelarBtn = new JButton("Cancelar");
				final PercentValidatorField percentField = new PercentValidatorField(
						2, aceptarBtn, cancelarBtn);
				final TextValidatorField textField = new TextValidatorField(10,
						percentField, cancelarBtn);

				aceptarBtn.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e1) {
						try {
							ArrayList<String> data = new ArrayList<String>();
							data.add(textField.getText());
							data.add(String.valueOf(percentField.getDouble()));
							StoreCore.removeRow(getRelatedTableName(),
									getOrderColumn(), tableResultSet
											.getString(1));
							StoreCore.newData(getRelatedTableName(), data);
							mainPanel.updateData("Impuesto Actualizado");
							cancelarBtn.doClick();
						} catch (ParseException e2) {
							ErrorLogLoader.addErrorEntry(e2);
							e2.printStackTrace();
						} catch (SQLException e2) {
							ErrorLogLoader.addErrorEntry(e2);
							e2.printStackTrace();
						}
					}
				});

				try {
					textField.setText(tableResultSet.getString(1));
					percentField.setDouble(tableResultSet.getDouble(2));
				} catch (SQLException e2) {
					ErrorLogLoader.addErrorEntry(e2);
					e2.printStackTrace();
				}
				textField.setPreferredSize(new Dimension(80, 20));
				percentField.setPreferredSize(new Dimension(40, 20));

				showDataDialog(title, textField, percentField, aceptarBtn,
						cancelarBtn);
			}

		});
		delBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new StoreTableRemoveRow(mainPanel);
			}

		});

		addBtn.setToolTipText("Adicionar un nuevo impuesto al sistema");
		editBtn.setToolTipText("Modificar el impuesto seleccionado");
		delBtn.setToolTipText("Eliminar el impuesto seleccionado");

		buttonsPnl.add(addBtn);
		buttonsPnl.add(editBtn);
		buttonsPnl.add(delBtn);

		add(mainPanel);
		add(buttonsPnl);
		mainPanel.requestFocus();
	}

	/**
	 * Crea un dialogo para mostrar la informacion del impuesto
	 * 
	 * @param title
	 *            Titulo de la ventana
	 * @param textField
	 *            Campo donde se escribira el nombre
	 * @param percentField
	 *            Valor del impuesto
	 * @param aceptarBtn
	 *            Boton de aceptar
	 * @param cancelarBtn
	 *            Boton de cancelar
	 * @since 1.0
	 */
	void showDataDialog(String title, TextValidatorField textField,
			PercentValidatorField percentField, JButton aceptarBtn,
			JButton cancelarBtn) {
		final JDialog dialog = new JDialog();
		JPanel upPnl = new JPanel();
		JPanel lowPnl = new JPanel();

		aceptarBtn.setToolTipText("Ingresar informacion y salir");
		cancelarBtn.setToolTipText("Cerrar ventana");
		cancelarBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}

		});

		upPnl.add(new JLabel("Impuesto"));
		upPnl.add(textField);
		upPnl.add(new JLabel("Valor"));
		upPnl.add(percentField);

		lowPnl.add(aceptarBtn);
		lowPnl.add(cancelarBtn);

		dialog.add(upPnl, BorderLayout.CENTER);
		dialog.add(lowPnl, BorderLayout.SOUTH);

		dialog.setTitle(title);
		dialog.setSize(250, 100);
		dialog.setLocation(
				(GlobalConstants.SCREEN_SIZE_WIDTH - getWidth()) / 2,
				(GlobalConstants.SCREEN_SIZE_HEIGHT - getHeight()) / 2);
		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		dialog.setModal(true);
		dialog.setAlwaysOnTop(true);
		dialog.setResizable(false);
		dialog.setVisible(true);
	}

	public String getRelatedTableName() {
		return "impuestos";
	}

	public String getOrderColumn() {
		return "Impuesto";
	}

	public String getOrderType() {
		return GlobalConstants.ORDER_ASCENDANT;
	}

	public void setStatusBarText(String message) {
		// Sin implementacion
	}

}
