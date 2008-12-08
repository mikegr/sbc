package marmik.sbc.task2.peer.xvsm;

import java.io.Serializable;

/**
 * 
 * @author mike
 */
public class SpaceLocalTopic implements Serializable {
	public SpaceLocalTopic(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	private static final long serialVersionUID = 1L;
	
	public Long id;
	public String name;
}
