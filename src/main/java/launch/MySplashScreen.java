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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.MalformedURLException;
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

public class MySplashScreen extends JDialog implements PropertyChangeListener  {

	private final JPanel contentPanel = new JPanel();
	private JProgressBar progressBar;
	private int positionX;
	private int positionY;
	private JLabel close;
	private JLabel titleLabel;
	private PropertyChangeSupport support;
	private Image cacheImage;
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

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		support.addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		support.removePropertyChangeListener(pcl);
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
		support = new PropertyChangeSupport(this);
	    try {
			   
		
		JPanel bgPane = new JPanel() {
	          @Override
	            public void paintComponent(final Graphics g) {
	        	  System.out.println("**paintComponent");
	        	  Image scaleImage = getBackgroundImage();
	                g.drawImage(scaleImage, 0, 0, null);
	            }

	            @Override
	            public Dimension getPreferredSize() {
	                Image scaleImage = MySplashScreen.this.getBackgroundImage();
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
		
		titleLabel = new JLabel(Config.splashTitle);
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
		//progressBar.setBackground(Color.BLUE);
		contentPanel.add(progressBar, BorderLayout.SOUTH);
		
		close.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dispose();
				support.firePropertyChange("close", false, true);
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

	private Image getBackgroundImage()  {
		if(cacheImage==null){
			try {
				URL url = new URL(Config.loadingImageSrc);
			    URLConnection connection = url.openConnection();
			    connection.setDoOutput(true);
			    BufferedImage image = ImageIO.read(connection.getInputStream());  

			    cacheImage = image.getScaledInstance(getWidth(),getHeight(),Image.SCALE_DEFAULT);

			} catch (Exception e) {

			}
		}

		return cacheImage;
		
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
	public JProgressBar getProgressBar() {
		return progressBar;
	}
	public JLabel getTitleLabel() {
		return titleLabel;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("backgroundImage")){
			try {
				if(evt.getNewValue()!=null && evt.getNewValue() instanceof URL){
					URL url = (URL) evt.getNewValue();
				    URLConnection connection = url.openConnection();
				    connection.setDoOutput(true);
				    BufferedImage image = ImageIO.read(connection.getInputStream());  

				    cacheImage = image.getScaledInstance(getWidth(),getHeight(),Image.SCALE_DEFAULT);
				    getContentPane().repaint();
				    
				}

			} catch (Exception e) {

			}
		}
	}
	
}
