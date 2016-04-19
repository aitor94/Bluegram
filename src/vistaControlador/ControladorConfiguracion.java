package vistaControlador;

import java.net.URL;
import java.util.ResourceBundle;

import datos.FicheroXML;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseEvent;
import modelo.Configuracion;
import utilities.UtilidadesOtros;

public class ControladorConfiguracion implements Initializable {

	@FXML
	private ChoiceBox<String> almacenamientoSelect;
	@FXML
	private ChoiceBox<String> eliminaHistorial;
	@FXML
	private Button guardar;
	@FXML
	private Button descartar;

	private Configuracion config;

	public Configuracion getConfig() {
		return config;
	}

	public void setConfig(Configuracion configu) {
		this.config = configu;
	}

	public ControladorConfiguracion() {

		this.config = cargarConfiguracion();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		config = cargarConfiguracion();
		almacenamientoSelect.setItems(FXCollections.observableArrayList("Online", "Local"));
		eliminaHistorial.setItems(FXCollections.observableArrayList("Si", "No"));

		if (config.getAlmacenamiento() != null)
			almacenamientoSelect.getSelectionModel().select(config.getAlmacenamiento());
		if(config.isEliminaHistorial()==true)
			eliminaHistorial.getSelectionModel().select("Si");
		else
			eliminaHistorial.getSelectionModel().select("No");

		guardar.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				guardar();
				UtilidadesOtros.ventanaFXML("/vistaControlador/Chat.fxml", almacenamientoSelect.getScene());
				event.consume();
			}
		});

		descartar.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				UtilidadesOtros.ventanaFXML("/vistaControlador/Chat.fxml", almacenamientoSelect.getScene());
				event.consume();
			}
		});

	}

	private void guardar() {
		config.setAlmacenamiento(almacenamientoSelect.getSelectionModel().getSelectedItem());

		switch (eliminaHistorial.getSelectionModel().getSelectedItem()) {
		case ("Si"): {
			config.setEliminaHistorial(true);
			break;
		}
		case ("No"): {
			config.setEliminaHistorial(false);
			break;
		}
		}
		FicheroXML.escribeConfig(config);
	}

	private Configuracion cargarConfiguracion() {
		Configuracion config = FicheroXML.leeConfig();
		if (config == null)
			config = new Configuracion();
		return config;
	}
}
