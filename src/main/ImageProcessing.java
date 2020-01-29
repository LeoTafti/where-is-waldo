package main;
public final class ImageProcessing {
	
    /**
     * Returns red component from given packed color.
     * @param rgb : a 32-bits RGB color
     * @return an integer,  between 0 and 255
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB(int, int, int)
     */
    public static int getRed(int rgb) {
    		return ((rgb >> 16) & 0xFF); 
    }

    /**
     * Returns green component from given packed color.
     * @param rgb : a 32-bits RGB color
     * @return an integer between 0 and 255
     * @see #getRed
     * @see #getBlue
     * @see #getRGB(int, int, int)
     */
    public static int getGreen(int rgb) {
    		return ((rgb >> 8) & 0xFF);
    }

    /**
     * Returns blue component from given packed color.
     * @param rgb : a 32-bits RGB color
     * @return an integer between 0 and 255
     * @see #getRed
     * @see #getGreen
     * @see #getRGB(int, int, int)
     */
    public static int getBlue(int rgb) {
    		return (rgb & 0xFF);
    }

   
    /**
     * Returns the average of red, green and blue components from given packed color.
     * @param rgb : 32-bits RGB color
     * @return a double between 0 and 255
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     * @see #getRGB(int)
     */
    public static double getGray(int rgb) {
    		int red = getRed(rgb);
		int green = getGreen(rgb);
		int blue = getBlue(rgb);
		
		return ((red + green + blue)/3.0);
    }

    /**
     * Returns packed RGB components from given red, green and blue components.
     * @param red : an integer 
     * @param green : an integer 
     * @param blue : an integer
     * @return a 32-bits RGB color
     * @see #getRed
     * @see #getGreen
     * @see #getBlue
     */
    public static int getRGB(int red, int green, int blue) {
	    	red = sanitizeValue(red);
	    	green = sanitizeValue(green);
	    	blue = sanitizeValue(blue);
	    	int rgb = (red << 16)|(green << 8)|(blue);
	    	return rgb;
    }

    /**
     * Returns packed RGB components from given gray-scale value.
     * @param gray : a double 
     * @return a 32-bits RGB color
     * @see #getGray
     */
    public static int getRGB(double gray) {
	    int grayInt = (int) Math.round(gray);
	    int rgb = getRGB(grayInt, grayInt, grayInt);
	    return rgb;
    }

    /**
     * Converts packed RGB image to gray-scale image.
     * @param image : a HxW integer array
     * @return a HxW double array
     * @see #encode
     * @see #getGray
     */
    public static double[][] toGray(int[][] image) {
    		//Requirement : image should contain at least 1 pixel
		assert image != null;
		assert image.length > 0;
		assert image[0].length > 0;
		
    		double[][] grayImage = new double[image.length][image[0].length];
		for(int i = 0; i < image.length; i++) {
			for(int j = 0; j < image[i].length; j++) {
				grayImage[i][j] = getGray(image[i][j]);
			}
		}
		return grayImage;
    }

    /**
     * Converts gray-scale image to packed RGB image.
     * @param channels : a HxW double array
     * @return a HxW integer array
     * @see #decode
     * @see #getRGB(double)
     */
    public static int[][] toRGB(double[][] gray) {
    		//Requirement : image should contain at least 1 pixel
		assert gray != null;
		assert gray.length > 0;
		assert gray[0].length > 0;
		
		int[][] rgbImage = new int[gray.length][gray[0].length];
		for(int i = 0; i < gray.length; i++) {
			for(int j = 0; j < gray[i].length; j++) {
				rgbImage[i][j] = getRGB(gray[i][j]);
			}
		}
		return rgbImage;
    }

    
    /**
     * Convert an arbitrary 2D double matrix into a 2D integer matrix 
     * which can be used as RGB image
     * @param matrix : the arbitrary 2D double array to convert into integer
     * @param min : a double, the minimum value the matrix could theoretically contains
     * @param max : a double, the maximum value the matrix could theoretically contains
     * @return an 2D integer array, containing a RGB mapping of the matrix 
     */
    public static int[][] matrixToRGBImage(double[][] matrix, double min, double max) {
    		//Requirement : matrix should contain at least one element
    		assert matrix != null;
		assert matrix.length > 0;
		assert matrix[0].length > 0;
    		
    		int[][] normalizedMatrix = new int[matrix.length][matrix[0].length];
    		for(int i = 0; i < matrix.length; i++) {
    			for(int j = 0; j < matrix[i].length; j++) {
    				//System.out.println("matrix[i][j] = " + matrix[i][j]);
    				//We first map the values [min, max] to [0, 255]
    				normalizedMatrix[i][j] = (int)Math.round(((matrix[i][j] - min)/(max - min))*255);
    				//Then we interpret this value as an gray value, and make an RGB pixel out of it
    				//System.out.println("normalizedMatrix[i][j] = " + normalizedMatrix[i][j]);
//    				System.out.println(matrix[i][j]);
    				normalizedMatrix[i][j] = getRGB(normalizedMatrix[i][j]);
    				//Note that we also could have called toRGB passing normalizedMatrix in argument
    				//outside of both for loops, also resulting in the matrix being converted
    				//to an RGB matrix (but it would have been less efficient)
    			}
    		}
    	return normalizedMatrix;
    }
    
    public static int sanitizeValue(int x) {
		if(x < 0) {
			x = 0;
		}
		else if (x > 255) {
			x = 255;
		}
		return x;
}
    
}
