package textinimagegenerator;

import Exeptions.ResourcesFileErrorException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Camille LECOURT
 */
public class TextInImageGenerator extends HBox {

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

        private final BooleanProperty changed = new SimpleBooleanProperty(false);

        private Font customFont =new Font("Arial", 14);
        private double oldFontSize = 14.0;
        private boolean fontChanged = false;
        
        private static final String PLACEHOLDER_TEXT="Veuillez Remplacer ce Texte";
        
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
                setTextSizeSlideBar(5,40,1);
                textSizeSlideBar.setValue(12.0);
                
                 TextHeighSlideBar.valueProperty().addListener((observable, oldValue, newValue) -> {
                });
                 setTextHeigthSlideBar(-1,1,  0.05);
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
        }

        /**
         * Refrash the font and the size of the text
         */
        private void refreshFont() {
                if (customFont != null & (fontChanged == true | (oldFontSize != textSizeSlideBar.getValue()))) {
                        if (oldFontSize != textSizeSlideBar.getValue()) {
                                customFont = changeFontSize(customFont, textSizeSlideBar.getValue());
                        }
                        oldFontSize = textSizeSlideBar.getValue();
                        PolicePreview.setFont(customFont);
                        fontChanged = false;
                }
        }

        public void setChanged(boolean value) {
                this.changed.set(value);
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
                textSizeSlideBar.setDisable(true);
        }

        /**
         * Activate the text field
         */
        public void activateTextSizeSlideBar() {
                textSizeSlideBar.setDisable(false);
        }

        /**
         * Desactivate thePoliceSelector
         */
        public void desactivatePoliceSelector() {
                fontSelectorButton.setDisable(true);
        }

        /**
         * Activate the PoliceSelector
         */
        public void activatePoliceSelector() {
                fontSelectorButton.setDisable(false);
        }

        /**
         * Set the Slide bar parameter
         *
         * @param min
         * @param max
         * @param step
         */
        private static void setSlideBar(Slider slideBar ,int min, int max, double step) {
                slideBar.setMin(min);
                slideBar.setMax(max);
                slideBar.setMajorTickUnit(step);
        }
        
        /**
         * set Text Size Slide bar parameter 
         * @param min
         * @param max
         * @param step 
         */
        public void setTextSizeSlideBar(int min, int max, double step){
                setSlideBar(this.textSizeSlideBar,min,max,step);
        }
        
            /**
         * set Text Size Slide bar parameter 
         * @param min
         * @param max
         * @param step 
         */
        public void setTextHeigthSlideBar(int min, int max, double step){
                setSlideBar(this.TextHeighSlideBar,min,max,step);
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
                        try {
                                fontFile=selectedFile;
                                //    System.out.println(selectedFile.toURL().toString());
                                customFont = Font.loadFont(selectedFile.toURL().toString(), textSizeSlideBar.getValue());
                                if (customFont != null) {
                                        PolicePreview.setFont(customFont);
                                        fontChanged = true;
                                        refreshFont();
                                        refreshText();
                                }
                        } catch (MalformedURLException e) {
                                Logger.getLogger(TextInImageGenerator.class.getName()).log(Level.SEVERE, "Exception while loading font: " + e.getMessage());
                        }
                }
        }

        public static Font changeFontSize(Font customFont, double newSize) {
                // Create a new font with the same family but new size
                return new Font(customFont.getFamily(), newSize);
        }
        
        
        public File getFontFile(){
                return this.fontFile;
        }
       
        /**
         * return the text write by the user
         * @return 
         */
        public String getTextValue(){
                return this.textField.getText();
        }
        
        /**
         * return the size of the text selected by the user
         * @return 
         */
        public double getTextSizeValue(){
                return this.textSizeSlideBar.getValue();
        }
        
        /**
         * return the heigth modifier selected by the user
         * @return 
         */
        public double getTextHeigthValue(){
                return this.TextHeighSlideBar.getValue();
        }
}
