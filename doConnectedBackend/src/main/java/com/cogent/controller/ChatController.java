package com.cogent.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cogent.model.Chat;
import com.cogent.model.ChatDto;
import com.cogent.service.ChatService;

@RestController
@CrossOrigin(origins ="http://localhost:4200")
@RequestMapping("/chat")
public class ChatController {


    @Autowired
    private ChatService chatService;

    @PostMapping("/addmsg")
    public Chat addChatMessage(@RequestBody ChatDto chat) {
        return chatService.addChatMessage(chat);
    }

    @DeleteMapping("/DeleteChatById")
    public boolean deleteChatMessageById(@RequestParam int id) {
        return chatService.deleteChatMessageById(id);
    }

    @GetMapping("/getallmsg")
    public List<Chat> getAllChatMessages() {
        return chatService.getAllChatMessages();
    }
    
    @GetMapping("/getmessages/{from}/{to}")
    public List<Chat> getAllChatMessages(@PathVariable String from, @PathVariable String to) {
        return chatService.getChatMessagesFromTo(from, to);
    }
    
    
}
