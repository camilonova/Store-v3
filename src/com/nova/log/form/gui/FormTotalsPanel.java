package com.nova.log.form.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.nova.log.input.validator.MoneyValidatorField;

/**
 * Clase que representa el panel de los totales en el formulario. Este panel
 * contiene inicialmente el valor excluido, gravado y total.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class FormTotalsPanel extends JPanel {

	JLabel excluidoLbl;

	MoneyValidatorField excluidoFld;

	JLabel gravadoLbl;

	MoneyValidatorField gravadoFld;

	JLabel impuestoLbl;

	MoneyValidatorField impuestoFld;

	JLabel totalLbl;

	MoneyValidatorField totalFld;

	Font font;

	/**
	 * Crea los componentes y los agrega al panel.
	 * <p>
	 * Creation date 20/04/2006 - 08:57:33 PM
	 * 
	 * @param form
	 *            Formato
	 * 
	 * @since 1.0
	 */
	public FormTotalsPanel(Form form) {
		excluidoLbl = new JLabel("Excluido", SwingConstants.CENTER);
		excluidoFld = new MoneyValidatorField();
		gravadoLbl = new JLabel("Base", SwingConstants.CENTER);
		gravadoFld = new MoneyValidatorField();
		impuestoLbl = new JLabel("Impuesto", SwingConstants.CENTER);
		impuestoFld = new MoneyValidatorField();
		totalLbl = new JLabel("Total", SwingConstants.CENTER);
		totalFld = new MoneyValidatorField();
		font = new Font("Arial", Font.BOLD, 16);

		excluidoLbl.setPreferredSize(new Dimension(60, 20));
		excluidoFld.setPreferredSize(new Dimension(120, 20));
		gravadoLbl.setPreferredSize(new Dimension(60, 20));
		gravadoFld.setPreferredSize(new Dimension(120, 20));
		impuestoLbl.setPreferredSize(new Dimension(60, 20));
		impuestoFld.setPreferredSize(new Dimension(120, 20));
		totalLbl.setPreferredSize(new Dimension(60, 20));
		totalFld.setPreferredSize(new Dimension(120, 20));

		excluidoFld.setFont(font);
		gravadoFld.setFont(font);
		impuestoFld.setFont(font);
		totalFld.setFont(font);

		excluidoFld.setEnabled(false);
		gravadoFld.setEnabled(false);
		impuestoFld.setEnabled(false);

		totalFld.setForeground(form.getFormColor());
		totalFld.setEditable(false);

		excluidoFld.setToolTipText("Total excluido del formato");
		gravadoFld.setToolTipText("Total gravado del formato");
		impuestoFld.setToolTipText("Total impuesto al valor gravado");
		totalFld.setToolTipText("Gran Total");

		add(excluidoLbl);
		add(excluidoFld);
		add(gravadoLbl);
		add(gravadoFld);
		add(impuestoLbl);
		add(impuestoFld);
		add(totalLbl);
		add(totalFld);

		setBorder(BorderFactory.createTitledBorder("Totales"));
		setPreferredSize(new Dimension(790, 60));
	}

	/**
	 * Actualiza el valor de los componentes.
	 * <p>
	 * Creation date 20/04/2006 - 09:05:24 PM
	 * 
	 * @param excluido
	 *            Valor excluido
	 * @param gravado
	 *            Valor gravado
	 * @param impuesto
	 *            Valor impuesto
	 * @since 1.0
	 */
	void setTotals(double excluido, double gravado, double impuesto) {
		excluidoFld.setDouble(excluido);
		gravadoFld.setDouble(gravado);
		impuestoFld.setDouble(impuesto);
		totalFld.setDouble(excluido + gravado + impuesto);
	}

	/**
	 * Retorna el valor excluido
	 * <p>
	 * Creation date 2/05/2006 - 09:51:19 AM
	 * 
	 * @return Valor excluido (Sin impuesto)
	 * @throws ParseException
	 * @since 1.0
	 */
	double getExempt() throws ParseException {
		return excluidoFld.getDouble();
	}

	/**
	 * Determina el valor excluido
	 * <p>
	 * Creation date 4/05/2006 - 08:08:08 AM
	 * 
	 * @param exempt
	 *            Valor excluido
	 * @since 1.0
	 */
	void setExempt(double exempt) {
		excluidoFld.setDouble(exempt);
	}

	/**
	 * Retorna el valor de la base (Gravado)
	 * <p>
	 * Creation date 2/05/2006 - 09:50:50 AM
	 * 
	 * @return Valor de la base (Gravado)
	 * @throws ParseException
	 * @since 1.0
	 */
	double getBase() throws ParseException {
		return gravadoFld.getDouble();
	}

	/**
	 * Determina el valor de la base
	 * <p>
	 * Creation date 4/05/2006 - 08:08:53 AM
	 * 
	 * @param base
	 *            Valor de la base
	 * @since 1.0
	 */
	void setBase(double base) {
		gravadoFld.setDouble(base);
	}

	/**
	 * Retorna el valor del impuesto
	 * <p>
	 * Creation date 2/05/2006 - 09:50:05 AM
	 * 
	 * @return Valor del impuesto
	 * @throws ParseException
	 * @since 1.0
	 */
	double getTax() throws ParseException {
		return impuestoFld.getDouble();
	}

	/**
	 * Determina el valor del impuesto
	 * <p>
	 * Creation date 4/05/2006 - 08:10:05 AM
	 * 
	 * @param tax
	 *            Valor del impuesto
	 * @since 1.0
	 */
	void setTax(double tax) {
		impuestoFld.setDouble(tax);
	}

	/**
	 * Retorna el total del formato
	 * <p>
	 * Creation date 2/05/2006 - 09:49:47 AM
	 * 
	 * @return Total del formato
	 * @throws ParseException
	 * @since 1.0
	 */
	double getTotal() throws ParseException {
		return totalFld.getDouble();
	}

	/**
	 * Determina el total del formato
	 * <p>
	 * Creation date 4/05/2006 - 08:10:50 AM
	 * 
	 * @param total
	 *            Total del formato
	 * @since 1.0
	 */
	void setTotal(double total) {
		totalFld.setDouble(total);
	}

}
