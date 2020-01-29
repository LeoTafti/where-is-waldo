package main;

public class Program {
	public static void principalProgramm() {
		basicTest();
//		testNullImageOrPattern();
//		testSameSizedImageAndPattern();
	}
	public static void distanceBasedTest(int[][] image, int[][] pattern) {
		double[][] distance = DistanceBasedSearch.distanceMatrix(pattern, image);
    	
	    int[][] distanceVisualization = ImageProcessing.matrixToRGBImage(distance, 0, 255);
	    	Helper.show(distanceVisualization, "distance matrix");
	    
	    	int[][] p = Collector.findNBest(1, distance, true);
	    	for(int i = 0; i < p.length; i++) {
	    		Helper.drawBox(p[i][0], p[i][1], pattern[0].length, pattern.length, image);
	    	}
	    	Helper.show(image, "Found!");	
	}
	
	public static void similarityBasedTest(int[][] image, int[][] pattern) {
		double[][] grayImage = ImageProcessing.toGray(image);
		double[][] grayPattern = ImageProcessing.toGray(pattern);
		
		double[][] similarity = SimilarityBasedSearch.similarityMatrix(grayPattern, grayImage);
		int[][] similarityVisualization = ImageProcessing.matrixToRGBImage(similarity, -10, 10);
		Helper.show(similarityVisualization, "similarity matrix");
		
		int[][] p = Collector.findNBest(1, similarity, false);
	    	for(int i = 0; i < p.length; i++) {
	    		Helper.drawBox(p[i][0], p[i][1], pattern[0].length, pattern.length, image);
	    	}
		Helper.show(image, "Found!");
	}
	
	public static void basicTest() {
		int[][] image = Helper.read("images/food.png");
		int[][] pattern = Helper.read("images/onions.png");
		
		distanceBasedTest(image, pattern);
//		similarityBasedTest(image, pattern);
	}
	
	public static void testNullImageOrPattern() {
		int[][] image = Helper.read("images/food.png");
		int[][] pattern = Helper.read("images/onions.png");
		
		//Null image:
		distanceBasedTest(null, pattern);
//		similarityBasedTest(null, pattern);
		
		//Null pattern:
//		distanceBasedTest(image, null);
//		similarityBasedTest(image, null);
		
		//Both image and pattern null:
//		distanceBasedTest(null, null);
//		similarityBasedTest(null, null);
		
		//Note that the program has also been tested with
		//empty but not null images and/or pattern
		//and the assertions consider these cases (as) well
	}
	
	public static void testSameSizedImageAndPattern() {
		//these cases are supposed to cause assertion errors
		
//		//first, two times the same picture.
//		int[][] image1 = Helper.read("images/charlie.png");
//		
//		distanceBasedTest(image1, image1);
//		similarityBasedTest(image1, image1);
//		
//		//then, two same sized image and pattern (1 pixel), but different from each other
//		int[][] image2 = {{0xFF22BB}};
//		int[][] pattern2 = {{0xAA4488}};
//		
//		Helper.show(image2, "image2");
//		Helper.show(pattern2, "pattern2");
//		
//		distanceBasedTest(image2, pattern2);
//		similarityBasedTest(image2, pattern2);
		
//		//then, two same sized image and pattern (9 pixel), different form each other
//		int[][] image3 = {
//				{0x112233, 0x445566, 0x778899},
//				{0x998877, 0x665544, 0x332211},
//				{0x123456, 0x456789, 0x987654}};
//		int[][] pattern3 = {
//				{0xAABBCC, 0xDDEEFF, 0xCCAAAA},
//				{0xAAAACC, 0xFFEEDD, 0xAAEEFF},
//				{0xABCDEF, 0xFEDCBA, 0xDACBAE}};
//		
//		Helper.show(image3, "image3");
//		Helper.show(pattern3, "pattern3");
//		
//		distanceBasedTest(image3, pattern3);
//		similarityBasedTest(image3, pattern3);
		
		//then, two different sized image and pattern, pixel [1][1] is the common to both
		//so it should locate the pattern in the middle of the image
		int[][] image4 = {
				{0x112233, 0x445566, 0x233952},
				{0xAEF4A3, 0x749211, 0x332211},
				{0x123456, 0xEFC5676, 0x987654}};
		int[][] pattern4 = {
				{0x332211},
				{0x987654}};
//		int[][] image4 = Helper.read("images/food.png");
//		int[][] pattern4 = Helper.read("images/onions.png");
		
		Helper.show(image4, "image4");
		Helper.show(pattern4, "pattern4");
		
//		distanceBasedTest(image4, pattern4);
		similarityBasedTest(image4, pattern4);
	}
}
