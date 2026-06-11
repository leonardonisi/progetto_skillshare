package it.unibo;

import java.io.Serializable;
//classe che rappresenta la risposta del server al client

@SuppressWarnings("serial")
public class GreetingResponse implements Serializable {
	private String greeting;
	private String serverInfo;
	private String userAgent;

	public String getGreeting() {
		return greeting;
	}

	public void setGreeting(String greeting) {
		this.greeting = greeting;
	}

	public String getServerInfo() {
		return serverInfo;
	}

	public void setServerInfo(String serverInfo) {
		this.serverInfo = serverInfo;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
}