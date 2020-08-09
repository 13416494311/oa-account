package com.ruoyi.common.utils;

import com.spire.pdf.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Pdf2PngUtil {

    public static List<File> pdf2png(File pdfFile, int indexOfStart, int indexOfEnd) {
        List<File> pngFileList = new ArrayList<File>();
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(pdfFile);
            PdfDocument pdf = new PdfDocument(inputStream);
            BufferedImage image;
            for (int i = indexOfStart; i < indexOfEnd; i++) {
                image = pdf.saveAsImage(i);
                File pngFile = new File(pdfFile.getName().substring(0, pdfFile.getName().lastIndexOf(".")) + "_" + (i + 1) + ".png");
                try {
                    ImageIO.write(image, "PNG", pngFile);
                    pngFileList.add(pngFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            inputStream.close();
            pdf.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pdfFile.exists()) {
                pdfFile.delete();
            }
        }
        return pngFileList;
    }

    //转换全部的pdf
    public static List<File> pdf2png(File pdfFile) {
        List<File> pngFileList = new ArrayList<File>();
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(pdfFile);
            PdfDocument pdf = new PdfDocument(inputStream);
            BufferedImage image;
            for (int i = 0; i < pdf.getPages().getCount(); i++) {
                image = pdf.saveAsImage(i);
                File pngFile = new File(pdfFile.getName().substring(0, pdfFile.getName().lastIndexOf(".")) + "_" + (i + 1) + ".png");
                try {
                    ImageIO.write(image, "PNG", pngFile);
                    pngFileList.add(pngFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            inputStream.close();
            pdf.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (pdfFile.exists()) {
                pdfFile.delete();
            }
        }
        return pngFileList;
    }

}
