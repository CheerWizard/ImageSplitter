package utils.factories;

import utils.ImageSplitter;

public final class ImageSplitterFactory {

    private static ImageSplitter imageSplitter;

    public synchronized static ImageSplitter getImageSplitter() {
        if (imageSplitter == null) imageSplitter = new ImageSplitter();
        return imageSplitter;
    }

}
