package com.xfinity.resourceprovider.sample

import android.graphics.drawable.Drawable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.Calendar

@RunWith(MockitoJUnitRunner.Silent::class)
class MainPresenterTest {
    @Mock
    private lateinit var mainView: MainView
    @Mock
    private lateinit var resourceProvider: ResourceProvider

    private lateinit var drawable: Drawable
    private lateinit var presenter: MainPresenter

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        resourceProvider.mock()
        presenter = MainPresenter(resourceProvider)
    }

    @Test
    fun drawable_presents_correctly() {
        presenter.setView(mainView)
        presenter.present()
        verify<MainView>(mainView).setDrawable(resourceProvider.drawables.icnNavDino)
    }

    @Test
    fun formatted_text_presents_correctly() {
        presenter.setView(mainView)
        presenter.present()

        verify<MainView>(mainView).setFormattedText(resourceProvider.strings.getOneArgFormattedString(anyInt()))
    }


    @Test
    fun date_presents_correctly() {
        val today = Calendar.getInstance()
        presenter.setView(mainView)
        presenter.present()

        if (today.get(Calendar.DAY_OF_MONTH) > 15) {
            verify<MainView>(mainView).setDateString(resourceProvider.strings.getSecondHalfOfMonth())
        } else {
            verify<MainView>(mainView).setDateString(resourceProvider.strings.getFirstHalfOfMonth())
        }
    }
}
