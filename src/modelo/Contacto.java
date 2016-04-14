package modelo;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.packet.Message;

public class Contacto 
{
	private String id;
	private String nombre;
	private String presencia;
	private List<Message> mensajes;
	private boolean isFriend;
	private boolean isSelected;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPresencia() {
		return presencia;
	}
	public void setPresencia(String presencia) {
		this.presencia = presencia;
	}
	public List<Message> getMensajes() {
		return mensajes;
	}
	public void setMensajes(List<Message> mensajes) {
		this.mensajes = mensajes;
	}
	
	public void addMessage(Message mensaje)
	{
		if(mensajes==null)
			mensajes=new ArrayList<Message>();
		mensajes.add(mensaje);
	}
	public boolean isFriend() {
		return isFriend;
	}
	public void setFriend(boolean isFriend) {
		this.isFriend = isFriend;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}
