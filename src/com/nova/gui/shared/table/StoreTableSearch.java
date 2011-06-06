package com.nova.gui.shared.table;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.sql.rowset.WebRowSet;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.nova.dat.loader.ErrorLogLoader;
import com.nova.dat.loader.ImageLoader;
import com.nova.dat.parameter.GlobalConstants;
import com.nova.log.input.validator.TextValidatorField;

/**
 * Esta clase representa la funcionalidad de busqueda de datos en la tabla.
 * Esto se hace creando un hilo que busca los campos que contengan la palabra a 
 * buscar sin diferenciar mayusculas o minusculas
 * @author Camilo Nova
 * @version 1.0
 */
public class StoreTableSearch extends JDialog implements Runnable {
//TODO mejorar el algoritmo, tiene algunos errores y falla al ordenar los datos
//de la tabla, intentar bajar los datos del rowset a String[] e iterar desde el
	JLabel searchLbl;

	TextValidatorField searchFld;

	JButton okBtn;

	JButton cancelBtn;

	JButton searchBtn;

	JButton backBtn;

	JButton nextBtn;

	StoreTablePanel tablePanel;

	ListIterator<Coordinates> iterator;

	private final ImageLoader imageLoader;

	/**
	 * Construye la ventana para busqueda y le agrega las funcionalidades
	 * correspondientes
	 * <p>Creation date 8/06/2006 - 11:01:02 AM
	 * @param tablePanel			Panel donde se encuentra la tabla con los datos
	 *
	 * @since 1.0
	 */
	public StoreTableSearch(StoreTablePanel tablePanel) {
		this.tablePanel = tablePanel;
		imageLoader = ImageLoader.getInstance();

		JPanel upPnl = new JPanel();
		JPanel lowPnl = new JPanel();
		okBtn = new JButton("Buscar");
		cancelBtn = new JButton("Cancelar");
		searchLbl = new JLabel("Buscar:");
		searchFld = new TextValidatorField(okBtn, cancelBtn);

		searchBtn = new JButton("Buscar", imageLoader.getImage(
				"find.png"));
		backBtn = new JButton("Anterior", imageLoader.getImage(
				"left.png"));
		nextBtn = new JButton("Siguiente", imageLoader.getImage(
				"right.png"));

		backBtn.setEnabled(false);
		nextBtn.setEnabled(false);

		searchLbl.setPreferredSize(new Dimension(50, 20));
		searchFld.setPreferredSize(new Dimension(200, 20));

		okBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
				Thread thread = new Thread(StoreTableSearch.this,
						"Search Thread");
				thread.start();
			}

		});
		cancelBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}

		});

		upPnl.add(searchLbl);
		upPnl.add(searchFld);
		lowPnl.add(okBtn);
		lowPnl.add(cancelBtn);

		add(upPnl, BorderLayout.CENTER);
		add(lowPnl, BorderLayout.SOUTH);

		setButtonsMnemonic();
		setButtonsTooltip();
		setButtonsAction();

		setSize(300, 90);
		setTitle("Buscar...");
		setLocation((GlobalConstants.SCREEN_SIZE_WIDTH - getWidth()) / 2,
				(GlobalConstants.SCREEN_SIZE_HEIGHT - getHeight()) / 2);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setAlwaysOnTop(true);
		setResizable(false);
	}

	private void setButtonsMnemonic() {
		searchBtn.setMnemonic(KeyEvent.VK_B);
		backBtn.setMnemonic(KeyEvent.VK_LEFT);
		nextBtn.setMnemonic(KeyEvent.VK_RIGHT);
	}

	private void setButtonsTooltip() {
		okBtn.setToolTipText("Buscar el texto en la tabla");
		cancelBtn.setToolTipText("Cancelar la busqueda");
		searchBtn.setToolTipText("Buscar un datos en la tabla");
		backBtn.setToolTipText("Ver resultado anterior");
		nextBtn.setToolTipText("Ver resultado siguiente");
	}

	private void setButtonsAction() {
		searchBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				setVisible(true);
			}

		});
		backBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Coordinates coordinates = iterator.previous();
				tablePanel.setSelectedCell(coordinates.row, coordinates.col);
				fireIteratorUpdate();
			}

		});
		nextBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Coordinates coordinates = iterator.next();
				tablePanel.setSelectedCell(coordinates.row, coordinates.col);
				fireIteratorUpdate();
			}

		});
	}

	/**
	 * Retorna el boton de busqueda
	 * <p>Creation date 8/06/2006 - 07:09:35 AM
	 *
	 * @return		Boton de busqueda
	 * @since 1.0
	 */
	public JButton getSearchBtn() {
		return searchBtn;
	}

	/**
	 * Retorna el boton de resultado anterior
	 * <p>Creation date 8/06/2006 - 07:09:53 AM
	 *
	 * @return		Resultado anterior
	 * @since 1.0
	 */
	public JButton getBackBtn() {
		return backBtn;
	}

	/**
	 * Retorna el boton de resultado siguiente
	 * <p>Creation date 8/06/2006 - 07:10:16 AM
	 *
	 * @return		Resultado siguiente
	 * @since 1.0
	 */
	public JButton getNextBtn() {
		return nextBtn;
	}

	public void run() {
		tablePanel.updateData("Buscando...");

		String searchKey = searchFld.getText().toLowerCase();
		WebRowSet data = tablePanel.getTableResultSet();
		ArrayList<Coordinates> results = new ArrayList<Coordinates>();

		try {
			data.beforeFirst();
			while (data.next()) {
				for (int i = 0; i < data.getMetaData().getColumnCount(); i++) {
					String cellData = data.getString(i + 1);
					if (cellData != null
							&& cellData.toLowerCase().contains(searchKey))
						results.add(new Coordinates(data.getRow() - 1, i));
				}
			}

			data.beforeFirst();
			iterator = results.listIterator();
			fireIteratorUpdate();

		} catch (SQLException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}

		tablePanel.setText("Busqueda terminada, encontrados " + results.size()
				+ " ocurrencias");
	}

	/**
	 * Este metodo debe ser llamado cuando el iterador cambie
	 * con el fin de actualizar el estado de los botones
	 * <p>Creation date 8/06/2006 - 11:00:13 AM
	 *
	 * @since 1.0
	 */
	void fireIteratorUpdate() {
		nextBtn.setEnabled(iterator.hasNext());
		backBtn.setEnabled(iterator.hasPrevious());
	}

	private class Coordinates {

		final int row;

		final int col;

		/**
		 * Almacena las coordenadas de un resultado
		 * <p>
		 * Creation date 8/06/2006 - 09:42:21 AM
		 * 
		 * @param row
		 *            Fila
		 * @param col
		 *            Columna
		 * 
		 * @since 1.0
		 */
		public Coordinates(int row, int col) {
			this.row = row;
			this.col = col;
		}

		@Override
		public String toString() {
			return "Row " + row + " - Column " + col;
		}

	}

}
