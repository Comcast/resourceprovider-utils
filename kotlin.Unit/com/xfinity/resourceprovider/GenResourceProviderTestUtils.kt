package com.xfinity.resourceprovider

import com.xfinity.resourceprovider.sample.ResourceProvider

fun ResourceProvider.mock() {
    whenever(this.strings).thenReturn(Mockito.mock(StringProvider::class.java, StringProviderAnswer()))
}
