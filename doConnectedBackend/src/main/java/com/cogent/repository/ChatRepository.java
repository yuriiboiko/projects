package com.cogent.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cogent.model.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

	@Query("SELECT c FROM Chat c WHERE c.from_user = ?1 AND c.to_user = ?2 ORDER BY c.datetime")
	List<Chat> findAllByFromUser(String fromUser,String toUser);
	
	
}