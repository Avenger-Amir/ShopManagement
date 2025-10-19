package org.example.Repository;

import org.example.DbModels.Item;
import org.example.DbModels.OrderedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderedItemRepository extends JpaRepository<OrderedItem, Long> {
}
