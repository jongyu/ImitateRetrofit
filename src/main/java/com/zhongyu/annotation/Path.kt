package com.zhongyu.annotation

/**
 * @author 钟宇
 * @date 2020/3/24
 * @desc Path注解
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Path(val url: String) {
}