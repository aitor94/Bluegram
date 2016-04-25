package utilities;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

public class UtilidadesServidor {
	
	public static XMPPTCPConnection scon;
	
	public static XMPPTCPConnection ServerConnection (String user,String pass) {
		//SmackConfiguration.DEBUG = true;
		SSLContext ssl=null;
		try {
			ssl = SSLContext.getInstance("TLS");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TrustManager tm = new X509TrustManager() 
		{
			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}           
        };
        
        try {
			ssl.init(null, new TrustManager[] {tm}, null);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
        
		XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
				  .setUsernameAndPassword(user,pass)
				  .setServiceName(Constantes.serviceName)
				  .setHost(Constantes.host)
				  .setPort(5222)
				  .setSecurityMode(ConnectionConfiguration.SecurityMode.required)
				  .setCustomSSLContext(ssl)
				  .setCompressionEnabled(false)
				  .setSendPresence(false)
				  .build();
		
		XMPPTCPConnection con = new XMPPTCPConnection(config);
		return con;
	}
}
