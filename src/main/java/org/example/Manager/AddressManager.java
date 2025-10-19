package org.example.Manager;

import org.example.DbModels.Address;
import org.example.Repository.AddressRepository;
import org.example.WsModels.WsAddress;
import org.example.enums.AddressableType;
import org.springframework.stereotype.Component;

@Component
public class AddressManager {

    private final AddressRepository addressRepository;

    public AddressManager(final AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address toAddress(final WsAddress wsAddress, final AddressableType addressableType, final long addressableId) {
        final Address address = new Address();
        address.setStreet(wsAddress.getStreet());
        address.setCity(wsAddress.getCity());
        address.setPostalCode(wsAddress.getPostalCode());
        address.setAddressableId(addressableId);
        return address;
    }

    public Address save(final Address address) {
        // Placeholder for saving the address to the database
        // In a real implementation, this would interact with a repository
        addressRepository.save(address);
        return address;
    }
}
