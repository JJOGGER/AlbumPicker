import android.widget.ImageView
import com.example.imageload.loader.ImageLoader
import com.example.imageload.loader.LoaderManager
import com.example.imageload.request.ImageRequest
import com.github.chrisbanes.photoview.PhotoView

//@file:JvmName("ImageViews")
//@file:Suppress("unused")
//
//package com.example.imageload
//
//import android.graphics.Bitmap
//import android.graphics.drawable.Drawable
//import android.net.Uri
//import android.widget.ImageView
//import androidx.annotation.DrawableRes
//import com.example.imageload.loader.ImageLoader
//import com.example.imageload.request.ImageRequest
//import java.io.File
//
//
///** @see ImageView.loadAny */
@JvmSynthetic
inline fun ImageView.load(
    uri: String?,
    imageLoader: ImageLoader = LoaderManager.getImageLoader(context),
    builder: ImageRequest.Builder.() -> Unit = {}
) = loadAny(uri, imageLoader, builder)

//
///** @see ImageView.loadAny */
//@JvmSynthetic
//inline fun ImageView.load(
//    url: HttpUrl?,
//    imageLoader: ImageLoader = context.imageLoader,
//    builder: ImageRequest.Builder.() -> Unit = {}
//): Disposable = loadAny(url, imageLoader, builder)
//
///** @see ImageView.loadAny */
//@JvmSynthetic
//inline fun ImageView.load(
//    uri: Uri?,
//    imageLoader: ImageLoader = context.imageLoader,
//    builder: ImageRequest.Builder.() -> Unit = {}
//): Disposable = loadAny(uri, imageLoader, builder)
//
///** @see ImageView.loadAny */
//@JvmSynthetic
//inline fun ImageView.load(
//    file: File?,
//    imageLoader: ImageLoader = context.imageLoader,
//    builder: ImageRequest.Builder.() -> Unit = {}
//): Disposable = loadAny(file, imageLoader, builder)
//
///** @see ImageView.loadAny */
//@JvmSynthetic
//inline fun ImageView.load(
//    @DrawableRes drawableResId: Int,
//    imageLoader: ImageLoader = context.imageLoader,
//    builder: ImageRequest.Builder.() -> Unit = {}
//): Disposable = loadAny(drawableResId, imageLoader, builder)
//
///** @see ImageView.loadAny */
//@JvmSynthetic
//inline fun ImageView.load(
//    drawable: Drawable?,
//    imageLoader: ImageLoader = context.imageLoader,
//    builder: ImageRequest.Builder.() -> Unit = {}
//): Disposable = loadAny(drawable, imageLoader, builder)
//
///** @see ImageView.loadAny */
//@JvmSynthetic
//inline fun ImageView.load(
//    bitmap: Bitmap?,
//    imageLoader: ImageLoader = context.imageLoader,
//    builder: ImageRequest.Builder.() -> Unit = {}
//): Disposable = loadAny(bitmap, imageLoader, builder)
//
///**
// * Load the image referenced by [data] and set it on this [ImageView].
// *
// * [ImageView.loadAny] is the type-unsafe version of [ImageView.load].
// *
// * Example:
// * ```
// * imageView.load("https://www.example.com/image.jpg") {
// *     crossfade(true)
// *     transformations(CircleCropTransformation())
// * }
// * ```
// *
// * @param data The data to load.
// * @param imageLoader The [ImageLoader] that will be used to enqueue the [ImageRequest].
// * @param builder An optional lambda to configure the request before it is enqueued.
// */

@JvmSynthetic
inline fun ImageView.loadAny(
    data: Any?,
    imageLoader: ImageLoader = LoaderManager.getImageLoader(context),
    builder: ImageRequest.Builder.() -> Unit = {}
) {
    val request = ImageRequest.Builder(context)
        .data(data)
        .target(this)
        .apply(builder)
        .build()
    return imageLoader.load(request)
}
//
///**
// * Cancel any in progress requests and clear all resources associated with this [ImageView].
// */
//fun ImageView.clear() {
//    CoilUtils.clear(this)
//}
//
///**
// * Get the metadata of the successful request attached to this view.
// */
//val ImageView.metadata: ImageResult.Metadata?
//    @JvmName("metadata") get() = CoilUtils.metadata(this)
