package com.cognifygroup.vgold.changepassword;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by shivraj on 7/26/18.
 */

public interface ChangePasswordService {

    @POST("update_forgot_passward.php?")
    @FormUrlEncoded
    Call<ChangePasswordModel> getChangePassword(@Field("id") String id,
                                                @Field("otp") String otp,
                                                @Field("pass") String pass);
}
