package org.jpedal.examples.viewer.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.jpedal.gui.ShowGUIMessage;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.utils.LogWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PropertiesFile
{
  private String separator = System.getProperty("file.separator");
  private String userDir = System.getProperty("user.dir");
  private String configFile = this.userDir + this.separator + ".properties.xml";
  private InputStream configInputStream = null;
  private boolean isTest = false;
  private boolean refactorProperties = false;
  private boolean isReadOnly = false;
  private Document doc;
  private int noOfRecentDocs = 6;
  private String[] properties = { "Flag to show popup information first time viewer is used", "showfirsttimepopup", "true", "The amount of days left of the trial", "daysLeft", "", "Show message about rhino and it's use", "showrhinomessage", "false", "Show messages for JAI libaries", "showddmessage", "true", "Set how the search functionality is displayed\n0 : External Window\n1 : Side Tab Bar\n2 : Menu bar", "searchWindowType", "2", "Set if border should be shown\n0 : Hide Border\n1 : Show Border", "borderType", "1", "Flag to turn on hiRes printing", "useHiResPrinting", "true", "This is set the number of pixels used to represent an inch on screen", "resolution", "110", "Flag to allow cursor to change such as when over text", "allowCursorToChange", "true", "Flag to allow view to scroll when dragging the mouse", "autoScroll", "true", "No longer used, please use startView", "pageMode", "1", "Flag to allow tips to be displayed at start up", "displaytipsonstartup", "false", "Flag to allow the viewer to auto update", "automaticupdate", "true", "Value shows what version of the library is being used", "currentversion", "5.06b04", "Show message when using tiffs", "showtiffmessage", "true", "The maximum number of viewers when using the multi viewer example", "maxmultiviewers", "20", "Shows the contents of the menu bar, menubar remains but empty if set to false", "MenuBarMenu", "true", "Shows the File menu on the menu bar", "FileMenu", "true", "Shows the Open menu on the File menu", "OpenMenu", "true", "Shows the Open menuitem on the Open menu", "Open", "true", "Shows the Open url menuitem on the Open menu", "Openurl", "true", "ENDCHILDREN", "Shows the Save menuitem on the File menu", "Save", "true", "Shows the Resave forms menuitem on the File menu", "Resaveasforms", "false", "Shows the Find menuitem on the File menu", "Find", "true", "Shows the Document Properties menuitem on the File menu", "Documentproperties", "true", "Shows the Sign pdf menuitem on the File menu", "Signpdf", "true", "Shows the Print menuitem on the File menu", "Print", "true", "Shows the Recent Documents menuitem on the File menu", "Recentdocuments", "true", "Shows the Exit menuitem on the File menu", "Exit", "true", "ENDCHILDREN", "Shows the Edit menu on the menu bar", "EditMenu", "true", "Shows the Copy menuitem on the Edit menu", "Copy", "true", "Shows the Select all menuitem on the Edit menu", "Selectall", "true", "Shows the Deselect all menuitem on the Edit menu", "Deselectall", "true", "Shows the Preferences menuitem on the Edit menu", "Preferences", "true", "ENDCHILDREN", "Shows the View menu on the menu bar", "ViewMenu", "true", "Shows the Goto menu on the View menu", "GotoMenu", "true", "Shows the First Page menuitem on the GoTo menu", "Firstpage", "true", "Shows the Back Page menuitem on the GoTo menu", "Backpage", "true", "Shows the Next Page menuitem on the GoTo menu", "Forwardpage", "true", "Shows the Last Page menuitem on the GoTo menu", "Lastpage", "true", "Shows the GoTo Page menuitem on the GoTo menu", "Goto", "true", "Shows the Previous Document menuitem on the GoTo menu", "Previousdocument", "true", "Shows the Next Document menuitem on the GoTo menu", "Nextdocument", "true", "ENDCHILDREN", "Shows the PageLayout menu on the View menu", "PagelayoutMenu", "true", "Shows the Single Page Mode menuitem on the PageLayout menu", "Single", "true", "Shows the Continuous Page Mode menuitem on the PageLayout menu", "Continuous", "true", "Shows the Facing Page Mode menuitem on the PageLayout menu", "Facing", "true", "Shows the Continuous Facing Page Mode menuitem on the PageLayout menu", "Continuousfacing", "true", "Shows the PageFlow Page Mode menuitem on the PageLayout menu", "PageFlow", "true", "ENDCHILDREN", "Shows the Separate Cover menuitem on the View menu", "separateCover", "true", "Shows the Text Select Mouse Mode menuitem on the View menu", "textSelect", "true", "Shows the Pan Mouse Mode menuitem on the View menu", "panMode", "true", "Shows the Fullscreen menuitem on the View menu", "Fullscreen", "true", "ENDCHILDREN", "Shows the Window menu on the menu bar", "WindowMenu", "true", "Shows the Cascade windows menuitem on the Window menu", "Cascade", "true", "Shows the Tile windows menuitem on the Window menu", "Tile", "true", "ENDCHILDREN", "Shows the Export menu on the menu bar", "ExportMenu", "false", "Shows the Pdf menu on the Export menu", "PdfMenu", "true", "Shows the One per page menuitem on the Pdf menu", "Oneperpage", "true", "Shows the Nup menuitem on the Pdf menu", "Nup", "true", "Shows the Handouts menuitem on the Pdf menu", "Handouts", "true", "ENDCHILDREN", "Shows the Content menu on the Export menu", "ContentMenu", "true", "Shows the Images menuitem on the Content menu", "Images", "true", "Shows the Text menuitem on the Content menu", "Text", "true", "ENDCHILDREN", "Shows the Bitmap menuitem on the Export menu", "Bitmap", "true", "ENDCHILDREN", "Shows the Page Tools menu on the menu bar", "PagetoolsMenu", "false", "Shows the Rotate Page menuitem on the Page Tools menu", "Rotatepages", "true", "Shows the Delete Page menuitem on the Page Tools menu", "Deletepages", "true", "Shows the Add Page menuitem on the Page Tools menu", "Addpage", "true", "Shows the Add header and footer menuitem on the Page Tools menu", "Addheaderfooter", "true", "Shows the stamp text menuitem on the Page Tools menu", "Stamptext", "true", "Shows the stamp image menuitem on the Page Tools menu", "Stampimage", "false", "Shows the crop menuitem on the Page Tools menu", "Crop", "true", "ENDCHILDREN", "Shows the Help menu on the menu bar", "HelpMenu", "true", "Shows the Visit website menuitem on the help menu", "Visitwebsite", "true", "Shows the tip of the day menuitem on the help menu", "Tipoftheday", "true", "Shows the check for updates menuitem on the help menu", "Checkupdates", "true", "Shows the about menuitem on the help menu", "About", "true", "Shows the help forum menuitem on the help menu", "Helpforum", "true", "ENDCHILDREN", "ENDCHILDREN", "Show the content of the Button bar, button bar remain but empty if false", "ButtonsMenu", "true", "Show the open file button on the button bar", "Openfilebutton", "true", "Show the print button on the button bar", "Printbutton", "true", "Show the search button on the button bar", "Searchbutton", "true", "Show the document properties button on the button bar", "Propertiesbutton", "false", "Show the about button on the button bar", "Aboutbutton", "false", "Show the snapshot button on the button bar", "Snapshotbutton", "true", "Show the help button on the button bar", "Helpbutton", "true", "Show the rss feed button on the button bar", "RSSbutton", "true", "Show the cursor button on the button bar", "CursorButton", "true", "Show the mouse mode button on the button bar", "MouseModeButton", "true", "ENDCHILDREN", "Show the contents of the display options bar, Display options bar remain empty if false", "DisplayOptionsMenu", "true", "Show the scaling options on the display options bar", "Scalingdisplay", "true", "Show the rotation options on the display options bar", "Rotationdisplay", "true", "Show the image optimisation options on the display options bar", "Imageopdisplay", "false", "Show the progress bar / display on the display options bar", "Progressdisplay", "true", "Show the download progress display on the display options bar", "Downloadprogressdisplay", "true", "ENDCHILDREN", "Show the contents of the navigation bar, navigation bar remains but empty if false", "NavigationBarMenu", "true", "Show the memory diplay on the navigation bar", "Memorybottom", "true", "Show the first page button on the navigation bar", "Firstbottom", "true", "Show the back 10 pages button on the navigation bar", "Back10bottom", "true", "Show the back 1 page button on the navigation bar", "Backbottom", "true", "Show the goto page button on the navigation bar", "Gotobottom", "true", "Show the forward 1 page button on the navigation bar", "Forwardbottom", "true", "Show the forward 10 page button on the navigation bar", "Forward10bottom", "true", "Show the last page button on the navigation bar", "Lastbottom", "true", "Show the single page display button on the navigation bar", "Singlebottom", "true", "Show the continuous page display button on the navigation bar", "Continuousbottom", "true", "Show the continuous facing page display button on the navigation bar", "Continuousfacingbottom", "true", "Show the facing page display button on the navigation bar", "Facingbottom", "true", "Show the pageflow page display button on the navigation bar", "PageFlowbottom", "true", "ENDCHILDREN", "Show the contents of the side tab bar, side tab remain but is empty if false", "SideTabBarMenu", "true", "Show the page tab, when applicable, on the side tab bar", "Pagetab", "true", "Show the bookmarks tab, when applicable, on the side tab bar", "Bookmarkstab", "true", "Show the layers tab, when applicable, on the side tab bar", "Layerstab", "true", "Show the signatures tab, when applicable, on the side tab bar", "Signaturestab", "true", "ENDCHILDREN", "This removes the menu bar entirely if set to false", "ShowMenubar", "true", "This removes the button bar entirely if set to false", "ShowButtons", "true", "This removes the display options bar entirely if set to false", "ShowDisplayoptions", "true", "This removes the navigation bar entirely if set to false", "ShowNavigationbar", "true", "This removes the side tab bar entirely if set to false", "ShowSidetabbar", "true", "The integer RGB value for the highlight color", "highlightBoxColor", "-16777216", "The integer RGB value for the highlighted text color", "highlightTextColor", "16750900", "Flag to replace document text colors with user defined value", "replaceDocumentTextColors", "false", "Integer RGB value for replace text colors", "vfgColor", "0", "Integer RGB value to replace document page color", "vbgColor", "16777215", "All color values (R,G and B), must be under this value in order to change text color", "TextColorThreshold", "255", "Flag to replace the background color of the display pane", "replacePdfDisplayBackground", "false", "Color to use as display pane color if flag is set", "pdfDisplayBackground", "16777215", "Allows text color change to also change color of shapes and line art", "changeTextAndLineart", "false", "Option to change the background color of the side tab bar background, CURRENTLY INACTIVE", "sbbgColor", "16777215", "Transparency value to be used for the highlight box color", "highlightComposite", "0.35", "This overrides the highlight box color and inverts the color of anything within the highlight area", "invertHighlights", "false", "Flag to open last document upon openning the viewer", "openLastDocument", "false", "Page to open last document to", "lastDocumentPage", "1", "The inset of the page in the play area", "pageInsets", "25", "The length of the tabbed pane when it has been collapsed", "sideTabBarCollapseLength", "30", "The length of the tabbed pane when it has been expanded", "sideTabBarExpandLength", "190", "Keep side tab bar consitent across multiple files", "consistentTabBar", "false", "Flag to allow for the right click menu to be used in the viewer", "allowRightClick", "true", "Flag to allow the mouse scroll wheel to zoom in / out of display", "allowScrollwheelZoom", "true", "Flag to set if the properties file can be modified using the preferences window", "readOnly", "false", "Flag to use enhanced viewer mode", "enhancedViewerMode", "true", "Flag to use enhanced facing mode", "enhancedFacingMode", "true", "Text to use in the window title", "windowTitle", "", "Flag to control if we requestion confirmation to close the viewer", "confirmClose", "false", "Location where the icons to be used by the viewer are stored", "iconLocation", "/org/jpedal/examples/simpleviewer/res/", "Flag if we use the enhance user interface", "enhancedGUI", "true", "Flag to control if we show a message when entering page flow mode", "showpageflowmessage", "true", "Specify a default printer to use", "defaultPrinter", "", "Flag to output additional printer / printing info", "debugPrinter", "false", "Default printing DPI", "defaultDPI", "600", "Default printing page size", "defaultPagesize", "", "List of printers to ignore", "printerBlacklist", "", "Flag to allow the use of hinting for true type fonts", "useHinting", "false", "Voice name to be used for text to speech functionality", "voice", "kevin16(general domain)", "Flag to turn on previews in single page mode when scrolling", "previewOnSingleScroll", "true", "Flag to show the bounding box of the mouse selection", "showMouseSelectionBox", "false", "Flag if we should spearate the cover of document when in facing mode", "separateCoverOn", "true", "Flag to track the users scaling between sessions", "trackScaling", "false", "Scaling value to use on viewer start up, used/modified when tracking between sessions", "startScaling", "Fit Page", "Flag to track the users display mode between sessions", "trackView", "false", "Display Mode value to use on viewer start up, used/modified when tracking between sessions", "startView", "1", "Flag to track the if the side bar tab is open between sessions", "trackSideTabOpen", "false", "Flag if side tab bar should be open on viewer start up, used/modified when tracking between sessions", "startSideTabOpen", "false", "Flag to track the currently selected tab on side bar between sessions", "trackSelectedSideTab", "false", "Side tab bar to be selected on viewer start up / file openning, used/modified when tracking between sessions", "startSelectedSideTab", "Pages", "Flag to track the viewer window size between sessions", "trackViewerSize", "false", "Viewer width to use on viewer start up, used/modified when tracking between sessions", "startViewerWidth", "-1", "Viewer height to use on viewer start up, used/modified when tracking between sessions", "startViewerHeight", "-1", "Flag to track the side tab bar expanded width between sessions", "trackSideTabExpandedSize", "false" };
  int position = 0;
  private boolean endMenu = false;

  public boolean isReadOnly()
  {
    return this.isReadOnly;
  }

  public PropertiesFile()
  {
    try
    {
      String str = getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
      this.userDir = str.substring(0, str.lastIndexOf(47));
      this.configFile = (this.userDir + this.separator + ".properties.xml");
      if ((DecoderOptions.isRunningOnWindows) && (this.configFile.length() > 1))
      {
        this.configFile = this.configFile.substring(1);
        this.configFile = this.configFile.replaceAll("\\\\", "/");
      }
    }
    catch (Exception localException)
    {
      this.userDir = System.getProperty("user.dir");
      this.configFile = (this.userDir + this.separator + ".properties.xml");
    }
  }

  private void showChildren(String paramString, NodeList paramNodeList)
  {
    for (int i = 0; i != paramNodeList.getLength(); i++)
    {
      System.out.println(paramString + "" + paramNodeList.item(i).getNodeType() + " , " + paramNodeList.item(i).getNodeName() + " , " + paramNodeList.item(i).getNodeValue());
      if (paramNodeList.item(i).hasChildNodes())
        showChildren(paramString + "    ", paramNodeList.item(i).getChildNodes());
    }
  }

  public void loadProperties()
  {
    try
    {
      DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
      File localFile = null;
      if (this.configInputStream != null)
      {
        try
        {
          this.doc = localDocumentBuilder.parse(this.configInputStream);
          this.isReadOnly = true;
        }
        catch (Exception localException2)
        {
          this.doc = localDocumentBuilder.newDocument();
        }
      }
      else
      {
        localFile = new File(this.configFile);
        if ((localFile.exists()) && (localFile.length() > 0L))
          try
          {
            this.doc = localDocumentBuilder.parse(localFile);
          }
          catch (Exception localException3)
          {
            this.doc = localDocumentBuilder.newDocument();
          }
        else
          this.doc = localDocumentBuilder.newDocument();
      }
      if ((this.configInputStream == null) && (localFile != null) && ((localFile.canWrite()) || ((!localFile.exists()) && (!localFile.canWrite()))) && (!getValue("readOnly").toLowerCase().equals("true")))
      {
        this.isReadOnly = false;
        boolean bool = checkAllElementsPresent();
        if ((this.refactorProperties) || (!bool))
        {
          this.position = 0;
          localFile.delete();
          localObject1 = (Document)this.doc.cloneNode(true);
          this.doc = localDocumentBuilder.newDocument();
          this.isReadOnly = ((this.isReadOnly) || (getValue("readOnly").toLowerCase().equals("true")));
          checkAllElementsPresent();
          localObject2 = this.doc.getElementsByTagName("recentfiles");
          localObject3 = (Element)((NodeList)localObject2).item(0);
          NodeList localNodeList1 = ((Document)localObject1).getElementsByTagName("recentfiles");
          Element localElement1 = (Element)localNodeList1.item(0);
          NodeList localNodeList2 = localElement1.getChildNodes();
          Object localObject4;
          for (int j = 0; j != localNodeList2.getLength(); j++)
            if (!localNodeList2.item(j).getNodeName().equals("#text"))
            {
              localObject4 = this.doc.createElement("file");
              ((Element)localObject4).setAttribute("name", ((Element)localNodeList2.item(j)).getAttribute("name"));
              ((Element)localObject3).appendChild((Node)localObject4);
            }
          for (j = 0; j != this.properties.length; j++)
            if (!this.properties[j].equals("ENDCHILDREN"))
            {
              j++;
              localObject4 = this.doc.getElementsByTagName(this.properties[j]);
              Element localElement2 = (Element)((NodeList)localObject4).item(0);
              if (localElement2 == null)
              {
                ShowGUIMessage.showGUIMessage("The property " + this.properties[j] + " was either not found in the properties file.", "Property not found.");
              }
              else
              {
                NodeList localNodeList3 = ((Document)localObject1).getElementsByTagName(this.properties[j]);
                Element localElement3 = (Element)localNodeList3.item(0);
                if (localElement3 != null)
                  localElement2.setAttribute("value", localElement3.getAttribute("value"));
              }
              j++;
            }
          if (!this.isTest)
            writeDoc();
        }
        Object localObject1 = getValue("vfgColor");
        Object localObject2 = getValue("vbgColor");
        Object localObject3 = getValue("sbbgColor");
        if ((!((String)localObject1).isEmpty()) && (!((String)localObject1).isEmpty()) && (!((String)localObject1).isEmpty()))
        {
          int i = Integer.parseInt((String)localObject1) + Integer.parseInt((String)localObject2) + Integer.parseInt((String)localObject3);
          if (i == -3)
          {
            setValue("vfgColor", "");
            setValue("vbgColor", "16777215");
            setValue("sbbgColor", "16777215");
          }
        }
      }
      else
      {
        this.isReadOnly = true;
      }
    }
    catch (Exception localException1)
    {
      LogWriter.writeLog("Exception " + localException1 + " generating properties file");
    }
  }

  public void removeRecentDocuments()
  {
    NodeList localNodeList1 = this.doc.getElementsByTagName("recentfiles");
    if ((localNodeList1 != null) && (localNodeList1.getLength() > 0))
    {
      NodeList localNodeList2 = ((Element)localNodeList1.item(0)).getElementsByTagName("*");
      for (int i = 0; i < localNodeList2.getLength(); i++)
      {
        Node localNode = localNodeList2.item(i);
        localNodeList1.item(0).removeChild(localNode);
      }
    }
  }

  public String[] getRecentDocuments()
  {
    String[] arrayOfString;
    try
    {
      NodeList localNodeList1 = this.doc.getElementsByTagName("recentfiles");
      ArrayList localArrayList = new ArrayList();
      if ((localNodeList1 != null) && (localNodeList1.getLength() > 0))
      {
        NodeList localNodeList2 = ((Element)localNodeList1.item(0)).getElementsByTagName("*");
        for (int i = 0; i < localNodeList2.getLength(); i++)
        {
          Node localNode = localNodeList2.item(i);
          NamedNodeMap localNamedNodeMap = localNode.getAttributes();
          localArrayList.add(localNamedNodeMap.getNamedItem("name").getNodeValue());
        }
      }
      while (localArrayList.size() > this.noOfRecentDocs)
        localArrayList.remove(0);
      Collections.reverse(localArrayList);
      arrayOfString = (String[])localArrayList.toArray(new String[this.noOfRecentDocs]);
    }
    catch (Exception localException)
    {
      LogWriter.writeLog("Exception " + localException + " getting recent documents");
      return null;
    }
    return arrayOfString;
  }

  public void addRecentDocument(String paramString)
  {
    try
    {
      Element localElement1 = (Element)this.doc.getElementsByTagName("recentfiles").item(0);
      checkExists(paramString, localElement1);
      Element localElement2 = this.doc.createElement("file");
      localElement2.setAttribute("name", paramString);
      localElement1.appendChild(localElement2);
      removeOldFiles(localElement1);
    }
    catch (Exception localException)
    {
      LogWriter.writeLog("Exception " + localException + " adding recent document to properties file");
    }
  }

  public void setValue(String paramString1, String paramString2)
  {
    try
    {
      NodeList localNodeList = this.doc.getElementsByTagName(paramString1);
      Element localElement = (Element)localNodeList.item(0);
      if ((localElement == null) || (paramString2 == null))
        ShowGUIMessage.showGUIMessage("The property " + paramString1 + " was either not found in the properties file or the value " + paramString2 + " was not set.", "Property not found.");
      else
        localElement.setAttribute("value", paramString2);
      writeDoc();
    }
    catch (Exception localException)
    {
      LogWriter.writeLog("Exception " + localException + " setting value in properties file");
    }
  }

  public NodeList getChildren(String paramString)
  {
    return this.doc.getElementsByTagName(paramString).item(0).getChildNodes();
  }

  public String getValue(String paramString)
  {
    NamedNodeMap localNamedNodeMap;
    try
    {
      NodeList localNodeList = this.doc.getElementsByTagName(paramString);
      Element localElement = (Element)localNodeList.item(0);
      if (localElement == null)
        return "";
      localNamedNodeMap = localElement.getAttributes();
    }
    catch (Exception localException)
    {
      LogWriter.writeLog("Exception " + localException + " generating properties file");
      return "";
    }
    return localNamedNodeMap.getNamedItem("value").getNodeValue();
  }

  private void removeOldFiles(Element paramElement)
    throws Exception
  {
    NodeList localNodeList = paramElement.getElementsByTagName("*");
    while (localNodeList.getLength() > this.noOfRecentDocs)
      paramElement.removeChild(localNodeList.item(0));
  }

  private static void checkExists(String paramString, Element paramElement)
    throws Exception
  {
    NodeList localNodeList = paramElement.getElementsByTagName("*");
    for (int i = 0; i < localNodeList.getLength(); i++)
    {
      Node localNode = localNodeList.item(i);
      NamedNodeMap localNamedNodeMap = localNode.getAttributes();
      String str = localNamedNodeMap.getNamedItem("name").getNodeValue();
      if (str.equals(paramString))
        paramElement.removeChild(localNode);
    }
  }

  public void writeDoc()
    throws Exception
  {
    if ((!this.isReadOnly) && (!getValue("readOnly").toLowerCase().equals("true")))
    {
      InputStream localInputStream = getClass().getResourceAsStream("/org/jpedal/examples/viewer/res/xmlstyle.xslt");
      StreamResult localStreamResult = new StreamResult(this.configFile);
      StreamSource localStreamSource = new StreamSource(localInputStream);
      DOMSource localDOMSource = new DOMSource(this.doc);
      TransformerFactory localTransformerFactory = TransformerFactory.newInstance();
      Transformer localTransformer = localTransformerFactory.newTransformer(localStreamSource);
      localTransformer.transform(localDOMSource, localStreamResult);
      localInputStream.close();
      if (localStreamSource != null)
        localStreamSource.getInputStream().close();
      if ((localStreamResult != null) && (localStreamResult.getOutputStream() != null))
        localStreamResult.getOutputStream().close();
    }
  }

  public void dispose()
  {
    this.doc = null;
    this.properties = null;
    this.configFile = null;
  }

  private boolean checkAllElementsPresent()
    throws Exception
  {
    NodeList localNodeList = this.doc.getElementsByTagName("*");
    ArrayList localArrayList = new ArrayList(localNodeList.getLength());
    for (int i = 0; i < localNodeList.getLength(); i++)
      localArrayList.add(localNodeList.item(i).getNodeName());
    Element localElement;
    Object localObject;
    if (localArrayList.contains("properties"))
    {
      localElement = (Element)this.doc.getElementsByTagName("properties").item(0);
    }
    else
    {
      localObject = DocumentBuilderFactory.newInstance();
      DocumentBuilder localDocumentBuilder = ((DocumentBuilderFactory)localObject).newDocumentBuilder();
      this.doc = localDocumentBuilder.newDocument();
      localElement = this.doc.createElement("properties");
      this.doc.appendChild(localElement);
    }
    if (!localArrayList.contains("recentfiles"))
    {
      localObject = this.doc.createElement("recentfiles");
      localElement.appendChild((Node)localObject);
    }
    localNodeList = localElement.getChildNodes();
    boolean bool = addProperties(localNodeList, localElement);
    return bool;
  }

  private boolean addMenuElement(NodeList paramNodeList, Element paramElement)
  {
    boolean bool = true;
    int i = -1;
    int j = -1;
    for (int k = 0; k < paramNodeList.getLength(); k++)
    {
      if (paramNodeList.item(k).getNodeName().equals(this.properties[(this.position + 1)]))
        i = k;
      if ((paramNodeList.item(k).getNodeType() == 8) && (paramNodeList.item(k).getNodeValue().equals(this.properties[this.position])))
        j = k;
      if ((i != -1) && (j != -1))
        k = paramNodeList.getLength();
    }
    Element localElement;
    if (i == -1)
    {
      paramElement.appendChild(this.doc.createComment(this.properties[this.position]));
      this.position += 1;
      localElement = this.doc.createElement(this.properties[this.position]);
      this.position += 1;
      localElement.setAttribute("value", this.properties[this.position]);
      paramElement.appendChild(localElement);
      this.position += 1;
      addProperties(paramNodeList, localElement);
      bool = false;
    }
    else
    {
      localElement = (Element)this.doc.getElementsByTagName(this.properties[(this.position + 1)]).item(0);
      if (j == -1)
        paramElement.insertBefore(this.doc.createComment(this.properties[this.position]), paramNodeList.item(i));
      this.position += 1;
      this.position += 1;
      this.position += 1;
      addProperties(paramNodeList, localElement);
    }
    return bool;
  }

  private boolean addChildElements(NodeList paramNodeList, Element paramElement)
  {
    boolean bool = true;
    if (!this.properties[this.position].equals("ENDCHILDREN"))
    {
      int i = -1;
      int j = -1;
      for (int k = 0; k < paramNodeList.getLength(); k++)
      {
        if (paramNodeList.item(k).getNodeName().equals(this.properties[(this.position + 1)]))
          i = k;
        if ((paramNodeList.item(k).getNodeType() == 8) && (paramNodeList.item(k).getNodeValue().equals(this.properties[this.position])))
          j = k;
        if ((i != -1) && (j != -1))
          k = paramNodeList.getLength();
      }
      Object localObject;
      if (i == -1)
      {
        paramElement.appendChild(this.doc.createComment(this.properties[this.position]));
        this.position += 1;
        localObject = this.doc.createElement(this.properties[this.position]);
        this.position += 1;
        ((Element)localObject).setAttribute("value", this.properties[this.position]);
        paramElement.appendChild((Node)localObject);
        bool = false;
      }
      else
      {
        if (j == -1)
          paramElement.insertBefore(this.doc.createComment(this.properties[this.position]), paramNodeList.item(i));
        this.position += 1;
        if (this.properties[this.position].equals("currentversion"))
        {
          localObject = this.doc.getElementsByTagName(this.properties[this.position]);
          Element localElement = (Element)((NodeList)localObject).item(0);
          if (localElement == null)
          {
            ShowGUIMessage.showGUIMessage("The property " + this.properties[this.position] + " was either not found in the properties file.", "Property not found.");
          }
          else if (!this.properties[(this.position + 1)].equals("5.06b04"))
          {
            float f1 = Float.parseFloat("5.06b04".substring(0, 4));
            String str1 = "0";
            String str2 = localElement.getAttribute("value");
            if (str2.length() > 3)
              str1 = str2.substring(0, 4);
            float f2 = Float.parseFloat(str1);
            if (f1 > f2)
            {
              localElement.setAttribute("value", "5.06b04");
              this.refactorProperties = true;
            }
          }
        }
        this.position += 1;
      }
    }
    else
    {
      this.endMenu = true;
    }
    this.position += 1;
    return bool;
  }

  private boolean addProperties(NodeList paramNodeList, Element paramElement)
  {
    boolean bool1 = true;
    while (this.position < this.properties.length)
    {
      boolean bool2;
      if (this.properties[(this.position + 1)].endsWith("Menu"))
      {
        bool2 = addMenuElement(paramNodeList, paramElement);
      }
      else
      {
        bool2 = addChildElements(paramNodeList, paramElement);
        if (this.endMenu)
        {
          this.endMenu = false;
          return bool1;
        }
      }
      if (!bool2)
        bool1 = false;
    }
    return bool1;
  }

  public int getNoRecentDocumentsToDisplay()
  {
    return this.noOfRecentDocs;
  }

  public String getConfigFile()
  {
    return this.configFile;
  }

  public void loadProperties(InputStream paramInputStream)
  {
    this.configInputStream = paramInputStream;
    loadProperties();
  }

  public void loadProperties(String paramString)
  {
    Object localObject;
    if (paramString.startsWith("jar:"))
    {
      paramString = paramString.substring(4);
      localObject = getClass().getResourceAsStream(paramString);
      if (localObject != null)
        this.configInputStream = ((InputStream)localObject);
      else
        throw new RuntimeException("unable to open resource stream for " + paramString);
    }
    else
    {
      if (paramString.startsWith("http:"))
        try
        {
          localObject = new URL(paramString);
          URLConnection localURLConnection = ((URL)localObject).openConnection();
          localURLConnection.setDoOutput(true);
          this.configInputStream = ((URL)localObject).openStream();
        }
        catch (MalformedURLException localMalformedURLException)
        {
          localMalformedURLException.printStackTrace();
        }
        catch (IOException localIOException)
        {
          localIOException.printStackTrace();
        }
      if (this.configInputStream == null)
      {
        File localFile = new File(paramString);
        if ((localFile.exists()) || ((!localFile.exists()) && (!localFile.canWrite())))
          this.configFile = paramString;
        else
          throw new RuntimeException();
        this.isReadOnly = (!localFile.canWrite());
      }
    }
    loadProperties();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.utils.PropertiesFile
 * JD-Core Version:    0.6.2
 */