package com.nova.gui.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import com.nova.dat.db.StoreCore;
import com.nova.dat.loader.ErrorLogLoader;
import com.nova.dat.loader.ImageLoader;
import com.nova.dat.parameter.GlobalConstants;

/**
 * Muestra un dialogo con los consejos de la aplicacion, el cual permite mostrar
 * o no mostrar el dialogo al inicio de la aplicacion, tambien permite avanzar
 * entre los consejos y cerrar el dialogo.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class StoreTip extends JDialog {

	JLabel iconLbl = new JLabel(ImageLoader.getInstance().getImage("tip.png"));

	JTextArea textArea = new JTextArea();

	JCheckBox checkBox = new JCheckBox("Mostrar esta ventana al inicio");

	JButton nextTipBtn = new JButton("Siguiente");

	JButton closeBtn = new JButton("Cerrar");

	String property = "SHOW_TIP";

	/**
	 * Construye la ventana donde se muestra el tip
	 * 
	 * @since 1.0
	 */
	public StoreTip() {
		JPanel upPanel = new JPanel();
		JPanel midPanel = new JPanel();
		JPanel lowPanel = new JPanel();
		upPanel.setBorder(BorderFactory.createLoweredBevelBorder());

		upPanel.setPreferredSize(new Dimension(290, 100));
		midPanel.setPreferredSize(new Dimension(290, 20));
		lowPanel.setPreferredSize(new Dimension(290, 40));

		try {
			checkBox.setSelected(Boolean.parseBoolean(StoreCore
					.getProperty(property)));
		} catch (Exception e1) {
			ErrorLogLoader.addErrorEntry(e1);
			e1.printStackTrace();
		}

		textArea.setLineWrap(true);
		textArea.setPreferredSize(new Dimension(220, 80));
		textArea.setBackground(upPanel.getBackground());
		textArea.setDisabledTextColor(Color.BLACK);
		textArea.setText(StoreCore.getTip());
		textArea.setEnabled(false);

		nextTipBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				textArea.setText(StoreCore.getTip());
				repaint();
			}

		});
		closeBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String value = checkBox.isSelected() ? "true" : "false";
				StoreCore.setDataAt("propiedades", "Propiedad", property,
						"Valor", value);
				dispose();
			}

		});

		upPanel.add(iconLbl);
		upPanel.add(textArea);
		midPanel.add(checkBox);
		lowPanel.add(nextTipBtn);
		lowPanel.add(closeBtn);

		add(upPanel, BorderLayout.NORTH);
		add(midPanel, BorderLayout.CENTER);
		add(lowPanel, BorderLayout.SOUTH);

		setTitle("Consejo del dia de Store");
		setSize(300, 200);
		setLocation((GlobalConstants.SCREEN_SIZE_WIDTH - getWidth()) / 2,
				(GlobalConstants.SCREEN_SIZE_HEIGHT - getHeight()) / 2);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		setModal(true);
		setAlwaysOnTop(true);
		setVisible(true);
	}
}
