package com.cloudwell.paywell.services.activity.healthInsurance.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RespseMemberShipPackage(

		@field:SerializedName("status_code")
		val statusCode: Int,

		@field:SerializedName("membershipPackages")
		val membershipPackages: List<MembershipPackagesItem>,

		@field:SerializedName("message")
		val message: String
) : Parcelable

@Parcelize
data class MembershipPackagesItem(

		@field:SerializedName("amount")
		val amount: String,

		@field:SerializedName("cash_back_amount")
		val cashBackAmount: String,

		@field:SerializedName("member")
		val member: String,

		@field:SerializedName("name")
		val name: String,

		@field:SerializedName("doctor_consultation")
		val doctorConsultation: List<DoctorConsultationItem>,

		@field:SerializedName("details")
		val details: String,

		@field:SerializedName("aditional")
		val aditional: List<AditionalItem>,

		@field:SerializedName("package_id")
		val packageId: String,

		@field:SerializedName("package_image")
		val packageImage: String,

		@field:SerializedName("validity")
		val validity: String,

		@field:SerializedName("cash_back_message")
		val cashBackMessage: String,

		@field:SerializedName("member_message")
		val memberMessage: String
) : Parcelable

@Parcelize
data class AditionalItem(

		@field:SerializedName("amount")
		val amount: String,

		@field:SerializedName("message")
		val message: String
) : Parcelable

@Parcelize
data class DoctorConsultationItem(

		@field:SerializedName("image")
		val image: String,

		@field:SerializedName("name")
		val name: String,

		@field:SerializedName("description")
		val description: String
) : Parcelable
