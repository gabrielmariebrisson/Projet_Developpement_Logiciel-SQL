package pdl.backend;

import java.awt.image.BufferedImage;

import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;


public class color {




	public static void increaseColor(Planar<GrayU8> image, Planar<GrayU8> output, int delta) {
		for (int y = 0; y < image.height; ++y) 
		{
			for (int x = 0; x < image.width; ++x) 
			{
				int valRed = image.getBand(0).get(x, y) + delta;
				int valGreen = image.getBand(1).get(x, y) + delta;
				int valBlue = image.getBand(2).get(x, y) + delta;

				if (valRed < 0) 
				{
					valRed = 0;
				} 
				if (valRed > 255)
				{
					valRed = 255;
				}

				if (valGreen > 255)
				{
					valGreen = 255;
				}
				if (valGreen < 0) 
				{
					valGreen = 0;
				} 

				if (valBlue > 255)
				{
					valBlue = 255;
				}
				if (valBlue < 0) 
				{
					valBlue = 0;
				} 
				
				output.getBand(0).set(x, y, valRed);
				output.getBand(1).set(x, y, valGreen);
				output.getBand(2).set(x, y, valBlue);

			}
		}
	}


	public static void convolution(Planar<GrayU8> imageIn, Planar<GrayU8> imageOut, int[][] kernel) 
  {
  
      int kernelHeight = kernel.length;
      int kernelWidth = kernel[0].length;
      int halfHeight = kernelHeight / 2;
      int halfWidth = kernelWidth / 2;
      int height = imageIn.height - halfHeight;
      int width = imageIn.width - halfWidth;

      int totalCoeff = 0;
      for (int i = 0; i < kernelHeight; i++) {
        for (int j = 0; j < kernelWidth; j++) {
            totalCoeff += kernel[i][j];
            
        }
      }
  
      for (int y = halfHeight; y < height; y++) 
      {
          for (int x = halfWidth; x < width; x++) 
          {
              int sumRed = 0;
			  int sumGreen = 0;
			  int sumBlue = 0;
              for (int i = 0; i < kernelHeight; i++) {
                  for (int j = 0; j < kernelWidth; j++) {
                      sumRed += kernel[i][j] * imageIn.getBand(0).get(x + j - halfWidth, y + i - halfHeight);
					  sumGreen += kernel[i][j] * imageIn.getBand(1).get(x + j - halfWidth, y + i - halfHeight);
					  sumBlue += kernel[i][j] * imageIn.getBand(2).get(x + j - halfWidth, y + i - halfHeight);      
                  	}
              	}

				  imageOut.getBand(0).set(x, y, sumRed/totalCoeff);
				  imageOut.getBand(1).set(x, y, sumGreen/totalCoeff);
				  imageOut.getBand(2).set(x, y, sumBlue/totalCoeff);

          }
      }
  }

  public static void colorToGray(Planar<GrayU8> imageIn, Planar<GrayU8> imageOut) 
   {
		int width = imageIn.getWidth();
		int height = imageIn.getHeight();

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				// Get color channels of input image
				int r = imageIn.getBand(0).get(x, y);
				int g = imageIn.getBand(1).get(x, y);
				int b = imageIn.getBand(2).get(x, y);

				// Compute grayscale value using formula
				int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);

				// Set grayscale value in output image
				imageOut.getBand(0).set(x, y, gray);
				imageOut.getBand(1).set(x, y, gray);
				imageOut.getBand(2).set(x, y, gray);
			}
		}
    }


	public static void rgbToHsv(int r, int g, int b, float[] hsv) {
		float rF = r / 255f;
		float gF = g / 255f;
		float bF = b / 255f;
	
		float cMax = Math.max(Math.max(rF, gF), bF);
		float cMin = Math.min(Math.min(rF, gF), bF);
		float delta = cMax - cMin;
	
		float hue = 0;
		if (delta != 0) {
			if (cMax == rF) {
				hue = ((gF - bF) / delta) % 6;
			} else if (cMax == gF) {
				hue = ((bF - rF) / delta) + 2;
			} else if (cMax == bF) {
				hue = ((rF - gF) / delta) + 4;
			}
			hue *= 60;
			if (hue < 0) {
				hue += 360;
			}
		}
	
		float saturation = (cMax == 0) ? 0 : delta / cMax;
		float value = cMax;
	
		hsv[0] = hue;
		hsv[1] = saturation;
		hsv[2] = value;
	}

	public static void hsvToRgb(float h, float s, float v, int[] rgb) {
		float c = v * s;
		float x = c * (1 - Math.abs((h / 60) % 2 - 1));
		float m = v - c;
	
		float r, g, b;
		if (h < 60) {
			r = c;
			g = x;
			b = 0;
		} else if (h < 120) {
			r = x;
			g = c;
			b = 0;
		} else if (h < 180) {
			r = 0;
			g = c;
			b = x;
		} else if (h < 240) {
			r = 0;
			g = x;
			b = c;
		} else if (h < 300) {
			r = x;
			g = 0;
			b = c;
		} else {
			r = c;
			g = 0;
			b = x;
		}
	
		rgb[0] = Math.round((r + m) * 255);
		rgb[1] = Math.round((g + m) * 255);
		rgb[2] = Math.round((b + m) * 255);
	}
	
	public static void colorizeImage(Planar<GrayU8> input, Planar<GrayU8> output, float hue) {

    // Parcourir tous les pixels de l'image
    for (int y = 0; y < input.height; y++) {
        for (int x = 0; x < input.width; x++) {
            // Récupérer la valeur RGB du pixel
            int r = input.getBand(0).get(x, y);
            int g = input.getBand(1).get(x, y);
            int b = input.getBand(2).get(x, y);

            // Convertir la valeur RGB en HSV
            float[] hsv = new float[3];
            rgbToHsv(r, g, b, hsv);

            // Remplacer la teinte par la valeur de teinte donnée
            hsv[0] = hue;

            // Convertir la valeur HSV en RGB
            int[] rgb = new int[3];
            hsvToRgb(hsv[0], hsv[1], hsv[2], rgb);

            // Stocker les nouvelles valeurs RGB dans l'image de sortie
            output.getBand(0).set(x, y, rgb[0]);
            output.getBand(1).set(x, y, rgb[1]);
            output.getBand(2).set(x, y, rgb[2]);
        }
    }
}






public static int[] getHistogram(GrayU8 input) {
      
	int[] histogram = new int[256];
	for (int grayLVL = 0; grayLVL < 256; grayLVL++) 
	{
		int cpt = 0;
	
		for (int y = 0; y < input.height; ++y) 
		{
			for (int x = 0; x < input.width; ++x) 
			{
				int gl = input.get(x, y);
				if (gl == grayLVL)
				{
					// +1 pixel de ce gris précis
					cpt++;
				}
				
			}
		}

		histogram[grayLVL] = cpt;

	}
	return histogram;
}

public static int[] getCumulativeHistogram(GrayU8 input) {
	int[] histogram = getHistogram(input);
	int[] cumulativeHistogram = new int[256];

	cumulativeHistogram[0] = histogram[0];
	for (int i = 1; i < 256; i++) {
		cumulativeHistogram[i] = cumulativeHistogram[i - 1] + histogram[i];
	}
	return cumulativeHistogram;
}



public static void equalizeHistogram(Planar<GrayU8> input, Planar<GrayU8> output) {

    // Get the cumulative histograms for each channel
    /* int[] cumulativeHistogramR = getCumulativeHistogram(input.getBand(0));
    int[] cumulativeHistogramG = getCumulativeHistogram(input.getBand(1));
    int[] cumulativeHistogramB = getCumulativeHistogram(input.getBand(2)); */


	
	Planar<GrayU8> gray = input.clone();
	colorToGray(input, gray); 
	int[] cumulativeHistogram = getCumulativeHistogram(gray.getBand(0));

    for (int y = 0; y < input.height; ++y) {
        for (int x = 0; x < input.width; ++x) {


			
            // Get the pixel values for each channel

			
            int r = input.getBand(0).get(x, y);
            int g = input.getBand(1).get(x, y);
            int b = input.getBand(2).get(x, y);
			


			/*
            // Apply the histogram equalization for each channel
            int rAfterOP = cumulativeHistogramR[r] * 255 / (input.width * input.height);
            int gAfterOP = cumulativeHistogramG[g] * 255 / (input.width * input.height);
            int bAfterOP = cumulativeHistogramB[b] * 255 / (input.width * input.height);


			int rgbAfterOP = (rAfterOP + gAfterOP + bAfterOP) / 3;
			// Convertir la valeur RGB en HSV
			*/


			
			int glR = gray.getBand(0).get(x, y);
			int glG = gray.getBand(1).get(x, y);
			int glB = gray.getBand(2).get(x, y);
			//int gl = (glR + glB + glG) / 3;
			
			int glAfterOPR = cumulativeHistogram[glR] * 255 / (gray.width * gray.height);
			int glAfterOPG = cumulativeHistogram[glG] * 255 / (gray.width * gray.height);
			int glAfterOPB = cumulativeHistogram[glB] * 255 / (gray.width * gray.height);

			
			float[] hsv_gray = new float[3];
			float[] hsv_color = new float[3];
			rgbToHsv(glAfterOPR, glAfterOPG, glAfterOPB, hsv_gray);
			rgbToHsv(r, g, b, hsv_color);
   
			//hsv[2] = (glAfterOP / 255.0f) * 100.0f;
			 
			
   
			// Convertir la valeur HSV en RGB
			int[] rgb = new int[3];
			hsvToRgb(hsv_color[0], hsv_color[1], hsv_gray[2], rgb);
			


             // Stocker les nouvelles valeurs RGB dans l'image de sortie
			 output.getBand(0).set(x, y, rgb[0]);
			 output.getBand(1).set(x, y, rgb[1]);
			 output.getBand(2).set(x, y, rgb[2]);
			
        }
    }

}





public static void gradientImageSobel(Planar<GrayU8> input, Planar<GrayU8> output) 
{

	Planar<GrayU8> gray = input.clone();
	colorToGray(input, gray); 

	int width = gray.width;
	int height = gray.height;

	int[] gradientX = {-1, 0, 1, -2, 0, 2, -1, 0, 1};
	int[] gradientY = {1, 2, 1, 0, 0, 0, -1, -2, -1};

	for (int y = 1; y < height - 1; y++) {
		for (int x = 1; x < width - 1; x++) {


			for (int c = 0; c < gray.getNumBands(); ++c) {
				GrayU8 channel = gray.getBand(c);

			int gx = 0;
			int gy = 0;

			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {

					
					int pixel = channel.get(x + j - 1, y + i - 1);
					gx += pixel * gradientX[i * 3 + j];
					gy += pixel * gradientY[i * 3 + j];
				}
			}

			int magnitude = (int) Math.sqrt(gx * gx + gy * gy);
			output.getBand(c).set(x, y, (byte) magnitude);
		
			}
		}
	}
}

	



    public static void main( String[] args ) {

    	// load image
		if (args.length < 2) {
			System.out.println("missing input or output image filename");
			System.exit(-1);
		}
		final String inputPath = args[0];
		//GrayU8 input = UtilImageIO.loadImage(inputPath, GrayU8.class);
		BufferedImage input = UtilImageIO.loadImage(inputPath);	
		if(input == null) {
			System.err.println("Cannot read input file '" + inputPath);
			System.exit(-1);
		}
		Planar<GrayU8> image = ConvertBufferedImage.convertFromPlanar(input, null, true, GrayU8.class);
		Planar<GrayU8> output = image.createSameShape();

		// processing veuillez decommenter la fonction à tester
			
		
		//increaseColor(image, output, 100);


		// Filtre Moyenneur
		/*
		int[][] kernel = new int[5][5];
		for (int i = 0; i < 5; i++) 
		{
		for (int j = 0; j < 5; j++) 
		{
			kernel[i][j] = 1;
		}
		}
		*/

		// Filtre Gaussien a décommenter pour essayer
		/* 
		int[][] kernel = 
					{
					{1, 2, 3, 2, 1},
					{2, 6, 8, 6, 2},
					{3, 8, 10, 8, 3},
					{2, 6, 8, 6, 2},
					{1, 2, 3, 2, 1}
					}; 
		*/

		//convolution(image, output, kernel); 
		

		//colorToGray(image, output);

		//colorizeImage(image, output, 270);

		equalizeHistogram(image, output);


		// save output image
		final String outputPath = args[1];
		UtilImageIO.saveImage(output, outputPath);
		System.out.println("Image saved in: " + outputPath);
	}

}