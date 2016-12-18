/*
 * Assignment No 3 - Video Analytics
 * Author - Srikant Iyengar
 * Net Id: sxi140530 

 */


import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Assignment3 {

	static{ 
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
	}
	
	public static double[][] readmatrix(String fileName) throws IOException{
		
		
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = br.readLine();
		String[] val = line.split(" ");
		double mat[][];
		if(val.length == 9){
			mat = new double[3][3];
		}
		else{
			mat = new double[4][4];
		}
		
		int k=0;
		for(int i=0;i<mat.length;i++){
			for(int j=0;j<mat[0].length;j++){
				mat[i][j] = new Double(val[k]);
				k++;
			}
		}
		br.close();
		return mat;
	}
	
	public static void print(double[][] mat){
		
		int  r = mat.length;
		int c = mat[0].length;
		
		for(int i=0;i<r;i++){
			for(int j=0;j<c;j++){
				System.out.print(mat[i][j] + " ");
			}
			System.out.println();
		}
		
		System.out.println(" ---------------------- ----------------------- ");
	}
	
	//Mat m1 is 1*3 matrix and Mat m2 is 3*3
	//Mat m1 is 1*4 matrix and Mat m2 is 4*4
	public static double[][] matricMulti(double[][] m1,double[][] m2){
		
		int rows = m1.length;
		int cols = m2[0].length;
		double[][] result = new double[rows][cols];
		
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				double temp = 0;
				for(int k=0;k<m1[0].length;k++){
					temp += m1[i][k] * m2[k][j];
				}
				result[i][j] = temp;
			}
		}
		
		return result;
	}
	
	public static double[][] scalarMul(int val, double[][] t){
		
		double[][] result = new double[t.length][t[0].length];
		for(int i=0;i<t.length;i++){
			for(int j=0;j<t[0].length;j++){
				result[i][j] = val*t[i][j];
			}
		}
		return result;
	}
	
	public static double[][] fourMat(double[][] t){
		
		double[][] result = new double[1][4];
		
		for(int i=0;i<t.length;i++){
			for(int j=0;j<t[0].length;j++){
				result[i][j] = t[i][j];
			}
		}
		result[0][3] = 1;
		return result;
	}
	
	public static double[][] divideMat(double[][] t){
		
		double[][] result = new double[1][3];
		
		for(int i=0; i<t.length ; i++){
			for(int j=0 ; j<t[0].length-1 ; j++){
				result[i][j] = t[i][j] / t[0][2];
			}
		}
		
		return result;
	}
	
	public static void displayImage(String filename) throws IOException
	 {   
		 BufferedImage img2 = ImageIO.read(new File(filename));
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
	

	public static Mat drawBoundary() throws IOException{
		
		double largest_area = 0;
		int largest_index = 0;
		Mat src = Highgui.imread("Depthcolor.png");
		Mat thr = new Mat(src.rows(),src.cols(),CvType.CV_8UC1);
		Imgproc.cvtColor(src, thr, Imgproc.COLOR_BGR2GRAY);
		Imgproc.threshold(thr, thr, 240 ,255 , Imgproc.THRESH_BINARY);
		
		ArrayList<MatOfPoint> contours = new ArrayList<>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(thr, contours, hierarchy, Imgproc.RETR_CCOMP , Imgproc.CHAIN_APPROX_SIMPLE);
		
		MatOfPoint2f         approxCurve = new MatOfPoint2f(); 
		for (int i=0; i<contours.size(); i++){
			double a = Imgproc.contourArea(contours.get(i));
		   if(a>largest_area){
			   largest_area = a;
			   largest_index = i;
		   }
		   
		}
		MatOfPoint2f contour2f = new MatOfPoint2f( contours.get(largest_index).toArray() );
		   double approxDistance = Imgproc.arcLength(contour2f, true)*0.02;
		   Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);
		   MatOfPoint points = new MatOfPoint( approxCurve.toArray() ); 
		   Rect rect = Imgproc.boundingRect(points);
		   Core.rectangle(src, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar( 0, 0, 255 ),4,8, 0);
		   
		   Point p1 = rect.tl();
		   Point p2 = rect.br();
		   
		   width(p1.x, p1.y, p2.x, p2.y);
		   return src;
	}
	
	public static void width(double x1,double y1,double x2,double y2) throws IOException{
		
		double[][] t1 = {{x1,y1,1}};
		double[][] t2 = {{x2,y2,1}};
		double[][] invintrinsic = readmatrix("InvIntrinsicDepth");
		Mat depth = Highgui.imread("Depth.png",-1);
		
		double[][] trans1 = matricMulti(t1, invintrinsic);
		double[][] trans2 = matricMulti(t2, invintrinsic);
		
		double val1[] = depth.get((int)x1,(int)y1);
		double val2[] = depth.get((int)x2,(int)y2);
		
		double[][] trans3 = scalarMul((int)val1[0], trans1);
		double[][] trans4 = scalarMul((int)val2[0], trans2);
				
		double width = trans4[0][0] - trans3[0][0];
		double height = trans4[0][1] - trans3[0][1];
		
		
		width  = width / 10;
		height = height / 10;
		
		 JFrame frame=new JFrame();
	     frame.setLayout(new FlowLayout());        
	     frame.setSize(250, 100);
	     String v = "Width : " + width + " cm";
	     String v2 = "Height : " + height + " cm";
	     JLabel lbl=new JLabel(v);
	     JLabel lb2=new JLabel(v2);
	     frame.add(lbl);
	     frame.add(lb2);
	     frame.setVisible(true);
	     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public static void main(String args[]) throws IOException {
		
		//reading IntrinsicRGB File and converting into matrix
		double[][] intrinsic = readmatrix("IntrinsicRGB");
		
		//reading InvIntrinsicDepth 
		double[][] invintrinsic = readmatrix("InvIntrinsicDepth");
		
		//reading TransformationD-C
		double[][] trans = readmatrix("TransformationD-C");
		
		//load the color image as mat
		Mat color = Highgui.imread("Color.png");
		
		//load the depth image as mat
		Mat depth = Highgui.imread("Depth.png",-1);
		
		//create a clone Mat object
		Mat clone = new Mat(depth.rows(), depth.cols(), color.type());
		double[] temp = {0,0,0};
		for(int i=0;i<clone.rows();i++){
			for(int j=0;j<clone.cols();j++){
				clone.put(i, j, temp);
			}
		}
		
		//iterate the depth image pixel by pixel
		for(int i=0;i<depth.rows();i++){
			for(int j=0;j<depth.cols();j++){
				
				double[][] t = {{j,i,1}};
				double[][] transform1 = matricMulti(t, invintrinsic);//Function to multiply the matrix
				
				double val[] = depth.get(i, j);
				if(val != null && val[0]>0){
					double[][] transform2 = scalarMul((int)val[0], transform1);//Multiply the depth value with matrix
					double[][] transform3 = fourMat(transform2);//create a 1*4 matrix
					double[][] transform4 = matricMulti(transform3, trans);//Matrix Multiplication
					double[][] transform5 = divideMat(transform4);//divide the 3rd coordinate value with 1st and 2nd
					double[][] transform6 = matricMulti(transform5, intrinsic);//Matrix multiplication
					int x = (int)(transform6[0][1]);
					int y = (int)(transform6[0][0]);
						
					if(x>0 && y>0 && color.get( x, y ) != null ){ 
						double[] colorval = color.get(x, y);//get corresponding color pixel value
						clone.put(i, j, colorval);//set the same on the new image
					}
				}	
			}
		}
		
		Highgui.imwrite("Depthcolor.png", clone);
		
		Mat newclone = drawBoundary();
	
		Highgui.imwrite("Depthcolor.png", newclone);
		
		displayImage("Depthcolor.png");
		
	}
	
	
}
