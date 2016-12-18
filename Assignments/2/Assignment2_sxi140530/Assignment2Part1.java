import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import org.opencv.core.Point;

public class Assignment2Part1 {
	
	public static JSlider hueSliderlower = new JSlider();
	public static JSlider hueSliderupper = new JSlider();
	public static JSlider satSliderlower = new JSlider();
	public static JSlider satSliderupper = new JSlider();
	public static JSlider valSliderlower = new JSlider();
	public static JSlider valSliderupper = new JSlider();
	public static int minhueval = 0;
	public static int maxhueval = 0;
	public static int minsatval = 0;
	public static int maxsatval = 0;
	public static int minval = 0;
	public static int maxval = 0;
	
	
	static{ 
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
	}

	public static void convertHsv(Mat frame, String Filename, int count) throws IOException{
		
		Mat mat1 = new Mat(frame.height(), frame.width(), CvType.CV_8UC3);
		Imgproc.cvtColor(frame, mat1, Imgproc.COLOR_RGB2HSV);
		byte[] data1 = new byte[mat1.rows()*mat1.cols()*(int)(mat1.elemSize())];
         mat1.get(0, 0, data1);
         BufferedImage image1 = new BufferedImage(mat1.cols(), mat1.rows(), 5);
         image1.getRaster().setDataElements(0, 0, mat1.cols(), mat1.rows(), data1);
         Filename = Filename + count + ".jpg";
         File ouptut = new File(Filename);
         ImageIO.write(image1, "jpg", ouptut);
		
	}
	
	
	
	public static BufferedImage detect(BufferedImage image){
		
		Mat hsv = new Mat();
		Mat mask = new Mat();
		Mat img = bufferedImagetoMat(image);
		
		Imgproc.cvtColor(img, hsv, Imgproc.COLOR_BGR2HSV);
		
		hueSliderlower.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent event) {
		       minhueval = hueSliderlower.getValue();
		        
		      }
		    });
		
		hueSliderupper.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent event) {
		       maxhueval = hueSliderupper.getValue();
		        
		      }
		    });
		
		satSliderlower.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent event) {
		       minsatval = satSliderlower.getValue();
		        
		      }
		    });
		
		satSliderupper.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent event) {
		       maxsatval = satSliderupper.getValue();
		        
		      }
		    });
		
		valSliderlower.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent event) {
		       minval = satSliderlower.getValue();
		        
		      }
		    });
		
		valSliderupper.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent event) {
		       maxval = satSliderupper.getValue();
		        
		      }
		    });
		
		
		Scalar minVal = new Scalar(minhueval,minsatval,minval);
		
		Scalar maxVal = new Scalar(maxhueval,maxsatval,maxval);
		
						
		Core.inRange(hsv, minVal, maxVal, mask);
		
		
		ArrayList<Mat> al = new ArrayList<Mat>();
		
		Core.split(hsv, al);
		
		al.get(0).setTo(new Scalar(100), mask);
		
		Core.merge(al, hsv);
		
		
		Mat image1 =  new Mat();
		
		Imgproc.cvtColor(hsv, image1, Imgproc.COLOR_HSV2BGR);
		
		return mattoBufferedImage(image1);
	}
	
	
	
	public static void createTaskbar(){
		
		JFrame sliderframe = new JFrame("Slider");
		JPanel jp2 = new JPanel(new GridLayout(0,1));
		sliderframe.setSize(1000,2000); //give the frame some arbitrary size 
		sliderframe.setBackground(Color.BLUE);
		
		sliderframe.setVisible(true);
		
		JLabel sliderhuelower = new JLabel("Lower Hue level");
	    sliderhuelower.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    JLabel sliderhueupper = new JLabel("Upper Hue level");
	    sliderhueupper.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    JLabel slidersatlower = new JLabel("Lower Saturation level");
	    slidersatlower.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    JLabel slidersatupper = new JLabel("Upper Saturation level");
	    slidersatupper.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    JLabel slidervallower = new JLabel("Lower Val level");
	    slidervallower.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    JLabel slidervalupper = new JLabel("Upper Val level");
	    slidervalupper.setAlignmentX(Component.CENTER_ALIGNMENT);

	    int minhuelower = 0;
	    int maxhuelower = 180;
	    int inihuelower =0;
	    
	    int minhueupper = 0;
	    int maxhueupper = 180;
	    int inihueupper =40;
	    
	    int minsatlower = 0;
	    int maxsatlower = 255;
	    int inisatlower =100;
	    
	    int minsatupper = 0;
	    int maxsatupper = 255;
	    int inisatupper =240;
	    
	    int minvallower = 0;
	    int maxvallower = 255;
	    int inivallower =0;
	    
	    int minvalupper = 0;
	    int maxvalupper = 255;
	    int inivalupper =255;
	    
	    
	    hueSliderlower = new JSlider(JSlider.HORIZONTAL,
	            minhuelower, maxhuelower, inihuelower);

	    hueSliderlower.setMajorTickSpacing(10);
	    hueSliderlower.setMinorTickSpacing(2);
	    hueSliderlower.setPaintTicks(true);
	    hueSliderlower.setPaintLabels(true);
	    
	    jp2.add(sliderhuelower);
	    jp2.add(hueSliderlower);
	    
	    hueSliderupper = new JSlider(JSlider.HORIZONTAL,
	            minhueupper, maxhueupper, inihueupper);

	    hueSliderupper.setMajorTickSpacing(10);
	    hueSliderupper.setMinorTickSpacing(2);
	    hueSliderupper.setPaintTicks(true);
	    hueSliderupper.setPaintLabels(true);
	    
	    jp2.add(sliderhueupper);
	    jp2.add(hueSliderupper);
	    
	    satSliderlower = new JSlider(JSlider.HORIZONTAL,
	            minsatlower, maxsatlower, inisatlower);

	   satSliderlower.setMajorTickSpacing(10);
	    satSliderlower.setMinorTickSpacing(2);
	    satSliderlower.setPaintTicks(true);
	    satSliderlower.setPaintLabels(true);
	    
	    jp2.add(slidersatlower);
	    jp2.add(satSliderlower);

	    satSliderupper = new JSlider(JSlider.HORIZONTAL,
	            minsatupper, maxsatupper, inisatupper);

	    satSliderupper.setMajorTickSpacing(10);
	    satSliderupper.setMinorTickSpacing(2);
	    satSliderupper.setPaintTicks(true);
	    satSliderupper.setPaintLabels(true);
	    
	    jp2.add(slidersatupper);
	    jp2.add(satSliderupper);
	    
	    valSliderlower = new JSlider(JSlider.HORIZONTAL,
	            minvallower, maxvallower, inivallower);

	    valSliderlower.setMajorTickSpacing(10);
	    valSliderlower.setMinorTickSpacing(2);
	    valSliderlower.setPaintTicks(true);
	    valSliderlower.setPaintLabels(true);
	    
	    jp2.add(slidervallower);
	    jp2.add(valSliderlower);
	    
	    valSliderupper = new JSlider(JSlider.HORIZONTAL,
	            minvalupper, maxvalupper, inivalupper);

	    valSliderupper.setMajorTickSpacing(10);
	    valSliderupper.setMinorTickSpacing(2);
	    valSliderupper.setPaintTicks(true);
	    valSliderupper.setPaintLabels(true);
	    
	    jp2.add(slidervalupper);
	    jp2.add(valSliderupper);
	    
	    sliderframe.setContentPane(jp2);
	    
	}
	
	public static Mat bufferedImagetoMat(BufferedImage img){
		
		  Mat mat = new Mat(img.getHeight(), img.getWidth(), CvType.CV_8UC3);
		  byte[] data = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		  mat.put(0, 0, data);
		 
		  return mat;
	}
	
	public static BufferedImage mattoBufferedImage(Mat mat){

		BufferedImage  image = null;
		  MatOfByte mb=new MatOfByte();  
          Highgui.imencode(".jpg", mat, mb);  
          try {  
               image = ImageIO.read(new ByteArrayInputStream(mb.toArray()));  
          } catch (IOException e) {  
               e.printStackTrace();   
          }  
       return image; 
		
	}
	
	public static BufferedImage drawRect(BufferedImage img, int x,int y, int width, int height){
		
		Mat resultImage = bufferedImagetoMat(img); // source image to mat object logic
		
		Core.rectangle(resultImage, new Point(x,y), new Point(x+width,y+height), new Scalar(0,0,255));
		
		img = mattoBufferedImage(resultImage);
		return img;
	}
	
	public static void main(String[] args) throws Exception {  //the exception is for the sleep call

		  JFrame frameCamera = new JFrame("WebCam Capture"); 
		  JFrame blueimage = new JFrame("Orange to Blue Image");
	      frameCamera.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	      
	      FacePanel facePanel = new FacePanel();
	      FacePanel facePanel1 = new FacePanel();
	      frameCamera.setSize(400,400); //give the frame some arbitrary size 
	      frameCamera.setBackground(Color.BLUE);
	      frameCamera.add(facePanel,BorderLayout.CENTER);
	      frameCamera.setVisible(true);
	      blueimage.setSize(400, 400);
	      blueimage.setBackground(Color.BLUE);
	      blueimage.add(facePanel1,BorderLayout.CENTER);
	      blueimage.setVisible(true);
		
	      createTaskbar();
	      
	   
	           
	      
        Mat frame = new Mat();
        VideoCapture cap = new VideoCapture(0);
        
        int count = 0;
        String Filename = "hsv";
        
        if(cap.isOpened()){
        	
        	Thread.sleep(500);
        	while(true){
        		
        		cap.read(frame);
        		if(!frame.empty()){
        			Thread.sleep(200);
        			frameCamera.setSize(frame.width()+40,frame.height()+60);
        			//Display Original live video
        			facePanel.matToBufferedImage(frame);
        			facePanel.repaint();
        			count++;
        			//Save HSV Images
        			if(count%5 == 0){
        				convertHsv(frame, Filename, count);
        				Filename = "hsv";
        			}
        			
        			//Detect Orange
        			
        			BufferedImage img = detect(facePanel.getImage());
        			
        			blueimage.setSize(img.getWidth()+40,img.getHeight()+60);
        			//Display the Blue Image
        			facePanel1.setImage(img);
        			
        			facePanel1.repaint();
        			
        		}
        		else{
        			 System.out.println(" --(!) No captured frame from webcam !");   
                     break;  
        		}
        	}
        	
        }

        cap.release(); 
      }
}
