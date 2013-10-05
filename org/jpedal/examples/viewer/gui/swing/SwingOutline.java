package org.jpedal.examples.viewer.gui.swing;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import org.jpedal.examples.viewer.gui.generic.GUIOutline;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SwingOutline extends JScrollPane
  implements GUIOutline
{
  private Map pageLookupTableViaTitle = new HashMap();
  private Map nodeToRef = new HashMap();
  private Map closedNodes = new HashMap();
  private DefaultMutableTreeNode top = new DefaultMutableTreeNode("Root");
  private JTree tree;
  private boolean hasDuplicateTitles;

  public SwingOutline()
  {
    getViewport().add(new JLabel("No outline"));
  }

  public void reset(Node paramNode)
  {
    this.top.removeAllChildren();
    if (this.tree != null)
      getViewport().remove(this.tree);
    this.pageLookupTableViaTitle.clear();
    this.nodeToRef.clear();
    this.closedNodes.clear();
    this.hasDuplicateTitles = false;
    if (paramNode != null)
    {
      this.hasDuplicateTitles = false;
      readChildNodes(paramNode, this.top, 0);
    }
    this.tree = new JTree(this.top);
    this.tree.setName("Tree");
    if (paramNode != null)
      expandAll();
    this.tree.setRootVisible(false);
    this.tree.getSelectionModel().setSelectionMode(1);
    getViewport().add(this.tree);
    setHorizontalScrollBarPolicy(30);
    setVerticalScrollBarPolicy(20);
  }

  private void expandAll()
  {
    for (int i = 1; i < 4; i++)
      if (!this.closedNodes.containsKey(Integer.valueOf(i)))
        this.tree.expandRow(i);
      else
        this.tree.collapseRow(i);
  }

  public int readChildNodes(Node paramNode, DefaultMutableTreeNode paramDefaultMutableTreeNode, int paramInt)
  {
    if (paramDefaultMutableTreeNode == null)
      paramDefaultMutableTreeNode = this.top;
    NodeList localNodeList = paramNode.getChildNodes();
    int i = localNodeList.getLength();
    for (int j = 0; j < i; j++)
    {
      Node localNode = localNodeList.item(j);
      Element localElement = (Element)localNode;
      String str1 = localElement.getAttribute("title");
      String str2 = localElement.getAttribute("page");
      String str3 = localElement.getAttribute("isClosed");
      String str4 = localElement.getAttribute("objectRef");
      if (this.pageLookupTableViaTitle.containsKey(str1))
        this.hasDuplicateTitles = true;
      else
        this.pageLookupTableViaTitle.put(str1, str2);
      if (str3.equals("true"))
        this.closedNodes.put(Integer.valueOf(paramInt), "x");
      this.nodeToRef.put(Integer.valueOf(paramInt), str4);
      paramInt++;
      DefaultMutableTreeNode localDefaultMutableTreeNode = new DefaultMutableTreeNode(str1);
      paramDefaultMutableTreeNode.add(localDefaultMutableTreeNode);
      if (localNode.hasChildNodes())
        paramInt = readChildNodes(localNode, localDefaultMutableTreeNode, paramInt);
    }
    return paramInt;
  }

  public String getPage(String paramString)
  {
    if (this.hasDuplicateTitles)
      return null;
    return (String)this.pageLookupTableViaTitle.get(paramString);
  }

  public String convertNodeIDToRef(int paramInt)
  {
    return (String)this.nodeToRef.get(Integer.valueOf(paramInt));
  }

  public void selectBookmark()
  {
  }

  public Object getTree()
  {
    return this.tree;
  }

  public DefaultMutableTreeNode getLastSelectedPathComponent()
  {
    return (DefaultMutableTreeNode)this.tree.getLastSelectedPathComponent();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.swing.SwingOutline
 * JD-Core Version:    0.6.2
 */