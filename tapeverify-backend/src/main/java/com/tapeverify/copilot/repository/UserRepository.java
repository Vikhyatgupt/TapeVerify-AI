package com.tapeverify.copilot.repository;
import com.tapeverify.copilot.entity.AppUser; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<AppUser,Long> { Optional<AppUser> findByUsername(String username); }
