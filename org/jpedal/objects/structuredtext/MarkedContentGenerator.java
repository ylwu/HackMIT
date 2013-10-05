package org.jpedal.objects.structuredtext;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.jpedal.io.ObjectStore;
import org.jpedal.io.PdfObjectReader;
import org.jpedal.objects.PdfPageData;
import org.jpedal.objects.PdfResources;
import org.jpedal.objects.layers.PdfLayerList;
import org.jpedal.objects.raw.MCObject;
import org.jpedal.objects.raw.PageObject;
import org.jpedal.objects.raw.PdfObject;
import org.jpedal.parser.PdfStreamDecoder;
import org.jpedal.render.SwingDisplay;
import org.jpedal.utils.LogWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class MarkedContentGenerator
{
  private PdfObjectReader currentPdfFile;
  private DocumentBuilder db = null;
  private Document doc;
  private Element root;
  private Map pageStreams = new HashMap();
  private PdfObject structTreeRootObj;
  private PdfResources res;
  private PdfLayerList layers;
  private PdfPageData pdfPageData;
  private boolean isDecoding = false;
  static final boolean debug = false;
  static boolean displayNoStructMsg = true;
  String indent = "";

  public Document getMarkedContentTree(PdfResources paramPdfResources, PdfPageData paramPdfPageData, PdfObjectReader paramPdfObjectReader)
  {
    this.structTreeRootObj = paramPdfResources.getPdfObject(3);
    this.res = paramPdfResources;
    this.layers = paramPdfResources.getPdfLayerList();
    this.pdfPageData = paramPdfPageData;
    this.currentPdfFile = paramPdfObjectReader;
    this.currentPdfFile.checkResolved(this.structTreeRootObj);
    setupTree();
    int i = (this.structTreeRootObj != null) && (this.structTreeRootObj.getDictionary(1719112618) != null) ? 1 : 0;
    if (i != 0)
    {
      buildTree();
      this.pageStreams.clear();
    }
    else
    {
      try
      {
        decodePageForMarkedContent(1, null, this.doc);
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException.getMessage()).toString());
      }
    }
    return this.doc;
  }

  private void setupTree()
  {
    try
    {
      DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
      this.db = localDocumentBuilderFactory.newDocumentBuilder();
    }
    catch (ParserConfigurationException localParserConfigurationException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localParserConfigurationException.getMessage()).toString());
    }
    this.doc = this.db.newDocument();
    this.doc.appendChild(this.doc.createComment(" Created from JPedal "));
    this.doc.appendChild(this.doc.createComment(" http://www.idrsolutions.com "));
  }

  private void buildTree()
  {
    this.root = this.doc.createElement("TaggedPDF-doc");
    this.doc.appendChild(this.root);
    PdfObject localPdfObject = this.structTreeRootObj.getDictionary(27);
    if (localPdfObject == null)
    {
      byte[][] arrayOfByte = this.structTreeRootObj.getStringArray(27);
      readKarray(arrayOfByte, this.root, null);
    }
    else
    {
      readChildNode(localPdfObject, this.root, null);
    }
  }

  private void readChildNode(PdfObject paramPdfObject, Element paramElement, Map paramMap)
  {
    byte[][] arrayOfByte = paramPdfObject.getStringArray(27);
    int i = paramPdfObject.getInt(27);
    PdfObject localPdfObject2 = paramPdfObject.getDictionary(27);
    String str1 = paramPdfObject.getTextStreamValue(472989239);
    String str2 = paramPdfObject.getName(35);
    Element localElement = null;
    if (str2 != null)
      if (str2.equals("Span"))
      {
        localElement = paramElement;
      }
      else
      {
        localElement = this.doc.createElement(cleanName(str2));
        if (str1 != null)
          localElement.setAttribute("xml:lang", str1);
        paramElement.appendChild(localElement);
      }
    PdfObject localPdfObject1 = paramPdfObject.getDictionary(8247);
    if ((localPdfObject1 != null) && (paramMap == null))
    {
      paramMap = new HashMap();
      try
      {
        decodePageForMarkedContent(-1, localPdfObject1, paramMap);
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException.getMessage()).toString());
      }
    }
    if (arrayOfByte != null)
      readKarray(arrayOfByte, localElement, paramMap);
    else if (localPdfObject2 != null)
      readChildNode(localPdfObject2, localElement, paramMap);
    else if (i != -1)
      addContentToNode(paramMap, String.valueOf(i), localElement);
    else if (paramPdfObject.getTextStreamValue(36) == null)
      System.out.println(new StringBuilder().append("unimplemented ").append(paramPdfObject.getObjectRefAsString()).toString());
  }

  private void addContentToNode(Map paramMap, String paramString, Element paramElement)
  {
    String str = (String)paramMap.get(paramString);
    if (str != null)
    {
      str = handleXMLCharacters(str);
      Text localText = this.doc.createTextNode(str);
      paramElement.appendChild(localText);
    }
  }

  private static String handleXMLCharacters(String paramString)
  {
    paramString = paramString.replaceAll("&lt;", "<");
    paramString = paramString.replaceAll("&gt;", ">");
    return paramString;
  }

  private void readKarray(byte[][] paramArrayOfByte, Element paramElement, Map paramMap)
  {
    int i = paramArrayOfByte.length;
    for (int j = 0; j < i; j++)
    {
      String str = new String(paramArrayOfByte[j]);
      if (i - j >= 3)
      {
        byte[] arrayOfByte = paramArrayOfByte[(j + 2)];
        if (arrayOfByte[0] == 82)
        {
          MCObject localMCObject = new MCObject(new StringBuilder().append(str).append(' ').append(new String(paramArrayOfByte[(j + 1)])).append(" R").toString());
          this.currentPdfFile.readObject(localMCObject);
          readChildNode(localMCObject, paramElement, paramMap);
          j += 2;
        }
        else
        {
          addContentToNode(paramMap, str, paramElement);
        }
      }
      else
      {
        addContentToNode(paramMap, str, paramElement);
      }
    }
  }

  private static String cleanName(String paramString)
  {
    StringBuilder localStringBuilder1 = new StringBuilder(10);
    int i = paramString.length();
    for (int j = 0; j < i; j++)
    {
      char c = paramString.charAt(j);
      if (c == '#')
      {
        StringBuilder localStringBuilder2 = new StringBuilder(2);
        for (int k = 0; k < 2; k++)
        {
          j++;
          localStringBuilder2.append(paramString.charAt(j));
        }
        c = (char)Integer.parseInt(localStringBuilder2.toString(), 16);
        if (!Character.isLetterOrDigit(c))
          c = '-';
      }
      if (c == ' ')
        localStringBuilder1.append('-');
      else if (c == '-')
        localStringBuilder1.append(c);
      else if (c == '_')
        localStringBuilder1.append(c);
      else if (Character.isLetterOrDigit(c))
        localStringBuilder1.append(c);
    }
    paramString = localStringBuilder1.toString();
    return paramString;
  }

  private final synchronized void decodePageForMarkedContent(int paramInt, PdfObject paramPdfObject, Object paramObject)
    throws Exception
  {
    if (this.isDecoding)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("[PDF]WARNING - this file is being decoded already");
    }
    else
    {
      Object localObject1;
      if (paramPdfObject == null)
      {
        localObject1 = this.currentPdfFile.getReferenceforPage(paramInt);
        paramPdfObject = new PageObject((String)localObject1);
        this.currentPdfFile.readObject(paramPdfObject);
      }
      else
      {
        paramInt = this.currentPdfFile.convertObjectToPageNumber(new String(paramPdfObject.getUnresolvedData()));
        this.currentPdfFile.checkResolved(paramPdfObject);
      }
      try
      {
        this.isDecoding = true;
        localObject1 = new ObjectStore();
        PdfStreamDecoder localPdfStreamDecoder = new PdfStreamDecoder(this.currentPdfFile, false, this.layers);
        localPdfStreamDecoder.setParameters(true, false, 0, 7, false);
        localPdfStreamDecoder.setXMLExtraction(false);
        localPdfStreamDecoder.setObjectValue(-9, "markedContent");
        localPdfStreamDecoder.setObjectValue(-8, localObject1);
        localPdfStreamDecoder.setObjectValue(-3, null);
        localPdfStreamDecoder.setObjectValue(-18, this.pdfPageData);
        localPdfStreamDecoder.setIntValue(-10, paramInt);
        localPdfStreamDecoder.setObjectValue(23, new SwingDisplay(paramInt, (ObjectStore)localObject1, false));
        this.res.setupResources(localPdfStreamDecoder, false, paramPdfObject.getDictionary(2004251818), paramInt, this.currentPdfFile);
        localPdfStreamDecoder.setObjectValue(-5, paramObject);
        localPdfStreamDecoder.decodePageContent(paramPdfObject);
        ((ObjectStore)localObject1).flush();
      }
      catch (Exception localException)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog(new StringBuilder().append("Exception: ").append(localException.getMessage()).toString());
      }
      finally
      {
        this.isDecoding = false;
      }
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.structuredtext.MarkedContentGenerator
 * JD-Core Version:    0.6.2
 */