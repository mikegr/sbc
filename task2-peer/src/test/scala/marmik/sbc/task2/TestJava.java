package marmik.sbc.task2;

import java.net.URI;

import org.junit.*;

import org.xvsm.core.*;
import org.xvsm.configuration.*;
import org.xvsm.interfaces.ICapi;


public class TestJava {

	@Test
	public void test() throws Exception {
<<<<<<< Updated upstream:task2-peer/src/test/scala/marmik/sbc/task2/TestJava.java
        /*
=======
>>>>>>> Stashed changes:task2-peer/src/test/scala/marmik/sbc/task2/TestJava.java
		System.out.println("TEST");
	    String uriSetting =  "TcpJava.uri";

	    String thisUrl = "tcpjava://localhost:56471";
	    new ConfigurationManager();
	    ConfigurationManager cm = new ConfigurationManager();
	    cm.setStringSetting(uriSetting, thisUrl);
	    Capi thisPeer = new Capi(cm);


	    String otherUrl = "tcpjava://localhost:56472";
	    cm = new ConfigurationManager();
	    cm.setStringSetting(uriSetting, otherUrl);
	    Capi otherPeer = new Capi(cm);


	    String thirdUrl = "tcpjava://localhost:56473";
	    cm = new ConfigurationManager();
	    cm.setStringSetting(uriSetting, thirdUrl);
	    Capi thirdPeer = new Capi(cm);

        thisPeer.createTransaction(new URI(otherUrl), ICapi.INFINITE_TIMEOUT);

<<<<<<< Updated upstream:task2-peer/src/test/scala/marmik/sbc/task2/TestJava.java
        */
=======

>>>>>>> Stashed changes:task2-peer/src/test/scala/marmik/sbc/task2/TestJava.java

	}
}
