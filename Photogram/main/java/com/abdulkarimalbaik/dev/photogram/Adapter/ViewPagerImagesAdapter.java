package com.abdulkarimalbaik.dev.photogram.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abdulkarimalbaik.dev.photogram.Interface.ViewPagerItemListener;
import com.abdulkarimalbaik.dev.photogram.Model.CipherImage;
import com.abdulkarimalbaik.dev.photogram.Model.DecryptImage;
import com.abdulkarimalbaik.dev.photogram.R;
import com.abdulkarimalbaik.dev.photogram.Utils.BitmapUtils;
import com.abdulkarimalbaik.dev.photogram.Utils.Common;
import com.abdulkarimalbaik.dev.photogram.Utils.EncryptDecryptUtils;
import com.github.chrisbanes.photoview.PhotoView;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

public class ViewPagerImagesAdapter extends PagerAdapter {

    Context context;
    List<CipherImage> cipherImages;
    LayoutInflater inflater;

    public ViewPagerImagesAdapter(Context context, List<CipherImage> cipherImages) {
        this.context = context;
        this.cipherImages = cipherImages;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return cipherImages.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View image_layout = inflater.inflate(R.layout.view_pager_item , container , false);

        PhotoView page_image = (PhotoView)image_layout.findViewById(R.id.page_image);

        try {
            page_image.setImageBitmap(
                    BitmapUtils.getBitmapFromByte(
                            new EncryptDecryptUtils().decryptImage(
                                    cipherImages.get(position).getPicture() , cipherImages.get(position).getLength_encrypt_image()
                            )
                    )
            );

        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        container.addView(image_layout);
        return image_layout;
    }


}
