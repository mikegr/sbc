package marmik.sbc.task2.peer.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class PostingComposite extends Composite {

	private Text text_3;
	private Text text_2;
	private Text text_1;
	private Text text;
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

		final Label titelLabel = new Label(this, SWT.NONE);
		titelLabel.setText("Autor:");

		text_1 = new Text(this, SWT.READ_ONLY);
		text_1.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Label peerlLabel = new Label(this, SWT.NONE);
		peerlLabel.setText("Peer:");

		text_2 = new Text(this, SWT.READ_ONLY);
		text_2.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		final Label titelLabel_1 = new Label(this, SWT.NONE);
		titelLabel_1.setText("Titel:");

		text = new Text(this, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));

		text_3 = new Text(this, SWT.MULTI | SWT.BORDER);
		final GridData gd_text_3 = new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1);
		gd_text_3.minimumWidth = 50;
		text_3.setLayoutData(gd_text_3);
		//
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
