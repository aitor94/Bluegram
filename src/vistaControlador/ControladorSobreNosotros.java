package vistaControlador;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import utilities.UtilidadesOtros;

public class ControladorSobreNosotros implements Initializable
{
	@FXML
	private Button boton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		boton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				UtilidadesOtros.ventanaFXML("/vistaControlador/Principal.fxml", boton.getScene());
			}
		});
	}

}
