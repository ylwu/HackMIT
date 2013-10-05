package org.jpedal.examples;

import java.io.File;
import java.io.PrintStream;
import org.jpedal.PdfDecoderInt;
import org.jpedal.PdfDecoderServer;

public class PageCount
{
  private String user_dir = System.getProperty("user.dir");
  private static String test_file = "/mnt/win_d/sample.pdf";

  public PageCount(String paramString)
  {
    String str = System.getProperty("file.separator");
    if (!this.user_dir.endsWith(str))
      this.user_dir += str;
    try
    {
      PdfDecoderServer localPdfDecoderServer = new PdfDecoderServer(false);
      System.out.println("Opening file :" + paramString);
      localPdfDecoderServer.openPdfFile(paramString);
      System.out.println("Page count=" + localPdfDecoderServer.getPageCount());
      localPdfDecoderServer.closePdfFile();
    }
    catch (Exception localException)
    {
      System.err.println("5.Exception " + localException + " in pdf code");
    }
  }

  public static void main(String[] paramArrayOfString)
  {
    System.out.println("Simple demo to extract number of pages");
    String str = test_file;
    if (paramArrayOfString.length != 1)
    {
      System.out.println("Default test file used");
    }
    else
    {
      str = paramArrayOfString[0];
      System.out.println("File :" + str);
    }
    File localFile = new File(str);
    if (!localFile.exists())
      System.out.println("File " + str + " not found");
    else
      new PageCount(str);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.PageCount
 * JD-Core Version:    0.6.2
 */