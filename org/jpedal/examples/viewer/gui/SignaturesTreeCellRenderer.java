package org.jpedal.examples.viewer.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class SignaturesTreeCellRenderer extends DefaultTreeCellRenderer
{
  private Icon icon;

  public Icon getLeafIcon()
  {
    return this.icon;
  }

  public Component getTreeCellRendererComponent(JTree paramJTree, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt, boolean paramBoolean4)
  {
    DefaultMutableTreeNode localDefaultMutableTreeNode1 = (DefaultMutableTreeNode)paramObject;
    paramObject = localDefaultMutableTreeNode1.getUserObject();
    int i = localDefaultMutableTreeNode1.getLevel();
    String str1 = paramObject.toString();
    this.icon = null;
    Font localFont = paramJTree.getFont();
    if (i == 2)
    {
      DefaultMutableTreeNode localDefaultMutableTreeNode2 = (DefaultMutableTreeNode)localDefaultMutableTreeNode1.getParent();
      String str2 = localDefaultMutableTreeNode2.getUserObject().toString();
      URL localURL;
      if (str2.equals("The following signature fields are not signed"))
      {
        localURL = getClass().getResource("/org/jpedal/examples/viewer/res/unlock.png");
        this.icon = new ImageIcon(localURL);
      }
      else
      {
        localURL = getClass().getResource("/org/jpedal/examples/viewer/res/lock.gif");
        this.icon = new ImageIcon(localURL);
        localFont = new Font(localFont.getFamily(), 1, localFont.getSize());
      }
    }
    setFont(localFont);
    setText(str1);
    setIcon(this.icon);
    if (paramBoolean1)
    {
      setBackground(new Color(236, 233, 216));
      setForeground(Color.BLACK);
    }
    else
    {
      setBackground(paramJTree.getBackground());
      setForeground(paramJTree.getForeground());
    }
    setEnabled(paramJTree.isEnabled());
    setOpaque(true);
    return this;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.SignaturesTreeCellRenderer
 * JD-Core Version:    0.6.2
 */