package com.example.imageload

class Test{
    companion object {
        /** 我是main入口函数 **/
        @JvmStatic
        fun main(args: Array<String>) {
            val items = setOf("apple", "banana", "kiwi")
            when {
                "banana" in items ->{}
                "apple" in items -> println("apple is fine too")
            }
        }
    }

}