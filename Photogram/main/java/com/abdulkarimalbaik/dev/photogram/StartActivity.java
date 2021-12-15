package com.abdulkarimalbaik.dev.photogram;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.abdulkarimalbaik.dev.photogram.Utils.Common;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class StartActivity extends AppCompatActivity {

    Button btn_designer , btn_browser;
    TextView title , txtSlogan;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Note: add this code before setContentView method
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/indie_flower.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_start);

        SQLiteDatabase.loadLibs(StartActivity.this);

        btn_designer = (Button)findViewById(R.id.btn_designer);
        btn_browser = (Button)findViewById(R.id.btn_browser);
        title = (TextView)findViewById(R.id.title);
        txtSlogan = (TextView)findViewById(R.id.txtslogan);

        Typeface typeSlogan = Typeface.createFromAsset(getAssets() , "fonts/indie_flower.ttf");
        txtSlogan.setTypeface(typeSlogan);

        Typeface typeTitle = Typeface.createFromAsset(getAssets() , "fonts/nabila.TTF");
        title.setTypeface(typeTitle);

        btn_designer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(StartActivity.this , MainActivity.class));
            }
        });

        btn_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(StartActivity.this , BrowserActivity.class));

            }
        });

        printKeyHash();
    }

    private void printKeyHash() {

        try {

            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo info = getPackageManager().getPackageInfo("com.abdulkarimalbaik.dev.photogram",
                    PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures){

                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash" , Base64.encodeToString(md.digest() , Base64.DEFAULT));

            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {

        File dir = new File(Common.getPathImages());
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }

        super.onDestroy();
    }
}
