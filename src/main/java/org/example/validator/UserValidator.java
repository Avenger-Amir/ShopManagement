package org.example.validator;

import org.example.DbModels.Shop;
import org.example.DbModels.ShopUser;
import org.example.Manager.UserManager;
import org.example.WsModels.WsUser;
import org.example.exceptions.ExceptionUtil;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    private final UserManager userManager;

    UserValidator(UserManager userManager) {
        this.userManager = userManager;
    }

    public void validateUser(final WsUser wsUser) {
        assert wsUser != null;
        assert wsUser.getNumber() != null;
        assert wsUser.getPassword() != null;
        assert wsUser.getEmailId() != null;

        if (wsUser.getPassword().length() != 8) {
            throw ExceptionUtil.error("Password length should be 8", "400");
        }

        if (wsUser.getNumber().length() != 10) {
            throw ExceptionUtil.error("Number length should be 10", "400");
        }

        if (!isAllDigit(wsUser.getNumber())){
            throw ExceptionUtil.error("Mobile Number should contain only digits", "400");
        }
    }

    public void validateExistingUser(final WsUser wsUser){
        final ShopUser user = userManager.getUserByMobileNumber(wsUser.getNumber());
        if (user != null){
            throw ExceptionUtil.error("User with mobile number " + wsUser.getNumber() + " already exists", "400");
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

    public void validateIsAdmin(final ShopUser user){
//        if(!user.isAdmin()){
//            throw new RuntimeException("User is not admin");
//        }
    }
}
