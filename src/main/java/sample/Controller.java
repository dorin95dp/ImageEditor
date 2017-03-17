package sample;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class Controller {

    public static Image makePicturegrey(int opacity, Image image) {
        WritableImage resultImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = resultImage.getPixelWriter();

        for(int i = 0; i < resultImage.getWidth(); i++) {
            for(int j = 0; j < resultImage.getHeight(); j++) {
                int pixel = pixelReader.getArgb(i, j);

                int red = ((pixel >> 16) & 0xff);
                int green = ((pixel >> 8) & 0xff);
                int blue = (pixel & 0xff);

                int grayLevel = (int) (0.2162 * (double)red + 0.7152 * (double)green + 0.0722 * (double)blue) / 3;
                grayLevel = opacity - grayLevel; // Inverted the grayLevel value here.
                int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;

                pixelWriter.setArgb(i, j, -gray); // AMENDED TO -gray here.
            }
        }

        return resultImage;
    }

    public static Image invertImage(Image image) {
        WritableImage resultImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        PixelReader pixelReader = image.getPixelReader();
        PixelWriter pixelWriter = resultImage.getPixelWriter();
        for (int i = 0; i < resultImage.getWidth(); i++) {
            for (int j = 0; j < resultImage.getHeight(); j++) {
                int pixel = pixelReader.getArgb(i, j);
                int red = ((pixel >> 16) & 0xff);
                int green = ((pixel >> 8) & 0xff);
                int blue = (pixel & 0xff);
                pixelWriter.setArgb(i, j, (1 - red)*(1-green)*(1-blue));
            }
        }

        return resultImage;
    }

    public static int getIntFromColor(int Red, int Green, int Blue){
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }
}
