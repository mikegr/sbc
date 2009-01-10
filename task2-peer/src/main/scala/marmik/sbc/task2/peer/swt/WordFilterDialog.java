package marmik.sbc.task2.peer.swt;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

public class WordFilterDialog extends Dialog {

  class Sorter extends ViewerSorter {
    public int compare(Viewer viewer, Object e1, Object e2) {
      Object item1 = e1;
      Object item2 = e2;
      return 0;
    }
  }
  class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
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

    final TableViewer tableViewer = new TableViewer(container, SWT.MULTI | SWT.BORDER);
    tableViewer.setSorter(new Sorter());
    tableViewer.setLabelProvider(new TableLabelProvider());
    tableViewer.setCellModifier(new CellModifier());
    table = tableViewer.getTable();
    table.setLinesVisible(true);
    table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));

    final Button addButton = new Button(container, SWT.NONE);
    final GridData gd_addButton = new GridData(SWT.FILL, SWT.TOP, false, false);
    addButton.setLayoutData(gd_addButton);
    addButton.setText("Add...");

    final Button removeButton = new Button(container, SWT.NONE);
    final GridData gd_removeButton = new GridData(SWT.FILL, SWT.TOP, false, false);
    removeButton.setLayoutData(gd_removeButton);
    removeButton.setText("Remove...");
    //
    return container;
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
        true);
    createButton(parent, IDialogConstants.CANCEL_ID,
        IDialogConstants.CANCEL_LABEL, false);
  }

  @Override
  protected Point getInitialSize() {
    return new Point(500, 375);
  }

}
