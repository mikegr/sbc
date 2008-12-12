package marmik.sbc.task2.peer.swt;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ComboViewer;
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
  private DataBindingContext m_bindingContext;
  private Binding testBinding;
  private Text peerNameText;
  private Combo urlCombo;
  private Combo serviceCombo;

  /**
   * Create the dialog
   * 
   * @param parentShell
   */
  public LoginDialog(Shell parentShell) {
    super(parentShell);
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

    final ComboViewer urlComboViewer = new ComboViewer(container, SWT.BORDER);
    urlCombo = urlComboViewer.getCombo();
    final GridData gd_urlCombo = new GridData(SWT.FILL, SWT.CENTER, true, false);
    urlCombo.setLayoutData(gd_urlCombo);

    final Label serviceLabel = new Label(container, SWT.NONE);
    serviceLabel.setText("Service:");

    final ComboViewer serviceComboViewer = new ComboViewer(container, SWT.READ_ONLY);
    serviceCombo = serviceComboViewer.getCombo();
    serviceCombo.setItems(new String[] { "XVSM", "Socket" });
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
    m_bindingContext = initDataBindings();
  }

  @Override
  protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    newShell.setText("Anmelden");
  }

  protected DataBindingContext initDataBindings() {
    IObservableValue peerNameTextTextObserveWidget = SWTObservables.observeText(peerNameText, SWT.Modify);
    IObservableValue urlComboTextObserveValue = BeansObservables.observeValue(urlCombo, "text");
    //
    //
    DataBindingContext bindingContext = new DataBindingContext();
    //
    testBinding = bindingContext.bindValue(peerNameTextTextObserveWidget, urlComboTextObserveValue, null, null);
    //
    return bindingContext;
  }

}
