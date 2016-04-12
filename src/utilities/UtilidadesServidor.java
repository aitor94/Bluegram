package utilities;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

public class UtilidadesServidor {
	
	public static XMPPTCPConnection scon;
	
	public static String server="macbook-pro-de-aitor";
	
	public static XMPPTCPConnection ServerConnection (String user,String pass) {
		SmackConfiguration.DEBUG = true;
		XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
				  .setUsernameAndPassword(user,pass)
				  .setServiceName(server+".local")
				  .setHost(server+".local")
				  .setPort(5222)
				  .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
				  .setCompressionEnabled(false)
				  .setSendPresence(false)
				  .build();
		
		XMPPTCPConnection con = new XMPPTCPConnection(config);
		return con;
	}
}
