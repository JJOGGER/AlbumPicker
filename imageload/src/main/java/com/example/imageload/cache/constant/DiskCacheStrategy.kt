package com.example.imageload.cache.constant


object DiskCacheStrategy {
    /**
     * 不缓存到磁盘
     */
    const val NONE = 0
    /**
     * 只缓存原图，当前demo之在本地获取图片，所以暂不实现此缓存功能
     */
    const val SOURCE = 1
    /**
     * 只缓存结果
     */
    const val RESULT = 2
    /**
     * 既缓存原图，也缓存结果
     */
    const val ALL = 3
}
