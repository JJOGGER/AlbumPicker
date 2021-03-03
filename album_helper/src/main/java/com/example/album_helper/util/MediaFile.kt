package com.example.album_helper.util

import java.util.*

object MediaFile{
    const val FILE_TYPE_MP3 = 1
    const val FILE_TYPE_M4A = 2
    const val FILE_TYPE_WAV = 3
    const val FILE_TYPE_AMR = 4
    const val FILE_TYPE_AWB = 5
    const val FILE_TYPE_WMA = 6
    const val FILE_TYPE_OGG = 7
    const val FILE_TYPE_AAC = 8
    const val FILE_TYPE_MKA = 9
    const val FILE_TYPE_FLAC = 10
    private const  val FIRST_AUDIO_FILE_TYPE = 1
    private const  val LAST_AUDIO_FILE_TYPE = 10
    const val FILE_TYPE_MID = 11
    const val FILE_TYPE_SMF = 12
    const val FILE_TYPE_IMY = 13
    private const val FIRST_MIDI_FILE_TYPE = 11
    private const val LAST_MIDI_FILE_TYPE = 13
    const val FILE_TYPE_MP4 = 21
    const val FILE_TYPE_M4V = 22
    const val FILE_TYPE_3GPP = 23
    const val FILE_TYPE_3GPP2 = 24
    const val FILE_TYPE_WMV = 25
    const val FILE_TYPE_ASF = 26
    const val FILE_TYPE_MKV = 27
    const val FILE_TYPE_MP2TS = 28
    const val FILE_TYPE_AVI = 29
    const val FILE_TYPE_WEBM = 30
    private const val FIRST_VIDEO_FILE_TYPE = 21
    private const val LAST_VIDEO_FILE_TYPE = 30
    const val FILE_TYPE_MP2PS = 200
    private const val FIRST_VIDEO_FILE_TYPE2 = 200
    private const val LAST_VIDEO_FILE_TYPE2 = 200
    const val FILE_TYPE_JPEG = 31
    const val FILE_TYPE_GIF = 32
    const val FILE_TYPE_PNG = 33
    const val FILE_TYPE_BMP = 34
    const val FILE_TYPE_WBMP = 35
    const val FILE_TYPE_WEBP = 36
    const val FILE_TYPE_HEIC = 37
    private const val FIRST_IMAGE_FILE_TYPE = 31
    private const val LAST_IMAGE_FILE_TYPE = 36
    const val FILE_TYPE_M3U = 41
    const val FILE_TYPE_PLS = 42
    const val FILE_TYPE_WPL = 43
    const val FILE_TYPE_HTTPLIVE = 44
    private const val FIRST_PLAYLIST_FILE_TYPE = 41
    private const val LAST_PLAYLIST_FILE_TYPE = 44
    const val FILE_TYPE_FL = 51
    private const val FIRST_DRM_FILE_TYPE = 51
    private const val LAST_DRM_FILE_TYPE = 51
    const val FILE_TYPE_TEXT = 100
    const val FILE_TYPE_HTML = 101
    const val FILE_TYPE_PDF = 102
    const val FILE_TYPE_XML = 103
    const val FILE_TYPE_MS_WORD = 104
    const val FILE_TYPE_MS_EXCEL = 105
    const val FILE_TYPE_MS_POWERPOINT = 106
    const val FILE_TYPE_ZIP = 107
    private val sFileTypeMap: HashMap<String?, MediaFileType?> =
        hashMapOf()
    private val sMimeTypeMap: HashMap<String?, Int?> =
       hashMapOf()

    fun MediaFile() {}

    fun addFileType(
        extension: String?,
        fileType: Int,
        mimeType: String?
    ) {
        sFileTypeMap[extension] = MediaFileType(fileType, mimeType)
        sMimeTypeMap[mimeType] = fileType
    }

    fun isAudioFileType(fileType: Int): Boolean {
        return fileType >= 1 && fileType <= 10 || fileType >= 11 && fileType <= 13
    }

    fun isVideoFileType(fileType: Int): Boolean {
        return fileType >= 21 && fileType <= 30 || fileType >= 200 && fileType <= 200
    }

    fun isImageFileType(fileType: Int): Boolean {
        return fileType >= 31 && fileType <= 36
    }

    fun isPlayListFileType(fileType: Int): Boolean {
        return fileType >= 41 && fileType <= 44
    }

    fun isDrmFileType(fileType: Int): Boolean {
        return fileType >= 51 && fileType <= 51
    }

    fun getFileType(path: String): MediaFileType? {
        val lastDot = path.lastIndexOf(46.toChar())
        return if (lastDot < 0) null else sFileTypeMap[path.substring(lastDot + 1)
            .toUpperCase(Locale.ROOT)]
    }

    fun isMimeTypeMedia(mimeType: String?): Boolean {
        val fileType = getFileTypeForMimeType(mimeType)
        return isAudioFileType(fileType) || isVideoFileType(fileType) || isImageFileType(fileType) || isPlayListFileType(
            fileType
        )
    }

    fun getFileTitle(path: String): String? {
        var path = path
        var lastSlash = path.lastIndexOf(47.toChar())
        if (lastSlash >= 0) {
            ++lastSlash
            if (lastSlash < path.length) {
                path = path.substring(lastSlash)
            }
        }
        val lastDot = path.lastIndexOf(46.toChar())
        if (lastDot > 0) {
            path = path.substring(0, lastDot)
        }
        return path
    }

    fun getFileTypeForMimeType(mimeType: String?): Int {
        return sMimeTypeMap[mimeType] ?: 0
    }

    fun getMimeTypeForFile(path: String): String? {
        val mediaFileType = getFileType(path)
        return mediaFileType?.mimeType
    }

    init {
        addFileType("MP3", 1, "audio/mpeg")
        addFileType("MPGA", 1, "audio/mpeg")
        addFileType("M4A", 2, "audio/mp4")
        addFileType("WAV", 3, "audio/x-wav")
        addFileType("AMR", 4, "audio/amr")
        addFileType("AWB", 5, "audio/amr-wb")
        addFileType("WMA", 6, "audio/x-ms-wma")
        addFileType("OGG", 7, "audio/ogg")
        addFileType("OGG", 7, "application/ogg")
        addFileType("OGA", 7, "application/ogg")
        addFileType("AAC", 8, "audio/aac")
        addFileType("AAC", 8, "audio/aac-adts")
        addFileType("MKA", 9, "audio/x-matroska")
        addFileType("MID", 11, "audio/midi")
        addFileType("MIDI", 11, "audio/midi")
        addFileType("XMF", 11, "audio/midi")
        addFileType("RTTTL", 11, "audio/midi")
        addFileType("SMF", 12, "audio/sp-midi")
        addFileType("IMY", 13, "audio/imelody")
        addFileType("RTX", 11, "audio/midi")
        addFileType("OTA", 11, "audio/midi")
        addFileType("MXMF", 11, "audio/midi")
        addFileType("MPEG", 21, "video/mpeg")
        addFileType("MPG", 21, "video/mpeg")
        addFileType("MP4", 21, "video/mp4")
        addFileType("M4V", 22, "video/mp4")
        addFileType("3GP", 23, "video/3gpp")
        addFileType("3GPP", 23, "video/3gpp")
        addFileType("3G2", 24, "video/3gpp2")
        addFileType("3GPP2", 24, "video/3gpp2")
        addFileType("MKV", 27, "video/x-matroska")
        addFileType("WEBM", 30, "video/webm")
        addFileType("TS", 28, "video/mp2ts")
        addFileType("AVI", 29, "video/avi")
        addFileType("WMV", 25, "video/x-ms-wmv")
        addFileType("ASF", 26, "video/x-ms-asf")
        addFileType("JPG", 31, "image/jpg")
        addFileType("JPEG", 31, "image/jpeg")
        addFileType("GIF", 32, "image/gif")
        addFileType("PNG", 33, "image/png")
        addFileType("BMP", 34, "image/x-ms-bmp")
        addFileType("WBMP", 35, "image/vnd.wap.wbmp")
        addFileType("WEBP", 36, "image/webp")
        addFileType("heic", 37, "image/heif")
        addFileType("M3U", 41, "audio/x-mpegurl")
        addFileType("M3U", 41, "application/x-mpegurl")
        addFileType("PLS", 42, "audio/x-scpls")
        addFileType("WPL", 43, "application/vnd.ms-wpl")
        addFileType("M3U8", 44, "application/vnd.apple.mpegurl")
        addFileType("M3U8", 44, "audio/mpegurl")
        addFileType("M3U8", 44, "audio/x-mpegurl")
        addFileType("FL", 51, "application/x-android-drm-fl")
        addFileType("TXT", 100, "text/plain")
        addFileType("HTM", 101, "text/html")
        addFileType("HTML", 101, "text/html")
        addFileType("PDF", 102, "application/pdf")
        addFileType("DOC", 104, "application/msword")
        addFileType("XLS", 105, "application/vnd.ms-excel")
        addFileType("PPT", 106, "application/mspowerpoint")
        addFileType("FLAC", 10, "audio/flac")
        addFileType("ZIP", 107, "application/zip")
        addFileType("MPG", 200, "video/mp2p")
        addFileType("MPEG", 200, "video/mp2p")

    }

    class MediaFileType internal constructor(val fileType: Int, val mimeType: String?)
}