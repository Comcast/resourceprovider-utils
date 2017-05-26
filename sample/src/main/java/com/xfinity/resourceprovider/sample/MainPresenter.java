package com.xfinity.resourceprovider.sample;

import java.util.Calendar;

public class MainPresenter {
    private static final int FORMAT_ARG = 10000;
    private static final int MONTH_HALFWAY_POINT = 15;

    private final ResourceProvider resourceProvider;
    private MainView mainView;

    public MainPresenter(ResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }
    
    public void setView(MainView mainView) {
        this.mainView = mainView;
    }

    public void present() {
        mainView.setFormattedText(resourceProvider.getOneArgFormattedString(FORMAT_ARG));

        Calendar today = Calendar.getInstance();
        if (today.get(Calendar.DAY_OF_MONTH) > MONTH_HALFWAY_POINT) {
            mainView.setDateString(resourceProvider.getSecondHalfOfMonth());
        } else {
            mainView.setDateString(resourceProvider.getFirstHalfOfMonth());
        }
    }
}
