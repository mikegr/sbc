package marmik.sbc.task2.peer.swt;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

import marmik.sbc.task2.peer.swt.model.Peer;
import marmik.sbc.task2.peer.swt.model.Topic;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;

public class SidebarLabelProvider extends ColumnLabelProvider implements IStyledLabelProvider {
  private static class DefaultBoldStyler extends Styler {
    private final String fForegroundColorName;
    private final String fBackgroundColorName;

    public DefaultBoldStyler(String foregroundColorName, String backgroundColorName) {
      fForegroundColorName = foregroundColorName;
      fBackgroundColorName = backgroundColorName;
      FontData[] boldFontData = getModifiedFontData(JFaceResources.getFontRegistry().defaultFont().getFontData(),
          SWT.BOLD);
      JFaceResources.getFontRegistry().put("default-bold", boldFontData);
    }

    public void applyStyles(TextStyle textStyle) {
      ColorRegistry colorRegistry = JFaceResources.getColorRegistry();
      if (fForegroundColorName != null) {
        textStyle.foreground = colorRegistry.get(fForegroundColorName);
      }
      if (fBackgroundColorName != null) {
        textStyle.background = colorRegistry.get(fBackgroundColorName);
      }
      textStyle.font = JFaceResources.getFontRegistry().get("default-bold");
    }

    private static FontData[] getModifiedFontData(FontData[] originalData, int additionalStyle) {
      FontData[] styleData = new FontData[originalData.length];
      for (int i = 0; i < styleData.length; i++) {
        FontData base = originalData[i];
        styleData[i] = new FontData(base.getName(), base.getHeight(), base.getStyle() | additionalStyle);
      }
      return styleData;
    }
  }

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
      if(topic.isSubscribed()) {
        styledString.append(" (5)", StyledString.COUNTER_STYLER);
      }
    } else if (element instanceof Peer) {
      Peer peer = (Peer) element;
      if (peer.isLocal())
        styledString.append(peer.getName(), new DefaultBoldStyler("sidebar.local_peer", null));
      else
        styledString.append(peer.getName(), new DefaultBoldStyler(null, null));
    }
    return styledString;
  }
}
