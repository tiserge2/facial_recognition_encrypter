package recognition;


import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class FaceDetector {
	int numberOfFace;
	
	public int getNumberOfFace() {
		return numberOfFace;
	}

	public void setNumberOfFace(int numberOfFace) {
		this.numberOfFace = numberOfFace;
	}

	public FaceDetector() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public void detectFace(String fullPathToFile) {
		//we load the image to detect faces in
//		String imgFile = "images/sergio9.jpg";
		Mat src = Imgcodecs.imread(fullPathToFile);
		
		//then we load the face classifier
		String xmlFile = "xml/haarcascade_frontalface_alt.xml";
		CascadeClassifier cc = new CascadeClassifier(xmlFile);
		
		//then we applied the classifier on the picture
		MatOfRect faceDetection = new MatOfRect();
		cc.detectMultiScale(src, faceDetection);
		System.out.println(String.format("Detected face: %d",  faceDetection.toArray().length));
		
		//then we put a square all the way arround the detected faces
		for(Rect rect: faceDetection.toArray()) {
			Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(125,25,125), 3);
		}
		
		//then we write the file to the system
		Imgcodecs.imwrite("images/taken_circled.jpg ", src);
		
		//then we set the number of faces that we have detected
		this.setNumberOfFace(faceDetection.toArray().length);
	}
	
	public static void main(String argv[]) {
		FaceDetector detector = new FaceDetector();
		detector.detectFace("images/faces/taken.jpg");
	}
}

