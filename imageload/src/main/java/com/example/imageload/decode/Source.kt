package com.example.imageload.decode

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Rect
import android.net.Uri
import com.example.imageload.request.ImageRequest
import com.example.imageload.util.context
import java.io.*

internal abstract class Source : Closeable {
    internal abstract val magic: Int
    internal abstract val orientation: Int
    internal abstract val data: ByteArray
    var magicNum = 0

    @Throws(IOException::class)
    internal abstract fun decode(options: BitmapFactory.Options): Bitmap?

    @Throws(IOException::class)
    internal abstract fun decodeRegion(rect: Rect, options: BitmapFactory.Options): Bitmap?

    internal class FileSource @Throws(IOException::class)
    constructor(private val file: File) : Source() {
        private val accessFile: RandomAccessFile = RandomAccessFile(file, "r")
        private val fd: FileDescriptor

        init {
            fd = accessFile.fd
        }

        override val magic: Int
            @Throws(IOException::class)
            get() {
                if (magicNum == 0) {
                    magicNum = accessFile.readInt()
                    accessFile.seek(0)
                }
                return magicNum
            }

        override val orientation: Int
            @Throws(IOException::class)
            get() = HeaderParser.getOrientation(FileInputStream(file))

        override val data: ByteArray
            @Throws(IOException::class)
            get() = ByteArrayPool.loadData(FileInputStream(file))

        override fun decode(options: BitmapFactory.Options): Bitmap? {
            return BitmapFactory.decodeFileDescriptor(fd, null, options)
        }

        @Throws(IOException::class)
        override fun decodeRegion(rect: Rect, options: BitmapFactory.Options): Bitmap? {
            return BitmapRegionDecoder.newInstance(fd, false).decodeRegion(rect, options)
        }

        @Throws(IOException::class)
        override fun close() {
            accessFile.close()
        }
    }

    internal class AssetSource(private val assetStream: AssetManager.AssetInputStream) : Source() {
        override val magic: Int
            @Throws(IOException::class)
            get() {
                if (magicNum == 0) {
                    assetStream.mark(4)
                    magicNum = HeaderParser.readInt(assetStream)
                    assetStream.reset()
                }
                return magicNum
            }

        override val orientation: Int
            @Throws(IOException::class)
            get() = HeaderParser.getOrientation(assetStream)

        override val data: ByteArray
            @Throws(IOException::class)
            get() = ByteArrayPool.loadData(assetStream)

        @Throws(IOException::class)
        override fun decode(options: BitmapFactory.Options): Bitmap? {
            if (options.inJustDecodeBounds) {
                assetStream.mark(Integer.MAX_VALUE)
                val bitmap = BitmapFactory.decodeStream(assetStream, null, options)
                assetStream.reset()
                return bitmap
            }
            return BitmapFactory.decodeStream(assetStream, null, options)
        }

        @Throws(IOException::class)
        override fun decodeRegion(rect: Rect, options: BitmapFactory.Options): Bitmap? {
            return BitmapRegionDecoder.newInstance(assetStream, false).decodeRegion(rect, options)
        }

        @Throws(IOException::class)
        override fun close() {
            assetStream.close()
        }
    }

    internal class StreamSource @Throws(IOException::class)
    constructor(inputStream: InputStream) : Source() {
        private val inputStream: RecycledInputStream = RecycledInputStream(inputStream)

        override val magic: Int
            @Throws(IOException::class)
            get() {
                if (magicNum == 0) {
                    magicNum = HeaderParser.readInt(inputStream)
                    inputStream.rewind()
                }
                return magicNum
            }

        override val orientation: Int
            @Throws(IOException::class)
            get() = HeaderParser.getOrientation(inputStream)

        override val data: ByteArray
            @Throws(IOException::class)
            get() = ByteArrayPool.loadData(inputStream)

        @Throws(IOException::class)
        override fun decode(options: BitmapFactory.Options): Bitmap? {
            if (options.inTempStorage == null) {
                options.inTempStorage = ByteArrayPool.basicArray
            }
            return if (options.inJustDecodeBounds) {
                inputStream.mark(Integer.MAX_VALUE)
                val bitmap = BitmapFactory.decodeStream(inputStream, null, options)
                inputStream.rewind()
                bitmap
            } else {
                BitmapFactory.decodeStream(inputStream, null, options)
            }
        }

        @Throws(IOException::class)
        override fun decodeRegion(rect: Rect, options: BitmapFactory.Options): Bitmap? {
            return BitmapFactory.decodeStream(inputStream, null, options)
        }

        @Throws(IOException::class)
        override fun close() {
            inputStream.close()
        }
    }

    companion object {
        private const val ASSET_PREFIX = "file:///android_asset/"
        private const val ASSET_PREFIX_LENGTH = ASSET_PREFIX.length
        private const val FILE_PREFIX = "file://"
        private const val FILE_PREFIX_LENGTH = FILE_PREFIX.length

        /**
         * wrap source
         *
         * @param src source
         * @return wrapped source
         */
        @Throws(IOException::class)
        fun valueOf(src: Any?): Source {
            if (src == null) {
                throw IllegalArgumentException("source is null")
            }
            return when (src) {
                is File -> FileSource(src)
                is AssetManager.AssetInputStream -> AssetSource(src)
                is InputStream -> StreamSource(src)
                else -> throw IllegalArgumentException("unsupported source " + src.javaClass.simpleName)
            }
        }

        @Throws(IOException::class)
        fun parse(request: ImageRequest): Source {
            val data = request.data
            when (data) {
                is String -> {
                    return when {
                        data.startsWith(ASSET_PREFIX) -> valueOf(
                            context.assets.open(
                                data.substring(
                                    ASSET_PREFIX_LENGTH
                                )
                            )
                        )
                        data.startsWith(FILE_PREFIX) -> valueOf(
                            File(
                                data.substring(
                                    FILE_PREFIX_LENGTH
                                )
                            )
                        )
                        else -> valueOf(
                            context.contentResolver.openInputStream(
                                (Uri.parse(data))
                            )
                        )
                    }
                }
                is Uri -> {
                    context.contentResolver.openInputStream(data)
                }
            }
            return valueOf(null)
        }
    } // end of companion

}

