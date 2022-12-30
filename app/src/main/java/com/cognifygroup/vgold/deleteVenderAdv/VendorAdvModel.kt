package com.cognifygroup.vgold.deleteVenderAdv

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VendorAdvModel {
    @Expose
    @SerializedName("Data")
    var data: ArrayList<Data>? = null

    @Expose
    @SerializedName("Message")
    var message: String? = null

    @Expose
    @SerializedName("status")
    var status: String? = null

    class Data { /* @Expose
        @SerializedName("vendor_id")
        private String vendor_id;
        @Expose
        @SerializedName("logo_path")
        private String logo_path;
        @Expose
        @SerializedName("letter_path")
        private String letter_path;
        @Expose
        @SerializedName("advertisement_path")
        private String advertisement_path;

        public String getAdvertisement_path() {
            return advertisement_path;
        }

        public void setAdvertisement_path(String advertisement_path) {
            this.advertisement_path = advertisement_path;
        }

        public String getVendor_id() {
            return vendor_id;
        }

        public void setVendor_id(String vendor_id) {
            this.vendor_id = vendor_id;
        }

        public String getLogo_path() {
            return logo_path;
        }

        public void setLogo_path(String logo_path) {
            this.logo_path = logo_path;
        }

        public String getLetter_path() {
            return letter_path;
        }

        public void setLetter_path(String letter_path) {
            this.letter_path = letter_path;
        }*/
    }
}
