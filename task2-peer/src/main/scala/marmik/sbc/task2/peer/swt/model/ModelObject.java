package marmik.sbc.task2.peer.swt.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class ModelObject {

  private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
      this);

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(listener);
  }

  public void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(listener);
  }

  public void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener) {
    propertyChangeSupport.removePropertyChangeListener(propertyName,
        listener);
  }

  protected void firePropertyChange(String propertyName, Object oldValue,
      Object newValue) {
    propertyChangeSupport.firePropertyChange(propertyName, oldValue,
        newValue);
  }
  protected void fireIndexedPropertyChange(String propertyName, int index,
        Object oldValue, Object newValue) {
    propertyChangeSupport.fireIndexedPropertyChange( propertyName,  index,
           oldValue,  newValue);
  }
}
