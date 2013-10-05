package org.jpedal.io;

import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PathSerializer
{
  public static void serializePath(ObjectOutput paramObjectOutput, PathIterator paramPathIterator)
    throws IOException
  {
    paramObjectOutput.writeObject(Integer.valueOf(paramPathIterator.getWindingRule()));
    ArrayList localArrayList = new ArrayList();
    while (!paramPathIterator.isDone())
    {
      float[] arrayOfFloat = new float[6];
      int i = paramPathIterator.currentSegment(arrayOfFloat);
      localArrayList.add(Integer.valueOf(i));
      localArrayList.add(arrayOfFloat);
      paramPathIterator.next();
    }
    paramObjectOutput.writeObject(localArrayList);
  }

  public static GeneralPath deserializePath(ObjectInput paramObjectInput)
    throws ClassNotFoundException, IOException
  {
    Integer localInteger = (Integer)paramObjectInput.readObject();
    if (localInteger == null)
      return null;
    List localList = (List)paramObjectInput.readObject();
    GeneralPath localGeneralPath = new GeneralPath();
    localGeneralPath.setWindingRule(localInteger.intValue());
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      int i = ((Integer)localIterator.next()).intValue();
      float[] arrayOfFloat = (float[])localIterator.next();
      switch (i)
      {
      case 1:
        localGeneralPath.lineTo(arrayOfFloat[0], arrayOfFloat[1]);
        break;
      case 0:
        localGeneralPath.moveTo(arrayOfFloat[0], arrayOfFloat[1]);
        break;
      case 2:
        localGeneralPath.quadTo(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3]);
        break;
      case 3:
        localGeneralPath.curveTo(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2], arrayOfFloat[3], arrayOfFloat[4], arrayOfFloat[5]);
        break;
      case 4:
        localGeneralPath.closePath();
        break;
      default:
        System.out.println("unrecognized general path type");
      }
    }
    return localGeneralPath;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.io.PathSerializer
 * JD-Core Version:    0.6.2
 */