package com.abdulkarimalbaik.dev.photogram;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulkarimalbaik.dev.photogram.Database.DBImage;
import com.abdulkarimalbaik.dev.photogram.Fragments.AddTextFragment;
import com.abdulkarimalbaik.dev.photogram.Fragments.BrushFragment;
import com.abdulkarimalbaik.dev.photogram.Fragments.EditImageFragment;
import com.abdulkarimalbaik.dev.photogram.Fragments.EmojiFragment;
import com.abdulkarimalbaik.dev.photogram.Fragments.FiltersListFragment;
import com.abdulkarimalbaik.dev.photogram.Fragments.FrameFragment;
import com.abdulkarimalbaik.dev.photogram.Interface.AddFrameListener;
import com.abdulkarimalbaik.dev.photogram.Interface.AddTextFragmentListener;
import com.abdulkarimalbaik.dev.photogram.Interface.BrushFragmentListener;
import com.abdulkarimalbaik.dev.photogram.Interface.EditImageFragmentListener;
import com.abdulkarimalbaik.dev.photogram.Interface.EmojiFragmentListener;
import com.abdulkarimalbaik.dev.photogram.Interface.FiltersListFragmentListener;
import com.abdulkarimalbaik.dev.photogram.Model.CipherImage;
import com.abdulkarimalbaik.dev.photogram.Model.DecryptImage;
import com.abdulkarimalbaik.dev.photogram.Utils.BitmapUtils;
import com.abdulkarimalbaik.dev.photogram.Utils.Common;
import com.abdulkarimalbaik.dev.photogram.Utils.EncryptDecryptUtils;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.crypto.NoSuchPaddingException;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements
        FiltersListFragmentListener,
        EditImageFragmentListener,
        BrushFragmentListener,
        EmojiFragmentListener,
        AddTextFragmentListener,
        AddFrameListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String pictureName = "images/sea.jpg";
    public static final int PERMISSION_PICK_IMAGE = 1000;
    public static final int PERMISSION_INSERT_IMAGE = 1001;
    private static final int CAMERA_REQUEST = 1002;

    //Google Drive variables
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_CREATOR = 2;
    private static final int REQUEST_CODE_RESOLUTION = 3;
    private boolean isAPÏConnected;
    private GoogleApiClient mGoogleApiClient;
    private Bitmap mBitmapToSave;

    PhotoEditorView photoEditorView;
    PhotoEditor photoEditor;

    Toolbar toolbar;
    ConstraintLayout coordinatorLayout;

//    TabLayout tabLayout;
//    ViewPager viewPager;

    Bitmap originalBitmap, filteredBitmap, finalBitmap;

    FiltersListFragment filtersListFragment;
    EditImageFragment editImageFragment;
    BrushFragment brushFragment;
    EmojiFragment emojiFragment;
    AddTextFragment addTextFragment;
    FrameFragment frameFragment;

    CardView btn_filters_list, btn_edit, btn_brush, btn_emoji, btn_add_text, btn_add_image, btn_frame, btn_crop,
            btn_back,
            btn_forward,
            btn_AI;

    int brightnessFinal = 0;
    float constraintFinal = 1.0f;
    float saturationFinal = 1.0f;
    Uri image_uri_selected;

    Bitmap flowerLine;
    Bitmap sunGlass;
    int numFaces;

    Uri saveImageCut;

    Canvas canvas;

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    //Load native image filter lib
    static {
        System.loadLibrary("NativeImageProcessor");
    }

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

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Photogram");

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        filtersListFragment = FiltersListFragment.getInstance(null, MainActivity.this);
        editImageFragment = EditImageFragment.getInstance();
        brushFragment = BrushFragment.getInstance();
        emojiFragment = EmojiFragment.getInstance();
        addTextFragment = AddTextFragment.getInstance();
        frameFragment = FrameFragment.getInstance();

        //Init Views
        photoEditorView = (PhotoEditorView) findViewById(R.id.image_preview);
        photoEditor = new PhotoEditor.Builder(this, photoEditorView)
                .setPinchTextScalable(true)
                .setDefaultEmojiTypeface(Typeface.createFromAsset(getAssets(), "fonts/emojione-android.ttf"))
                .build();
//        PhotoViewAttacher photoView = new PhotoViewAttacher();
//        photoView.update();

        coordinatorLayout = (ConstraintLayout) findViewById(R.id.coordinator);
        btn_edit = (CardView) findViewById(R.id.btn_edit);
        btn_filters_list = (CardView) findViewById(R.id.btn_filter_list);
        btn_brush = (CardView) findViewById(R.id.btn_brush);
        btn_emoji = (CardView) findViewById(R.id.btn_emoji);
        btn_add_text = (CardView) findViewById(R.id.btn_add_text);
        btn_add_image = (CardView) findViewById(R.id.btn_add_image);
        btn_frame = (CardView) findViewById(R.id.btn_add_frame);
        btn_crop = (CardView) findViewById(R.id.btn_crop);
        btn_back = (CardView) findViewById(R.id.btn_back);
        btn_forward = (CardView) findViewById(R.id.btn_forward);
        btn_AI = (CardView) findViewById(R.id.btn_AI);

        btn_AI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Please waiting...");
                progressDialog.show();

                Bitmap originalBitmap = ((BitmapDrawable) photoEditorView.getSource().getDrawable()).getBitmap();

                flowerLine = BitmapFactory.decodeResource(getResources(), R.drawable.flower);
                sunGlass = BitmapFactory.decodeResource(getResources(), R.drawable.eye_patch);

                Bitmap faceBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.RGB_565);
                canvas = new Canvas(faceBitmap);
                canvas.drawBitmap(originalBitmap, 0, 0, null);

                FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .setMode(FaceDetector.FAST_MODE)
                        .build();

                if (!faceDetector.isOperational()) {

                    Toast.makeText(MainActivity.this, "Face detector could not be set up on your device !", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }

                Frame frame = new Frame.Builder().setBitmap(originalBitmap).build();
                SparseArray<Face> sparseArray = faceDetector.detect(frame);
                numFaces = sparseArray.size();

                if (sparseArray.size() <= 0) {

                    Toast.makeText(MainActivity.this, "No face was detected !", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    return;
                }

                for (int i = 0; i < sparseArray.size(); i++) {

                    com.google.android.gms.vision.face.Face face = sparseArray.valueAt(i);
                    //drawFaceRectangle(canvas , drawBitmap(canvas , originalBitmap) , face);
                    detectLandmark(face, progressDialog);
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                photoEditor.undo();
            }
        });

        btn_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                photoEditor.redo();
            }
        });

        btn_filters_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (originalBitmap != null) {

                    if (filtersListFragment != null) {

                        filtersListFragment.setListener(MainActivity.this);
                        filtersListFragment.show(getSupportFragmentManager(), filtersListFragment.getTag());
                    } else {

                        FiltersListFragment filtersListFragment = FiltersListFragment.getInstance(null, MainActivity.this);
                        filtersListFragment.setListener(MainActivity.this);
                        filtersListFragment.show(getSupportFragmentManager(), filtersListFragment.getTag());
                    }
                } else
                    Toast.makeText(MainActivity.this, "Open image first !", Toast.LENGTH_SHORT).show();


            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (originalBitmap != null) {

                    editImageFragment.setListener(MainActivity.this);
                    editImageFragment.show(getSupportFragmentManager(), editImageFragment.getTag());
                } else
                    Toast.makeText(MainActivity.this, "Open image first !", Toast.LENGTH_SHORT).show();

            }
        });

        btn_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Enable brush mode
                if (originalBitmap != null) {

                    photoEditor.setBrushDrawingMode(true);
                    brushFragment.setBrushFragmentListener(MainActivity.this);
                    brushFragment.show(getSupportFragmentManager(), brushFragment.getTag());
                } else
                    Toast.makeText(MainActivity.this, "Open image first !", Toast.LENGTH_SHORT).show();
            }
        });

        btn_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (originalBitmap != null) {

                    emojiFragment.setListener(MainActivity.this);
                    emojiFragment.show(getSupportFragmentManager(), emojiFragment.getTag());
                } else
                    Toast.makeText(MainActivity.this, "Open image first !", Toast.LENGTH_SHORT).show();

            }
        });

        btn_add_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (originalBitmap != null) {

                    addTextFragment.setListener(MainActivity.this);
                    addTextFragment.show(getSupportFragmentManager(), addTextFragment.getTag());
                } else
                    Toast.makeText(MainActivity.this, "Open image first !", Toast.LENGTH_SHORT).show();
            }
        });

        btn_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (originalBitmap != null) {

                    addImageToPicture();
                } else
                    Toast.makeText(MainActivity.this, "Open image first !", Toast.LENGTH_SHORT).show();

            }
        });

        btn_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (originalBitmap != null) {

                    frameFragment.setListener(MainActivity.this);
                    frameFragment.show(getSupportFragmentManager(), frameFragment.getTag());
                } else
                    Toast.makeText(MainActivity.this, "Open image first !", Toast.LENGTH_SHORT).show();

            }
        });

        btn_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (originalBitmap != null) {

                    startCrop(image_uri_selected);
                } else
                    Toast.makeText(MainActivity.this, "Open image first !", Toast.LENGTH_SHORT).show();

            }
        });

        if (getIntent() != null) {
            if (getIntent().getStringExtra(Common.IMAGE_SELECTED_BROWSER) != null) {

                try {
                    createFileToImage();

                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchProviderException e) {
                    e.printStackTrace();
                }

            }
        }

        if (getIntent() != null) {
            if (getIntent().getStringExtra(Common.IMAGE_SELECTED_BROWSER) == null) {

                loadImage();
            }
        }

//        tabLayout = (TabLayout)findViewById(R.id.tabs);
//        viewPager = (ViewPager)findViewById(R.id.viewPager);
//        setupViewPager(viewPager);
//        tabLayout.setupWithViewPager(viewPager);

    }

    private double drawBitmap(Canvas canvas, Bitmap mBitmap) {
        double viewWidth = canvas.getWidth();
        double viewHeight = canvas.getHeight();
        double imageWidth = mBitmap.getWidth();
        double imageHeight = mBitmap.getHeight();
        double scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight);

        Rect destBounds = new Rect(0, 0, (int) (imageWidth * scale), (int) (imageHeight * scale));
        canvas.drawBitmap(mBitmap, null, destBounds, null);
        return scale;
    }

    private void drawFaceRectangle(Canvas canvas, double scale, Face face) {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        canvas.drawRect((float) (face.getPosition().x * scale),
                (float) (face.getPosition().y * scale),
                (float) ((face.getPosition().x + face.getWidth()) * scale),
                (float) ((face.getPosition().y + face.getHeight()) * scale),
                paint);

    }

    private void detectLandmark(Face face, ProgressDialog progressDialog) {

        for (Landmark landmark : face.getLandmarks()) {

            int cx = (int) (landmark.getPosition().x);
            int cy = (int) (landmark.getPosition().y);

            drawOnImageView(landmark.getType(), cx, cy, progressDialog);
        }
    }

    private void drawOnImageView(int type, int cx, int cy, ProgressDialog progressDialog) {

        //Draw flowers
        if (type == Landmark.NOSE_BASE) {

            int scaleWidth = flowerLine.getScaledWidth(canvas);
            int scaleHeight = flowerLine.getScaledHeight(canvas);

            canvas.drawBitmap(flowerLine, cx - (scaleWidth / 2), cy - (scaleHeight * 2), null);

            //Draw SunGlass
            canvas.drawBitmap(sunGlass, cx - 500, cy - (scaleHeight) + 120, null);

            progressDialog.dismiss();
            Snackbar.make(coordinatorLayout, "Face was detected successfully , Faces: " + numFaces + " !!!", Snackbar.LENGTH_LONG).show();
        }

    }

    private void createFileToImage() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {

        CipherImage cipherImage = DBImage.getInstance(MainActivity.this).selectImage(getIntent().getStringExtra(Common.IMAGE_SELECTED_BROWSER));

        Bitmap bitmap = BitmapUtils.getBitmapFromByte(
                new EncryptDecryptUtils().decryptImage(cipherImage.getPicture(), cipherImage.getLength_encrypt_image()));

        originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);

        photoEditorView.getSource().setImageBitmap(originalBitmap);
        //bitmap.recycle();

        //Fix Crash FilterListFragment
        filtersListFragment = FiltersListFragment.getInstance(originalBitmap, MainActivity.this);
        filtersListFragment.setListener(new FiltersListFragmentListener() {
            @Override
            public void onFilterSelected(Filter filter) {
                resetControl();
                filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                photoEditorView.getSource().setImageBitmap(filter.processFilter(filteredBitmap));
                finalBitmap = filteredBitmap.copy(Bitmap.Config.ARGB_8888, true);
            }
        });

        filtersListFragment.displayThumbnail(originalBitmap);

        File file = Common.createFile(originalBitmap);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            image_uri_selected = FileProvider.getUriForFile(MainActivity.this, "com.abdulkarimalbaik.dev.photogram.myprovider", file);
            grantUriPermission(getPackageName(), image_uri_selected, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else
            image_uri_selected = Uri.fromFile(file);
    }

    private void startCrop(Uri uri) {

        String destinationFileName = new StringBuilder(UUID.randomUUID().toString()).append(".png").toString();

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));
        uCrop.start(MainActivity.this);
    }

    private void addImageToPicture() {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {

                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, PERMISSION_INSERT_IMAGE);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                        Toast.makeText(MainActivity.this, "Permission denied !", Toast.LENGTH_SHORT).show();
                    }
                })
                .check();
    }

    private void loadImage() {

        originalBitmap = BitmapUtils.getBitmapFromAssets(this, pictureName, 300, 300);
        filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
        photoEditorView.getSource().setImageBitmap(originalBitmap);

        FillUri();
    }

    private void FillUri() {

        File file = Common.createFile(originalBitmap);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

            image_uri_selected = FileProvider.getUriForFile(MainActivity.this, "com.abdulkarimalbaik.dev.photogram.myprovider", file);
            grantUriPermission(getPackageName(), image_uri_selected, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        else
            image_uri_selected = Uri.fromFile(file);
    }

    public void setupViewPager(ViewPager upViewPager) {

//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        filtersListFragment = new FiltersListFragment();
        filtersListFragment.setListener(this);

        editImageFragment = new EditImageFragment();
        editImageFragment.setListener(this);

//        adapter.addFragment(filtersListFragment , "FILTERS");
//        adapter.addFragment(editImageFragment , "EDIT");

//        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_open: {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Open with...")
                        .setMessage("Select open mode :")
                        .setIcon(R.drawable.ic_image_orange_24dp)
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                View view = getLayoutInflater().inflate(R.layout.select_open_mode, null);
                LinearLayout open_browser = (LinearLayout) view.findViewById(R.id.open_browser);
                LinearLayout open_gallery = (LinearLayout) view.findViewById(R.id.open_gallery);

                builder.setView(view);
                final AlertDialog alertDialog = builder.show();

                open_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        openImageFromGallery();
                        alertDialog.dismiss();
                    }
                });

                open_browser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        alertDialog.dismiss();
                        startActivity(new Intent(MainActivity.this, BrowserActivity.class));
                        finish();

                    }
                });


                return true;
            }
            case R.id.action_save: {

                final DialogInterface[] dialogInterface = new DialogInterface[1];
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Save in...")
                        .setMessage("Select save mode :")
                        .setIcon(R.drawable.ic_save_orange_24dp)
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                View view = getLayoutInflater().inflate(R.layout.select_open_mode, null);
                LinearLayout save_browser = (LinearLayout) view.findViewById(R.id.open_browser);
                LinearLayout save_gallery = (LinearLayout) view.findViewById(R.id.open_gallery);

                builder.setView(view);
                final AlertDialog alertDialog = builder.show();
                // builder.show();

                save_gallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (photoEditorView.getSource().getDrawable() != null) {

                            alertDialog.dismiss();
                            saveImageToGallery();
                        }
                        else
                            Toast.makeText(MainActivity.this, "Please select a picture first !", Toast.LENGTH_SHORT).show();
                    }
                });

                save_browser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (photoEditorView.getSource().getDrawable() != null) {

                            final String imagePath =
                                    Common.getPathImages() +
                                            "/" + UUID.randomUUID().toString() + "_profile.png";

                            final CipherImage cipherImage = new CipherImage();

                            Dexter.withActivity(MainActivity.this)
                                    .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                                    .withListener(new MultiplePermissionsListener() {
                                        @Override
                                        public void onPermissionsChecked(MultiplePermissionsReport report) {

                                            if (report.areAllPermissionsGranted()) {

                                                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                    // TODO: Consider calling
                                                    //    ActivityCompat#requestPermissions
                                                    // here to request the missing permissions, and then overriding
                                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                    //                                          int[] grantResults)
                                                    // to handle the case where the user grants the permission. See the documentation
                                                    // for ActivityCompat#requestPermissions for more details.
                                                    return;
                                                }
                                                photoEditor.saveImage(imagePath, new PhotoEditor.OnSaveListener() {
                                                    @Override
                                                    public void onSuccess(@NonNull String imagePath) {

                                                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                                                        try {

                                                            DecryptImage decryptImage = new EncryptDecryptUtils().encryptImage(BitmapUtils.getByteFromBitmap(
                                                                    (bitmap)));

                                                            cipherImage.setPicture(decryptImage.getImageEncrypt());
                                                            cipherImage.setTitle("Title");
                                                            cipherImage.setDisplay_name(UUID.randomUUID().toString() + "_profile.png");
                                                            cipherImage.setDescription("Description");
                                                            cipherImage.setDate_added(String.valueOf(UUID.randomUUID().toString()));
                                                            cipherImage.setLength_encrypt_image(decryptImage.getImageEncryptLength());

                                                            DBImage.getInstance(MainActivity.this).saveImage(cipherImage);
                                                            alertDialog.dismiss();
                                                            //dialogInterface[0].dismiss();
                                                            Snackbar.make(coordinatorLayout, "image was saved !", Snackbar.LENGTH_SHORT).show();

                                                        } catch (NoSuchPaddingException e) {
                                                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            e.printStackTrace();
                                                        } catch (NoSuchAlgorithmException e) {
                                                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            e.printStackTrace();
                                                        } catch (NoSuchProviderException e) {
                                                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {

                                                    }
                                                });
                                            } else
                                                Toast.makeText(MainActivity.this, "permission denied !", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                                        }
                                    })
                                    .check();

                        } else
                            Toast.makeText(MainActivity.this, "Please select a picture first !", Toast.LENGTH_SHORT).show();
                    }
                });

                return true;
            }
            case R.id.action_camera: {

                openCamera();
                return true;
            }
            case R.id.action_share: {

                final AlertDialog alertDialog;

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Select share mode : ")
                        .setTitle("Share by...")
                        .setIcon(R.drawable.ic_share_orange_24dp)
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                View view = getLayoutInflater().inflate(R.layout.select_share_mode, null);
                LinearLayout facebookMode = (LinearLayout) view.findViewById(R.id.select_facebook);
                LinearLayout instagramMode = (LinearLayout) view.findViewById(R.id.select_instagram);
                LinearLayout messengerMode = (LinearLayout) view.findViewById(R.id.select_messenger);
                LinearLayout whatsappMode = (LinearLayout) view.findViewById(R.id.select_whatsapp);

                builder.setView(view);
                alertDialog = builder.show();

                facebookMode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Common.isConnectionToInternet(MainActivity.this)) {

                            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                                @Override
                                public void onSuccess(Sharer.Result result) {
                                    Toast.makeText(MainActivity.this, "Share successful !", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancel() {
                                    Toast.makeText(MainActivity.this, "Share cancelled !", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onError(FacebookException error) {
                                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            shareFacebook();
                            alertDialog.dismiss();

                        } else
                            Toast.makeText(MainActivity.this, "Check your connection internet first !", Toast.LENGTH_SHORT).show();
                    }
                });

                instagramMode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Common.isConnectionToInternet(MainActivity.this)) {

                            shareInstagram();
                            alertDialog.dismiss();
                        } else
                            Toast.makeText(MainActivity.this, "Check your connection internet first !", Toast.LENGTH_SHORT).show();
                    }
                });

                messengerMode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Common.isConnectionToInternet(MainActivity.this)) {

                            shareMessenger();
                            alertDialog.dismiss();
                        } else
                            Toast.makeText(MainActivity.this, "Check your connection internet first !", Toast.LENGTH_SHORT).show();
                    }
                });

                whatsappMode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Common.isConnectionToInternet(MainActivity.this)) {

                            shareWhatsapp();
                            alertDialog.dismiss();
                        } else
                            Toast.makeText(MainActivity.this, "Check your connection internet first !", Toast.LENGTH_SHORT).show();
                    }
                });

                return true;
            }
            case R.id.action_drive: {

                if (Common.isConnectionToInternet(MainActivity.this)) {

                    connectAPIClient();
//                    try {
//                        checkConnectionDrive();
//
//                    }
//                    catch (GeneralSecurityException e) {
//                        e.printStackTrace();
//                    }
//                    catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    if (isAPÏConnected) {

                        Dexter.withActivity(MainActivity.this)
                                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                                .withListener(new MultiplePermissionsListener() {
                                    @Override
                                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                                        if (report.areAllPermissionsGranted()) {

                                            if (photoEditorView.getSource().getDrawable() != null) {

                                                final String imagePath =
                                                        Common.getPathImages() +
                                                                "/" + UUID.randomUUID().toString() + "_profile.png";

                                                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                                    // TODO: Consider calling
                                                    //    ActivityCompat#requestPermissions
                                                    // here to request the missing permissions, and then overriding
                                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                    //                                          int[] grantResults)
                                                    // to handle the case where the user grants the permission. See the documentation
                                                    // for ActivityCompat#requestPermissions for more details.
                                                    return;
                                                }
                                                photoEditor.saveImage(imagePath, new PhotoEditor.OnSaveListener() {
                                                    @Override
                                                    public void onSuccess(@NonNull String imagePath) {

                                                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                                                        saveFileToDrive(bitmap);
                                                    }

                                                    @Override
                                                    public void onFailure(@NonNull Exception exception) {

                                                    }
                                                });
                                            }
                                        }
                                        else
                                            Toast.makeText(MainActivity.this, "Permission denied !", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                                    }
                                })
                                .check();
                    }
//                    else
//                        //Snackbar.make(coordinatorLayout , "Error Google API is disable or permissions are required !!!" , Snackbar.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(this, "Please check your connection internet first !", Toast.LENGTH_SHORT).show();

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

//    private void checkConnectionDrive() throws GeneralSecurityException, IOException {
//
//        // Build a new authorized API client service.
//        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//
//        Toast.makeText(this, "Right connection !", Toast.LENGTH_SHORT).show();
//
//        // Print the names and IDs for up to 10 files.
//        FileList result = service.files().list()
//                .setPageSize(10)
//                .setFields("nextPageToken, files(id, name)")
//                .execute();
//        List<com.google.api.services.drive.model.File> files = result.getFiles();
//        if (files == null || files.isEmpty()) {
//            System.out.println("No files found.");
//        } else {
//            System.out.println("Files:");
//            for (com.google.api.services.drive.model.File file : files) {
//                System.out.printf("%s (%s)\n", file.getName(), file.getId());
//            }
//        }
//    }

    private void saveFileToDrive(final Bitmap bitmap) {

        Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
            @Override
            public void onResult(@NonNull DriveApi.DriveContentsResult result) {

                if (!result.getStatus().isSuccess()) {
                    Log.e(TAG, "Failed to create new content!.");
                    return;
                }

                Log.i(TAG, "New content has been created.");
                // Get an output stream for the contents.
                OutputStream outputStream = result.getDriveContents().getOutputStream();
                // Write the bitmap data from it.
                ByteArrayOutputStream bitmapStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bitmapStream);
                try {
                    outputStream.write(bitmapStream.toByteArray());
                }
                catch (IOException e1) {
                    Log.i(TAG, "Unable to write file contents.");
                }
                // Create the initial metadata - MIME type and title.
                // Note that the user will be able to change the title later.
                MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                        .setMimeType("image/png").setTitle("myPhoto.png").build();
                // Create an intent for the file chooser, and start it.
                IntentSender intentSender = Drive.DriveApi
                        .newCreateFileActivityBuilder()
                        .setInitialMetadata(metadataChangeSet)
                        .setInitialDriveContents(result.getDriveContents())
                        .build(mGoogleApiClient);
                try {
                    startIntentSenderForResult(
                            intentSender, REQUEST_CODE_CREATOR, null, 0, 0, 0);
                }
                catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "Failed to launch file chooser.");
                }
            }
        });
    }

    private void shareWhatsapp() {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {

                            String imagePath =
                                    Common.getPathImages() +
                                            "/" + UUID.randomUUID().toString() + "_profile.png";
                            if (photoEditorView.getSource().getDrawable() != null) {

                                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                photoEditor.saveImage(imagePath, new PhotoEditor.OnSaveListener() {
                                    @Override
                                    public void onSuccess(@NonNull String imagePath) {

                                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

                                        Uri data;
                                        File file = Common.createFile(bitmap);

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

                                            data = FileProvider.getUriForFile(MainActivity.this, "com.abdulkarimalbaik.dev.photogram.myprovider", file);
                                            grantUriPermission(getPackageName(), data, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        }
                                        else
                                            data = Uri.fromFile(file);

                                        Intent intent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
                                        if (intent != null) {
                                            Intent shareIntent = new Intent();
                                            shareIntent.setAction(Intent.ACTION_SEND);
                                            shareIntent.setType("image/png");
                                            shareIntent.setPackage("com.whatsapp");
                                            shareIntent.putExtra(Intent.EXTRA_STREAM, data);

                                            startActivity(shareIntent);

                                        } else {
                                            Toast.makeText(MainActivity.this, "Please download Whatsapp app !", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onFailure(@NonNull Exception exception) {

                                        Toast.makeText(MainActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } else
                            Toast.makeText(MainActivity.this, "Permission denied !", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                })
                .check();
    }

    private void shareMessenger() {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {

                            String imagePath =
                                    Common.getPathImages() +
                                            "/" + UUID.randomUUID().toString() + "_profile.png";
                            if (photoEditorView.getSource().getDrawable() != null) {

                                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                photoEditor.saveImage(imagePath, new PhotoEditor.OnSaveListener() {
                                    @Override
                                    public void onSuccess(@NonNull String imagePath) {

                                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

                                        Uri data;
                                        File file = Common.createFile(bitmap);

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

                                            data = FileProvider.getUriForFile(MainActivity.this, "com.abdulkarimalbaik.dev.photogram.myprovider", file);
                                            grantUriPermission(getPackageName(), data, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        }
                                        else
                                            data = Uri.fromFile(file);

                                        Intent intent = getPackageManager().getLaunchIntentForPackage("com.facebook.orca");
                                        if (intent != null) {
                                            Intent sendIntent = new Intent();
                                            sendIntent.setType("image/png");
                                            sendIntent.setAction(Intent.ACTION_SEND);
                                            sendIntent.putExtra(Intent.EXTRA_STREAM, data);
                                            sendIntent.setPackage("com.facebook.orca");

                                            startActivity(sendIntent);
                                        } else {
                                            Toast.makeText(MainActivity.this, "Please download Messenger app !", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onFailure(@NonNull Exception exception) {

                                        Toast.makeText(MainActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } else
                            Toast.makeText(MainActivity.this, "Permission denied !", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                })
                .check();
    }

    private void shareInstagram() {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {

                            String imagePath =
                                    Common.getPathImages() +
                                            "/" + UUID.randomUUID().toString() + "_profile.png";
                            if (photoEditorView.getSource().getDrawable() != null) {

                                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                photoEditor.saveImage(imagePath, new PhotoEditor.OnSaveListener() {
                                    @Override
                                    public void onSuccess(@NonNull String imagePath) {

                                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

                                        Uri data;
                                        File file = Common.createFile(bitmap);

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

                                            data = FileProvider.getUriForFile(MainActivity.this, "com.abdulkarimalbaik.dev.photogram.myprovider", file);
                                            grantUriPermission(getPackageName(), data, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        }
                                        else
                                            data = Uri.fromFile(file);

                                        Intent intent = getPackageManager().getLaunchIntentForPackage("com.instagram.android");
                                        if (intent != null) {
                                            Intent shareIntent = new Intent();
                                            shareIntent.setAction(Intent.ACTION_SEND);
                                            shareIntent.setPackage("com.instagram.android");
                                            shareIntent.putExtra(Intent.EXTRA_STREAM, data);
                                            shareIntent.setType("image/png");

                                            startActivity(shareIntent);

                                        } else {
                                            Toast.makeText(MainActivity.this, "Please download Instagram app !", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onFailure(@NonNull Exception exception) {

                                        Toast.makeText(MainActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                        } else
                            Toast.makeText(MainActivity.this, "Permission denied !", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                })
                .check();
    }

    private void shareFacebook() {

        if (photoEditorView.getSource().getDrawable() != null) {

            Dexter.withActivity(MainActivity.this)
                    .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {

                            if (report.areAllPermissionsGranted()) {

                                String imagePath =
                                        Common.getPathImages() +
                                                "/" + UUID.randomUUID().toString() + "_profile.png";

                                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                photoEditor.saveImage(imagePath, new PhotoEditor.OnSaveListener() {
                                    @Override
                                    public void onSuccess(@NonNull String imagePath) {

                                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

                                        SharePhoto sharePhoto = new SharePhoto.Builder()
                                                .setBitmap(bitmap)
                                                .build();
                                        if (ShareDialog.canShow(SharePhotoContent.class)){

                                            SharePhotoContent content = new SharePhotoContent.Builder()
                                                    .addPhoto(sharePhoto)
                                                    .build();

                                            shareDialog.show(content);
                                        }

                                    }

                                    @Override
                                    public void onFailure(@NonNull Exception exception) {

                                    }
                                });
                            }
                            else
                                Toast.makeText(MainActivity.this, "Permission denied !", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                        }
                    })
                    .check();

        }

    }

    private void openCamera() {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {

//                            ContentValues contentValues = new ContentValues();
//                            contentValues.put(MediaStore.Images.Media.TITLE , "New Picture");
//                            contentValues.put(MediaStore.Images.Media.DESCRIPTION , "From Camera");
//                            image_uri_selected = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI , contentValues);



                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            //intent.putExtra(MediaStore.EXTRA_OUTPUT , image_uri_selected);
                            startActivityForResult(intent , CAMERA_REQUEST);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                        Toast.makeText(MainActivity.this, "Permission denied !", Toast.LENGTH_SHORT).show();
                    }
                })
                .check();
    }

    private void openImageFromGallery() {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {

                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent, PERMISSION_PICK_IMAGE);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                        Toast.makeText(MainActivity.this, "Permission denied !", Toast.LENGTH_SHORT).show();
                    }
                })
                .check();
    }

    private void saveImageToGallery() {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {

                            try {

                                String imagePath =
                                        Common.getPathImages() +
                                        "/" + UUID.randomUUID().toString() + "_profile.png";

                                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                photoEditor.saveImage(imagePath, new PhotoEditor.OnSaveListener() {
                                    @Override
                                    public void onSuccess(@NonNull final String imagePath) {

                                        final String path = BitmapUtils.insertImage(getContentResolver(),
                                                BitmapFactory.decodeFile(imagePath),
                                        UUID.randomUUID().toString() + "_profile.png",
                                        "DESCRIPTION");

                                        if (!TextUtils.isEmpty(path)){

                                            Snackbar snackbar = Snackbar.make(coordinatorLayout ,
                                                    "Image saved to gallery !",
                                                    Snackbar.LENGTH_LONG)
                                                    .setAction("OPEN", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {

                                                            openImage(path);
                                                        }
                                                    });

                                            snackbar.show();
                                        }
                                        else {

                                            Snackbar snackbar = Snackbar.make(coordinatorLayout ,
                                                    "Unable to save image !",
                                                    Snackbar.LENGTH_LONG);
                                            snackbar.show();

                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Exception exception) {

                                        Toast.makeText(MainActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        else
                            Toast.makeText(MainActivity.this, "Permission denied !", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                        token.continuePermissionRequest();
                    }
                })
                .check();
    }

    private void openImage(String path) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path) , "image/*");
        startActivity(intent);

    }

    private void handleCropResult(Intent data) {

        Uri resultUri = UCrop.getOutput(data);
        if (resultUri != null){

            photoEditorView.getSource().setImageURI(resultUri);

            //Fix error return original image after crop and use Filters / Edit image
            Bitmap bitmap = ((BitmapDrawable)photoEditorView.getSource().getDrawable()).getBitmap();
            originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888 , true);
            filteredBitmap = originalBitmap;
            finalBitmap = originalBitmap;
        }
        else
            Toast.makeText(this, "Con't retrieve crop image !", Toast.LENGTH_SHORT).show();
    }

    private void handleCropError(Intent data) {

        final  Throwable cropError = UCrop.getError(data);
        if (cropError != null)
            Toast.makeText(this, "You can't use this service for browser'images !", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Unexpected Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK){

            if (requestCode == PERMISSION_PICK_IMAGE ){

                photoEditor.clearAllViews();

                Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this , data.getData() , 800 , 800);

                image_uri_selected = data.getData();

                //Clear bitmap memory
                originalBitmap.recycle();
                finalBitmap.recycle();
                filteredBitmap.recycle();

                originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888 , true);
                finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888 , true);
                filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888 , true);

                photoEditorView.getSource().setImageBitmap(originalBitmap);
                bitmap.recycle();

                //Fix Crash FilterListFragment
                filtersListFragment = FiltersListFragment.getInstance(originalBitmap , MainActivity.this);
                filtersListFragment.setListener(this);

                filtersListFragment.displayThumbnail(originalBitmap);

            }
            else if (requestCode == PERMISSION_INSERT_IMAGE){

                Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this , data.getData() , 250 , 250);
                photoEditor.addImage(bitmap);
            }
            else  if (requestCode == CAMERA_REQUEST ){

                photoEditor.clearAllViews();

              //image_uri_selected = data.getData();

                Bundle b = data.getExtras();
                Bitmap bitmap = (Bitmap)b.get("data");

                File file = Common.createFile(bitmap);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){

                    image_uri_selected = FileProvider.getUriForFile(MainActivity.this, "com.abdulkarimalbaik.dev.photogram.myprovider", file);
                    grantUriPermission(getPackageName(), image_uri_selected, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                else
                    image_uri_selected = Uri.fromFile(file);


                //Clear bitmap memory
                originalBitmap.recycle();
                finalBitmap.recycle();
                filteredBitmap.recycle();

                originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888 , true);
                finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888 , true);
                filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888 , true);

                photoEditorView.getSource().setImageBitmap(originalBitmap);
                bitmap.recycle();

                //Fix Crash FilterListFragment
                filtersListFragment = FiltersListFragment.getInstance(originalBitmap , MainActivity.this);
                filtersListFragment.setListener(this);

                filtersListFragment.displayThumbnail(originalBitmap);



            }
            else if (requestCode == UCrop.REQUEST_CROP)
                handleCropResult(data);

            else if (requestCode == REQUEST_CODE_CREATOR){

                Log.d(TAG, "Image successfully saved.");
                mBitmapToSave = null;
                Snackbar snackbar =  Snackbar.make(findViewById(android.R.id.content), "Photo successfully saved to Google Drive!", Snackbar.LENGTH_LONG)
                        .setActionTextColor(Color.parseColor("#FE6B2D"));

                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(Color.DKGRAY);
                TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(Color.parseColor("#FE6B2D"));
                snackbar.show();
            }
        }
        else if (resultCode == UCrop.RESULT_ERROR)
            handleCropError(data);

        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onBrightnessChanged(int brightness) {

        brightnessFinal = brightness;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightness));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888 , true)));

    }

    @Override
    public void onSaturationCanged(float saturation) {

        saturationFinal = saturation;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(saturationFinal));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888 , true)));
    }

    @Override
    public void onConstraintChanged(float constraint) {

        constraintFinal = constraint;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new ContrastSubFilter(constraintFinal));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888 , true)));
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditComplete() {

        Bitmap bitmap = filteredBitmap.copy(Bitmap.Config.ARGB_8888 , true);

        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightnessFinal));
        myFilter.addSubFilter(new SaturationSubfilter(saturationFinal));
        myFilter.addSubFilter(new ContrastSubFilter(constraintFinal));

        finalBitmap = myFilter.processFilter(bitmap);
    }

    @Override
    public void onFilterSelected(Filter filter) {

        resetControl();
        filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888 , true);
        photoEditorView.getSource().setImageBitmap(filter.processFilter(filteredBitmap));
        finalBitmap = filteredBitmap.copy(Bitmap.Config.ARGB_8888 , true);
    }

    private void resetControl() {

        if (editImageFragment != null){
            setupEditImageFragment();
            brightnessFinal = 0;
            saturationFinal = 1.0f;
            constraintFinal = 1.0f;
            editImageFragment.resetControls();
        }


//        brightnessFinal = 0;
//        saturationFinal = 1.0f;
//        constraintFinal = 1.0f;
    }

    private void setupEditImageFragment() {

        View view = getLayoutInflater().inflate(R.layout.fragment_edit_image , null);

        editImageFragment.seekBar_brightness = (SeekBar)view.findViewById(R.id.seekBar_brightness);
        editImageFragment.seekBar_constraint = (SeekBar)view.findViewById(R.id.seekBar_constraint);
        editImageFragment.seekBar_saturation = (SeekBar)view.findViewById(R.id.seekBar_saturation);

        editImageFragment.seekBar_brightness.setMax(200);
        editImageFragment.seekBar_brightness.setProgress(100);

        editImageFragment.seekBar_constraint.setMax(200);
        editImageFragment.seekBar_constraint.setProgress(100);

        editImageFragment.seekBar_saturation.setMax(200);
        editImageFragment.seekBar_saturation.setProgress(100);
    }

    @Override
    public void onBrushSizeChangedListener(float size) {

        photoEditor.setBrushSize(size);
    }

    @Override
    public void onBrushOpacityChangedListener(int opacity) {

        photoEditor.setOpacity(opacity);
    }

    @Override
    public void onBrushColorChangedListener(int color) {

        photoEditor.setBrushColor(color);
    }

    @Override
    public void onBrushStateChangedListener(boolean isEraser) {

        if (isEraser)
            photoEditor.brushEraser();
        else
            photoEditor.setBrushDrawingMode(true);
    }

    @Override
    public void omEmojiSelected(String emoji) {

        photoEditor.addEmoji(emoji);
    }

    @Override
    public void onAddTextButtonClick(Typeface typeface, String text, int color) {

        photoEditor.addText(typeface , text , color);
    }

    @Override
    public void onAddFrame(int frame) {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources() , frame);
        photoEditor.addImage(bitmap);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    //Disconnect only when the application is closed!
    @Override
    protected void onDestroy() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    //Implement Google Dive events
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.d(TAG, "* API client connected !!!.");
        isAPÏConnected = true;
    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.d(TAG, "GoogleApiClient connection suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {

        Log.d(TAG, "GoogleApiClient connection failed: " + result.toString());
        //Toast.makeText(this, "GoogleApiClient connection failed: " + result.toString(), Toast.LENGTH_LONG).show();
        isAPÏConnected = false;
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }
        // Called typically when the app is not yet authorized, and authorization dialog is displayed to the user.
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        }
        catch (IntentSender.SendIntentException e) {
            Log.d(TAG, "Exception while starting resolution activity. " + e.getMessage());
        }
    }


    private void connectAPIClient(){
        if (mGoogleApiClient == null) {
            Log.d(TAG, "connectAPIClient().");
          
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        mGoogleApiClient.connect();
    }


}
