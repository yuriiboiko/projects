package com.cogent.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cogent.model.Chat;
import com.cogent.model.ChatDto;
import com.cogent.repository.ChatRepository;


@Repository
public class ChatService {

	@Autowired
	private ChatRepository chatRepository;

	public Chat addChatMessage(ChatDto chat) {
		// Set the message's datetime to the current time
		Chat c= new Chat();
		c.setDatetime(LocalDateTime.now().toString());
		c.setFrom_user(chat.getFrom_user());
		c.setMessage(chat.getMessage());
		c.setTo_user(chat.getTo_user());
		return chatRepository.save(c);
	}

	public boolean deleteChatMessageById(int id) {
		Optional<Chat> optionalChat = chatRepository.findById(id);
		if (!optionalChat.isPresent()) {
			return false;
		}

		chatRepository.delete(optionalChat.get());
		return true;
	}

	public List<Chat> getAllChatMessages() {
		return chatRepository.findAll();
	}
	
	public List<Chat> getChatMessagesFromTo(String from,String to) {
		return chatRepository.findAllByFromUser(from,to);
	}
	
	
}
