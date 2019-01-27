package launch;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class BarUI extends BasicProgressBarUI { 

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