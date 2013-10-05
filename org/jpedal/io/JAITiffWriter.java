package org.jpedal.io;

import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.TIFFEncodeParam;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class JAITiffWriter
{
  public static void saveAsTiff(boolean paramBoolean1, boolean paramBoolean2, BufferedImage paramBufferedImage, String paramString, boolean paramBoolean3, BufferedImage[] paramArrayOfBufferedImage)
    throws IOException
  {
    TIFFEncodeParam localTIFFEncodeParam = new TIFFEncodeParam();
    if (paramBoolean1)
      localTIFFEncodeParam.setCompression(32773);
    if (!paramBoolean2)
    {
      JAIHelper.encode(paramBufferedImage, new BufferedOutputStream(new FileOutputStream(paramString)), "TIFF", localTIFFEncodeParam);
    }
    else if (paramBoolean3)
    {
      BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(paramString));
      ImageEncoder localImageEncoder = ImageCodec.createImageEncoder("TIFF", localBufferedOutputStream, localTIFFEncodeParam);
      ArrayList localArrayList = new ArrayList();
      localArrayList.addAll(Arrays.asList(paramArrayOfBufferedImage).subList(1, paramArrayOfBufferedImage.length));
      localTIFFEncodeParam.setExtraImages(localArrayList.iterator());
      localImageEncoder.encode(paramArrayOfBufferedImage[0]);
      localBufferedOutputStream.close();
    }
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.JAITiffWriter
 * JD-Core Version:    0.6.2
 */