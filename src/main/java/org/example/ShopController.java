package org.example;

import org.example.DbModels.Item;
import org.example.DbModels.Shop;
import org.example.DbModels.ShopUser;
import org.example.Manager.*;
import org.example.Repository.ShopRepository;
import org.example.WsModels.WsItem;
import org.example.WsModels.WsShop;
import org.example.WsModels.WsShopOrder;
import org.example.WsModels.WsUser;
import org.example.validator.ItemValidator;
import org.example.validator.OrderValidator;
import org.example.validator.UserValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ShopController {

    private final UserValidator userValidator;
    private final UserManager userManager;
    private final OrderManager orderManager;
    private final OrderValidator orderValidator;
    private final ItemManager itemManager;
    private final ItemValidator itemValidator;
    private final ShopRepository shopRepository;
    private final ShopManager shopManager;
    ShopController(final UserValidator userValidator,
                   final UserManager userManager,
                   final OrderManager orderManager,
                   final OrderValidator orderValidator,
                   final ItemManager itemManager,
                   final ItemValidator itemValidator,
                   final ShopRepository shopRepository,
                   final ShopManager shopManager) {
        this.userValidator = userValidator;
        this.userManager = userManager;
        this.orderManager = orderManager;
        this.orderValidator = orderValidator;
        this.itemManager = itemManager;
        this.itemValidator = itemValidator;
        this.shopRepository = shopRepository;
        this.shopManager = shopManager;
    }

    @PostMapping("/signUp")
    public ResponseEntity<WsUser> signUp(@RequestBody final WsUser wsUser){
//        userValidator.validateUser(wsUser);
//        userValidator.validateExistingUser(wsUser);
        ShopUser user = userManager.saveUser(wsUser);
        String sessionId = SessionManager.createSession(user);

        return ResponseEntity.ok()
                .header("x-session-id", sessionId)
                .body(wsUser);
//        return ResponseEntity.ok(wsUser);
    }

//    @PostMapping("/login")
//    public ResponseEntity<WsUser> login(final WsUser wsUser){
//        ShopUser user = userManager.getUserByMobileNumberAndPassword(wsUser.getNumber(), wsUser.getPassword());
//        //Todo(mushtaqu): Set header with session id and expire it in 10 minutes
//        return ResponseEntity.ok(wsUser);
//    }

    @PostMapping("/login")
    public ResponseEntity<WsUser> login(@RequestBody WsUser wsUser) {
        ShopUser user = userManager.getUserByMobileNumberAndPassword(wsUser.getNumber(), wsUser.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String sessionId = SessionManager.createSession(user);

        return ResponseEntity.ok()
                .header("x-session-id", sessionId)
                .body(wsUser);
    }

    @GetMapping("/shop/")
    public ResponseEntity<List<WsShop>> getShop(){
//        orderValidator.validateItems(wsShopOrder);
//        final List<Item> items = orderManager.shopOrderToItems(shopOrders);
        // fetch user from session
//        ShopUser user = SessionManager.validateSession(sessionId);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }

//        final WsShopOrder bookedOrder = orderManager.bookOrder(wsShopOrder, user);
//        final List<WsShopOrder> wsBookedItems = bookedItems.forEach(item -> {
//            return orderManager.itemToShopOrder(item);
//        });

        final List<WsShop> wsShops = shopManager.toWsShops(shopManager.getAllShops());
        return ResponseEntity.ok(wsShops);
//        return ResponseEntity.ok(bookedOrder);
    }

    @PostMapping("/shop/orderBookingPreview")
    public ResponseEntity<WsShopOrder> orderBookingPreview(@RequestBody final WsShopOrder wsShopOrder){
        orderValidator.validateItems(wsShopOrder);
        final WsShopOrder previewedWsShopOrder = orderManager.previewShopOrder(wsShopOrder);
        return ResponseEntity.ok(previewedWsShopOrder);
    }

    @PostMapping("/shop/order")
    public ResponseEntity<WsShopOrder> bookOrder(@RequestHeader("x-session-id") String sessionId, @RequestBody final WsShopOrder wsShopOrder){
        orderValidator.validateItems(wsShopOrder);
//        final List<Item> items = orderManager.shopOrderToItems(shopOrders);
        // fetch user from session
        ShopUser user = SessionManager.validateSession(sessionId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final WsShopOrder bookedOrder = orderManager.bookOrder(wsShopOrder, user);
//        final List<WsShopOrder> wsBookedItems = bookedItems.forEach(item -> {
//            return orderManager.itemToShopOrder(item);
//        });
        return ResponseEntity.ok(bookedOrder);
    }

    @GetMapping("/shop/items")
    public ResponseEntity<List<WsItem>> getItemByNameSubArray(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "shopid", required = false) Long shopId) {
        if(name.equals("all") && shopId == null){
            final List<Item> items = itemManager.getAllItem();
            final List<WsItem> wsItems = itemManager.toWsItems(items);
            return ResponseEntity.ok(wsItems);
        }
        final List<Item> items = itemManager.getItemsByNameAndShopId(name, shopId);
        final List<WsItem> wsItems = itemManager.toWsItems(items);
        return ResponseEntity.ok(wsItems);
    }

//    @GetMapping("/shop/items/all")
//    public ResponseEntity<List<WsItem>> getAllItem() {
//        final List<Item> items = itemManager.getAllItem();
//        final List<WsItem> wsItems = itemManager.toWsItems(items);
//        return ResponseEntity.ok(wsItems);
//    }

//    @PostMapping("/shop/items/add")
//    public ResponseEntity<List<WsItem>> addItems(@RequestHeader("X-Session-Id") String sessionId, @RequestBody final List<WsItem> wsItems){
//
//        // fetch user from session
//        ShopUser user = SessionManager.validateSession(sessionId);
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        userValidator.validateIsAdmin(user);
//
//        final Shop shop = shopRepository.findByOwner_Id(user.getId());
//
//        itemValidator.validateItemBelongsToSameShop(wsItems, shop.getId());
////        ordervalidator.validateItems(item);
//        return ResponseEntity.ok(itemManager.addItem(wsItems));
//    }

@PostMapping(value = "/shop/owner/items/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<List<WsItem>> addItems(
        @RequestHeader("x-session-id") String sessionId,
        @RequestPart("items") final List<WsItem> wsItems,
        @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {

    // Validate session
    ShopUser user = SessionManager.validateSession(sessionId);
    if (user == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    userValidator.validateIsShopOwner(user, wsItems.get(0).getShopId());

    final Shop shop = shopRepository.findByOwner_Id(user.getId());

    itemValidator.validateItemBelongsToSameShop(wsItems, shop.getId());

    // ✅ Handle image saving
    if (images != null && !images.isEmpty()) {
        for (int i = 0; i < images.size(); i++) {
            MultipartFile file = images.get(i);
            if (!file.isEmpty()) {
                String uploadDir = "resources/images/";
                Files.createDirectories(Paths.get(uploadDir));

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Attach image path to the item (assuming WsItem has setImageUrl)
                wsItems.get(i).setImageUrl("/resources/images/" + fileName);
            }
        }
    }

    return ResponseEntity.ok(itemManager.addItem(wsItems));
}

    @PostMapping("/shop/items/remove")
    public ResponseEntity<List<WsItem>> removeItems(@RequestHeader("x-session-id") String sessionId,
                                                            @RequestBody final List<WsItem> wsItems){
        // fetch user from session
        ShopUser user = SessionManager.validateSession(sessionId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userValidator.validateIsShopOwner(user, wsItems.get(0).getShopId());
        itemValidator.validateAllItemExisting(wsItems);

        final Shop shop = shopRepository.findByOwner_Id(user.getId());


        itemValidator.validateItemBelongsToSameShop(wsItems, shop.getId());
        return ResponseEntity.ok(itemManager.removeItems(wsItems));
    }

    @PostMapping("/shop/items/changePrice")
    public ResponseEntity<WsItem> changePrice(@RequestHeader("x-session-id") String sessionId, @RequestBody final WsItem wsItem){

        // fetch user from session
        ShopUser user = SessionManager.validateSession(sessionId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        userValidator.validateIsShopOwner(user, wsItem.getShopId());
        itemValidator.validateAllItemExisting(List.of(wsItem));

        final Shop shop = shopRepository.findByOwner_Id(user.getId());

        itemValidator.validateItemBelongsToSameShop(List.of(wsItem), shop.getId());
        itemManager.updatePrice(wsItem);
        return ResponseEntity.ok(wsItem);
    }

}
