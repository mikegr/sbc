package marmik.sbc.task2.peer.swt;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;
import java.util.List;

import marmik.sbc.task2.peer.swt.model.SessionFactory;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class LoginDialog extends Dialog {
  private ComboViewer serviceComboViewer;
  private ComboViewer urlComboViewer;
  private DataBindingContext bindingContext;
  private Text peerNameText;
  private Combo urlCombo;
  private Combo serviceCombo;
  private Model model;
  private List<SessionFactory> factories;

  static public class Model {
    private String url, name;
    private SessionFactory factory;
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener l) {
      changeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
      changeSupport.removePropertyChangeListener(l);
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      Object oldValue = this.url;
      this.url = url;
      changeSupport.firePropertyChange("property", oldValue, this.url);
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      Object oldValue = this.name;
      this.name = name;
      changeSupport.firePropertyChange("property", oldValue, this.name);
    }

    public SessionFactory getFactory() {
      return factory;
    }

    public void setFactory(SessionFactory factory) {
      Object oldValue = this.factory;
      this.factory = factory;
      changeSupport.firePropertyChange("property", oldValue, this.factory);
    }
  }

  public LoginDialog(Shell parentShell, Model model, List<SessionFactory> factories) {
    super(parentShell);
    this.model = model;
    this.factories = factories;
  }

  public LoginDialog(Shell parentShell, List<SessionFactory> factories) {
    this(parentShell, new Model(), factories);
  }

  public LoginDialog(Shell parentShell) {
    this(parentShell, new Model(), new LinkedList<SessionFactory>());
  }

  public Model getModel() {
    return model;
  }

  public List<SessionFactory> getSessionFactories() {
    return factories;
  }

  /**
   * Create contents of the dialog
   *
   * @param parent
   */
  @Override
  protected Control createDialogArea(Composite parent) {
    Composite container = (Composite) super.createDialogArea(parent);
    final GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 2;
    container.setLayout(gridLayout);

    final Label messageLabel = new Label(container, SWT.NONE);
    final GridData gd_messageLabel = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
    gd_messageLabel.minimumWidth = 350;
    messageLabel.setLayoutData(gd_messageLabel);
    messageLabel.setText("Bitte melden Sie sich an.");

    final Label nameLabel = new Label(container, SWT.NONE);
    nameLabel.setLayoutData(new GridData());
    nameLabel.setText("Name:");

    peerNameText = new Text(container, SWT.BORDER);
    final GridData gd_peerNameText = new GridData(SWT.FILL, SWT.CENTER, true, false);
    peerNameText.setLayoutData(gd_peerNameText);

    final Label urlLabel = new Label(container, SWT.NONE);
    urlLabel.setText("URL:");

    urlComboViewer = new ComboViewer(container, SWT.BORDER);
    urlCombo = urlComboViewer.getCombo();
    final GridData gd_urlCombo = new GridData(SWT.FILL, SWT.CENTER, true, false);
    urlCombo.setLayoutData(gd_urlCombo);

    final Label serviceLabel = new Label(container, SWT.NONE);
    serviceLabel.setText("Service:");

    serviceComboViewer = new ComboViewer(container, SWT.READ_ONLY);
    serviceCombo = serviceComboViewer.getCombo();
    final GridData gd_serviceCombo = new GridData(SWT.FILL, SWT.CENTER, true, false);
    serviceCombo.setLayoutData(gd_serviceCombo);
    //
    return container;
  }

  /**
   * Create contents of the button bar
   *
   * @param parent
   */
  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.OK_ID, "Anmelden", true);
    createButton(parent, IDialogConstants.CANCEL_ID, "Beenden", false);
    bindingContext = initDataBindings();
  }

  @Override
  protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    newShell.setText("Anmelden");
  }

  protected DataBindingContext initDataBindings() {
    // IObservableValue urlComboTextObserveValue = BeansObservables.observeValue(urlCombo, "text");
    // IObservableValue serviceComboTextObserveWidget = SWTObservables.observeText(serviceCombo, SWT.Modify);
    //
    //
    DataBindingContext bindingContext = new DataBindingContext();
    //
    // bindingContext.bindValue(serviceComboTextObserveWidget, urlComboTextObserveValue, null, null);

    ObservableListContentProvider serviceComboViewerContentProvider = new ObservableListContentProvider();
    serviceComboViewer.setContentProvider(serviceComboViewerContentProvider);

    // And a standard label provider that maps columns
    IObservableMap[] attributeMaps = BeansObservables.observeMaps(
        serviceComboViewerContentProvider.getKnownElements(), SessionFactory.class,
        new String[] { "name" });
    serviceComboViewer.setLabelProvider(new ObservableMapLabelProvider(attributeMaps));

    // Now set the Viewer's input
    serviceComboViewer.setInput(new WritableList(factories, SessionFactory.class));

    bindingContext.bindValue(ViewersObservables.observeSingleSelection(serviceComboViewer), BeansObservables.observeValue(model, "factory"), null, null);
    bindingContext.bindValue(SWTObservables.observeText(urlCombo), BeansObservables.observeValue(model, "url"), null, null);
    bindingContext.bindValue(SWTObservables.observeText(peerNameText, SWT.Modify), BeansObservables.observeValue(model, "name"), null, null);
    //
    return bindingContext;
  }

}
