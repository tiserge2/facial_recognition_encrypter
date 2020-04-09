package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Mat;
import org.opencv.face.FaceRecognizer;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import recognition.*;
import utils.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modeldb.Person;

/**
 * The controller for our application, where the application logic is
 * implemented. It handles the button for starting/stopping the camera and the
 * acquired video stream.
 *
 * @author <a href="mailto:luigi.derussis@polito.it">Luigi De Russis</a>
 * @author <a href="http://max-z.de">Maximilian Zuleger</a> (minor fixes)
 * @version 2.0 (2016-09-17)
 * @since 1.0 (2013-10-20)
 *
 */
public class RecognitionCameraController
{
	// the FXML start button
	@FXML
	private Button Start;
	// the FXML image view
	@FXML
	private ImageView currentFrame;
	//the FXML take picture button
	@FXML
	private Button startRecognitionButton;
	//the FXML label for number of faces
	@FXML
	private Label NumberOfFaces;
	@FXML
	private Button Cancel;
	@FXML
	private ProgressBar progressIndicator;
	
	// a timer for acquiring the video stream
	private ScheduledExecutorService timer;
	// the OpenCV object that realizes the video capture
	private VideoCapture capture = new VideoCapture();
	// a flag to change the button behavior
	private boolean cameraActive = false;
	// the id of the camera to be used
	private static int cameraId = 0;
	
	/**
	 * The action triggered by pushing the button on the GUI
	 *
	 * @param event
	 *            the push button event
	 */
	@FXML
	protected void startRecognition(ActionEvent event)
	{
		CascadeClassifier faceCascade = new CascadeClassifier();
	    faceCascade.load("xml/haarcascade_frontalface_alt.xml");
	    FaceRecognizer faceRecognizer = LBPHFaceRecognizer.create();
	    faceRecognizer.read("trainmodel/face_model.yml");
	    
//		information.setText("");
		
		if (!this.cameraActive)
		{
			if(this.timer!=null && !this.timer.isShutdown()) {
				try {
					this.timer.shutdown();
					this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
				} catch(InterruptedException e)
				{
					// log any exception
					System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
				}
			}
			// start the video capture
			this.capture.open(cameraId);
			boolean onlyOnce = true;

			// is the video stream available?
			if (this.capture.isOpened())
			{
				this.cameraActive = true;
				// grab a frame every 33 ms (30 frames/sec)
				Runnable frameGrabber = new Runnable() {
					
					@Override
					public void run()
					{	
						
						// effectively grab and process a single frame
						Mat frame = grabFrame();
						Recognition.detectAndDisplay(frame, faceCascade, faceRecognizer);
						// convert and show the frame
						if(StaticVariable.recognized) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									// TODO Auto-generated method stub
									closeRecognitionWindow();
								}
							});
						}
//						NumberOfFaces.setText(StaticVariable.message);
						Image imageToShow = Utils.mat2Image(frame);
						updateImageView(currentFrame, imageToShow);
					}
				};
				
				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
				
				// update the button content
				this.startRecognitionButton.setText("Stop Camera");
			}
			else
			{
				// log the error
				System.err.println("Impossible to open the camera connection...");
			}
		}
		else
		{
			// the camera is not active at this point
			this.cameraActive = false;
			// update again the button content
			this.startRecognitionButton.setText("Start Camera");
			
			// stop the timer
			this.stopAcquisition(0);
		}
	}
	/**
	 * Cancel the acition of taken picture
	 * @param event
	 */
	@FXML
	protected void cancelCamera(ActionEvent event) {
		this.stopAcquisition(0);
		Stage stage = (Stage) Cancel.getScene().getWindow();
	    // do what you have to do
	    stage.close();
	}
	
	/*
	 * The action triggered by pushing the take picture button 
	 * on the GUI
	 * 
	 */
	@FXML
	protected void startTakingPicture(ActionEvent event) throws InterruptedException {
//		System.out.println("Button Pressed");
//	    CascadeClassifier faceCascade = new CascadeClassifier();
//	    faceCascade.load("xml/haarcascade_frontalface_alt.xml");
//		
//	 // stop the timer
//		this.timer.shutdown();
//		this.timer.awaitTermination(35, TimeUnit.MILLISECONDS);
//		
//		if(this.cameraActive) {
//			//if the camera is active we take a shot
//			//and after we stop the video camera
//			// effectively grab and process a single frame
//			while(true) {
//				Mat frame = grabFrame();
//				CollectData.detectAndCollect(frame, faceCascade);
//				Image takenImage = Utils.mat2Image(frame);
//				//we need to save it in some place
//				Imgcodecs.imwrite("images/faces/taken.jpg", frame);
//				NumberOfFaces.setText(this.checkFaces("images/faces/taken.jpg") + " faces found");
//				if(CollectData.sample == 50) 
//					break;
//				Runnable setPicture = new Runnable() {
//					
//					@Override
//					public void run()
//					{
//						// effectively grab and process a single frame
//						Mat frame = grabFrame();
//						// convert and show the frame
//						updateImageView(currentFrame, takenImage);
//					}
//				};
//				this.timer = Executors.newSingleThreadScheduledExecutor();
//				this.timer.scheduleAtFixedRate(setPicture, 36, 35, TimeUnit.MILLISECONDS);	
//			}
//			
//			
//			
//			// the camera is not active at this point
////			this.cameraActive = false;
//			// update again the button content
////			this.Start.setText("Start Camera");
//						
//			// stop the timer
////			this.stopAcquisition(1);
//					
//		} else {
//			System.out.println("Camera is not active");
//		}
	}
	/**
	 * Get a frame from the opened video stream (if any)
	 *
	 * @return the {@link Mat} to show
	 */
	private Mat grabFrame()
	{
		// init everything
		Mat frame = new Mat();
		
		// check if the capture is open
		if (this.capture.isOpened())
		{
			try
			{
				// read the current frame
				this.capture.read(frame);
				
				// if the frame is not empty, process it
//				if (!frame.empty())
//				{
//					Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
//				}
				
			}
			catch (Exception e)
			{
				// log the error
				System.err.println("Exception during the image elaboration: " + e);
			}
		}
		
		return frame;
	}
	
	/**
	 * Check how many faces are there in the taken pictures
	 * @param pathToImage
	 * @return numberOfFaces
	 */
	
	public int checkFaces(String pathToImage) {
		FaceDetector detector = new FaceDetector();
		detector.detectFace(pathToImage);
		return detector.getNumberOfFace();
	}
	
	/**
	 * Stop the acquisition from the camera and release all the resources
	 * @param i
	 * will help me know if i have to add the camera picture
	 */
	private void stopAcquisition(int i)
	{
		
		Runnable setCameraPicture = new Runnable() {
			
			@Override
			public void run()
			{
				// effectively grab and process a single frame
				try {
					FileInputStream input = new FileInputStream("images/camera.png");
					Image image = new Image(input);
			        updateImageView(currentFrame, image);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//				File file = new File("../images/camera.png");
		        
			}
		};
		
		if (this.timer!=null && !this.timer.isShutdown())
		{
			try
			{
				// stop the timer
				this.timer.shutdown();
				this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e)
			{
				// log any exception
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}
		}
		
		if (this.capture.isOpened())
		{
			// release the camera
			this.capture.release();
			this.startRecognitionButton.setText("Start Camera");
			if(i == 0) {
				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(setCameraPicture, 0, 35, TimeUnit.MILLISECONDS);
			}
		}
	}
	
	/**
	 * Update the {@link ImageView} in the JavaFX main thread
	 * 
	 * @param view
	 *            the {@link ImageView} to update
	 * @param image
	 *            the {@link Image} to show
	 */
	private void updateImageView(ImageView view, Image image)
	{
		Utils.onFXThread(view.imageProperty(), image);
	}
	
	/**
	 * On application close, stop the acquisition from the camera
	 */
	protected void setClosed()
	{
		this.stopAcquisition(0);
	}
	
	public void closeRecognitionWindow() {
		this.stopAcquisition(0);
		Stage stage = (Stage)Cancel.getScene().getWindow();
	    // do what you have to do
	    stage.close();
	}
	
}

