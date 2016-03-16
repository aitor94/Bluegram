package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class Main extends Application 
{
	
	@Override
	public void start(Stage primaryStage) {
		try 
		{
			FXMLLoader loader=new FXMLLoader();
			loader.setLocation(Main.class.getResource("/vistaControlador/Principal.fxml"));
			AnchorPane rootLayout = loader.load();
			Scene scene=new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Bluegram v1.0");
			primaryStage.show();
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
