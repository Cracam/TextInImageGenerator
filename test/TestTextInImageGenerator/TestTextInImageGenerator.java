/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TestTextInImageGenerator;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.stage.Stage;
import textinimagegenerator.TextInImageGenerator;




public class TestTextInImageGenerator extends Application  {  
        
        @Override
         public void start(Stage primaryStage) {
                  

                  TextInImageGenerator gradientInterface = new TextInImageGenerator();
                  Scene scene = new Scene(gradientInterface);
                  primaryStage.setTitle("Gradient Interface Test");
                  primaryStage.setScene(scene);
                  primaryStage.show();
         }

         public static void main(String[] args) {
                  launch(args);
         }
}
