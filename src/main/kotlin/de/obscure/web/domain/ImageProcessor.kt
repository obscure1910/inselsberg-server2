package de.obscure.web.domain

import java.awt.AlphaComposite
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

object ImageProcessor {

    fun resizePicture(picture: ByteArray, size: PictureSize): BufferedImage {
        return resizePictureTo(size.width, size.height, picture)
    }

    fun getBytesFromBufferedImage(originalImage: BufferedImage): ByteArray {
        return try {
            val baos = ByteArrayOutputStream()
            ImageIO.write(originalImage, "jpg", baos)
            baos.flush()
            val imageInByte = baos.toByteArray()
            baos.close()
            imageInByte
        } catch (ex: Exception) {
            ByteArray(0)
        }
    }

    private fun resizePictureTo(width: Int, height: Int, picture: ByteArray): BufferedImage {
        val originalImage = toBufferedImage(picture)
        val imgType =
                if (originalImage.type == 0)
                    BufferedImage.TYPE_INT_ARGB
                else
                    originalImage.type
        val resizedImage = BufferedImage(width, height, imgType)
        val g = resizedImage.createGraphics()
        g.composite = AlphaComposite.Src
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC)
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY)
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.drawImage(originalImage, 0, 0, width, height, null)
        g.dispose()
        return resizedImage
    }

    private fun toBufferedImage(bytes: ByteArray): BufferedImage {
        return ImageIO.read(ByteArrayInputStream(bytes))
    }

}