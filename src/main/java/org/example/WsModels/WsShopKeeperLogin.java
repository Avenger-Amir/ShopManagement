package org.example.WsModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class WsShopKeeperLogin {
    @JsonProperty("password")
    @NonNull
    private String password;

    @JsonProperty("mobile_number")
    @NonNull
    private String number;
}
