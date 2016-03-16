package utilities;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

public class UtilidadesServidor 
{
	public static XMPPTCPConnection ServerConnection(String user,String pass)
	{
		SmackConfiguration.DEBUG = true;
		XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
				  .setUsernameAndPassword(user,pass)
				  .setServiceName("MacBook-Pro-de-aitor.local")
				  .setHost("MacBook-Pro-de-aitor.local")
				  .setPort(5222)
				  .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
				  .setCompressionEnabled(false)
				  .setSendPresence(true)
				  .build();
		
		
		XMPPTCPConnection con = new XMPPTCPConnection(config);
		return con;		
	}
}
