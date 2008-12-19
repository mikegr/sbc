package marmik.sbc.task2.peer.swt

import java.beans._

trait JavaBean {
  private val changeSupport: PropertyChangeSupport = new PropertyChangeSupport(this)

  def addPropertyChangeListener(propertyName: String, listener: PropertyChangeListener) {
    changeSupport.addPropertyChangeListener(propertyName, listener)
  }
  def removePropertyChangeListener(propertyName: String, listener: PropertyChangeListener) {
    changeSupport.removePropertyChangeListener(propertyName, listener)
  }
}
