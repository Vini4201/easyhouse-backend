package net.easyhouse.repository;

import net.easyhouse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Admin_user_repository extends JpaRepository<User, Integer> {
}