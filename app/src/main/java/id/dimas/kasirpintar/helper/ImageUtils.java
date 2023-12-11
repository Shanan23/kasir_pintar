package id.dimas.kasirpintar.helper;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtils {

    private static final String TAG = "ImageUtils";

    public static byte[] uriToByteArray(Context context, Uri uri) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(uri);

            if (inputStream != null) {
                // Convert InputStream to byte array
                byte[] byteArray = readBytes(inputStream);

                // Close the InputStream
                inputStream.close();

                return byteArray;
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Error reading InputStream: " + e.getMessage());
        }

        return null;
    }

    private static byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, bytesRead);
        }

        return byteBuffer.toByteArray();
    }

    // Optionally, you can resize the image before converting it to a byte array
    public static byte[] resizeAndConvertUriToByteArray(Context context, Uri uri, int targetWidth, int targetHeight) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(uri);

            if (inputStream != null) {
                // Decode the input stream into a Bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                // Resize the Bitmap to the target width and height
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);

                // Convert the resized Bitmap to a byte array
                byte[] byteArray = bitmapToByteArray(resizedBitmap);

                // Close the InputStream
                inputStream.close();

                return byteArray;
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "Error reading InputStream: " + e.getMessage());
        }

        return null;
    }

    private static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}