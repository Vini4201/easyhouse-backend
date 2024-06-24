package net.easyhouse.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import net.easyhouse.exception.PropertyAlreadyExistsException;
import net.easyhouse.model.Property;
import net.easyhouse.service.PropertyService;


@RestController
@RequestMapping("/easyhouse") //changed from /api/v1 to /easyhouse
@CrossOrigin("http://localhost:4200")
public class PropertyController {

    @Autowired
    private PropertyService propertyService;

//    @GetMapping("/properties") // initially for seller
//    public List<Property> getAllProperties() {
//        return propertyService.getAllProperties();
//    }
    
    @GetMapping("/properties") // admin
    public List<Property> getAllProperties() {
    return propertyService.getAllProperties();
    }

//    @GetMapping("/properties/{id}") // initially had added for seller
//    public ResponseEntity<Property> getPropertyById(@PathVariable Long id) {
//        Property property = propertyService.getPropertyById(id);
//        return ResponseEntity.ok(property);
//    }
    
    @GetMapping("/properties/{id}") // admin
    public Optional<Property> getPropertyById(@PathVariable Integer id) {
    return propertyService.getPropertyById(id);
    }

    
    @GetMapping("/properties/seller/{sellerId}") // seller
    public List<Property> getPropertiesBySellerId(@PathVariable Integer sellerId) {
        return propertyService.getPropertiesBySellerId(sellerId);
    }
    
    
    @PostMapping("/properties") // seller
    public ResponseEntity<?> createProperty(@RequestBody Property property) {
        try {
            boolean result = propertyService.findIfPropertyExists(property.getPropertyName());
            if (result) {
                propertyService.addProperty(property);
                return ResponseEntity.status(HttpStatus.CREATED).body(property);
            }
        } catch (PropertyAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
    }


//    @PostMapping("/properties")
//    public Property createProperty(@RequestBody Property property) {
//        return propertyService.createProperty(property);
//    }
    
//    @PostMapping("/properties")
//    public ResponseEntity<Property> createProperty(@RequestBody Property property) {
//        Property createdProperty = propertyService.createProperty(property);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdProperty);
//    }

    @PutMapping("/properties/{id}") //seller
    public ResponseEntity<Property> updateProperty(@PathVariable Integer id, @RequestBody Property propertyDetails) {
        Property updatedProperty = propertyService.updateProperty(id, propertyDetails);
        return ResponseEntity.ok(updatedProperty);
    }

    @DeleteMapping("/properties/{id}") //seller
    public ResponseEntity<Map<String, Boolean>> deleteProperty(@PathVariable Integer id) {
        propertyService.deleteProperty(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
