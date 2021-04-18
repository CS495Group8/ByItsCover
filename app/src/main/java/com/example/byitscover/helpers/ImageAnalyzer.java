package com.example.byitscover.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * This class produces a suggested search query based on an image of a book.
 *
 * @author: Ripley
 * @version: 1.0
 */
public class ImageAnalyzer {

     public ImageAnalyzer(){
     }

 /**
  *
  * @param imgName The image file name to perform OCR on, should be in the .../files/images/ directory
  * @return result string from OCR execution on the image at the specified location
  */
 public Query analyze(String imgName) throws ExecutionException, InterruptedException, TimeoutException {
     Bitmap bitmap = BitmapFactory.decodeFile(imgName);
     InputImage image = InputImage.fromBitmap(bitmap, 0);
     TextRecognizer recognizer = TextRecognition.getClient();

     Task<Text> result = recognizer.process(image);
     String resultText = "";
     Tasks.await(result);
     resultText = result.getResult().getText();
     System.out.println("OCR FINISHED:" + '\n' + resultText);

     return new Query(resultText,"",null);
 }

     //Releases all resources associated with current instance
     public void release(){
     }
}
