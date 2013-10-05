package org.jpedal.io;

import com.sun.media.jai.codec.TIFFEncodeParam;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkEvent.EventType;
import javax.swing.event.HyperlinkListener;
import org.jpedal.exception.PdfException;
import org.jpedal.parser.DecoderOptions;
import org.jpedal.utils.BrowserLauncher;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;

public class JAIHelper
{
  private static boolean JAI_TESTED;
  private static boolean useJAI = false;
  private static boolean isJAIFound = false;

  public static void confirmJAIOnClasspath()
  {
    if (JAI_TESTED)
      return;
    JAI_TESTED = true;
    if (useJAI)
      try
      {
        Class.forName("javax.media.jai.JAI");
        isJAIFound = true;
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        LogWriter.writeLog("org.jpedal.jai set to true but unable to load JAI");
        if (DecoderOptions.showErrorMessages)
        {
          String str = Messages.getMessage("PdfViewer.JAINotOnClasspathWarning") + Messages.getMessage("PdfViewer.JAINotOnClasspathWarning1") + Messages.getMessage("PdfViewer.JAINotOnClasspathWarning2");
          if (str.contains("PdfViewer"))
            str = "We recommend you add JAI to classpath";
          JEditorPane localJEditorPane = new JEditorPane("text/html", str);
          localJEditorPane.setEditable(false);
          localJEditorPane.setOpaque(false);
          localJEditorPane.addHyperlinkListener(new HyperlinkListener()
          {
            public void hyperlinkUpdate(HyperlinkEvent paramAnonymousHyperlinkEvent)
            {
              if (paramAnonymousHyperlinkEvent.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
                try
                {
                  BrowserLauncher.openURL("http://java.sun.com/products/java-media/jai/current.html");
                }
                catch (IOException localIOException)
                {
                  if (DecoderOptions.showErrorMessages)
                    JOptionPane.showMessageDialog(null, Messages.getMessage("PdfViewer.ErrorWebsite"));
                  if (LogWriter.isOutput())
                    LogWriter.writeLog("Exception: " + localIOException.getMessage());
                }
            }
          });
          if (DecoderOptions.showErrorMessages)
            JOptionPane.showMessageDialog(null, localJEditorPane);
        }
      }
  }

  public static boolean isJAIused()
  {
    return useJAI;
  }

  public static boolean isJAIOnClasspath()
  {
    if (!isJAIFound)
    {
      String str = System.getProperty("java.class.path");
      if (str.contains("jai"))
        isJAIFound = true;
    }
    return isJAIFound;
  }

  public static void useJAI(boolean paramBoolean)
  {
    useJAI = paramBoolean;
    JAI_TESTED = false;
  }

  public static BufferedImage getJPEG2000(byte[] paramArrayOfByte)
    throws PdfException
  {
    BufferedImage localBufferedImage = null;
    try
    {
      ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
      ImageReader localImageReader = (ImageReader)ImageIO.getImageReadersByFormatName("JPEG2000").next();
      if (localImageReader == null)
        return null;
      ImageInputStream localImageInputStream = ImageIO.createImageInputStream(localByteArrayInputStream);
      try
      {
        localImageReader.setInput(localImageInputStream, true);
        localBufferedImage = localImageReader.read(0);
      }
      catch (Exception localException2)
      {
        if (LogWriter.isOutput())
          LogWriter.writeLog("Problem reading JPEG 2000: " + localException2);
        localException2.printStackTrace();
        localBufferedImage = null;
      }
      finally
      {
        localImageReader.dispose();
        localImageInputStream.close();
        localByteArrayInputStream.close();
      }
    }
    catch (Exception localException1)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Problem reading JPEG 2000: " + localException1);
      String str = "Exception " + localException1 + " with JPeg 2000 Image";
      if (!isJAIused())
        str = "JPeg 2000 Images and JAI not setup.\nYou need both JAI and imageio.jar on classpath, and the VM parameter -Dorg.jpedal.jai=true switch turned on";
      throw new PdfException(str);
    }
    catch (Error localError)
    {
      localError.printStackTrace();
      if (LogWriter.isOutput())
        LogWriter.writeLog("Problem reading JPEG 2000: " + localError);
      throw new PdfException("JPeg 2000 Images need both JAI (imageio.jar) on classpath, and the VM parameter -Dorg.jpedal.jai=true switch turned on");
    }
    return localBufferedImage;
  }

  public static BufferedImage getJPEG2000OnServer(byte[] paramArrayOfByte)
    throws PdfException
  {
    Object localObject1 = null;
    BufferedImage localBufferedImage = null;
    try
    {
      Class localClass = Class.forName("org.jpedal.io.JPEG2000Helper");
      localObject2 = localClass.getDeclaredMethod("getJPEG2000OnServer", new Class[] { [B.class });
      localBufferedImage = (BufferedImage)((Method)localObject2).invoke(localClass.newInstance(), new Object[] { paramArrayOfByte });
    }
    catch (Exception localException)
    {
      if (LogWriter.isOutput())
        LogWriter.writeLog("Problem reading JPEG 2000: " + localException);
      Object localObject2 = "Exception " + localException + " with JPeg 2000 Image";
      if (!isJAIused())
        localObject2 = "JPeg 2000 Images and JAI not setup.\nYou need both JAI and imageio.jar on classpath, and the VM parameter -Dorg.jpedal.jai=true switch turned on";
      throw new PdfException((String)localObject2);
    }
    catch (Error localError)
    {
      localError.printStackTrace();
      if (LogWriter.isOutput())
        LogWriter.writeLog("Problem reading JPEG 2000: " + localError);
      throw new PdfException("JPeg 2000 Images need both JAI (imageio.jar) on classpath, and the VM parameter -Dorg.jpedal.jai=true switch turned on");
    }
    finally
    {
      try
      {
        if (localObject1 != null)
          localObject1.close();
      }
      catch (IOException localIOException2)
      {
        LogWriter.writeLog("Problem reading JPEG 2000: " + localIOException2);
        localIOException2.printStackTrace();
      }
    }
    return localBufferedImage;
  }

  public static void filestore(BufferedImage paramBufferedImage, String paramString1, String paramString2)
  {
    JAI.create("filestore", paramBufferedImage, paramString1, paramString2);
  }

  public static BufferedImage fileload(String paramString)
  {
    return JAI.create("fileload", paramString).getAsBufferedImage();
  }

  public static void encode(BufferedImage paramBufferedImage, OutputStream paramOutputStream, String paramString, boolean paramBoolean)
  {
    TIFFEncodeParam localTIFFEncodeParam = null;
    if (paramBoolean)
    {
      localTIFFEncodeParam = new TIFFEncodeParam();
      localTIFFEncodeParam.setCompression(32946);
    }
    JAI.create("encode", paramBufferedImage, paramOutputStream, paramString, localTIFFEncodeParam);
  }

  public static void encode(BufferedImage paramBufferedImage, OutputStream paramOutputStream, String paramString, TIFFEncodeParam paramTIFFEncodeParam)
  {
    JAI.create("encode", paramBufferedImage, paramOutputStream, paramString, paramTIFFEncodeParam);
  }

  public static void encode(BufferedImage paramBufferedImage, OutputStream paramOutputStream, String paramString)
  {
    JAI.create("encode", paramBufferedImage, paramOutputStream, paramString);
  }

  public static void saveAsTiff(boolean paramBoolean1, boolean paramBoolean2, BufferedImage paramBufferedImage, String paramString, boolean paramBoolean3, BufferedImage[] paramArrayOfBufferedImage)
    throws IOException
  {
    JAITiffWriter.saveAsTiff(paramBoolean1, paramBoolean2, paramBufferedImage, paramString, paramBoolean3, paramArrayOfBufferedImage);
  }

  public static BufferedImage affine(BufferedImage paramBufferedImage, AffineTransform paramAffineTransform)
  {
    return JAI.create("affine", paramBufferedImage, paramAffineTransform, new InterpolationNearest()).getAsBufferedImage();
  }

  static
  {
    String str = System.getProperty("org.jpedal.jai");
    if ((str != null) && (str.toLowerCase().equals("true")))
      useJAI = true;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.JAIHelper
 * JD-Core Version:    0.6.2
 */