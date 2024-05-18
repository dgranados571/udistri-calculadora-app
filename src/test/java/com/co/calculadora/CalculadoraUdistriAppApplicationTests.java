package com.co.calculadora;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CalculadoraUdistriAppApplicationTests {

	static String abcdario = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // Danny

	@Test
	void contextLoads() {
		String messageSecret = "**AQUI EL MENSAJE A ENCRIPTAR**";
		practiceRSA(messageSecret);
	}
	
	public static void practiceRSA(String messageSecret) {		
		System.out.println("Mensaje Nat --> " + messageSecret);
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(2048);
			KeyPair pair = generator.generateKeyPair();			 
			PrivateKey privateKey = pair.getPrivate();
			PublicKey publicKey = pair.getPublic();			
			try{				   
			    
				Cipher encryptCipher = Cipher.getInstance("RSA");
			    encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);			    
			    
			    byte[] secretMessageBytes = messageSecret.getBytes(StandardCharsets.UTF_8);
			    byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);			    
			    
			    String encodedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);
			    System.out.println("Mensaje Cod --> " + encodedMessage);			    
			    
			    Cipher decryptCipher = Cipher.getInstance("RSA");
				decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
				
				byte[] decryptedMessageBytes = decryptCipher.doFinal(encryptedMessageBytes);
				String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);
				System.out.println("Mensaje Dec --> " + decryptedMessage);
				
			} catch (NoSuchPaddingException e) {
				System.out.println("Error 4 en --> " + e);
			} catch (InvalidKeyException e) {
				System.out.println("Error 5 en --> " + e);
			} catch (IllegalBlockSizeException e) {
				System.out.println("Error 6 en --> " + e);
			} catch (BadPaddingException e) {
				System.out.println("Error 7 en --> " + e);
			} catch (NoSuchAlgorithmException e) {
				System.out.println("Error 8 en --> " + e);
			}	    
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Error In --> " + e);
		}
		
	}
	
	public static void diffieHellman() {
		int g = 15;
		int p = 97;

		// P1 Elige --> a = 12
		double Ax = Math.pow(g, 12);
		System.out.println("Ax --> " + Ax);
		int A = (int) (Ax % p);

		System.out.println("A --> " + A);

		// P1 Elige --> b = 30
		double Bx = Math.pow(g, 30);
		System.out.println("Bx --> " + Bx);
		int B = (int) (Bx % p);
		System.out.println("B --> " + B);

		double K1x = Math.pow(B, 12);
		System.out.println("K1x --> " + K1x);
		int K1 = (int) (K1x % p);

		double K2x = Math.pow(A, 30);
		System.out.println("K2x --> " + K2x);
		int K2 = (int) (K2x % p);

		System.out.println((K1 == K2) + " " + K1 + " " + K2);
	}

	public static void desMesjGen() {
		String msjEncriptado = "HLWWGWHGHBDQOHLGSWW UHGOOQLRLDOHU  WLQDDUHRSDOLR VQ F  E OLHJF  HAVD XU ";

		List<String> columnas = desEncriptaMsj(msjEncriptado, 3);
		List<String> columnasDesEcriptesCesar = new ArrayList<>();
		for (String str : columnas) {
			columnasDesEcriptesCesar.add(desEncriptaCesar(str));
		}
		String msjFinal = "";
		for (int x = 0; x < columnasDesEcriptesCesar.get(0).length(); x++) {
			String msjTemp = "";
			for (int y = 0; y < columnasDesEcriptesCesar.size(); y++) {
				msjTemp = msjTemp + columnasDesEcriptesCesar.get(y).charAt(x);
			}
			msjFinal = msjFinal + msjTemp;
		}
		System.out.println("MSJ FINAL 1 --> " + msjFinal);
	}

	public static List<String> desEncriptaMsj(String mensaje, int numColumns) {

		List<String> columnas = new ArrayList<>();

		int longColumnas = mensaje.length() / numColumns;
		boolean controlWhile = false;
		int indInicial = 0;
		int indFinal = longColumnas;
		while (true) {
			String mensajeProcesa = mensaje.substring(indInicial, indFinal);
			columnas.add(mensajeProcesa);
			indInicial = indFinal;
			indFinal = indFinal + longColumnas;
			if (indFinal > mensaje.length()) {
				controlWhile = true;
			}
			if (controlWhile) {
				break;
			}
		}
		return columnas;
	}

	public static String desEncriptaCesar(String mensaje) {

		String msjCifradoCesar = "";
		for (int x = 0; x < mensaje.length(); x++) {
			char letra = mensaje.charAt(x);
			msjCifradoCesar = msjCifradoCesar + validaLetraEncriptaCesar(letra);
		}
		return msjCifradoCesar;
	}

	public static String validaLetraEncriptaCesar(char letra) {
		String letraCesar = "";
		int longAbcd = abcdario.length();
		int indiceLetra = 0;
		boolean isSpace = false;
		for (int x = 0; x < longAbcd; x++) {
			char letraAbcd = abcdario.charAt(x);
			if (letraAbcd == letra) {
				int indAValidar = x - 3;
				if (indAValidar < 0) {
					indiceLetra = (longAbcd) - (indAValidar * -1);
				} else if (indAValidar == 0) {
					indiceLetra = indAValidar;
				} else {
					indiceLetra = indAValidar;
				}
				break;
			} else if (letra == ' ') {
				isSpace = true;
				break;
			}
		}
		letraCesar = String.valueOf(abcdario.charAt(indiceLetra));
		if (isSpace) {
			letraCesar = " ";
		}
		return letraCesar;
	}

}
