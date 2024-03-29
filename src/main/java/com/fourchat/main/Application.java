package com.fourchat.main;

import com.fourchat.application.adapters.ChatServiceImpl;
import com.fourchat.domain.models.*;
import com.fourchat.domain.ports.ChatRepository;
import com.fourchat.domain.ports.ChatService;
import com.fourchat.infrastructure.adapters.ChatRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

		User carlos = new BasicUser("Carlos", "user1@email.com");
		User raul = new BasicUser("Raul", "user2@email.com");

		ChatRepository chatRepository = new ChatRepositoryImpl();
		ChatService chatService = new ChatServiceImpl(chatRepository);



		// Create a chat between Carlos and Raul
		Message message1 = new TextMessage(carlos, "Hello Raul ", new Date());

		chatService.sendMessage(carlos, message1, raul);
		Chat carlosChat = chatService.getChats(carlos).getFirst();
		int size = carlosChat.getMessages().size();

		Message message2 = new TextMessage(raul, "Hello Carlos", new Date());
		chatService.sendMessage(raul, message2, carlos);
		carlosChat = chatService.getChats(carlos).getFirst();



		// Update the first message
		Message messageToUpdate = carlosChat.getMessages().getFirst();
		Message messageUpdated = new TextMessage(raul, "Hello Carlos, how are you?", new Date());
		messageUpdated.setId(messageToUpdate.getId());
		chatService.updateMessageInChat(carlosChat.getId(),  messageUpdated);

		size = carlosChat.getMessages().size();
		carlosChat = chatService.getChats(carlos).getFirst();





	}

}
