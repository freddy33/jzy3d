/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.jzy3d.demos.multiview3;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.CanvasSwing;
import org.jzy3d.plot3d.rendering.canvas.Quality;
import org.jzy3d.plot3d.rendering.scene.Scene;
import org.jzy3d.plot3d.rendering.view.modes.CameraMode;
import org.jzy3d.plot3d.rendering.view.modes.ViewPositionMode;

public class SplitPaneDividerDemo extends JPanel
        implements ActionListener {
    
    private JSplitPane splitPane;
    
    CanvasSwing c1, c2;
    JPanel toolBar;
    
    public SplitPaneDividerDemo() {
        super(new BorderLayout());
        
        Font font = new Font("Serif", Font.ITALIC, 24);
        
        JPanel panel1 = new JPanel(new BorderLayout());
        JPanel panel2 = new JPanel(new BorderLayout());
        
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                panel1, panel2);
        splitPane.setResizeWeight(0.5);
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);
        
        add(splitPane, BorderLayout.CENTER);
        toolBar = createControlPanel();
        add(toolBar, BorderLayout.PAGE_END);
        
        // Create a scene
        Scene scene = new Scene(true);
        
        
        // First window, will hold the mouse source
        c1 = new CanvasSwing(scene, Quality.Advanced);
        c1.getView().setBackgroundColor(Color.WHITE);
        c2 = new CanvasSwing(scene, Quality.Intermediate);
        scene.newView(c2, Quality.Advanced);
        c2.getView().setBackgroundColor(Color.WHITE);
        c2.getView().setViewPositionMode(ViewPositionMode.TOP);
        
        panel1.add(c1);
        panel2.add(c2);
        
        Mapper mapper = new Mapper(){
            public double f(double x, double y) {
                return 10*Math.sin(x/10)*Math.cos(y/20)*x;
            }
        };
        Range range = new Range(-150,150);
        int steps   = 50;
        scene.add( getSurface(mapper, range, steps) );
        
    }
    
    public static Shape getSurface(Mapper mapper, Range range, int steps){
        // Create the object to represent the function over the given range.
        final Shape surface = (Shape)Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps), mapper);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), surface.getBounds().getZmax(), new Color(1,1,1,.5f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(true);
        surface.setWireframeColor(Color.BLACK);
        
        return surface;
    }
    
    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        JLabel lbl = new JLabel("Some text I want in the screenshot");
        panel.add(lbl);
        JButton reset = new JButton("Click for screenshot");
        reset.addActionListener(this);
        panel.add(reset);
        panel.setBackground(java.awt.Color.WHITE);
        return panel;
    }
    
    //Required by ActionListener interface. Respond to reset button.
    public void actionPerformed(ActionEvent e) {
        try{
            int rows = 2;
            int cols = 1;
            int chunks = rows*cols;
            
            int width, height;
            int type;
            BufferedImage[] buffImages = new BufferedImage[3];
            buffImages[0] = c1.screenshot();
            buffImages[1] = c2.screenshot();
            type = buffImages[0].getType();
            buffImages[2] = new BufferedImage(toolBar.getWidth(), toolBar.getHeight(), type);
            
            Graphics2D g = buffImages[2].createGraphics();
            toolBar.paint(g);
            g.dispose();
            
            width = buffImages[0].getWidth();
            height = buffImages[0].getHeight() + buffImages[1].getHeight() + buffImages[2].getHeight();
            BufferedImage finalImage = new BufferedImage(width, height, type);
            Graphics2D g2 = finalImage.createGraphics();
            g2.drawImage(buffImages[0], 0, 0, null);
            g2.drawImage(buffImages[1], 0, buffImages[0].getHeight(), null);
            g2.drawImage(buffImages[2], 0, buffImages[0].getHeight() + buffImages[1].getHeight(), null);
            ImageIO.write(finalImage, "jpg", new File("a.jpg"));
            
        } catch (Exception ex){
            
        }
    }
    
    /** Returns an ImageIcon, or null if the path was invalid. */
    private static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = SplitPaneDividerDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("SplitPaneDividerDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Create and set up the content pane.
        SplitPaneDividerDemo newContentPane = new SplitPaneDividerDemo();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

