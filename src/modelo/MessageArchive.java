package modelo;

import java.math.BigInteger;

public class MessageArchive {

	private String body;
	private	String stanza;
	private BigInteger sentDate;
	private String toJIDResource;
	private String toJID;
	private String fromJIDResource;
	private String fromJID;
	private BigInteger conversationID;
	private BigInteger messageID;
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getStanza() {
		return stanza;
	}
	public void setStanza(String stanza) {
		this.stanza = stanza;
	}
	public BigInteger getSentDate() {
		return sentDate;
	}
	public void setSentDate(BigInteger sentDate) {
		this.sentDate = sentDate;
	}
	public String getToJIDResource() {
		return toJIDResource;
	}
	public void setToJIDResource(String toJIDResource) {
		this.toJIDResource = toJIDResource;
	}
	public String getToJID() {
		return toJID;
	}
	public void setToJID(String toJID) {
		this.toJID = toJID;
	}
	public String getFromJIDResource() {
		return fromJIDResource;
	}
	public void setFromJIDResource(String fromJIDResource) {
		this.fromJIDResource = fromJIDResource;
	}
	public String getFromJID() {
		return fromJID;
	}
	public void setFromJID(String fromJID) {
		this.fromJID = fromJID;
	}
	public BigInteger getConversationID() {
		return conversationID;
	}
	public void setConversationID(BigInteger conversationID) {
		this.conversationID = conversationID;
	}
	public BigInteger getMessageID() {
		return messageID;
	}
	public void setMessageID(BigInteger messageID) {
		this.messageID = messageID;
	}
	
	
}
