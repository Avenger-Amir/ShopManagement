package org.example.Manager;

import jakarta.ws.rs.BadRequestException;
import org.example.DbModels.ShopUser;
import org.example.Repository.UserRepository;
import org.example.WsModels.WsUser;
import org.springframework.stereotype.Component;

@Component
public class UserManager {

    private final UserRepository userRepository;
    public UserManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ShopUser saveUser(final WsUser wsUser) {
        return userRepository.save(toUser(wsUser));
    }

    public ShopUser getUserByMobileNumber(final String mobileNumber){
        return userRepository.findByMobileNumber(mobileNumber);
    }

    public ShopUser getUserByMobileNumberAndPassword(final String mobileNumber, final String password){
        final ShopUser user = userRepository.findByMobileNumber(mobileNumber);
        if(user == null){
            throw new BadRequestException("Mobile Number is wrong");
        }

        if(!user.getPassword().equals(password)){
            throw new BadRequestException("Password is incorrect");
        }
        return user;
    }

    private ShopUser toUser(final WsUser wsUser) {
        ShopUser user = new ShopUser();
        user.setUserName(wsUser.getUsername());
        user.setPassword(wsUser.getPassword());
        user.setEmailId(wsUser.getEmailId());
        user.setMobileNumber(wsUser.getNumber());
        return user;
    }
}
