package org.example.DbModels;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Table(name="shop_order")
@Entity
@Setter
@Getter
public class ShopOrder {

    @Id
//    @SequenceGenerator(
//            name = "shop_order_id_generator",
//            sequenceName = "shop_order_id_seq",
//            allocationSize = 50,
//            initialValue = 50 // optional, not always respected by all DBs
//    )
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "shop_order_id_generator"
//    )
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private ShopUser user;

    @Column(name="item_pojo", nullable=false)
    private String itemPojoList;

    @Column(name="timestamp", nullable = false)
    private Timestamp timestamp;

    @OneToOne
    @JoinColumn(name="shop_id", nullable = false)
    private Shop shop;
}
