package com.example.byitscover.helpers;

/*import com.chaquo.python.PyException;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.byitscover.MainActivity;*/

import java.io.File;
import java.nio.ByteBuffer;

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
 public Query analyze(String imgName){
        Python py = Python.getInstance();
        PyObject module = py.getModule("Tesseract");
        String imgPath = "/data/data/com.example.byitscover/files/images/SearchBook.jpg"; //changed image name to match the one used in the camera
        String query = "";
        try{
            query = module.callAttr("Tesseract", imgPath).toString();
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