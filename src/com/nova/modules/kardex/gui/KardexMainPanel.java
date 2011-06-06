package com.nova.modules.kardex.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import com.nova.dat.db.StoreCore;
import com.nova.log.input.special.InputComponent;
import com.nova.log.input.validator.BarCodeValidatorField;
import com.nova.log.input.validator.MoneyValidatorField;
import com.nova.log.input.validator.NumberValidatorField;
import com.nova.modules.kardex.dat.KardexTableModel;
import com.nova.modules.stock.log.StockItem;

/**
 * Crea el panel que contiene el panel de informacion y el panel donde se
 * muestra la tabla con la informacion del producto seleccionado.
 * <p>
 * Se ha optimizado la actualizacion de los datos en la tabla con el uso de un
 * hilo
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class KardexMainPanel extends JPanel implements Runnable {

	JLabel codigoLbl;

	BarCodeValidatorField codigoFld;

	JLabel descripcionLbl;

	InputComponent descripcionFld;

	JLabel unidadesLbl;

	NumberValidatorField unidadesFld;

	JLabel costoLbl;

	MoneyValidatorField costoFld;

	JLabel precioLbl;

	MoneyValidatorField precioFld;

	KardexTableModel model;

	Thread thread;

	String selectedBarCode;

	private final KardexBuilder builder;

	private String stockItemCost;

	private String stockItemQuantity;

	/**
	 * Agrega el panel de busqueda y la tabla donde se muestran los procesos del
	 * producto.
	 * <p>
	 * Creation date 19/05/2006 - 10:06:18 AM
	 * 
	 * @param builder
	 *            Modulo constructor
	 * 
	 * @since 1.0
	 */
	public KardexMainPanel(KardexBuilder builder) {
		this.builder = builder;
		thread = new Thread(this);
		add(getSearchPanel(), BorderLayout.NORTH);
		add(getTablePanel(), BorderLayout.CENTER);
	}

	/**
	 * Retorna el panel que muestra los campos de busqueda
	 * <p>
	 * Creation date 25/05/2006 - 06:12:21 PM
	 * 
	 * @return Panel que muestra los campos de busqueda
	 * @since 1.0
	 */
	private JPanel getSearchPanel() {
		JPanel searchPnl = new JPanel();
		codigoLbl = new JLabel("Codigo");
		codigoFld = new BarCodeValidatorField();
		descripcionLbl = new JLabel("Descripcion");
		descripcionFld = new InputComponent(StoreCore.getStockAllItems());
		unidadesLbl = new JLabel("Unidades Actuales");
		unidadesFld = new NumberValidatorField();
		costoLbl = new JLabel("Costo Actual");
		costoFld = new MoneyValidatorField();
		precioLbl = new JLabel("Precio Actual");
		precioFld = new MoneyValidatorField();

		codigoFld.setPreferredSize(new Dimension(120, 20));
		descripcionFld.setPreferredSize(new Dimension(300, 20));
		unidadesFld.setPreferredSize(new Dimension(40, 20));
		costoFld.setPreferredSize(new Dimension(100, 20));
		precioFld.setPreferredSize(new Dimension(100, 20));
		searchPnl.setPreferredSize(new Dimension(590, 50));

		unidadesFld.setEnabled(false);
		costoFld.setEnabled(false);
		precioFld.setEnabled(false);

		codigoFld.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				selectedBarCode = codigoFld.getText();
				thread.run();
			}

		});
		descripcionFld.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				selectedBarCode = StoreCore.getStockBarCode(descripcionFld
						.getText());
				thread.run();
			}
		});
		descripcionFld.setNoItemAction(new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
				Toolkit.getDefaultToolkit().beep();
			}
		
		});

		searchPnl.add(codigoLbl);
		searchPnl.add(codigoFld);
		searchPnl.add(descripcionLbl);
		searchPnl.add(descripcionFld);
		searchPnl.add(unidadesLbl);
		searchPnl.add(unidadesFld);
		searchPnl.add(costoLbl);
		searchPnl.add(costoFld);
		searchPnl.add(precioLbl);
		searchPnl.add(precioFld);

		return searchPnl;
	}

	/**
	 * Retorna el panel que muestra la tabla.
	 * <p>
	 * Creation date 25/05/2006 - 06:11:58 PM
	 * 
	 * @return Panel que muestra la tabla
	 * @since 1.0
	 */
	private JScrollPane getTablePanel() {
		model = new KardexTableModel();
		JTable table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);

		TableColumn tableColumn;
		DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
		cellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

		tableColumn = table.getColumn("Operacion");
		tableColumn.setPreferredWidth(200);

		tableColumn = table.getColumn("Unidades");
		tableColumn.setCellRenderer(cellRenderer);

		tableColumn = table.getColumn("Costo");
		tableColumn.setCellRenderer(cellRenderer);

		tableColumn = table.getColumn("Total");
		tableColumn.setCellRenderer(cellRenderer);

		tableColumn = table.getColumn("SaldoUnidades");
		tableColumn.setCellRenderer(cellRenderer);

		tableColumn = table.getColumn("SaldoCosto");
		tableColumn.setCellRenderer(cellRenderer);

		tableColumn = table.getColumn("SaldoTotal");
		tableColumn.setCellRenderer(cellRenderer);

		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPane.setPreferredSize(new Dimension(590, 250));

		return scrollPane;
	}

	public void run() {
		try {
			StockItem stockItem = new StockItem(selectedBarCode,
					StockItem.BARCODE);
			if (stockItem.getType().equals("Servicio"))
				throw new Exception();

			String stockDescriptionAndBrand = StoreCore
					.getStockDescriptionAndBrand(selectedBarCode);
			stockItemCost = stockItem.getCost();
			stockItemQuantity = stockItem.getQuantity();

			codigoFld.setText(selectedBarCode);
			descripcionFld.setText(stockDescriptionAndBrand);
			unidadesFld.setText(stockItemQuantity);
			costoFld.setDouble(Double.parseDouble(stockItemCost));
			precioFld.setDouble(Double.parseDouble(stockItem.getSellPrice()));

			model.updateTableData(selectedBarCode);
			builder.setToolbarEnabled(true);
			builder.setStatusBarText("Procesos del kardex de '"
					+ stockDescriptionAndBrand + "' cargados.");
		} catch (Exception e) {
			// El codigo ingresado no existe
			selectedBarCode = null;
			descripcionFld.setText(selectedBarCode);
			unidadesFld.setText(selectedBarCode);
			costoFld.setText(selectedBarCode);
			precioFld.setText(selectedBarCode);

			model.updateTableData(selectedBarCode);
			builder.setToolbarEnabled(false);
			builder.setStatusBarText("El codigo ingresado no existe!!!");

			codigoFld.requestFocus();
			codigoFld.selectAll();
		}
	}

	/**
	 * Retorna el costo del producto mostrado en la tabla
	 * <p>
	 * Creation date 25/05/2006 - 06:32:37 PM
	 * 
	 * @return Costo del producto mostrado actualmente
	 * @since 1.0
	 */
	public String getStockItemCost() {
		return stockItemCost;
	}

	/**
	 * Retorna la cantidad disponible de articulos del producto
	 * <p>
	 * Creation date 25/05/2006 - 06:43:38 PM
	 * 
	 * @return Cantidad de articulos del producto
	 * @since 1.0
	 */
	public String getStockItemQuantity() {
		return stockItemQuantity;
	}

	/**
	 * Retorna el codigo de barras del producto mostrado en la tabla
	 * <p>
	 * Creation date 25/05/2006 - 06:54:00 PM
	 * 
	 * @return Codigo del producto
	 * @since 1.0
	 */
	public String getSelectedBarCode() {
		return selectedBarCode;
	}

}
