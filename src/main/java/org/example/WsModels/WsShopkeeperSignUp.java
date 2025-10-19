package org.example.WsModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class WsShopkeeperSignUp {
    @JsonProperty("id")
    private int id;

    @JsonProperty("user_name")
//    @NonNull
    private String name;

    @JsonProperty("email_id")
//    @NonNull
    private String emailId;

    @JsonProperty("password")
    @NonNull
    private String password;

    @JsonProperty("mobile_number")
    @NonNull
    private String mobileNumber;

    @JsonProperty("user_address")
    @NonNull
    private WsAddress address;

//    @JsonProperty
}
