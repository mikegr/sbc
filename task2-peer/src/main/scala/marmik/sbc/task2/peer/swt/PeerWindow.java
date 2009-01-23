package marmik.sbc.task2.peer.swt;

import marmik.sbc.task2.peer.swt.model.*;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.ComputedValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.slf4j.Logger;

public class PeerWindow extends org.eclipse.jface.window.ApplicationWindow {

  private Logger log = org.slf4j.LoggerFactory.getLogger(PeerWindow.class);
  private DataBindingContext bindingContext;
  private Button editButton;
  private Button replyButton;
  private Button topicButton;
  private Button addPostingButton;
  private Button subscribeButton;
  private PostingComposite postingComposite;
  private TableViewer tableViewer;
  private Tree tree;
  private Table table;
  private Session session;
  private TreeViewer treeViewer;

  class Sorter extends ViewerSorter {

    @Override
    public int category(Object element) {
      if (element instanceof SidebarEntry) {
        SidebarEntry e = (SidebarEntry) element;
        if (e instanceof Topic) {
          Topic t = (Topic) e;
          return t.getPeer().hashCode();
        } else {
          Peer p = (Peer) e;
          return p.hashCode();
        }
      } else
        throw new IllegalArgumentException("Can only handle SidebarEntry, not " + element.getClass().getName());
    }

  }

  /**
   * Create the application window
   */
  protected PeerWindow() {
    super(null);
    createActions();
    JFaceResources.getColorRegistry().put(JFacePreferences.COUNTER_COLOR, new RGB(0, 127, 174));
    JFaceResources.getColorRegistry().put("sidebar.local_peer",
        Display.getDefault().getSystemColor(SWT.COLOR_DARK_GREEN).getRGB());
    addToolBar(SWT.FLAT | SWT.WRAP);
    addMenuBar();
    addStatusLine();
  }

  public PeerWindow(Session session) {
    this();
    this.session = session;
  }

  /**
   * Create contents of the application window
   *
   * @param parent
   */
  @Override
  protected Control createContents(Composite parent) {
    Composite container = new Composite(parent, SWT.NONE);
    final GridLayout gridLayout = new GridLayout();
    container.setLayout(gridLayout);

    final Composite composite_1 = new Composite(container, SWT.NONE);
    composite_1.setLayoutData(new GridData());
    final GridLayout gridLayout_1 = new GridLayout();
    gridLayout_1.numColumns = 6;
    composite_1.setLayout(gridLayout_1);

    topicButton = new Button(composite_1, SWT.NONE);
    final GridData gd_topicButton = new GridData(100, SWT.DEFAULT);
    topicButton.setLayoutData(gd_topicButton);
    topicButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(final SelectionEvent e) {
        InputDialog d = new InputDialog(PeerWindow.this.getShell(), "Add Thema", "Name:", null, null);
        d.open();
        session.getLocalPeer().newTopic(d.getValue());
      }
    });
    topicButton.setText("Add Topic");

    addPostingButton = new Button(composite_1, SWT.NONE);
    final GridData gd_addPostingButton = new GridData(100, SWT.DEFAULT);
    addPostingButton.setLayoutData(gd_addPostingButton);
    addPostingButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(final SelectionEvent e) {
        PostingDialog d = new PostingDialog(PeerWindow.this.getShell(), true);
        d.create();
        d.setContentEditable(true);
        d.open();
        if (d.isModified()) {
          Topic t = (Topic) topicSelection.getValue();
          t.createPosting(d.getSubject(), d.getContent());
          t.refresh();
        }
      }
    });
    addPostingButton.setText("Add Posting");

    subscribeButton = new Button(composite_1, SWT.NONE);
    final GridData gd_subscribeButton = new GridData(100, SWT.DEFAULT);
    subscribeButton.setLayoutData(gd_subscribeButton);
    subscribeButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(final SelectionEvent e) {
        Topic t = (Topic) topicSelection.getValue();
        t.setSubscribed(!t.isSubscribed());
        subscribeButton.setText(t.isSubscribed() ? "Unsubscribe" : "Subscribe");
        tableViewer.refresh(true);
      }
    });
    subscribeButton.setText("Subscribe");

    editButton = new Button(composite_1, SWT.NONE);
    final GridData gd_editButton = new GridData(100, SWT.DEFAULT);
    editButton.setLayoutData(gd_editButton);
    editButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(final SelectionEvent e) {
        Posting p = (Posting) postingSelection.getValue();
        PostingDialog d = new PostingDialog(PeerWindow.this.getShell());
        d.create();
        d.setInput(p);
        d.open();
        if (d.isModified()) {
          p.setContent(d.getContent());
          postingComposite.setInput(p);
        }
      }
    });
    editButton.setText("Edit");

    replyButton = new Button(composite_1, SWT.NONE);
    final GridData gd_replyButton = new GridData(100, SWT.DEFAULT);
    replyButton.setLayoutData(gd_replyButton);
    replyButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(final SelectionEvent e) {
        Posting p = (Posting) postingSelection.getValue();
        PostingDialog d = new PostingDialog(PeerWindow.this.getShell(), true);
        d.create();
        d.setContentEditable(true);
        d.open();
        if (d.isModified()) {
          Posting reply = p.reply(d.getSubject(), d.getContent());
          treeViewer.setSelection(new StructuredSelection(reply));
        }
      }
    });
    replyButton.setText("Reply");

    final Button wordfilterButton = new Button(composite_1, SWT.NONE);
    wordfilterButton.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(final SelectionEvent e) {
        WordFilterDialog d = new WordFilterDialog(PeerWindow.this.getShell());
        d.create();
        d.setInput(session.getWordlist());
        if (d.open() == Window.OK) {
          session.setWordlist(d.getInput());
        }
      }
    });
    final GridData gd_wordfilterButton = new GridData(SWT.FILL, SWT.CENTER, false, false);
    gd_wordfilterButton.widthHint = 100;
    wordfilterButton.setLayoutData(gd_wordfilterButton);
    wordfilterButton.setText("Wordfilter");

    final SashForm sashForm = new SashForm(container, SWT.NONE);
    sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    tableViewer = new TableViewer(sashForm, SWT.BORDER | SWT.FULL_SELECTION);
    tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
      // Disable selection of instanceof Peer
      private IStructuredSelection previousSelection = null;

      public void selectionChanged(SelectionChangedEvent event) {
        IStructuredSelection selection = (IStructuredSelection) event.getSelection();
        if (selection.getFirstElement() instanceof Peer)
          tableViewer.setSelection(previousSelection);
        else {
          postingComposite.setInput(null); // TODO: Delegate that to databinding
          Topic t = (Topic) selection.getFirstElement();
          if (t != null) {
            if(!selection.equals(previousSelection))
              t.refresh();
            t.resetNewPostings();
            tableViewer.refresh(true);
            subscribeButton.setText(t.isSubscribed() ? "Unsubscribe" : "Subscribe");
          }
          previousSelection = selection;
        }
      }
    });

    tableViewer.setSorter(new Sorter());
    table = tableViewer.getTable();
    final GridData gd_table = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 2);
    gd_table.minimumHeight = 50;
    gd_table.widthHint = 160;
    gd_table.minimumWidth = 20;
    table.setLayoutData(gd_table);

    final Composite composite = new Composite(sashForm, SWT.NONE);
    final GridLayout compositeGridLayout = new GridLayout();
    compositeGridLayout.marginHeight = 0;
    compositeGridLayout.marginWidth = 0;
    composite.setLayout(compositeGridLayout);

    final SashForm sashForm_1 = new SashForm(composite, SWT.VERTICAL);
    sashForm_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    treeViewer = new TreeViewer(sashForm_1, SWT.FULL_SELECTION | SWT.BORDER);
    TreeViewerColumn column = new TreeViewerColumn(treeViewer, SWT.NONE);
    column.getColumn().setWidth(200);
    column.getColumn().setText("Subject");
    column = new TreeViewerColumn(treeViewer, SWT.NONE);
    column.getColumn().setWidth(200);
    column.getColumn().setText("Author");
    tree = treeViewer.getTree();
    tree.setHeaderVisible(true);
    final GridData gd_tree = new GridData(SWT.FILL, SWT.FILL, true, true);
    gd_tree.minimumHeight = 50;
    gd_tree.widthHint = 400;
    gd_tree.minimumWidth = 100;
    tree.setLayoutData(gd_tree);
    tree.addListener(SWT.Resize, new Listener() {
      public void handleEvent(Event e) {
        Rectangle rect = tree.getClientArea();
        tree.getColumn(0).setWidth(rect.width / 10 * 8);
        tree.getColumn(1).setWidth(rect.width / 10 * 2);
      }
    });

    final ScrolledComposite scrolledComposite = new ScrolledComposite(sashForm_1, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    scrolledComposite.setExpandVertical(true);
    scrolledComposite.setExpandHorizontal(true);

    postingComposite = new PostingComposite(scrolledComposite, SWT.NONE);
    postingComposite.setSize(300, 0);
    scrolledComposite.setContent(postingComposite);
    sashForm_1.setWeights(new int[]{1, 2});
    sashForm.setWeights(new int[]{1, 3});

    //
    bindingContext = initDataBindings();
    return container;
  }

  /**
   * Create the actions
   */
  private void createActions() {
    // Create the actions
  }

  /**
   * Create the menu manager
   *
   * @return the menu manager
   */
  @Override
  protected MenuManager createMenuManager() {
    MenuManager menuManager = new MenuManager("menu");
    return menuManager;
  }

  /**
   * Create the toolbar manager
   *
   * @return the toolbar manager
   */
  @Override
  protected ToolBarManager createToolBarManager(int style) {
    ToolBarManager toolBarManager = new ToolBarManager(style);
    return toolBarManager;
  }

  /**
   * Create the status line manager
   *
   * @return the status line manager
   */
  @Override
  protected StatusLineManager createStatusLineManager() {
    StatusLineManager statusLineManager = new StatusLineManager();
    statusLineManager.setMessage(null, "");
    return statusLineManager;
  }

  @Override
  protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    newShell.setText("New Application");
  }

  /**
   * Return the initial size of the window
   */
  @Override
  protected Point getInitialSize() {
    return new Point(585, 362);
  }

  IObservableValue postingSelection;
  IObservableValue topicSelection;

  protected DataBindingContext initDataBindings() {
    //
    ObservableListContentProvider listViewerContentProviderList = new ObservableListContentProvider();
    tableViewer.setContentProvider(listViewerContentProviderList);
    tableViewer.setLabelProvider(new DelegatingStyledCellLabelProvider(new SidebarLabelProvider()));
    tableViewer.setInput(session.getSidebarEntries());
    session.setSidebarViewer(tableViewer);

    ObservableListTreeContentProvider contentProvider = new ObservableListTreeContentProvider(BeansObservables.listFactory(Realm
        .getDefault(), "replies", Posting.class), null);
    treeViewer.setContentProvider(contentProvider);
    treeViewer.setLabelProvider(new ObservableMapLabelProvider(new IObservableMap[]{
        BeansObservables.observeMap(contentProvider.getKnownElements(), Posting.class, "subject"),
        BeansObservables.observeMap(contentProvider.getKnownElements(), Posting.class, "author")}));

    topicSelection = ViewersObservables.observeSingleSelection(tableViewer);
    postingSelection = ViewersObservables.observeSingleSelection(treeViewer);
    IObservableValue postings = BeansObservables.observeDetailValue(Realm.getDefault(), topicSelection, "topLevelPosting",
        Posting.class);

    IObservableValue topicSelected = new ComputedValue(Boolean.TYPE) {
      protected Object calculate() {
        return Boolean.valueOf(topicSelection.getValue() != null);
      }
    };

    IObservableValue postingSelected = new ComputedValue(Boolean.TYPE) {
      protected Object calculate() {
        return Boolean.valueOf(postingSelection.getValue() != null);
      }
    };

    //
    DataBindingContext bindingContext = new DataBindingContext();
    bindingContext.bindValue(ViewersObservables.observeInput(treeViewer), postings, new UpdateValueStrategy(
        UpdateValueStrategy.POLICY_NEVER), null);
    bindingContext.bindValue(BeansObservables.observeValue(postingComposite, "input"), postingSelection, new UpdateValueStrategy(
        UpdateValueStrategy.POLICY_NEVER), null);

    bindingContext.bindValue(SWTObservables.observeEnabled(subscribeButton), topicSelected, null, null);
    bindingContext.bindValue(SWTObservables.observeEnabled(addPostingButton), topicSelected, null, null);
    bindingContext.bindValue(SWTObservables.observeEnabled(editButton), postingSelected, null, null);
    bindingContext.bindValue(SWTObservables.observeEnabled(replyButton), postingSelected, null, null);
    //
    //
    return bindingContext;
  }

}
