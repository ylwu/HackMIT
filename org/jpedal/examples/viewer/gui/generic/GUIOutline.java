package org.jpedal.examples.viewer.gui.generic;

import javax.swing.tree.DefaultMutableTreeNode;
import org.w3c.dom.Node;

public abstract interface GUIOutline
{
  public abstract Object getTree();

  public abstract DefaultMutableTreeNode getLastSelectedPathComponent();

  public abstract String getPage(String paramString);

  public abstract void selectBookmark();

  public abstract void reset(Node paramNode);

  public abstract String convertNodeIDToRef(int paramInt);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.generic.GUIOutline
 * JD-Core Version:    0.6.2
 */