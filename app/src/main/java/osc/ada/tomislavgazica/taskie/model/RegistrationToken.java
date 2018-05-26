package osc.ada.tomislavgazica.taskie.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationToken {
    @Expose
    @SerializedName("name")
    public String userName;
    @Expose
    @SerializedName("email")
    public String email;
    @Expose
    @SerializedName("password")
    public String password;
}
