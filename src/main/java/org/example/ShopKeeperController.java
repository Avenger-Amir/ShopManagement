package org.example;

import org.example.DbModels.Address;
import org.example.DbModels.ShopOrder;
import org.example.DbModels.Shopkeeper;
import org.example.DbModels.ShopUser;
import org.example.Manager.AddressManager;
import org.example.Manager.OrderManager;
import org.example.Manager.SessionManager;
import org.example.Manager.ShopKeeperManager;
import org.example.WsModels.*;
import org.example.enums.AddressableType;
import org.example.validator.ShopkeeperValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shopkeeper")
@CrossOrigin
public class ShopKeeperController {

    private final ShopKeeperManager shopKeeperManager;
    private final AddressManager addressManager;
    private final OrderManager orderManager;
    private final ShopkeeperValidator shopkeeperValidator;
//    private final SessionManager sessionManager;

    ShopKeeperController(final ShopKeeperManager shopKeeperManager,
                         final AddressManager addressManager,
                         final OrderManager orderManager,
                         final ShopkeeperValidator shopkeeperValidator){
        this.shopKeeperManager = shopKeeperManager;
        this.addressManager = addressManager;
        this.orderManager = orderManager;
        this.shopkeeperValidator = shopkeeperValidator;
//        this.sessionManager = sessionManager;
    }

    @PostMapping("/login")
    public ResponseEntity<WsShopKeeperLogin> shopLogin(@RequestBody final WsShopKeeperLogin wsShopKeeperLogin) {
        if(!shopkeeperValidator.isExistingShopkeeperByMobileNumber(wsShopKeeperLogin.getMobileNumber())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        final Shopkeeper shopKeeper = shopKeeperManager.getByMobileNumberAndPassword(wsShopKeeperLogin.getMobileNumber(), wsShopKeeperLogin.getPassword());
        if (shopKeeper == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final String sessionId = SessionManager.createShopKeeperSession(shopKeeper);

        return ResponseEntity.ok()
                .header("x-session-id", sessionId)
                .body(wsShopKeeperLogin);
    }

    @PostMapping("/mostSold")
    public ResponseEntity<List<WsShopOrderList>> mostSold(@RequestHeader("x-session-id") final String sessionId, @RequestBody final WsTimeStampBound wsTimeStampBound) {
        final Shopkeeper shopkeeper = SessionManager.validateShopKeeperSession(sessionId);
        if (shopkeeper == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(orderManager.getMostSoldItems(shopkeeper.getId(), wsTimeStampBound.getStartTime(), wsTimeStampBound.getEndTime()));
    }

    @PostMapping("/signUp")
    public ResponseEntity<WsShopkeeperSignUp> signUp(@RequestBody final WsShopkeeperSignUp wsShopkeeper){
//        userValidator.validateUser(wsUser);
        if(shopkeeperValidator.isExistingShopkeeperByMobileNumber(wsShopkeeper.getMobileNumber())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
//       return shopkeeperValidator.isExistingShopkeeperByMobileNumber(wsShopkeeper.getMobileNumber()) == true?
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(): null;
        final Shopkeeper shopkeeper = shopKeeperManager.save(toShopkeeper(wsShopkeeper));
        final Address address =  addressManager.toAddress(wsShopkeeper.getAddress(), AddressableType.SHOPKEEPER, shopkeeper.getId());
        shopkeeper.getAddresses().add(address);
        shopKeeperManager.save(shopkeeper);
        addressManager.save(address);
        final String sessionId = SessionManager.createShopKeeperSession(shopkeeper);

        return ResponseEntity.ok()
                .header("x-session-id", sessionId)
                .body(wsShopkeeper);
//        return ResponseEntity.ok(wsUser);
    }

    private Shopkeeper toShopkeeper(final WsShopkeeperSignUp wsShopkeeper){
        final Shopkeeper shopkeeper = new Shopkeeper();
        shopkeeper.setName(wsShopkeeper.getName());
        shopkeeper.setEmailId(wsShopkeeper.getEmailId());
        shopkeeper.setPassword(wsShopkeeper.getPassword());
        shopkeeper.setMobileNumber(wsShopkeeper.getMobileNumber());
//        shopkeeper.getAddresses().add(addressManager.toAddress(wsShopkeeper.getAddress(), AddressableType.SHOPKEEPER));
        return shopkeeper;
    }
}
