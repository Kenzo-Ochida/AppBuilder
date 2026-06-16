package br.mackenzie.mackleaps.utils;


import org.jasypt.util.text.AES256TextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Codec {

		private Codec() {
		}
		
		private static final Logger LOGGER = LoggerFactory.getLogger(Codec.class);

		public static String decrypt(String input) {
			LOGGER.debug("Decrypting content");
			
			AES256TextEncryptor aesEncryptor = new AES256TextEncryptor();
		    aesEncryptor.setPassword(Util.getBin());
		    return aesEncryptor.decrypt(input);
		}
		
		public static String encrypt(String input){
			LOGGER.debug("Encrypting content");
	        String enc = "";
	        AES256TextEncryptor aesEncryptor = new AES256TextEncryptor();
	        aesEncryptor.setPassword(Util.getBin());
	        enc = aesEncryptor.encrypt(input);
			return enc;
		}
	
}
