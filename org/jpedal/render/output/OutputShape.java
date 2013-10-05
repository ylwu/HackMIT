package org.jpedal.render.output;

import java.awt.BasicStroke;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.jpedal.objects.GraphicsState;
import org.jpedal.objects.PdfPageData;

public class OutputShape
{
  protected boolean shapeIsOpen = false;
  boolean hasMoveTo = false;
  protected int shapeCount = 0;
  double minXcoord = 999999.0D;
  double minYcoord = 999999.0D;
  protected List pathCommands;
  protected int currentColor;
  protected Rectangle cropBox;
  private Point2D midPoint;
  private double[] lastVisiblePoint;
  private double[] lastInvisiblePoint;
  private double[] previousPoint;
  private boolean isPathSegmentVisible;
  protected double[] entryPoint;
  boolean includeClip = true;
  private double[] exitPoint;
  private List largeBox;
  private boolean isLargeBox;
  private boolean largeBoxSideAlternation;
  Rectangle clipBox = null;
  private int pageRotation = 0;
  protected float scaling;
  private boolean debugPath = false;
  int pathCommand;
  private float[][] ctm;
  private Shape currentShape;
  private int minX;
  private int minY;
  protected int cmd = -1;
  boolean isFilled = false;
  boolean evenStroke = true;

  public OutputShape(int paramInt1, float paramFloat, Shape paramShape, GraphicsState paramGraphicsState, Point2D paramPoint2D, Rectangle paramRectangle, int paramInt2, int paramInt3, PdfPageData paramPdfPageData, int paramInt4, boolean paramBoolean)
  {
    this.includeClip = paramBoolean;
    this.cmd = paramInt1;
    if (paramInt1 == 21610)
    {
      if (paramRectangle != null)
      {
        this.minX = ((int)paramRectangle.getMinX());
        this.minY = ((int)paramRectangle.getMinY());
      }
    }
    else
    {
      this.minX = paramPdfPageData.getCropBoxX(paramInt4);
      this.minY = paramPdfPageData.getCropBoxY(paramInt4);
    }
    if (this.debugPath)
      System.out.println(new StringBuilder().append("raw shape=").append(paramShape.getBounds()).append(" minx=").append(this.minX).append(' ').append(this.minY).toString());
    Area localArea = paramGraphicsState.getClippingShape();
    if (localArea != null)
      this.clipBox = localArea.getBounds();
    else
      this.clipBox = null;
    if (((this.minX != 0) || (this.minY != 0)) && (this.clipBox != null) && (paramRectangle != null))
      this.clipBox.translate(-this.minX, -this.minY);
    this.currentShape = paramShape;
    this.ctm = paramGraphicsState.CTM;
    this.scaling = paramFloat;
    this.currentColor = paramInt2;
    this.cropBox = paramRectangle;
    this.midPoint = paramPoint2D;
    this.pageRotation = paramInt3;
    this.isPathSegmentVisible = true;
    this.lastVisiblePoint = new double[2];
    this.lastInvisiblePoint = new double[2];
    this.previousPoint = new double[2];
    this.exitPoint = new double[2];
    this.entryPoint = new double[2];
    this.pathCommands = new ArrayList();
    this.largeBox = new ArrayList();
    this.isLargeBox = true;
  }

  protected void generateShapeFromG2Data(GraphicsState paramGraphicsState, AffineTransform paramAffineTransform, Rectangle paramRectangle)
  {
    this.isFilled = (paramGraphicsState.getFillType() == 2);
    int i = (int)(paramGraphicsState.getCTMAdjustedLineWidth() * this.scaling + 0.99D);
    this.evenStroke = ((i != 0) && (i % 2 == 0));
    paramGraphicsState.setOutputLineWidth(i);
    PathIterator localPathIterator = this.currentShape.getPathIterator(paramAffineTransform);
    this.shapeIsOpen = true;
    beginShape();
    int j = 1;
    if (this.debugPath)
    {
      System.out.println(new StringBuilder().append("About to generate commands for shape with bounds").append(this.currentShape.getBounds()).toString());
      System.out.println("------------------------------------------------");
      System.out.println(new StringBuilder().append("crop bounds=").append(paramRectangle.getBounds()).toString());
      System.out.println(new StringBuilder().append("shape bounds=").append(this.currentShape.getBounds()).toString());
      System.out.println(new StringBuilder().append("minX=").append(this.minX).append(" minY=").append(this.minY).toString());
      if (this.clipBox != null)
        System.out.println(new StringBuilder().append("clip bounds=").append(this.clipBox.getBounds()).toString());
    }
    int k = 0;
    while (!localPathIterator.isDone())
    {
      double[] arrayOfDouble2 = { 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D };
      this.pathCommand = localPathIterator.currentSegment(arrayOfDouble2);
      k++;
      if (paramRectangle != null)
        for (bool1 = false; bool1 < true; bool1++)
        {
          arrayOfDouble2[(bool1 * true)] -= this.minX;
          arrayOfDouble2[(bool1 * true + 1)] -= this.minY;
        }
      if (this.debugPath)
        System.out.println(new StringBuilder().append("\n=======Get pathCommand segment ").append((int)arrayOfDouble2[0]).append(' ').append((int)arrayOfDouble2[1]).append(' ').append((int)arrayOfDouble2[2]).append(' ').append((int)arrayOfDouble2[3]).append(' ').append((int)arrayOfDouble2[4]).append(' ').append((int)arrayOfDouble2[5]).toString());
      boolean bool1 = false;
      if (this.cmd != 110)
        bool1 = checkLargeBox(arrayOfDouble2, this.pathCommand);
      if (bool1)
      {
        localPathIterator.next();
      }
      else
      {
        if (j != 0)
        {
          int m = (int)arrayOfDouble2[getCoordOffset(this.pathCommand)];
          int n = (int)arrayOfDouble2[(getCoordOffset(this.pathCommand) + 1)];
          Rectangle localRectangle = paramRectangle;
          if (this.clipBox != null)
            localRectangle = this.clipBox;
          if ((localRectangle != null) && (localRectangle.width > 1))
            if (m == localRectangle.x)
              m += 1;
            else if (m == localRectangle.x + localRectangle.width)
              m -= 1;
          this.isPathSegmentVisible = ((localRectangle == null) || (localRectangle.contains(m, n)));
          j = 0;
          if (this.debugPath)
            System.out.println(new StringBuilder().append("isPathSegmentVisible=").append(this.isPathSegmentVisible).toString());
        }
        boolean bool2;
        if (this.pathCommand == 3)
        {
          bool2 = testDrawLimits(arrayOfDouble2, 3);
          if (this.debugPath)
            System.out.println(new StringBuilder().append("PathIterator.SEG_CUBICTO isPointVisible=").append(bool2).toString());
          if ((bool2) && (this.isPathSegmentVisible))
            bezierCurveTo(arrayOfDouble2);
          this.isPathSegmentVisible = bool2;
        }
        else if (this.pathCommand == 1)
        {
          double[] arrayOfDouble1 = { this.previousPoint[0], this.previousPoint[1] };
          bool2 = testDrawLimits(arrayOfDouble2, 1);
          if (this.debugPath)
            System.out.println(new StringBuilder().append("PathIterator.SEG_LINETO isPointVisible=").append(bool2).append(" isPathSegmentVisible=").append(this.isPathSegmentVisible).append(' ').append((int)arrayOfDouble2[0]).append(' ').append((int)arrayOfDouble2[1]).toString());
          if ((bool2) && (this.isPathSegmentVisible))
          {
            if (this.debugPath)
              System.out.println(new StringBuilder().append("pdf.lineTo(").append(coordsToStringParam(arrayOfDouble2, 2)).append(')').toString());
            lineTo(arrayOfDouble2);
          }
          else if (bool2 != this.isPathSegmentVisible)
          {
            if (!this.isPathSegmentVisible)
            {
              if (paramGraphicsState.getFillType() != 2)
              {
                moveTo(this.entryPoint);
                this.hasMoveTo = true;
                if (this.debugPath)
                  System.out.println(new StringBuilder().append("pdf.moveTo(").append(coordsToStringParam(arrayOfDouble2, 2)).toString());
              }
              else
              {
                if ((!this.hasMoveTo) && (!paramRectangle.contains(arrayOfDouble1[0], arrayOfDouble1[1])))
                {
                  double d1 = paramRectangle.x + paramRectangle.width;
                  double d2 = paramRectangle.y + paramRectangle.height;
                  if (d1 > this.lastInvisiblePoint[0])
                    d1 = paramRectangle.x;
                  if (this.lastInvisiblePoint[1] < paramRectangle.y)
                    d2 = paramRectangle.y;
                  else if (d2 < this.lastInvisiblePoint[1])
                    d2 = paramRectangle.y;
                  double[] arrayOfDouble3 = { d1, d2 };
                  this.hasMoveTo = true;
                  moveTo(arrayOfDouble3);
                  if (this.debugPath)
                    System.out.println(new StringBuilder().append("pdf.moveTo(").append(coordsToStringParam(arrayOfDouble3, 2)).toString());
                }
                lineTo(this.entryPoint);
              }
              lineTo(arrayOfDouble2);
              if (this.debugPath)
                System.out.println(new StringBuilder().append("pdf.lineTo(").append(coordsToStringParam(arrayOfDouble2, 2)).append(");").toString());
              this.isPathSegmentVisible = true;
            }
            else
            {
              lineTo(this.exitPoint);
              this.isPathSegmentVisible = false;
              if (this.debugPath)
                System.out.println(new StringBuilder().append("pdf.lineTo(").append(coordsToStringParam(this.exitPoint, 2)).append(");").toString());
            }
          }
        }
        else if (this.pathCommand == 2)
        {
          if (this.debugPath)
            System.out.println("PathIterator.SEG_QUADTO");
          if (testDrawLimits(arrayOfDouble2, 2))
          {
            if (this.debugPath)
              System.out.println(new StringBuilder().append("pdf.quadraticCurveTo(").append(coordsToStringParam(arrayOfDouble2, 4)).toString());
            quadraticCurveTo(arrayOfDouble2);
            this.isPathSegmentVisible = true;
          }
          else
          {
            this.isPathSegmentVisible = false;
          }
        }
        else if (this.pathCommand == 0)
        {
          if (this.debugPath)
            System.out.println("PathIterator.SEG_MOVETO");
          if (testDrawLimits(arrayOfDouble2, 0))
          {
            this.isPathSegmentVisible = true;
            if (this.debugPath)
              System.out.println(new StringBuilder().append("pdf.moveTo(").append(coordsToStringParam(arrayOfDouble2, 2)).append(");").toString());
            this.hasMoveTo = true;
            moveTo(arrayOfDouble2);
          }
          else
          {
            this.isPathSegmentVisible = false;
          }
        }
        else if (this.pathCommand == 4)
        {
          if (this.debugPath)
            System.out.println("pdf.closePath();");
          closePath();
        }
        localPathIterator.next();
      }
    }
    if (this.pathCommands.size() == 1)
    {
      this.pathCommands.clear();
    }
    else
    {
      if ((this.cmd == 21610) || ((!this.isPathSegmentVisible) && (paramGraphicsState.getFillType() != 2)))
      {
        this.shapeIsOpen = false;
        finishShape();
        if (this.debugPath)
          System.out.println("pdf.closePath();");
      }
      applyGraphicsStateToPath(paramGraphicsState);
    }
    if ((this.cmd == 83) && (k == 6) && (this.clipBox != null) && (this.currentShape.getBounds().width > 10) && (this.currentShape.getBounds().height > 10) && (this.clipBox.x >= this.currentShape.getBounds().x) && (this.clipBox.x + this.clipBox.width <= this.currentShape.getBounds().x + this.currentShape.getBounds().width) && (this.clipBox.y >= this.currentShape.getBounds().y) && (this.clipBox.y + this.clipBox.height <= this.currentShape.getBounds().y + this.currentShape.getBounds().height))
    {
      this.pathCommands.clear();
      if (this.debugPath)
        System.out.println("shape removed");
    }
    checkShapeClosed();
    this.isFilled = false;
  }

  public void checkShapeClosed()
  {
  }

  protected void moveTo(double[] paramArrayOfDouble)
  {
  }

  protected void quadraticCurveTo(double[] paramArrayOfDouble)
  {
  }

  protected void lineTo(double[] paramArrayOfDouble)
  {
  }

  protected void closePath()
  {
  }

  protected void finishShape()
  {
  }

  protected void beginShape()
  {
  }

  protected void bezierCurveTo(double[] paramArrayOfDouble)
  {
  }

  private String setPrecision(double paramDouble, boolean paramBoolean)
  {
    String str = "";
    if ((this.cmd != 110) && ((this.isFilled) || (this.evenStroke)))
    {
      str = new StringBuilder().append("").append((int)paramDouble).toString();
    }
    else
    {
      int i = (int)paramDouble;
      str = new StringBuilder().append(str).append(i).append(".5").toString();
    }
    if ((paramBoolean) && (paramDouble < this.minXcoord))
      this.minXcoord = paramDouble;
    else if ((!paramBoolean) && (paramDouble < this.minYcoord))
      this.minYcoord = paramDouble;
    return str;
  }

  protected void applyGraphicsStateToPath(GraphicsState paramGraphicsState)
  {
  }

  protected static String determineLineCap(BasicStroke paramBasicStroke)
  {
    String str;
    switch (paramBasicStroke.getEndCap())
    {
    case 1:
      str = "round";
      break;
    case 2:
      str = "square";
      break;
    default:
      str = "butt";
    }
    return str;
  }

  protected static String determineLineJoin(BasicStroke paramBasicStroke)
  {
    String str;
    switch (paramBasicStroke.getLineJoin())
    {
    case 1:
      str = "round";
      break;
    case 2:
      str = "bevel";
      break;
    default:
      str = "miter";
    }
    return str;
  }

  private boolean testDrawLimits(double[] paramArrayOfDouble, int paramInt)
  {
    if (this.includeClip)
      return true;
    if (this.debugPath)
      System.out.println(new StringBuilder().append("testDrawLimits coords[0] + coords[1]=").append((int)paramArrayOfDouble[0]).append(' ').append((int)paramArrayOfDouble[1]).toString());
    int i = getCoordOffset(paramInt);
    if (this.debugPath)
      System.out.println(new StringBuilder().append("crop=").append(this.cropBox).append(" clip=").append(this.clipBox).toString());
    Rectangle localRectangle = this.cropBox;
    if (this.clipBox != null)
    {
      localRectangle = localRectangle.intersection(this.clipBox);
      if (this.debugPath)
        System.out.println(new StringBuilder().append("merged crop=").append(localRectangle).toString());
    }
    double d1 = paramArrayOfDouble[i];
    if (d1 > localRectangle.x + 1)
      d1 -= 1.0D;
    double d2 = paramArrayOfDouble[(i + 1)];
    if (d2 > localRectangle.y + 1)
      d2 -= 1.0D;
    double[] arrayOfDouble = { d1, d2 };
    boolean bool;
    if (localRectangle.contains(arrayOfDouble[0], arrayOfDouble[1]))
    {
      this.lastVisiblePoint = arrayOfDouble;
      bool = true;
      if (this.debugPath)
        System.out.println("Point visible in cropBox or clip");
    }
    else
    {
      this.lastInvisiblePoint = arrayOfDouble;
      bool = false;
      if (this.debugPath)
        System.out.println(new StringBuilder().append("Point invisible ").append((int)arrayOfDouble[0]).append(' ').append((int)arrayOfDouble[1]).append(" crop=").append(localRectangle.getBounds()).toString());
    }
    if ((!bool) && (this.isPathSegmentVisible))
    {
      if (this.debugPath)
        System.out.println(new StringBuilder().append("Case1 this point ").append((int)d1).append(',').append((int)d2).append(" invisible and isPathSegmentVisible").toString());
      findSwitchPoint(arrayOfDouble, true);
    }
    else if ((bool) && (!this.isPathSegmentVisible))
    {
      if (this.debugPath)
        System.out.println(new StringBuilder().append("Case2 this point ").append((int)d1).append(',').append((int)d2).append(" visible and isPathSegment invisible").toString());
      findSwitchPoint(arrayOfDouble, false);
    }
    else if (this.debugPath)
    {
      System.out.println(new StringBuilder().append("Case3 NOT COVERED isCurrentPointVisible=").append(bool).append(" isPathSegmentVisible").append(this.isPathSegmentVisible).toString());
    }
    if ((!bool) && (!localRectangle.contains(this.previousPoint[0], this.previousPoint[1])) && (paramInt == 1))
    {
      if (this.debugPath)
        System.out.println("checkTraversalPoints");
      checkTraversalPoints(arrayOfDouble, this.previousPoint, localRectangle);
    }
    this.previousPoint = arrayOfDouble;
    return bool;
  }

  private void checkTraversalPoints(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, Rectangle paramRectangle)
  {
    boolean bool1 = ((paramArrayOfDouble2[0] < paramRectangle.x) && (paramArrayOfDouble1[0] > paramRectangle.x + paramRectangle.width)) || ((paramArrayOfDouble1[0] < paramRectangle.x) && (paramArrayOfDouble2[0] > paramRectangle.x + paramRectangle.width));
    boolean bool2 = ((paramArrayOfDouble2[1] < paramRectangle.y) && (paramArrayOfDouble1[1] > paramRectangle.y + paramRectangle.height)) || ((paramArrayOfDouble1[1] < paramRectangle.y) && (paramArrayOfDouble2[1] > paramRectangle.y + paramRectangle.height));
    boolean bool3 = isCompleteCropBoxMiss(paramArrayOfDouble1, paramArrayOfDouble2, paramRectangle);
    if (this.debugPath)
    {
      System.out.println(new StringBuilder().append("checkTraversalPoints xtrav=").append(bool1).append(" yTrav=").append(bool2).append(" completeCropBoxMiss=").append(bool3).append(' ').append((paramArrayOfDouble2[0] < paramRectangle.x) && (paramArrayOfDouble1[0] > paramRectangle.x + paramRectangle.width)).toString());
      System.out.println(new StringBuilder().append("start=").append((int)paramArrayOfDouble1[0]).append(' ').append((int)paramArrayOfDouble1[1]).append("  end=").append((int)paramArrayOfDouble2[0]).append(' ').append((int)paramArrayOfDouble2[1]).toString());
    }
    if ((!bool1) && (!bool2))
      return;
    double d1 = (paramArrayOfDouble2[1] - paramArrayOfDouble1[1]) / (paramArrayOfDouble2[0] - paramArrayOfDouble1[0]);
    double d2 = d1;
    if (Math.abs(paramArrayOfDouble2[0] - paramArrayOfDouble1[0]) < 1.0D)
      d1 = 0.0D;
    if (d1 < 0.001D)
      d1 = 0.0D;
    if (d1 == 0.0D)
    {
      if ((d2 < 0.0D) && (paramArrayOfDouble2[0] < 0.0D) && (paramArrayOfDouble2[0] < paramArrayOfDouble1[0]))
      {
        this.exitPoint = calcCrossoverPoint(paramArrayOfDouble2, paramRectangle, d1);
        this.entryPoint = calcCrossoverPoint(paramArrayOfDouble1, paramRectangle, d1);
      }
      else
      {
        this.entryPoint = calcCrossoverPoint(paramArrayOfDouble2, paramRectangle, d1);
        this.exitPoint = calcCrossoverPoint(paramArrayOfDouble1, paramRectangle, d1);
      }
    }
    else
    {
      this.exitPoint = calcCrossoverPoint(paramArrayOfDouble2, paramRectangle, d1);
      this.entryPoint = calcCrossoverPoint(paramArrayOfDouble1, paramRectangle, d1);
    }
    if (this.debugPath)
    {
      System.out.println(new StringBuilder().append("entry=").append(this.entryPoint[0]).append(' ').append(this.entryPoint[1]).append(" exit=").append(this.exitPoint[0]).append(' ').append(this.exitPoint[1]).toString());
      System.out.println(new StringBuilder().append("XXpdf.lineTo(").append(coordsToStringParam(this.entryPoint, 2)).append(");").toString());
      System.out.println(new StringBuilder().append("XXpdf.lineTo(").append(coordsToStringParam(this.exitPoint, 2)).append("); hasMoveTo=").append(this.hasMoveTo).toString());
    }
    if ((!this.hasMoveTo) && (paramArrayOfDouble2[0] < 0.0D) && (paramArrayOfDouble1[0] > this.entryPoint[0]) && (this.entryPoint[0] > this.exitPoint[0]))
    {
      double d3 = paramRectangle.x + paramRectangle.width;
      double d4 = paramRectangle.y + paramRectangle.height;
      if (d3 > this.lastVisiblePoint[0])
        d3 = paramRectangle.x;
      if (d4 < this.lastVisiblePoint[1])
        d4 = paramRectangle.y;
      double[] arrayOfDouble = { d3, d4 };
      this.hasMoveTo = true;
      moveTo(arrayOfDouble);
      if (this.debugPath)
        System.out.println(new StringBuilder().append("pdf.moveTo(").append(coordsToStringParam(arrayOfDouble, 2)).toString());
      lineTo(this.exitPoint);
      lineTo(this.entryPoint);
    }
    else
    {
      lineTo(this.entryPoint);
      lineTo(this.exitPoint);
    }
  }

  private static double[] calcCrossoverPoint(double[] paramArrayOfDouble, Rectangle paramRectangle, double paramDouble)
  {
    double d1 = 0.0D;
    double d2 = 0.0D;
    if ((paramArrayOfDouble[1] < paramRectangle.y) || (paramArrayOfDouble[1] > paramRectangle.y + paramRectangle.height))
    {
      if (paramArrayOfDouble[1] < paramRectangle.y)
        d2 = paramRectangle.y;
      else
        d2 = paramRectangle.y + paramRectangle.height;
      if (paramDouble == 0.0D)
        d1 = paramArrayOfDouble[0];
      else
        d1 = (d2 - paramArrayOfDouble[1]) / paramDouble + paramArrayOfDouble[0];
      if ((d1 < paramRectangle.x) || (d1 > paramRectangle.x + paramRectangle.width))
      {
        if (d1 < paramRectangle.x)
          d1 = paramRectangle.x;
        else
          d1 = paramRectangle.x + paramRectangle.width;
        d2 = paramDouble * (d1 - paramArrayOfDouble[0]) + paramArrayOfDouble[1];
      }
    }
    else if ((paramArrayOfDouble[0] < paramRectangle.x) || (paramArrayOfDouble[0] > paramRectangle.x + paramRectangle.width))
    {
      if (paramArrayOfDouble[0] < paramRectangle.x)
        d1 = paramRectangle.x;
      else
        d1 = paramRectangle.x + paramRectangle.width;
      d2 = paramDouble * (d1 - paramArrayOfDouble[0]) + paramArrayOfDouble[1];
      if ((d2 < paramRectangle.y) || (d2 > paramRectangle.y + paramRectangle.height))
      {
        if (paramArrayOfDouble[1] < paramRectangle.y)
          d2 = paramRectangle.y;
        else
          d2 = paramRectangle.y + paramRectangle.height;
        d1 = (d2 - paramArrayOfDouble[1]) / paramDouble + paramArrayOfDouble[0];
      }
    }
    return new double[] { d1, d2 };
  }

  private void findSwitchPoint(double[] paramArrayOfDouble, boolean paramBoolean)
  {
    double[] arrayOfDouble1 = new double[2];
    Object localObject = paramBoolean ? this.lastVisiblePoint : this.lastInvisiblePoint;
    if (this.debugPath)
      if (paramBoolean)
        System.out.println(new StringBuilder().append("Find point of exit lastPoint=").append((int)localObject[0]).append(' ').append((int)localObject[1]).append(" current=").append((int)paramArrayOfDouble[0]).append(' ').append((int)paramArrayOfDouble[1]).toString());
      else
        System.out.println(new StringBuilder().append("Find point of entry lastPoint=").append((int)localObject[0]).append(' ').append((int)localObject[1]).append(" current=").append((int)paramArrayOfDouble[0]).append(' ').append((int)paramArrayOfDouble[1]).toString());
    if (!paramBoolean)
    {
      double[] arrayOfDouble2 = paramArrayOfDouble;
      paramArrayOfDouble = (double[])localObject;
      localObject = arrayOfDouble2;
    }
    double d1 = paramArrayOfDouble[0] - localObject[0];
    double d2 = paramArrayOfDouble[1] - localObject[1];
    int i = 0;
    int j = 0;
    if ((this.clipBox != null) && (paramArrayOfDouble[0] >= this.clipBox.width + this.clipBox.x))
    {
      arrayOfDouble1[0] = (this.clipBox.width + this.clipBox.x);
      i = 1;
    }
    else if (paramArrayOfDouble[0] >= this.cropBox.width + this.cropBox.x)
    {
      arrayOfDouble1[0] = (this.cropBox.width + this.cropBox.x);
      i = 1;
    }
    else if ((this.clipBox != null) && (paramArrayOfDouble[0] < this.clipBox.x))
    {
      arrayOfDouble1[0] = this.clipBox.x;
      i = 1;
    }
    else if (paramArrayOfDouble[0] < this.cropBox.x)
    {
      arrayOfDouble1[0] = this.cropBox.x;
      i = 1;
    }
    if ((this.clipBox != null) && (paramArrayOfDouble[1] > this.clipBox.height + this.clipBox.y))
    {
      arrayOfDouble1[1] = (this.clipBox.height + this.clipBox.y);
      j = 1;
    }
    else if (paramArrayOfDouble[1] > this.cropBox.height + this.cropBox.y)
    {
      arrayOfDouble1[1] = (this.cropBox.height + this.cropBox.y);
      j = 1;
    }
    else if ((this.clipBox != null) && (paramArrayOfDouble[1] < this.clipBox.y))
    {
      arrayOfDouble1[1] = this.clipBox.y;
      j = 1;
    }
    else if (paramArrayOfDouble[1] < this.cropBox.y)
    {
      arrayOfDouble1[1] = this.cropBox.y;
      j = 1;
    }
    double d3;
    double d4;
    if (j != 0)
      if (d1 == 0.0D)
      {
        arrayOfDouble1[0] = paramArrayOfDouble[0];
      }
      else
      {
        d3 = d1 / d2;
        if (d2 == 0.0D)
          d3 = 1.0D;
        d4 = arrayOfDouble1[1] - paramArrayOfDouble[1];
        paramArrayOfDouble[0] += d3 * d4;
      }
    if (i != 0)
      if (d2 == 0.0D)
      {
        arrayOfDouble1[1] = paramArrayOfDouble[1];
      }
      else
      {
        d3 = d2 / d1;
        d4 = arrayOfDouble1[0] - paramArrayOfDouble[0];
        paramArrayOfDouble[1] += d3 * d4;
      }
    if (paramBoolean)
    {
      this.exitPoint = arrayOfDouble1;
      if (this.debugPath)
        System.out.println(new StringBuilder().append("returns exit=").append((int)arrayOfDouble1[0]).append(' ').append((int)arrayOfDouble1[1]).toString());
    }
    else
    {
      this.entryPoint = arrayOfDouble1;
      if (this.debugPath)
        System.out.println(new StringBuilder().append("returns entry=").append((int)arrayOfDouble1[0]).append(' ').append((int)arrayOfDouble1[1]).toString());
    }
  }

  private boolean checkLargeBox(double[] paramArrayOfDouble, int paramInt)
  {
    boolean bool = false;
    if (this.debugPath)
      System.out.println(new StringBuilder().append("check large ").append((int)paramArrayOfDouble[0]).append(' ').append((int)paramArrayOfDouble[1]).toString());
    if ((!this.isLargeBox) && ((paramInt != 1) || (paramInt != 0)))
      return false;
    double d1 = paramArrayOfDouble[getCoordOffset(paramInt)];
    double d2 = paramArrayOfDouble[(getCoordOffset(paramInt) + 1)];
    double[] arrayOfDouble = correctCoords(new double[] { d1, d2 });
    d1 = arrayOfDouble[0];
    d2 = arrayOfDouble[1];
    Point localPoint1 = new Point((int)d1, (int)d2);
    if (this.largeBox.isEmpty())
    {
      this.largeBox.add(localPoint1);
    }
    else
    {
      Point localPoint2 = (Point)this.largeBox.get(this.largeBox.size() - 1);
      double d3 = localPoint2.getX() - localPoint1.getX();
      double d4 = localPoint2.getY() - localPoint1.getY();
      if (this.largeBox.size() == 1)
        if (d4 != 0.0D)
          this.largeBoxSideAlternation = (d3 / d4 != 0.0D);
        else
          this.largeBoxSideAlternation = true;
      if ((d3 / d4 == 0.0D) || (d4 / d3 == 0.0D))
      {
        int i = d3 / d4 == 0.0D ? 1 : 0;
        if ((this.largeBox.size() > 1) || (i != this.largeBoxSideAlternation))
        {
          this.largeBox.add(localPoint1);
          this.largeBoxSideAlternation = (d3 / d4 == 0.0D);
        }
      }
      else if (!localPoint1.equals(this.largeBox.get(this.largeBox.size() - 1)))
      {
        this.isLargeBox = false;
        return false;
      }
      if ((this.largeBox.size() >= 4) && (isLargerThanCropBox()))
      {
        drawCropBox();
        bool = true;
      }
    }
    return bool;
  }

  private boolean isLargerThanCropBox()
  {
    if ((!this.isLargeBox) || (this.cropBox == null))
      return false;
    Point localPoint1 = (Point)this.largeBox.get(this.largeBox.size() - 4);
    Point localPoint2 = (Point)this.largeBox.get(this.largeBox.size() - 3);
    Point localPoint3 = (Point)this.largeBox.get(this.largeBox.size() - 2);
    Point localPoint4 = (Point)this.largeBox.get(this.largeBox.size() - 1);
    int i = this.cropBox.width < this.cropBox.height ? this.cropBox.width : this.cropBox.height;
    if ((localPoint1.distance(localPoint2) < i) || (localPoint2.distance(localPoint3) < i) || (localPoint3.distance(localPoint4) < i))
      return false;
    int j = 0;
    if (!this.cropBox.contains(localPoint1.getX(), localPoint1.getY()))
      j++;
    if (!this.cropBox.contains(localPoint2.getX(), localPoint2.getY()))
      j++;
    if (!this.cropBox.contains(localPoint3.getX(), localPoint3.getY()))
      j++;
    if (!this.cropBox.contains(localPoint4.getX(), localPoint4.getY()))
      j++;
    if (j <= 2)
      return false;
    HashSet localHashSet = new HashSet();
    localHashSet.add(localPoint1);
    localHashSet.add(localPoint2);
    localHashSet.add(localPoint3);
    localHashSet.add(localPoint4);
    j = 0;
    for (int k = -1; k <= 1; k++)
      for (int m = -1; m <= 1; m++)
        if ((k != 0) || (m != 0))
        {
          Rectangle localRectangle = new Rectangle(this.cropBox);
          localRectangle.translate(m * this.cropBox.width, k * this.cropBox.height);
          if (doesPointSetCollide(localHashSet, localRectangle))
            j++;
        }
    return j >= 3;
  }

  private static boolean doesPointSetCollide(Set paramSet, Rectangle paramRectangle)
  {
    Iterator localIterator = paramSet.iterator();
    while (localIterator.hasNext())
    {
      Object localObject = localIterator.next();
      Point2D localPoint2D = (Point2D)localObject;
      if (paramRectangle.contains(localPoint2D))
        return true;
    }
    return false;
  }

  protected void drawCropBox()
  {
  }

  private double[] correctCoords(double[] paramArrayOfDouble)
  {
    if (this.cropBox != null)
    {
      int i;
      switch (this.pathCommand)
      {
      case 3:
        i = 4;
        break;
      case 2:
        i = 2;
        break;
      default:
        i = 0;
      }
      if (i > paramArrayOfDouble.length)
        i = paramArrayOfDouble.length - 2;
      for (int j = 0; j < i + 2; j += 2)
      {
        paramArrayOfDouble[j] = (paramArrayOfDouble[j] - this.midPoint.getX() + this.minX);
        paramArrayOfDouble[j] += this.cropBox.width / 2;
        paramArrayOfDouble[(j + 1)] = (paramArrayOfDouble[(j + 1)] - this.midPoint.getY() + this.minY);
        paramArrayOfDouble[(j + 1)] = (0.0D - paramArrayOfDouble[(j + 1)]);
        paramArrayOfDouble[(j + 1)] += this.cropBox.height / 2;
      }
    }
    return paramArrayOfDouble;
  }

  private static int getCoordOffset(int paramInt)
  {
    int i;
    switch (paramInt)
    {
    case 3:
      i = 4;
      break;
    case 2:
      i = 2;
      break;
    default:
      i = 0;
    }
    return i;
  }

  private static boolean isCompleteCropBoxMiss(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, Rectangle paramRectangle)
  {
    int i = paramRectangle.x;
    int j = i + paramRectangle.width;
    int k = paramRectangle.y;
    int m = j + paramRectangle.height;
    return ((paramArrayOfDouble1[0] < i) && (paramArrayOfDouble2[0] < i)) || ((paramArrayOfDouble1[0] > j) && (paramArrayOfDouble2[0] > j) && (((paramArrayOfDouble1[1] < k) && (paramArrayOfDouble2[1] < k)) || ((paramArrayOfDouble1[1] > m) && (paramArrayOfDouble2[1] > m))));
  }

  protected String coordsToStringParam(double[] paramArrayOfDouble, int paramInt)
  {
    int i = paramArrayOfDouble.length;
    double[] arrayOfDouble = new double[i];
    System.arraycopy(paramArrayOfDouble, 0, arrayOfDouble, 0, i);
    paramArrayOfDouble = correctCoords(arrayOfDouble);
    return convertCoords(paramArrayOfDouble, paramInt);
  }

  protected String convertCoords(double[] paramArrayOfDouble, int paramInt)
  {
    String str = "";
    int i = 0;
    int j = 0;
    if (this.cropBox != null)
    {
      i = this.cropBox.width;
      j = this.cropBox.height;
    }
    int k;
    switch (this.pageRotation)
    {
    case 90:
      k = 0;
      while (k < paramInt)
      {
        if (k != 0)
          str = new StringBuilder().append(str).append(",").toString();
        if ((this.ctm[0][0] == 0.0F) && (this.ctm[1][1] == 0.0F) && (this.ctm[0][1] > 0.0F) && ((this.ctm[1][0] < 0.0F) || (Math.abs(this.ctm[1][0]) < 0.5D)))
        {
          str = new StringBuilder().append(str).append(setPrecision((j - paramArrayOfDouble[(k + 1)]) * this.scaling, false)).toString();
          str = new StringBuilder().append(str).append(",").toString();
          str = new StringBuilder().append(str).append(setPrecision(paramArrayOfDouble[k] * this.scaling, (k & 0x1) != 1)).toString();
        }
        else
        {
          str = new StringBuilder().append(str).append(setPrecision((j - paramArrayOfDouble[(k + 1)]) * this.scaling, false)).toString();
          str = new StringBuilder().append(str).append(",").toString();
          str = new StringBuilder().append(str).append(setPrecision(paramArrayOfDouble[k] * this.scaling, (k & 0x1) != 1)).toString();
        }
        k += 2;
      }
      break;
    case 180:
      k = 0;
      while (k < paramInt)
      {
        if (k != 0)
          str = new StringBuilder().append(str).append(",").toString();
        str = new StringBuilder().append(str).append(setPrecision((i - paramArrayOfDouble[k]) * this.scaling, true)).toString();
        if (k + 1 != 0)
          str = new StringBuilder().append(str).append(",").toString();
        str = new StringBuilder().append(str).append(setPrecision((j - paramArrayOfDouble[(k + 1)]) * this.scaling, false)).toString();
        k += 2;
      }
      break;
    case 270:
      k = 0;
      while (k < paramInt)
      {
        if (k != 0)
          str = new StringBuilder().append(str).append(",").toString();
        if ((this.ctm[0][0] == 0.0F) && (this.ctm[1][1] == 0.0F) && (this.ctm[0][1] > 0.0F) && ((this.ctm[1][0] < 0.0F) || (Math.abs(this.ctm[1][0]) < 0.5D)))
        {
          str = new StringBuilder().append(str).append(setPrecision((j - paramArrayOfDouble[(k + 1)]) * this.scaling, false)).toString();
          str = new StringBuilder().append(str).append(",").toString();
          str = new StringBuilder().append(str).append(setPrecision(paramArrayOfDouble[k] * this.scaling, (k & 0x1) != 1)).toString();
        }
        else
        {
          str = new StringBuilder().append(str).append(setPrecision(paramArrayOfDouble[(k + 1)] * this.scaling, false)).toString();
          str = new StringBuilder().append(str).append(",").toString();
          str = new StringBuilder().append(str).append(setPrecision((i - paramArrayOfDouble[k]) * this.scaling, (k & 0x1) != 1)).toString();
        }
        k += 2;
      }
      break;
    default:
      k = 0;
      while (k < paramInt)
      {
        if (k != 0)
          str = new StringBuilder().append(str).append(",").toString();
        str = new StringBuilder().append(str).append(setPrecision(paramArrayOfDouble[k] * this.scaling, true)).toString();
        if (k + 1 != 0)
          str = new StringBuilder().append(str).append(",").toString();
        str = new StringBuilder().append(str).append(setPrecision(paramArrayOfDouble[(k + 1)] * this.scaling, false)).toString();
        k += 2;
      }
    }
    return str;
  }

  public static String rgbToCSSColor(int paramInt)
  {
    int i = paramInt >> 16 & 0xFF;
    int j = paramInt >> 8 & 0xFF;
    int k = paramInt & 0xFF;
    return new StringBuilder().append("rgb(").append(i).append(',').append(j).append(',').append(k).append(')').toString();
  }

  public boolean isEmpty()
  {
    return this.pathCommands.isEmpty();
  }

  public int getShapeColor()
  {
    return this.currentColor;
  }

  public double getMinXcoord()
  {
    return this.minXcoord;
  }

  public double getMinYcoord()
  {
    return this.minYcoord;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.render.output.OutputShape
 * JD-Core Version:    0.6.2
 */