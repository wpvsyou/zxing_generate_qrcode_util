package com.zxing.generate.qrcode.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Hashtable;

/**
 * Sample Zxing util, only use to create a QR-image.
 *
 * @author peng.wang@pekall.com
 * @deprecated 2015-12-21
 */
public class GenerateQrCodeUtil {

    public GenerateQrCodeUtil() {
    }

    /**
     * Create QR-image.
     *
     * @param content  QR-image content must no null.
     * @param qrWidth  width of QR-image.
     * @param qrHeight height of QR-image.
     * @return the bitmap of QR-image or null.
     * @throws NullPointerException
     */
    public Bitmap createQRImage(String content, int qrWidth, int qrHeight) {
        Bitmap bitmap = null;
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().
                    encode(content, BarcodeFormat.QR_CODE, qrWidth, qrHeight, hints);
            int[] pixels = new int[qrWidth * qrHeight];
            for (int y = 0; y < qrHeight; y++) {
                for (int x = 0; x < qrWidth; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * qrWidth + x] = 0xff000000;
                    } else {
                        pixels[y * qrWidth + x] = 0xffffffff;
                    }
                }
            }
            bitmap = Bitmap.createBitmap(qrWidth, qrHeight, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, qrWidth, 0, 0, qrWidth, qrHeight);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * Create QR-image with a logo in it center!
     *
     * @param content    QR-image content must no null.
     * @param qrWidth    width of QR-image.
     * @param qrHeight   height of QR-image.
     * @param centerLogo the logo bitmap.
     */
    public Bitmap createQRImage(String content, int qrWidth, int qrHeight, Bitmap centerLogo) {
        final Bitmap bitmap = createQRImage(content, qrWidth, qrHeight);
        if (centerLogo != null) {
            return addLogo(bitmap, centerLogo);
        }
        return null;
    }

    /**
     * Create QR-image with a logo in it center!
     *
     * @param saveQrImagePath the path save the QR-image.
     * @param content         QR-image content must no null.
     * @param qrWidth         width of QR-image.
     * @param qrHeight        height of QR-image.
     * @param centerLogo      the logo bitmap.
     */
    public void createQRImage(String content, int qrWidth,
                              int qrHeight, Bitmap centerLogo, String saveQrImagePath) {
        final Bitmap bitmap = createQRImage(content, qrWidth, qrHeight, centerLogo);
        if (null != bitmap) {
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG,
                        200, new FileOutputStream(saveQrImagePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Add logo in the QR-image.
     *
     * @param qrImage    QR-image bitmap.
     * @param centerLogo logo bitmap.
     * @return QR-image with the center logo.
     */
    private static Bitmap addLogo(Bitmap qrImage, Bitmap centerLogo) {
        if (qrImage == null) {
            return null;
        }

        if (centerLogo == null) {
            return qrImage;
        }

        int qrWidth = qrImage.getWidth();
        int qrHeight = qrImage.getHeight();
        int logoWidth = centerLogo.getWidth();
        int logoHeight = centerLogo.getHeight();

        if (qrWidth == 0 || qrHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return qrImage;
        }

        float scaleFactor = qrWidth * 1.0f / 5 / logoWidth;
        Bitmap qrSrc = Bitmap.createBitmap(qrWidth, qrHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(qrSrc);
            canvas.drawBitmap(qrImage, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, qrWidth / 2, qrHeight / 2);
            canvas.drawBitmap(centerLogo, (qrWidth - logoWidth)
                    / 2, (qrHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            qrSrc = null;
            e.getStackTrace();
        }

        return qrSrc;
    }
}
