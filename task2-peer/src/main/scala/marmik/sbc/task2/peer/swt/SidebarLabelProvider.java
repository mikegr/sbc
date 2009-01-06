package marmik.sbc.task2.peer.swt;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

import marmik.sbc.task2.peer.swt.model.Peer;
import marmik.sbc.task2.peer.swt.model.Topic;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class SidebarLabelProvider extends ColumnLabelProvider implements IStyledLabelProvider {

  @Override
  public Color getBackground(Object element) {
    if (element instanceof Peer)
      return Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
    else
      return null;
  }

  @Override
  public String getText(Object element) {
    return getStyledText(element).toString();
  }

  public StyledString getStyledText(Object element) {
    StyledString styledString = new StyledString();

    if (element instanceof Topic) {
      Topic topic = (Topic) element;
      styledString.append(topic.getName());
      styledString.append(" (5)", StyledString.COUNTER_STYLER);
    } else if (element instanceof Peer) {
      Peer peer = (Peer) element;
      if(peer.isLocal())
        styledString.append(peer.getName(), StyledString.createColorRegistryStyler("sidebar.local_peer", null));
      else
        styledString.append(peer.getName());
    }
    return styledString;
  }
}
