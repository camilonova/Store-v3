package com.nova.log.cryptoStore;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.nova.dat.loader.ErrorLogLoader;

/**
 * Esta clase, provee los metodos de encriptacion de los datos en los que la
 * seguridad es vital. En la fase inicial se utiliza el algoritmo MD5 para tal
 * fin.
 * 
 * @author Camilo Nova
 * @version 1.0
 */
public class CryptoStore {

	/**
	 * Encripta la cadena pasada por parametro y la retorna. Si la cadena no se
	 * puede encriptar se retorna null
	 * 
	 * @param cadena
	 *            Cadena a encriptar
	 * @return Cadena encriptada
	 */
	public static String encriptar(String cadena) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] b = md.digest(cadena.getBytes());

			int size = b.length;
			StringBuffer h = new StringBuffer(size);

			for (int i = 0; i < size; i++) {
				int u = b[i] & 255; // unsigned conversion

				if (u < 16)
					h.append("0" + Integer.toHexString(u));
				else
					h.append(Integer.toHexString(u));
			}
			return h.toString();
		} catch (NoSuchAlgorithmException e) {
			ErrorLogLoader.addErrorEntry(e);
			e.printStackTrace();
		}
		return null;
	}
}