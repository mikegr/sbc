package marmik.sbc.task2.peer.swt;

import marmik.sbc.task2.peer.swt.model.Posting;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class PostingComposite extends Composite {

  private Text contentText;
  private Text peerText;
  private Text authorText;
  private Text subjectText;
  private Posting posting;
  /**
   * Create the composite
   * @param parent
   * @param style
   */
  public PostingComposite(Composite parent, int style) {
    super(parent, style);
    final GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 4;
    setLayout(gridLayout);

    final Label authorLabel = new Label(this, SWT.NONE);
    authorLabel.setText("Autor:");

    authorText = new Text(this, SWT.READ_ONLY);
    authorText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
    final GridData gd_authorText = new GridData(SWT.FILL, SWT.CENTER, true, false);
    authorText.setLayoutData(gd_authorText);

    final Label peerlLabel = new Label(this, SWT.NONE);
    peerlLabel.setText("Peer:");

    peerText = new Text(this, SWT.READ_ONLY);
    peerText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
    final GridData gd_peerTexxt = new GridData(SWT.FILL, SWT.CENTER, true, false);
    peerText.setLayoutData(gd_peerTexxt);

    final Label subjectLabel = new Label(this, SWT.NONE);
    subjectLabel.setText("Subject:");

    subjectText = new Text(this, SWT.BORDER);
    final GridData gd_subjectText = new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1);
    subjectText.setLayoutData(gd_subjectText);

    contentText = new Text(this, SWT.MULTI | SWT.BORDER);
    final GridData gd_contentText = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1);
    gd_contentText.minimumWidth = 50;
    contentText.setLayoutData(gd_contentText);
    //
  }

  @Override
  protected void checkSubclass() {
    // Disable the check that prevents subclassing of SWT components
  }

  public Posting getInput() {
    return posting;
  }

  public void setInput(Posting posting) {
    this.posting = posting;
    this.authorText.setText(posting.getAuthor());
    this.subjectText.setText(posting.getSubject());
    this.peerText.setText(posting.getTopic().getPeer().getName());
    this.contentText.setText(posting.getContent());
  }

}
