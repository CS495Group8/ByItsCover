package com.example.byitscover.helpers;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import com.example.byitscover.MainActivity;

import java.io.File;
import java.nio.ByteBuffer;

import com.googlecode.tesseract.android.TessBaseAPI;


/**
 * This class produces a suggested search query based on an image of a book.
 *
 *
 * @author: Jack, Ripley
 * @version: 1.0
 */
public class ImageAnalyzer {
    private static final String TESSERACT_TRAINED_DATA_FOLDER = "tessdata";
    private static final String TESSERACT_PATH = "~/";

    private String suggestedSearchQuery;
    private TessBaseAPI ocr;

    // Constructor initializes the Tesseract OCR object and sets the relevant config values
     public ImageAnalyzer(){
      ocr = new TessBaseAPI();
      ocr.init(TESSERACT_PATH, "eng");
      ocr.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SPARSE_TEXT);
     }

 /**
  *
  * @param image The image to perform OCR on
  * @return result string from OCR execution on the image at the specified location
  */
 public Query analyze(Bitmap bitmapImage){
//     ByteBuffer buffer = image.getPlanes()[0].getBuffer();
//     byte[] bytes = new byte[buffer.capacity()];
//     buffer.get(bytes);
//     Bitmap bitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
     this.ocr.setImage(bitmapImage);
      try{
       String result = ocr.getUTF8Text();
      }
      catch(Exception e){
       e.printStackTrace();
      }

      return null;
     }

     //Releases all resources associated with current instance
     public void release(){

     }
}