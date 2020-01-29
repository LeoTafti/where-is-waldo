package main;

public class SimilarityBasedSearch {

	/**
	 * Computes the mean value of a gray-scale image given as a 2D array 
	 * @param image : a 2D double array, the gray-scale Image
	 * @return a double value between 0 and 255 which is the mean value
	 */
	public static double mean(double[][] image) {
		//Requirement : image should contain at least 1 pixel
		assert image != null;
		assert image.length > 0;
		assert image[0].length > 0;
		
		double mean = 0;
		for(int i = 0; i < image.length; i++) {
			for(int j = 0; j < image[0].length; j++) {
				mean += image[i][j];
			}
		}
		mean /= (image.length*image[0].length);
		return mean; 
	}
	
	public static double windowMean(double[][] matrix, int row, int col, int width, int height) {
		//Requirement : matrix should contain at least 1 pixel
		assert matrix != null;
		assert matrix.length > 0;
		assert matrix[0].length > 0;
		
//		System.out.println("Entering windowmean : ");
		double[][] subMatrix = new double[height][width];
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				subMatrix[i][j] = matrix[row+i][col+j];
//				System.out.println("matrix[" + row + "+" + i + "][" + col + "+" + j + "] : " + matrix[row+i][col+j]);
			}
		}
//		System.out.println("subMatrix.length : " + subMatrix.length);
//		System.out.println("SubMatrix[0].length : " + subMatrix[0].length);
//		System.out.println();
//		for(int i = 0; i<height; i++) {
//			for(int j = 0; j<width; j++) {
//				System.out.print(subMatrix[i][j]);
//			}
//			System.out.println("");
//		}
//		System.out.println();
		return mean(subMatrix);
	}

	
	/**
	 * Computes the Normalized Cross Correlation of a gray-scale pattern if positioned
	 * at the provided row, column-coordinate in a gray-scale image
	 * @param row : a integer, the row-coordinate of the upper left corner of the pattern in the image.
	 * @param column : a integer, the column-coordinate of the upper left corner of the pattern in the image.
	 * @param pattern : an 2D array of doubles, the gray-scale pattern to find
	 * @param image : an 2D array of double, the gray-scale image where to look for the pattern
	 * @return a double, the Normalized Cross Correlation value at position (row, col) between the pattern and the part of
	 * the base image that is covered by the pattern, if the pattern is shifted by x and y.
	 * should return -1 if the denominator is 0
	 */
	public static double normalizedCrossCorrelation(int row, int col, double[][] pattern, double[][] image) {
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
		
		//num is the numerator of the normalized cross correlation formula
		//denom1 is the left part of the denominator, under the square root
		//denom2 is the right part of the denominator, under the square root
		double num = 0;
		double denom1 = 0;
		double denom2 = 0;
		
		double patternMean = mean(pattern);
//		System.out.println("patternMean : " + patternMean);
		double imageWindowMean = windowMean(image, row, col, pattern[0].length, pattern.length);
//		System.out.println("imageWindowMean at " + row + ", " + col + " : " + imageWindowMean);
		for(int i = 0; i < pattern.length; i++) {
			for(int j = 0; j < pattern[i].length; j++) {
//				System.out.println("I(r+i, c+j) : " + (image[row+i][col+j]));
//				System.out.println("W/ : " + imageWindowMean);
//				System.out.println("I(r+i, c+j) - W : " + (image[row+i][col+j] - imageWindowMean));
//				System.out.println("");
//				System.out.println("M(i, j) : " + pattern[i][j]);
//				System.out.println("M/ : " + patternMean);
//				System.out.println("M(i,j); - M : " + (pattern[i][j]-patternMean));
//				System.out.println("");
				num += (image[row+i][col+j] - imageWindowMean)*(pattern[i][j] - patternMean);
				denom1 += Math.pow(image[row+i][col+j] - imageWindowMean, 2);
				denom2 += Math.pow(pattern[i][j]-patternMean, 2);
			}
		}
//		System.out.println("denom1 : " + denom1);
//		System.out.println("denom2 : " + denom2);
		
		double normCross = 0;
//		System.out.println("round(denom1 * denom2) : " + Math.round(denom1*denom2));
		System.out.println();
		if(Math.round(denom1*denom2) == 0) { //Math.round() used to avoid problems due to denom1, denom2 being doubles
			return -1;
		}
		else {
			return (num / Math.sqrt(denom1*denom2));
		}
	}
	
	/**
	 * Compute the similarityMatrix between a gray-scale image and a gray-scale pattern
	 * @param pattern : an 2D array of doubles, the gray-scale pattern to find
	 * @param image : an 2D array of doubles, the gray-scale image where to look for the pattern
	 * @return a 2D array of doubles, containing for each pixel of a original gray-scale image, 
	 * the similarity (normalized cross-correlation) between the image's window and the pattern
	 * placed over this pixel (upper-left corner)
	 */
	public static double[][] similarityMatrix(double[][] pattern, double[][] image) {
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
				matrix[i][j] += normalizedCrossCorrelation(i, j, pattern, image);
			}
		}
		
		return matrix; 
	}

}
