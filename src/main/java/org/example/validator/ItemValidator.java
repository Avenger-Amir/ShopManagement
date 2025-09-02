package org.example.validator;

import jakarta.ws.rs.BadRequestException;
import org.example.DbModels.Item;
import org.example.Repository.ItemRepository;
import org.example.WsModels.WsItem;
import org.example.exceptions.ExceptionUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemValidator {

    final ItemRepository itemRepository;

    ItemValidator(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void validateAllItemExisting(final List<WsItem> wsItems) {
        wsItems.forEach(wsItem -> {
            if(wsItem.getId() == null) {
                throw ExceptionUtil.error("id is null", "400");
            }
        });

        final List<Long> wsItemIds = wsItems.stream().map(WsItem::getId).collect(Collectors.toList());
        final List<Item> items = itemRepository.findAllById(wsItemIds);
        final List<Long> itemIds = items.stream().map(Item::getId).collect(Collectors.toList());
        if (!itemIds.containsAll(wsItemIds)) {
            throw ExceptionUtil.error("itemIds not existing", "400");
        }
    }

    public void validateItemBelongsToSameShop(final List<WsItem> wsItem, final Long shopId) {
        final List<Long> wsItemIds = wsItem.stream().map(WsItem::getId).collect(Collectors.toList());
        final List<Item> items = itemRepository.findAllById(wsItemIds);
        final boolean isItemBelongingToDifferentShop = items.stream().anyMatch(wsItem1 -> !wsItem1.getShop().getId().equals(shopId));
        if (isItemBelongingToDifferentShop) {
            throw ExceptionUtil.error("item belongs to different shop", "400");
        }
    }
}
