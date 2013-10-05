package org.jpedal.objects.acroforms.utils;

import java.io.PrintStream;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConvertToString
{
  public static String convertArrayToString(float[] paramArrayOfFloat)
  {
    if (paramArrayOfFloat != null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      for (int i = 0; i < paramArrayOfFloat.length; i++)
      {
        if (i > 0)
          localStringBuilder.append(", ");
        localStringBuilder.append(paramArrayOfFloat[i]);
      }
      return localStringBuilder.toString();
    }
    return null;
  }

  public static String convertArrayToString(int[] paramArrayOfInt)
  {
    if (paramArrayOfInt != null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      for (int i = 0; i < paramArrayOfInt.length; i++)
      {
        if (i > 0)
          localStringBuilder.append(", ");
        localStringBuilder.append(paramArrayOfInt[i]);
      }
      return localStringBuilder.toString();
    }
    return null;
  }

  public static void printStackTrace(int paramInt)
  {
    printStackTrace(2, paramInt + 1, false);
  }

  public static void printStackTrace(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    Throwable localThrowable = new Throwable();
    StackTraceElement[] arrayOfStackTraceElement = localThrowable.getStackTrace();
    if ((paramInt2 == -1) || (paramInt2 > arrayOfStackTraceElement.length - 1))
      paramInt2 = arrayOfStackTraceElement.length - 1;
    for (int i = paramInt1; i <= paramInt2; i++)
      if (paramBoolean)
        System.err.println(arrayOfStackTraceElement[i]);
      else
        System.out.println(arrayOfStackTraceElement[i]);
  }

  public static String convertDocumentToString(Node paramNode)
  {
    return convertDocumentToString(paramNode, 0);
  }

  private static String convertDocumentToString(Node paramNode, int paramInt)
  {
    if (paramNode == null)
      return null;
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramNode.getNodeName());
    localStringBuilder.append(" = ");
    localStringBuilder.append(paramNode.getNodeValue());
    localStringBuilder.append(" type=");
    localStringBuilder.append(paramNode.getNodeType());
    localStringBuilder.append(" textContent=");
    localStringBuilder.append(paramNode.getTextContent());
    NamedNodeMap localNamedNodeMap = paramNode.getAttributes();
    if (localNamedNodeMap != null)
    {
      localStringBuilder.append(" attributes=[");
      for (int i = 0; i < localNamedNodeMap.getLength(); i++)
      {
        if (i > 0)
          localStringBuilder.append(',');
        localStringBuilder.append(localNamedNodeMap.item(i));
      }
      localStringBuilder.append(']');
    }
    NodeList localNodeList = paramNode.getChildNodes();
    for (int j = 0; j < localNodeList.getLength(); j++)
    {
      localStringBuilder.append('\n');
      for (int k = 0; k < paramInt; k++)
        localStringBuilder.append('|');
      localStringBuilder.append(convertDocumentToString(localNodeList.item(j), paramInt + 1));
    }
    return localStringBuilder.toString();
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.objects.acroforms.utils.ConvertToString
 * JD-Core Version:    0.6.2
 */