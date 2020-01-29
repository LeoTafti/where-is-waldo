package main;

import java.util.ArrayList;

public class Collector {

	/**
	 * Find the row, column coordinates of the best element (biggest or smallest) for the given matrix
	 * @param matrix : an 2D array of doubles
	 * @param smallestFirst : a boolean, indicates if the smallest element is the best or not (biggest is then the best)
	 * @return an array of two integer coordinates, row first and then column
	 */
	public static int[] findBest(double[][] matrix, boolean smallestFirst) {
		//By default, the best element is the first one (before running the algorithm)
		//Note that it can be done without declaring a variable best, and just keeping track of
		//its coordinates (we can then access its value by writing matrix[coordsOfBest[0]][coordsOfBest[1]]
		//However, it is more readable this way
		
		double best = matrix[0][0];
		int[] coordsOfBest = {0, 0};
		
		//This structure of the code makes it shorter, but it also means checking at
		//each iteration the value of smallestFirst
		//The other solution would be to write to first check smallesFirst value, then
		//write two different for loops, one for the minimum, one for the maximum
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[i].length; j++) {
				if(smallestFirst) {
					if(matrix[i][j] < best) {
						best = matrix[i][j];
						coordsOfBest[0] = i;
						coordsOfBest[1] = j;
					}
				}
				else {
					if(matrix[i][j] > best) {
						best = matrix[i][j];
						coordsOfBest[0] = i;
						coordsOfBest[1] = j;
					}
				}
			}
		}
		return coordsOfBest;
	}

	
	/**
	 * Find the row, column coordinate-pairs of the n best (biggest or smallest) elements of the given matrix
	 * @param n : an integer, the number of best elements we want to find 
	 * @param matrix : an 2D array of doubles
	 * @param smallestFirst : a boolean,  indicates if the smallest element is the best or not (biggest is the best)
	 * @return an array of size n containing row, column-coordinate pairs
	 */
	public static int[][] findNBest(int n, double[][] matrix, boolean smallestFirst) {
		//First make a copy of matrix, to avoid modifying it
		//Because each row has the same length, we can do it this way
		//See commented code block below for a more universal way of copying a given 2d array
		double[][] matrixCopy = new double[matrix.length][matrix[0].length];
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix[0].length; j++) {
				matrixCopy[i][j] = matrix[i][j];
			}
		}
		
//		double[][] matrixCopy = new double[matrix.length][];
//		for(int i = 0; i < matrix.length; i++) {
//			matrixCopy[i] = matrix[i].clone();
//		}
		
		double best = 0;
		int[][] coordsOfNBest = new int[n][2];
		int found = 0; //number of found best values
		
		while (found < n) {
			int[] nextBestCoords = findBest(matrixCopy, smallestFirst);
			coordsOfNBest[found] = nextBestCoords;
			if(smallestFirst) {
				matrixCopy[nextBestCoords[0]][nextBestCoords[1]] = Double.POSITIVE_INFINITY;
			}
			else {
				matrixCopy[nextBestCoords[0]][nextBestCoords[1]] = Double.NEGATIVE_INFINITY;
			}
			found++;
		}
		return coordsOfNBest;
	}
	
	

	/**
	 * BONUS 
	 * Notice : Bonus points are underpriced ! 
	 * 
	 * Sorts all the row, column coordinates based on their pixel value
	 * Hint : Use recursion !
	 * @param matrix : an 2D array of doubles
	 * @return A list of points, each point is an array of length 2.
	 */
	public static ArrayList<int[]> quicksortPixelCoordinates(double[][] matrix) {

		// TODO implement me correctly for "underpriced" bonus!
		return new ArrayList<int[]>();
	}

	
	/**
	 * BONUS
	 * Notice : Bonus points are underpriced !
	 * 
	 * Use a quick sort to find the row, column coordinate-pairs of the n best (biggest or smallest) elements of the given matrix
	 * Hint : return the n first or n last elements of a sorted ArrayList  
	 * @param n : an integer, the number of best elements we want to find 
	 * @param matrix : an 2D array of doubles
	 * @param smallestFirst : a boolean, indicate if the smallest element is the best or not (biggest is the best)
	 * @return an array of size n containing row, column-coordinate pairs
	 */
	public static int[][] findNBestQuickSort(int n, double[][] matrix, boolean smallestFirst) {

    	// TODO implement me correctly for underpriced bonus!
		return new int[][]{};
	}
}
