package com.nova.log.form.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JToolBar;

import com.nova.dat.loader.ImageLoader;
import com.nova.log.form.log.FormCommand;

/**
 * Esta clase representa la barra de herramientas del formulario, la cual provee
 * botones para la ejecuccion de los comandos.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class FormToolBar extends JToolBar {

	JButton register;

	JButton up;

	JButton down;

	JButton delRow;

	JButton delAll;

	JButton exit;

	JButton print;

	JButton copy;

	final FormCommand comando;

	final ImageLoader imageLoader;

	/**
	 * Crea la barra de herramientas y agrega el comando respectivo a cada
	 * boton.
	 * <p>
	 * Creation date 20/04/2006 - 07:40:30 PM
	 * 
	 * @param command
	 *            Comando del formulario
	 * 
	 * @since 1.0
	 */
	public FormToolBar(FormCommand command) {
		this.comando = command;
		this.imageLoader = ImageLoader.getInstance();
		register = new JButton("Registrar", imageLoader.getImage(
				"register.png"));
		up = new JButton("Subir Elemento", imageLoader.getImage(
				"up.png"));
		down = new JButton("Bajar Elemento", imageLoader
				.getImage("down.png"));
		delRow = new JButton("Borrar Fila", imageLoader.getImage(
				"delRow.png"));
		delAll = new JButton("Borrar Todo", imageLoader.getImage(
				"delAll.png"));
		exit = new JButton("Salir", imageLoader.getImage(
				"exit.png"));

		register.setMnemonic(KeyEvent.VK_R);
		up.setMnemonic(KeyEvent.VK_UP);
		down.setMnemonic(KeyEvent.VK_DOWN);
		delRow.setMnemonic(KeyEvent.VK_F);
		delAll.setMnemonic(KeyEvent.VK_T);
		exit.setMnemonic(KeyEvent.VK_S);

		register.setToolTipText("Registrar los datos del formato");
		up.setToolTipText("Subir una posicion la fila seleccionada");
		down.setToolTipText("Bajar una posicion la fila seleccionada");
		delRow.setToolTipText("Elimina la fila seleccionada");
		delAll.setToolTipText("Eliminar todas las filas del formato");
		exit.setToolTipText("Salir del formato");

		register.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				comando.register();
			}

		});
		up.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				comando.upRow();
			}

		});
		down.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				comando.downRow();
			}

		});
		delRow.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				comando.delRow();
			}

		});
		delAll.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				comando.delAllRows();
			}

		});
		exit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				comando.exitForm();
			}

		});

		add(register);
		addSeparator();
		add(up);
		add(down);
		addSeparator();
		add(delRow);
		add(delAll);
		addSeparator();
		add(exit);

		setFloatable(false);
		setPreferredSize(new Dimension(790, 30));
	}

	/**
	 * Modifica la barra de herramientas para el evento de ver un formato
	 * <p>
	 * Creation date 4/05/2006 - 10:12:16 AM
	 * 
	 * @since 1.0
	 */
	void loadSeeToolbar() {
		removeAll();

		print = new JButton("Imprimir", imageLoader.getImage(
				"print.png"));
		copy = new JButton("Copiar", imageLoader.getImage(
				"copy.png"));
		exit = new JButton("Salir", imageLoader.getImage(
				"exit.png"));

		print.setMnemonic(KeyEvent.VK_P);
		copy.setMnemonic(KeyEvent.VK_C);
		exit.setMnemonic(KeyEvent.VK_S);

		print.setToolTipText("Imprime el formato actual");
		copy.setToolTipText("Copiar los datos de la tabla en un formato nuevo");
		exit.setToolTipText("Salir del formato");

		print.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				comando.print();
			}

		});
		copy.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				comando.copyDataInNewForm();
			}

		});
		exit.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				comando.exitWithOutSave();
			}

		});

		add(print);
		addSeparator();
		add(copy);
		addSeparator();
		add(exit);
	}
}
