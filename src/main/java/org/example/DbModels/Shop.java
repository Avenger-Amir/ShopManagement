package org.example.DbModels;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name="shop")
@Entity
@Getter
@Setter
public class Shop {

    @Id
//    @SequenceGenerator(
//            name = "shop_id_generator",
//            sequenceName = "shop_id_seq",
//            allocationSize = 50,
//            initialValue = 50 // optional, not always respected by all DBs
//    )
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "shop_id_generator"
//    )
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="name", nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private ShopUser owner;

//    @Column(name = "owner_id", nullable = false)
//    private Long ownerId;

    @Column(name="address", nullable = false)
    private String address;
}
