package org.example.DbModels;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name="item")
@Entity
@Setter
@Getter
public class Item {

    @Id
//    @SequenceGenerator(
//            name = "item_id_generator",
//            sequenceName = "item_id_seq",
//            allocationSize = 50,
//            initialValue = 50 // optional, not always respected by all DBs
//    )
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "item_id_generator"
//    )
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="name", unique = true, nullable = false)
    private String name;

    @Column(name="description")
    private String description;

    @Column(name="quantity", nullable = false)
    private Long quantity;

    @Column(name="price", nullable = false)
    private double price;

    @OneToOne
    @JoinColumn(name="shop_id", nullable = false)
    private Shop shop;
}
