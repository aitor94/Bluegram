package vistaControlador;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

public class ControladorChat implements Initializable
{

	@FXML
	private ListView listaContactos;
	@FXML
	private MenuItem anadir;
	@FXML
	private MenuItem eliminar;
	@FXML
	private AnchorPane panelChat;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		
	}

}
