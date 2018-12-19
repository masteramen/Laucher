package launch;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;
class BarUI extends BasicProgressBarUI { 

    private Rectangle r = new Rectangle(); 

    @Override 
    protected void paintIndeterminate(Graphics g, JComponent c) { 
     Graphics2D G2D = (Graphics2D) g; 
     G2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
               RenderingHints.VALUE_ANTIALIAS_ON); 
     r = getBox(r); 
     g.setColor(c.getForeground()); 
     g.fillRect(r.x,r.y,r.width,r.height); 
    } 
   } 


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
  	
    JPanel wrapPanel = new JPanel(new BorderLayout());

  	setTitle("Loading");
	title = new JLabel("Loading");
	this.setSize(260, 60);
	close = new JLabel(" x");
	
	title.setPreferredSize(new Dimension(230, 26));
	title.setVerticalTextPosition(JLabel.CENTER);
	title.setHorizontalTextPosition(JLabel.CENTER);
	title.setFont(new Font("宋体", Font.PLAIN, 12));
	title.setForeground(Color.black);
	title.setOpaque(false);
	close.setFont(new Font("Arial", Font.BOLD, 15));
	close.setPreferredSize(new Dimension(20, 20));
	close.setVerticalTextPosition(JLabel.CENTER);
	close.setHorizontalTextPosition(JLabel.CENTER);
	close.setCursor(new Cursor(12));
	close.setToolTipText("关闭取消更新");
	JPanel headPan = new JPanel();
	headPan.setOpaque(false);
	headPan.setLayout(new BorderLayout(0, 0));
	headPan.add(title, BorderLayout.CENTER);
	headPan.add(close, BorderLayout.EAST);
	
    imageLabel.setIcon(imageIcon);
    this.getContentPane().setLayout(borderLayout1);
    progressBar.setBorderPainted(false);
   // progressBar.setBorderPainted(false);
    //progressBar.setUI(new BarUI());
    progressBar.setForeground(Color.BLACK);
    progressBar.setBorder(new EmptyBorder(0, 0, 0, 0));
    progressBar.setPreferredSize(new Dimension(250, 25));
    progressBar.setBackground(Color.RED);
    progressBar.setOpaque(false);
    wrapPanel.add(headPan, BorderLayout.NORTH);
    wrapPanel.setOpaque(false);
    URL url = new URL(Config.loadingImageSrc);
    URLConnection connection = url.openConnection();
    connection.setDoOutput(true);
    BufferedImage image = ImageIO.read(connection.getInputStream());  
    int srcWidth = image .getWidth();      // 源图宽度
    int srcHeight = image .getHeight();    // 源图高度

    imageIcon = new ImageIcon(image.getScaledInstance(300,300,Image.SCALE_DEFAULT));
    
    JLayeredPane layeredPane_1 = new JLayeredPane();
    setContentPane(layeredPane_1);
    
   // getContentPane().add(layeredPane_1, BorderLayout.CENTER);
    
    JLabel label = new JLabel("");
    label.setBounds(0, 0, 300, 300);
    //layeredPane_1.add(label,-1);
    wrapPanel.setBounds(0, 0, 300, 300);
    layeredPane_1.add(wrapPanel,1,1);
    
    JPanel panel = new JPanel();
    panel.setOpaque(false);
    wrapPanel.add(panel, BorderLayout.CENTER);
    panel.setLayout(new BorderLayout(0, 0));
    layeredPane_1.add(label,0,1);
    label.setPreferredSize(new Dimension(200, 150));
    label.setIcon(imageIcon);
    handle();
    panel.add(progressBar, BorderLayout.SOUTH);

    setSize(300, 300);

    //this.pack();
    
    
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
