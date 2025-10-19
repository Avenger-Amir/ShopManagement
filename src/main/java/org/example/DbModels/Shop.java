package org.example.DbModels;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany
    @JoinColumn(
            name = "addressable_id", // This is the column in the 'address' table
            referencedColumnName = "id" // This is the PK column in this 'shopkeeper' table
    )
    // Add this @Where clause to filter for the correct type
    @Where(clause = "addressable_type = 'SHOP'") // Use the ENUM string value
    private List<Address> addresses = new ArrayList<>();
}
