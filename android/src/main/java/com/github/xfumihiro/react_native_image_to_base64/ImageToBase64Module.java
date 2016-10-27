package com.github.xfumihiro.react_native_image_to_base64;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.Callback;

import java.util.Map;
import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.provider.MediaStore;
import android.net.Uri;
import java.io.IOException;
import java.io.FileNotFoundException;

public class ImageToBase64Module extends ReactContextBaseJavaModule {

  private int quality = 100;
  private String url;
  private Context context;

  public ImageToBase64Module(ReactApplicationContext reactContext) {
    super(reactContext);
    this.context = (Context) reactContext;
  }

  @Override
  public String getName() {
    return "RNImageToBase64";
  }

  @ReactMethod
  public void getBase64String(final ReadableMap options, final Callback callback) {
    try {
      parseOptions(options);

      Bitmap image = MediaStore.Images.Media.getBitmap(
        this.context.getContentResolver(),
        Uri.parse(url));

      if (image == null) {
        callback.invoke("Failed to decode Bitmap, uri: " + url);
      } else {
        callback.invoke(null, bitmapToBase64(image));
      }
    } catch (IOException e) {
    }
  }

  private void parseOptions(final ReadableMap options) {
    if (options.hasKey("quality")) {
      quality = (int) (options.getDouble("quality") * 100);
    }

    if (options.hasKey("url")) {
      url = options.getString("url");
    }
  }

  private String bitmapToBase64(final Bitmap bitmap) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
    byte[] byteArray = byteArrayOutputStream.toByteArray();
    return Base64.encodeToString(byteArray, Base64.DEFAULT);
  }
}
