package org.jpedal.examples.viewer;

import java.util.Stack;
import java.util.StringTokenizer;
import org.jpedal.examples.viewer.utils.PropertiesFile;

public class RecentDocuments
{
  private Stack previousFiles = new Stack();
  private Stack nextFiles = new Stack();

  public RecentDocuments(int paramInt, PropertiesFile paramPropertiesFile)
  {
  }

  static String getShortenedFileName(String paramString)
  {
    if (paramString.length() <= 30)
      return paramString;
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, "\\/");
    int i = localStringTokenizer.countTokens();
    if (i == 1)
      return paramString.substring(0, 30);
    String[] arrayOfString = new String[i];
    for (int j = 0; j < i; j++)
      arrayOfString[j] = localStringTokenizer.nextToken();
    String str = paramString.substring(arrayOfString[0].length(), paramString.length() - arrayOfString[(i - 1)].length());
    StringBuilder localStringBuilder = new StringBuilder(str);
    for (int n = i - 2; n > 0; n--)
    {
      int k = localStringBuilder.lastIndexOf(arrayOfString[n]);
      int m = k + arrayOfString[n].length();
      localStringBuilder.replace(k, m, "...");
      if (localStringBuilder.toString().length() <= 30)
        break;
    }
    return new StringBuilder().append(arrayOfString[0]).append(localStringBuilder).append(arrayOfString[(i - 1)]).toString();
  }

  public String getPreviousDocument()
  {
    String str = null;
    if (this.previousFiles.size() > 1)
    {
      this.nextFiles.push(this.previousFiles.pop());
      str = (String)this.previousFiles.pop();
    }
    return str;
  }

  public String getNextDocument()
  {
    String str = null;
    if (!this.nextFiles.isEmpty())
      str = (String)this.nextFiles.pop();
    return str;
  }

  public void addToFileList(String paramString)
  {
    this.previousFiles.push(paramString);
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.RecentDocuments
 * JD-Core Version:    0.6.2
 */