package launcher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class SplashScreen extends JFrame {
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel imageLabel = new JLabel();
  JProgressBar progressBar = new JProgressBar();
  ImageIcon imageIcon;
  JLabel title = null;							// 栏目名称
  JLabel head = null;								// 蓝色标题
  JLabel close = null;	
private int positionX;
private int positionY;

  public SplashScreen(ImageIcon imageIcon) {
	  super();
    this.imageIcon = imageIcon;
    try {
      jbInit();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  // note - this class created with JBuilder
  void jbInit() throws Exception {
  	setUndecorated(true);
  	setTitle("Loading");
	title = new JLabel("Loading");
	this.setSize(260, 60);
	close = new JLabel(" x");
	
	title.setPreferredSize(new Dimension(230, 26));
	title.setVerticalTextPosition(JLabel.CENTER);
	title.setHorizontalTextPosition(JLabel.CENTER);
	title.setFont(new Font("宋体", Font.PLAIN, 12));
	title.setForeground(Color.black);

	close.setFont(new Font("Arial", Font.BOLD, 15));
	close.setPreferredSize(new Dimension(20, 20));
	close.setVerticalTextPosition(JLabel.CENTER);
	close.setHorizontalTextPosition(JLabel.CENTER);
	close.setCursor(new Cursor(12));
	close.setToolTipText("关闭取消更新");
	JPanel headPan = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
	headPan.setOpaque(false);
	headPan.add(title);
	headPan.add(close);
	
    imageLabel.setIcon(imageIcon);
    this.getContentPane().setLayout(borderLayout1);
    progressBar.setBorderPainted(false);
    getContentPane().add(progressBar, BorderLayout.CENTER);
    progressBar.setForeground(Color.BLACK);
    progressBar.setBorder(new EmptyBorder(0, 0, 0, 0));
    progressBar.setPreferredSize(new Dimension(250, 25));

    this.getContentPane().add(headPan, BorderLayout.NORTH);
    handle();
   // this.pack();
    
    
  }
	public void handle() {

		title.addMouseMotionListener(
				new java.awt.event.MouseMotionAdapter() {
					public void mouseDragged(MouseEvent e) {
						this_mouseDragged(e);
					}

				});
		
		title.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				this_mousePressed(e);

			}

			public void mousePressed(MouseEvent e) {
				this_mousePressed(e);
			}
		});
		// 右上角关闭按钮事件
		close.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
			public void mouseEntered(MouseEvent e) {
				close.setBorder(BorderFactory.createLineBorder(Color.gray));
			}
			public void mouseExited(MouseEvent e) {
				close.setBorder(null);
			}
		});
	}

	void this_mouseDragged(MouseEvent e) {
		Point point = this.getLocation();
		this.setLocation(point.x + e.getX() - positionX, point.y + e.getY()
				- positionY);
	}

	void this_mousePressed(MouseEvent e) {
		positionX = e.getX();
		positionY = e.getY();
	}

	public void setProgressMax(int maxProgress) {
		progressBar.setMaximum(maxProgress);
	}

	public void setProgress(int progress) {
		final int theProgress = progress;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				progressBar.setValue(theProgress);
			}
		});
	}

  public void setProgress(String message, int progress)
  {
    final int theProgress = progress;
    final String theMessage = message;
    setProgress(progress);
    SwingUtilities.invokeLater(new ProcessRunnable(theProgress, theMessage));
/*    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        progressBar.setValue(theProgress);
        setMessage(theMessage);
      }
    });*/
  }

  class ProcessRunnable implements Runnable
  {
	  private int theProgress;
	  private String theMessage;
	  
	public ProcessRunnable(int theProgress, String theMessage) {
		super();
		this.theProgress = theProgress;
		this.theMessage = theMessage;
	}

	public void run() {
		// TODO Auto-generated method stub
        progressBar.setValue(theProgress);
        setMessage(theMessage);
	}
	  
  }
  public void setScreenVisible(boolean b)
  {
    final boolean boo = b;
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        setVisible(boo);
      }
    });
  }

  private void setMessage(String message)
  {
    if (message==null)
    {
      message = "";
      progressBar.setStringPainted(false);
    }
    else
    {
      progressBar.setStringPainted(true);
    }
    progressBar.setString(message);
  }
}
