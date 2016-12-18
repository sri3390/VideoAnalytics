import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Part3 {

	public Part3() throws IOException{
		
		
		detectRGB("fruitimage.png",0);
		detectRGB("Brightness1.png",10);
		detectRGB("Brightness2.png",50);
		detectRGB("Brightness3.png",100);
		
		detectHSV("hsv1.png",0);
		detectHSV("hsv2.png",0);
		detectHSV("hsv3.png",40);
		detectHSV("hsv4.png",86);
		
		BufferedImage d4 = ImageIO.read(new File("fruitimagedetect.png"));
		BufferedImage d1 = ImageIO.read(new File("Brightness1detect.png"));
		BufferedImage d2 = ImageIO.read(new File("Brightness2detect.png"));
		BufferedImage d3 = ImageIO.read(new File("Brightness3detect.png"));
		BufferedImage d5 = ImageIO.read(new File("hsv1detect.png"));
		BufferedImage d6 = ImageIO.read(new File("hsv2detect.png"));
		BufferedImage d7 = ImageIO.read(new File("hsv3detect.png"));
		BufferedImage d8 = ImageIO.read(new File("hsv4detect.png"));
		displayImage(d4);
		displayImage(d1);
		displayImage(d2);
		displayImage(d3);
		displayImage(d5);
		displayImage(d6);
		displayImage(d7);
		displayImage(d8);
		
	}
	
	public void detectRGB(String path,int beta){
	
		try{
			
			BufferedImage image = ImageIO.read(new File(path));
			int h = image.getHeight();
			int w = image.getWidth();
			Color white = new Color(255,255,255);
			int rgbwhite = white.getRGB();
			Color black = new Color(0,0,0);
			int rgbblack = black.getRGB();
			
			//parsing the image pixel by pixel

			for(int i=0;i<h;i++){
				for(int j=0;j<w;j++){
					//RGB Part
					int pixelval = image.getRGB(j, i);
					int red = (pixelval >> 16) & 0xff;
					int green = (pixelval >> 8) & 0xff;
				    int blue = (pixelval) & 0xff;
				    
				    int threshold1 = 180 + beta < 255 ? 180 + beta : 180;
				    int threshold2 =  80 + beta < 255 ? 80 + beta : 80;
				    int threshold3 = 5 + beta < 255 ? 5 + beta : 5;	
				    
				    if(red > threshold1 && green > threshold2 &&  blue < threshold3){
				    	image.setRGB(j, i, rgbwhite);
				    }
				    else{
				    	image.setRGB(j, i, rgbblack);
				    }
				    
				}
			}
			
			String [] split = path.split(Pattern.quote("."));
			String path2 = split[0] + "detect." + "png";
			//save image
			//File outputfile1 = new File(path2+"test.png");
			// ImageIO.write(image1, "png", outputfile1);
			 File outputfile = new File(path2);
			 ImageIO.write(image, "png", outputfile);
			 
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		
	}
	
	public void detectHSV(String path,int beta){
		
		try{
			
			BufferedImage image = ImageIO.read(new File(path));
			int h = image.getHeight();
			int w = image.getWidth();
			Color white = new Color(255,255,255);
			int rgbwhite = white.getRGB();
			Color black = new Color(0,0,0);
			int rgbblack = black.getRGB();
			
			//parsing the image pixel by pixel
			for(int i=0;i<h;i++){
				for(int j=0;j<w;j++){
					//RGB Part
					int pixelval = image.getRGB(j, i);
					int red = (pixelval >> 16) & 0xff;
					int green = (pixelval >> 8) & 0xff;
				    int blue = (pixelval) & 0xff;
				    
				    int threshold1 = 240 - beta > 0 ? 240 - beta : 0;
				    int threshold2 =  10 - beta > 0 ? 10 - beta : 0;
				    int threshold3 = 180 - beta > 0 ? 180 - beta : 0;	
				    
				    if(green > threshold1 && red > threshold2 &&  blue > threshold3 ){
				    	image.setRGB(j, i, rgbwhite);
				    }
				    else{
				    	image.setRGB(j, i, rgbblack);
				    }
				    
				}
			}
			
			String [] split = path.split(Pattern.quote("."));
			String path2 = split[0] + "detect." + "png";
			//save image
			
			 File outputfile = new File(path2);
			 ImageIO.write(image, "png", outputfile);
			 
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		
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
	
	public static void main( String[] args ) throws IOException {
	    	  
	    	 new Part3(); 
	   }
}


