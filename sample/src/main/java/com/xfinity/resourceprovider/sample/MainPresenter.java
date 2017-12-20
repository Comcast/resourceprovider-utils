package com.xfinity.resourceprovider.sample;

import java.util.Calendar;

class MainPresenter {
    private static final int FORMAT_ARG = 10000;
    private static final int MONTH_HALFWAY_POINT = 15;

    private final ResourceProvider resourceProvider;
    private MainView mainView;

    MainPresenter(ResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }
    
    void setView(MainView mainView) {
        this.mainView = mainView;
    }

    void present() {
        mainView.setFormattedText(resourceProvider.getOneArgFormattedString(FORMAT_ARG));

        Calendar today = Calendar.getInstance();
        if (today.get(Calendar.DAY_OF_MONTH) > MONTH_HALFWAY_POINT) {
            mainView.setDateString(resourceProvider.getSecondHalfOfMonth());
        } else {
            mainView.setDateString(resourceProvider.getFirstHalfOfMonth());
        }

        int dayOfWeek = today.get(Calendar.DAY_OF_WEEK);
        int daysUntilFriday = Calendar.FRIDAY - dayOfWeek;
        if (daysUntilFriday >= 0) {
            mainView.setPluralsString(resourceProvider.getDaysUntilFridayQuantityString(daysUntilFriday, daysUntilFriday));
        } else {
            mainView.setPluralsString(resourceProvider.getSaturday());
        }

        mainView.setDrawable(resourceProvider.getIcnNavDino());
        mainView.setDimenText("The Test Dimen is " + resourceProvider.getTestDimenPixelSize() + " in pixels");
        mainView.setIntegerText("The Test Integer is " + resourceProvider.getTestInteger());
        mainView.setColorViewBackgroundColor(resourceProvider.getBabyBlue());
    }
}
