package org.example.DbModels;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Table(name = "shop_user")
@Entity
@Setter
@Getter
public class ShopUser {

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

    @Column(name="email_id", unique=true)
    private String emailId;

    @Column(name="mobile_number", unique=true)
    private String mobileNumber;

    @Column(name="name", nullable = false)
    private String userName;

    @Column(name="password", nullable = false)
    private String password;

    @OneToMany
    @JoinColumn(
            name = "addressable_id", // This is the column in the 'address' table
            referencedColumnName = "id" // This is the PK column in this 'shopkeeper' table
    )
    // Add this @Where clause to filter for the correct type
    @Where(clause = "addressable_type = 'USER'") // Use the ENUM string value
    private List<Address> addresses = new ArrayList<>();
}
