package com.example.byitscover.helpers;



import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;

import com.chaquo.python.PyException;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.byitscover.MainActivity;

import java.io.File;
import java.nio.ByteBuffer;



/**
 * This class produces a suggested search query based on an image of a book.
 *
 *
 * @author: Jack, Ripley
 * @version: 1.0
 */
public class ImageAnalyzer {

    // Constructor initializes the Tesseract OCR object and sets the relevant config values
     public ImageAnalyzer(){
//      ocr = new TessBaseAPI();
//      ocr.init(TESSERACT_PATH, "eng");
//      ocr.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SPARSE_TEXT);

     }

 /**
  *
  * @param bitmapImage The image (in bitmap format) to perform OCR on
  * @return result string from OCR execution on the image at the specified location
  */
 public Query analyze(String ImagePath){
        Python py = Python.getInstance();
        PyObject module = py.getModule("Tesseract");
        String query = "";
        try{
            query = module.callAttr("Tesseract", ImagePath).toString();
        }
        catch (PyException e){
            e.printStackTrace();
        }
      Query q = new Query(query,"",null);

      return q;
     }

     //Releases all resources associated with current instance
     public void release(){
     }
}