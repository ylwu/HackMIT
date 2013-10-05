package org.jpedal.examples.viewer.javabean;

import java.awt.Image;
import java.beans.BeanDescriptor;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

public class ViewerBeanBeanInfo extends SimpleBeanInfo
{
  private static final int PROPERTY_document = 0;
  private static final int PROPERTY_pageNumber = 1;
  private static final int PROPERTY_rotation = 2;
  private static final int PROPERTY_zoom = 3;
  private static final int PROPERTY_menuBar = 4;
  private static final int PROPERTY_toolBar = 5;
  private static final int PROPERTY_displayOptionsBar = 6;
  private static final int PROPERTY_sideTabBar = 7;
  private static final int PROPERTY_navigationBar = 8;
  private static final int defaultPropertyIndex = -1;
  private static final int defaultEventIndex = -1;

  public BeanDescriptor getBeanDescriptor()
  {
    BeanDescriptor localBeanDescriptor = new BeanDescriptor(ViewerBean.class, null);
    return localBeanDescriptor;
  }

  public PropertyDescriptor[] getPropertyDescriptors()
  {
    PropertyDescriptor[] arrayOfPropertyDescriptor = new PropertyDescriptor[9];
    try
    {
      arrayOfPropertyDescriptor[0] = new PropertyDescriptor("document", ViewerBean.class, null, "setDocument");
      arrayOfPropertyDescriptor[0].setDisplayName("Document");
      arrayOfPropertyDescriptor[0].setShortDescription("Set the default open document");
      arrayOfPropertyDescriptor[0].setPreferred(true);
      arrayOfPropertyDescriptor[1] = new PropertyDescriptor("pageNumber", ViewerBean.class, "getPageNumber", "setPageNumber");
      arrayOfPropertyDescriptor[1].setDisplayName("Page Number");
      arrayOfPropertyDescriptor[1].setShortDescription("Set the page number to open on");
      arrayOfPropertyDescriptor[1].setPreferred(true);
      arrayOfPropertyDescriptor[2] = new PropertyDescriptor("rotation", ViewerBean.class, "getRotation", "setRotation");
      arrayOfPropertyDescriptor[2].setPropertyEditorClass(RotationEditor.class);
      arrayOfPropertyDescriptor[2].setDisplayName("Rotation");
      arrayOfPropertyDescriptor[2].setShortDescription("Set the default rotation");
      arrayOfPropertyDescriptor[2].setPreferred(true);
      arrayOfPropertyDescriptor[3] = new PropertyDescriptor("zoom", ViewerBean.class, "getZoom", "setZoom");
      arrayOfPropertyDescriptor[3].setDisplayName("Zoom");
      arrayOfPropertyDescriptor[3].setShortDescription("Set the default scaling factor");
      arrayOfPropertyDescriptor[3].setPreferred(true);
      arrayOfPropertyDescriptor[4] = new PropertyDescriptor("menuBar", ViewerBean.class, "getMenuBar", "setMenuBar");
      arrayOfPropertyDescriptor[4].setDisplayName("Show Menu Bar");
      arrayOfPropertyDescriptor[4].setShortDescription("Show the Menu Bar");
      arrayOfPropertyDescriptor[4].setPreferred(false);
      arrayOfPropertyDescriptor[4].setConstrained(true);
      arrayOfPropertyDescriptor[5] = new PropertyDescriptor("toolBar", ViewerBean.class, "getToolBar", "setToolBar");
      arrayOfPropertyDescriptor[5].setDisplayName("Show Tool Bar");
      arrayOfPropertyDescriptor[5].setShortDescription("Show the Tool Bar");
      arrayOfPropertyDescriptor[5].setPreferred(false);
      arrayOfPropertyDescriptor[5].setConstrained(true);
      arrayOfPropertyDescriptor[6] = new PropertyDescriptor("displayOptionsBar", ViewerBean.class, "getDisplayOptionsBar", "setDisplayOptionsBar");
      arrayOfPropertyDescriptor[6].setDisplayName("Show Display Options Bar");
      arrayOfPropertyDescriptor[6].setShortDescription("Show the Display Options Bar");
      arrayOfPropertyDescriptor[6].setPreferred(false);
      arrayOfPropertyDescriptor[6].setConstrained(true);
      arrayOfPropertyDescriptor[7] = new PropertyDescriptor("sideTabBar", ViewerBean.class, "getSideTabBar", "setSideTabBar");
      arrayOfPropertyDescriptor[7].setDisplayName("Display Side Tab Bar");
      arrayOfPropertyDescriptor[7].setShortDescription("Display the Side Tab Bar");
      arrayOfPropertyDescriptor[7].setPreferred(false);
      arrayOfPropertyDescriptor[7].setConstrained(true);
      arrayOfPropertyDescriptor[8] = new PropertyDescriptor("navigationBar", ViewerBean.class, "getNavigationBar", "setNavigationBar");
      arrayOfPropertyDescriptor[8].setDisplayName("Display Navigation Bar");
      arrayOfPropertyDescriptor[8].setShortDescription("Display the Navigation Bar");
      arrayOfPropertyDescriptor[8].setPreferred(false);
      arrayOfPropertyDescriptor[8].setConstrained(true);
    }
    catch (IntrospectionException localIntrospectionException)
    {
      localIntrospectionException.printStackTrace();
    }
    return arrayOfPropertyDescriptor;
  }

  public EventSetDescriptor[] getEventSetDescriptors()
  {
    EventSetDescriptor[] arrayOfEventSetDescriptor = new EventSetDescriptor[0];
    return arrayOfEventSetDescriptor;
  }

  public MethodDescriptor[] getMethodDescriptors()
  {
    MethodDescriptor[] arrayOfMethodDescriptor = new MethodDescriptor[0];
    return arrayOfMethodDescriptor;
  }

  public int getDefaultPropertyIndex()
  {
    return -1;
  }

  public int getDefaultEventIndex()
  {
    return -1;
  }

  public Image getIcon(int paramInt)
  {
    return loadImage("/org/jpedal/examples/viewer/res/pdf.png");
  }
}

/* Location:           C:\Users\Yang\Desktop\jpedal\
 * Qualified Name:     org.jpedal.examples.viewer.javabean.ViewerBeanBeanInfo
 * JD-Core Version:    0.6.2
 */