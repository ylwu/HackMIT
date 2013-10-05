package org.jpedal.examples.viewer.gui.swing;

import java.awt.event.MouseEvent;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import org.jpedal.utils.Messages;

public class SearchList extends JList
{
  private Map textPages;
  private Map textAreas;
  private String pageStr = "Page";
  private int Length = 0;
  public static final int NO_RESULTS_FOUND = 1;
  public static final int SEARCH_COMPLETE_SUCCESSFULLY = 2;
  public static final int SEARCH_INCOMPLETE = 4;
  private int status = 1;
  private String searchTerm = "";

  public SearchList(DefaultListModel paramDefaultListModel, Map paramMap1, Map paramMap2)
  {
    super(paramDefaultListModel);
    this.Length = paramDefaultListModel.capacity();
    this.textPages = paramMap1;
    this.textAreas = paramMap2;
    this.pageStr = (Messages.getMessage("PdfViewerSearch.Page") + ' ');
  }

  public String getToolTipText(MouseEvent paramMouseEvent)
  {
    int i = locationToIndex(paramMouseEvent.getPoint());
    Object localObject = this.textPages.get(Integer.valueOf(i));
    if (localObject != null)
      return this.pageStr + localObject;
    return null;
  }

  public Map getTextPages()
  {
    return this.textPages;
  }

  public Map textAreas()
  {
    return this.textAreas;
  }

  public int getResultCount()
  {
    return this.textAreas.size();
  }

  public void setLength(int paramInt)
  {
    this.Length = paramInt;
  }

  public void setStatus(int paramInt)
  {
    this.status = paramInt;
  }

  public void setSearchTerm(String paramString)
  {
    this.searchTerm = paramString;
  }

  public String getSearchTerm()
  {
    return this.searchTerm;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.swing.SearchList
 * JD-Core Version:    0.6.2
 */