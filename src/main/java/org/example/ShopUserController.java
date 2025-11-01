package org.example;

import org.example.DbModels.Item;
import org.example.DbModels.Shop;
import org.example.DbModels.ShopUser;
import org.example.DbModels.Shopkeeper;
import org.example.Manager.*;
import org.example.Repository.ShopRepository;
import org.example.WsModels.*;
import org.example.exceptions.ExceptionUtil;
import org.example.validator.ItemValidator;
import org.example.validator.OrderValidator;
import org.example.validator.ShopkeeperValidator;
import org.example.validator.UserValidator;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ShopUserController {

    private final UserValidator userValidator;
    private final UserManager userManager;
    private final OrderManager orderManager;
    private final OrderValidator orderValidator;
    private final ItemManager itemManager;
    private final ItemValidator itemValidator;
    private final ShopRepository shopRepository;
    private final ShopManager shopManager;
    private final ShopKeeperManager shopKeeperManager;
    private final ShopkeeperValidator shopkeeperValidator;

    ShopUserController(final UserValidator userValidator,
                       final UserManager userManager,
                       final OrderManager orderManager,
                       final OrderValidator orderValidator,
                       final ItemManager itemManager,
                       final ItemValidator itemValidator,
                       final ShopRepository shopRepository,
                       final ShopManager shopManager,
                       final ShopKeeperManager shopKeeperManager, ShopkeeperValidator shopkeeperValidator) {
        this.userValidator = userValidator;
        this.userManager = userManager;
        this.orderManager = orderManager;
        this.orderValidator = orderValidator;
        this.itemManager = itemManager;
        this.itemValidator = itemValidator;
        this.shopRepository = shopRepository;
        this.shopManager = shopManager;
        this.shopKeeperManager = shopKeeperManager;
        this.shopkeeperValidator = shopkeeperValidator;
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/signUp")
    public ResponseEntity<WsUser> signUp(@RequestBody final WsUser wsUser){
//        userValidator.validateUser(wsUser);
//        userValidator.validateExistingUser(wsUser);
        ShopUser user = userManager.saveUser(wsUser);
        String sessionId = SessionManager.createSession(user);

        return ResponseEntity.ok()
                .header("x-session-id", sessionId)
                .body(wsUser);
    }

    @PostMapping("/login")
    public ResponseEntity<WsUser> login(@RequestBody WsUser wsUser) {
        ShopUser user = userManager.getUserByMobileNumberAndPassword(wsUser.getMobileNumber(), wsUser.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String sessionId = SessionManager.createSession(user);

        return ResponseEntity.ok()
                .header("x-session-id", sessionId)
                .body(wsUser);
    }

    @PostMapping("/shop_login")
    public ResponseEntity<WsUser> shopLogin(@RequestBody WsUser wsUser) {
        ShopUser user = userManager.getUserByMobileNumberAndPassword(wsUser.getMobileNumber(), wsUser.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String sessionId = SessionManager.createSession(user);

        return ResponseEntity.ok()
                .header("x-session-id", sessionId)
                .body(wsUser);
    }

    @GetMapping("/shop")
    public ResponseEntity<List<WsShop>> getShop(){

        final List<WsShop> wsShops = shopManager.toWsShops(shopManager.getAllShops());
        return ResponseEntity.ok(wsShops);
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
        // fetch user from session
        ShopUser user = SessionManager.validateSession(sessionId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final WsShopOrder bookedOrder = orderManager.bookOrder(wsShopOrder, user);
        return ResponseEntity.ok(bookedOrder);
    }

    @GetMapping("/shop/items")
    public ResponseEntity<List<WsItem>> getItemByNameSubArray(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "shopid", required = false) Long shopId) {
        if(name.equals("all") && shopId == null){
            final List<Item> items = itemManager.getAllItem();
            final List<WsItem> wsItems = itemManager.toWsItems(items);
            return ResponseEntity.ok(wsItems);
        }
//        shopId=1L;
        final List<Item> items = itemManager.getItemsByShopId(1L);
        final List<WsItem> wsItems = itemManager.toWsItems(items);
        return ResponseEntity.ok(wsItems);
    }


    @PostMapping(value = "/shop/owner/items/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<WsItem>> addItems(
            @RequestHeader("x-session-id") String sessionId,
            @RequestPart("item") final WsItem wsItem,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {


        if (images != null && !images.isEmpty()) {
            MultipartFile file = images.get(0); // Assuming one image per item for simplicity

            if (!file.isEmpty()) {
                // 2. Create the directory if it doesn't exist
               final  Path uploadPath = Paths.get(uploadDir);
//                Files.createDirectories(uploadPath);

                // 3. Create a unique filename to avoid collisions
                final String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                final Path filePath = uploadPath.resolve(uniqueFileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // 4. IMPORTANT: Save the RELATIVE URL to the database, not the full system path
                final String imageUrl = "/uploads/images/" + uniqueFileName;
                wsItem.setImageUrl(imageUrl);
            }
        }

        return ResponseEntity.ok(itemManager.addItem(List.of(wsItem)));
    }

    @GetMapping("/shop/session")
    public ResponseEntity<WsShop> getShopBySessionId(@RequestHeader("x-session-id") String sessionId){
        // fetch user from session
        ShopUser user = SessionManager.validateSession(sessionId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        final Shop shop = shopRepository.findByOwner_Id(user.getId());
        if(shop == null){
            throw ExceptionUtil.error("Shop for user " + user.getId() + " does not exist", "204");
        }
        final WsShop wsShop = shopManager.toWsShops(List.of(shop)).getFirst();
        return ResponseEntity.ok(wsShop);
    }

    @DeleteMapping("/shop/items/remove/{itemId}")
    public ResponseEntity<String> removeItems(@RequestHeader("x-session-id") String sessionId, @RequestParam("itemId") final Long itemId){
        // fetch user from session
        final Shopkeeper shopkeeper = SessionManager.validateShopKeeperSession(sessionId);
        if (shopkeeper == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        final Item item = itemManager.getItemById(itemId);
        if(item == null){
            throw ExceptionUtil.error("Item with id " + itemId + " does not exist", "204");
        }
        shopkeeperValidator.validateItemOwnership(shopkeeper, item);

        itemManager.archiveItem(itemId);
        return ResponseEntity.ok(String.format("Item %s archived successfully", item.getName()));
    }

    @GetMapping("/shop/order_history")
    public ResponseEntity<List<WsShopOrderList>> getOrderHistoryByUser(@RequestHeader("x-session-id") final String sessionId, @RequestBody final WsShopOrderHistoryRequest wsShopOrderHistoryRequest){
        // fetch user from session
        final ShopUser user = SessionManager.validateSession(sessionId);
        final Shopkeeper shopkeeper = SessionManager.validateShopKeeperSession(sessionId);
        if (shopkeeper == null && user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        if(shopkeeper!=null){
            final Shop shop = shopRepository.findByOwner_Id(shopkeeper.getId());
            return ResponseEntity.ok(orderManager.getShopOrderByShopId(shop.getId(), wsShopOrderHistoryRequest.getStartTimestamp(), wsShopOrderHistoryRequest.getEndTimestamp()));
        }

        return ResponseEntity.ok(orderManager.getShopOrderByUserId(user.getId(), wsShopOrderHistoryRequest.getStartTimestamp(), wsShopOrderHistoryRequest.getEndTimestamp()));
    }

}
