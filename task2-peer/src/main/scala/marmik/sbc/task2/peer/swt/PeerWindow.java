package marmik.sbc.task2.peer.swt;

import marmik.sbc.task2.peer.swt.model.Session;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import marmik.sbc.task2.peer.swt.model.Peer;

public class PeerWindow extends org.eclipse.jface.window.ApplicationWindow {

  private DataBindingContext bindingContext;
  private ListViewer listViewer;
  private Tree tree;
  private List list;
  private Session session;

  /**
   * Create the application window
   */
  protected PeerWindow() {
    super(null);
    createActions();
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
    gridLayout.numColumns = 2;
    container.setLayout(gridLayout);

    listViewer = new ListViewer(container, SWT.BORDER);
    list = listViewer.getList();
    final GridData gd_list = new GridData(SWT.LEFT, SWT.FILL, false, true, 1, 2);
    gd_list.minimumHeight = 50;
    gd_list.widthHint = 160;
    gd_list.minimumWidth = 20;
    list.setLayoutData(gd_list);

    final TreeViewer treeViewer = new TreeViewer(container, SWT.BORDER);
    tree = treeViewer.getTree();
    final GridData gd_tree = new GridData(SWT.FILL, SWT.FILL, true, true);
    gd_tree.minimumHeight = 50;
    gd_tree.widthHint = 400;
    gd_tree.minimumWidth = 100;
    tree.setLayoutData(gd_tree);

    final ScrolledComposite scrolledComposite = new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL
        | SWT.V_SCROLL);
    scrolledComposite.setExpandVertical(true);
    scrolledComposite.setExpandHorizontal(true);
    final GridData gd_scrolledComposite = new GridData(SWT.FILL, SWT.FILL, true, true);
    gd_scrolledComposite.widthHint = 400;
    gd_scrolledComposite.minimumHeight = 80;
    scrolledComposite.setLayoutData(gd_scrolledComposite);

    final PostingComposite postingComposite = new PostingComposite(scrolledComposite, SWT.NONE);
    postingComposite.setSize(300, 0);
    scrolledComposite.setContent(postingComposite);

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

  /**
   * Launch the application
   *
   * @param args
   */
  public static void main(String args[]) {
    Display display = Display.getDefault();
    Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
      public void run() {
        try {
          PeerWindow window = new PeerWindow();
          window.setBlockOnOpen(true);
          window.open();
          Display.getCurrent().dispose();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Configure the shell
   *
   * @param newShell
   */
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
    return new Point(500, 375);
  }

  protected DataBindingContext initDataBindings() {
    //
    ObservableListContentProvider listViewerContentProviderList = new ObservableListContentProvider();
    listViewer.setContentProvider(listViewerContentProviderList);
    //
    IObservableMap[] listViewerLabelProviderMaps = BeansObservables.observeMaps(listViewerContentProviderList
        .getKnownElements(), Peer.class, new String[] { "name" });
    listViewer.setLabelProvider(new ObservableMapLabelProvider(listViewerLabelProviderMaps));
    //
    listViewer.setInput(session.getPeers());
    //
    DataBindingContext bindingContext = new DataBindingContext();
    //
    //
    return bindingContext;
  }

}
