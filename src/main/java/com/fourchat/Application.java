package com.fourchat;

import com.fourchat.domain.ports.ChatService;
import com.fourchat.domain.ports.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private final ChatService chatService;
    private final UserService userService;

    @Autowired
    public Application(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

//
//        UserRepository userRepository = new UserRepositoryImpl();
//        UserService userService = new UserServiceImpl(userRepository);
//
//        ChatRepository chatRepository = new ChatRepositoryImpl();
//        ChatService chatService = new ChatServiceImpl(chatRepository, userService);
//
//
//        User carlos = userService.createBasicUser("Carlos", "user1@email.com");
//        User raul = userService.createBasicUser("Raul", "user2@email.com");
//
//
//
//        // Create a chat between Carlos and Raul
//        System.out.println("------- Creating a chat between Carlos and Raul ----------------");
//        // Send Messages
//        Message message1 = new TextMessage(carlos, "Hello Raul ", new Date());
//
//        chatService.sendMessage(carlos.getUserName(), message1, raul.getUserName());
//        Chat carlosChat = chatService.getChatsFromUser(carlos.getUserName()).getFirst();
//        int size = carlosChat.getMessages().size();
//
//        Message message2 = new TextMessage(raul, "Hello Carlos", new Date());
//        chatService.sendMessage(raul.getUserName(), message2, carlos.getUserName());
//        carlosChat = chatService.getChatsFromUser(carlos.getUserName()).getFirst();
//
//        Message message3 = new TextMessage(carlos, "I'm creating a chat app", new Date());
//        chatService.sendMessage(carlos.getUserName(), message3, raul.getUserName());
//
//        // Update the first message
//        Message messageToUpdate = carlosChat.getMessages().getFirst();
//        Message messageUpdated = new TextMessage(raul, "Hello Carlos, how are you?", new Date());
//        messageUpdated.setId(messageToUpdate.getId());
//        chatService.updateMessageInChat(carlosChat.getId(), messageUpdated);
//
//        size = carlosChat.getMessages().size();
//        carlosChat = chatService.getChatsFromUser(carlos.getUserName()).getFirst();
//
//
//        // Remove message from chat
//        Message messageToDelete = carlosChat.getMessages().get(2);
//        boolean messageDeleted = chatService.removeMessageFromChat(carlosChat.getId(), messageToDelete.getId());
//        size = carlosChat.getMessages().size();
//        carlosChat = chatService.getChatsFromUser(carlos.getUserName()).getFirst();
//
//
//        // Create another Chat
//        System.out.println("------- Creating another chat between Carlos and Malek ----------------");
//
//        User malek = userService.createBasicUser("malek", "malek@gmail.com");
//        Message message4 = new TextMessage(carlos, "Hello Malek", new Date());
//        chatService.sendMessage(carlos.getUserName(), message4, malek.getUserName());
//
//        carlosChat = chatService.getChatsFromUser(carlos.getUserName()).get(1);
//        Chat malekChat = chatService.getChatsFromUser(malek.getUserName()).getFirst();
//
//
//        // Create a group chat
//        System.out.println("------- Creating a group chat ----------------");
//
//        Chat groupChat = chatService.createGroupChat(Arrays.asList(carlos.getUserName(), raul.getUserName(), malek.getUserName()), Arrays.asList(carlos.getUserName()), "The Avengers", "The earth's protectors");
//        carlosChat = chatService.getChatsFromUser(carlos.getUserName()).getLast();
//
//        // Send message to the group
//        System.out.println("------- Sending a message to the group ----------------");
//        Message messageGroup1 = new TextMessage(carlos, "Hello everyone, Im Carlos", new Date());
//        chatService.sendMessage(groupChat.getId(), messageGroup1);
//
//        Message messageGroup2 = new TextMessage(raul, "Hello everyone, Im Raul", new Date());
//        chatService.sendMessage(groupChat.getId(), messageGroup2);
//        carlosChat = chatService.getChatsFromUser(carlos.getUserName()).getLast();
//
//        System.out.println("------- Updating a message in the group chat ----------------");
//        Message messageToUpdateGroup = carlosChat.getMessages().get(1);
//        Message messageUpdatedGroup = new TextMessage(raul, "Hello everyone, Im Raul and I'm updating my message", new Date());
//        messageUpdatedGroup.setId(messageToUpdateGroup.getId());
//        chatService.updateMessageInChat(groupChat.getId(), messageUpdatedGroup);
//
//        size = carlosChat.getMessages().size();
//        carlosChat = chatService.getChatsFromUser(carlos.getUserName()).getLast();
//
//        System.out.println("------- Removing a message from the group chat ----------------");
//
//        Message messageGroup3 = new TextMessage(raul, "This message will be removed", new Date());
//        chatService.sendMessage(groupChat.getId(), messageGroup3);
//        carlosChat = chatService.getChatsFromUser(carlos.getUserName()).getLast();
//
//        Message messageToDeleteGroup = carlosChat.getMessages().getLast();
//        boolean messageDeletedGroup = chatService.removeMessageFromChat(groupChat.getId(), messageToDeleteGroup.getId());
//        size = carlosChat.getMessages().size();
//
//        System.out.println("------- Updating the group chat description ----------------");
//        chatService.updateGroupChatDescription(groupChat.getId(), "This is the new description of the group chat");
//        carlosChat = chatService.getChatsFromUser(carlos.getUserName()).getLast();
//
//        System.out.println("------- Updating the group chat title -------");
//        chatService.updateGroupChatName(groupChat.getId(), "The Avengers: Earth's Mightiest Heroes");
//        carlosChat = chatService.getChatsFromUser(carlos.getUserName()).getLast();
//
//
//        System.out.println("------- Removing a participant from the group chat ----------------");
//        // THIS USER IS NOT AN ADMIN
//        chatService.removeParticipantFromGroupChat(groupChat.getId(), raul.getUserName(), malek.getUserName());
//        carlosChat = chatService.getChatsFromUser(carlos.getUserName()).getLast();
//
//        // THIS USER IS AN ADMIN
//        chatService.removeParticipantFromGroupChat(groupChat.getId(), carlos.getUserName(), malek.getUserName());
//        carlosChat = chatService.getChatsFromUser(carlos.getUserName()).getLast();
//
//
//        System.out.println("------- Making a participant an admin of the group chat ----------------");
//        chatService.makeParticipantAdmin(groupChat.getId(), carlos.getUserName(), raul.getUserName());
//        carlosChat = chatService.getChatsFromUser(carlos.getUserName()).getLast();
//
//        System.out.println("------- Removing a participant from admin of the group chat ----------------");
//        chatService.removeParticipantFromAdmins(groupChat.getId(), carlos.getUserName(), raul.getUserName());
//        carlosChat = chatService.getChatsFromUser(carlos.getUserName()).getLast();
//
//
//        System.out.println("------- Adding a participant to the group chat ----------------");
//        chatService.addParticipantToGroupChat(groupChat.getId(), carlos.getUserName(), malek.getUserName());
//        carlosChat = chatService.getChatsFromUser(carlos.getUserName()).getLast();

    }

    @Override
    public void run(String... args) throws Exception {

    }
}
