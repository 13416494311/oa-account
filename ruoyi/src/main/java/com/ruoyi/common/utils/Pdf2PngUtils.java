package com.ruoyi.common.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Pdf2PngUtils {

    public static List<File> pdf2png(File pdfFile, int indexOfStart, int indexOfEnd) {
        List<File> pngFileList = new ArrayList<File>();
        try {
            PDDocument doc = PDDocument.load(pdfFile);
            PDFRenderer renderer = new PDFRenderer(doc);
            for (int i = indexOfStart; i < indexOfEnd; i++) {
                //BufferedImage image = renderer.renderImageWithDPI(i, 144); // Windows native DPI
                BufferedImage image = renderer.renderImage(i, 2.5f);
                File pngFile = new File(pdfFile.getName().substring(0, pdfFile.getName().lastIndexOf(".")) + "_" + (i + 1) + ".png");
                try {
                    ImageIO.write(image, "PNG", pngFile);
                    pngFileList.add(pngFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (pdfFile.exists()) {
                pdfFile.delete();
            }
        }
        return pngFileList;
    }

    //转换全部的pdf
    public static List<File> pdf2png(File pdfFile) {
        List<File> pngFileList = new ArrayList<File>();
        try {
            PDDocument doc = PDDocument.load(pdfFile);
            PDFRenderer renderer = new PDFRenderer(doc);
            for (int i = 0; i < doc.getNumberOfPages(); i++) {
                //BufferedImage image = renderer.renderImageWithDPI(i, 144); // Windows native DPI
                BufferedImage image = renderer.renderImage(i, 2.5f);
                File pngFile = new File(pdfFile.getName().substring(0, pdfFile.getName().lastIndexOf(".")) + "_" + (i + 1) + ".png");
                try {
                    ImageIO.write(image, "PNG", pngFile);
                    pngFileList.add(pngFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (pdfFile.exists()) {
                pdfFile.delete();
            }
        }
        return pngFileList;
    }

}
