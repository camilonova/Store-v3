package com.nova.dat.loader;

import java.awt.Component;
import java.net.URL;

import javax.swing.ImageIcon;

/**
 * Esta clase aplica el patron singleton para cargar en memoria una imagen
 * con el metodo getImage().
 * @author Camilo Nova
 * @version 1.0
 */
public class ImageLoader extends Component {

	private static ImageLoader instance;

	private ImageLoader() {
		// Solamente instancia la clase el metodo getInstance()
	}
	
	/**
	 * Retorna la imagen identificada por los parametros. Si el nombre de la
	 * imagen no se encuentra en el cache, entonces la busca en el disco lo cual
	 * tarda un poco mas de tiempo.
	 * 
	 * @param imageName
	 *            Nombre de la imagen
	 * @return Imagen
	 * @since 1.0
	 */
	public synchronized ImageIcon getImage(String imageName) {
		URL resource = getClass().getResource("/images/" + imageName);
		return new ImageIcon(resource);
	}
	
	/**
	 * Retorna la instancia de la clase
	 * <p>Creation date 23/06/2006 - 06:40:26 PM
	 *
	 * @return	Instancia de la clase
	 * @since 1.0
	 */
	public static ImageLoader getInstance() {
		if (instance == null)
			instance = new ImageLoader();
		return instance;
	}

}
