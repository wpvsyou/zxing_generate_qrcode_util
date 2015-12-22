package com.zxing.generate.qrcode.util;

import android.graphics.Bitmap;

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
}
