

/**
 * Class: CMSC203  CRN: 20798
 * Assignment: 3
 * Description: This program encrypts and decrypts messages using Vigenere, Playfair, 
 * and Caesar ciphers with a keyword or numeric key.
 * Name: Arbaz Saghir
 */
package asaghir1_assignment3;
public class CryptoManager {

    private static final char LOWER_RANGE = ' ';
    private static final char UPPER_RANGE = '_';
    private static final int RANGE =
            UPPER_RANGE - LOWER_RANGE + 1;

    // Includes the missing space to provide 64 characters.
    private static final String ALPHABET64 =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
            + "!\"#$%&'()*+,-./:;<=>?@[\\]^_ ";

    private static final String ERROR_MESSAGE =
            "The selected string is not in bounds, Try again.";

    /**
     * Checks whether every character is within ASCII 32–95.
     */
    public static boolean isStringInBounds(String plainText) {
        for (int i = 0; i < plainText.length(); i++) {
            char character = plainText.charAt(i);

            if (character < LOWER_RANGE
                    || character > UPPER_RANGE) {
                return false;
            }
        }

        return true;
    }

    /**
     * Encrypts text using a repeating keyword.
     */
    public static String vigenereEncryption(
            String plainText, String key) {

        if (!isStringInBounds(plainText)) {
            return ERROR_MESSAGE;
        }

        validateVigenereKey(key);

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < plainText.length(); i++) {
            int textPosition =
                    plainText.charAt(i) - LOWER_RANGE;

            int keyPosition =
                    key.charAt(i % key.length()) - LOWER_RANGE;

            int encryptedPosition =
                    (textPosition + keyPosition) % RANGE;

            result.append(
                    (char) (encryptedPosition + LOWER_RANGE));
        }

        return result.toString();
    }

    /**
     * Decrypts text using the original keyword.
     */
    public static String vigenereDecryption(
            String encryptedText, String key) {

        if (!isStringInBounds(encryptedText)) {
            return ERROR_MESSAGE;
        }

        validateVigenereKey(key);

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < encryptedText.length(); i++) {
            int textPosition =
                    encryptedText.charAt(i) - LOWER_RANGE;

            int keyPosition =
                    key.charAt(i % key.length()) - LOWER_RANGE;

            int decryptedPosition =
                    Math.floorMod(textPosition - keyPosition, RANGE);

            result.append(
                    (char) (decryptedPosition + LOWER_RANGE));
        }

        return result.toString();
    }

    /**
     * Encrypts consecutive character pairs using Playfair.
     * An X completes the final pair when necessary.
     */
    public static String playfairEncryption(
            String plainText, String key) {

        if (!isStringInBounds(plainText)) {
            return ERROR_MESSAGE;
        }

        char[][] matrix = buildPlayfairMatrix(key);

        if (plainText.length() % 2 != 0) {
            plainText += "X";
        }

        return transformPlayfair(plainText, matrix, 1);
    }

    /**
     * Decrypts Playfair character pairs.
     * Removes a final X under the chosen padding convention.
     */
    public static String playfairDecryption(
            String encryptedText, String key) {

        if (!isStringInBounds(encryptedText)) {
            return ERROR_MESSAGE;
        }

        if (encryptedText.length() % 2 != 0) {
            throw new IllegalArgumentException(
                    "Playfair encrypted text must have an even length.");
        }

        char[][] matrix = buildPlayfairMatrix(key);

        String result =
                transformPlayfair(encryptedText, matrix, -1);

        if (result.endsWith("X")) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

    /**
     * Encrypts text using a numeric shift.
     */
    public static String caesarEncryption(
            String plainText, int key) {

        if (!isStringInBounds(plainText)) {
            return ERROR_MESSAGE;
        }

        int shift = Math.floorMod(key, RANGE);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < plainText.length(); i++) {
            int position = plainText.charAt(i) - LOWER_RANGE;
            int encryptedPosition = (position + shift) % RANGE;

            result.append(
                    (char) (encryptedPosition + LOWER_RANGE));
        }

        return result.toString();
    }

    /**
     * Decrypts text using the original numeric shift.
     */
    public static String caesarDecryption(
            String encryptedText, int key) {

        if (!isStringInBounds(encryptedText)) {
            return ERROR_MESSAGE;
        }

        int shift = Math.floorMod(key, RANGE);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < encryptedText.length(); i++) {
            int position =
                    encryptedText.charAt(i) - LOWER_RANGE;

            int decryptedPosition =
                    Math.floorMod(position - shift, RANGE);

            result.append(
                    (char) (decryptedPosition + LOWER_RANGE));
        }

        return result.toString();
    }

    /**
     * Validates a Vigenere keyword.
     */
    private static void validateVigenereKey(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException(
                    "A nonempty keyword is required.");
        }

        if (!isStringInBounds(key)) {
            throw new IllegalArgumentException(
                    "The keyword contains an invalid character.");
        }
    }

    /**
     * Builds the matrix from unique keyword characters,
     * followed by the remaining alphabet characters.
     */
    private static char[][] buildPlayfairMatrix(String key) {
        if (key == null || !isStringInBounds(key)) {
            throw new IllegalArgumentException(
                    "The keyword contains an invalid character.");
        }

        StringBuilder characters = new StringBuilder();
        String combined = key + ALPHABET64;

        for (int i = 0; i < combined.length(); i++) {
            char character = combined.charAt(i);

            if (characters.indexOf(
                    String.valueOf(character)) == -1) {
                characters.append(character);
            }
        }

        char[][] matrix = new char[8][8];
        int position = 0;

        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                matrix[row][column] =
                        characters.charAt(position);
                position++;
            }
        }

        return matrix;
    }

    /**
     * Finds a character's row and column.
     */
    private static int[] findPosition(
            char character, char[][] matrix) {

        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                if (matrix[row][column] == character) {
                    return new int[] {row, column};
                }
            }
        }

        throw new IllegalArgumentException(
                "Character was not found in the Playfair matrix.");
    }

    /**
     * Applies Playfair rules.
     * Direction 1 encrypts; direction -1 decrypts.
     */
    private static String transformPlayfair(
            String text, char[][] matrix, int direction) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i += 2) {
            int[] first =
                    findPosition(text.charAt(i), matrix);

            int[] second =
                    findPosition(text.charAt(i + 1), matrix);

            int firstRow = first[0];
            int firstColumn = first[1];
            int secondRow = second[0];
            int secondColumn = second[1];

            if (firstRow == secondRow) {
                firstColumn =
                        Math.floorMod(firstColumn + direction, 8);
                secondColumn =
                        Math.floorMod(secondColumn + direction, 8);

            } else if (firstColumn == secondColumn) {
                firstRow =
                        Math.floorMod(firstRow + direction, 8);
                secondRow =
                        Math.floorMod(secondRow + direction, 8);

            } else {
                int temporaryColumn = firstColumn;
                firstColumn = secondColumn;
                secondColumn = temporaryColumn;
            }

            result.append(matrix[firstRow][firstColumn]);
            result.append(matrix[secondRow][secondColumn]);
        }

        return result.toString();
    }
}
