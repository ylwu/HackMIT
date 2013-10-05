package org.jpedal.gui;

import java.awt.Container;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.text.BadLocationException;
import org.jpedal.utils.repositories.Vector_Int;

public abstract interface GUIFactory
{
  public static final int BUTTONBAR = 0;
  public static final int NAVBAR = 1;
  public static final int PAGES = 2;
  public static final Integer MULTIPAGE = Integer.valueOf(1);

  public abstract void initLayoutMenus(JMenu paramJMenu, String[] paramArrayOfString, int[] paramArrayOfInt);

  public abstract void getInfoBox();

  public abstract void resetRotationBox();

  public abstract void showDocumentProperties(String paramString, long paramLong, int paramInt1, int paramInt2);

  public abstract void init(String[] paramArrayOfString, Object paramObject1, Object paramObject2);

  public abstract void addCursor();

  public abstract void addButton(int paramInt1, String paramString1, String paramString2, int paramInt2);

  public abstract void addMenuItem(JMenu paramJMenu, String paramString1, String paramString2, int paramInt);

  public abstract void addCombo(String paramString1, String paramString2, int paramInt);

  public abstract void setViewerTitle(String paramString);

  public abstract void resetComboBoxes(boolean paramBoolean);

  public abstract JScrollPane createPane(JTextPane paramJTextPane, String paramString, boolean paramBoolean)
    throws BadLocationException;

  public abstract int getSelectedComboIndex(int paramInt);

  public abstract void setSelectedComboIndex(int paramInt1, int paramInt2);

  public abstract void setSelectedComboItem(int paramInt, String paramString);

  public abstract Object getSelectedComboItem(int paramInt);

  public abstract void zoom();

  public abstract int getRotation();

  public abstract float getScaling();

  public abstract void rotate();

  public abstract void toogleAutoScrolling();

  public abstract void setAutoScrolling(boolean paramBoolean);

  public abstract void decodePage();

  public abstract void initStatus();

  public abstract void initThumbnails(int paramInt, Vector_Int paramVector_Int);

  public abstract void setNoPagesDecoded();

  public abstract void setCoordText(String paramString);

  public abstract void setPageNumber();

  public abstract void addToMainMenu(JMenu paramJMenu);

  public abstract Container getFrame();

  public abstract JToolBar getTopButtonBar();

  public abstract void resetNavBar();

  public abstract void showMessageDialog(Object paramObject);

  public abstract void showMessageDialog(Object paramObject, String paramString, int paramInt);

  public abstract String showInputDialog(Object paramObject, String paramString, int paramInt);

  public abstract String showInputDialog(String paramString);

  public abstract int showOptionDialog(Object paramObject1, String paramString, int paramInt1, int paramInt2, Object paramObject2, Object[] paramArrayOfObject, Object paramObject3);

  public abstract void showMessageDialog(JTextArea paramJTextArea);

  public abstract int showConfirmDialog(String paramString1, String paramString2, int paramInt);

  public abstract int showOverwriteDialog(String paramString, boolean paramBoolean);

  public abstract void showItextPopup();

  public abstract void showFirstTimePopup();

  public abstract int showConfirmDialog(Object paramObject, String paramString, int paramInt1, int paramInt2);

  public abstract boolean allowScrolling();

  public abstract boolean confirmClose();

  public abstract boolean isPDFOutlineVisible();

  public abstract void setPDFOutlineVisible(boolean paramBoolean);

  public abstract void setSplitDividerLocation(int paramInt);

  public abstract void updateStatusMessage(String paramString);

  public abstract void resetStatusMessage(String paramString);

  public abstract void setStatusProgress(int paramInt);

  public abstract Object printDialog(String[] paramArrayOfString, String paramString);

  public abstract void setQualityBoxVisible(boolean paramBoolean);

  public abstract void setPage(int paramInt);
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.gui.GUIFactory
 * JD-Core Version:    0.6.2
 */