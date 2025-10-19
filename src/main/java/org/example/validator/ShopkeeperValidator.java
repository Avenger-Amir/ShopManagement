package org.example.validator;

import org.example.DbModels.ShopUser;
import org.example.Manager.ShopKeeperManager;
import org.example.WsModels.WsShopkeeperSignUp;
import org.example.WsModels.WsUser;
import org.example.exceptions.ExceptionUtil;
import org.springframework.stereotype.Component;

@Component
public class ShopkeeperValidator {

    private final ShopKeeperManager shopkeeperManager;

    public ShopkeeperValidator(final ShopKeeperManager shopkeeperManager) {
        this.shopkeeperManager = shopkeeperManager;
    }

    public boolean isExistingShopkeeperByMobileNumber(final String mobileNumber) {
        return shopkeeperManager.getByMobileNumber(mobileNumber) != null;
    }
}
