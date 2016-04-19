package modelo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Configuracion {
	private String almacenamiento;
	private boolean eliminaHistorial;

	public Configuracion()
	{
		this.almacenamiento="Local";
		this.eliminaHistorial=false;
	}
	@XmlElement
	public String getAlmacenamiento() {
		return almacenamiento;
	}

	public void setAlmacenamiento(String almacenamiento) {
		this.almacenamiento = almacenamiento;
	}
	
	@XmlElement
	public boolean isEliminaHistorial() {
		return eliminaHistorial;
	}
	public void setEliminaHistorial(boolean eliminaHistorial) {
		this.eliminaHistorial = eliminaHistorial;
	}
	

}
