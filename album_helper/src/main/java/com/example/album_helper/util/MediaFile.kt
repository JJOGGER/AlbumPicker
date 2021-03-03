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
        return fileType >= FIRST_AUDIO_FILE_TYPE  && fileType <= LAST_AUDIO_FILE_TYPE  || fileType >= FIRST_MIDI_FILE_TYPE  && fileType <= LAST_MIDI_FILE_TYPE
    }

    fun isVideoFileType(fileType: Int): Boolean {
        return fileType >= FIRST_VIDEO_FILE_TYPE  && fileType <= LAST_VIDEO_FILE_TYPE  || fileType >= FIRST_VIDEO_FILE_TYPE2  && fileType <= LAST_VIDEO_FILE_TYPE2
    }

    fun isImageFileType(fileType: Int): Boolean {
        return fileType >= FIRST_IMAGE_FILE_TYPE  && fileType <= LAST_IMAGE_FILE_TYPE
    }

    fun isPlayListFileType(fileType: Int): Boolean {
        return fileType >= FIRST_PLAYLIST_FILE_TYPE  && fileType <= LAST_PLAYLIST_FILE_TYPE
    }

    fun isDrmFileType(fileType: Int): Boolean {
        return fileType >= FIRST_DRM_FILE_TYPE  && fileType <= LAST_DRM_FILE_TYPE
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
        addFileType("MP3", FILE_TYPE_MP3, "audio/mpeg")
        addFileType("MPGA", FILE_TYPE_MP3, "audio/mpeg")
        addFileType("M4A", FILE_TYPE_M4A , "audio/mp4")
        addFileType("WAV", FILE_TYPE_WAV , "audio/x-wav")
        addFileType("AMR", FILE_TYPE_AMR , "audio/amr")
        addFileType("AWB", FILE_TYPE_AWB , "audio/amr-wb")
        addFileType("WMA", FILE_TYPE_WMA , "audio/x-ms-wma")
        addFileType("OGG", FILE_TYPE_OGG , "audio/ogg")
        addFileType("OGG", FILE_TYPE_OGG , "application/ogg")
        addFileType("OGA", FILE_TYPE_OGG , "application/ogg")
        addFileType("AAC", FILE_TYPE_AAC , "audio/aac")
        addFileType("AAC", FILE_TYPE_AAC , "audio/aac-adts")
        addFileType("MKA", FILE_TYPE_MKA , "audio/x-matroska")
        addFileType("MID", FILE_TYPE_MID , "audio/midi")
        addFileType("MIDI", FILE_TYPE_MID , "audio/midi")
        addFileType("XMF", FILE_TYPE_MID, "audio/midi")
        addFileType("RTTTL", FILE_TYPE_MID, "audio/midi")
        addFileType("SMF", FILE_TYPE_SMF , "audio/sp-midi")
        addFileType("IMY", FILE_TYPE_IMY , "audio/imelody")
        addFileType("RTX", FILE_TYPE_MID, "audio/midi")
        addFileType("OTA", FILE_TYPE_MID, "audio/midi")
        addFileType("MXMF", FILE_TYPE_MID, "audio/midi")
        addFileType("MPEG", FILE_TYPE_MP4 , "video/mpeg")
        addFileType("MPG", FILE_TYPE_MP4, "video/mpeg")
        addFileType("MP4", FILE_TYPE_MP4 , "video/mp4")
        addFileType("M4V", FILE_TYPE_M4V , "video/mp4")
        addFileType("3GP", FILE_TYPE_3GPP , "video/3gpp")
        addFileType("3GPP", FILE_TYPE_3GPP , "video/3gpp")
        addFileType("3G2", FILE_TYPE_3GPP2 , "video/3gpp2")
        addFileType("3GPP2", FILE_TYPE_3GPP2 , "video/3gpp2")
        addFileType("MKV", FILE_TYPE_MKV , "video/x-matroska")
        addFileType("WEBM", FILE_TYPE_WEBM , "video/webm")
        addFileType("TS", FILE_TYPE_MP2TS , "video/mp2ts")
        addFileType("AVI", FILE_TYPE_AVI , "video/avi")
        addFileType("WMV", FILE_TYPE_WMV , "video/x-ms-wmv")
        addFileType("ASF", FILE_TYPE_ASF , "video/x-ms-asf")
        addFileType("JPG", FILE_TYPE_JPEG , "image/jpg")
        addFileType("JPEG", FILE_TYPE_JPEG , "image/jpeg")
        addFileType("GIF", FILE_TYPE_GIF , "image/gif")
        addFileType("PNG", FILE_TYPE_PNG , "image/png")
        addFileType("BMP", FILE_TYPE_BMP , "image/x-ms-bmp")
        addFileType("WBMP", FILE_TYPE_WBMP , "image/vnd.wap.wbmp")
        addFileType("WEBP", FILE_TYPE_WEBP , "image/webp")
        addFileType("heic", FILE_TYPE_HEIC , "image/heif")
        addFileType("M3U", FILE_TYPE_M3U , "audio/x-mpegurl")
        addFileType("M3U", FILE_TYPE_M3U , "application/x-mpegurl")
        addFileType("PLS", FILE_TYPE_PLS , "audio/x-scpls")
        addFileType("WPL", FILE_TYPE_WPL , "application/vnd.ms-wpl")
        addFileType("M3U8", FILE_TYPE_HTTPLIVE , "application/vnd.apple.mpegurl")
        addFileType("M3U8", FILE_TYPE_HTTPLIVE , "audio/mpegurl")
        addFileType("M3U8", FILE_TYPE_HTTPLIVE , "audio/x-mpegurl")
        addFileType("FL", FIRST_DRM_FILE_TYPE , "application/x-android-drm-fl")
        addFileType("TXT", FILE_TYPE_TEXT , "text/plain")
        addFileType("HTM", FILE_TYPE_HTML , "text/html")
        addFileType("HTML", FILE_TYPE_HTML , "text/html")
        addFileType("PDF", FILE_TYPE_PDF , "application/pdf")
        addFileType("DOC", FILE_TYPE_MS_WORD , "application/msword")
        addFileType("XLS", FILE_TYPE_MS_EXCEL , "application/vnd.ms-excel")
        addFileType("PPT", FILE_TYPE_MS_POWERPOINT , "application/mspowerpoint")
        addFileType("FLAC", FILE_TYPE_FLAC , "audio/flac")
        addFileType("ZIP", FILE_TYPE_ZIP , "application/zip")
        addFileType("MPG", FILE_TYPE_MP2PS , "video/mp2p")
        addFileType("MPEG", FILE_TYPE_MP2PS , "video/mp2p")

    }

    class MediaFileType internal constructor(val fileType: Int, val mimeType: String?)
}