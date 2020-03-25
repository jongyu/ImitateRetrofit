package com.zhongyu.example

import com.google.gson.Gson
import com.google.gson.stream.JsonReader
import com.zhongyu.annotation.Api
import com.zhongyu.annotation.Get
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.reflect.Proxy
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.valueParameters

/**
 * @author 钟宇
 * @date 2020/3/25
 * @desc
 */
object RetroApi {

    const val PATH_PATTERN = """(\{(\w+)\})"""
    val okHttp = OkHttpClient()
    val gson = Gson()

    val enclosing = { cls: Class<*> ->
        var currentCls: Class<*>? = cls
        sequence {
            while (currentCls != null) {
//                yield(currentCls)
//                currentCls = currentCls?.enclosingClass
                currentCls = currentCls?.also { yield(it) }?.enclosingClass
            }
        }
    }

    inline fun <reified T> create(): T {
        val functionMap = T::class.functions.map { it.name to it }.toMap()
        val interfaces = enclosing(T::class.java).takeWhile { it.isInterface }.toList()
        val apiPath = interfaces.foldRight(StringBuilder()) { clazz, acc ->
            acc.append(clazz.getAnnotation(Api::class.java)?.url?.takeIf { it.isNotEmpty() } ?: clazz.name).append("/")
        }.toString()

        return Proxy.newProxyInstance(RetroApi.javaClass.classLoader, arrayOf(T::class.java)) { _, method, args ->
            functionMap[method.name]?.takeIf { it.isAbstract }?.let { function ->
                val parameterMap = function.valueParameters.map {
                    it.name to args[it.index - 1]
                }.toMap()
                val endPoint = function.findAnnotation<Get>()!!.url.takeIf { it.isNotEmpty() } ?: function.name
                val compiledEndPoint = Regex(PATH_PATTERN).findAll(endPoint).map { matchResult ->
                    matchResult.groups[1]!!.range to parameterMap[matchResult.groups[2]!!.value]
                }.fold(endPoint) { acc, pair ->
                    acc.replaceRange(pair.first, pair.second.toString())
                }
                val url = apiPath + compiledEndPoint
                println(url)
                okHttp.newCall(Request.Builder().url(url).get().build()).execute().body?.charStream()?.use {
                    gson.fromJson(JsonReader(it), method.genericReturnType)
                }
            }
        } as T
    }

}

fun main() {
    val userApi = RetroApi.create<GitHubApi.Users>()
    println(userApi.get("yuowo"))
    println(userApi.followers("yuowo").map { it.login })
}