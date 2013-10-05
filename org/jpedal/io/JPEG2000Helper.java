package org.jpedal.io;

import com.sun.media.imageio.plugins.jpeg2000.J2KImageReadParam;
import com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReader;
import com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReaderSpi;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import org.jpedal.exception.PdfException;
import org.jpedal.utils.LogWriter;

public class JPEG2000Helper
{
  public BufferedImage getJPEG2000OnServer(byte[] paramArrayOfByte)
    throws PdfException
  {
    ImageInputStream localImageInputStream = null;
    BufferedImage localBufferedImage = null;
    try
    {
      localImageInputStream = ImageIO.createImageInputStream(new ByteArrayInputStream(paramArrayOfByte));
      J2KImageReaderSpi localJ2KImageReaderSpi = new J2KImageReaderSpi();
      localObject1 = new J2KImageReader(localJ2KImageReaderSpi);
      ((J2KImageReader)localObject1).setInput(localImageInputStream, true);
      localBufferedImage = ((J2KImageReader)localObject1).read(0, new J2KImageReadParam());
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      if (LogWriter.isOutput())
        LogWriter.writeLog("Problem reading JPEG 2000: " + localException);
      Object localObject1 = "Exception " + localException + " with JPeg 2000 Image";
      if (!JAIHelper.isJAIused())
        localObject1 = "JPeg 2000 Images and JAI not setup.\nYou need both JAI and imageio.jar on classpath, and the VM parameter -Dorg.jpedal.jai=true switch turned on";
      throw new PdfException((String)localObject1);
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
        if (localImageInputStream != null)
          localImageInputStream.close();
      }
      catch (IOException localIOException2)
      {
        LogWriter.writeLog("Problem reading JPEG 2000: " + localIOException2);
        localIOException2.printStackTrace();
      }
    }
    return localBufferedImage;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.JPEG2000Helper
 * JD-Core Version:    0.6.2
 */