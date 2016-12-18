import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class Part2 {

	public Part2() throws IOException{
		
		BufferedImage image = ImageIO.read(new File("fruitimage.png"));
		BufferedImage image2 = ImageIO.read(new File("Brightness1.png"));
		BufferedImage image3 = ImageIO.read(new File("Brightness2.png"));
		BufferedImage image4 = ImageIO.read(new File("Brightness3.png"));
		RGBtoHSV(image,1);
		RGBtoHSV(image2,2);
		RGBtoHSV(image3,3);
		RGBtoHSV(image4,4);
		BufferedImage d1 = ImageIO.read(new File("hsv1.png"));
		BufferedImage d2 = ImageIO.read(new File("hsv2.png"));
		BufferedImage d3 = ImageIO.read(new File("hsv3.png"));
		BufferedImage d4 = ImageIO.read(new File("hsv4.png"));
		displayImage(d1);
		displayImage(d2);
		displayImage(d3);
		displayImage(d4);
		
	}
	
	public void RGBtoHSV(BufferedImage image, int value) throws IOException{
		
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
		
		int h = image.getHeight();
		int w = image.getWidth();
		
		Mat mat1 = new Mat(image.getHeight(), image.getWidth(), CvType.CV_8UC3);
		
		for(int i=0;i<h;i++){
			for(int j=0;j<w;j++){
				int pixelval = image.getRGB(j, i);
				float red = (pixelval >> 16) & 0xff;
				float green = (pixelval >> 8) & 0xff;
			    float blue = (pixelval) & 0xff;
			    
			    red = red / 255;
			    green = green / 255;
			    blue = blue / 255;
			    
			    float cmax =  Math.max(red, Math.max(green, blue));
			    float cmin = Math.min(red, Math.min(green, blue));
			    
			    float delta = cmax - cmin;
			    
			    //hue calculation
			    float hue;
			    if(delta == 0){
			    	hue = 0;
			    }
			    else{
			    	if(cmax == red){
			    		
			    		float temp = (green-blue)/delta;
			    		hue = 60 * (temp);
			    	}
			    	else{
			    		if(cmax == green){
			    			hue = 60 * ((blue-red)/delta) + 120;
			    		}
			    		else{
			    			hue = 60 * ((red - green)/delta) + 240;
			    		}
			    	}
			    }
			    
			    //saturation calculation
			    
			    float sat;
			    if(cmax == 0){
			    	sat = 0;
			    }
			    else{
			    	sat  = delta / cmax;
			    }
			    
			    //Value Calculation
			    float val = cmax;
			    
			    val = 255 * val;
			    sat = sat * 255;
			    hue = hue / 2;
			    
			    double[] hsv = new double[3];
			    hsv[0] = hue;
			    hsv[1] = sat;
			    hsv[2] = val;
			    
			    mat1.put(i, j, hsv);
			}
		}
		
		
		byte[] data1 = new byte[mat1.rows()*mat1.cols()*(int)(mat1.elemSize())];
		mat1.get(0, 0, data1);
        BufferedImage image1 = new BufferedImage(mat1.cols(), mat1.rows(), 5);
        image1.getRaster().setDataElements(0, 0, mat1.cols(), mat1.rows(), data1);
        
        String path2 = "hsv" + value + ".png";
		File outputfile2 = new File(path2);
		ImageIO.write(image1, "png", outputfile2);
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
		new Part2();
	}
}
