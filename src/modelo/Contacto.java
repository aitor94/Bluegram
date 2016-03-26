package modelo;

import org.jivesoftware.smack.chat.Chat;

public class Contacto 
{
	private String id;
	private String nombre;
	private String presencia;
	private Chat chat;
	
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
	public Chat getChat() {
		return chat;
	}
	public void setChat(Chat chat) {
		this.chat = chat;
	}
}
