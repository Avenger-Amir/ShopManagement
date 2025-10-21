package org.example.WsModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.example.enums.ShopOrderStatus;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class WsShopOrder {

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("shop_id")
    @NonNull
    private Long ShopId;

    @JsonProperty("status")
    private ShopOrderStatus status;

    @JsonProperty("shop_order_list")
    private List<WsShopOrderList> wsShopOrderList = new ArrayList<>();
}
