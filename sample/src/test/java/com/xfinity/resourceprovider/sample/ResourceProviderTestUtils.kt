package com.xfinity.resourceprovider.sample

import com.nhaarman.mockito_kotlin.whenever
import com.xfinitymobile.resourceprovider.testutils.DrawableProviderAnswer
import com.xfinitymobile.resourceprovider.testutils.IntegerProviderAnswer
import com.xfinitymobile.resourceprovider.testutils.StringProviderAnswer
import org.mockito.Mockito

//fun ResourceProvider.mock() {
//    this.mockStrings()
//    this.mockDrawables()
//    this.mockDimens()
//    this.mockColors()
//    this.mockIntegers()
//}

fun ResourceProvider.mockStrings() {
    whenever(this.strings).thenReturn(Mockito.mock(StringProvider::class.java, StringProviderAnswer()))
}

fun ResourceProvider.mockDrawables() {
    whenever(this.drawables).thenReturn(Mockito.mock(DrawableProvider::class.java, DrawableProviderAnswer()))
}

fun ResourceProvider.mockColors() {
    whenever(this.colors).thenReturn(Mockito.mock(ColorProvider::class.java, IntegerProviderAnswer()))
}

fun ResourceProvider.mockDimens() {
    whenever(this.dimens).thenReturn(Mockito.mock(DimensionProvider::class.java, IntegerProviderAnswer()))
}

fun ResourceProvider.mockIntegers() {
    whenever(this.integers).thenReturn(Mockito.mock(IntegerProvider::class.java, IntegerProviderAnswer()))
}
