package org.example.Manager;

import jakarta.ws.rs.BadRequestException;
import org.example.DbModels.Item;
import org.example.DbModels.Shop;
import org.example.Repository.ItemRepository;
import org.example.Repository.ShopRepository;
import org.example.WsModels.WsItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ItemManager {

    private final ItemRepository itemRepository;
    private final ShopRepository shopRepository;

    ItemManager(final ItemRepository itemRepository, final ShopRepository shopRepository) {
        this.itemRepository = itemRepository;
        this.shopRepository = shopRepository;
    }

    public void validateAllItemExisting(final List<Long> requestedItemIds, final Long shopId){
        final List<Item> existingItems = itemRepository.findAllById(requestedItemIds);
        existingItems.forEach(existingItem -> {
            if(!existingItem.getShop().getId().equals(shopId)){
                throw new BadRequestException(String.format("Given item %d not exist in the shop", shopId));
            }
        });
//        final Set<Long> requestedItemIds = Set.of(itemIds);
        final Set<Long> existingItemIds = existingItems.stream().map(Item::getId).collect(Collectors.toSet());
        if (!existingItemIds.containsAll(Set.of(requestedItemIds))) {
            throw new BadRequestException("All items are not present in the shop");
        }


    }

    public List<WsItem> addItem(final List<WsItem> wsItems){
        wsItems.forEach(wsItem -> {
            Item item = itemRepository.findByNameAndShopId(wsItem.getName(), wsItem.getShopId());
            if (item == null) {
                item = toItem(wsItem);
                item = itemRepository.saveAndFlush(item);
                wsItem.setId(item.getId());
            }else{
                item.setPrice(wsItem.getPrice());
                item.setQuantity(item.getQuantity()+wsItem.getQuantity());
                itemRepository.saveAndFlush(item);
            }
        });
        return wsItems;
    }

    public List<WsItem> removeItems(final List<WsItem> wsItems){
        final List<Long> itemIds = wsItems.stream().map(WsItem::getId).collect(Collectors.toList());
        itemRepository.deleteAllByIdInBatch(itemIds);
        return wsItems;
    }

    public void updatePrice(final WsItem wsItem){
        final Item item = itemRepository.getReferenceById(wsItem.getId());
        item.setPrice(wsItem.getPrice());
        itemRepository.saveAndFlush(item);
    }

    public Item toItem(final WsItem wsItem){
        final Item item = new Item();
        item.setName(wsItem.getName());
        item.setPrice(wsItem.getPrice());
        item.setDescription(wsItem.getDescription());
        item.setQuantity(wsItem.getQuantity());
        final Optional<Shop> shop = shopRepository.findById(wsItem.getId());
        assert shop.isPresent();
        item.setShop(shop.get());
        return item;
    }

    public List<WsItem> toWsItems(final List<Item> items){
        final List<WsItem> wsItems = new ArrayList<>();
        items.forEach(item -> {
            final WsItem wsItem = new WsItem();
            wsItem.setName(item.getName());
            wsItem.setPrice(item.getPrice());
            wsItem.setQuantity(item.getQuantity());
            wsItem.setDescription(item.getDescription());
            wsItem.setImageUrl(item.getImageUrl());
            wsItem.setShopId(item.getShop().getId());
            wsItems.add(wsItem);
        });
        return wsItems;
    }

    public List<Item> getAllItem(){
        return itemRepository.findAll();
    }

    public List<Item> getItemsByNameAndShopId(final String name, final Long shopId){
//        String SQL_QUERY = ItemRepository.SELECT_ITEM + ItemRepository.WHERE + ItemRepository.By_NAME_SQL;
//        if(shopId != null){
//            SQL_QUERY += ItemRepository.AND + ItemRepository.By_SHOP_ID_SQL;
//            SQL_QUERY = String.format(SQL_QUERY, name, shopId);
//        }else{
//            SQL_QUERY = String.format(SQL_QUERY, name);
//        }
//
//        return itemRepository.runNativeQuery(SQL_QUERY);
        if(shopId == null){
            return itemRepository.findByNameContainingIgnoreCase(name);
        }else{
            return itemRepository.findByNameContainingIgnoreCaseAndShopId(name, shopId);
        }
    }
}
