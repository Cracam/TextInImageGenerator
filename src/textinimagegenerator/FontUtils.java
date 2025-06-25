package textinimagegenerator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

// Static utility class for font character replacement
public final class FontUtils {

    // Define the equivalence map as a final static variable
    // Contains mappings for unsupported characters to their replacements
    public static final Map<Character, Character> EQUIVALENCE_MAP;
    
    // Static initializer block to populate the equivalence map
    static {
        EQUIVALENCE_MAP = new HashMap<>();
        EQUIVALENCE_MAP.put('à', 'a');
        EQUIVALENCE_MAP.put('á', 'a');
        EQUIVALENCE_MAP.put('â', 'a');
        EQUIVALENCE_MAP.put('ã', 'a');
        EQUIVALENCE_MAP.put('ä', 'a');
        EQUIVALENCE_MAP.put('å', 'a');
        EQUIVALENCE_MAP.put('é', 'e');
        EQUIVALENCE_MAP.put('è', 'e');
        EQUIVALENCE_MAP.put('ê', 'e');
        EQUIVALENCE_MAP.put('ë', 'e');
        EQUIVALENCE_MAP.put('í', 'i');
        EQUIVALENCE_MAP.put('ì', 'i');
        EQUIVALENCE_MAP.put('î', 'i');
        EQUIVALENCE_MAP.put('ï', 'i');
        EQUIVALENCE_MAP.put('ó', 'o');
        EQUIVALENCE_MAP.put('ò', 'o');
        EQUIVALENCE_MAP.put('ô', 'o');
        EQUIVALENCE_MAP.put('õ', 'o');
        EQUIVALENCE_MAP.put('ö', 'o');
        EQUIVALENCE_MAP.put('ú', 'u');
        EQUIVALENCE_MAP.put('ù', 'u'); // U+00F9
        EQUIVALENCE_MAP.put('û', 'u');
        EQUIVALENCE_MAP.put('ü', 'u');
        EQUIVALENCE_MAP.put('ç', 'c');
        EQUIVALENCE_MAP.put('\uFFFD', '?'); // Replacement character
        // Add more mappings as needed
    }

    // Private constructor to prevent instantiation
    private FontUtils() {
        throw new AssertionError("Utility class - cannot be instantiated");
    }

    // Replaces specific characters in the input text with equivalents based on the equivalence map
    // Parameters:
    // - customFontAwt: The AWT Font to check for character support
    // - text: The input text to process
    // Returns: The processed text with specified characters replaced
        public static String replaceUnsupportedCharacters(Font customFontAwt, String text) {
                if (text == null || text.isEmpty()) {
                        return text;
                }

                StringBuilder result = new StringBuilder();

                Character replacement ;
                // Iterate through each character in the input text
                for (char c : text.toCharArray()) {
                         replacement = EQUIVALENCE_MAP.get(c);
                        
                        if (!customFontAwt.canDisplay(c)) {
                                if (replacement == null) {
                                        // If not supported by the font and no replacement, use '?'
                                        result.append(' ');
                                } else {
                                        result.append(replacement);
                                }

                        } else {
                                //test if letter is displayed
                                if (c==' ' ||  isCharacterRendered(c, customFontAwt)) { //we verify that it's not a space
                                        // If supported by the font keep the original
                                        result.append(c);
                                } else {
                                        //Try to remplace it or remplace by a space
                                        if (replacement == null) {
                                                // If not supported by the font and no replacement, use '?'
                                                result.append(' ');
                                        } else {
                                                result.append(replacement);
                                        }
                                }
                        }

                }

                return result.toString();
        }

    
    
    
    // Creates an image with the given character and checks if it renders non-empty
    // Parameters:
    // - c: The character to render
    // - font: The AWT Font to use for rendering
    // Returns: true if the image contains non-transparent pixels, false if it is empty
    public static boolean isCharacterRendered(char c, Font font) {
        // Create a small BufferedImage for the character
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Set rendering hints for better quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Set the font and color
        g2d.setFont(font);
        g2d.setColor(Color.BLACK);

        // Draw the character
        g2d.drawString(String.valueOf(c), 0, 40);

        // Dispose the graphics context
        g2d.dispose();

        // Check if the image is empty (all pixels transparent)
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if ((image.getRGB(x, y) >> 24) != 0x00) {
                    return true; // Non-transparent pixel found, character is rendered
                }
            }
        }
        return false; // All pixels are transparent, character is not rendered
    }
}