package com.nova.modules.stock.log;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.dat.loader.ImageLoader;
import com.nova.dat.parameter.GlobalConstants;
import com.nova.gui.StoreSession;
import com.nova.log.input.special.AutoCompleteJComboBox;
import com.nova.log.input.validator.BarCodeValidatorField;
import com.nova.log.input.validator.MoneyValidatorField;
import com.nova.log.input.validator.NumberValidatorField;
import com.nova.log.input.validator.PercentValidatorField;
import com.nova.log.input.validator.TextValidatorField;
import com.nova.log.math.StoreMath;
import com.nova.modules.provider.log.AddProvider;

/**
 * Esta abstraccion provee los metodos para el manejo de los elementos del
 * inventario
 * 
 * @author Camilo Nova
 * @version 1.0
 */
abstract class StockDataGUI extends JDialog {

	JLabel imagenLbl;

	JButton examinarBtn;

	JButton removerBtn;

	ButtonGroup tipoGrp;

	JRadioButton productRbtn;

	JRadioButton serviceRbtn;

	JCheckBox continuarAgregandoChbx;

	BarCodeValidatorField barCodeFld;

	TextValidatorField descriptionFdl;

	AutoCompleteJComboBox brandCbx;

	AutoCompleteJComboBox providerCbx;

	TextValidatorField itemPlaceFld;

	AutoCompleteJComboBox packingCbx;

	AutoCompleteJComboBox groupCbx;

	NumberValidatorField quantityFld;

	NumberValidatorField minimumFld;

	MoneyValidatorField costFld;

	PercentValidatorField utilidadFld;

	JComboBox taxCbx;

	MoneyValidatorField sellPriceFld;

	String ProductID;

	String imageUrl;

	JButton limpiarBtn;

	protected JButton aceptarBtn;

	protected JButton cancelarBtn;

	/**
	 * Construye la ventana donde se muestra el formulario de entrada de datos
	 * utilizando varios paneles y dividendolos para ubicarlos correctamente.
	 * <p>
	 * Creation date 13/04/2006 - 08:24:08 AM
	 * 
	 * @since 1.0
	 */
	public StockDataGUI() {
		// Creamos los panel que vamos a utilizar
		JPanel imgButtonsPnl = new JPanel();
		JPanel imagePanel = new JPanel();
		JPanel typePanel = new JPanel();
		JPanel continuePanel = new JPanel();
		JPanel descriptionPanel = new JPanel();
		JPanel othersPanel = new JPanel();
		JPanel amountPanel = new JPanel();
		JPanel valuePanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		JPanel leftPnl = new JPanel(new FlowLayout());
		JPanel rightPnl = new JPanel(new FlowLayout());

		// Instanciamos los botones
		limpiarBtn = new JButton("Limpiar");
		aceptarBtn = new JButton("Aceptar");
		cancelarBtn = new JButton("Cancelar");

		// Instanciamos los demas componentes
		imagenLbl = new JLabel(ImageLoader.getInstance().getImage(
				"no-image.jpg"));
		examinarBtn = new JButton("Examinar");
		removerBtn = new JButton("Remover");
		tipoGrp = new ButtonGroup();
		productRbtn = new JRadioButton("Producto", true);
		serviceRbtn = new JRadioButton("Servicio", false);
		continuarAgregandoChbx = new JCheckBox("Continuar Agregando", false);
		barCodeFld = new BarCodeValidatorField(aceptarBtn, cancelarBtn);
		descriptionFdl = new TextValidatorField(aceptarBtn, cancelarBtn);
		brandCbx = new AutoCompleteJComboBox(StoreCore.getAllColumnData(
				"marcas", "Marca"));
		providerCbx = new AutoCompleteJComboBox(StoreCore.getAllColumnData(
				"proveedores", "Proveedor"));
		itemPlaceFld = new TextValidatorField(aceptarBtn, cancelarBtn);
		packingCbx = new AutoCompleteJComboBox(StoreCore.getAllColumnData(
				"empaques", "Empaque"));
		groupCbx = new AutoCompleteJComboBox(StoreCore.getAllColumnData(
				"categorias", "Categoria"));
		quantityFld = new NumberValidatorField(12, aceptarBtn, cancelarBtn);
		minimumFld = new NumberValidatorField(12, aceptarBtn, cancelarBtn);
		costFld = new MoneyValidatorField(aceptarBtn, cancelarBtn);
		utilidadFld = new PercentValidatorField(aceptarBtn, cancelarBtn);
		taxCbx = new JComboBox(StoreCore.getAllColumnData("impuestos",
				"Impuesto").toArray());
		sellPriceFld = new MoneyValidatorField(aceptarBtn, cancelarBtn);
		ProductID = new String();
		imageUrl = new String();

		// Grupos de botones
		tipoGrp.add(productRbtn);
		tipoGrp.add(serviceRbtn);

		// Componentes que reciben solo mayusculas
		groupCbx.convertToUpperCase();
		packingCbx.convertToUpperCase();

		// Bordes de los paneles
		imagePanel.setBorder(BorderFactory
				.createTitledBorder("Imagen del articulo"));
		typePanel.setBorder(BorderFactory
				.createTitledBorder("Tipo de articulo"));
		continuePanel.setBorder(BorderFactory.createTitledBorder("Luego..."));
		descriptionPanel.setBorder(BorderFactory
				.createTitledBorder("Datos basicos"));
		othersPanel.setBorder(BorderFactory.createTitledBorder("Varios"));
		amountPanel.setBorder(BorderFactory.createTitledBorder("Cantidades"));
		valuePanel.setBorder(BorderFactory.createTitledBorder("Valores"));

		// Tamaños de los componentes
		imagenLbl.setPreferredSize(new Dimension(120, 120));
		barCodeFld.setPreferredSize(new Dimension(130, 20));
		brandCbx.setPreferredSize(new Dimension(130, 20));
		descriptionFdl.setPreferredSize(new Dimension(280, 20));
		providerCbx.setPreferredSize(new Dimension(280, 20));
		itemPlaceFld.setPreferredSize(new Dimension(280, 20));
		groupCbx.setPreferredSize(new Dimension(280, 20));
		packingCbx.setPreferredSize(new Dimension(120, 20));
		quantityFld.setPreferredSize(new Dimension(90, 20));
		minimumFld.setPreferredSize(new Dimension(90, 20));
		costFld.setPreferredSize(new Dimension(120, 20));
		utilidadFld.setPreferredSize(new Dimension(120, 20));
		taxCbx.setPreferredSize(new Dimension(120, 20));
		sellPriceFld.setPreferredSize(new Dimension(110, 20));

		// Tamaños de los paneles
		imagePanel.setPreferredSize(new Dimension(210, 190));
		typePanel.setPreferredSize(new Dimension(210, 60));
		continuePanel.setPreferredSize(new Dimension(210, 60));
		descriptionPanel.setPreferredSize(new Dimension(380, 130));
		othersPanel.setPreferredSize(new Dimension(380, 60));
		amountPanel.setPreferredSize(new Dimension(380, 80));
		valuePanel.setPreferredSize(new Dimension(380, 80));
		buttonsPanel.setPreferredSize(new Dimension(650, 40));
		leftPnl.setPreferredSize(new Dimension(250, 330));
		rightPnl.setPreferredSize(new Dimension(400, 370));

		// Botones del panel de imagen
		imgButtonsPnl.add(examinarBtn);
		imgButtonsPnl.add(removerBtn);

		// Listeners
		examinarBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(imageUrl);
				if (chooser.showOpenDialog(examinarBtn) == JFileChooser.APPROVE_OPTION) {
					imageUrl = chooser.getSelectedFile().toURI().getPath();
					imagenLbl.setIcon(new ImageIcon(imageUrl));
				}
			}
		});
		removerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageUrl = new String();
				imagenLbl.setIcon(ImageLoader.getInstance().getImage(
						"no-image.jpg"));
			}
		});
		productRbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagenLbl.setEnabled(true);
				examinarBtn.setEnabled(true);
				removerBtn.setEnabled(true);
				quantityFld.setEnabled(true);
				minimumFld.setEnabled(true);
				brandCbx.setEnabled(true);
				packingCbx.setEnabled(true);
				providerCbx.setEnabled(true);
				groupCbx.setEnabled(true);
				groupCbx.getEditor().setItem("");
			}
		});
		serviceRbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imagenLbl.setEnabled(false);
				examinarBtn.setEnabled(false);
				removerBtn.setEnabled(false);
				quantityFld.setEnabled(false);
				minimumFld.setEnabled(false);
				brandCbx.setEnabled(false);
				packingCbx.setEnabled(false);
				providerCbx.setEnabled(false);
				groupCbx.setEnabled(false);
				groupCbx.getEditor().setItem("SERVICIOS");
			}
		});
		costFld.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				double percent = 0;
				double base = 0;
				try {
					percent = utilidadFld.getDouble();
					base = Double.parseDouble(costFld.getText());
				} catch (Exception e1) {
					// Nothing
				}
				if (base > 0 && percent > 0) {
					sellPriceFld.setText(StoreMath.parseDouble(StoreMath
							.multiply(base, percent, false)));
				}
				super.keyReleased(e);
			}
		});
		utilidadFld.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				double percent = 0;
				double base = 0;
				try {
					percent = Double.parseDouble(utilidadFld.getText());
					base = costFld.getDouble();
				} catch (Exception e1) {
					// Nothing
				}
				if (base > 0 && percent > 0) {
					sellPriceFld.setText(StoreMath.parseDouble(StoreMath
							.multiply(base, percent, false)));
				}
				super.keyReleased(e);
			}
		});
		sellPriceFld.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				double cost = 0;
				double sell = 0;
				try {
					cost = costFld.getDouble();
					sell = Double.parseDouble(sellPriceFld.getText());
				} catch (Exception e1) {
					// Nothing
				}
				if (cost > 0 && sell > 0) {
					utilidadFld.setDouble(StoreMath.divide(sell, cost, false));
				}
				super.keyReleased(e);
			}
		});
		limpiarBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				cleanGUI();
			}

		});
		cancelarBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}

		});

		// Adicionar componentes a los paneles
		imagePanel.add(imagenLbl, BorderLayout.CENTER);
		imagePanel.add(imgButtonsPnl, BorderLayout.SOUTH);

		typePanel.add(productRbtn);
		typePanel.add(serviceRbtn);

		continuePanel.add(continuarAgregandoChbx);

		descriptionPanel.add(new JLabel("Codigo"));
		descriptionPanel.add(barCodeFld);
		descriptionPanel.add(new JLabel("Marca"));
		descriptionPanel.add(brandCbx);
		descriptionPanel.add(new JLabel("Descripcion"));
		descriptionPanel.add(descriptionFdl);
		descriptionPanel.add(new JLabel("Proveedor"));
		descriptionPanel.add(providerCbx);
		descriptionPanel.add(new JLabel("Ubicacion"));
		descriptionPanel.add(itemPlaceFld);

		othersPanel.add(new JLabel("Empaque"));
		othersPanel.add(packingCbx);
		othersPanel.add(Box.createHorizontalStrut(150));

		amountPanel.add(new JLabel("Categoria"));
		amountPanel.add(groupCbx);
		amountPanel.add(new JLabel("Cantidad "));
		amountPanel.add(quantityFld);
		amountPanel.add(new JLabel("Cantidad Minima"));
		amountPanel.add(minimumFld);

		valuePanel.add(new JLabel("Costo $"));
		valuePanel.add(costFld);
		valuePanel.add(new JLabel("Utilidad %"));
		valuePanel.add(utilidadFld);
		valuePanel.add(new JLabel("Impuesto"));
		valuePanel.add(taxCbx);
		valuePanel.add(new JLabel("Precio $"));
		valuePanel.add(sellPriceFld);

		buttonsPanel.add(limpiarBtn);
		buttonsPanel.add(aceptarBtn);
		buttonsPanel.add(cancelarBtn);

		// Adicionar paneles
		leftPnl.add(imagePanel);
		leftPnl.add(typePanel);
		leftPnl.add(continuePanel);

		rightPnl.add(descriptionPanel);
		rightPnl.add(othersPanel);
		rightPnl.add(amountPanel);
		rightPnl.add(valuePanel);

		add(leftPnl, BorderLayout.WEST);
		add(rightPnl, BorderLayout.CENTER);
		add(buttonsPanel, BorderLayout.SOUTH);

		pack();
		cleanGUI();
		setComponentsTooltip();
		setLocation((GlobalConstants.SCREEN_SIZE_WIDTH - getWidth()) / 2,
				(GlobalConstants.SCREEN_SIZE_HEIGHT - getHeight()) / 2);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setAlwaysOnTop(true);
		setResizable(false);
	}

	/**
	 * Determina los tooltips de los componentes del formulario
	 * <p>
	 * Creation date 12/04/2006 - 09:10:43 AM
	 * 
	 * @since 1.0
	 */
	private void setComponentsTooltip() {
		imagenLbl.setToolTipText("Imagen del articulo");
		examinarBtn.setToolTipText("Buscar una imagen para el articulo");
		removerBtn.setToolTipText("Quitar la imagen actual del articulo");
		productRbtn.setToolTipText("Seleccione si el articulo es un producto");
		serviceRbtn.setToolTipText("Seleccione se el articulo es un servicio");
		continuarAgregandoChbx
				.setToolTipText("Seleccione si desea seguir agregando articulos");
		barCodeFld
				.setToolTipText("Codigo de barras del articulo o codigo que lo identifique");
		brandCbx.setToolTipText("Marca del articulo");
		descriptionFdl
				.setToolTipText("Nombre del articulo o servicio, puede ser igual a otro pero con diferente marca");
		providerCbx.setToolTipText("Proveedor del articulo");
		itemPlaceFld
				.setToolTipText("Lugar fisico dentro del almacen donde se encuentra ubicado");
		groupCbx.setToolTipText("Categoria en la que se encuentra el articulo");
		packingCbx.setToolTipText("Presentacion del articulo");
		quantityFld.setToolTipText("Cantidad en inventario fisico");
		minimumFld
				.setToolTipText("Cantidad minima que debe presentar el articulo");
		costFld.setToolTipText("Costo Final del articulo");
		utilidadFld.setToolTipText("Utilidad en base al costo del articulo");
		taxCbx.setToolTipText("Tipo de impuesto que el articulo presenta");
		sellPriceFld
				.setToolTipText("Precio de venta final del articulo. Incluido impuesto");
		limpiarBtn.setToolTipText("Reinicializa el formulario");
		cancelarBtn.setToolTipText("Cerrar la ventana y cancelar");
	}

	/**
	 * Metodo que valida los datos en los componentes del formulario,
	 * asegurandose que sean congruentes para ser ingresados al sistema.
	 * <p>
	 * Creation date 13/04/2006 - 08:19:10 AM
	 * 
	 * @param isUpdate
	 *            True, si esta actualizando un producto, False de lo contrario
	 * @return True si los datos son validos, False de lo contrario
	 * @since 1.0
	 */
	protected boolean isValidData(boolean isUpdate) {
		// Validamos que el codigo no se encuentre asignado
		if (StoreCore.isRegisteredData("inventario", "Codigo", barCodeFld
				.getText())
				&& !isUpdate) {
			JOptionPane.showMessageDialog(this,
					"El codigo ya esta asignado, intente otro.");
			barCodeFld.requestFocus();
			return false;
		}
		// Validamos la marca
		if (productRbtn.isSelected()
				&& ((String) brandCbx.getSelectedItem()).length() < 2) {
			JOptionPane
					.showMessageDialog(this, "La marca debe ser mas precisa");
			brandCbx.requestFocus();
			return false;
		}
		if (productRbtn.isSelected()
				&& !StoreCore.isRegisteredData("marcas", "Marca",
						(String) brandCbx.getSelectedItem())) {
			int choice = JOptionPane
					.showConfirmDialog(
							this,
							"La marca no esta registrada, para continuar debe estarlo. \nDesea registrarla?",
							"Marca no encontrada", JOptionPane.YES_NO_OPTION);
			if (choice == JOptionPane.YES_OPTION) {
				try {
					ArrayList<String> arrayList = new ArrayList<String>();
					arrayList.add((String) brandCbx.getSelectedItem());
					StoreCore.newData("marcas", arrayList);
				} catch (SQLException e) {
					ErrorLogLoader.addErrorEntry(e);
					e.printStackTrace();
					return false;
				}
			} else {
				brandCbx.requestFocus();
				return false;
			}
		}
		// Validamos la longitud de la descripcion
		if (descriptionFdl.getText().length() < 8) {
			JOptionPane.showMessageDialog(this,
					"Debe utilizar una buena descripcion del articulo");
			descriptionFdl.requestFocus();
			return false;
		}
		// Validamos que la misma marca y descripcion no existan en otro
		// articulo, si es servicio que no exista
		String marca = (String) brandCbx.getSelectedItem();
		String descripcion = descriptionFdl.getText();
		if (serviceRbtn.isSelected()) {
			if (isUpdate) {
				String code = StoreCore.getDataAt("inventario", "Description",
						descripcion, "Codigo");
				if (!code.equals(barCodeFld.getText())) {
					JOptionPane.showMessageDialog(this, "El servicio "
							+ descripcion + " ya se encuentra registrado.");
					descriptionFdl.requestFocus();
					return false;
				}
			} else if (StoreCore.isRegisteredData("inventario", "Descripcion",
					descripcion)) {
				JOptionPane.showMessageDialog(this, "El servicio "
						+ descripcion + " ya se encuentra registrado.");
				descriptionFdl.requestFocus();
				return false;
			}
		}
		if (StoreCore.isStockRegistered(descripcion, marca) && !isUpdate) {
			// Se ejecuta si es producto y ya se encuentra registrado
			JOptionPane.showMessageDialog(this, "Otro articulo"
					+ " tiene la misma descripcion y marca.");
			descriptionFdl.requestFocus();
			return false;
		}
		// Validamos el proveedor
		if (!StoreCore.isRegisteredData("proveedores", "Proveedor",
				(String) providerCbx.getSelectedItem())
				&& productRbtn.isSelected()) {
			int choice = JOptionPane
					.showConfirmDialog(
							this,
							"El proveedor que ingreso no se encuentra registrado. \nDesea registrarlo?",
							"Proveedor no registrado",
							JOptionPane.YES_NO_OPTION);
			if (choice == JOptionPane.YES_OPTION) {
				new AddProvider((String) providerCbx.getSelectedItem());
			} else {
				providerCbx.requestFocus();
				return false;
			}
		}
		// Validamos la categoria
		if (productRbtn.isSelected()
				&& ((String) groupCbx.getSelectedItem()).length() < 2) {
			JOptionPane
					.showMessageDialog(this, "La categoria debe ser mas precisa");
			groupCbx.requestFocus();
			return false;
		}
		if (!StoreCore.isRegisteredData("categorias", "Categoria",
				(String) groupCbx.getSelectedItem())) {
			int choice = JOptionPane
					.showConfirmDialog(
							this,
							"La categoria que ingreso no se encuentra registrada. \nDesea registrarla?",
							"Categoria no registrada",
							JOptionPane.YES_NO_OPTION);
			if (choice == JOptionPane.YES_OPTION) {
				try {
					ArrayList<String> arrayList = new ArrayList<String>();
					arrayList.add((String) groupCbx.getSelectedItem());
					StoreCore.newData("categorias", arrayList);
				} catch (SQLException e) {
					ErrorLogLoader.addErrorEntry(e);
					e.printStackTrace();
					return false;
				}
			} else {
				groupCbx.requestFocus();
				return false;
			}
		}
		// Validamos el empaque
		if (productRbtn.isSelected()
				&& ((String) packingCbx.getSelectedItem()).length() < 2) {
			JOptionPane
					.showMessageDialog(this, "El tipo de empaque debe ser mas preciso");
			packingCbx.requestFocus();
			return false;
		}
		if (productRbtn.isSelected() && !StoreCore.isRegisteredData("empaques", "Empaque",
				(String) packingCbx.getSelectedItem())) {
			int choice = JOptionPane
					.showConfirmDialog(
							this,
							"El tipo de empaque que ingreso no se encuentra registrado. \nDesea registrarlo?",
							"Empaque no registrado", JOptionPane.YES_NO_OPTION);
			if (choice == JOptionPane.YES_OPTION) {
				try {
					ArrayList<String> arrayList = new ArrayList<String>();
					arrayList.add((String) packingCbx.getSelectedItem());
					StoreCore.newData("empaques", arrayList);
				} catch (SQLException e) {
					ErrorLogLoader.addErrorEntry(e);
					e.printStackTrace();
					return false;
				}
			} else {
				packingCbx.requestFocus();
				return false;
			}
		}
		// Validamos las cantidades
		if (productRbtn.isSelected()) {
			int cantDisp;
			int cantMin;
			try {
				cantDisp = Integer.parseInt(quantityFld.getText());
			} catch (NumberFormatException e) {
				quantityFld.requestFocus();
				return false;
			}
			try {
				cantMin = Integer.parseInt(minimumFld.getText());
			} catch (NumberFormatException e) {
				minimumFld.requestFocus();
				return false;
			}
			if (cantDisp <= cantMin) {
				JOptionPane
						.showMessageDialog(this,
								"La cantidad minima no puede ser menor que la cantidad disponible.");
				minimumFld.requestFocus();
				return false;
			}
		}
		// Validamos que el panel de valores contenga datos
		try {
			costFld.getDouble();
		} catch (ParseException e) {
			costFld.requestFocus();
			return false;
		}
		try {
			utilidadFld.getDouble();
		} catch (ParseException e) {
			utilidadFld.requestFocus();
			return false;
		}
		try {
			sellPriceFld.getDouble();
		} catch (ParseException e) {
			sellPriceFld.requestFocus();
			return false;
		}

		return true;
	}

	/**
	 * Reinicializa todos los componentes.
	 * <p>
	 * Creation date 12/04/2006 - 08:54:24 AM
	 * 
	 * @since 1.0
	 */
	protected void cleanGUI() {
		try {
			barCodeFld.setText(StoreCore.getNextId("inventario", "Codigo"));
			brandCbx.setSelectedItem("");
			descriptionFdl.setText(null);
			providerCbx.setSelectedItem("");
			itemPlaceFld.setText(null);
			groupCbx.setSelectedItem("");
			packingCbx.setSelectedItem("");
			quantityFld.setText(null);
			minimumFld.setText(null);
			costFld.setText(null);
			utilidadFld.setText(null);
			taxCbx.setSelectedItem(StoreCore.getProperty("DEFAULT_TAX"));
			sellPriceFld.setText(null);

			if (productRbtn.isSelected()) {
				productRbtn.doClick();
			} else
				serviceRbtn.doClick();

		} catch (Exception e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
	}

	/**
	 * Retorna los datos listos para agregarlos a la base de datos. <b>Es
	 * necesario que se haga el proceso kardex</b>
	 * <p>
	 * Creation date 12/04/2006 - 12:26:28 PM
	 * 
	 * @return Datos listos para agregar a stock
	 * @since 1.0
	 */
	protected ArrayList<String> getData() {
		double sell = 0;
		double cost = 0;
		try {
			sell = sellPriceFld.getDouble();
			cost = costFld.getDouble();
		} catch (ParseException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}

		String barCode = barCodeFld.getText();
		String group = (String) groupCbx.getSelectedItem();
		String quantity;
		String minimum;
		String description = descriptionFdl.getText();
		String type;
		String brand;
		String packing;
		String tax = StoreCore.getDataAt("impuestos", "Impuesto",
				(String) taxCbx.getSelectedItem(), "Valor");
		String sellPrice = String.valueOf(sell);
		String provider;
		String costPrice = String.valueOf(cost);
		String itemPlace = itemPlaceFld.getText();

		if (productRbtn.isSelected()) {
			quantity = quantityFld.getText();
			minimum = minimumFld.getText();
			type = "Producto";
			brand = (String) brandCbx.getSelectedItem();
			packing = (String) packingCbx.getSelectedItem();
			provider = (String) providerCbx.getSelectedItem();
		} else {
			quantity = "0";
			minimum = "0";
			type = "Servicio";
			brand = null;
			packing = null;
			provider = null;
			imageUrl = null;
		}

		ArrayList<String> data = new ArrayList<String>();
		data.add(barCode);
		data.add(group);
		data.add(quantity);
		data.add(minimum);
		data.add(description);
		data.add(type);
		data.add(brand);
		data.add(packing);
		data.add(tax);
		data.add(sellPrice);
		data.add(provider);
		data.add(costPrice);
		data.add(itemPlace);
		data.add(imageUrl);
		data.add(StoreSession.getUserName());
		data.add("NOW()");

		return data;
	}
}