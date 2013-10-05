package org.jpedal.examples.images;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import org.jpedal.PdfDecoderInt;
import org.jpedal.PdfDecoderServer;
import org.jpedal.constants.JPedalSettings;
import org.jpedal.objects.PdfPageData;

public class ConvertPagesToGoogleMaps
{
  public static void main(String[] paramArrayOfString)
  {
    try
    {
      if (paramArrayOfString.length != 2)
        throw new Exception("Arguments incorrect. Arguments are \"/PDF_Location/pdf.pdf\" \"/Output_Directory/\"");
      if (!paramArrayOfString[0].endsWith(".pdf"))
        throw new Exception(new StringBuilder().append(paramArrayOfString[0]).append(" not a PDF.").toString());
      File localFile1 = new File(paramArrayOfString[0]);
      if (!localFile1.exists())
        throw new Exception(new StringBuilder().append(localFile1.getAbsolutePath()).append(" does not exist.").toString());
      File localFile2 = new File(paramArrayOfString[1]);
      if (!localFile2.exists())
        throw new Exception(new StringBuilder().append(localFile2.getAbsolutePath()).append(" does not exist.").toString());
      PdfDecoderServer localPdfDecoderServer = new PdfDecoderServer(true);
      localPdfDecoderServer.openPdfFile(paramArrayOfString[0]);
      String str1 = localFile1.getName().substring(0, localFile1.getName().length() - 4);
      localFile2 = new File(localFile2, str1);
      localFile2.mkdir();
      int i = localPdfDecoderServer.getPageCount();
      String str2;
      for (int j = 1; j <= i; j++)
      {
        str2 = getPageAsString(j, i);
        new File(new StringBuilder().append(localFile2.getAbsolutePath()).append(File.separator).append(str2).append(File.separator).toString()).mkdir();
        PdfPageData localPdfPageData = localPdfDecoderServer.getPdfPageData();
        int k = localPdfPageData.getCropBoxWidth(j);
        int m = localPdfPageData.getCropBoxHeight(j);
        for (int n = 1; n <= 4; n++)
        {
          int i1 = (int)Math.sqrt((int)Math.pow(4.0D, n));
          int i2 = 256 * i1;
          float f;
          if (k > m)
            f = i2 / k;
          else
            f = i2 / m;
          BufferedImage localBufferedImage1;
          if (f > 1.0F)
          {
            localObject = new HashMap();
            ((Map)localObject).put(JPedalSettings.EXTRACT_AT_PAGE_SIZE, new String[] { String.valueOf(i2), String.valueOf(i2) });
            ((Map)localObject).put(JPedalSettings.PAGE_SIZE_OVERRIDES_IMAGE, Boolean.TRUE);
            if (localObject != null)
              localPdfDecoderServer.modifyNonstaticJPedalParameters((Map)localObject);
            localBufferedImage1 = localPdfDecoderServer.getPageAsHiRes(j);
          }
          else
          {
            localPdfDecoderServer.setPageParameters(f, j);
            localBufferedImage1 = localPdfDecoderServer.getPageAsImage(j);
          }
          Object localObject = new BufferedImage(i2, i2, 2);
          Graphics2D localGraphics2D = ((BufferedImage)localObject).createGraphics();
          int i3 = (i2 - localBufferedImage1.getWidth()) / 2;
          int i4 = (i2 - localBufferedImage1.getHeight()) / 2;
          localGraphics2D.drawImage(localBufferedImage1, i3, i4, localBufferedImage1.getWidth(), localBufferedImage1.getHeight(), null);
          for (int i5 = 0; i5 < i1; i5++)
          {
            int i6 = i5 * 256;
            for (int i7 = 0; i7 < i1; i7++)
            {
              int i8 = i7 * 256;
              BufferedImage localBufferedImage2 = ((BufferedImage)localObject).getSubimage(i8, i6, 256, 256);
              ImageIO.write(localBufferedImage2, "png", new FileOutputStream(new StringBuilder().append(localFile2.getAbsolutePath()).append(File.separator).append(str2).append(File.separator).append("tile_").append(n).append('_').append(i7).append('-').append(i5).append(".png").toString()));
            }
          }
        }
        try
        {
          BufferedOutputStream localBufferedOutputStream2 = new BufferedOutputStream(new FileOutputStream(new StringBuilder().append(localFile2.getAbsolutePath()).append(File.separator).append(str2).append(".html").toString()));
          String str3 = j > 1 ? getPageAsString(j - 1, i) : null;
          String str4 = j < i ? getPageAsString(j + 1, i) : null;
          localBufferedOutputStream2.write(getHTML(str1, str2, str3, str4, new StringBuilder().append("").append(i).toString(), 1, 4).getBytes());
          localBufferedOutputStream2.flush();
          localBufferedOutputStream2.close();
        }
        catch (Exception localException3)
        {
          localException3.printStackTrace();
        }
        System.out.println(new StringBuilder().append("Page ").append(j).append(" completed!").toString());
      }
      localPdfDecoderServer.closePdfFile();
      try
      {
        BufferedOutputStream localBufferedOutputStream1 = new BufferedOutputStream(new FileOutputStream(new StringBuilder().append(localFile2.getAbsolutePath()).append(File.separator).append("index.html").toString()));
        str2 = new StringBuilder().append("<!DOCTYPE html><html><head><meta http-equiv=\"Refresh\" content=\"0; url=").append(getPageAsString(1, i)).append(".html\"></head><body></body></html>").toString();
        localBufferedOutputStream1.write(str2.getBytes());
        localBufferedOutputStream1.flush();
        localBufferedOutputStream1.close();
      }
      catch (Exception localException2)
      {
        localException2.printStackTrace();
      }
    }
    catch (Exception localException1)
    {
      System.out.println(new StringBuilder().append("Failed: ").append(localException1.getMessage()).toString());
      localException1.printStackTrace();
    }
  }

  private static String getPageAsString(int paramInt1, int paramInt2)
  {
    int i = new StringBuilder().append("").append(paramInt2).toString().length() - new StringBuilder().append("").append(paramInt1).toString().length();
    String str = "";
    for (int j = 0; j < i; j++)
      str = new StringBuilder().append(str).append("0").toString();
    str = new StringBuilder().append(str).append("").append(paramInt1).toString();
    return str;
  }

  private static String getHTML(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, int paramInt1, int paramInt2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("<!DOCTYPE html>\n");
    localStringBuilder.append("<html lang=\"en\">\n");
    localStringBuilder.append("<head>\n");
    localStringBuilder.append("\t<meta charset=\"utf-8\" />\n");
    localStringBuilder.append("\t<title>").append(paramString1).append(" Page ").append(paramString2).append("</title>\n");
    localStringBuilder.append("</head>\n");
    localStringBuilder.append('\n');
    localStringBuilder.append("<body>\n");
    localStringBuilder.append("\t<div id=\"page").append(paramString2).append("\" style=\"width:1050px;height:590px;margin:10px auto;border:2px solid #000;\"></div>\n");
    localStringBuilder.append("\t<center>").append(paramString3 != null ? new StringBuilder().append("<a href=\"").append(paramString3).append(".html\" >&lt;&lt;</a>").toString() : "&lt;&lt;").append(" Page ").append(paramString2).append(" of ").append(paramString5).append(' ').append(paramString4 != null ? new StringBuilder().append("<a href=\"").append(paramString4).append(".html\" > &gt;&gt;</a>").toString() : "&gt;&gt;").append("</center>\n");
    localStringBuilder.append('\n');
    localStringBuilder.append("\t<script type=\"text/javascript\" src=\"http://maps.google.com/maps/api/js?libraries=geometry&sensor=false\"></script>\n");
    localStringBuilder.append("\t<script type=\"text/javascript\">\n");
    localStringBuilder.append("\t/* <![CDATA[ */\n");
    localStringBuilder.append("\t\t// Google Maps Demo\n");
    localStringBuilder.append("\t\tvar Demo = Demo || {};\n");
    localStringBuilder.append("\t\tDemo.ImagesBaseUrl = '';\n");
    localStringBuilder.append('\n');
    localStringBuilder.append("\t\t//Page").append(paramString2).append('\n');
    localStringBuilder.append("\t\tDemo.Page").append(paramString2).append(" = function (container) {\n");
    localStringBuilder.append("\t\t\t// Create map\n");
    localStringBuilder.append("\t\t\tthis._map = new google.maps.Map(container, {\n");
    localStringBuilder.append("\t\t\t\tzoom: ").append(paramInt1).append(",\n");
    localStringBuilder.append("\t\t\t\tcenter: new google.maps.LatLng(0, -20),\n");
    localStringBuilder.append("\t\t\t\tmapTypeControl: false,\n");
    localStringBuilder.append("\t\t\t\tstreetViewControl: false\n");
    localStringBuilder.append("\t\t\t});\n");
    localStringBuilder.append('\n');
    localStringBuilder.append("\t\t\t// Set custom tiles\n");
    localStringBuilder.append("\t\t\tthis._map.mapTypes.set('").append(paramString2).append("', new Demo.ImgMapType('").append(paramString2).append("', '#E5E3DF'));\n");
    localStringBuilder.append("\t\t\tthis._map.setMapTypeId('").append(paramString2).append("');\n");
    localStringBuilder.append("\t\t};\n");
    localStringBuilder.append('\n');
    localStringBuilder.append("\t\t// ImgMapType class\n");
    localStringBuilder.append("\t\tDemo.ImgMapType = function (theme, backgroundColor) {\n");
    localStringBuilder.append("\t\t\tthis.name = this._theme = theme;\n");
    localStringBuilder.append("\t\t\tthis._backgroundColor = backgroundColor;\n");
    localStringBuilder.append("\t\t};\n");
    localStringBuilder.append('\n');
    localStringBuilder.append("\t\tDemo.ImgMapType.prototype.tileSize = new google.maps.Size(256, 256);\n");
    localStringBuilder.append("\t\tDemo.ImgMapType.prototype.minZoom = ").append(paramInt1).append(";\n");
    localStringBuilder.append("\t\tDemo.ImgMapType.prototype.maxZoom = ").append(paramInt2).append(";\n");
    localStringBuilder.append('\n');
    localStringBuilder.append("\t\tDemo.ImgMapType.prototype.getTile = function (coord, zoom, ownerDocument) {\n");
    localStringBuilder.append("\t\t\tvar tilesCount = Math.pow(2, zoom);\n");
    localStringBuilder.append('\n');
    localStringBuilder.append("\t\t\tif (coord.x >= tilesCount || coord.x < 0 || coord.y >= tilesCount || coord.y < 0) {\n");
    localStringBuilder.append("\t\t\t\tvar div = ownerDocument.createElement('div');\n");
    localStringBuilder.append("\t\t\t\tdiv.style.width = this.tileSize.width + 'px';\n");
    localStringBuilder.append("\t\t\t\tdiv.style.height = this.tileSize.height + 'px';\n");
    localStringBuilder.append("\t\t\t\tdiv.style.backgroundColor = this._backgroundColor;\n");
    localStringBuilder.append("\t\t\t\treturn div;\n");
    localStringBuilder.append("\t\t\t}\n");
    localStringBuilder.append('\n');
    localStringBuilder.append("\t\t\tvar img = ownerDocument.createElement('IMG');\n");
    localStringBuilder.append("\t\t\timg.width = this.tileSize.width;\n");
    localStringBuilder.append("\t\t\timg.height = this.tileSize.height;\n");
    localStringBuilder.append("\t\t\timg.src = Demo.Utils.GetImageUrl(this._theme + '/tile_' + zoom + '_' + coord.x + '-' + coord.y + '.png');\n");
    localStringBuilder.append('\n');
    localStringBuilder.append("\t\t\treturn img;\n");
    localStringBuilder.append("\t\t};\n");
    localStringBuilder.append('\n');
    localStringBuilder.append("\t\t// Other\n");
    localStringBuilder.append("\t\tDemo.Utils = Demo.Utils || {};\n");
    localStringBuilder.append('\n');
    localStringBuilder.append("\t\tDemo.Utils.GetImageUrl = function (image) {\n");
    localStringBuilder.append("\t\t\treturn Demo.ImagesBaseUrl + image;\n");
    localStringBuilder.append("\t\t};\n");
    localStringBuilder.append('\n');
    localStringBuilder.append("\t\t// Map creation\n");
    localStringBuilder.append("\t\tgoogle.maps.event.addDomListener(window, 'load', function () {\n");
    localStringBuilder.append("\t\t\tvar page").append(paramString2).append(" = new Demo.Page").append(paramString2).append("(document.getElementById('page").append(paramString2).append("'));\n");
    localStringBuilder.append("\t\t});\n");
    localStringBuilder.append("\t/* ]]> */\n");
    localStringBuilder.append("\t</script>\n");
    localStringBuilder.append("</body>\n");
    localStringBuilder.append("</html>\n");
    return localStringBuilder.toString();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.images.ConvertPagesToGoogleMaps
 * JD-Core Version:    0.6.2
 */