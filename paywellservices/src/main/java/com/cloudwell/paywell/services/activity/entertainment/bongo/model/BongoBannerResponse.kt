package com.cloudwell.paywell.services.activity.entertainment.bongo.model

import com.google.gson.annotations.SerializedName

data class BongoBannerResponse(

	@field:SerializedName("displayPictureCount")
	val displayPictureCount: Int? = null,

	@field:SerializedName("imageLink")
	val imageLink: ArrayList<ImageLinkItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)

data class ImageLinkItem(
	@field:SerializedName("image_address")
	val imageAddress: String? = null
)
