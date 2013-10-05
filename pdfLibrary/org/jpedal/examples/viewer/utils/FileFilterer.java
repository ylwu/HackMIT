package org.jpedal.examples.viewer.utils;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class FileFilterer extends FileFilter
{
  String[] extensions;
  String description;
  int items = 0;

  public FileFilterer(String[] paramArrayOfString, String paramString)
  {
    this.items = paramArrayOfString.length;
    this.extensions = new String[this.items];
    for (int i = 0; i < this.items; i++)
    {
      this.extensions[i] = paramArrayOfString[i].toLowerCase();
      this.description = paramString;
    }
  }

  public final String getDescription()
  {
    return this.description;
  }

  public final boolean accept(File paramFile)
  {
    boolean bool = false;
    if (paramFile.isDirectory())
    {
      bool = true;
    }
    else
    {
      String str = paramFile.getName().toLowerCase();
      for (int i = 0; i < this.items; i++)
        if (str.endsWith(this.extensions[i]))
          bool = true;
    }
    return bool;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.utils.FileFilterer
 * JD-Core Version:    0.6.2
 */