package org.example.validator;

import org.example.DbModels.Shop;
import org.example.DbModels.ShopOrder;
import org.example.DbModels.ShopUser;
import org.example.Manager.UserManager;
import org.example.Repository.ShopRepository;
import org.example.WsModels.WsUser;
import org.example.exceptions.ExceptionUtil;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    private final UserManager userManager;
    private final ShopRepository shopRepository;

    UserValidator(final UserManager userManager,
                  final ShopRepository shopRepository) {
        this.userManager = userManager;
        this.shopRepository = shopRepository;
    }

    public void validateUser(final WsUser wsUser) {
        assert wsUser != null;
        assert wsUser.getMobileNumber() != null;
        assert wsUser.getPassword() != null;
        assert wsUser.getEmailId() != null;

        if (wsUser.getPassword().length() != 8) {
            throw ExceptionUtil.error("Password length should be 8", "400");
        }

        if (wsUser.getMobileNumber().length() != 10) {
            throw ExceptionUtil.error("Number length should be 10", "400");
        }

        if (!isAllDigit(wsUser.getMobileNumber())){
            throw ExceptionUtil.error("Mobile Number should contain only digits", "400");
        }
    }

    public void validateExistingUser(final WsUser wsUser){
        final ShopUser user = userManager.getUserByMobileNumber(wsUser.getMobileNumber());
        if (user != null){
            throw ExceptionUtil.error("User with mobile number " + wsUser.getMobileNumber() + " already exists", "400");
        }
    }

    private boolean isAllDigit(final String number){
        for (int i = 0; i < number.length(); i++){
            if (!Character.isDigit(number.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public void validateIsShopOwner(final ShopUser user, final Long shopId){
        if(shopId == null){
            throw ExceptionUtil.error("Shop Id is null", "400");
        }

        final Shop shop = shopRepository.findById(shopId).orElse(null);
        if(shop == null){
            throw ExceptionUtil.error("Shop with id " + shopId + " does not exist", "400");
        }

        if(!shop.getOwner().getId().equals(user.getId())){
            throw ExceptionUtil.error("User is not owner of the shop", "403");
        }

//        if(!user.isAdmin()){
//            throw new RuntimeException("User is not admin");
//        }
    }
}
