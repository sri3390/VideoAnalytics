import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Part1 {

	public Part1() throws IOException{
		
		
		BufferedImage img1 = increaseBrightness(1,10);
		
		File outputfile = new File("Brightness1.png");
		ImageIO.write(img1, "png", outputfile);
		
		displayImage(img1);
		
		BufferedImage img2 = increaseBrightness(1,50);
		
		 File outputfile1 = new File("Brightness2.png");
		 ImageIO.write(img2, "png", outputfile1);
		 
		 displayImage(img2);
		 
		BufferedImage img3 = increaseBrightness(1,100);
		
		 File outputfile2 = new File("Brightness3.png");
		 ImageIO.write(img3, "png", outputfile2);
		 
		 displayImage(img3);
		
	}
	
	public BufferedImage increaseBrightness(int alpha, int beta) throws IOException{
		
		BufferedImage image = ImageIO.read(new File("fruitimage.png"));
		int h = image.getHeight();
		int w = image.getWidth();
		
		
		for(int i=0;i<h;i++){
			for(int j=0;j<w;j++){
				int pixelval = image.getRGB(j, i);
				int red = (pixelval >> 16) & 0xff;
				int green = (pixelval >> 8) & 0xff;
			    int blue = (pixelval) & 0xff;
			    
			    red = red + beta < 255 ? red + beta : 255;
			    green = green + beta < 255 ? green + beta : 255;
			    blue = blue + beta < 255 ? blue + beta : 255;
			    
			    Color c1 = new Color(red,green,blue);
			    
			    image.setRGB(j, i, c1.getRGB());
			}
		}
		
		return image;
		
	}
	
	public void displayImage(Image img2)
	 {   
	     //BufferedImage img=ImageIO.read(new File("/HelloOpenCV/lena.png"));
	     ImageIcon icon=new ImageIcon(img2);
	     JFrame frame=new JFrame();
	     frame.setLayout(new FlowLayout());        
	     frame.setSize(img2.getWidth(null)+50, img2.getHeight(null)+50);     
	     JLabel lbl=new JLabel();
	     lbl.setIcon(icon);
	     frame.add(lbl);
	     frame.setVisible(true);
	     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	 }
	
	public static void main(String args[]) throws IOException{
		new Part1();
	}
}
