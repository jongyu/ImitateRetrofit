package com.zhongyu.annotation

/**
 * @author 钟宇
 * @date 2020/3/24
 * @desc
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Query(val name: String = "") {
}