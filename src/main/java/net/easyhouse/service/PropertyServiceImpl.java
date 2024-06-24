package net.easyhouse.service;

import net.easyhouse.model.Property;
import net.easyhouse.repository.PropertyRepository;
import net.easyhouse.exception.PropertyAlreadyExistsException;
//import net.housing.springboot.exception.DuplicatePropertyException;
import net.easyhouse.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
//import java.util.Optional;

@Service
public class PropertyServiceImpl implements PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

//    @Override
//    public List<Property> getAllProperties() { // seller (initially, not now) and admin
//        return propertyRepository.findAll();
//    }
    
    public List<Property> getAllProperties() { // admin
    	 return propertyRepository.findAll();
    	 }

//    @Override
//    public Property getPropertyById(Long id) throws ResourceNotFoundException { // initally for seller
//        return propertyRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
//    }
    
    public Optional<Property> getPropertyById(Integer id) { //admin
    	 return propertyRepository.findById(id);
    	 }

    
    // DIFFERENT NAME
    
    @Override
    public boolean findIfPropertyExists(String propertyName) throws PropertyAlreadyExistsException { //seller
        Property existingProperty = propertyRepository.findByPropertyName(propertyName);
        if (existingProperty != null) {
            throw new PropertyAlreadyExistsException("Enter a unique property value");
        }
        return true;
    }

    @Override
    public Property addProperty(Property property) throws PropertyAlreadyExistsException { //seller
        findIfPropertyExists(property.getPropertyName());
        return propertyRepository.save(property);
    }
    
    
    // CHECK FOR DUPLICATE NAME AND URL
    
//    @Override
//    public Property createProperty(Property property) throws DuplicatePropertyException {
//        Optional<Property> existingProperty = propertyRepository.findByNameOrImageUrl(property.getPropertyName(), property.getPropertyImageUrl());
//        if (existingProperty.isPresent()) {
//            throw new DuplicatePropertyException("Property with the same name or image URL already exists");
//        }
//        return propertyRepository.save(property);
//    }

    
//    @Override
//    public Property createProperty(Property property) {
//        return propertyRepository.save(property);
//    }
    
    

    @Override
    public Property updateProperty(Integer id, Property propertyDetails) throws ResourceNotFoundException { //seller
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));

        property.setSellerId(propertyDetails.getSellerId());
        property.setPropertyName(propertyDetails.getPropertyName());
        property.setPropertyType(propertyDetails.getPropertyType());
        property.setPropertyCost(propertyDetails.getPropertyCost());
        property.setPropertyArea(propertyDetails.getPropertyArea());
        property.setPropertyAddress(propertyDetails.getPropertyAddress());
        property.setPropertyDescription(propertyDetails.getPropertyDescription());
        property.setPropertyImageUrl(propertyDetails.getPropertyImageUrl());
        property.setPropertyStatus(propertyDetails.getPropertyStatus());
        property.setPropertyPostDate(propertyDetails.getPropertyPostDate());

        return propertyRepository.save(property);
    }

    @Override
    public void deleteProperty(Integer id) throws ResourceNotFoundException { 
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
        propertyRepository.delete(property);
    }
    
    
    @Override
    public List<Property> getPropertiesBySellerId(Integer sellerId) { //seller
        return propertyRepository.findBySellerId(sellerId);
    }
    
    
//    public Property saveProperty(Property property) { //admin
//        return propertyRepository.save(property);
//    }
    
//    @Override
//    public Optional<Property> findByNameOrImageUrl(String propertyName, String propertyImageUrl) {
//        return propertyRepository.findByNameOrImageUrl(propertyName, propertyImageUrl);
//    }
    
}