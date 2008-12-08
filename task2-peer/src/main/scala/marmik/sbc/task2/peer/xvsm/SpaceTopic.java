package marmik.sbc.task2.peer.xvsm;

import java.io.Serializable;

public class SpaceTopic implements Serializable {

	
	public SpaceTopic(String url, String name) {
		super();
		this.url = url;
		this.name = name;
	}
	
	public String url;
	public String name;
	
}
