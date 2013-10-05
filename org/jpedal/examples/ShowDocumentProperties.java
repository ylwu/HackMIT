package org.jpedal.examples;

import java.io.File;
import java.io.PrintStream;
import org.jpedal.PdfDecoderInt;
import org.jpedal.PdfDecoderServer;
import org.jpedal.objects.PdfFileInformation;

public class ShowDocumentProperties
{
  private String user_dir = System.getProperty("user.dir");
  private static String test_file = "/mnt/win_d/sample.pdf";

  public ShowDocumentProperties(String paramString)
  {
    String str = System.getProperty("file.separator");
    if (!this.user_dir.endsWith(str))
      this.user_dir += str;
    PdfDecoderServer localPdfDecoderServer = null;
    try
    {
      localPdfDecoderServer = new PdfDecoderServer(false);
      System.out.println("Opening file :" + paramString);
      localPdfDecoderServer.openPdfFile(paramString);
    }
    catch (Exception localException)
    {
      System.err.println("3.Exception " + localException + " in pdf code");
    }
    if ((localPdfDecoderServer.isEncrypted()) && (!localPdfDecoderServer.isExtractionAllowed()))
    {
      System.out.println("Encrypted settings");
      System.out.println("Please look at Viewer for code sample to handle such files");
      System.out.println("Or get support/consultancy");
    }
    PdfFileInformation localPdfFileInformation = localPdfDecoderServer.getFileInformationData();
    String[] arrayOfString1 = localPdfFileInformation.getFieldValues();
    String[] arrayOfString2 = PdfFileInformation.getFieldNames();
    int i = arrayOfString2.length;
    System.out.println("Fields");
    System.out.println("======");
    for (int j = 0; j < i; j++)
      System.out.println(arrayOfString2[j] + " = " + arrayOfString1[j]);
    System.out.println("\nMetadata");
    System.out.println("======");
    System.out.println(localPdfFileInformation.getFileXMLMetaData());
    localPdfDecoderServer.closePdfFile();
  }

  public static void main(String[] paramArrayOfString)
  {
    System.out.println("Simple demo to extract pdf file properties");
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
      new ShowDocumentProperties(str);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.ShowDocumentProperties
 * JD-Core Version:    0.6.2
 */