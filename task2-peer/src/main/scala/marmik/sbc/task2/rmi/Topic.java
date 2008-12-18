package marmik.sbc.task2.rmi;

import java.io.Serializable;

public class Topic implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 8814687055329539494L;
	private String url;
	private String name;


	public Topic() {
		// TODO Auto-generated constructor stub
	}
	public Topic(String url, String name) {
		super();
		this.url = url;
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

  @Override
  public String toString() {
	  return url + " : " + name;
  }

}
