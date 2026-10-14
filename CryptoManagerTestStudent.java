
/**
 * Class: CMSC203
 * Assignment: 3
 * Description: Student tests for CryptoManager.
 * Name: Arbaz Saghir
 */
package asaghir1_assignment3;


import junit.framework.TestCase;


public class CryptoManagerTestStudent extends TestCase {

    /**
     * Tests allowed characters and characters outside the bounds.
     */
    public void testIsStringInBounds() {
        assertTrue(CryptoManager.isStringInBounds("JAVA 203!"));
        assertTrue(CryptoManager.isStringInBounds(" _"));
        assertTrue(CryptoManager.isStringInBounds(""));

        assertFalse(CryptoManager.isStringInBounds("Java"));
        assertFalse(CryptoManager.isStringInBounds("`"));
        assertFalse(CryptoManager.isStringInBounds("\n"));
    }

    /**
     * Tests Vigenere encryption and repeating the keyword.
     */
    public void testVigenereEncryption() {
        // ASCII-based shifts: A with A produces a quotation mark.
        assertEquals("\"$\"",
                CryptoManager.vigenereEncryption("ABA", "AB"));

        assertEquals("",
                CryptoManager.vigenereEncryption("", "CODE"));

        assertEquals(
                "The selected string is not in bounds, Try again.",
                CryptoManager.vigenereEncryption("java", "CODE"));
    }

    /**
     * Tests Vigenere decryption.
     */
    public void testVigenereDecryption() {
        assertEquals("ABA",
                CryptoManager.vigenereDecryption("\"$\"", "AB"));

        String original = "STUDY JAVA!";
        String key = "CODE9";

        String encrypted =
                CryptoManager.vigenereEncryption(original, key);

        assertEquals(original,
                CryptoManager.vigenereDecryption(encrypted, key));
    }

    /**
     * Tests Playfair's row, column, rectangle, and padding rules.
     */
    public void testPlayfairEncryption() {
        // With an empty keyword, the matrix starts with ABCDEFGH.
        assertEquals("BC",
                CryptoManager.playfairEncryption("AB", ""));

        assertEquals("IQ",
                CryptoManager.playfairEncryption("AI", ""));

        assertEquals("BI",
                CryptoManager.playfairEncryption("AJ", ""));

        // An odd-length message receives a final X.
        assertEquals(
                CryptoManager.playfairEncryption("CATX", "CODE"),
                CryptoManager.playfairEncryption("CAT", "CODE"));

        assertEquals(
                "The selected string is not in bounds, Try again.",
                CryptoManager.playfairEncryption("java", "CODE"));
    }

    /**
     * Tests Playfair decryption and a message containing a space.
     */
    public void testPlayfairDecryption() {
        assertEquals("AB",
                CryptoManager.playfairDecryption("BC", ""));

        assertEquals("AI",
                CryptoManager.playfairDecryption("IQ", ""));

        assertEquals("AJ",
                CryptoManager.playfairDecryption("BI", ""));

        String original = "JAVA CODE";
        String key = "STUDY";

        String encrypted =
                CryptoManager.playfairEncryption(original, key);

        assertEquals(original,
                CryptoManager.playfairDecryption(encrypted, key));
    }

    /**
     * Tests Caesar encryption, wrapping, and negative shifts.
     */
    public void testCaesarEncryption() {
        assertEquals("EFG",
                CryptoManager.caesarEncryption("ABC", 4));

        assertEquals(" ",
                CryptoManager.caesarEncryption("_", 1));

        assertEquals("_",
                CryptoManager.caesarEncryption(" ", -1));

        assertEquals("JAVA",
                CryptoManager.caesarEncryption("JAVA", 64));

        assertEquals(
                "The selected string is not in bounds, Try again.",
                CryptoManager.caesarEncryption("java", 4));
    }

    /**
     * Tests Caesar decryption, wrapping, and large shifts.
     */
    public void testCaesarDecryption() {
        assertEquals("ABC",
                CryptoManager.caesarDecryption("EFG", 4));

        assertEquals("_",
                CryptoManager.caesarDecryption(" ", 1));

        assertEquals(" ",
                CryptoManager.caesarDecryption("_", -1));

        String original = "JAVA 203!";
        int key = 130;

        String encrypted =
                CryptoManager.caesarEncryption(original, key);

        assertEquals(original,
                CryptoManager.caesarDecryption(encrypted, key));
    }
}
