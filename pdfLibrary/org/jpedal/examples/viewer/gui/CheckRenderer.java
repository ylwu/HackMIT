package org.jpedal.examples.viewer.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.TreeCellRenderer;

public class CheckRenderer extends JPanel
  implements TreeCellRenderer
{
  protected JCheckBox check;
  protected TreeLabel label;

  public CheckRenderer()
  {
    setLayout(null);
    add(this.check = new JCheckBox());
    add(this.label = new TreeLabel());
    this.check.setBackground(UIManager.getColor("Tree.textBackground"));
  }

  public Component getTreeCellRendererComponent(JTree paramJTree, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt, boolean paramBoolean4)
  {
    String str = paramJTree.convertValueToText(paramObject, paramBoolean1, paramBoolean2, paramBoolean3, paramInt, paramBoolean4);
    setEnabled(paramJTree.isEnabled());
    if ((paramObject instanceof CheckNode))
    {
      this.check.setSelected(((CheckNode)paramObject).isSelected());
      setEnabled(((CheckNode)paramObject).isEnabled());
      this.check.setEnabled(((CheckNode)paramObject).isEnabled());
      this.label.setFont(paramJTree.getFont());
      this.label.setText(str);
      this.label.setSelected(paramBoolean1);
      this.label.setFocus(paramBoolean4);
      return this;
    }
    return new JLabel(str);
  }

  public Dimension getPreferredSize()
  {
    Dimension localDimension1 = this.check.getPreferredSize();
    Dimension localDimension2 = this.label.getPreferredSize();
    return new Dimension(localDimension1.width + localDimension2.width, localDimension1.height < localDimension2.height ? localDimension2.height : localDimension1.height);
  }

  public void doLayout()
  {
    Dimension localDimension1 = this.check.getPreferredSize();
    Dimension localDimension2 = this.label.getPreferredSize();
    int i = 0;
    int j = 0;
    if (localDimension1.height < localDimension2.height)
      i = (localDimension2.height - localDimension1.height) / 2;
    else
      j = (localDimension1.height - localDimension2.height) / 2;
    this.check.setLocation(0, i);
    this.check.setBounds(0, i, localDimension1.width, localDimension1.height);
    this.label.setLocation(localDimension1.width, j);
    this.label.setBounds(localDimension1.width, j, localDimension2.width, localDimension2.height);
  }

  public void setBackground(Color paramColor)
  {
    if ((paramColor instanceof ColorUIResource))
      paramColor = null;
    super.setBackground(paramColor);
  }

  static class TreeLabel extends JLabel
  {
    boolean isSelected;
    boolean hasFocus;

    public void setBackground(Color paramColor)
    {
      if ((paramColor instanceof ColorUIResource))
        paramColor = null;
      super.setBackground(paramColor);
    }

    public void paint(Graphics paramGraphics)
    {
      String str;
      if (((str = getText()) != null) && (!str.isEmpty()))
      {
        if (this.isSelected)
          paramGraphics.setColor(UIManager.getColor("Tree.selectionBackground"));
        else
          paramGraphics.setColor(UIManager.getColor("Tree.textBackground"));
        Dimension localDimension = getPreferredSize();
        int i = 0;
        Icon localIcon = getIcon();
        if (localIcon != null)
          i = localIcon.getIconWidth() + Math.max(0, getIconTextGap() - 1);
        paramGraphics.fillRect(i, 0, localDimension.width - 1 - i, localDimension.height);
        if (this.hasFocus)
        {
          paramGraphics.setColor(UIManager.getColor("Tree.selectionBorderColor"));
          paramGraphics.drawRect(i, 0, localDimension.width - 1 - i, localDimension.height - 1);
        }
      }
      super.paint(paramGraphics);
    }

    public Dimension getPreferredSize()
    {
      Dimension localDimension = super.getPreferredSize();
      if (localDimension != null)
        localDimension = new Dimension(localDimension.width + 3, localDimension.height);
      return localDimension;
    }

    void setSelected(boolean paramBoolean)
    {
      this.isSelected = paramBoolean;
    }

    void setFocus(boolean paramBoolean)
    {
      this.hasFocus = paramBoolean;
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.CheckRenderer
 * JD-Core Version:    0.6.2
 */