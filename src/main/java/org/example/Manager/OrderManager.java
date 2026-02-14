package org.example.Manager;

import org.example.DbModels.*;
import org.example.Repository.*;
import org.example.WsModels.WsShopOrder;
import org.example.WsModels.WsShopOrderList;
import org.example.enums.ShopOrderStatus;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Component
public class OrderManager {

    private final ItemRepository itemRepository;
    private final ShopRepository shopRepository;
    private final ShopOrderRepository shopOrderRepository;
    private final OrderedItemRepository orderedItemRepository;
    private final CouponRepository couponRepository;

    OrderManager(final ItemRepository itemRepository,
                 final ShopRepository shopRepository,
                 final ShopOrderRepository shopOrderRepository,
                 final OrderedItemRepository orderedItemRepository,
                 final CouponRepository couponRepository) {
        this.itemRepository = itemRepository;
        this.shopRepository = shopRepository;
        this.shopOrderRepository = shopOrderRepository;
        this.orderedItemRepository = orderedItemRepository;
        this.couponRepository = couponRepository;
    }

    synchronized public WsShopOrder bookOrder(final WsShopOrder wsShopOrder, final ShopUser user) {
            final Map<Long, Item> itemMap = shopOrderToItemsMap(wsShopOrder);
            final WsShopOrder bookedWsOrder = new WsShopOrder();
            bookedWsOrder.setShopId(wsShopOrder.getShopId());
            AtomicReference<Double> totalPrice= new AtomicReference<>((double) 0);
            final List<OrderedItem> orderedItemList = new ArrayList<>();
        final ShopOrder shopOrder = new ShopOrder();
        wsShopOrder.getWsShopOrderList().forEach(order -> {
                final Item item = itemMap.get(order.getItemId());

                final WsShopOrderList wsShopOrderList = new WsShopOrderList();
                wsShopOrderList.setName(item.getName());
                wsShopOrderList.setPrice(item.getPrice());
                wsShopOrderList.setQuantity(order.getQuantity());
                wsShopOrderList.setItemId(order.getItemId());


                bookedWsOrder.getWsShopOrderList().add(wsShopOrderList);
                totalPrice.updateAndGet(v -> v + order.getQuantity() * item.getPrice());
                final OrderedItem orderedItem = new OrderedItem();
                orderedItem.setItem(item);
                orderedItem.setQuantity(order.getQuantity());
                orderedItem.setPrice(item.getPrice());
                shopOrder.addOrderedItem(orderedItem);
            });

            shopOrder.setShop(shopRepository.getReferenceById(wsShopOrder.getShopId()));
            shopOrder.setInstant(Instant.now());
            shopOrder.setUser(user);
            shopOrder.setStatus(ShopOrderStatus.PENDING);
            shopOrder.setStatus(wsShopOrder.getStatus());

        double subTotal = totalPrice.get();
        double discount = 0.0;
        double finalPrice = subTotal;

        if (wsShopOrder.getCouponCode() != null) {
            Coupon coupon = couponRepository.findByCode(wsShopOrder.getCouponCode());

            // Validate Coupon
            if (coupon != null && coupon.isActive() &&
                    coupon.getExpiryDate().isAfter(Instant.now()) &&
                    subTotal >= coupon.getMinOrderAmount()) {

                double calculatedDiscount = (subTotal * coupon.getDiscountPercentage()) / 100;
                discount = Math.min(calculatedDiscount, coupon.getMaxDiscountAmount());
            }
            finalPrice-= discount;
        }

//        double finalPrice = subTotal - discount;
// Save 'discount' and 'finalPrice' to your ShopOrder entity (add these fields to ShopOrder.java first!)
        shopOrder.setDiscount(discount);
        shopOrder.setFinalPrice(finalPrice);

            final ShopOrder savedShopOrder = shopOrderRepository.saveAndFlush(shopOrder);

            final WsShopOrder bookedWsShopOrder = new WsShopOrder();
            bookedWsShopOrder.setShopId(wsShopOrder.getShopId());
            bookedWsShopOrder.setStatus(wsShopOrder.getStatus());
            bookedWsShopOrder.setOrderId(savedShopOrder.getId());
            bookedWsShopOrder.setWsShopOrderList(bookedWsOrder.getWsShopOrderList());

            return bookedWsShopOrder;
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

    public List<WsShopOrderList> getMostSoldItems(final Long shopkeeperId, final Instant startTime, final Instant endTime){
        final List<ShopOrder> shopOrders = shopOrderRepository.findAllByShop_IdAndInstantIsBetween(shopkeeperId, startTime, endTime);
        return getCombinedOrdersByItemId(shopOrders);
    }

    private List<WsShopOrderList> getCombinedOrdersByItemId(final List<ShopOrder> shopOrders){
        final Map<Long, WsShopOrderList> itemIdToOrderMap = new HashMap<>();
        for (final ShopOrder shopOrder : shopOrders) {
            final List<OrderedItem> items = shopOrder.getOrderedItems();
            items.forEach(item -> {
                if(itemIdToOrderMap.containsKey(item.getItem().getId())){
                    final WsShopOrderList existingOrder = itemIdToOrderMap.get(item.getItem().getId());
                    existingOrder.setQuantity(existingOrder.getQuantity() + item.getQuantity());
                } else {
                    final WsShopOrderList newOrder = new WsShopOrderList();
                    newOrder.setItemId(item.getItem().getId());
                    newOrder.setName(item.getItem().getName());
                    newOrder.setQuantity(item.getQuantity());
                    itemIdToOrderMap.put(item.getItem().getId(), newOrder);
                }
            });
        }
        return itemIdToOrderMap.values().stream().toList();
    }

    public List<WsShopOrderList> getShopOrderByShopId(final Long shopId, final Instant startTime, final Instant endTime){
        final List<ShopOrder> shopOrders = shopOrderRepository.findAllByShop_IdAndInstantIsBetween(shopId, startTime, endTime);
        return getCombinedOrdersByItemId(shopOrders);
    }

    public List<WsShopOrderList> getShopOrderByUserId(final Long userId, final Instant startTime, final Instant endTime){
        final List<ShopOrder> shopOrders = shopOrderRepository.findAllByUser_IdAndInstantIsBetween(userId, startTime, endTime);
        return getCombinedOrdersByItemId(shopOrders);
    }

}
