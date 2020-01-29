package main;

public class DistanceBasedSearch {

	/**
	 * Computes the mean absolute error between two RGB pixels, channel by channel.
	 * @param patternPixel : a integer, the second RGB pixel.
	 * @param imagePixel : a integer, the first RGB pixel.
	 * @return a double, the value of the error for the RGB pixel pair. (an integer in [0, 255])
	 */
	public static double pixelAbsoluteError(int patternPixel, int imagePixel) {
//		//First extract the RGB values of each pixel, and store them in an array
//		int[] patternPixelComp = {ImageProcessing.getRed(patternPixel),
//				ImageProcessing.getBlue(patternPixel),
//				ImageProcessing.getGreen(patternPixel)};
//		int[] imagePixelComp = {ImageProcessing.getRed(imagePixel),
//				ImageProcessing.getBlue(imagePixel),
//				ImageProcessing.getGreen(imagePixel)};
//		
//		//Then compute the absolute error
//		double absErr = 0;
//		for(int i = 0; i<3; i++) {
//			absErr += Math.abs(patternPixelComp[i] - imagePixelComp[i]);
//		}
//		absErr /= 3;
		
		//The above implementation, although a little bit cleaner, is way slower (approx 2 times)[
		double absErr = (Math.abs(ImageProcessing.getRed(patternPixel)-ImageProcessing.getRed(imagePixel)))
				+(Math.abs(ImageProcessing.getGreen(patternPixel)-ImageProcessing.getGreen(imagePixel)))
				+(Math.abs(ImageProcessing.getBlue(patternPixel)-ImageProcessing.getBlue(imagePixel)));
		
		return absErr/3;
	}

	/**
	 * Computes the mean absolute error loss of a RGB pattern if positioned
	 * at the provided row, column-coordinates in a RGB image
	 * @param row : a integer, the row-coordinate of the upper left corner of the pattern in the image.
	 * @param column : a integer, the column-coordinate of the upper left corner of the pattern in the image.
	 * @param pattern : an 2D array of integers, the RGB pattern to find
	 * @param image : an 2D array of integers, the RGB image where to look for the pattern
	 * @return a double, the mean absolute error
	 * @return a double, mean absolute error value at position (row, col) between the pattern and the part of
	 * the base image that is covered by the pattern, if the pattern is shifted by x and y.
	 */
	public static double meanAbsoluteError(int row, int col, int[][] pattern, int[][] image) {
		//Requirement : pattern and image should both contain at least 1 pixel
		assert pattern != null;
		assert pattern.length > 0;
		assert pattern[0].length > 0;
		
		assert image != null;
		assert image.length > 0;
		assert image[0].length > 0;
		
		//Requirement : pattern fits entirely in the image given its position (row, col)
		assert pattern.length <= (image.length - row);
		assert pattern[0].length <= (image[0].length - col);
		
		int patternHeight = pattern.length;
		int patternWidth = pattern[0].length;
		
		//Sum the absolute error for each pixel of the pattern, and the corresponding pixel from the image
		double meanAbsErr = 0;
		for(int i = 0; i<patternHeight; i++) {
			for(int j = 0; j<patternWidth; j++) {
				meanAbsErr += pixelAbsoluteError(pattern[i][j], image[i+row][j+col]);
			}
		}
		
		//Divide by the total number of pixel of the pattern
		meanAbsErr /= patternHeight*patternWidth;
		return meanAbsErr; 
	}

	/**
	 * Compute the distanceMatrix between a RGB image and a RGB pattern
	 * @param pattern : an 2D array of integers, the RGB pattern to find
	 * @param image : an 2D array of integers, the RGB image where to look for the pattern
	 * @return a 2D array of doubles, containing for each pixel of a original RGB image, 
	 * the distance (meanAbsoluteError) between the image's window and the pattern
	 * placed over this pixel (upper-left corner) 
	 */
	public static double[][] distanceMatrix(int[][] pattern, int[][] image) {
		//Requirement : pattern and image should both contain at least 1 pixel
		assert pattern != null;
		assert pattern.length > 0;
		assert pattern[0].length > 0;
		
		assert image != null;
		assert image.length > 0;
		assert image[0].length > 0;
		
		//Requirement : pattern must entirely fit at least once into the image
		//	=> it has to be smaller or equal in size
		assert pattern.length <= image.length;
		assert pattern[0].length <= image[0].length;
		
		int patternHeight = pattern.length;
		int patternWidth = pattern[0].length;
		int imageHeight = image.length;
		int imageWidth = image[0].length;
		
		//The pattern must fit entirely in the image, we cannot move it further right than
		//the width of the image minus the width of the pattern itself (same logic applies to height)
		//and +1, because if image and pattern are the same size, we still have to calculate
		//the mean absolute error once.
		double[][] matrix = new double[imageHeight-patternHeight+1][imageWidth-patternWidth+1];
		
		for(int i = 0; i < imageHeight-patternHeight+1; i++) {
			for(int j = 0; j < imageWidth-patternWidth+1; j++) {
				matrix[i][j] += meanAbsoluteError(i, j, pattern, image);
			}
		}
		
		return matrix; 
	}
	
	/**
	 * BONUS
	 * distanceMatrix with wrapping and mirroring strategies
	 **/
	public static double[][] distanceMatrix(int[][] pattern, int[][] image, String strategy) {
		//Requirement : pattern and image should both contain at least 1 pixel
		assert pattern != null;
		assert pattern.length > 0;
		assert pattern[0].length > 0;
		
		assert image != null;
		assert image.length > 0;
		assert image[0].length > 0;
		
		//Notice that here, it is not required anymore that the pattern fits entirely in the image
		//Although it doesn't make much sense to search for a pattern bigger than the image itself
		//the program (and algorithm) would work.
		
		//Requirement : user must have chosen a strategy. Either "wrap" or "mirror"
		assert (strategy.equals("wrap") || strategy.equals("mirror"));
		
		int imageHeight = image.length;
		int imageWidth = image[0].length;
		
		//Here, we are going to move the pattern over the whole image,
		//including when the pattern goes partially beyond the limits of the image itself
		double[][] matrix = new double[imageHeight][imageWidth];
		
		if(strategy.equals("wrap")) {
			for(int i = 0; i < imageHeight; i++) {
				for(int j = 0; j < imageWidth; j++) {
					matrix[i][j] += meanAbsoluteErrorWrap(i, j, pattern, image); //see implementation just below
				}
			}
		}
		else { // strategy.equals("mirror")
			for(int i = 0; i < imageHeight; i++) {
				for(int j = 0; j < imageWidth; j++) {
					matrix[i][j] += meanAbsoluteErrorMirror(i, j, pattern, image);
				}
			}
		}
		
		return matrix; 
	}
	
	//This method is very similar to meanAbsoluteError, just notice the modulo operator when calling pixelAbsoluteError
	public static double meanAbsoluteErrorWrap(int row, int col, int[][] pattern, int[][] image) {
		//Requirement : pattern and image should both contain at least 1 pixel
		assert pattern != null;
		assert pattern.length > 0;
		assert pattern[0].length > 0;
		
		assert image != null;
		assert image.length > 0;
		assert image[0].length > 0;
		
		int patternHeight = pattern.length;
		int patternWidth = pattern[0].length;
		int imageHeight = image.length;
		int imageWidth = image[0].length;
		
		double meanAbsErr = 0;
		
		for(int i = 0; i<patternHeight; i++) {
			for(int j = 0; j<patternWidth; j++) {
				meanAbsErr += pixelAbsoluteError(pattern[i][j], image[(i+row)%imageHeight][(j+col)%imageWidth]); //This line is what effectively makes the wrapping
			}
		}
		
		//Divide by the total number of pixel of the pattern
		meanAbsErr /= patternHeight*patternWidth;
		return meanAbsErr; 
	}
	
	public static double meanAbsoluteErrorMirror(int row, int col, int[][] pattern, int[][] image) {
		//Requirement : pattern and image should both contain at least 1 pixel
		assert pattern != null;
		assert pattern.length > 0;
		assert pattern[0].length > 0;
		
		assert image != null;
		assert image.length > 0;
		assert image[0].length > 0;
		
		int patternHeight = pattern.length;
		int patternWidth = pattern[0].length;
		int imageHeight = image.length;
		int imageWidth = image[0].length;
		
		double meanAbsErr = 0;
		
		//Each case of exceeding the image boundaries is assessed, because the formula given
		// i' = n-2-(i%n) is valid only if i>=n
		//(when inside of the boundaries, we just access the pixels at their normal indexes,
		// using i+row or j+col)
		for(int i = 0; i < patternHeight; i++) {
			if(i+row < imageHeight) {
				//pattern fits vertically in the image
				for(int j = 0; j < patternWidth; j++){
					if(j+col < imageWidth) {
						//pattern fits horizontally in the image
						meanAbsErr += pixelAbsoluteError(pattern[i][j], image[i+row][j+col]);
					}
					else {
						//pattern exceeds image in width
						meanAbsErr += pixelAbsoluteError(pattern[i][j], image[i+row][imageWidth-2-(j+col)%imageWidth]);
					}
				}
			}
			else {
				//pattern exceeds image height
				for(int j = 0; j < patternWidth; j++) {
					if(j+col < imageWidth) {
						//pattern fits horizontally in the image
						meanAbsErr += pixelAbsoluteError(pattern[i][j], image[imageHeight-2-(i+row)%imageHeight][j+col]);
					}
					else {
						meanAbsErr += pixelAbsoluteError(pattern[i][j], image[imageHeight-2-(i+row)%imageHeight][imageWidth-2-(j+col)%imageWidth]);
					}
				}
			}
		}
		
		//Divide by the total number of pixel of the pattern
		meanAbsErr /= patternHeight*patternWidth;
		return meanAbsErr; 
	}
}
