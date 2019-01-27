package launch;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.text.LayeredHighlighter.LayerPainter;

//https://stackoverflow.com/questions/15233526/image-with-shadow-and-rounded-edges-in-java-swing
public class TransparentRoundPanel {

    private JFrame frame;

    public TransparentRoundPanel() {
        initComponents();
    }

    public static void main(String[] args) {
    	System.setProperty("java.net.useSystemProxies", "true");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TransparentRoundPanel();
            }
        });
    }

    private void initComponents() {
        frame = new JFrame();
        //frame.setTitle("Example");
        frame.setUndecorated(true);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//app exited when frame closes
        //frame.setResizable(false);
        frame.setLayout(new GridBagLayout());
        frame.setBackground(new Color(50, 50, 50, 0));
        frame.setSize(600, 600);
        
        JComponent contentPane = new JPanel();
        contentPane.setOpaque(false);
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		frame.setContentPane(contentPane);
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setOpaque(false);
		contentPane.add(layeredPane, BorderLayout.CENTER);
		//layeredPane.setLayout(new BorderLayout(0, 0));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(0, 0, 0,0);
        //frame.add(new RoundedPanel(), gc);
        GridBagConstraints gbc_layeredPane = new GridBagConstraints();
        gbc_layeredPane.fill = GridBagConstraints.BOTH;
        gbc_layeredPane.gridx = 0;
        gbc_layeredPane.gridy = 1;
        RoundedPanel rp = new RoundedPanel();
        //rp.setSize(300,300);
        layeredPane.add(rp, new Integer(99),1);
        layeredPane.setPreferredSize(rp.getSize());
        //frame.add(rp);
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(0,0,40,40);
		layeredPane.add(lblNewLabel, new Integer(100),2);
		
		frame.setVisible(true);
		frame.setLocation(200, 200);
		/*
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(10, 10, 10,10);
        //frame.add(new RoundedPanel(), gc);
        JLayeredPane layeredPane = new JLayeredPane();
        GridBagConstraints gbc_layeredPane = new GridBagConstraints();
        gbc_layeredPane.fill = GridBagConstraints.BOTH;
        gbc_layeredPane.gridx = 0;
        gbc_layeredPane.gridy = 1;
        frame.setBounds(100, 100, 450, 300);
		frame.setSize(400,400);

        frame.getContentPane().add(layeredPane, gbc_layeredPane);
        JLabel jb = new JLabel("HELLO");
        jb.setBounds(0, 0, 60, 60);
        jb.setBackground(Color.green);
        layeredPane.add(jb, new Integer(100),2);
*/
        //pack frame (size components to preferred size)
       // frame.pack();
//		
		frame.pack();
		System.out.println(layeredPane.getHeight());
        frame.setVisible(true);//make frame visible
    }
}

class RoundedPanel extends JPanel {

    /**
     * Stroke size. it is recommended to set it to 1 for better view
     */
    protected int strokeSize = 1;
    /**
     * Color of shadow
     */
    protected Color shadowColor = Color.black;
    /**
     * Sets if it drops shadow
     */
    protected boolean shady = false;
    /**
     * Sets if it has an High Quality view
     */
    protected boolean highQuality = true;
    /**
     * Double values for Horizontal and Vertical radius of corner arcs
     */
    //protected Dimension arcs = new Dimension(0, 0);
    protected Dimension arcs = new Dimension(20, 20);//creates curved borders and panel
    /**
     * Distance between shadow border and opaque panel border
     */
    protected int shadowGap = 0;
    /**
     * The offset of shadow.
     */
    protected int shadowOffset = 0;
    /**
     * The transparency value of shadow. ( 0 - 255)
     */
    protected int shadowAlpha = 150;
    int width = 300, height = 300;
    BufferedImage image;
    BufferedImage roundedImage;

    public RoundedPanel() {
        super();
        setOpaque(false);
        try {
        	URL imgUrl = new File("/Users/alexwang/git/launch/src/main/java/launch/Angry_Bird_red.png").toURL();
            image = ImageIO.read(imgUrl);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        roundedImage = makeRoundedCorner(image, 20);//make image round

        width = roundedImage.getWidth();//set width and height of panel accordingly
       // width = roundedImage.getWidth()+arcs.width/2;//set width and height of panel accordingly
        height = roundedImage.getHeight();
       // height = roundedImage.getHeight()+arcs.height/2;
        setSize(width,height);
    }

    public static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = output.createGraphics();

        // This is what we want, but it only does hard-clipping, i.e. aliasing
        // g2.setClip(new RoundRectangle2D ...)

        // so instead fake soft-clipping by first drawing the desired clip shape
        // in fully opaque white with antialiasing enabled...
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));

        // ... then compositing the image on top,
        // using the white shape from above as alpha source
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);

        g2.dispose();

        return output;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color shadowColorA = new Color(shadowColor.getRed(),
                shadowColor.getGreen(), shadowColor.getBlue(), shadowAlpha);
        Graphics2D graphics = (Graphics2D) g;

        //Sets antialiasing if HQ.
        if (highQuality) {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }

        //Draws shadow borders if any.
        if (shady) {
            graphics.setColor(shadowColorA);
            graphics.fillRoundRect(
                    shadowOffset,// X position
                    shadowOffset,// Y position
                    width - strokeSize - shadowOffset, // width
                    height - strokeSize - shadowOffset, // height
                    arcs.width, arcs.height);// arc Dimension
        } else {
            shadowGap = 1;
        }

        //Draws the rounded opaque panel with borders.
        graphics.setColor(getBackground());
        graphics.fillRoundRect(0, 0, width - shadowGap,
                height - shadowGap, arcs.width, arcs.height);
        graphics.setColor(getForeground());
        graphics.setStroke(new BasicStroke(0));
        graphics.drawRoundRect(0, 0, width - shadowGap,
                height - shadowGap, arcs.width, arcs.height);

        graphics.drawImage(roundedImage, 0, 0, this);//draw the rounded image

        //Sets strokes to default, is better.
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }
}