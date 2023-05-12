package pdl.backend;

import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;
import boofcv.struct.border.BorderType;


public class Couleur {
    public static void thresholdBrightness(Planar<GrayU8> input, int delta) {
        for (int c = 0; c < input.getNumBands(); ++c) {
            GrayU8 channel = input.getBand(c);
            for (int y = 0; y < input.height; ++y) {
                for (int x = 0; x < input.width; ++x) {
                    int gl = channel.get(x, y);
                    int new_gl = gl + delta;
                    if (0 <= new_gl && new_gl <= 255) {
                        gl = new_gl;
                    } else if (new_gl < 0) {
                        gl = 0;
                    } else if (new_gl > 255) {
                        gl = 255;
                    }
                    channel.set(x, y, gl);
                }
            }
        }
    }


    public static void meanFilterSimple(Planar<GrayU8> input, int size) {
        for (int c = 0; c < input.getNumBands(); ++c) {
            GrayU8 channel = input.getBand(c);
            for (int y = size/2; y< input.height-size/2; ++y) {
                for (int x = size/2; x < input.width-size/2; ++x) { 
                    int gl=0;
                    int halfSize=size/2;
                    if(size%2==1){
                        halfSize+=1;
                    }
                    for(int i=-size/2;i<halfSize;i++){
                        for(int j=-size/2;j<halfSize;j++){
                            gl=gl+channel.get((x+i), (y+j));
                        } 
                    }
                    gl=gl/(size*size);
                    channel.set(x, y, gl);
                }
            } 
        }
    }

      
    //2. Convolution
    public static void convolution(Planar<GrayU8> input, int[][] kernel) {
        int Size = kernel.length;
        int Radius = Size / 2;

        for (int c = 0; c < input.getNumBands(); ++c) {
            GrayU8 channel = input.getBand(c);
            for (int y = Radius; y < input.height - Radius; y++) {
                for (int x = Radius; x < input.width - Radius; x++) {
                    double sum = 0.0;
                    for (int ky = -Radius; ky <= Radius; ky++) {
                        for (int kx = -Radius; kx <= Radius; kx++) {
                            int gl = channel.get(x + kx, y + ky);
                            int Value = kernel[ky + Radius][kx + Radius];
                            sum += gl * Value;
                        }
                    }
                    int outputValue = (int) Math.round(sum / getKernelSum(kernel));
                    channel.set(x, y, outputValue);
                }
            }
        }
    }
    
    private static int getKernelSum(int[][] kernel) {
        int sum = 0;
        for (int[] row : kernel) {
            for (int value : row) {
                sum += value;
            }
        }
        return sum;
    }
    
    //3. Conversion couleur / niveaux de gris
    public static void colorToGrey(Planar<GrayU8> input) {
        for (int y = 0; y < input.height; ++y) {
            for (int x = 0; x < input.width; ++x) {
                for (int c = 0; c < input.getNumBands(); ++c) {
                    GrayU8 channel = input.getBand(c);
                    double gl=0.3*input.getBand(0).get(x ,y )+ 0.59*input.getBand(1).get(x ,y )+ 0.11*input.getBand(2).get(x ,y );
                    channel.set(x, y, (int) gl);
                }
            }
        }
    }



  public static void colorToGray(Planar<GrayU8> imageIn) 
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
               imageIn.getBand(0).set(x, y, gray);
               imageIn.getBand(1).set(x, y, gray);
               imageIn.getBand(2).set(x, y, gray);
           }
       }
   }



    //4. Conversion RGB / HSV
    public static void rgbToHsv(int r, int g, int b, float[] hsv) {
        float rF = r / 255.0f;
        float gF = g / 255.0f;
        float bF = b / 255.0f;
        float max = Math.max(rF, Math.max(gF, bF));
        float min = Math.min(rF, Math.min(gF, bF));
        float h, s, v;
        if (max == min) {
            h = 0;
        } else if (max == rF) {
            h = ((gF - bF) / (max - min)) % 6;
        } else if (max == gF) {
            h = (bF - rF) / (max - min) + 2;
        } else {
            h = (rF - gF) / (max - min) + 4;
        }
        h = h * 60;
        if (h < 0) {
            h += 360;
        }
        s = max == 0 ? 0 : 1 - (min / max);
        v = max;
        hsv[0] = h;
        hsv[1] = s;
        hsv[2] = v;
    }
    
    public static void hsvToRgb(float h, float s, float v, int[] rgb) {
        int ti = (int) Math.floor(h / 60) % 6;
        float f = h / 60 - ti;
        float l = v * (1 - s);
        float m = v * (1 - f * s);
        float n = v * (1 - (1 - f) * s);
        
        switch (ti) {
            case 0:
                rgb[0] = Math.round(v * 255);
                rgb[1] = Math.round(n * 255);
                rgb[2] = Math.round(l * 255);
                break;
            case 1:
                rgb[0] = Math.round(m * 255);
                rgb[1] = Math.round(v * 255);
                rgb[2] = Math.round(l * 255);
                break;
            case 2:
                rgb[0] = Math.round(l * 255);
                rgb[1] = Math.round(v * 255);
                rgb[2] = Math.round(n * 255);
                break;
            case 3:
                rgb[0] = Math.round(l * 255);
                rgb[1] = Math.round(m * 255);
                rgb[2] = Math.round(v * 255);
                break;
            case 4:
                rgb[0] = Math.round(n * 255);
                rgb[1] = Math.round(l * 255);
                rgb[2] = Math.round(v * 255);
                break;
            default:
                rgb[0] = Math.round(v * 255);
                rgb[1] = Math.round(l * 255);
                rgb[2] = Math.round(m * 255);
                break;
        }
    }
    

    public static void colorizeImage(Planar<GrayU8> input, float hue) {
    
        // Loop over all pixels in the image
        for (int y = 0; y < input.height; y++) {
            for (int x = 0; x < input.width; x++) {
                // Get the RGB value of the pixel
                int[] rgb = new int[3];
                float[] hsv = new float[3];
                
                GrayU8 channelr = input.getBand(0);
                GrayU8 channelg = input.getBand(1);
                GrayU8 channelb = input.getBand(2); 

                rgbToHsv(channelr.get(x, y), channelg.get(x, y), channelb.get(x, y),hsv);

                hsvToRgb(hue, hsv[1], hsv[2], rgb);
    
                // Set the new RGB value for the pixel
                channelr.set(x, y, rgb[0]);
                channelg.set(x, y, rgb[1]);
                channelb.set(x, y, rgb[2]);
            }
        }
    }

     // 5. égalisation d'histogramme
    public static int[] generatehistogramme(GrayU8 input) {
		int[] histo = new int[256];
		for (int i = 0; i< 256; i++) {
			histo[i]=0;
		}
		for (int y = 0; y< input.height; ++y) {
			for (int x = 0; x < input.width; ++x) {
			  int gl = input.get(x, y);
			  histo[gl]=histo[gl]+1;
			}
		  }
		return histo;
	  }

    public static int[] generateCumHistogramme(GrayU8 input) {
		int[] histo =generatehistogramme(input);
		int[] cumHisto = new int[256];
		int sum=0;
		for (int i = 0; i < 256; i++) {
			sum=sum+histo[i];
			cumHisto[i] = sum;
		}
		return cumHisto;
	  }

      public static void Histogram_equalization(Planar<GrayU8> input, boolean s, boolean v) {
        Planar<GrayU8> grey = input.clone();

        // Convertir l'image en niveaux de gris
        colorToGrey(grey);
    
        // Calculer la table de correspondance pour l'image en niveaux de gris
		int[] cumHisto =generateCumHistogramme(grey.getBand(0));
		int nb_pixel= input.height * input.width;
        for(int y = 0; y< input.height; ++y) {
            for (int x = 0; x < input.width; ++x) {
                int[] rgb = new int[3];
                float[] hsv_input = new float[3];
                float[] hsv_grey = new float[3];
                
                GrayU8 channelr_input = input.getBand(0);
                GrayU8 channelg_input = input.getBand(1);
                GrayU8 channelb_input = input.getBand(2); 

                GrayU8 channelr_grey = grey.getBand(0);
                GrayU8 channelg_grey = grey.getBand(1);
                GrayU8 channelb_grey = grey.getBand(2); 

                rgbToHsv((cumHisto[channelr_grey.get(x, y)]*255)/nb_pixel , (cumHisto[channelg_grey.get(x, y)]*255)/nb_pixel , (cumHisto[channelb_grey.get(x, y)]*255)/nb_pixel,hsv_grey);
                rgbToHsv(channelr_input.get(x, y), channelg_input.get(x, y), channelb_input.get(x, y),hsv_input);

                 if(s==true && v==true){
                    hsvToRgb(hsv_grey[0],hsv_input[1],hsv_input[2],rgb);
                }if(s==true && v==false){
                    hsvToRgb(hsv_grey[0],hsv_grey[1],hsv_input[2],rgb);
                }if(s==false && v==true){
                    hsvToRgb(hsv_grey[0],hsv_input[1],hsv_grey[2],rgb);
                }if(s==false && v==false){
                    hsvToRgb(hsv_grey[0],hsv_grey[1],hsv_grey[2],rgb);
                }

                channelr_input.set(x, y, rgb[0]);
                channelg_input.set(x, y, rgb[1]);
                channelb_input.set(x, y, rgb[2]);
            }
        }
    }
    // 5. extension de dynamique
	public static int[] generateLUT(int min, int max) {
		int[] lut = new int[256];
		for (int i = 0; i < 256; i++) {
		  if (max - min != 0) {
			lut[i] = (255 * (i - min)) / (max - min);
		  }
		}
		return lut;
	  }
	  
	  public static void lutColor(Planar<GrayU8> input) {
        Planar<GrayU8> grey = input.clone();

        // Convertir l'image en niveaux de gris
        colorToGrey(grey);
        int min=255;
		int max=1;
        for (int c = 0; c < input.getNumBands(); ++c) {
            GrayU8 channel = input.getBand(c);
            for (int y = 0; y < grey.height; ++y) {
                for (int x = 0; x < grey.width; ++x) {
                    int gl = channel.get(x, y);
                    if(min>gl){
                        min=gl;
                    }if(max<gl){
                        max=gl;
                    }
                }
            }
        }
		int[] lut = generateLUT(min, max);
		for (int y = 0; y< input.height; ++y) {
		  for (int x = 0; x < input.width; ++x) {
            int[] rgb = new int[3];
            float[] hsv_input = new float[3];
            float[] hsv_grey = new float[3];
                
            GrayU8 channelr_input = input.getBand(0);
            GrayU8 channelg_input = input.getBand(1);
            GrayU8 channelb_input = input.getBand(2); 

            GrayU8 channelr_grey = grey.getBand(0);
            GrayU8 channelg_grey = grey.getBand(1);
            GrayU8 channelb_grey = grey.getBand(2); 

            rgbToHsv(lut[channelr_grey.get(x, y)] ,lut[channelg_grey.get(x, y)] ,lut[channelb_grey.get(x, y)] ,hsv_grey);
            rgbToHsv(channelr_input.get(x, y), channelg_input.get(x, y), channelb_input.get(x, y),hsv_input);

            hsvToRgb(hsv_grey[0],hsv_input[1],hsv_input[2],rgb);

            channelr_input.set(x, y, rgb[0]);
            channelg_input.set(x, y, rgb[1]);
            channelb_input.set(x, y, rgb[2]);
		  }
		}
	  }
      public static void gradientImageSobel(Planar<GrayU8> input) {
        colorToGray(input);
        
        int width = input.width;
	    int height = input.height;

        int[] gradientX = {-1, 0, 1, -2, 0, 2, -1, 0, 1};
        int[] gradientY = {1, 2, 1, 0, 0, 0, -1, -2, -1};

        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) 
            {


                for (int c = 0; c < input.getNumBands(); ++c) {

                    GrayU8 channel = input.getBand(c);

                    int gx = 0;
                    int gy = 0;

                for (int i = 0; i < 3; i++) 
                {
                    for (int j = 0; j < 3; j++) 
                    {

                        
                        int pixel = channel.get(x + j - 1, y + i - 1);
                        gx += pixel * gradientX[i * 3 + j];
                        gy += pixel * gradientY[i * 3 + j];
                    }
                }

                int magnitude = (int) Math.sqrt(gx * gx + gy * gy);
                channel.set(x, y, (byte) magnitude);
            
                }
            }
        }
    }

      public static void meanFilterWithBorders(Planar<GrayU8> input, int size, BorderType borderType) {
        if(borderType==BorderType.SKIP){
          meanFilterSimple(input,size);
        }else{
            for (int c = 0; c < input.getNumBands(); ++c){
                GrayU8 channel = input.getBand(c);
                for (int y = 0; y< input.height; ++y) {
                    for (int x = 0; x < input.width; ++x) { 
                    int gl=0;
                    int halfSize=size/2;
                    if(size%2==1){
                        halfSize+=1;
                    }
                    for(int i=-size/2;i<halfSize;i++){
                        for(int j=-size/2;j<halfSize;j++){
                            int value_x=x+i,value_y=y+j;
                            if(borderType==BorderType.EXTENDED){
                                if(value_x<0){
                                    value_x=0;
                                }
                                if(value_y<0){
                                    value_y=0;
                                }if(value_x>=input.width){
                                    value_x=input.width-2;
                                }if(value_y>=input.height){
                                    value_y=input.height-2;
                                }
                            }if(borderType==BorderType.REFLECT){
                                if(value_x<0){
                                    value_x=Math.abs(value_x);
                                }
                                if(value_y<0){
                                    value_y=Math.abs(value_x);;
                                }if(value_x>=input.width){
                                    value_x=x-i;
                                }if(value_y>=input.height){
                                    value_y=y-j;
                                }
                            }
                            if(borderType==BorderType.NORMALIZED){
                                if(value_x >= 0 && value_x < input.width && value_y >= 0 && value_y < input.height) {
                                    gl += channel.get(value_x, value_y);
                                }
                                }if(borderType!=BorderType.NORMALIZED){
                                    gl=gl+channel.get(value_x, value_y);
                            }
                        } 
                    }
                    if(borderType==BorderType.NORMALIZED){
                        gl = gl + (input.width * input.height - (size * size)) * (gl / (size * size));
                        gl = gl / (input.width * input.height);
                    }else{
                        gl=gl/(size*size);
                    }
                    channel.set(x, y, gl);
                    }
                    }
            }
        }
      }

}