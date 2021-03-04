//package com.example.imageload.request
//
//import android.content.ContentValues
//import android.util.Log
//import java.util.concurrent.BlockingQueue
//import java.util.concurrent.PriorityBlockingQueue
//import java.util.concurrent.atomic.AtomicInteger
//
//
//class ImageRequestQueue {
//    /*
//     * 阻塞式队列
//     * 多线程共享
//     * 生产效率和消费效率相差太远
//     * disPlayImage()
//     * 使用优先级队列
//     * 优先级高的队列先被消费
//     * 每一个产品都有编号
//     */
//    private val mRequestQueue: BlockingQueue<BitmapRequest> =
//        PriorityBlockingQueue<BitmapRequest>()
//
//    //转发器的数量
//    private var threadCount = 0
//
//    //线程安全的int值
//    private val i =
//        AtomicInteger(0)
//
//    //一组转发器
//    private var mRequestDispatchers: Array<RequestDispatcher?>
//
//    fun RequestQueue(threadCount: Int) {
//        this.threadCount = threadCount
//    }
//
//    /**
//     * 添加请求对象
//     * @param bitmapRequest
//     */
//    fun addRequest(bitmapRequest: BitmapRequest) {
//        //判断队列中是否有请求对象
//        if (!mRequestQueue.contains(bitmapRequest)) {
//            //给请求进行编号
//            try {
//                bitmapRequest.setSerialNumber(i.incrementAndGet())
//                mRequestQueue.put(bitmapRequest)
//            } catch (e: InterruptedException) {
//                e.printStackTrace()
//            }
//        } else {
//            Log.i(ContentValues.TAG, "请求已经存在 编号： " + bitmapRequest.getSerialNumber())
//        }
//    }
//
//    /**
//     * 开启请求
//     */
//    fun start() {
//        //先停止
//        stop()
//        startDispatchers()
//    }
//
//    /**
//     * 开启转发器
//     */
//    private fun startDispatchers() {
//        mRequestDispatchers = arrayOfNulls<RequestDispatcher>(threadCount)
//        for (i in 0 until threadCount) {
//            val p = RaequestDispatcher(mRequestQueue)
//            mRequestDispatchers[i] = p
//            p.start()
//        }
//    }
//
//    /**
//     * 停止
//     */
//    fun stop() {}
//}