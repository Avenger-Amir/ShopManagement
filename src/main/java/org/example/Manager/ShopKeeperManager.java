package org.example.Manager;

import org.example.DbModels.Shopkeeper;
import org.example.Repository.ShopKeeperRepository;
import org.springframework.stereotype.Component;

@Component
public class ShopKeeperManager {

    private final ShopKeeperRepository shopKeeperRepository;

    public ShopKeeperManager(final ShopKeeperRepository shopKeeperRepository) {
        this.shopKeeperRepository = shopKeeperRepository;
    }

    public final Shopkeeper save(final Shopkeeper shopkeeper) {
        return shopKeeperRepository.save(shopkeeper);
    }

    public final Shopkeeper getByMobileNumberAndPassword(final String mobileNumber, final String password) {
        return shopKeeperRepository.findByMobileNumberAndPassword(mobileNumber, password);
    }

    public final Shopkeeper getByMobileNumber(final String mobileNumber) {
        return shopKeeperRepository.findByMobileNumber(mobileNumber);
    }

//    public final Shopkeeper updateShopKeeper(final Shopkeeper shopkeeper) {
//        return shopKeeperRepository.save(shopkeeper);
//    }
}
