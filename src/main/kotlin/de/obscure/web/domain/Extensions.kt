package de.obscure.web.domain

import java.awt.image.BufferedImage

object Extensions {

    fun BufferedImage.toImage(cameraType: CameraType): Image {
        val bytes = ImageProcessor.getBytesFromBufferedImage(this)
        return Image(
                null,
                cameraType,
                bytes.size.toLong(),
                PictureSize.NORMAL.width,
                PictureSize.NORMAL.height,
                bytes.toTypedArray()
        )
    }

    fun BufferedImage.toByteArray(): ByteArray {
        return ImageProcessor.getBytesFromBufferedImage(this)
    }

}