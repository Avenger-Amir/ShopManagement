package org.example.Repository;

import org.example.DbModels.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    public static final String SELECT_ITEM = " SELECT * FROM item ";
    public static final String By_NAME_SQL = " name like %'%s'%";
    public static final String By_SHOP_ID_SQL = " shop_id = '%d'";
    public static final String WHERE = " WHERE ";
    public static final String AND = " AND ";


    Item findByNameAndShopId(String name, Long shopId);

//    List<Item> findByNameContaining(String name);
    List<Item> findByNameContainingIgnoreCase(String name);
    List<Item> findByNameContainingIgnoreCaseAndShopId(String name, Long shop_id);

    Item findByName(String name);

    List<Item> findByShopId(Long shopId);


//    @Query(value = "SELECT * FROM item WHERE name like  AND shop_id = :shopId", nativeQuery = true)
//    List<Item> runNativeQuery(@Param("name") String name, @Param("shopId") Long shopId);
//
//    List<Item> runNativeQuery(String sql);

//    List<Item> findAllById(final List<Long> ids);
}
