package launch;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLConnection;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class MySplashScreen extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JProgressBar progressBar;
	private int positionX;
	private int positionY;
	private JLabel close;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			MySplashScreen dialog = new MySplashScreen();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public MySplashScreen() {
		setAlwaysOnTop(true);
		setResizable(false);
		setUndecorated(true);
		setBounds(100, 100, 450, 300);
		setTitle(Config.appName);
		
	    try {
			   URL url = new URL(Config.loadingImageSrc);
			    URLConnection connection = url.openConnection();
			    connection.setDoOutput(true);
			    BufferedImage image = ImageIO.read(connection.getInputStream());  
			    int srcWidth = image .getWidth();      // 源图宽度
			    int srcHeight = image .getHeight();    // 源图高度

			    Image scaleImage = image.getScaledInstance(getWidth(),getHeight(),Image.SCALE_DEFAULT);
				ImageIcon imageIcon = new ImageIcon(scaleImage);
			    setIconImage(image.getScaledInstance(16,16,Image.SCALE_DEFAULT));
		
		JPanel bgPane = new JPanel() {
	          @Override
	            public void paintComponent(final Graphics g) {
	                g.drawImage(scaleImage, 0, 0, null);
	            }

	            @Override
	            public Dimension getPreferredSize() {
	                return new Dimension(
	                		scaleImage.getWidth(this), scaleImage.getHeight(this));
	            }

		};
		setContentPane(bgPane);
		
		}catch(Exception e) {
			e.printStackTrace();
		}

	    
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setOpaque(false);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel topPanel = new JPanel();
		topPanel.setOpaque(false);
		topPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPanel.add(topPanel, BorderLayout.NORTH);
		topPanel.setLayout(new BorderLayout(0, 0));
		
		JLabel titleLabel = new JLabel(Config.splashTitle);
		topPanel.add(titleLabel, BorderLayout.CENTER);
		

		titleLabel.addMouseMotionListener(
				new java.awt.event.MouseMotionAdapter() {
					public void mouseDragged(MouseEvent e) {
						this_mouseDragged(e);
					}

				});
		
		titleLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				this_mousePressed(e);

			}

			public void mousePressed(MouseEvent e) {
				this_mousePressed(e);
			}
		});
		
		close = new JLabel("x");
		close.setToolTipText("关闭取消更新");

		topPanel.add(close, BorderLayout.EAST);
		
		progressBar = new JProgressBar();
		progressBar.setBorderPainted(false);
		contentPanel.add(progressBar, BorderLayout.SOUTH);
		
		close.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
			public void mouseEntered(MouseEvent e) {
				close.setForeground(Color.red);
				//close.setBorder(BorderFactory.createLineBorder(Color.gray));
			}
			public void mouseExited(MouseEvent e) {
				//close.setBorder(null);
				close.setForeground(Color.BLACK);
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

	
	public void setProgress(String message, int progress)
	  {
	    final int theProgress = progress;
	    final String theMessage = message;
	    
	    SwingUtilities.invokeLater(new Runnable() {
	      public void run() {
	    	progressBar.setStringPainted(theMessage!=null);
	        progressBar.setValue(theProgress);
	        progressBar.setString(theMessage);
	      }
	    });
	  }

	public void setProgressMax(int maxProgress) {
		progressBar.setMaximum(maxProgress);
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
	public JLabel getClose() {
		return close;
	}
}
