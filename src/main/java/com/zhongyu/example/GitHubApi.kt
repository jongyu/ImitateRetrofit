package com.zhongyu.example

import com.zhongyu.annotation.Api
import com.zhongyu.annotation.Get
import com.zhongyu.bean.User

/**
 * @author 钟宇
 * @date 2020/3/24
 * @desc
 */
@Api("https://api.github.com")
interface GitHubApi {

    @Api("users")
    interface Users {

        @Get("{name}")
        fun get(name: String): User

        @Get("{name}/followers")
        fun followers(name: String): List<User>

    }

}