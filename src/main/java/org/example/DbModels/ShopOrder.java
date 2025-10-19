package org.example.DbModels;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.ShopOrderStatus;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


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

    @Column(name="timestamp", nullable = false)
    private Instant timestamp;

    @OneToOne
    @JoinColumn(name="shop_id", nullable = false)
    private Shop shop;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private ShopOrderStatus status;

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "orderedItem",
            fetch = FetchType.LAZY,
            targetEntity = OrderedItem.class)
    private List<OrderedItem> orderedItems = new ArrayList<>();
}
