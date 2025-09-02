package org.example.Manager;

import org.example.DbModels.*;
import org.example.Repository.ItemRepository;
import org.example.Repository.ShopOrderRepository;
import org.example.Repository.ShopRepository;
import org.example.WsModels.WsShopOrder;
import org.example.WsModels.WsShopOrderList;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
public class OrderManager {

    private final ItemRepository itemRepository;
    private final ShopRepository shopRepository;
    private final ShopOrderRepository shopOrderRepository;

    OrderManager(final ItemRepository itemRepository,
                 final ShopRepository shopRepository,
                 final ShopOrderRepository shopOrderRepository) {
        this.itemRepository = itemRepository;
        this.shopRepository = shopRepository;
        this.shopOrderRepository = shopOrderRepository;
    }

    synchronized public WsShopOrder bookOrder(final WsShopOrder wsShopOrder, final ShopUser user) {
//            final List<Item> items = shopOrderToItems(order);
            final Map<Long, Item> itemMap = shopOrderToItemsMap(wsShopOrder);
            final WsShopOrder bookedWsOrder = new WsShopOrder();
            bookedWsOrder.setShopId(wsShopOrder.getShopId());
            AtomicReference<Double> totalPrice= new AtomicReference<>((double) 0);
            final List<ItemPojo> itemPojoList = new ArrayList<>();
            wsShopOrder.getWsShopOrderList().forEach(order -> {
                final Item item = itemMap.get(order.getItemId());
                final Long bookedQuantity = Math.min(order.getQuantity(), item.getQuantity());
                item.setQuantity(item.getQuantity()-bookedQuantity);

                final WsShopOrderList wsShopOrderList = new WsShopOrderList();
                wsShopOrderList.setName(order.getName());
                wsShopOrderList.setPrice(order.getPrice());
                wsShopOrderList.setQuantity(bookedQuantity);
                wsShopOrderList.setItemId(order.getItemId());


                bookedWsOrder.getWsShopOrderList().add(wsShopOrderList);
                totalPrice.updateAndGet(v -> v + bookedQuantity * item.getPrice());
                final ItemPojo itemPojo = new ItemPojo();
                itemPojo.setItemName(item.getName());
                itemPojo.setItemQuantity(item.getQuantity());
                itemPojo.setItemPrice(item.getPrice());
                itemPojoList.add(itemPojo);
            });

            final ShopOrder shopOrder = new ShopOrder();
            shopOrder.setShop(shopRepository.getReferenceById(wsShopOrder.getShopId()));
            shopOrder.setTimestamp(Timestamp.from(Instant.now()));
            shopOrder.setUser(user);
            shopOrder.setItemPojoList(itemPojoList.toString());
            shopOrderRepository.saveAndFlush(shopOrder);

            final WsShopOrder bookedWsShopOrder = new WsShopOrder();
            bookedWsShopOrder.setShopId(wsShopOrder.getShopId());

            return wsShopOrder;
        }

    public WsShopOrder previewShopOrder(final WsShopOrder wsShopOrder){
        final Map<Long, Item> itemMap = shopOrderToItemsMap(wsShopOrder);
        final WsShopOrder previewedWsShopOrder = new WsShopOrder();
        previewedWsShopOrder.setShopId(wsShopOrder.getShopId());
        wsShopOrder.getWsShopOrderList().forEach(order -> {
            final Item item = itemMap.get(order.getItemId());
            final Long bookedQuantity = Math.min(order.getQuantity(), item.getQuantity());

            final WsShopOrderList previewedOrder = new WsShopOrderList();
            previewedOrder.setName(item.getName());
            previewedOrder.setPrice(item.getPrice());
            previewedOrder.setQuantity(bookedQuantity);
            previewedOrder.setItemId(order.getItemId());

            previewedWsShopOrder.getWsShopOrderList().add(previewedOrder);
        });
        return previewedWsShopOrder;
    }

    public Map<Long, Item> shopOrderToItemsMap(final WsShopOrder order) {
        final List<Long> itemIds = order.getWsShopOrderList().stream().map(WsShopOrderList::getItemId).toList();
        final List<Item> items = itemRepository.findAllById(itemIds);
        final Map<Long, Item> itemMap = items.stream().collect(Collectors.toMap(Item::getId, item -> item));
        return itemMap;
    }
}
