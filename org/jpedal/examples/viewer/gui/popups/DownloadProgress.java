package org.jpedal.examples.viewer.gui.popups;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.jpedal.io.ObjectStore;
import org.jpedal.utils.LogWriter;

public class DownloadProgress
{
  File tempURLFile;
  boolean isDownloading = true;
  int progress = 0;
  private String pdfUrl;

  public DownloadProgress(String paramString)
  {
    this.pdfUrl = paramString;
  }

  public void startDownload()
  {
    try
    {
      int j = 0;
      this.progress = 0;
      String str1 = "file.pdf";
      InputStream localInputStream;
      if (this.pdfUrl.startsWith("jar:/"))
      {
        localInputStream = getClass().getResourceAsStream(this.pdfUrl.substring(4));
      }
      else
      {
        URL localURL = new URL(this.pdfUrl);
        localInputStream = localURL.openStream();
        str1 = localURL.getPath().substring(localURL.getPath().lastIndexOf('/') + 1);
        int i = localURL.openConnection().getContentLength();
        j = i / 100;
      }
      String str2 = str1;
      this.tempURLFile = ObjectStore.createTempFile(str2);
      FileOutputStream localFileOutputStream = new FileOutputStream(this.tempURLFile);
      byte[] arrayOfByte = new byte[4096];
      int m = 0;
      int k;
      while ((k = localInputStream.read(arrayOfByte)) != -1)
      {
        m += k;
        this.progress = (m / j);
        localFileOutputStream.write(arrayOfByte, 0, k);
      }
      localFileOutputStream.flush();
      localInputStream.close();
      localFileOutputStream.close();
      this.progress = 100;
    }
    catch (Exception localException)
    {
      LogWriter.writeLog("[PDF] Exception " + localException + " opening URL " + this.pdfUrl);
      localException.printStackTrace();
      this.progress = 100;
    }
    this.isDownloading = false;
  }

  public File getFile()
  {
    return this.tempURLFile;
  }

  public boolean isDownloading()
  {
    return this.isDownloading;
  }

  public int getProgress()
  {
    return this.progress;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.popups.DownloadProgress
 * JD-Core Version:    0.6.2
 */