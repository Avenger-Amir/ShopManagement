package org.example.Manager;

import org.example.DbModels.Shop;
import org.example.Repository.ShopRepository;
import org.example.WsModels.WsShop;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShopManager {

    private final ShopRepository shopRepository;

    ShopManager(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    public List<Shop> getAllShops() {
        return (List<Shop>) shopRepository.findAll();
    }

    public List<WsShop> toWsShops(final List<Shop> shops) {
        return shops.stream().map(shop -> {
            final WsShop wsShop = new WsShop();
            wsShop.setId(shop.getId());
            wsShop.setName(shop.getName());
            return wsShop;
        }).toList();
    }
}
