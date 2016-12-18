#include <stdio.h>
#include <stdlib.h>
#include <iostream>
#include <fstream>
#include <string>
#include "stasm_lib.h"
#include <iostream>
#include <cmath>
#include "opencv2/ml/ml.hpp"
#include <opencv2/core/core.hpp>
#include "opencv2/highgui/highgui.hpp"
#include "opencv2/imgproc/imgproc.hpp"
#include "opencv2/objdetect/objdetect.hpp"
#include "opencv2/contrib/contrib.hpp"

using namespace std;
using namespace cv;

float distanceBetween(float x1, float y1, float x2, float y2){

	float t1 = pow((x2-x1),2);
	float t2 = pow((y2-y1),2);
	float ans = sqrt(t1+t2);

	return ans;

}


float angleBetween(float x1, float y1, float x2, float y2)
{
    float len1 = sqrt(x1 * x1 + y1 * y1);
    float len2 = sqrt(x2 * x2 + y2 * y2);

    float dot = x1 * x2 + y1 * y2;

    float a = dot / (len1 * len2);

    if (a >= 1.0)
        return 0.0;
    else if (a <= -1.0)
        return 180;
    else
        return acos(a); // 0..PI
}

void returnPoints(const char* path, int row, float landmarks[][2 * stasm_NLANDMARKS] , float train[][13]){
 
	Mat img;
	img = imread(path, 0);
	if (!img.data)
	{
		printf("Cannot load %s\n", path);
		exit(1);
	}
	int foundface;
	//float landmarks[2 * stasm_NLANDMARKS]; // x,y coords
	if (!stasm_search_single(&foundface, landmarks[row],(char*)img.data, img.cols, img.rows, path, "../data"))
	{
		printf("Error in stasm_search_single: %s\n", stasm_lasterr());
		exit(1);
	}

	train[row][0] = distanceBetween(landmarks[row][59*2+1], landmarks[row][59*2] , landmarks[row][65*2+1] , landmarks[row][65*2]);
	train[row][1] = distanceBetween(landmarks[row][59*2+1], landmarks[row][59*2] , landmarks[row][30*2+1] , landmarks[row][30*2]);
	train[row][2] = distanceBetween(landmarks[row][40*2+1], landmarks[row][40*2] , landmarks[row][65*2+1] , landmarks[row][65*2]);
	train[row][3] = distanceBetween(landmarks[row][62*2+1], landmarks[row][62*2] , landmarks[row][74*2+1] , landmarks[row][74*2]);
	train[row][4] = distanceBetween(landmarks[row][56*2+1], landmarks[row][56*2] , landmarks[row][62*2+1] , landmarks[row][62*2]);
	train[row][5] = distanceBetween(landmarks[row][56*2+1], landmarks[row][56*2] , landmarks[row][74*2+1] , landmarks[row][74*2]);
	train[row][6] = distanceBetween(landmarks[row][21*2+1], landmarks[row][21*2] , landmarks[row][22*2+1] , landmarks[row][22*2]);
	train[row][7] = distanceBetween(landmarks[row][21*2+1], landmarks[row][21*2] , landmarks[row][30*2+1] , landmarks[row][30*2]);
	train[row][8] = distanceBetween(landmarks[row][40*2+1], landmarks[row][40*2] , landmarks[row][22*2+1] , landmarks[row][22*2]);
	train[row][9] = distanceBetween(landmarks[row][30*2+1], landmarks[row][30*2] , landmarks[row][17*2+1] , landmarks[row][17*2]);
	train[row][10] = distanceBetween(landmarks[row][40*2+1], landmarks[row][40*2] , landmarks[row][24*2+1] , landmarks[row][24*2]);
	float p1x = landmarks[row][21*2+1] - landmarks[row][17*2+1];
	float p1y = landmarks[row][21*2] - landmarks[row][17*2];
	float p2x = landmarks[row][18*2+1] - landmarks[row][17*2+1];
	float p2y = landmarks[row][18*2] - landmarks[row][17*2];
	train[row][11] = angleBetween(p1x,p1y,p2x,p2y);
	p1x = landmarks[row][22*2+1] - landmarks[row][24*2+1];
	p1y = landmarks[row][22*2] - landmarks[row][24*2];
	p2x = landmarks[row][24*2+1] - landmarks[row][25*2+1];
	p2y = landmarks[row][24*2] - landmarks[row][25*2];
	train[row][12] = angleBetween(p1x,p1y,p2x,p2y);
	
}


void readImages(float landmarks[][2 * stasm_NLANDMARKS] , float labels[], float train [][13]){

	char* path = "../data/ImageList.txt";
	string line;
	string prefix = "../data/";
	ifstream myfile (path);
	int i=0;

	if (myfile.is_open())
	{
		while ( getline (myfile,line) )
		{
			string imagepath = line.substr(0,line.find(";"));
			unsigned l = line.length() - 1;
			char label = line.at(l);
			imagepath = prefix + imagepath;
			const char* ipath;
			ipath = imagepath.c_str();
			cout << ipath << endl;
			returnPoints(ipath,i,landmarks,train);
			float la = (float) (label - '0');
			labels[i] = la;
			i++;
		}
		cout << " Totally Trained " << i << " Images " << endl;
		myfile.close();
  }

  else cout << "Unable to open file";

}

int main()
{
	const int n=45;

	float landmarks[n][2 * stasm_NLANDMARKS];
	float train[n][13];
	float labels[n];
	cout  << " Training Images Now ................" << endl;
	readImages(landmarks,labels, train);


	Mat trainData = Mat(n,13,DataType<float>::type, train);
	Mat trainLabels = Mat(n,1,DataType<float>::type,labels);

	CvSVMParams params;
	params.svm_type    = CvSVM::C_SVC;
    params.kernel_type = CvSVM::LINEAR;
    params.term_crit   = cvTermCriteria(CV_TERMCRIT_ITER, 100, 1e-6);

	CvSVM svm;
	//training
	svm.train(trainData, trainLabels, Mat(), Mat(), params);

	//video capture
	string filename = "../data/koushik.mp4";
	cv::VideoCapture cap(filename);

	//VideoCapture cap(0);

	if (!cap.isOpened())
    {
        cerr << "Capture Device ID " << 0 << "cannot be opened." << endl;
        return -1;
    }

	
	//cut image at this point by 640*490
	char* path2 = "../data/face.png";

	while(true)
    {
		Mat frame;
		cap >> frame;

        if (!cap.read(frame))             
            break;

		Mat gray;

		cvtColor(frame,gray,CV_BGR2GRAY);

		imwrite(path2,gray);

		//prediction (loop for video)

		float pred[1][2 * stasm_NLANDMARKS];
		float predtrain[1][13];
		returnPoints(path2,0,pred,predtrain);
		Mat predMat = Mat(1,13,DataType<float>::type,predtrain);

		float prediction = svm.predict(predMat);

		string box_text ;

		if(prediction == 0) box_text = format("Prediction = Angry");
		if(prediction == 1) box_text = format("Prediction = Happy");
		if(prediction == 2) box_text = format("Prediction = Sad");

		putText(frame, box_text, Point(150,550), FONT_HERSHEY_PLAIN, 2.0, CV_RGB(255,0,0), 2.0);

		cv::imshow("window", frame);

        char key = cvWaitKey(2);
        if (key == 27) // ESC
            break;
    }


	return 0;
}
