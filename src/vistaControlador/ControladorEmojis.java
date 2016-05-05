package vistaControlador;

import java.net.URL;
import java.util.ResourceBundle;

import datos.Emoji;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ControladorEmojis implements Initializable
{
	@FXML private VBox vbox;
	
	private TextField texto;
	private Stage stage;

	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{	
		Emoji[] emojis = Emoji.values();
		int count = 0;
		
		for(Node hb:vbox.getChildren())
		{
			HBox hbox = (HBox) hb;
			
			for(Node bt:hbox.getChildren())
			{
				Button btn = (Button) bt;
				btn.setFont(Font.loadFont("file:fonts/OpenSansEmoji.ttf", 13));
				btn.setText(emojis[count].toString());
				
				btn.setOnMouseClicked(new EventHandler<MouseEvent>() 
				{
					@Override
					public void handle(MouseEvent event) 
					{
						texto.setText(texto.getText()+btn.getText());
					}

				});
				
				count++;
			}
		}
	}

	public TextField getTexto() {
		return texto;
	}

	public void setTexto(TextField texto) {
		this.texto = texto;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
}
