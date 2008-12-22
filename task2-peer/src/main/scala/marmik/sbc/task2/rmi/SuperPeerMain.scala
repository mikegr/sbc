/*
 * ScalaSuperPeerMain.scala
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package marmik.sbc.task2.rmi

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

object SuperPeerMain {

    def setup(url:String):RemoteSuperPeer =  {
      LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

	  val rsp:RemoteSuperPeer = new RemoteSuperPeerImpl(url);
	  Naming.rebind(url, rsp);

      rsp;

    }
    def main(args:Array[String]) {
      val localUrl = "rmi://" + args(0) + "/superpeer";
      setup(localUrl);
	  System.out.println("Server ready");
    }

}
