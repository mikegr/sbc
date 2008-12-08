package marmik.sbc.task2.peer.xvsm;

import java.io.Serializable;
import java.util.GregorianCalendar;

public class SpacePosting implements Serializable {

	
	public SpacePosting(String author, 
			String subject, String content, GregorianCalendar date) {
		super();
		this.author = author;
		this.subject = subject;
		this.content = content;
		this.date = date;
	}
	
	public String author;
	public GregorianCalendar date;
	public String subject;
	public String content;
	
	
}
