package com.nova.modules.utilities.city;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.dat.loader.ImageLoader;
import com.nova.dat.parameter.GlobalConstants;
import com.nova.log.input.validator.TextValidatorField;
import com.nova.modules.ModuleBuilder;

/**
 * Esta clase se encarga de proveer los metodos para la manipulacion de las
 * ciudades en la aplicacion.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class CityBuilder extends JPanel implements ModuleBuilder {

	JList groupsList;
	private final ImageLoader imageLoader;

	/**
	 * Constructor, se encarga de crear la interfaz grafica y de determinar los
	 * comandos de crear, editar y borrar.
	 * <p>
	 * Creation date 30/03/2006 - 10:30:27 AM
	 * 
	 * @since 1.0
	 */
	public CityBuilder() {
		imageLoader = ImageLoader.getInstance();
		JPanel leftPnl = new JPanel();
		JPanel rightPnl = new JPanel();

		leftPnl.setPreferredSize(new Dimension(170, 150));
		rightPnl.setPreferredSize(new Dimension(100, 150));

		groupsList = new JList(StoreCore.getAllColumnData(
				getRelatedTableName(), getOrderColumn()).toArray());
		leftPnl.add(new JScrollPane(groupsList));

		JButton newBtn = new JButton("Nuevo", imageLoader
				.getImage("gold_new.png"));
		JButton editBtn = new JButton("Editar", imageLoader
				.getImage("gold_add.png"));
		JButton delBtn = new JButton("Borrar", imageLoader
				.getImage("gold_del.png"));

		newBtn.setToolTipText("Agregar una nueva ciudad");
		editBtn.setToolTipText("Editar la ciudad seleccionada");
		delBtn.setToolTipText("Borrar la ciudad seleccionada");
		groupsList.setToolTipText("Listado de ciudades registradas");

		newBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String title = "Nueva Ciudad";
				final JButton aceptarBtn = new JButton("Aceptar");
				final JButton cancelarBtn = new JButton("Cancelar");
				final TextValidatorField field = new TextValidatorField(20,
						aceptarBtn, cancelarBtn);

				field.setPreferredSize(new Dimension(100, 20));
				aceptarBtn.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e2) {
						try {
							ArrayList<String> data = new ArrayList<String>();
							data.add(field.getText());
							StoreCore.newData(getRelatedTableName(), data);
							updateList();
							cancelarBtn.doClick();
						} catch (SQLException e3) {
							ErrorLogLoader.addErrorEntry(e3);
							e3.printStackTrace();
						}
					}

				});
				showDataDialog(title, field, aceptarBtn, cancelarBtn);
			}

		});
		editBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				final String ciudad = (String) groupsList.getSelectedValue();
				if (ciudad == null) {
					JOptionPane.showMessageDialog(null,
							"Debe seleccionar una ciudad!!!");
					return;
				}

				String title = "Editar Ciudad";
				final JButton aceptarBtn = new JButton("Aceptar");
				final JButton cancelarBtn = new JButton("Cancelar");
				final TextValidatorField field = new TextValidatorField(20,
						aceptarBtn, cancelarBtn);

				aceptarBtn.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e2) {
						try {
							ArrayList<String> data = new ArrayList<String>();
							data.add(field.getText());
							StoreCore.removeRow(getRelatedTableName(),
									getOrderColumn(), ciudad);
							StoreCore.newData(getRelatedTableName(), data);
							updateList();
							cancelarBtn.doClick();
						} catch (SQLException e3) {
							ErrorLogLoader.addErrorEntry(e3);
							e3.printStackTrace();
						}
					}

				});
				field.setPreferredSize(new Dimension(100, 20));
				field.setText(ciudad);

				showDataDialog(title, field, aceptarBtn, cancelarBtn);
			}

		});
		delBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String categoria = (String) groupsList.getSelectedValue();
				if (categoria == null) {
					JOptionPane.showMessageDialog(null,
							"Debe seleccionar una ciudad!!!");
					return;
				}
				StoreCore.removeRow(getRelatedTableName(), getOrderColumn(),
						categoria);
				updateList();
			}

		});

		rightPnl.add(newBtn);
		rightPnl.add(editBtn);
		rightPnl.add(delBtn);

		add(leftPnl);
		add(rightPnl);

	}

	/**
	 * Actualiza la lista de ciudades
	 * 
	 * <p>
	 * Creation date 30/03/2006 - 10:28:25 AM
	 * 
	 * @since 1.0
	 */
	void updateList() {
		groupsList.removeAll();
		groupsList.setListData(StoreCore.getAllColumnData(
				getRelatedTableName(), getOrderColumn()).toArray());
	}

	/**
	 * Crea un dialogo para mostrar la ciudad
	 * 
	 * @param title
	 *            Titulo de la ventana
	 * @param field
	 *            Campo donde se escribira la informacion
	 * @param aceptarBtn
	 *            Boton de aceptar
	 * @param cancelarBtn
	 *            Boton de cancelar
	 * @since 1.0
	 */
	void showDataDialog(String title, TextValidatorField field,
			JButton aceptarBtn, JButton cancelarBtn) {
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

		upPnl.add(new JLabel("Ciudad"));
		upPnl.add(field);

		lowPnl.add(aceptarBtn);
		lowPnl.add(cancelarBtn);

		dialog.add(upPnl, BorderLayout.CENTER);
		dialog.add(lowPnl, BorderLayout.SOUTH);

		dialog.setTitle(title);
		dialog.setSize(200, 100);
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
		return "ciudades";
	}

	public String getOrderColumn() {
		return "Ciudad";
	}

	public String getOrderType() {
		return GlobalConstants.ORDER_ASCENDANT;
	}

	public void setStatusBarText(String message) {
		// Sin implementacion
	}

}
