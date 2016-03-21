package main;

import javafx.application.Application;
import javafx.stage.Stage;
import utilities.UtilidadesOtros;
import javafx.scene.Scene;

public class Main extends Application 
{
	
	@Override
	public void start (Stage primaryStage) {
		
		Scene scene = UtilidadesOtros.escenaFXML("/vistaControlador/Principal.fxml");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Bluegram v1.0");
		primaryStage.show();
	}
	
	public static void main (String[] args) {
		launch(args);
	}
}
