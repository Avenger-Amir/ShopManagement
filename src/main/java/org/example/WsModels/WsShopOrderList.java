package org.example.WsModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
public class WsShopOrderList {

    @JsonProperty("item_id")
    @NonNull private Long itemId;

    @JsonProperty("quantity")
    @NonNull private Long quantity;

    @JsonProperty("name")
    private String name;

    @JsonProperty("price")
    private Double price;
}
