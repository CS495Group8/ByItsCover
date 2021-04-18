package com.example.byitscover.helpers;

import com.chaquo.python.PyException;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

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

        String query = "";
        try{
            query = module.callAttr("Tesseract", imgName).toString();
        }
        catch (PyException e){
            e.printStackTrace();
        }
        return new Query(query,"",null);
     }

     //Releases all resources associated with current instance
     public void release(){
     }
}
