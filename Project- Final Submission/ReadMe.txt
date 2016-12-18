
Name: Srikant Iyengar
Net ID: sxi140530
Project: Emotion Recognition

************************************************************************************************************************
Contents of Folder:

1) Documentation: Detailed description of the approach used to perform emotion recognition.
2) Project folder: Contains all the files needed to run the stasm algorithm and the emotion recognition application.
3) ReadMe file: Current text file.
4) csvcreator.py - python script to create text file for trainig.

*************************************************************************************************************************
Process to execute the application:

1) Copy the folder to the local directory of your system.
2) open the project titleed minimum within the VC10 folder of the project.
3) Link your opencv installation with the given project.
4) execute the minimal.exe file generated after building the project.

*************************************************************************************************************************
References used and contribution to the project.

1) To detect fetures I used the stasm library to achieve the task.
2) Once the landmarks are obtaiined I perform the computations as described in the Documentation file.
3) I also created the svm model used to predict the emotion in the video frame.
4) I used the cohn-kanade dataset to train the svm model.

*************************************************************************************************************************