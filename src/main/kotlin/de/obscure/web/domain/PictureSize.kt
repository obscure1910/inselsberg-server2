package de.obscure.web.domain

sealed class PictureSize(val width: Int, val height: Int) {
    object NORMAL : PictureSize(2048, 1536)
    object SMALL : PictureSize(640, 480)
    object PREVIEW : PictureSize(160, 120)
}