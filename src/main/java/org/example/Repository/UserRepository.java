package org.example.Repository;

import org.example.DbModels.ShopUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<ShopUser, Long> {


    ShopUser findByMobileNumber(String mobileNumber);
}
