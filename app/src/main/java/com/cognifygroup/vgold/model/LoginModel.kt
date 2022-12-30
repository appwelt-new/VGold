package com.cognifygroup.vgold.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginModel {
    @Expose
    @SerializedName("data")
    var data: ArrayList<LoginModel.Data>? = null

    @Expose
    @SerializedName("Message")
    var message: String? = null

    @Expose
    @SerializedName("status")
    var status: Int? = null

    class Data {
        @Expose
        @SerializedName("Qrcode")
        var qrcode: String? = null

        @Expose
        @SerializedName("Gender")
        var gender: String? = null

        @Expose
        @SerializedName("Date_of_birth")
        var date_of_birth: String? = null

        @Expose
        @SerializedName("User_role")
        var user_role: String? = null

        @Expose
        @SerializedName("Mobile_no")
        var mobile_no: String? = null

        @Expose
        @SerializedName("Email")
        var email: String? = null

        @Expose
        @SerializedName("Last_Name")
        var last_Name: String? = null

        @Expose
        @SerializedName("Middle_Name")
        var middle_Name: String? = null

        @Expose
        @SerializedName("First_Name")
        var first_Name: String? = null

        @Expose
        @SerializedName("User_ID")
        var user_ID: String? = null
            private set

        @Expose
        @SerializedName("profile_photo")
        var profile_photo: String? = null

        @Expose
        @SerializedName("pan_no")
        var pan_no: String? = null

        @Expose
        @SerializedName("Address")
        var address: String? = null

        @Expose
        @SerializedName("City")
        var city: String? = null

        @Expose
        @SerializedName("State")
        var state: String? = null

        @Expose
        @SerializedName("validity_date")
        var validity_date: String? = null

        @Expose
        @SerializedName("Version_code")
        var version_code: String? = null

        @Expose
        @SerializedName("is_cp")
        var is_cp: Int? = null

        @Expose
        @SerializedName("channel_partner")
        var channelPartner: ChannelPartner? = null

        @Expose
        @SerializedName("identity_photo")
        var identity_photo: String? = null

        @Expose
        @SerializedName("address_photo")
        var address_photo: String? = null

        @Expose
        @SerializedName("address_photo_back")
        var address_photo_back: String? = null

        @Expose
        @SerializedName("aadhar_no")
        var aadhar_no: String? = null

        fun setUgser_ID(User_ID: String?) {
            user_ID = User_ID
        }

        class ChannelPartner {
            @Expose
            @SerializedName("channel_partner_detail_id")
            var channel_partner_detail_id: String? = null

            @Expose
            @SerializedName("user_id")
            var user_id: String? = null

            @Expose
            @SerializedName("channel_partner_code")
            var channel_partner_code: String? = null

            @Expose
            @SerializedName("status")
            var status: Int? = null

            @Expose
            @SerializedName("created_date")
            var created_date: String? = null

            @Expose
            @SerializedName("end_date")
            var end_date: String? = null

            @Expose
            @SerializedName("total_user")
            var total_user: String? = null

            @Expose
            @SerializedName("total_gold_booking")
            var total_gold_booking: String? = null

            @Expose
            @SerializedName("total_commission")
            var total_commission: String? = null
        }
    }
}
