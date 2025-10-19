package org.example.DbModels;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.enums.AddressType;
import org.example.enums.AddressableType;

@Table(name="address")
@Entity
@Getter
@Setter
public class Address {

    @Id
//    @SequenceGenerator(
//            name = "shop_user_id_generator",
//            sequenceName = "shop_user_id_seq",
//            allocationSize = 50,
//            initialValue = 50 // optional, not always respected by all DBs
//    )
//    @GeneratedValue(
//            strategy = GenerationType.SEQUENCE,
//            generator = "shop_user_id_generator"
//    )
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @Column(name="addressable_id", nullable = false)
    private Long addressableId;

    @Enumerated(EnumType.STRING)
    @Column(name="addressable_type", nullable = false)
    private AddressableType addressableType;

    @Enumerated(EnumType.STRING)
    @Column(name="address_type", nullable = false)
    private AddressType addressType;

    @Column(name="street", nullable = false)
    private String street;

    @Column(name="city", nullable =false)
    private String city;

    @Column(name="postal_code", nullable = false)
    private String postalCode;

//    @Column(name="password", nullable = false)
//    private String password;
}
