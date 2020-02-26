package cesar;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CesarEncryptionCLI {
	// ключ шифрования
	static final int key = 1;
	// код буквы А
	static final int enBigALetterCode = 65;
	static final int enSmallALetterCode = 97;
	static final int rusBigALetterCode = 1040;
	static final int rusSmallALetterCode = 1072;
	// всего букв
	static final int enLettersTotal = 26;
	static final int rusLettersTotal = 32;
	static final Map<Integer, Integer> lettersTotalByFirstLetter = Map.of(
			enBigALetterCode, enLettersTotal,
			enSmallALetterCode, enLettersTotal,
			rusBigALetterCode, rusLettersTotal,
			rusSmallALetterCode, rusLettersTotal
	);


	public static void main(String[] args) {
		List<String> encryptedFiles = Arrays.asList("encrypted1.txt");
		List<String> normalFiles = Arrays.asList("normal1.txt", "normal2.txt");

		// decryption
		for (String file : encryptedFiles) {
			InputStream encryptedStream = CesarEncryptionCLI.class
					.getResourceAsStream("/" + file);

			if (encryptedStream == null) {
				log("No such file: " + file);
				continue;
			}

			writeStringToFile( decrypt( convertStreamToString(encryptedStream) ),
					"src/main/resources/out/" + file + "_decrypted");

			log(file + " file decrypted");
		}

		// encryption
		for (String file : normalFiles) {
			InputStream decryptedStream = CesarEncryptionCLI.class
					.getResourceAsStream("/" + file);

			if (decryptedStream == null) {
				log("No such file: " + file);
				continue;
			}

			writeStringToFile( encrypt( convertStreamToString(decryptedStream) ),
					"src/main/resources/out/" + file + "_encrypted");

			log(file + " file encrypted");
		}
	}


	public static String encrypt(String str) {
		StringBuilder encryptedStr = new StringBuilder();

		for (int i = 0; i < str.length(); i++) {
			encryptedStr.append( encodeCharIfAlphabetic(str.charAt(i), key) );
		}

		return encryptedStr.toString();
	}

	public static String decrypt(String encryptedStr) {
		StringBuilder decryptedStr = new StringBuilder();

		for (int i = 0; i < encryptedStr.length(); i++) {
			decryptedStr.append( encodeCharIfAlphabetic(encryptedStr.charAt(i), -key) );
		}

		return decryptedStr.toString();
	}
	
	public static char encodeCharIfAlphabetic(char chr, int offset) {
		for (Map.Entry<Integer, Integer> l : lettersTotalByFirstLetter.entrySet()) {
			int firstLetter = l.getKey();
			int totalLetters = l.getValue();
			if (chr >= firstLetter && chr < firstLetter + totalLetters) {
				return (char) ( mod((chr + offset - firstLetter), totalLetters) + firstLetter);
			}
		}
		return chr;
	}

	private static int mod(int x, int y) {
		int result = x % y;
		if (result < 0)
			result += y;
		return result;
	}

	public static void writeStringToFile(String str, String path) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(path, StandardCharsets.UTF_8);
		} catch (FileNotFoundException e) {
			log("Can't write to file '" + path + "'");
			return;
		} catch (UnsupportedEncodingException e) {
			log("UnsupportedEncodingException");
			return;
		} catch (IOException e) {
			log("IOException");
			e.printStackTrace();
			return;
		}
		writer.println(str);
		writer.close();
	}

	public static void log(String str) {
		System.out.println(str);
	}

	public static String convertStreamToString(InputStream is) {
		Scanner s = new Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
}
