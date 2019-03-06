package utils;

import constants.FileConstants;
import utils.loggers.PuzzleLogger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public final class ImageSplitter {

    private static final String class_name = "Image Splitter";
    //global vars
    private BufferedImage bufferedImage;
    private int absolute_width;
    private int absolute_height;
    private String fileName;

    public synchronized void run(String fileName , int countOfPiecesByWidth , int countOfPiecesByHeight) {
        final long start = System.nanoTime();
        try {
            this.fileName = fileName.substring(0 , fileName.length() - 4);
            /**read res file to buffer image*/
            bufferedImage = ImageIO.read(new File(FileConstants.IMAGES_DIR , fileName));
            /**get width and height of image*/
            absolute_width = bufferedImage.getWidth();
            absolute_height = bufferedImage.getHeight();
            if (countOfPiecesByHeight == 0 || countOfPiecesByWidth == 0) {
                PuzzleLogger.error(class_name, "Sorry , you should input count of pieces , in which you want to split!");
                throw new RuntimeException("Input format is not valid");
            }
            if (absolute_height == absolute_width) splitSquare(countOfPiecesByHeight * countOfPiecesByWidth);
            else {
                if (countOfPiecesByHeight != countOfPiecesByWidth)
                    splitRectangle(countOfPiecesByWidth, countOfPiecesByHeight);
                else {
                    PuzzleLogger.error(class_name, "Sorry , but I can't split this image!");
                    throw new RuntimeException("Image format is not valid!");
                }
            }
            PuzzleLogger.info(class_name, "Program has been executed without exceptions and errors!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            PuzzleLogger.info(class_name, "Has finished all processes!");
            PuzzleLogger.info(class_name, "Total execution time : " + TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - start) + " seconds!");
            notify();
        }
    }

    private void splitSquare(int countOfPieces) throws IOException {
        /**check square case*/
        if (Math.sqrt(countOfPieces) == Math.floor(Math.sqrt(countOfPieces))) {
            int repeat = 0;
             /**Attention! As you see here , their can be probability that it will return not accurate value , as that is double.*/
            final double step = (absolute_width / Math.sqrt(countOfPieces));
            for (int pieceId = 0; pieceId < countOfPieces;) {
                pieceId = algorithm(step , step , repeat , pieceId);
                //change repeat
                repeat++;
            }
        }
        else {
            PuzzleLogger.error(class_name , "Count of pieces should have integer result of square root!");
            throw new RuntimeException("Input format is not valid!");
        }
    }

    private void splitRectangle(int countOfPiecesByWidth , int countOfPiecesByHeight) throws IOException {
        /**Attention! As you see here , their can be probability that it will return not accurate value , as that is double.*/
        final double step_width = (double) absolute_width / countOfPiecesByWidth;
        final double step_height = (double) absolute_height / countOfPiecesByHeight;
        /**vertical rectangle case*/
        if (isVerticalRectangle()) {
            if (countOfPiecesByHeight > countOfPiecesByWidth) start(countOfPiecesByHeight , countOfPiecesByWidth , step_width , step_height);
            else {
                PuzzleLogger.error(class_name , "Vertical rectangular image should be splitted by height more often then by width!");
                throw new RuntimeException("Input format is not valid");
            }
        }
        /**horizontal rectangle case*/
        else {
            if (countOfPiecesByWidth > countOfPiecesByHeight) start(countOfPiecesByHeight , countOfPiecesByWidth , step_width , step_height);
            else {
                PuzzleLogger.error(class_name , "Horizontal rectangular image should be splitted by width more often then by height!");
                throw new RuntimeException("Input format is not valid");
            }
        }
    }

    private void start(int countOfPiecesByHeight , int countOfPiecesByWidth , final double step_width , final double step_height) throws IOException {
        int repeat = 0;
        for (int pieceId = 0; pieceId < countOfPiecesByHeight * countOfPiecesByWidth;) {
            pieceId = algorithm(step_width, step_height, repeat, pieceId);
            //change repeat
            repeat++;
        }
    }

    private int algorithm(final double step_width , final double step_height , int repeat , int pieceId) throws IOException {
        double x = step_width*repeat;
        double y = step_height*repeat;
        ImageIO.write(bufferedImage.getSubimage((int) x,(int) y,(int) step_width,(int) step_height), "png", new File(FileConstants.PIECES_DIR , fileName + FileConstants.PIECE_OF_IMAGE + pieceId++ + FileConstants.PNG_FORMAT));
        PuzzleLogger.info(class_name , "x : " + x);
        PuzzleLogger.info(class_name , "y : " + y);
        /**split moving by width (using X coordinate)*/
        pieceId = splitWidth(step_width*(repeat + 1) , step_height*repeat , step_width , step_height , pieceId);
        /**split moving by height (using Y coordinate)*/
        pieceId = splitHeight(step_width*repeat , step_height*(repeat + 1) , step_width , step_height , pieceId);
        //change repeat
        return pieceId;
    }

    private int splitWidth(double x , double y , final double step_width , final double step_height , int pieceId) throws IOException {
        /**Attention! As we can have the probability that step will not be accurate , we will change x to x + 1 here!*/
        while (x + 1 < absolute_width) {
            ImageIO.write(bufferedImage.getSubimage((int) x,(int) y,(int) step_width,(int) step_height), "png", new File(FileConstants.PIECES_DIR , fileName + FileConstants.PIECE_OF_IMAGE + pieceId++ + FileConstants.PNG_FORMAT));
            x = x + step_width;
            PuzzleLogger.info(class_name , "x : " + x);
        }
        return pieceId;
    }

    private int splitHeight(double x, double y, final double step_width , final double step_height, int pieceId) throws IOException {
        /**Attention! As we can have the probability that step will not be accurate , we will change y to y + 1 here!*/
        while (y + 1 < absolute_height) {
            ImageIO.write(bufferedImage.getSubimage((int) x,(int) y,(int) step_width,(int) step_height), "png", new File(FileConstants.PIECES_DIR , fileName + FileConstants.PIECE_OF_IMAGE + pieceId++ + FileConstants.PNG_FORMAT));
            y = y + step_height;
            PuzzleLogger.info(class_name , "y : " + y);
        }
        return pieceId;
    }

    private boolean isVerticalRectangle() {
        return absolute_height > absolute_width;
    }
}
