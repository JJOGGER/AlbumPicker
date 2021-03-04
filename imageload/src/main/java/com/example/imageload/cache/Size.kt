//package com.example.imageload.cache
//
//import android.annotation.SuppressLint
//import android.os.Parcel
//import android.os.Parcelable
//import androidx.annotation.Px
//
///**
// * Represents the target size of an image request.
// *
// * @see ImageRequest.Builder.size
// * @see SizeResolver.size
// */
//sealed class Size : Parcelable
//
///** Represents the width and height of the source image. */
//
//
//
///** A positive width and height in pixels. */
//
//data class PixelSize(
//    @Px val width: Int,
//    @Px val height: Int
//) : Size() {
//
//    constructor(parcel: Parcel) : this(
//        parcel.readInt(),
//        parcel.readInt()
//    ) {
//    }
//
//    init {
//        require(width > 0 && height > 0) { "width and height must be > 0." }
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        super.writeToParcel(parcel, flags)
//        parcel.writeInt(width)
//        parcel.writeInt(height)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<PixelSize> {
//        override fun createFromParcel(parcel: Parcel): PixelSize {
//            return PixelSize(parcel)
//        }
//
//        override fun newArray(size: Int): Array<PixelSize?> {
//            return arrayOfNulls(size)
//        }
//    }
//}
