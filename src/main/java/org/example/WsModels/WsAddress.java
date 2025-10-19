package org.example.WsModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class WsAddress {
    @JsonProperty("id")
    private int id;

    @JsonProperty("street")
    @NonNull
    private String street;

    @JsonProperty("city")
    @NonNull
    private String city;

    @JsonProperty("postal_code")
    @NonNull
    private String postalCode;

}
