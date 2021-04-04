import android.media.Image;

public class ImageAnalyzer {
    public ImageAnalyzer();

    // Produces a search query from an image
    public Query analyze(Image image);

    // Releases all resources associated with current instance
    public void release();
}