package com.xfinity.resourceprovider.sample;

import android.graphics.drawable.Drawable;

interface MainView {
    void setFormattedText(String formattedText);
    void setDateString(String dateString);
    void setPluralsString(String pluralsString);
    void setDrawable(Drawable drawable);
}
