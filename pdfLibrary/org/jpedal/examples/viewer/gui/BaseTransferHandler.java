package org.jpedal.examples.viewer.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.Values;
import org.jpedal.utils.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BaseTransferHandler extends TransferHandler
{
  protected Commands currentCommands;
  protected SwingGUI currentGUI;
  protected Values commonValues;

  public BaseTransferHandler(Values paramValues, SwingGUI paramSwingGUI, Commands paramCommands)
  {
    this.commonValues = paramValues;
    this.currentGUI = paramSwingGUI;
    this.currentCommands = paramCommands;
  }

  public boolean canImport(JComponent paramJComponent, DataFlavor[] paramArrayOfDataFlavor)
  {
    return true;
  }

  protected Object getImport(Transferable paramTransferable)
    throws Exception
  {
    DataFlavor[] arrayOfDataFlavor = paramTransferable.getTransferDataFlavors();
    DataFlavor localDataFlavor1 = null;
    int i = arrayOfDataFlavor.length - 1;
    for (int j = 0; j <= i; j++)
      if (arrayOfDataFlavor[j].isFlavorJavaFileListType())
        localDataFlavor1 = arrayOfDataFlavor[j];
    try
    {
      DataFlavor localDataFlavor2 = DataFlavor.selectBestTextFlavor(arrayOfDataFlavor);
      if (localDataFlavor2 != null)
      {
        Reader localReader = localDataFlavor2.getReaderForText(paramTransferable);
        String str = readTextDate(localReader);
        str = removeChar(str, '\000');
        if (str.contains("ftp:/"))
        {
          this.currentGUI.showMessageDialog("Files cannot be opened via FTP");
          return null;
        }
        str = getURL(str);
        str = str.replaceAll("%20", " ");
        return str;
      }
      if (localDataFlavor1 != null)
        return paramTransferable.getTransferData(localDataFlavor1);
    }
    catch (Exception localException)
    {
      return null;
    }
    return null;
  }

  private static String removeChar(String paramString, char paramChar)
  {
    String str = "";
    for (int i = 0; i < paramString.length(); i++)
      if (paramString.charAt(i) != paramChar)
        str = str + paramString.charAt(i);
    return str;
  }

  private static String getURL(String paramString)
    throws ParserConfigurationException, SAXException, IOException
  {
    if ((!paramString.startsWith("http://")) && (!paramString.startsWith("file://")))
    {
      DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
      Document localDocument = localDocumentBuilder.parse(new ByteArrayInputStream(StringUtils.toBytes(paramString)));
      Element localElement = (Element)localDocument.getElementsByTagName("a").item(0);
      paramString = getHrefAttribute(localElement);
    }
    return paramString;
  }

  private static String readTextDate(Reader paramReader)
    throws IOException
  {
    BufferedReader localBufferedReader = new BufferedReader(paramReader);
    String str1 = "";
    for (String str2 = localBufferedReader.readLine(); str2 != null; str2 = localBufferedReader.readLine())
      str1 = str1 + str2;
    localBufferedReader.close();
    return str1;
  }

  private static String getHrefAttribute(Element paramElement)
  {
    NamedNodeMap localNamedNodeMap = paramElement.getAttributes();
    Node localNode = localNamedNodeMap.getNamedItem("href");
    if (localNode != null)
      return localNode.getNodeValue();
    return null;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.BaseTransferHandler
 * JD-Core Version:    0.6.2
 */