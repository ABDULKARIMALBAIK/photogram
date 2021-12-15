package com.abdulkarimalbaik.dev.photogram;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulkarimalbaik.dev.photogram.Adapter.ViewPagerImagesAdapter;
import com.abdulkarimalbaik.dev.photogram.Database.DBImage;
import com.abdulkarimalbaik.dev.photogram.Interface.ViewPagerItemListener;
import com.abdulkarimalbaik.dev.photogram.Model.CipherImage;
import com.abdulkarimalbaik.dev.photogram.Utils.Common;
import com.wajahatkarim3.easyflipviewpager.BookFlipPageTransformer;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BrowserActivity extends AppCompatActivity {

    FloatingActionButton fabSelect , fabDelete;
    TextView txt_image_name;
    ViewPager viewPager;
    RelativeLayout rootLayout;

    List<CipherImage> cipherImages;
    ViewPagerImagesAdapter adapter;

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

        setContentView(R.layout.activity_browser);


        fabSelect = (FloatingActionButton)findViewById(R.id.fabSelected);
        fabDelete = (FloatingActionButton)findViewById(R.id.fabDelete);
        txt_image_name = (TextView)findViewById(R.id.txt_image_name);
        viewPager = (ViewPager)findViewById(R.id.view_pager);
        rootLayout = (RelativeLayout)findViewById(R.id.rootLayout);

        Common.txt_image_name = txt_image_name;

        loadImageDB();

        if (cipherImages.size() > 0)
            txt_image_name.setText(cipherImages.get(0).getDisplay_name());
        else
            Toast.makeText(this, "You don't have any images !", Toast.LENGTH_SHORT).show();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                Common.viewPagerItem = position;
                Common.txt_image_name.setText(cipherImages.get(position).getDisplay_name().substring(0 , 15));
            }

            @Override
            public void onPageSelected(int position) {



            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        fabSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cipherImages.size() > 0){

                    Intent intent = new Intent(BrowserActivity.this , MainActivity.class);
                    intent.putExtra(Common.IMAGE_SELECTED_BROWSER , cipherImages.get(Common.viewPagerItem).getDisplay_name());
                    startActivity(intent);
                    finish();
                }
                else
                    Toast.makeText(BrowserActivity.this, "You don't have any images !", Toast.LENGTH_SHORT).show();
            }
        });

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    
                    if (cipherImages.size() > 0){

                        DBImage.getInstance(BrowserActivity.this).deleteImage(cipherImages.get(Common.viewPagerItem).getDisplay_name());
                        loadImageDB();
                        adapter.notifyDataSetChanged();
                        Snackbar.make(rootLayout , "Image was deleted !" , Snackbar.LENGTH_SHORT).show();

                    }
                    else
                        Toast.makeText(BrowserActivity.this, "You don't have any images !", Toast.LENGTH_SHORT).show();
                  
                }
                catch (Exception e){

                    Toast.makeText(BrowserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });
    }

    private void loadImageDB() {

        cipherImages = DBImage.getInstance(this).getAllImages();

        if (cipherImages.size() <= 0)
            txt_image_name.setText("");

        adapter = new ViewPagerImagesAdapter(BrowserActivity.this,
                 cipherImages);

        viewPager.setAdapter(adapter);

        setFlipPageTransformer();

        if (adapter.getCount() == 0)
            Toast.makeText(this, "You don't have any image was stoaged !", Toast.LENGTH_SHORT).show();
    }

    private void setFlipPageTransformer() {

        //Create Book Flip Page
        BookFlipPageTransformer bookFlipPageTransformer = new BookFlipPageTransformer();
        bookFlipPageTransformer.setScaleAmountPercent(10f);
        viewPager.setPageTransformer(true , bookFlipPageTransformer);
    }

}
