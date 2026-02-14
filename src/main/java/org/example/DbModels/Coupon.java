package org.example.DbModels;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Table(name="coupon")
@Entity
@Getter
@Setter
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code; // e.g., "WELCOME50"

    @Column(name = "discount_percentage")
    private Double discountPercentage; // e.g., 10.0 for 10%

    @Column(name= "max_discount_amount", nullable = false)
    private Double maxDiscountAmount;  // e.g., 100.0

    @Column(name = "min_order_amount", nullable = false)
    private Double minOrderAmount;     // e.g., 500.0

    @Column(name = "expiry_date")
    private Instant expiryDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="shop_id", nullable = false, referencedColumnName = "id")
    private Shop shop;

    @Column(name="usage_count", nullable = false)
    private Integer usageCount = 0;

    @Column(name="usage_limit", nullable = false)
    private Integer usageLimit = 1; // default to 1 use per coupon
}