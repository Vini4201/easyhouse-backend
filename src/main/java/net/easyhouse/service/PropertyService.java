package net.easyhouse.service;

import net.easyhouse.model.Property;
import net.easyhouse.exception.PropertyAlreadyExistsException;
//import net.housing.springboot.exception.DuplicatePropertyException;
import net.easyhouse.exception.ResourceNotFoundException;
import java.util.List;
//import java.util.Optional;
import java.util.Optional;

public interface PropertyService {
    List<Property> getAllProperties();
//    Property getPropertyById(Long id) throws ResourceNotFoundException; // initially for seller
    Optional<Property> getPropertyById(Integer id);
//    Property createProperty(Property property) throws DuplicatePropertyException;
//    Optional<Property> findByNameOrImageUrl(String propertyName, String propertyImageUrl); // Declare method
    boolean findIfPropertyExists(String propertyName) throws PropertyAlreadyExistsException;
    Property addProperty(Property property) throws PropertyAlreadyExistsException;
//    Property createProperty(Property property) throws PropertyAlreadyExistsException;
    Property updateProperty(Integer id, Property propertyDetails) throws ResourceNotFoundException;
    void deleteProperty(Integer id) throws ResourceNotFoundException;
    List<Property> getPropertiesBySellerId(Integer sellerId);
}

