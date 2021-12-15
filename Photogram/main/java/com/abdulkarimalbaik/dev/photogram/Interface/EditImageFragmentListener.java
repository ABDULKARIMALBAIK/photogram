package com.abdulkarimalbaik.dev.photogram.Interface;

public interface EditImageFragmentListener {

    void onBrightnessChanged(int brightness);
    void onSaturationCanged(float saturation);
    void onConstraintChanged(float constraint);
    void onEditStarted();
    void onEditComplete();
}
