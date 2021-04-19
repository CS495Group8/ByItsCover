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
     Tasks.await(result);

     String resultText = "";

     for (Text.TextBlock block : result.getResult().getTextBlocks()) {
         String blockText = block.getText();
         for (Text.Line line : block.getLines()) {
             String lineText = line.getText();
             for (Text.Element element : line.getElements()) {
                 String elementText = element.getText(); //each word
                 if (isAllUpper(elementText) && !elementText.contains("BO") && !elementText.contains("FI")) {
                     resultText += elementText + " ";
                 }
             }
         }
     }

     System.out.println("OCR FINISHED:" + '\n' + result.getResult().getText());
     System.out.println("Query: " + resultText);
     resultText = resultText.substring(0, resultText.lastIndexOf("DEMICK")+6);
     System.out.println("Query post: " + resultText);


     return new Query(resultText,"",null);
 }

     //Releases all resources associated with current instance
     public void release(){
     }

     private static boolean isAllUpper(String s) {
         for(char c : s.toCharArray()) {
             if(Character.isLetter(c) && Character.isLowerCase(c)) {
                 return false;
             }
         }
         return true;
     }
}
