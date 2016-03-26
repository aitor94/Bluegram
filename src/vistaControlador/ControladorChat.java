package vistaControlador;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import modelo.Contacto;
import utilities.UtilidadesChat;
import utilities.UtilidadesServidor;

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
	
	private List<Contacto> contactos;
	private UtilidadesChat utilidades;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{
		utilidades = new UtilidadesChat();
		contactos = utilidades.getContacts();
		
		ChatManager.getInstanceFor(UtilidadesServidor.scon).addChatListener(new ChatManagerListener() {
            @Override
            public void chatCreated(Chat chat, boolean createdLocally) {
                chat.addMessageListener(new ChatMessageListener() {
                    @Override
                    public void processMessage(Chat chat, Message message) 
                    {
                    	Platform.runLater(() -> {
                    		//aqui pongo el mensaje de forma grafica en la conversacion que corresponda
                    	});
                    }
                });
            }
        });
	}

}
