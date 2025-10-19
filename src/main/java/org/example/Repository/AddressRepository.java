package org.example.Repository;

import org.example.DbModels.Address;
import org.example.DbModels.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {


}
