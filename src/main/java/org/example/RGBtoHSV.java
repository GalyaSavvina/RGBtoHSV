package org.example;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class RGBtoHSV {

    public static void main(String[] args) {
        try {
            // Открываем файл с JPEG-изображением
            File inputFile = new File("D:\\RGBtoHSV\\3.jpg");
            BufferedImage originalImage = ImageIO.read(inputFile);

            // Получаем ширину и высоту изображения
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            // Создаем новое изображение для измененных HSV значений
            BufferedImage modifiedImage = convertRGBtoHSV(originalImage, width, height);

            // Сохраняем измененное изображение
            saveImage(modifiedImage, "D:\\RGBtoHSV\\done.jpg");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage convertRGBtoHSV(BufferedImage originalImage, int width, int height) {
        BufferedImage modifiedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = originalImage.getRGB(x, y);
                double[] hsv = rgbToHSV(rgb);
                // Проверяем, является ли пиксель красным
                if (isRedPixel(hsv[0], hsv[1], hsv[2])) {
                    // Заменяем красный цвет на зеленый в HSV
                    hsv[0] = 120.0; //зеленый
                }

                int newRGB = hsvToRgb(hsv[0], hsv[1], hsv[2]);
                modifiedImage.setRGB(x, y, newRGB);
            }
        }

        return modifiedImage;
    }

    private static boolean isRedPixel(double hue, double saturation, double value) {
        // Проверяем, является ли пиксель красным в модели HSV
        return (hue >= 0 && hue <= 20) || (hue >= 330 && hue <= 360);
    }

    private static double[] rgbToHSV(int rgb) {

        // Извлекаем значения компонентов RGB
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        // Нормализуем значения
        double rr = red / 255.0;
        double gg = green / 255.0;
        double bb = blue / 255.0;

        double maxVal = Math.max(Math.max(rr, gg), bb);
        double minVal = Math.min(Math.min(rr, gg), bb);

        double hue, saturation, value;

        value = maxVal;

        if (maxVal > 0.0) {
            saturation = (maxVal - minVal) / maxVal;
        } else {
            saturation = 0.0;
        }
        hue = 0.0;
        if (maxVal == minVal) {
            hue = 0.0;
        } else {
            double delta = maxVal - minVal;
            if (maxVal == rr) {
                hue = 60.0 * ((gg - bb) / delta + ((gg < bb) ? 6 : 0));
            } else if (maxVal == gg) {
                hue = 60.0 * ((bb - rr) / delta + 2);
            } else if (maxVal == bb) {
                hue = 60.0 * ((rr - gg) / delta + 4);
            }

            if (hue >= 360.0) {
                hue -= 360.0;
            }
        }

        return new double[]{hue, saturation, value};
    }

    private static int hsvToRgb(double hue, double saturation, double value) {
        int hi = (int) (hue / 60) % 6;
        double f = hue / 60 - hi;
        double p = value * (1 - saturation);
        double q = value * (1 - f * saturation);
        double t = value * (1 - (1 - f) * saturation);

        double red, green, blue;
        switch (hi) {
            case 0:
                red = value;
                green = t;
                blue = p;
                break;
            case 1:
                red = q;
                green = value;
                blue = p;
                break;
            case 2:
                red = p;
                green = value;
                blue = t;
                break;
            case 3:
                red = p;
                green = q;
                blue = value;
                break;
            case 4:
                red = t;
                green = p;
                blue = value;
                break;
            case 5:
                red = value;
                green = p;
                blue = q;
                break;
            default:
                red = green = blue = 0;
                break;
        }

        // Преобразовываем значения RGB в целые числа от 0 до 255
        int r = (int) (red * 255);
        int g = (int) (green * 255);
        int b = (int) (blue * 255);

        // Объединяем значения в одно число
        return (r << 16) | (g << 8) | b;
    }

    private static void saveImage(BufferedImage image, String outputPath) {
        try {
            File outputFile = new File(outputPath);
            ImageIO.write(image, "jpg", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}






