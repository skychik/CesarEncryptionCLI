import cesar.CesarEncryptionCLI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static cesar.CesarEncryptionCLI.encodeCharIfAlphabetic;
import static cesar.CesarEncryptionCLI.log;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {
	final static String pathToFile = "/file.txt";
	static String strToTest;

	@BeforeAll
	static void setUp() {
		InputStream stream = Tests.class
				.getResourceAsStream(pathToFile);

		if (stream == null) {
			log("No such file: " + pathToFile);
			System.exit(1);
		}

		strToTest = CesarEncryptionCLI.convertStreamToString(stream);
	}

	@Test
	void testEncodeCharIfAlphabetic() {
		char a = 'a';
		char aEncoded = encodeCharIfAlphabetic(a, 2);
		assertEquals('c', aEncoded);
		char aEncoded2 = encodeCharIfAlphabetic(a, -2);
		assertEquals('y', aEncoded2);

		a = 'A';
		aEncoded = encodeCharIfAlphabetic(a, 2);
		assertEquals('C', aEncoded);
		aEncoded2 = encodeCharIfAlphabetic(a, -2);
		assertEquals('Y', aEncoded2);

		a = 'а';
		aEncoded = encodeCharIfAlphabetic(a, 2);
		assertEquals('в', aEncoded);
		aEncoded2 = encodeCharIfAlphabetic(a, -2);
		assertEquals('ю', aEncoded2);

		a = 'А';
		aEncoded = encodeCharIfAlphabetic(a, 2);
		assertEquals('В', aEncoded);
		aEncoded2 = encodeCharIfAlphabetic(a, -2);
		assertEquals('Ю', aEncoded2);
	}

	@Test
	void test1() {
		String resultStr = CesarEncryptionCLI.encrypt(CesarEncryptionCLI.decrypt(strToTest));
		assertEquals(strToTest, resultStr);
	}

	@Test
	void test2() {
		String resultStr = CesarEncryptionCLI.decrypt(CesarEncryptionCLI.encrypt(strToTest));
		assertEquals(resultStr, strToTest);
	}
}
