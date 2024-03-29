package com.thomas.apps.dailywallpaper.utils.network

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class NullOnEmptyConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<ResponseBody, *> {

        val delegate = retrofit.nextResponseBodyConverter<Any>(this, type, annotations)

        return Converter {
            if (it.contentLength() == 0L) {
                return@Converter null
            } else {
                return@Converter delegate.convert(it)
            }
        }
    }
}