package org.jpedal.render;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.BufferedImage;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;
import org.jpedal.color.GenericColorSpace;
import org.jpedal.color.PdfPaint;
import org.jpedal.fonts.PdfFont;
import org.jpedal.fonts.glyph.PdfGlyph;
import org.jpedal.io.ObjectStore;
import org.jpedal.objects.GraphicsState;

public class FXDisplay extends BaseDisplay
  implements DynamicVectorRenderer
{
  private Group pdfContent = new Group();
  private Shape lastClip = null;

  public FXDisplay()
  {
  }

  public FXDisplay(int paramInt1, boolean paramBoolean, int paramInt2, ObjectStore paramObjectStore)
  {
    this.rawPageNumber = paramInt1;
    this.objectStoreRef = paramObjectStore;
    this.addBackground = paramBoolean;
  }

  public FXDisplay(int paramInt, ObjectStore paramObjectStore, boolean paramBoolean)
  {
    this.rawPageNumber = paramInt;
    this.objectStoreRef = paramObjectStore;
    this.isPrinting = paramBoolean;
  }

  public void flush()
  {
    this.pdfContent.getChildren().clear();
  }

  public int drawImage(int paramInt1, BufferedImage paramBufferedImage, GraphicsState paramGraphicsState, boolean paramBoolean, String paramString, int paramInt2, int paramInt3)
  {
    this.rawPageNumber = paramInt1;
    float[][] arrayOfFloat = paramGraphicsState.CTM;
    WritableImage localWritableImage = SwingFXUtils.toFXImage(paramBufferedImage, null);
    double d1 = localWritableImage.getWidth();
    double d2 = localWritableImage.getHeight();
    ImageView localImageView = new ImageView(localWritableImage);
    double[] arrayOfDouble1 = new double[6];
    if (paramInt2 == 1)
    {
      arrayOfDouble1 = new double[] { arrayOfFloat[0][0] / d1, arrayOfFloat[0][1] / d1, arrayOfFloat[1][0] / d2, arrayOfFloat[1][1] / d2, arrayOfFloat[2][0], arrayOfFloat[2][1] };
      localImageView.getTransforms().setAll(new Transform[] { Transform.affine(arrayOfDouble1[0], arrayOfDouble1[1], arrayOfDouble1[2], arrayOfDouble1[3], arrayOfDouble1[4], arrayOfDouble1[5]) });
    }
    else if (paramInt2 == 0)
    {
      arrayOfDouble1 = new double[] { arrayOfFloat[0][0] / d1, arrayOfFloat[0][1] / d1, arrayOfFloat[1][0] / d2, -arrayOfFloat[1][1] / d2, arrayOfFloat[2][0], arrayOfFloat[2][1] + arrayOfFloat[1][1] };
      localImageView.getTransforms().setAll(new Transform[] { Transform.affine(arrayOfDouble1[0], arrayOfDouble1[1], arrayOfDouble1[2], arrayOfDouble1[3], arrayOfDouble1[4], arrayOfDouble1[5]) });
    }
    Shape localShape = paramGraphicsState.getFXClippingShape();
    if (localShape != null)
      try
      {
        AffineTransform localAffineTransform = new AffineTransform(arrayOfDouble1[0], arrayOfDouble1[1], arrayOfDouble1[2], arrayOfDouble1[3], arrayOfDouble1[4], arrayOfDouble1[5]);
        double[] arrayOfDouble2 = new double[6];
        localAffineTransform = localAffineTransform.createInverse();
        localAffineTransform.getMatrix(arrayOfDouble2);
        localShape.getTransforms().add(Transform.affine(arrayOfDouble2[0], arrayOfDouble2[1], arrayOfDouble2[2], arrayOfDouble2[3], arrayOfDouble2[4], arrayOfDouble2[5]));
        localImageView.setClip(localShape);
      }
      catch (NoninvertibleTransformException localNoninvertibleTransformException)
      {
        localNoninvertibleTransformException.printStackTrace();
      }
    this.pdfContent.getChildren().add(localImageView);
    this.currentItem += 1;
    return this.currentItem - 1;
  }

  public void drawShape(Path paramPath, GraphicsState paramGraphicsState, int paramInt)
  {
    setFXParams(paramPath, paramGraphicsState.getFillType(), paramGraphicsState);
    this.pdfContent.getChildren().add(paramPath);
  }

  private void setFXParams(Shape paramShape, int paramInt, GraphicsState paramGraphicsState)
  {
    int i;
    int j;
    int k;
    int m;
    double d;
    if ((paramInt == 2) || (paramInt == 3))
    {
      i = paramGraphicsState.nonstrokeColorSpace.getColor().getRGB();
      j = i >> 16 & 0xFF;
      k = i >> 8 & 0xFF;
      m = i & 0xFF;
      d = paramGraphicsState.getAlpha(2);
      paramShape.setFill(Color.rgb(j, k, m, d));
    }
    if (paramInt == 1)
    {
      i = paramGraphicsState.strokeColorSpace.getColor().getRGB();
      j = i >> 16 & 0xFF;
      k = i >> 8 & 0xFF;
      m = i & 0xFF;
      d = paramGraphicsState.getAlpha(1);
      paramShape.setStroke(Color.rgb(j, k, m, d));
    }
  }

  public void drawEmbeddedText(float[][] paramArrayOfFloat, int paramInt1, PdfGlyph paramPdfGlyph, Object paramObject, int paramInt2, GraphicsState paramGraphicsState, AffineTransform paramAffineTransform, String paramString, PdfFont paramPdfFont, float paramFloat)
  {
    if (paramInt2 == 6)
      return;
    Object localObject1;
    Object localObject2;
    if ((paramPdfGlyph == null) && (paramObject == null))
    {
      localObject1 = new Text(paramString);
      localObject2 = new double[6];
      paramAffineTransform.getMatrix((double[])localObject2);
      Font localFont = Font.font("Arial", paramInt1);
      ((Text)localObject1).setFont(localFont);
      setFXParams((Shape)localObject1, 2, paramGraphicsState);
      if ((paramGraphicsState.getTextRenderType() & 0x1) == 1)
        setFXParams((Shape)localObject1, 1, paramGraphicsState);
      double d2;
      if (paramInt2 != 4)
      {
        d2 = 1.0D / paramInt1;
        ((Text)localObject1).getTransforms().add(Transform.affine(localObject2[0] * d2, localObject2[1] * d2, localObject2[2] * d2, localObject2[3] * d2, paramArrayOfFloat[2][0], paramArrayOfFloat[2][1]));
      }
      else
      {
        d2 = 1.0D / paramInt1;
        ((Text)localObject1).getTransforms().setAll(new Transform[] { Transform.affine(paramArrayOfFloat[0][0] * d2, paramArrayOfFloat[0][1] * d2, paramArrayOfFloat[1][0] * d2, paramArrayOfFloat[1][1] * d2, paramArrayOfFloat[2][0], paramArrayOfFloat[2][1]) });
      }
      this.pdfContent.getChildren().add(localObject1);
    }
    else
    {
      localObject1 = new double[6];
      paramAffineTransform.getMatrix((double[])localObject1);
      localObject2 = paramPdfGlyph.getPath();
      ((Path)localObject2).setFillRule(FillRule.EVEN_ODD);
      if (paramInt2 != 4)
      {
        ((Path)localObject2).getTransforms().setAll(new Transform[] { Transform.affine(localObject1[0], localObject1[1], localObject1[2], localObject1[3], localObject1[4], localObject1[5]) });
      }
      else
      {
        double d1 = 0.01D;
        ((Path)localObject2).getTransforms().add(Transform.affine(localObject1[0] * d1, localObject1[1] * d1, localObject1[2] * d1, localObject1[3] * d1, localObject1[4], localObject1[5]));
      }
      setFXParams((Shape)localObject2, paramGraphicsState.getTextRenderType(), paramGraphicsState);
      this.pdfContent.getChildren().add(localObject2);
    }
  }

  public Group getFXPane()
  {
    return this.pdfContent;
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.render.FXDisplay
 * JD-Core Version:    0.6.2
 */