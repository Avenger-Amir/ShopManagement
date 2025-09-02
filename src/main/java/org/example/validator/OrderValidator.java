package org.example.validator;

import org.example.DbModels.Item;
import org.example.Manager.ItemManager;
import org.example.WsModels.WsShopOrder;
import org.example.WsModels.WsShopOrderList;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderValidator {

    private final ItemManager itemManager;

    OrderValidator(final ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    public void validateItems(WsShopOrder wsShopOrder) {
        itemManager.validateAllItemExisting(wsShopOrder.getWsShopOrderList().stream().map(WsShopOrderList::getItemId).toList(), wsShopOrder.getShopId());
    }


}
