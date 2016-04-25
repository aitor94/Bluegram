package vistaControlador;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Message;

import datos.FicheroXML;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import modelo.Contacto;
import utilities.UtilidadesArchivos;
import utilities.UtilidadesChat;
import utilities.UtilidadesServidor;

public class ControladorConversacion extends Contacto implements Initializable
{
	@FXML private TextArea texto;
	@FXML private Button enviar;
	@FXML private Label contacto;
	@FXML private VBox vbox;
	@FXML private ScrollPane scrollPane;
	@FXML public void handleEnterPressed(KeyEvent event)
	{
	    if (event.getCode() == KeyCode.ENTER && !texto.getText().isEmpty()) 
	    {
	    	enviarMensaje();
	    }
	}	
	@FXML public void handleEnterReleased(KeyEvent event)
	{
	    if (event.getCode() == KeyCode.ENTER) 
	    {
	    	texto.clear();
	    }
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) 
	{		
		enviar.setOnMouseClicked(new EventHandler<MouseEvent>() 
		{
			@Override
			public void handle(MouseEvent event) 
			{
				enviarMensaje();
				texto.clear();
			}

		});
		
		vbox.heightProperty().addListener(new ChangeListener<Number>() 
		{
	        @Override
	        public void changed(ObservableValue<? extends Number> arg0,Number arg1, Number arg2) 
	        {
	            scrollPane.setVvalue(1.0);
	        }
		});
		
		vbox.setOnDragOver(new EventHandler<DragEvent>() 
		{
            @Override
            public void handle(DragEvent event) 
            {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) 
                {
                    event.acceptTransferModes(TransferMode.COPY);
                } 
                else 
                {
                    event.consume();
                }
            }
        });
		
		vbox.setOnDragDropped(new EventHandler<DragEvent>() 
		{						
            @Override
            public void handle(DragEvent event) 
            {
            	HBox hb = new HBox();
    			ProgressIndicator pi = new ProgressIndicator();
    			Text txt = new Text();
    			
    			pi.setProgress(0);
    			pi.setVisible(true);
    			
                Dragboard db = event.getDragboard();
                String filePath = null;
                if (db.hasFiles()) 
                {                  
	        		File file = db.getFiles().get(0);
	                filePath = file.getAbsolutePath();
	                
	                txt.setText("Enviando "+file.getName());
	                
	                hb.getChildren().add(pi);
	                hb.getChildren().add(txt);
	                
	                HBox.setMargin(pi,new Insets(5,5,5,5));
	                HBox.setMargin(txt,new Insets(5,5,5,5));
	                
	                vbox.getChildren().add(hb);
	                
	                Task<?> task = hiloSubida(file.getName(),filePath,pi);
	                
	                task.setOnSucceeded(new EventHandler<WorkerStateEvent>() 
	        		{
	        			@Override
	        			public void handle(WorkerStateEvent t) 
	        			{
	        				txt.setText("Enviado"+file.getName());
	        				hb.getChildren().remove(pi);
	        				t.consume();
	        			}
	        		});
	                
	                new Thread(task).start();
	                System.out.println("hilo lanzado");
                }
                event.consume();
            }
		});
		
	}
	
	public void enviarMensaje()
	{
		Message msg = new Message();

		try 
		{
			Chat chat = ChatManager.getInstanceFor(UtilidadesServidor.scon)
					.createChat(contact.getId());
			msg.setBody(texto.getText());
			msg.setFrom(UtilidadesServidor.scon.getUser());
			msg.setTo(contact.getId());
			msg.setSubject("txt");
			UtilidadesChat.labelGenerator(msg.getBody(), Pos.TOP_LEFT, "paleturquoise");
			contact.addMessage(msg);
			Message ms = new Message(msg.getTo(), msg.getBody());
			ms.setSubject("txt");
			chat.sendMessage(ms);

			FicheroXML.escribeMensaje(msg,
					UtilidadesServidor.scon.getUser().split("@")[0]+contact.getNombre());
			
		} 
		catch (NotConnectedException e) 
		{
			System.out.println("Error al mandar chat");
		}
	}
	
	public Task<Void> hiloSubida(String name, String path,ProgressIndicator pi)
	{
		Task<Void> task = new Task<Void>() 
		{
			@Override
			public Void call() 
			{
				String nam = name;
				String pat = path;
				System.out.println("realizando envio");
				String key = UtilidadesArchivos.sendFile(nam,pat,pi);
                System.out.println("Envio realizado"+key);
                Chat chat = ChatManager.getInstanceFor(UtilidadesServidor.scon)
    					.createChat(contact.getId());
                
                Message msg = new Message();
                msg.setSubject("file/"+nam);//utilizamos este tipo para identificar los archivos
                msg.setFrom(UtilidadesServidor.scon.getUser());
    			msg.setTo(contact.getId());
    			msg.setBody(key);
    			System.out.println(msg);
    			contact.addMessage(msg);
    			Message ms = new Message(msg.getTo(), msg.getBody());
    			ms.setSubject("file/"+nam);
    			try 
    			{
					chat.sendMessage(ms);
				} 
    			catch (NotConnectedException e) 
    			{
					e.printStackTrace();
				}

    			FicheroXML.escribeFichero(contact.getMensajes(),
    					UtilidadesServidor.scon.getUser().split("@")[0]+contact.getNombre());
				return null;
			}
		};	
		return task;
	}
	
	public void setContacto(Label contacto) 
	{
		this.contacto = contacto;
	}

	public TextArea getTexto() 
	{
		return texto;
	}

	public void setTexto(TextArea texto) 
	{
		this.texto = texto;
	}

	public Button getEnviar() 
	{
		return enviar;
	}

	public void setEnviar(Button enviar) 
	{
		this.enviar = enviar;
	}

	public Label getContacto() 
	{
		return contacto;
	}

	public void setContacto(String contacto) 
	{
		this.contacto.setText(contacto);
	}

	public void setMargin(Node node, Insets i) 
	{
		VBox.setMargin(node, i);
	}

	public void addChildren(Node node) 
	{
		vbox.getChildren().add(node);
	}
	
	public void setTitulo(String titulo)
	{
		contacto.setText(titulo);
	}
	
	public VBox getVbox() 
	{
		return vbox;
	}

	public void setVbox(VBox vbox) 
	{
		this.vbox = vbox;
	}

	public void setVSize(double height)
	{
		scrollPane.setVvalue(height);
	}

	private Contacto contact;

	public void setContact(Contacto contacto) 
	{
		super.setId(contacto.getId());
		super.setFriend(contacto.isFriend());
		super.setMensajes(contacto.getMensajes());
		super.setNombre(contacto.getNombre());
		super.setPresencia(contacto.getPresencia());
		super.setSelected(contacto.isSelected());
		this.contact = contacto;
	}

	public Contacto getContact() 
	{
		Contacto contacto = new Contacto();

		contacto.setFriend(super.isFriend());
		contacto.setId(super.getId());
		contacto.setMensajes(super.getMensajes());
		contacto.setNombre(super.getNombre());
		contacto.setPresencia(super.getPresencia());
		contacto.setSelected(super.isSelected());

		return contacto;
	}
}
