package com.zhongyu.annotation

/**
 * @author 钟宇
 * @date 2020/3/24
 * @desc
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Get(val url: String)