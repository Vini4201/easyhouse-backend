package net.easyhouse.repository;

import java.util.List;
//import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.easyhouse.model.Property;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Integer> {
	
	List<Property> findBySellerId(Integer sellerId);
//  Optional<Property> findByNameOrImageUrl(String propertyName, String propertyImageUrl);
	Property findByPropertyName(String propertyName);

}