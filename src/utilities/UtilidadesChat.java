package utilities;

import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.SmackException.NotLoggedInException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smackx.offline.OfflineMessageManager;

import datos.FicheroXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import modelo.Contacto;
import vistaControlador.ControladorChat;
import vistaControlador.ControladorConfiguracion;

public class UtilidadesChat {
	public Map<String, Contacto> getContacts() {
		Roster roster = Roster.getInstanceFor(UtilidadesServidor.scon);
		
		if(!roster.isLoaded())
		{
			try {
				roster.reloadAndWait();
			} catch (NotLoggedInException e) {
				e.printStackTrace();
			} catch (NotConnectedException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		Map<String, Contacto> contactos = new HashMap<String, Contacto>();
		Contacto cc;
		ControladorConfiguracion config = new ControladorConfiguracion();

		for (RosterEntry entry : roster.getEntries()) {
			cc = new Contacto();
			cc.setFriend(true);
			cc.setSelected(false);
			cc.setId(entry.getUser() + "@" + Constantes.serviceName);
			cc.setNombre(entry.getName());
			cc.setPresencia(roster.getPresence(entry.getUser()).toString());

			switch (config.getConfig().getAlmacenamiento()) {
			case ("Online"): {
				cc.setMensajes(UtilidadesConversacion.getOnlineHistory(new BD(), UtilidadesServidor.scon.getUser().split("/")[0],
						entry.getUser()+"@"+Constantes.host));
				break;
			}
			case ("Local"): {
				cc.setMensajes(FicheroXML.leeFichero(entry.getUser()));
				break;
			}
			}

			contactos.put(cc.getNombre(), cc);
		}

		return contactos;
	}

	public void anadirContacto() {
		Roster roster = Roster.getInstanceFor(UtilidadesServidor.scon);
		System.out.println("Boton añadir del menu pulsado");
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Añadir contacto");
		dialog.setContentText("Inserte el ID del contacto:");

		Optional<String> result = dialog.showAndWait();

		if (new BD().buscaUsuario(result.get())) {
			try {
				roster.createEntry(result.get(), result.get(), null);
				roster.reload();
			} catch (NoResponseException e) {
				System.out.println("Error de no respuesta");
			} catch (XMPPErrorException e) {
				System.out.println("Error de XMPP");
			} catch (NotConnectedException e) {
				System.out.println("Error de no conexion");
			} catch (NotLoggedInException e) {
				System.out.println("Error de no logeado");
			}

		} else {
			UtilidadesOtros.alerta(AlertType.INFORMATION, "Aviso",
					"El usuario introducido no esta registrado en la aplicacion");
		}

	}

	public void eliminarContacto(String string) {

		Roster roster = Roster.getInstanceFor(UtilidadesServidor.scon);

		RosterEntry re = roster.getEntry(string);
		try {
			roster.removeEntry(re);
			roster.reload();
		} catch (NoResponseException e) {
			System.out.println("Error de no respuesta");
		} catch (XMPPErrorException e) {
			System.out.println("Error de XMPP");
		} catch (NotConnectedException e) {
			System.out.println("Error de no conexion");
		} catch (NotLoggedInException e) {
			System.out.println("Error de no logeado");
		}
	}

	public List<Message> getOfflineMessages() throws NoResponseException, XMPPErrorException, NotConnectedException {
		OfflineMessageManager omm = new OfflineMessageManager(UtilidadesServidor.scon);
		List<Message> mensajes = null;

		if (omm.getMessageCount() == 0) {
			mensajes = new ArrayList<Message>();
			System.out.println("0 mensajes offline");
		} else {
			mensajes = omm.getMessages();
			omm.deleteMessages();
			System.out.println("varios mensajes offline->" + mensajes.size());
		}

		return mensajes;
	}

	public void asignaMensajes(List<Message> mensajesOff) {
		for (Message mensaje : mensajesOff) {
			Contacto cto = ControladorChat.contactos.get(mensaje.getFrom());

			if (cto != null) {
				cto.addMessage(mensaje);
			} else {
				cto = new Contacto();
				cto.setNombre(mensaje.getFrom());
				cto.setId(mensaje.getFrom());
				cto.setFriend(false);
				cto.setMensajes(new ArrayList<Message>() {
				{
						add(mensaje);
				}
				});
			}
			ControladorChat.contactos.put(cto.getId(), cto);
		}
		mensajesOff.clear();
	}

	public static void labelGenerator(String texto, Pos pos, String color) {
		StackPane pane = new StackPane();
		Text txt = new Text();
		HBox hbox = new HBox();

		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		FontMetrics fm = img.getGraphics().getFontMetrics();
		double width = fm.stringWidth(texto);

		pane.getChildren().add(txt);
		txt.setText(texto);
		txt.setFont(Font.font("Verdana", 11));

		if (width > 150) {
			txt.setWrappingWidth(150);
			pane.setMaxWidth(150);
			pane.setPrefWidth(150);
		} else {
			pane.setMaxWidth(width);
			pane.setPrefWidth(width);
		}

		pane.setStyle(
				"-fx-background-color: " + color + "; -fx-background-radius: 5; -fx-border-radius: 5; -fx-padding: 5;");
		StackPane.setAlignment(txt, pos);
		pane.setPadding(new Insets(5, 5, 5, 5));

		hbox.getChildren().add(pane);
		hbox.setAlignment(pos);

		ControladorChat.conversacionActual.setMargin(hbox, new Insets(5, 5, 5, 5));
		ControladorChat.conversacionActual.addChildren(hbox);
	}
}
