package org.example.WsModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class WsUser {

    @JsonProperty("id")
    private int id;

    @JsonProperty("user_name")
//    @NonNull
    private String username;

    @JsonProperty("email_id")
//    @NonNull
    private String emailId;

    @JsonProperty("password")
    @NonNull
    private String password;

    @JsonProperty("mobile_number")
    @NonNull
    private String mobileNumber;

    @JsonProperty("address")
//    @NonNull
    private WsAddress address;
}
