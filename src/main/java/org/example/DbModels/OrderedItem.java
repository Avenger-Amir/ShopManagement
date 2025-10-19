package org.example.DbModels;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name="ordered_item")
@Entity
@Getter
@Setter
public class OrderedItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name="shop_order_id", nullable = false)
    private ShopOrder shopOrder;

    @Column(name="quantity", nullable = false)
    private Long quantity;

    @Column(name="price", nullable = false)
    private double price;
}
