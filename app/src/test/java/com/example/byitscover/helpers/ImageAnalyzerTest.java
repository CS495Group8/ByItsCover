package com.example.byitscover.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.widget.ImageView;

import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;

public class ImageAnalyzerTest {

    @Test
    public void testAnalyze() throws InterruptedException, ExecutionException, TimeoutException {
        ImageAnalyzer testAnalyzer = new ImageAnalyzer();
        String picturePath = "C:/Users/Ripley Ryan/OneDrive/Documents/College Year Four/CS495/TesseractTest/image_test/gunslinger.jpg";
        // Bitmap img = BitmapFactory.decodeFile(picturePath);
        Query response = testAnalyzer.analyze(picturePath);
        System.out.println(response.getTitle());
        assertEquals(response.getTitle(), "Gunslinger");
    }
}
