package org.example.WsModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Enumerated;
import lombok.NonNull;
import org.example.enums.ShopType;

public class WsShopRegistration {

    @JsonProperty("id")
    private int id;

    @JsonProperty("shop_name")
    @NonNull
    private String shopName;

    @JsonProperty("number")
    @NonNull
    private String number;

    @JsonProperty("address")
    @NonNull
    private WsAddress address;

    @JsonProperty("shop_keeper_id")
    private long shop_keeper_id;

    @Enumerated(jakarta.persistence.EnumType.STRING)
    @JsonProperty("shop_type")
    @NonNull
    private ShopType shopType;
}
