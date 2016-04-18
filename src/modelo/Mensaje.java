package modelo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Mensaje {
	private String body;
	private String from;
	private String to;
	
	@XmlElement
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	@XmlElement
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	@XmlElement
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
}
