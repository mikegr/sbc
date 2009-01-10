package marmik.sbc.task2.peer.swt;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import marmik.sbc.task2.peer.swt.PostingComposite;
import marmik.sbc.task2.peer.swt.model.Posting;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class PostingDialog extends Dialog {

  private Composite container;
  private Posting posting;
  private PostingComposite postingComposite;
  private String content;
  private String subject;
  private boolean modified;
  private boolean subjectEditable;

  public PostingDialog(Shell parentShell) {
    this(parentShell, false);
  }

  public PostingDialog(Shell parentShell, boolean subjectEditable) {
    super(parentShell);
    this.subjectEditable = subjectEditable;
  }

  public void setInput(Posting posting) {
    this.posting = posting;
    this.postingComposite.setInput(posting);
    this.postingComposite.setEditable(true);
  }

  public void setContentEditable(boolean editable) {
    this.postingComposite.setEditable(editable);
  }

  public String getSubject() {
    return subject;
  }

  public String getContent() {
    return content;
  }

  public boolean isModified() {
    return modified;
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    container = (Composite) super.createDialogArea(parent);
    final GridLayout gridLayout = new GridLayout();
    gridLayout.marginWidth = 0;
    gridLayout.marginHeight = 0;
    container.setLayout(gridLayout);

    postingComposite = new PostingComposite(container, SWT.NONE, subjectEditable);
    postingComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    //
    return container;
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

  protected void buttonPressed(int buttonId) {
    if (buttonId == IDialogConstants.CANCEL_ID) {
      if (posting != null) {
        this.subject = posting.getSubject();
        this.content = posting.getContent();
      }
      modified = false;
    }
    if (buttonId == IDialogConstants.OK_ID) {
      this.subject = postingComposite.getSubject();
      this.content = postingComposite.getContent();
      if (posting != null) {
        boolean subjectModified = !(this.subject.equals(posting.getSubject()));
        boolean contentModified = !(this.content.equals(posting.getContent()));
        modified = subjectModified || contentModified;
      } else {
        modified = true;
      }
    }
    super.buttonPressed(buttonId);
  }

}
