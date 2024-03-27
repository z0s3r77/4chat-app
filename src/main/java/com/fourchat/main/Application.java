package com.fourchat.main;

import com.fourchat.application.adapters.ChatServiceImpl;
import com.fourchat.domain.models.*;
import com.fourchat.domain.ports.ChatRepository;
import com.fourchat.domain.ports.ChatService;
import com.fourchat.infrastructure.adapters.ChatRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import java.util.List;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

		User user1 = new BasicUser("Carlos", "user1@email.com");
		User user2 = new BasicUser("Raul", "user2@email.com");

		ChatRepository chatRepository = new ChatRepositoryImpl();
		ChatService chatService = new ChatServiceImpl(chatRepository);

		Message message = new TextMessage(user1, "Hello ma frien, Raul", new Date());

		IndividualChat chat = (IndividualChat) chatService.sendMessage(user1, message, user2);

		Message message2 = new TextMessage(user2, "Hello ma frien, Carlos, de vuelta", new Date());
		Message message3 = new TextMessage(user1, "Como está la familia?", new Date());
		Message message4 = new TextMessage(user1, "Y los niños?", new Date());

		chatService.sendMessage(user2, message2, user1);
		chatService.sendMessage(user1, message3, user2);
		chatService.sendMessage(user1, message4, user2);

		System.out.println("-----------------------------Un nuevo chat ----------------------------");
		User user3 = new BasicUser("Sebas", "user3@email.com");
		User user4 = new BasicUser("Alma", "user4@email.com");

		Message message5 = new TextMessage(user3, "Hola Alma", new Date());
		Message message6 = new TextMessage(user3, "Como has estado?", new Date());

		chatService.sendMessage(user3, message5, user4);
		chatService.sendMessage(user3, message6, user4);

		Message message7 = new TextMessage(user4, "Muy bien y tú?", new Date());
		chatService.sendMessage(user4, message7, user3);

		System.out.println("-----------------------------Respuesta anterior chat----------------------------");
		Message message8 = new TextMessage(user1, "Los niños bien, yo Genial! ", new Date());
		chatService.sendMessage(user1, message8, user2);

		System.out.println("-----------------------------Actualizar mensaje en un chat----------------------------");

		chatService.updateMessageInChat(chat.getId(), "3", new TextMessage(user1, "como están los perros?*", new Date()));
		Message message9 = new TextMessage(user2, "Los perros están bien, gracias por preguntar", new Date());
		chatService.sendMessage(user2, message9, user1);

		Chat chat2 = chatService.findChat(user1, user2);

	}

}
