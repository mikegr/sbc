package marmik.sbc.task2.peer.swt;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.IViewerObservableValue;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import java.util.LinkedList;

public class WordFilterDialog extends Dialog {
  private WritableList wordList;
  private TableViewer tableViewer;
  private IViewerObservableValue wordSelection;
  private Button addButton;
  private Button removeButton;

  public java.util.List<String> getInput() {
    LinkedList<String> result = new LinkedList<String>();
    for (Object o : wordList) {
      result.add((String) o);
    }
    return result;
  }

  class TableLabelProvider extends LabelProvider {
    public String getColumnText(Object element, int columnIndex) {
      return element.toString();
    }

    public Image getColumnImage(Object element, int columnIndex) {
      return null;
    }
  }

  class CellModifier implements ICellModifier {
    public boolean canModify(Object element, String property) {
      return false;
    }

    public Object getValue(Object element, String property) {
      return null;
    }

    public void modify(Object element, String property, Object value) {
    }
  }

  private Table table;

  public WordFilterDialog(Shell parentShell) {
    super(parentShell);
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    Composite container = (Composite) super.createDialogArea(parent);
    final GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 2;
    container.setLayout(gridLayout);

    Label pleaseSelectLabel;
    pleaseSelectLabel = new Label(container, SWT.NONE);
    final GridData gd_pleaseSelectLabel = new GridData(SWT.FILL, SWT.CENTER, true, false);
    pleaseSelectLabel.setLayoutData(gd_pleaseSelectLabel);
    pleaseSelectLabel.setText("Please select");
    new Label(container, SWT.NONE);

    tableViewer = new TableViewer(container, SWT.MULTI | SWT.BORDER);
    tableViewer.setSorter(new ViewerSorter());
    tableViewer.setLabelProvider(new TableLabelProvider());
    tableViewer.setCellModifier(new CellModifier());
    table = tableViewer.getTable();
    table.setLinesVisible(true);
    table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));

    addButton = new Button(container, SWT.NONE);
    final GridData gd_addButton = new GridData(SWT.FILL, SWT.TOP, false, false);
    addButton.setLayoutData(gd_addButton);
    addButton.setText("Add...");
    addButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        InputDialog d = new InputDialog(WordFilterDialog.this.getShell(), "Add Stopword", "Word:", null, null);
        if (d.open() == Window.OK)
          wordList.add(d.getValue());
      }
    });

    removeButton = new Button(container, SWT.NONE);
    final GridData gd_removeButton = new GridData(SWT.FILL, SWT.TOP, false, false);
    removeButton.setLayoutData(gd_removeButton);
    removeButton.setText("Remove...");
    removeButton.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        wordList.remove(wordSelection.getValue());
      }
    });
    //
    return container;
  }

  public void setInput(java.util.List<String> wordList) {
    this.wordList = new WritableList(new LinkedList(wordList), String.class);
    tableViewer.setContentProvider(new ObservableListContentProvider());
    tableViewer.setInput(this.wordList);
    wordSelection = ViewersObservables.observeSingleSelection(tableViewer);
    IObservableValue wordSelected = new ComputedValue(Boolean.TYPE) {
      protected Object calculate() {
        return Boolean.valueOf(wordSelection.getValue() != null);
      }
    };
    DataBindingContext bindingContext = new DataBindingContext();
    bindingContext.bindValue(SWTObservables.observeEnabled(removeButton), wordSelected, null, null);
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
  }

  @Override
  protected Point getInitialSize() {
    return new Point(500, 375);
  }

}
