package org.example.Repository;

import org.example.DbModels.Shopkeeper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopKeeperRepository extends JpaRepository<Shopkeeper, Long>  {
    Shopkeeper findByMobileNumber(String mobileNumber);
    Shopkeeper findByMobileNumberAndPassword(String mobileNumber, String password);
}
