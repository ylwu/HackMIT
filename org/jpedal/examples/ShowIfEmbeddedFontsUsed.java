package org.jpedal.examples;

import java.io.File;
import java.io.PrintStream;
import org.jpedal.PdfDecoderInt;
import org.jpedal.PdfDecoderServer;

public class ShowIfEmbeddedFontsUsed
{
  private String user_dir = System.getProperty("user.dir");
  private static String test_file = "/mnt/win_d/sample.pdf";

  public ShowIfEmbeddedFontsUsed(String paramString)
  {
    String str = System.getProperty("file.separator");
    if (!this.user_dir.endsWith(str))
      this.user_dir += str;
    try
    {
      PdfDecoderServer localPdfDecoderServer = new PdfDecoderServer(false);
      System.out.println("Opening file :" + paramString);
      localPdfDecoderServer.openPdfFile(paramString);
      System.out.println("File contains embedded fonts=" + localPdfDecoderServer.PDFContainsEmbeddedFonts());
      localPdfDecoderServer.closePdfFile();
    }
    catch (Exception localException)
    {
      System.err.println("2.Exception " + localException + " in pdf code");
    }
  }

  public static void main(String[] paramArrayOfString)
  {
    System.out.println("Simple demo to see if file contains embedded fonts");
    String str = test_file;
    if (paramArrayOfString.length != 1)
    {
      System.out.println("Please pass the file name and any path (ie \"C:/sample.pdf\" ) as a command line value - use double quotes if it includes spaces");
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
      new ShowIfEmbeddedFontsUsed(str);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.ShowIfEmbeddedFontsUsed
 * JD-Core Version:    0.6.2
 */