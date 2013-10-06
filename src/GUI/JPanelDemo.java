package GUI;

/*
 * ===========================================
 * Java Pdf Extraction Decoding Access Library
 * ===========================================
 *
 * Project Info:  http://www.idrsolutions.com
 * Help section for developers at http://www.idrsolutions.com/java-pdf-library-support/
 *
 * (C) Copyright 1997-2013, IDRsolutions and Contributors.
 *
 *   This file is part of JPedal
 *
 This source code is copyright IDRSolutions 2012


 *
 * ---------------
 * JPanelDemo.java
 * ---------------
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EventObject;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import leap.LeapEvent;
import leap.LeapEventListener;
import leap.LeapInput;

import org.jpedal.PdfDecoder;
import org.jpedal.examples.viewer.utils.FileFilterer;
import org.jpedal.examples.viewer.utils.IconiseImage;
import org.jpedal.exception.PdfException;
import org.jpedal.fonts.FontMappings;
import org.jpedal.objects.PdfImageData;
import org.jpedal.objects.PdfPageData;


/**
 * very simple example of a viewer
 * 
 * We recommend you look at our fully-featured, totally configurable version
 * http
 * ://files.idrsolutions.com/samplecode/org/jpedal/examples/viewer/Viewer.java
 * .html
 */
public class JPanelDemo extends JFrame {

	private String viewerTitle = "Jpanel Demo";

	/** the actual JPanel/decoder object */
	public PdfDecoder pdfDecoder;

	/** name of current PDF file */
	private String currentFile = null;

	/** current page number (first page is 1) */
	private int currentPage = 1;
	private int currentScale = 100;
	private PdfImageData[] pageImages;
	private PdfPageData[] pageTexts;
	
	private Container cPane;
	private JScrollPane display;

	private final JLabel pageCounter1 = new JLabel("Page ");
	private JTextField pageCounter2 = new JTextField(4);// 000 used to set //
														// prefered size
	private final JLabel pageCounter3 = new JLabel("of");// 000 used to set
															// prefered // size
	private JTextField scaling = new JTextField(4);
	private final JLabel scaling2 = new JLabel("%"); 

	/**
	 * construct a pdf viewer, passing in the full file name
	 */
	public JPanelDemo(String name) {
		pdfDecoder = new PdfDecoder(true);
		// ensure non-embedded font map to sensible replacements
		FontMappings.setFontReplacements();
		currentFile = name;// store file name for use in page changer
		// setup our GUI display
		initializeViewer();
		// set page number display
		LeapInput leap = new LeapInput();
        leap.start();
        leap.addEventListener(new LeapMotion());
	}

	/**
	 * construct an empty pdf viewer and pop up the open window
	 */
	public JPanelDemo() {
		setTitle(viewerTitle);
		pdfDecoder = new PdfDecoder(true);
		// ensure non-embedded font map to sensible replacements
		FontMappings.setFontReplacements();
		initializeViewer();
		LeapInput leap = new LeapInput();
        leap.start();
        leap.addEventListener(new LeapMotion());
	}

	/**
	 * opens a chooser and allows user to select a pdf file and opens it
	 */
	private void selectFile() {
		pageImages = null;
		pageTexts = null;
		JFileChooser open = new JFileChooser(".");
		open.setFileSelectionMode(JFileChooser.FILES_ONLY);
		String[] pdf = new String[] { "pdf" };
		open.addChoosableFileFilter(new FileFilterer(pdf, "Pdf (*.pdf)"));
		int resultOfFileSelect = JFileChooser.ERROR_OPTION;
		while (resultOfFileSelect == JFileChooser.ERROR_OPTION) {
			resultOfFileSelect = open.showOpenDialog(this);
			if (resultOfFileSelect == JFileChooser.ERROR_OPTION)
				System.err.println("JFileChooser error");
			if (resultOfFileSelect == JFileChooser.APPROVE_OPTION) {
				currentFile = open.getSelectedFile().getAbsolutePath();
				currentPage = 1;
				try {
					// close the current pdf before opening another
					pdfDecoder.closePdfFile();
					// this opens the PDF and reads its internal details
					pdfDecoder.openPdfFile(currentFile);
					// check for password encription and acertain
					if (!checkEncryption()) {
						// if file content is not accessable make user select a
						// different file
						resultOfFileSelect = JFileChooser.CANCEL_OPTION;
					}

					// these 2 lines opens page 1 at 100% scaling
/*					pageImages = new PdfImageData[pageNumber];
					pageTexts = new PdfPageData[pageNumber];
					for (int i=0; i<pageNumber; i++){
						pdfDecoder.decodePage(i+1);
						pageImages[i+1] = pdfDecoder.getPdfImageData();
						pageTexts[i+1] = pdfDecoder.getPdfPageData();
					}*/
					pdfDecoder.decodePage(currentPage);
					// these 2 lines opens page 1 at 100% scaling
					pdfDecoder.setPageParameters(1, 1); // values scaling // (1=100%). page number
					pdfDecoder.invalidate();
				} catch (Exception e) {
					e.printStackTrace();
				}

				// set page number display
				pageCounter2.setText(String.valueOf(currentPage));
				pageCounter3.setText("of " + pdfDecoder.getPageCount());
				setTitle(viewerTitle + " - " + currentFile);
				scaling.setText(String.valueOf(currentScale));
				repaint();
				display.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				display.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			}
		}
	}

	/**
	 * check if encryption present and acertain password, return true if content
	 * accessable
	 */
	private boolean checkEncryption() {

		// check if file is encrypted
		if (pdfDecoder.isEncrypted()) {

			// if file has a null password it will have been decoded and
			// isFileViewable will return true
			while (!pdfDecoder.isFileViewable()) {

				/** popup window if password needed */
				String password = JOptionPane.showInputDialog(this,
						"Please enter password");

				/** try and reopen with new password */
				if (password != null) {
					try {
						pdfDecoder.setEncryptionPassword(password);
					} catch (PdfException e) {
						e.printStackTrace(); // To change body of catch
												// statement use File | Settings
												// | File Templates.
					}
					// pdfDecoder.verifyAccess();

				}
			}
			return true;
		}
		// if not encrypted return true
		return true;
	}

	/**
	 * setup the viewer and its components
	 */
	private void initializeViewer() {

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		cPane = getContentPane();
		cPane.setLayout(new BorderLayout());
		
		JButton open = initOpenBut();// setup open button
		Component[] itemsToAdd = initChangerPanel();// setup page display and
													// changer
		
		JPanel topBar = new JPanel();
		topBar.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		topBar.add(open);
		// topBar.add(pageChanger);
		for (Component anItemsToAdd : itemsToAdd) {
			topBar.add(anItemsToAdd);
		}

		cPane.add(topBar, BorderLayout.NORTH);
		// setup scrollpane with pdf							
		initPDFDisplay();
		// display inside
		cPane.add(display, BorderLayout.CENTER);
		pack();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screen.width*2/3, screen.height*2/3);
		setLocationRelativeTo(null);// centre on screen
		setVisible(true);
	}

	/**
	 * returns the open button with listener
	 */
	private JButton initOpenBut() {

		JButton open = new JButton();
		open.setIcon(new ImageIcon(getClass().getResource(
				"/org/jpedal/examples/viewer/res/open.gif"))); //$NON-NLS-1$
		open.setText("Open");
		open.setToolTipText("Open a file");
		open.setBorderPainted(false);
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectFile();
			}
		});

		return open;
	}

	/**
	 * returns the scrollpane with pdfDecoder set as the viewport
	 */
	private void initPDFDisplay() {
		display = new JScrollPane();
		display
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		display
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		display.setViewportView(pdfDecoder);
	}

	/**
	 * setup the page display and changer panel and return it
	 */
	private Component[] initChangerPanel() {

		Component[] list = new Component[19];

		/** back to page 1 */
		JButton start = new JButton();
		start.setBorderPainted(false);
		URL startImage = getClass().getResource(
				"/org/jpedal/examples/viewer/res/start.gif");
		start.setIcon(new ImageIcon(startImage));
		start.setToolTipText("Rewind to page 1");
		// currentBar1.add(start);
		list[0] = start;
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pdfDecoder.setDisplayView(1, 1);
				if (currentFile != null && currentPage != 1) {
					currentPage = 1;
					try {
						pdfDecoder.decodePage(currentPage);
						pdfDecoder.invalidate();
						repaint();
						display.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						display.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
						display.getVerticalScrollBar().setValue(0);
						display.getHorizontalScrollBar().setValue(0);
					} catch (Exception e1) {
						System.err.println("back to page 1");
						e1.printStackTrace();
					}
					// set page number display
					pageCounter2.setText(String.valueOf(currentPage));
				}
			}
		});

		/** back 10 icon */
		JButton fback = new JButton();
		fback.setBorderPainted(false);
		URL fbackImage = getClass().getResource(
				"/org/jpedal/examples/viewer/res/fback.gif");
		fback.setIcon(new ImageIcon(fbackImage));
		fback.setToolTipText("Rewind 10 pages");
		// currentBar1.add(fback);
		list[1] = fback;
		fback.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pdfDecoder.setDisplayView(1, 1);
				if (currentFile != null && currentPage > 10) {
					currentPage -= 10;
					try {
						pdfDecoder.decodePage(currentPage);
						pdfDecoder.invalidate();
						repaint();
						display.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						display.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
						display.getVerticalScrollBar().setValue(0);
						display.getHorizontalScrollBar().setValue(0);
					} catch (Exception e1) {
						System.err.println("back 10 pages");
						e1.printStackTrace();
					}
					// set page number display
					pageCounter2.setText(String.valueOf(currentPage));
				}
			}
		});

		/** back icon */
		JButton back = new JButton();
		back.setBorderPainted(false);
		URL backImage = getClass().getResource(
				"/org/jpedal/examples/viewer/res/back.gif");
		back.setIcon(new ImageIcon(backImage));
		back.setToolTipText("Rewind one page");
		// currentBar1.add(back);
		list[2] = back;
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pdfDecoder.setDisplayView(1, 1);
				if (currentFile != null && currentPage > 1) {
					currentPage -= 1;
					try {
						pdfDecoder.decodePage(currentPage);
						pdfDecoder.invalidate();
						repaint();
						display.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						display.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
						display.getVerticalScrollBar().setValue(0);
						display.getHorizontalScrollBar().setValue(0);
					} catch (Exception e1) {
						System.err.println("back 1 page");
						e1.printStackTrace();
					}
					// set page number display
					pageCounter2.setText(String.valueOf(currentPage));
				}
			}
		});

		pageCounter2.setEditable(true);
		pageCounter2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent a) {

				String value = pageCounter2.getText().trim();
				int newPage;

				// allow for bum values
				try {
					pdfDecoder.setDisplayView(1, 1);
					newPage = Integer.parseInt(value);
					if ((newPage > pdfDecoder.getPageCount()) | (newPage < 1)) {
						return;
					}
					currentPage = newPage;
					try {
						pdfDecoder.decodePage(currentPage);
						pdfDecoder.invalidate();
						repaint();
						display.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						display.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
						display.getVerticalScrollBar().setValue(0);
						display.getHorizontalScrollBar().setValue(0);
					} catch (Exception e) {
						System.err.println("page number entered");
						e.printStackTrace();
					}
				} catch (Exception e) {
					JOptionPane
							.showMessageDialog(
									null,
									'>'
											+ value
											+ "< is Not a valid Value.\nPlease enter a number between 1 and "
											+ pdfDecoder.getPageCount());
				}

			}

		});

		/** put page count in middle of forward and back */
		// currentBar1.add(pageCounter1);
		// currentBar1.add(new JPanel());//add gap
		// currentBar1.add(pageCounter2);
		// currentBar1.add(new JPanel());//add gap
		// currentBar1.add(pageCounter3);
		list[3] = pageCounter1;
		list[4] = new JPanel();
		list[5] = pageCounter2;
		list[6] = new JPanel();
		list[7] = pageCounter3;

		/** forward icon */
		JButton forward = new JButton();
		forward.setBorderPainted(false);
		URL fowardImage = getClass().getResource(
				"/org/jpedal/examples/viewer/res/forward.gif");
		forward.setIcon(new ImageIcon(fowardImage));
		forward.setToolTipText("forward 1 page");
		// currentBar1.add(forward);
		list[8] = forward;
		forward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pdfDecoder.setDisplayView(1, 1);
				if (currentFile != null
						&& currentPage < pdfDecoder.getPageCount()) {
					currentPage += 1;
					try {
						pdfDecoder.decodePage(currentPage);
						pdfDecoder.invalidate();
						repaint();
						display.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						display.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
						display.getVerticalScrollBar().setValue(0);
						display.getHorizontalScrollBar().setValue(0);
					} catch (Exception e1) {
						System.err.println("forward 1 page");
						e1.printStackTrace();
					}
					// set page number display
					pageCounter2.setText(String.valueOf(currentPage));
				}
			}
		});

		/** fast forward icon */
		JButton fforward = new JButton();
		fforward.setBorderPainted(false);
		URL ffowardImage = getClass().getResource(
				"/org/jpedal/examples/viewer/res/fforward.gif");
		fforward.setIcon(new ImageIcon(ffowardImage));
		fforward.setToolTipText("Fast forward 10 pages");
		// currentBar1.add(fforward);
		list[9] = fforward;
		fforward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pdfDecoder.setDisplayView(1, 1);
				if (currentFile != null
						&& currentPage < pdfDecoder.getPageCount() - 9) {
					currentPage += 10;
					try {
						pdfDecoder.decodePage(currentPage);
						pdfDecoder.invalidate();
						repaint();
						display.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						display.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
						display.getVerticalScrollBar().setValue(0);
						display.getHorizontalScrollBar().setValue(0);
					} catch (Exception e1) {
						System.err.println("forward 10 pages");
						e1.printStackTrace();
					}

					// set page number display
					pageCounter2.setText(String.valueOf(currentPage));
				}
			}
		});

		/** goto last page */
		JButton end = new JButton();
		end.setBorderPainted(false);
		URL endImage = getClass().getResource(
				"/org/jpedal/examples/viewer/res/end.gif");
		end.setIcon(new ImageIcon(endImage));
		end.setToolTipText("Fast forward to last page");
		// currentBar1.add(end);
		list[10] = end;
		end.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pdfDecoder.setDisplayView(1, 1);
				if (currentFile != null
						&& currentPage < pdfDecoder.getPageCount()) {
					currentPage = pdfDecoder.getPageCount();
					try {
						pdfDecoder.decodePage(currentPage);
						pdfDecoder.invalidate();
						repaint();
					} catch (Exception e1) {
						System.err.println("forward to last page");
						e1.printStackTrace();
					}

					// set page number display
					pageCounter2.setText(String.valueOf(currentPage));
					repaint();
					display.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					display.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
					display.getVerticalScrollBar().setValue(0);
					display.getHorizontalScrollBar().setValue(0);
				}
			}
		});

		list[11] = new JPanel();
		scaling.setEditable(true);
		list[12] = scaling;
		scaling.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {
				pdfDecoder.setDisplayView(1, 1);
				String value = scaling.getText().trim();
				try {
					currentScale = Integer.parseInt(value);
					float factor = (float) ((double) currentScale / 100);
					pdfDecoder.setPageParameters(factor, currentPage);
					pdfDecoder.invalidate();
				} catch (Exception e) {
					JOptionPane
							.showMessageDialog(
									null,
									"< "
											+ value
											+ " > is Not a valid Value.\nPlease enter an integer");
				}
				repaint();
				display.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				display.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			}
		});

		list[13] = scaling2;
		
		JButton scrollUp = new JButton();
		scrollUp.setToolTipText("scroll up");
		scrollUp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pdfDecoder.setDisplayView(1, 1);
				int position = display.getVerticalScrollBar().getValue();
				if(position>100){
					display.getVerticalScrollBar().setValue(position-100);
				}else{
					display.getVerticalScrollBar().setValue(0);
				}
			}
		});
		
		JButton scrollDown = new JButton();
		scrollDown.setToolTipText("scroll down");
		scrollDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pdfDecoder.setDisplayView(1, 1);
				int position = display.getVerticalScrollBar().getValue();
				int possPosi = display.getVerticalScrollBar().getMaximum()-display.getVerticalScrollBar().getHeight();
				if(possPosi-100>position){
					display.getVerticalScrollBar().setValue(position+100);
				}else{
					display.getVerticalScrollBar().setValue(possPosi);
				}
			}
		});
		
		JButton scrollLeft = new JButton();
		scrollLeft.setToolTipText("scroll left");
		scrollLeft.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pdfDecoder.setDisplayView(1, 1);
				int position = display.getHorizontalScrollBar().getValue();
				if(position>100){
					display.getHorizontalScrollBar().setValue(position-100);
				}else{
					display.getHorizontalScrollBar().setValue(0);
				}
			}
		});
		
		JButton scrollRight = new JButton();
		scrollRight.setToolTipText("scroll right");
		scrollRight.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				pdfDecoder.setDisplayView(1, 1);
				int position = display.getHorizontalScrollBar().getValue();
				int possPosi = display.getHorizontalScrollBar().getMaximum()-display.getHorizontalScrollBar().getHeight();
				if(possPosi-100>position){
					display.getHorizontalScrollBar().setValue(position+100);
				}else{
					display.getHorizontalScrollBar().setValue(possPosi);
				}
			}
		});
		
		list[14] = new JPanel();
		list[15] = scrollUp;
		list[16] = scrollDown;
		list[17] = scrollLeft;
		list[18] = scrollRight;
		
		return list;
	}
	
	
	public void extractSelectedScreenAsImage(int i,int k, int j, int m)
	  {
   
    float f = 100.0F;
    final BufferedImage localBufferedImage = pdfDecoder.getSelectedRectangleOnscreen(i, k, j, m, f);
    File outputfile = new File("image.jpg");
    try {
        ImageIO.write(localBufferedImage, "jpg", outputfile);
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }  
	}
	
	public void zoomIn(float zoomF){
	    pdfDecoder.setDisplayView(1,1);
            currentScale = Integer.parseInt(scaling.getText().trim());
            int newScale = (int)(currentScale + zoomF/2);
            float factor = (float) ((double)newScale/100);
            pdfDecoder.setPageParameters(factor, currentPage);
            pdfDecoder.invalidate();
            scaling.setText(Integer.toString(newScale));
        repaint();
        display.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		display.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	}
	
	public void zoomOut(float zoomF){
	        pdfDecoder.setDisplayView(1,1);
	            currentScale = Integer.parseInt(scaling.getText().trim());
	            int newScale = (int)(currentScale - zoomF/2);
	            float factor = (float) ((double)newScale/100);
	            pdfDecoder.setPageParameters(factor, currentPage);
	            pdfDecoder.invalidate();
	            scaling.setText(Integer.toString(newScale));
	        repaint();
	        display.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			display.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	}
	

	public void forwardPage() {
		if (currentFile != null && currentPage < pdfDecoder.getPageCount()) {
			currentPage += 1;
			try {
				pdfDecoder.decodePage(currentPage);
				pdfDecoder.invalidate();
				repaint();
				display.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				display.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				display.getVerticalScrollBar().setValue(0);
				display.getHorizontalScrollBar().setValue(0);
			} catch (Exception e1) {
				System.err.println("forward 1 page");
				e1.printStackTrace();
			}

			// set page number display
			pageCounter2.setText(String.valueOf(currentPage));
		}
	}

	public void backPage() {
		if (currentFile != null && currentPage > 1) {
			currentPage -= 1;
			try {
				pdfDecoder.decodePage(currentPage);
				pdfDecoder.invalidate();
				repaint();
				display.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				display.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				display.getVerticalScrollBar().setValue(0);
				display.getHorizontalScrollBar().setValue(0);
			} catch (Exception e1) {
				System.err.println("back 1 page");
				e1.printStackTrace();
			}

			// set page number display
			pageCounter2.setText(String.valueOf(currentPage));
		}

	}

	public void getFirstPage() {
		if (currentFile != null && currentPage != 1) {
			currentPage = 1;
			try {
				pdfDecoder.decodePage(currentPage);
				pdfDecoder.invalidate();
				repaint();
				display.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				display.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				display.getVerticalScrollBar().setValue(0);
				display.getHorizontalScrollBar().setValue(0);
			} catch (Exception e1) {
				System.err.println("back to page 1");
				e1.printStackTrace();
			}

			// set page number display
			pageCounter2.setText(String.valueOf(currentPage));
		}

	}

	public void fastbackward() {
		if (currentFile != null && currentPage > 10) {
			currentPage -= 10;
			try {
				pdfDecoder.decodePage(currentPage);
				pdfDecoder.invalidate();
				repaint();
				display.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				display.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				display.getVerticalScrollBar().setValue(0);
				display.getHorizontalScrollBar().setValue(0);
			} catch (Exception e1) {
				System.err.println("back 10 pages");
				e1.printStackTrace();
			}

			// set page number display
			pageCounter2.setText(String.valueOf(currentPage));
		}
	}

	public void fastforward() {
		if (currentFile != null && currentPage < pdfDecoder.getPageCount() - 9) {
			currentPage += 10;
			try {
				pdfDecoder.decodePage(currentPage);
				pdfDecoder.invalidate();
				repaint();
				display.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				display.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				display.getVerticalScrollBar().setValue(0);
				display.getHorizontalScrollBar().setValue(0);
			} catch (Exception e1) {
				System.err.println("forward 10 pages");
				e1.printStackTrace();
			}

			// set page number display
			pageCounter2.setText(String.valueOf(currentPage));
		}
	}

	public void getLastPage() {
		if (currentFile != null && currentPage < pdfDecoder.getPageCount()) {
			currentPage = pdfDecoder.getPageCount();
			try {
				pdfDecoder.decodePage(currentPage);
				pdfDecoder.invalidate();
				repaint();
				display.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
				display.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				display.getVerticalScrollBar().setValue(0);
				display.getHorizontalScrollBar().setValue(0);
			} catch (Exception e1) {
				System.err.println("forward to last page");
				e1.printStackTrace();
			}

			// set page number display
			pageCounter2.setText(String.valueOf(currentPage));
		}

	}
	
	public void scrollUp(float speed){
		pdfDecoder.setDisplayView(1, 1);
		int move = (int) Math.exp((double)speed/10)*5;
		int position = display.getVerticalScrollBar().getValue();
		if(position>move){
			display.getVerticalScrollBar().setValue(position-move);
		}else{
			display.getVerticalScrollBar().setValue(0);
		}
	}
	
	public void scrollDown(float speed){
		pdfDecoder.setDisplayView(1, 1);
		int move = (int) Math.exp((double)speed/10)*5;
		int position = display.getVerticalScrollBar().getValue();
		int possPosi = display.getVerticalScrollBar().getMaximum()-display.getVerticalScrollBar().getHeight();
		if(possPosi-move>position){
			display.getVerticalScrollBar().setValue(position+move);
		}else{
			display.getVerticalScrollBar().setValue(possPosi);
		}
	}
	
	public void scrollLeft(float speed){
		pdfDecoder.setDisplayView(1, 1);
		int move = (int) Math.exp((double)speed/10)*5;
		int position = display.getHorizontalScrollBar().getValue();
		if(position>move){
			display.getHorizontalScrollBar().setValue(position-move);
		}else{
			display.getHorizontalScrollBar().setValue(0);
		}
	}
	
	public void scrollRight(float speed){
		pdfDecoder.setDisplayView(1, 1);
		int move = (int) Math.exp((double)speed/10)*5;
		int position = display.getHorizontalScrollBar().getValue();
		int possPosi = display.getHorizontalScrollBar().getMaximum()-display.getHorizontalScrollBar().getHeight();
		if(possPosi-move>position){
			display.getHorizontalScrollBar().setValue(position+move);
		}else{
			display.getHorizontalScrollBar().setValue(possPosi);
		}
	}

	public class LeapMotion implements LeapEventListener {
	    
	    public LeapMotion(){
	        
	    }

	    public void handleLeapEvent(LeapEvent e) {
            
	        System.out.println("getEvent");
	        
			String command = e.message;
			System.out.println(command);
			String[] coordinate = command.split(",");

			// forward 1 page
			if (coordinate[0] .equals("swipe")
					&& Float.parseFloat(coordinate[1]) > 100) {
				forwardPage();
			}
			// back 1 page
			if (coordinate[0].equals("swipe")
					&& Float.parseFloat(coordinate[1]) < -100) {
				backPage();
			}
			// back to first page
			if (coordinate[0].equals("jump")
					&& Float.parseFloat(coordinate[0]) < -100) {
				getFirstPage();
			}
			// fast backward 10 pages
			if (coordinate[0].equals("fast")
					&& Float.parseFloat(coordinate[0]) < -100) {
				fastbackward();

			}
			// fast forward 10 pages
			if (coordinate[0].equals("fast")
					&& Float.parseFloat(coordinate[0]) > 100) {
				fastforward();

			}
			// fast forward to last page
			if (coordinate[0].equals("jump")
					&& Float.parseFloat(coordinate[0]) > 100) {
				getLastPage();
			}
			
			if (coordinate[0].equals("zoomIn")){
			    zoomIn(Float.parseFloat(coordinate[1]));
			}
			
			if (coordinate[0].equals("zoomOut")){
                zoomOut(Float.parseFloat(coordinate[1]));
            }
			
			if (coordinate[0].equals("scroll")){
				float speedX = Float.parseFloat(coordinate[1]);
				float speedY = Float.parseFloat(coordinate[2]);
				if(speedX>0){scrollRight(speedX);}
				else{scrollLeft(Math.abs(speedX));}
				if(speedY>0){scrollUp(speedY);}
				else{scrollDown(Math.abs(speedY));}	
			}
			        
		}

	}

	/** create a standalone program. User may pass in name of file as option */
/*	public static void main(String[] args) {
		*//** Run the software *//*
		if (args.length > 0) {
			new JPanelDemo(args[0]);
		} else {
			new JPanelDemo();
		}
	}*/
}
