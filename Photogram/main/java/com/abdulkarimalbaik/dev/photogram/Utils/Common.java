package com.abdulkarimalbaik.dev.photogram.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class Common  {

    public static final String IMAGE_SELECTED_BROWSER = "IMAGE_BROWSER";
    public static int color_selected = -1;
    public static boolean segmentedButtonState = false;
    public static int row_selected = -1;
    public static int frame_selected = -1;

    public static TextView txt_image_name;
    public static int viewPagerItem = -1;

    public static final String CLIENT_ID_GOOGLE_DRIVE = ".....................................................................";
    public static final String API_KEY_GOOGLE_DRIVE = ".....................................";

    public static String getPathImages(){

        File file = new File(new StringBuilder(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .append("/")
                .append("PhotogramPhotos").toString());

        if (!file.isDirectory())
            file.mkdir();

        return new StringBuilder(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .append("/")
                .append("PhotogramPhotos").toString();
    }

    public static boolean isConnectionToInternet(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null){

            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null){

                for (int i = 0; i < info.length; i++) {

                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }

    public static File createFile(Bitmap bitmap){

        //Make sure you set permission Read/Write external storage AND set Provider that exists in Manifest

        String file_path = getPathImages();
        File dir = new File(file_path);
//        if(!dir.exists())
//            dir.mkdirs();
        File file = new File(dir.getAbsoluteFile(), new StringBuilder(UUID.randomUUID().toString()).append(".png").toString());
        FileOutputStream fOut;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }
}
