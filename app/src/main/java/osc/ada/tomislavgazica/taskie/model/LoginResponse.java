package osc.ada.tomislavgazica.taskie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @Expose
    @SerializedName("token")
    public String token;
}
