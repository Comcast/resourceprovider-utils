package com.xfinity.resourceprovider.sample;

import android.graphics.Color;
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

    @Mock private MainView mainView;
    @Mock private ResourceProvider resourceProvider;
    @Mock private StringProvider stringProvider;
    @Mock private DrawableProvider drawableProvider;
    @Mock private DimensionProvider dimenProvider;
    @Mock private ColorProvider colorProvider;
    @Mock private IntegerProvider integerProvider;
    @Mock private Drawable drawable;

    private MainPresenter presenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(resourceProvider.getStrings()).thenReturn(stringProvider);
        when(resourceProvider.getDrawables()).thenReturn(drawableProvider);
        when(resourceProvider.getColors()).thenReturn(colorProvider);
        when(resourceProvider.getDimens()).thenReturn(dimenProvider);
        when(resourceProvider.getIntegers()).thenReturn(integerProvider);

        when(stringProvider.getOneArgFormattedString(anyInt())).thenReturn(FORMATTED_STRING);
        when(stringProvider.getFirstHalfOfMonth()).thenReturn(FIRST_HALF_OF_MONTH);
        when(stringProvider.getSecondHalfOfMonth()).thenReturn(SECOND_HALF_OF_MONTH);
        when(drawableProvider.getIcnNavDino()).thenReturn(drawable);
        when(dimenProvider.getTestDimenPixelSize()).thenReturn(42);
        when(colorProvider.getBabyBlue()).thenReturn(Color.BLUE);
        when(integerProvider.getTestInteger()).thenReturn(42);

        presenter = new MainPresenter(resourceProvider);
    }

    @Test
    public void drawable_presents_correctly() {
        presenter.setView(mainView);
        presenter.present();
        verify(drawableProvider).getIcnNavDino();
        verify(mainView).setDrawable(drawable);
    }

    @Test
    public void formatted_text_presents_correctly() {
        presenter.setView(mainView);
        presenter.present();

        verify(stringProvider).getOneArgFormattedString(anyInt());
        verify(mainView).setFormattedText(FORMATTED_STRING);
    }


    @Test
    public void date_presents_correctly() {
        Calendar today = Calendar.getInstance();
        presenter.setView(mainView);
        presenter.present();

        if (today.get(Calendar.DAY_OF_MONTH) > 15) {
            verify(stringProvider).getSecondHalfOfMonth();
            verify(mainView).setDateString(SECOND_HALF_OF_MONTH);
        } else {
            verify(stringProvider).getFirstHalfOfMonth();
            verify(mainView).setDateString(FIRST_HALF_OF_MONTH);
        }
    }

}
