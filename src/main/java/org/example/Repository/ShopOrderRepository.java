package org.example.Repository;

import org.example.DbModels.ShopOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrder, Long> {
    List<ShopOrder> findAllByShop_IdAndInstantIsBetween(final Long shopId, Instant timestampAfter, Instant timestampBefore);

    List<ShopOrder> finalAllByShopUser_IdAndInstantIsBetween(final Long userId, final Instant timestampAfter, final Instant timestampBefore);
    List<ShopOrder> findAllByShop_Shopkeeper_IdAndInstantIsBetween(final Long shopkeeperId, final Instant timestampAfter, final Instant timestampBefore);
}
