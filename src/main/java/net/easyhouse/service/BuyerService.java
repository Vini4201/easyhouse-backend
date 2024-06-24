package net.easyhouse.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import net.easyhouse.exception.CartNotEmptyException;
import net.easyhouse.model.Booking;
import net.easyhouse.model.Booking.PaymentMethod;
import net.easyhouse.model.Booking.PaymentStatus;
import net.easyhouse.model.Cart;
import net.easyhouse.model.Property;
import net.easyhouse.model.PropertyStatus;
import net.easyhouse.model.User;
import net.easyhouse.repository.BookingRepository;
import net.easyhouse.repository.CartRepository;
import net.easyhouse.repository.PropertyRepository;
import net.easyhouse.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class BuyerService {

	@Autowired	
	private PropertyRepository propertyRepository;
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private UserRepository userRepository;

	
	
	public List<Booking> getBookingsByBuyerId(Integer buyerId)
	{
		return bookingRepository.findByBuyerId(buyerId);
	}
	
	public User getBuyerById(Integer buyerId)
	{
		return userRepository.findById(buyerId).orElse(null);
	}
	
	public List<Cart> getCartItemsByBuyerId(Integer buyerId)
	{
		return cartRepository.findByBuyerId(buyerId);
	}
	
	public List<Property> getAllProperties()
	{
		return propertyRepository.findAll();
	}
	
	public Property getPropertyById(Integer propertyId) {
        Optional<Property> optionalProperty = propertyRepository.findById(propertyId);
        
        if (optionalProperty.isPresent()) {
            return optionalProperty.get(); 
        } else {
            throw new EntityNotFoundException("Property not found for id: " + propertyId);
        }
    }
	
	public Cart addToCart(Integer buyerId, Integer propertyId)
	{
		//Check if the property is already booked
		if(bookingRepository.existsByBuyerIdAndPropertyId(buyerId, propertyId)) {
			throw new IllegalStateException("Property is already booked by this buyer:" + propertyId);
		}
		
		//Check if the property is already in the car
		if(cartRepository.existsByBuyerIdAndPropertyId(buyerId, propertyId)) {
			throw new IllegalStateException("Property already exists in the cart:" + propertyId);
		}
		
		//Check property Status
		Property property = propertyRepository.findById(propertyId)
							.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Property not found"));
		
//		if(property.getPropertyStatus() != Property.Status.AVAILABLE) {
//			throw new IllegalStateException("Property is not available: " + propertyId);
//		}
		
		if(property.getPropertyStatus() != PropertyStatus.AVAILABLE) {
			throw new IllegalStateException("Property is not available: " + propertyId);
		}
		
		if(!userRepository.existsById(buyerId))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Buyer not found");
		
		Cart cartItem = new Cart();
		cartItem.setBuyerId(buyerId);
		cartItem.setProperty(property);
		cartItem.setPropertyName(property.getPropertyName());
		cartItem.setPropertyCost(property.getPropertyCost());
		
		return cartRepository.save(cartItem);
	}
	
	@Transactional
	public List<Booking> purchaseProperties(Integer buyerId, List<Integer> cartItemIds,Booking.PaymentMethod paymentMethod)
	{
		List<Booking> bookings = new ArrayList<>();
		
		for(Integer cartItemId: cartItemIds) {
			Optional<Cart> optionalCart = cartRepository.findById(cartItemId);
			if(optionalCart.isPresent()) {
				Cart cartItem = optionalCart.get();
				
				//Check property availability before booking
//				Property property = cartItem.getProperty();
//				if(property.getPropertyStatus() != Property.Status.AVAILABLE) {
//					throw new IllegalStateException("Property is not available: " + property.getId());
//				}
				
				Property property = cartItem.getProperty();
				if(property.getPropertyStatus() != PropertyStatus.AVAILABLE) {
					throw new IllegalStateException("Property is not available: " + property.getId());
				}
				
				
				//Created Booking object to store the bookings in the database
				Booking booking = createBooking(buyerId,cartItem,paymentMethod);
				bookings.add(booking);
				
				//Changing the property availability status
				property.setPropertyStatus(PropertyStatus.SOLD);
				propertyRepository.save(property);
				
				
				//Removing the property from the cart
				cartRepository.delete(cartItem);
			}
		}
		return bookings;
	}
	
	public boolean isPropertyInCart(Integer buyerId,Integer propertyId)
	{
		return cartRepository.existsByBuyerIdAndPropertyId(buyerId, propertyId);
	}

	
	public void clearCart(Integer buyerId) 
	{
		List<Cart> cartItems = cartRepository.findByBuyerId(buyerId);
		if(cartItems != null && !cartItems.isEmpty()) {
			throw new CartNotEmptyException("Cart is not empty. Please confirm to clear the cart");
		}
		cartRepository.deleteAll(cartItems);
	}
	
	//Method to create Bookings
	public Booking createBooking(Integer buyerId, Cart cartItem, PaymentMethod paymentMethod)
	{
		//Check property availability
		
		Property property = cartItem.getProperty();
		//Generate order id
		String orderId = generateOrderId();
		
		
		//Creating Booking Object
		Booking booking = new Booking();
		booking.setBuyerId(buyerId);
		booking.setProperty(property);
		booking.setPropertyName(property.getPropertyName());
		booking.setPropertyCost(property.getPropertyCost());
		booking.setPaymentMethod(paymentMethod);
		booking.setPaymentStatus(PaymentStatus.PENDING);
		booking.setOrderId(orderId);
		booking.setPropertyBuyDate(new java.sql.Date(System.currentTimeMillis()));
		
		return bookingRepository.save(booking);
	}
	
	//Method to generate random order id
	private String generateOrderId()
	{
		return UUID.randomUUID().toString().substring(0,6);
	}
}