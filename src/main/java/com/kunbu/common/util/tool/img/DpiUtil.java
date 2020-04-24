package com.kunbu.common.util.tool.img;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.w3c.dom.Element;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * https://blog.csdn.net/wxh_xdk/article/details/79145726
 *
 */
public class DpiUtil {
    
    /** 1英寸是2.54里面 */
    private static final double INCH_2_CM = 2.54d;
    
    public static byte[] processPNG(BufferedImage image, int dpi) throws IOException {
        for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName("png"); iw.hasNext();) {
            ImageWriter writer = iw.next();
            ImageWriteParam writeParam = writer.getDefaultWriteParam();
            ImageTypeSpecifier typeSpecifier = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_INT_RGB);
            IIOMetadata metadata = writer.getDefaultImageMetadata(typeSpecifier, writeParam);
            if (metadata.isReadOnly() || !metadata.isStandardMetadataFormatSupported()) {
                continue;
            }
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageOutputStream stream = null;
            try {
                // for PNG, it's dots per millimeter
                double dotsPerMilli = 1.0 * dpi / 10 / INCH_2_CM;
                IIOMetadataNode horiz = new IIOMetadataNode("HorizontalPixelSize");
                horiz.setAttribute("value", Double.toString(dotsPerMilli));

                IIOMetadataNode vert = new IIOMetadataNode("VerticalPixelSize");
                vert.setAttribute("value", Double.toString(dotsPerMilli));
                IIOMetadataNode dim = new IIOMetadataNode("Dimension");
                dim.appendChild(horiz);
                dim.appendChild(vert);
                IIOMetadataNode root = new IIOMetadataNode("javax_imageio_1.0");
                root.appendChild(dim);
                metadata.mergeTree("javax_imageio_1.0", root);

                stream = ImageIO.createImageOutputStream(output);
                writer.setOutput(stream);
                writer.write(metadata, new IIOImage(image, null, metadata), writeParam);
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
            return output.toByteArray();
        }
        return null;
    }

    public static byte[] processJPG(BufferedImage image, int dpi) throws IOException {
        for (Iterator<ImageWriter> iw = ImageIO.getImageWritersByFormatName("jpeg"); iw.hasNext();) {
            ImageWriter writer = iw.next();

            ImageWriteParam writeParams = writer.getDefaultWriteParam();
            writeParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            //调整图片质量
            writeParams.setCompressionQuality(1f);

            IIOMetadata data = writer.getDefaultImageMetadata(new ImageTypeSpecifier(image), writeParams);
            Element tree = (Element) data.getAsTree("javax_imageio_jpeg_image_1.0");
            Element jfif = (Element) tree.getElementsByTagName("app0JFIF").item(0);
            jfif.setAttribute("Xdensity", dpi + "");
            jfif.setAttribute("Ydensity", dpi + "");
            // density is dots per inch
            jfif.setAttribute("resUnits", "1");

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageOutputStream stream = null;
            try {
                stream = ImageIO.createImageOutputStream(out);
                writer.setOutput(stream);
                writer.write(data, new IIOImage(image, null, null), writeParams);
            } finally {
                stream.close();
            }

            return out.toByteArray();
        }
        return null;
    }

    /**
     * 调整分辨率，java默认生成图片分辨率是72（sun包下的，服务器不能用）
     * @param image
     * @param os
     * @param xDensity
     * @param yDensity
     */
    @Deprecated
    public static void handleDpi(BufferedImage image, OutputStream os, int xDensity, int yDensity) throws Exception{
        JPEGImageEncoder jpegEncoder = JPEGCodec.createJPEGEncoder(os);
        JPEGEncodeParam jpegEncodeParam = jpegEncoder.getDefaultJPEGEncodeParam(image);
        jpegEncodeParam.setDensityUnit(JPEGEncodeParam.DENSITY_UNIT_DOTS_INCH);
        jpegEncoder.setJPEGEncodeParam(jpegEncodeParam);
        jpegEncodeParam.setQuality(1f, false);
        jpegEncodeParam.setXDensity(xDensity);
        jpegEncodeParam.setYDensity(yDensity);
        jpegEncoder.encode(image, jpegEncodeParam);
        image.flush();
    }
}
