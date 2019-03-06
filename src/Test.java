import utils.factories.ImageSplitterFactory;

/** This is demonstration class , that uses only in test cases!
 * If you want to test your images , then go to res/images directory and replace there your files=)*/
public final class Test {

    public static void main(String [] args) {
        /** Here I will split pikachu.png image from images package.
         * As this image has square shape , we should input similar counts of pieces , otherwise it will throw you the message in logger and stop execution! */
        // returns 9 pieces.
        new Thread(() -> ImageSplitterFactory.getImageSplitter().run("pikachu.png", 5, 5)).start();
        /** Here I will split coffee.png image from images package.
         * As this image has rectangular (with horizontal position) shape , we should input count of pieces by width more , than by height.
         * Otherwise it will throw the message in logger and stop execution!*/
        // returns 8 pieces.
        new Thread(() -> ImageSplitterFactory.getImageSplitter().run("coffee.png", 4, 1)).start();
    }

}
