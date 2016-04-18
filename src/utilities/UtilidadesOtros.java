package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Main;

public class UtilidadesOtros {

	public static Scene escenaFXML(String url) {

		Scene scene = null;

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource(url));
			AnchorPane ap = loader.load();
			scene = new Scene(ap);
		}

		catch (Exception e) {
			UtilidadesOtros.alerta(AlertType.ERROR, "Error", "Error al cargar la ventana");
			e.printStackTrace();
		}

		return scene;
	}

	public static void ventanaFXML(String url, Scene escena) {
		Scene scene = escenaFXML(url);
		Stage stg = (Stage) escena.getWindow();
		stg.setScene(scene);
		stg.show();
	}

	public static void alerta(AlertType tipo, String titulo, String texto) {

		Alert al = new Alert(tipo);
		al.setTitle(titulo);
		al.setHeaderText(null);
		al.setContentText(texto);
		al.showAndWait();
	}

	public static void guardarEnFichero(String selected, String user, String pass) {

		try (BufferedWriter bw = new BufferedWriter(new FileWriter("userpass.txt"))) {

			bw.write(selected + "\n" + user + "\n" + pass);

		} catch (IOException e) {

		}
	}

	public static String[] leerDeFichero() {

		if (new File("userpass.txt").isFile() == false)
			return null;

		String userpass[] = new String[3];

		try (BufferedReader br = new BufferedReader(new FileReader("userpass.txt"))) {

			userpass[0] = br.readLine();
			userpass[1] = br.readLine();
			userpass[2] = br.readLine();

		} catch (IOException e) {

			userpass = null;
		}
		return userpass;
	}
}
