package org.example.Repository;

import org.example.DbModels.ShopOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ShopOrderRepository extends JpaRepository<ShopOrder, Long> {
    List<ShopOrder> findByShop_IdAndInstantIsBetween(Long shopId, Instant timestampAfter, Instant timestampBefore);
}
