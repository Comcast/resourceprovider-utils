package com.xfinity.resourceprovider.sample;

import android.graphics.drawable.Drawable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Calendar;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MainPresenterTest {
    private static final String FORMATTED_STRING = "formattedString";
    private static final String FIRST_HALF_OF_MONTH = "firstHalfOfMonth";
    private static final String SECOND_HALF_OF_MONTH = "secondHalfOfMonth";

    @Mock MainView mainView;
    @Mock ResourceProvider resourceProvider;
    @Mock Drawable drawable;

    private MainPresenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(resourceProvider.getOneArgFormattedString(anyInt())).thenReturn(FORMATTED_STRING);
        when(resourceProvider.getFirstHalfOfMonth()).thenReturn(FIRST_HALF_OF_MONTH);
        when(resourceProvider.getSecondHalfOfMonth()).thenReturn(SECOND_HALF_OF_MONTH);
        when(resourceProvider.getIcnNavDino()).thenReturn(drawable);

        presenter = new MainPresenter(resourceProvider);
    }

    @Test
    public void drawable_presents_correctly() {
        presenter.setView(mainView);
        presenter.present();
        verify(resourceProvider).getIcnNavDino();
        verify(mainView).setDrawable(drawable);
    }

    @Test
    public void formatted_text_presents_correctly() {
        presenter.setView(mainView);
        presenter.present();

        verify(resourceProvider).getOneArgFormattedString(anyInt());
        verify(mainView).setFormattedText(FORMATTED_STRING);
    }


    @Test
    public void date_presents_correctly() {
        Calendar today = Calendar.getInstance();
        presenter.setView(mainView);
        presenter.present();

        if (today.get(Calendar.DAY_OF_MONTH) > 15) {
            verify(resourceProvider).getSecondHalfOfMonth();
            verify(mainView).setDateString(SECOND_HALF_OF_MONTH);
        } else {
            verify(resourceProvider).getFirstHalfOfMonth();
            verify(mainView).setDateString(FIRST_HALF_OF_MONTH);
        }
    }

}
