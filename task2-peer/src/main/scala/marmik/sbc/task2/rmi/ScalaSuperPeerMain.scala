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

object ScalaSuperPeerMain {

    def main(args:Array[String]) {
      LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

	  val localUrl = "rmi://" + args(0) + "/superpeer";
	  val rsp:ScalaSuperPeer = new RmiScalaSuperPeer();
	  Naming.rebind(localUrl, rsp);

	  System.out.println("Server ready");
    }

}
