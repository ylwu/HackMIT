package org.jpedal.examples.viewer.gui.popups;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import org.jpedal.io.ObjectStore;
import org.jpedal.utils.LogWriter;
import org.jpedal.utils.Messages;

public class FileDownload
{
  File tempURLFile;
  JFrame download = null;
  JPanel p;
  JProgressBar pb;
  JLabel downloadMessage;
  JLabel downloadFile;
  JLabel turnOff;
  int downloadCount = 0;
  boolean visible = true;
  String progress = "";
  Point coords = null;

  public FileDownload(boolean paramBoolean, Point paramPoint)
  {
    this.visible = paramBoolean;
    this.coords = paramPoint;
    if (this.visible)
    {
      this.download = new JFrame();
      this.download.setDefaultCloseOperation(2);
      this.p = new JPanel(new GridBagLayout());
      this.pb = new JProgressBar();
      this.downloadMessage = new JLabel();
      this.downloadFile = new JLabel();
      this.turnOff = new JLabel();
      this.download.setResizable(false);
      this.download.setTitle(Messages.getMessage("PageLayoutViewMenu.DownloadWindowTitle"));
      GridBagConstraints localGridBagConstraints = new GridBagConstraints();
      localGridBagConstraints.fill = 1;
      localGridBagConstraints.gridy = 0;
      localGridBagConstraints.gridx = 0;
      localGridBagConstraints.gridwidth = 2;
      this.downloadFile.setSize(250, this.downloadFile.getHeight());
      this.downloadFile.setMinimumSize(new Dimension(250, 15));
      this.downloadFile.setMaximumSize(new Dimension(250, 15));
      this.downloadFile.setPreferredSize(new Dimension(250, 15));
      this.p.add(this.downloadFile, localGridBagConstraints);
      localGridBagConstraints.gridy = 1;
      this.downloadMessage.setSize(250, this.downloadFile.getHeight());
      this.downloadMessage.setMinimumSize(new Dimension(250, 15));
      this.downloadMessage.setMaximumSize(new Dimension(250, 15));
      this.downloadMessage.setPreferredSize(new Dimension(250, 15));
      this.p.add(this.downloadMessage, localGridBagConstraints);
      localGridBagConstraints.gridy = 2;
      this.pb.setSize(260, this.downloadFile.getHeight());
      this.pb.setMinimumSize(new Dimension(260, 20));
      this.pb.setMaximumSize(new Dimension(260, 20));
      this.pb.setPreferredSize(new Dimension(260, 20));
      this.p.add(this.pb, localGridBagConstraints);
      localGridBagConstraints.gridy = 3;
      this.p.add(this.turnOff, localGridBagConstraints);
      this.download.getContentPane().add(this.p);
      this.download.setSize(320, 100);
    }
  }

  public File createWindow(String paramString)
  {
    try
    {
      URL localURL = new URL(paramString);
      InputStream localInputStream = localURL.openStream();
      String str1 = localURL.getPath().substring(localURL.getPath().lastIndexOf('/') + 1);
      int i = localURL.openConnection().getContentLength();
      String str2 = str1;
      this.tempURLFile = File.createTempFile(str2.substring(0, str2.lastIndexOf('.')), str2.substring(str2.lastIndexOf('.')), new File(ObjectStore.temp_dir));
      FileOutputStream localFileOutputStream = new FileOutputStream(this.tempURLFile);
      if ((this.visible) && (this.coords != null))
      {
        this.download.setLocation(this.coords.x - this.download.getWidth() / 2, this.coords.y - this.download.getHeight() / 2);
        this.download.setVisible(true);
      }
      if (this.visible)
      {
        this.pb.setMinimum(0);
        this.pb.setMaximum(i);
        localObject = Messages.getMessage("PageLayoutViewMenu.DownloadWindowMessage");
        localObject = ((String)localObject).replaceAll("FILENAME", str2);
        this.downloadFile.setText((String)localObject);
        Font localFont = this.turnOff.getFont();
        this.turnOff.setFont(new Font(localFont.getName(), localFont.getStyle(), 8));
        this.turnOff.setAlignmentY(1.0F);
        this.turnOff.setText(Messages.getMessage("PageLayoutViewMenu.DownloadWindowTurnOff"));
      }
      Object localObject = new byte[4096];
      int k = 0;
      String str3 = "kb";
      int m = 1000;
      if (i > 1000000)
      {
        str3 = "mb";
        m = 1000000;
      }
      String str4;
      if (this.visible)
      {
        this.progress = Messages.getMessage("PageLayoutViewMenu.DownloadWindowProgress");
        if (i < 1000000)
        {
          this.progress = this.progress.replaceAll("DVALUE", i / m + " " + str3);
        }
        else
        {
          str4 = String.valueOf(i % m / 10000);
          if (i % m / 10000 < 10)
            str4 = '0' + str4;
          this.progress = this.progress.replaceAll("DVALUE", i / m + "." + str4 + ' ' + str3);
        }
      }
      int j;
      while ((j = localInputStream.read((byte[])localObject)) != -1)
      {
        k += j;
        this.downloadCount += j;
        if (this.visible)
        {
          if (i < 1000000)
          {
            this.downloadMessage.setText(this.progress.replaceAll("DSOME", k / m + " " + str3));
          }
          else
          {
            str4 = String.valueOf(k % m / 10000);
            if (k % m / 10000 < 10)
              str4 = '0' + str4;
            this.downloadMessage.setText(this.progress.replaceAll("DSOME", k / m + "." + str4 + ' ' + str3));
          }
          this.pb.setValue(k);
          this.download.repaint();
        }
        localFileOutputStream.write((byte[])localObject, 0, j);
      }
      localFileOutputStream.flush();
      localInputStream.close();
      localFileOutputStream.close();
      if (this.visible)
        this.downloadMessage.setText("Download of " + str2 + " is complete.");
    }
    catch (Exception localException)
    {
      LogWriter.writeLog("[PDF] Exception " + localException + " opening URL " + paramString);
      localException.printStackTrace();
    }
    if (this.visible)
      this.download.setVisible(false);
    return this.tempURLFile;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.gui.popups.FileDownload
 * JD-Core Version:    0.6.2
 */