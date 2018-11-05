package com.xfinity.resourceprovider.testutils

import android.graphics.drawable.Drawable
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import java.lang.reflect.Method

/**
 * This file contains Mockito Answer sub-classes for automatically mocking ResourceProvider function calls.
 * Refer to the sample application MainPresenterTest and ResourceProviderTestUtils classes for usage examples
 */

class DrawableProviderAnswer : Answer<Any?> {
    private val delegate = Mockito.RETURNS_DEFAULTS!!
    private val drawableMap = mutableMapOf<Method, Drawable>()

    override fun answer(invocation: InvocationOnMock?): Any? {
        val invocationMethodReturn = invocation?.method?.returnType
        val drawableType = Drawable::class.java
        return when (invocationMethodReturn) {
            drawableType -> {
                val mappedMock = drawableMap.get(invocation.method)
                if (mappedMock != null) {
                    mappedMock
                } else {
                    val mock = Mockito.mock(Drawable::class.java)
                    drawableMap.put(invocation.method, mock)
                    mock
                }
            }
            else -> delegate.answer(invocation)
        }
    }
}

class StringProviderAnswer : Answer<Any?> {
    private val delegate = Mockito.RETURNS_DEFAULTS!!

    override fun answer(invocation: InvocationOnMock?): Any? {
        invocation?.method?.isVarArgs
        val invocationMethodReturn = invocation?.method?.returnType
        val stringType = String::class.java
        return when (invocationMethodReturn) {
            stringType -> invocation.method?.name.toString() + invocation.arguments.joinToString()
            else -> delegate.answer(invocation)
        }
    }

}

class IntegerProviderAnswer : Answer<Any?> {
    private val delegate = Mockito.RETURNS_DEFAULTS!!

    override fun answer(invocation: InvocationOnMock?): Any? {
        val invocationMethodReturn = invocation?.method?.returnType
        val intType = Int::class.java
        return when (invocationMethodReturn) {
            intType -> invocation.method?.name?.hashCode()
            else -> delegate.answer(invocation)
        }
    }
}