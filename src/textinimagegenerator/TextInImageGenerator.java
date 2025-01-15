package textinimagegenerator;

import Exeptions.ResourcesFileErrorException;
import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Camille LECOURT
 */
public class TextInImageGenerator extends VBox {

        @FXML
        private VBox root;

        @FXML
        private TextField textField;

        @FXML
        private Button fontSelectorButton;

        @FXML
        private Label PolicePreview;

        @FXML
        private Slider textSizeSlideBar;

        @FXML
        private Slider TextHeighSlideBar;

        @FXML
        private VBox FontChargerHBox;

        @FXML
        private VBox VboxTextSize;

        @FXML
        private VBox VboxTextHeight;

        private final BooleanProperty changed = new SimpleBooleanProperty(false);

        private java.awt.Font customFontAwt = new java.awt.Font("Arial", 0, 10);
        private javafx.scene.text.Font customFontFX = new javafx.scene.text.Font("Arial", 10);
        private double oldFontSize = 14.0;
        private boolean fontChanged = false;

        private static final String PLACEHOLDER_TEXT = "Veuillez Remplacer ce Texte";

        private File fontFile;

        public TextInImageGenerator() {
                try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/interfaceTextSelector.fxml"));
                        if (fxmlLoader == null) {
                                throw new ResourcesFileErrorException();
                        }
                        fxmlLoader.setRoot(this);
                        fxmlLoader.setController(this);
                        fxmlLoader.load();

                        // Initialize event handlers
                        initialize();

                } catch (IOException | ResourcesFileErrorException | IllegalArgumentException ex) {
                        Logger.getLogger(TextInImageGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

        @FXML
        public void initialize() {
                // Initialize the controller and set up event handlers
                textSizeSlideBar.valueProperty().addListener((observable, oldValue, newValue) -> {
                        refreshFont();
                });
                setTextSizeSlideBar(0, 1, 0.05);
                textSizeSlideBar.setValue(0.5);

                TextHeighSlideBar.valueProperty().addListener((observable, oldValue, newValue) -> {
                });
                setTextHeigthSlideBar(-1, 1, 0.05);
                TextHeighSlideBar.setValue(0.0);

                fontSelectorButton.setOnAction(event -> {
                        // Handle button click event
                        loadPolicie();
                        refreshFont();
                });

                textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (textField.getText().isEmpty()) {
                                textField.setText(PLACEHOLDER_TEXT);
                                refreshText();
                        }
                });

        }

        /**
         * Refresh the size of the text
         */
        @FXML
        private void refreshText() {
                String text = textField.getText();
                PolicePreview.setText(text);
                setChanged(true);
        }

        /**
         * Refrash the font and the size of the text
         */
        private void refreshFont() {
                if (customFontFX != null & fontChanged == true) {
                        PolicePreview.setFont(customFontFX);
                        fontChanged = false;
                }
        }

        public void setChanged(boolean value) {
                this.changed.set(value);
        }

        public BooleanProperty isChanged() {
                return changed;
        }

        /**
         * Desactivate the text field
         */
        public void desactivateTextField() {
                textField.setDisable(true);
        }

        /**
         * Activate the text field
         */
        public void activateTextField() {
                textField.setDisable(false);
        }

        /**
         * Desactivate the text field
         */
        public void desactivateTextSizeSlideBar() {
                VboxTextSize.setDisable(true);
                VboxTextSize.setVisible(false);
                VboxTextSize.setManaged(false);
        }

        /**
         * Activate the text field
         */
        public void activateTextSizeSlideBar() {
                VboxTextSize.setDisable(false);
                VboxTextSize.setVisible(true);
                VboxTextSize.setManaged(true);

        }

        /**
         * This function hide the Font charger HBox
         */
        public void desactivateFontCharger() {
                FontChargerHBox.setVisible(false);
                FontChargerHBox.setDisable(true);
                FontChargerHBox.setManaged(false);

        }

        /**
         * This function show the Font charger HBox
         */
        public void activateFontCharger() {
                FontChargerHBox.setVisible(true);
                FontChargerHBox.setDisable(false);
                FontChargerHBox.setManaged(true);

        }

        public void desactivateTextHeighSlideBar() {
                VboxTextHeight.setVisible(false);
                VboxTextHeight.setDisable(true);
                VboxTextHeight.setManaged(false);

        }

        public void activateTextHeighSlideBar() {
                VboxTextHeight.setVisible(true);
                VboxTextHeight.setDisable(false);
                VboxTextHeight.setManaged(true);

        }

        /**
         * Set the Slide bar parameter
         *
         * @param min
         * @param max
         * @param step
         */
        private static void setSlideBar(Slider slideBar, int min, int max, double step) {
                slideBar.setMin(min);
                slideBar.setMax(max);
                slideBar.setMajorTickUnit(step);
        }

        /**
         * set Text Size Slide bar parameter
         *
         * @param min
         * @param max
         * @param step
         */
        public void setTextSizeSlideBar(int min, int max, double step) {
                setSlideBar(this.textSizeSlideBar, min, max, step);
        }

        /**
         * set Text Size Slide bar parameter
         *
         * @param min
         * @param max
         * @param step
         */
        public void setTextHeigthSlideBar(int min, int max, double step) {
                setSlideBar(this.TextHeighSlideBar, min, max, step);
        }

        /**
         * Load a text policie file (.ttf add other if you want)
         */
        public void loadPolicie() {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select Font File");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Font Files", "*.ttf", "*.otf")
                );

                File selectedFile = fileChooser.showOpenDialog(new Stage());
                if (selectedFile != null) {
                        loadNewFont(selectedFile);
                }
        }

        public void loadNewFont(File selectedFile) {
                try {
                        fontFile = selectedFile;
                        // Load the font using AWT
                        java.awt.Font awtFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, selectedFile);
                        awtFont = awtFont.deriveFont((float) 10.0);

                        // Convert AWT font to JavaFX font
                        customFontAwt = awtFont;
                        customFontFX = javafx.scene.text.Font.loadFont(selectedFile.toURI().toString(), 10);

                        if (customFontFX != null) {
                                PolicePreview.setFont(customFontFX);
                                fontChanged = true;

                        }
                        refreshFont();
                        refreshText();
                } catch (FontFormatException | IOException e) {
                        Logger.getLogger(TextInImageGenerator.class.getName()).log(Level.SEVERE, "Exception while loading font: " + e.getMessage());
                }
        }

        public static java.awt.Font changeFontSize(java.awt.Font customFont, double newSize) {
                // Create a new font with the same family but new size
                ///    return new java.awt.Font(customFont.getFamily(),PLAIN , (int) newSize);
                return customFont.deriveFont((float) newSize);

        }

        public File getFontFile() {
                return this.fontFile;
        }

        /**
         * return the text write by the user
         *
         * @return
         */
        public String getTextValue() {
                return this.textField.getText();
        }

        /**
         * return the size of the text selected by the user
         *
         * @return
         */
        public double getTextSizeValue() {
                return this.textSizeSlideBar.getValue();
        }

        /**
         * return the heigth modifier selected by the user
         *
         * @return
         */
        public double getTextHeigthValue() {
                return this.TextHeighSlideBar.getValue();
        }

        public void loadValues(String text, double textSize, double textHeightModifier) {
                this.textField.setText(text);
                this.TextHeighSlideBar.setValue(textHeightModifier);
                this.textSizeSlideBar.setValue(textSize);
        }

                
                
                  /**
         * Generate a BufferedImage with the specified dimensions and an opacity
         * of 0 everywhere except for the text.
         *
         * @param size_factor The factor by which to scale the text size to IN
         * pixel determine the image dimensions.
         * @param textSizeMin
         * @param textSizeMax
         * @return A BufferedImage with the text drawn on it.
         */
        public BufferedImage getImageOut(float size_factor, float textSizeMin, float textSizeMax) {
                return getImageOut(textField.getText(),size_factor,textSizeMin,textSizeMax);
         }
                
                
                
                
                
        /**
         * Generate a BufferedImage with the specified dimensions and an opacity
         * of 0 everywhere except for the text.
         *
         * @param text
         * @param size_factor The factor by which to scale the text size to IN
         * pixel determine the image dimensions.
         * @param textSizeMin
         * @param textSizeMax
         * @return A BufferedImage with the text drawn on it.
         */
        public BufferedImage getImageOut(String text, float size_factor, float textSizeMin, float textSizeMax) {

                // Create a temporary BufferedImage to measure the text size
                BufferedImage tempImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
                Graphics2D tempG2d = tempImage.createGraphics();

                // Set rendering hints for better quality
                tempG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                tempG2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                // Set the font
                double textSize = (textSizeMin + textSizeSlideBar.getValue() * (textSizeMax - textSizeMin)) * size_factor * 72 / 96;
                tempG2d.setFont(changeFontSize(customFontAwt, textSize));

                // Measure the text size
                FontMetrics fontMetrics = tempG2d.getFontMetrics();
                int textWidth = fontMetrics.stringWidth(text);
                if (textWidth == 0) {
                        textWidth = 2;
                }
                int textHeight = fontMetrics.getHeight();

                // Dispose the temporary graphics context
                tempG2d.dispose();

                // Create the final BufferedImage with the calculated dimensions
                BufferedImage image = new BufferedImage((int) (textWidth), (int) (textHeight * 2), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = image.createGraphics();

                // Set rendering hints for better quality
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                // Clear the image with transparent background
                g2d.setComposite(java.awt.AlphaComposite.Clear);
                g2d.fillRect(0, 0, textWidth, textHeight);
                g2d.setComposite(java.awt.AlphaComposite.SrcOver);

                // Set the font and color
                g2d.setFont(changeFontSize(customFontAwt, textSize));
                g2d.setColor(Color.BLACK);

                // Draw the text centered in the image
                g2d.drawString(text, 0, (int) (textHeight));

                // Dispose the graphics context
                g2d.dispose();

                return image;
        }
        
        
        public void setText(String text){
                this.textField.setText(text);
        }
        
        
}
