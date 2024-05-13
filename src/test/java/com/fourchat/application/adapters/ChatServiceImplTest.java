package com.fourchat.application.adapters;

import com.fourchat.domain.models.*;
import com.fourchat.domain.ports.ChatRepository;
import com.fourchat.domain.ports.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChatServiceImplTest {

    ChatRepository chatRepositoryMock = Mockito.mock(ChatRepository.class);
    UserService userServiceMock = Mockito.mock(UserService.class);
    ChatServiceImpl chatService = new ChatServiceImpl(this.chatRepositoryMock, this.userServiceMock);

    User user1 = new BasicUser("Carlos", "user1@email.com");
    User user2 = new BasicUser("Raul", "user2@email.com");

    @BeforeEach
    void setUp() {
        User user1 = new BasicUser("Carlos", "user1@email.com");
        User user2 = new BasicUser("Raul", "user2@email.com");

        when(userServiceMock.getUserByUserName(user1.getUserName())).thenReturn(Optional.of(user1));
        when(userServiceMock.getUserByUserName(user2.getUserName())).thenReturn(Optional.of(user2));
    }




    @Test
    void getChats() {

        User testUser = new BasicUser("Carlos", "user1@email.com");
        Chat chat1 = new IndividualChat(Arrays.asList(testUser, new BasicUser("Raul", "user2@email.com")), new Date());
        Chat chat2 = new IndividualChat(Arrays.asList(testUser, new BasicUser("Ana", "user3@email.com")), new Date());

        when(this.chatRepositoryMock.findByUserId(testUser.getUserName())).thenReturn(Arrays.asList(chat1, chat2));

        List<Chat> result = this.chatService.getChatsFromUser(testUser.getUserName());

        assertEquals(2, result.size(), "Should return 2 chats");
        assertTrue(result.contains(chat1), "Should contain the first chat");
        assertTrue(result.contains(chat2), "Should contain the second chat");
    }

    @Test
    void removeMessageFromChat_ChatDoesNotExist_ReturnsFalse() {

        String chatId = "123";
        String messageId = "456";

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.empty());

        boolean result = this.chatService.removeMessageFromChat(chatId, messageId);

        assertFalse(result);
    }

    @Test
    void removeMessageFromChat_MessageExists_ReturnsTrue() {

        String chatId = "123";
        String messageId = "0";

        Message message = new TextMessage(this.user1, "Hello Raul", new Date());
        message.setId(messageId);

        Chat chatExpected = new IndividualChat(Arrays.asList(this.user1, this.user2), new Date());
        chatExpected.setId(chatId);
        chatExpected.addMessage(message);

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(chatExpected));
        when(this.chatRepositoryMock.update(chatExpected)).thenReturn(true);

        boolean result = this.chatService.removeMessageFromChat(chatId, messageId);


        assertTrue(result);
    }

    @Test
    void removeMessageFromChat_MessageDoesNotExist_ReturnsFalse() {

        String chatId = "123";
        String messageId = "456";

        Message message = new TextMessage(this.user1, "Hello Raul", new Date());
        message.setId(messageId);

        Chat chatExpected = new IndividualChat(Arrays.asList(this.user1, this.user2), new Date());
        chatExpected.setId(chatId);
        chatExpected.addMessage(message);

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(chatExpected));

        boolean result = this.chatService.removeMessageFromChat(chatId, messageId);


        assertFalse(result);
    }


    @Test
    void createGroupChat() {

        List<User> participants = Arrays.asList(this.user1, this.user2);
        List<User> groupAdmin = Collections.singletonList(this.user1);
        String groupName = "Test Group";
        String description = "This is a test group";

        Chat groupChatExpected = new GroupChat(groupName, description, participants, groupAdmin, new Date());

        when(this.chatRepositoryMock.save(any(Chat.class))).thenReturn(groupChatExpected);

        List<String> participantsName = Arrays.asList(this.user1.getUserName(), this.user2.getUserName());
        List<String> groupAdminName = Collections.singletonList(this.user1.getUserName());

        Chat result = this.chatService.createGroupChat(participantsName, groupAdminName, groupName, description);

        assertEquals(groupChatExpected, result, "Should return the group chat created");
    }

    @Test
    void updateGroupChatDescription_ChatIsGroup_ReturnsTrue() {

        String chatId = "123";
        String newDescription = "New description";
        List<User> participants = Arrays.asList(this.user1, this.user2);
        List<User> groupAdmin = Collections.singletonList(this.user1);
        String groupName = "Test Group";
        String description = "This is a test group";

        GroupChat groupChatExpected = new GroupChat(groupName, description, participants, groupAdmin, new Date());
        groupChatExpected.setId(chatId);


        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(groupChatExpected));
        when(this.chatRepositoryMock.update(any(Chat.class))).thenReturn(true);


        boolean result = this.chatService.updateGroupChatDescription(chatId, newDescription);


        assertTrue(result);
    }

    @Test
    void updateGroupChatDescription_ChatIsNotGroup_ReturnsFalse() {

        String chatId = "123";
        String newDescription = "New description";

        Chat chatExpected = new IndividualChat(Arrays.asList(this.user1, this.user2), new Date());
        chatExpected.setId(chatId);


        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(chatExpected));


        boolean result = this.chatService.updateGroupChatDescription(chatId, newDescription);


        assertFalse(result);
    }


    @Test
    void updateGroupChatDescription_ChatDoesNotExist_ReturnsFalse() {

        String chatId = "123";
        String newDescription = "New description";


        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.empty());


        boolean result = this.chatService.updateGroupChatDescription(chatId, newDescription);


        assertFalse(result);
    }


    @Test
    void updateGroupChatName_ChatIsGroup_ReturnsTrue() {

        String chatId = "123";
        String newGroupName = "New Group Name";
        List<User> participants = Arrays.asList(this.user1, this.user2);
        List<User> groupAdmin = Collections.singletonList(this.user1);
        String groupName = "Test Group";
        String description = "This is a test group";

        GroupChat groupChatExpected = new GroupChat(groupName, description, participants, groupAdmin, new Date());
        groupChatExpected.setId(chatId);


        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(groupChatExpected));
        when(this.chatRepositoryMock.update(any(Chat.class))).thenReturn(true);


        boolean result = this.chatService.updateGroupChatName(chatId, newGroupName);


        assertTrue(result);
    }

    @Test
    void updateGroupChatName_ChatIsNotGroup_ReturnsFalse() {

        String chatId = "123";
        String newGroupName = "New Group Name";

        Chat chatExpected = new IndividualChat(Arrays.asList(this.user1, this.user2), new Date());
        chatExpected.setId(chatId);


        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(chatExpected));


        boolean result = this.chatService.updateGroupChatName(chatId, newGroupName);


        assertFalse(result);
    }


    @Test
    void updateGroupChatName_ChatDoesNotExist_ReturnsFalse() {

        String chatId = "123";
        String newGroupName = "New Group Name";

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.empty());

        boolean result = this.chatService.updateGroupChatName(chatId, newGroupName);

        assertFalse(result);
    }

    @Test
    void removeParticipantFromGroupChat_ChatIsGroup_AdminAndUserExist_ReturnsTrue() {

        String chatId = "123";
        String adminName = "Carlos";
        String userName = "Raul";
        List<User> participants = Arrays.asList(this.user1, this.user2);
        List<User> groupAdmin = Collections.singletonList(this.user1);
        String groupName = "Test Group";
        String description = "This is a test group";

        GroupChat groupChatExpected = new GroupChat(groupName, description, participants, groupAdmin, new Date());
        groupChatExpected.setId(chatId);

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(groupChatExpected));
        when(this.chatRepositoryMock.update(any(Chat.class))).thenReturn(true);

        boolean result = this.chatService.removeParticipantFromGroupChat(chatId, adminName, userName);

        assertTrue(result);
    }

    @Test
    void removeParticipantFromGroupChat_ChatIsNotGroup_ReturnsFalse() {

        String chatId = "123";
        String adminName = "Carlos";
        String userName = "Raul";

        Chat chatExpected = new IndividualChat(Arrays.asList(this.user1, this.user2), new Date());
        chatExpected.setId(chatId);

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(chatExpected));

        boolean result = this.chatService.removeParticipantFromGroupChat(chatId, adminName, userName);

        assertFalse(result);
    }

    @Test
    void removeParticipantFromGroupChat_AdminDoesNotExist_ReturnsFalse() {

        String chatId = "123";
        String adminName = "Carlos";
        String userName = "Raul";
        List<User> participants = Collections.singletonList(this.user2);
        List<User> groupAdmin = Collections.singletonList(this.user2);
        String groupName = "Test Group";
        String description = "This is a test group";

        GroupChat groupChatExpected = new GroupChat(groupName, description, participants, groupAdmin, new Date());
        groupChatExpected.setId(chatId);

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(groupChatExpected));

        boolean result = this.chatService.removeParticipantFromGroupChat(chatId, adminName, userName);

        assertFalse(result);
    }

    @Test
    void removeParticipantFromGroupChat_UserDoesNotExist_ReturnsFalse() {

        String chatId = "123";
        String adminName = "Carlos";
        String userName = "Raul";
        List<User> participants = Collections.singletonList(this.user1);
        List<User> groupAdmin = Collections.singletonList(this.user1);
        String groupName = "Test Group";
        String description = "This is a test group";

        GroupChat groupChatExpected = new GroupChat(groupName, description, participants, groupAdmin, new Date());
        groupChatExpected.setId(chatId);

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(groupChatExpected));

        boolean result = this.chatService.removeParticipantFromGroupChat(chatId, adminName, userName);

        assertFalse(result);
    }

    @Test
    void removeParticipantFromGroupChat_ChatDoesNotExist_ReturnsFalse() {

        String chatId = "123";
        String adminName = "Carlos";
        String userName = "Raul";

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.empty());

        boolean result = this.chatService.removeParticipantFromGroupChat(chatId, adminName, userName);

        assertFalse(result);
    }

    @Test
    void makeParticipantAdmin_ChatIsGroup_AdminAndUserExist_ReturnsTrue() {

        String chatId = "123";
        String adminName = "Carlos";
        String userName = "Raul";
        List<User> participants = Arrays.asList(this.user1, this.user2);
        List<User> groupAdmin = Collections.singletonList(this.user1);
        String groupName = "Test Group";
        String description = "This is a test group";

        GroupChat groupChatExpected = new GroupChat(groupName, description, participants, groupAdmin, new Date());
        groupChatExpected.setId(chatId);

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(groupChatExpected));
        when(this.chatRepositoryMock.update(any(Chat.class))).thenReturn(true);

        boolean result = this.chatService.makeParticipantAdmin(chatId, adminName, userName);

        assertTrue(result);
    }

    @Test
    void makeParticipantAdmin_ChatIsNotGroup_ReturnsFalse() {

        String chatId = "123";
        String adminName = "Carlos";
        String userName = "Raul";

        Chat chatExpected = new IndividualChat(Arrays.asList(this.user1, this.user2), new Date());
        chatExpected.setId(chatId);

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(chatExpected));

        boolean result = this.chatService.makeParticipantAdmin(chatId, adminName, userName);

        assertFalse(result);
    }

    @Test
    void makeParticipantAdmin_AdminDoesNotExist_ReturnsFalse() {

        String chatId = "123";
        String adminName = "Carlos";
        String userName = "Raul";
        List<User> participants = Collections.singletonList(this.user2);
        List<User> groupAdmin = Collections.singletonList(this.user2);
        String groupName = "Test Group";
        String description = "This is a test group";

        GroupChat groupChatExpected = new GroupChat(groupName, description, participants, groupAdmin, new Date());
        groupChatExpected.setId(chatId);

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(groupChatExpected));

        boolean result = this.chatService.makeParticipantAdmin(chatId, adminName, userName);

        assertFalse(result);
    }

    @Test
    void makeParticipantAdmin_UserDoesNotExist_ReturnsFalse() {

        String chatId = "123";
        String adminName = "Carlos";
        String userName = "Raul";
        List<User> participants = Collections.singletonList(this.user1);
        List<User> groupAdmin = Collections.singletonList(this.user1);
        String groupName = "Test Group";
        String description = "This is a test group";

        GroupChat groupChatExpected = new GroupChat(groupName, description, participants, groupAdmin, new Date());
        groupChatExpected.setId(chatId);

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(groupChatExpected));

        boolean result = this.chatService.makeParticipantAdmin(chatId, adminName, userName);

        assertFalse(result);
    }

    @Test
    void makeParticipantAdmin_ChatDoesNotExist_ReturnsFalse() {

        String chatId = "123";
        String adminName = "Carlos";
        String userName = "Raul";

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.empty());

        boolean result = this.chatService.makeParticipantAdmin(chatId, adminName, userName);

        assertFalse(result);
    }


    @Test
    void removeParticipantFromAdmins_ChatIsGroup_AdminAndUserExist_ReturnsTrue() {

        String chatId = "123";
        String adminName = "Carlos";
        String userName = "Raul";
        List<User> participants = Arrays.asList(this.user1, this.user2);
        List<User> groupAdmin = Arrays.asList(this.user1, this.user2);
        String groupName = "Test Group";
        String description = "This is a test group";

        GroupChat groupChatExpected = new GroupChat(groupName, description, participants, groupAdmin, new Date());
        groupChatExpected.setId(chatId);

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(groupChatExpected));
        when(this.chatRepositoryMock.update(any(Chat.class))).thenReturn(true);

        boolean result = this.chatService.removeParticipantFromAdmins(chatId, adminName, userName);

        assertTrue(result);
    }

    @Test
    void removeParticipantFromAdmins_ChatIsNotGroup_ReturnsFalse() {

        String chatId = "123";
        String adminName = "Carlos";
        String userName = "Raul";

        Chat chatExpected = new IndividualChat(Arrays.asList(this.user1, this.user2), new Date());
        chatExpected.setId(chatId);

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(chatExpected));

        boolean result = this.chatService.removeParticipantFromAdmins(chatId, adminName, userName);

        assertFalse(result);
    }

    @Test
    void removeParticipantFromAdmins_AdminDoesNotExist_ReturnsFalse() {

        String chatId = "123";
        String adminName = "Carlos";
        String userName = "Raul";
        List<User> participants = Collections.singletonList(this.user2);
        List<User> groupAdmin = Collections.singletonList(this.user2);
        String groupName = "Test Group";
        String description = "This is a test group";

        GroupChat groupChatExpected = new GroupChat(groupName, description, participants, groupAdmin, new Date());
        groupChatExpected.setId(chatId);

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(groupChatExpected));

        boolean result = this.chatService.removeParticipantFromAdmins(chatId, adminName, userName);

        assertFalse(result);
    }

    @Test
    void removeParticipantFromAdmins_UserDoesNotExist_ReturnsFalse() {

        String chatId = "123";
        String adminName = "Carlos";
        String userName = "Raul";
        List<User> participants = Collections.singletonList(this.user1);
        List<User> groupAdmin = Collections.singletonList(this.user1);
        String groupName = "Test Group";
        String description = "This is a test group";

        GroupChat groupChatExpected = new GroupChat(groupName, description, participants, groupAdmin, new Date());
        groupChatExpected.setId(chatId);

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(groupChatExpected));

        boolean result = this.chatService.removeParticipantFromAdmins(chatId, adminName, userName);

        assertFalse(result);
    }

    @Test
    void removeParticipantFromAdmins_ChatDoesNotExist_ReturnsFalse() {

        String chatId = "123";
        String adminName = "Carlos";
        String userName = "Raul";

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.empty());

        boolean result = this.chatService.removeParticipantFromAdmins(chatId, adminName, userName);

        assertFalse(result);
    }

    @Test
    void sendMessage_ChatExists_ReturnsChatWithMessage() {

        User sender = new BasicUser("Carlos", "user1@email.com");
        User receiver = new BasicUser("Raul", "user2@email.com");
        Message message = new TextMessage(sender, "Hello Raul", new Date());
        Chat chatExpected = new IndividualChat(Arrays.asList(sender, receiver), new Date());

        when(chatRepositoryMock.save(any(Chat.class))).thenReturn(chatExpected);

        Chat result = chatService.sendMessage(sender.getUserName(), message, receiver.getUserName());

        assertEquals(chatExpected, result, "Should return the chat created");
        assertTrue(result.getMessages().contains(message), "Should contain the message sent");
    }

    @Test
    void sendMessage_ChatDoesNotExist_ReturnsNewChatWithMessage() {

        User sender = new BasicUser("Carlos", "user1@email.com");
        User receiver = new BasicUser("Raul", "user2@email.com");
        Message message = new TextMessage(sender, "Hello Raul", new Date());
        Chat chatExpected = new IndividualChat(Arrays.asList(sender, receiver), new Date());

        when(chatRepositoryMock.save(any(Chat.class))).thenReturn(chatExpected);

        Chat result = chatService.sendMessage(sender.getUserName(), message, receiver.getUserName());

        assertEquals(chatExpected, result, "Should return a new chat created");
        assertTrue(result.getMessages().contains(message), "Should contain the message sent");
    }

    @Test
    void updateMessageInChat_ChatExists_MessageExists_UpdatesMessage() {

        String chatId = "123";
        String messageId = "0";
        User sender = new BasicUser("Carlos", "user1@email.com");
        Message message = new TextMessage(sender, "Hello Raul", new Date());
        message.setId(messageId);
        Chat chatExpected = new IndividualChat(Arrays.asList(sender, this.user2), new Date());
        chatExpected.setId(chatId);
        chatExpected.addMessage(message);

        Message messageUpdated = new TextMessage(sender, "Hello Raul Updated", new Date());
        messageUpdated.setId(messageId);

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(chatExpected));

        this.chatService.updateMessageInChat(chatId, messageUpdated);

        Message messageInChat = chatExpected.getMessages().stream()
                .filter(m -> m.getId().equals(messageId))
                .findFirst()
                .orElse(null);
        assertNotNull(messageInChat, "The message should be found in the chat");
        assertEquals(messageUpdated.getContent(), messageInChat.getContent(), "The message content should be updated");
        assertTrue(messageInChat.getUpdated(), "The message should be updated");
    }


    @Test
    void updateMessageInChat_ChatExists_MessageDoesNotExist_DoesNotUpdateMessage() {

        String chatId = "123";
        String messageId = "0";
        User sender = new BasicUser("Carlos", "user1@email.com");
        Message message = new TextMessage(sender, "Hello Raul", new Date());
        message.setId(messageId);
        Chat chatExpected = new IndividualChat(Arrays.asList(sender, this.user2), new Date());
        chatExpected.setId(chatId);
        chatExpected.addMessage(message);

        Message messageUpdated = new TextMessage(sender, "Hello Raul Updated", new Date());
        messageUpdated.setId("789");


        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(chatExpected));


        this.chatService.updateMessageInChat(chatId, messageUpdated);


        Message messageInChat = chatExpected.getMessages().stream()
                .filter(m -> m.getId().equals(messageId))
                .findFirst()
                .orElse(null);
        assertNotNull(messageInChat, "The message should be found in the chat");
        assertNotEquals(messageUpdated.getContent(), messageInChat.getContent(), "The message content should not be updated");
        assertFalse(messageInChat.getUpdated(), "The message should not be updated");
    }


    @Test
    void updateMessageInChat_ChatDoesNotExist_DoesNotUpdateMessage() {

        String chatId = "123";
        String messageId = "456";
        User sender = new BasicUser("Carlos", "user1@email.com");
        Message messageUpdated = new TextMessage(sender, "Hello Raul Updated", new Date());
        messageUpdated.setId(messageId);

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.empty());

        this.chatService.updateMessageInChat(chatId, messageUpdated);

        verify(this.chatRepositoryMock, never()).update(any(Chat.class));
    }


    @Test
    void sendMessage_ChatExists_ReturnsTrue() {

        String chatId = "123";
        Message message = new TextMessage(this.user1, "Hello Raul", new Date());
        Chat chatExpected = new IndividualChat(Arrays.asList(this.user1, this.user2), new Date());
        chatExpected.setId(chatId);

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(chatExpected));
        when(this.chatRepositoryMock.save(any(Chat.class))).thenReturn(chatExpected);

        boolean result = this.chatService.sendMessage(chatId, message);

        assertTrue(result);
    }

    @Test
    void sendMessage_ChatDoesNotExist_ReturnsFalse() {

        String chatId = "123";
        Message message = new TextMessage(this.user1, "Hello Raul", new Date());

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.empty());

        boolean result = this.chatService.sendMessage(chatId, message);

        assertFalse(result);
    }



    @Test
    void findChatById() {

        String chatId = "123";
        Chat chatExpected = new IndividualChat(Arrays.asList(this.user1, this.user2), new Date());
        chatExpected.setId(chatId);

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(chatExpected));

        Optional<Chat> result = this.chatService.findChatById(chatId);

        assertTrue(result.isPresent(), "Should return a chat");
        assertEquals(chatExpected, result.get(), "Should return the chat created");
    }

    @Test
    void addParticipantToGroupChat_ChatDoesNotExist_ReturnsFalse() {
        String chatId = "123";
        String adminName = "Carlos";
        String userName = "Raul";

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.empty());

        boolean result = this.chatService.addParticipantToGroupChat(chatId, adminName, userName);

        assertFalse(result);
    }

    @Test
    void addParticipantToGroupChat_ChatIsNotGroup_ReturnsFalse() {
        String chatId = "123";
        String adminName = "Carlos";
        String userName = "Raul";

        Chat chat = new IndividualChat(Arrays.asList(this.user1, this.user2), new Date());
        chat.setId(chatId);

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(chat));

        boolean result = this.chatService.addParticipantToGroupChat(chatId, adminName, userName);

        assertFalse(result);
    }

    @Test
    void addParticipantToGroupChat_AdminDoesNotExist_ReturnsFalse() {
        String chatId = "123";
        String adminName = "Carlos";
        String userName = "Raul";

        GroupChat groupChat = new GroupChat("Test Group", "This is a test group", Collections.singletonList(this.user2), Collections.singletonList(this.user2), new Date());
        groupChat.setId(chatId);

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(groupChat));

        boolean result = this.chatService.addParticipantToGroupChat(chatId, adminName, userName);

        assertFalse(result);
    }

    @Test
    void addParticipantToGroupChat_UserDoesNotExist_ReturnsFalse() {
        String chatId = "123";
        String adminName = "Carlos";
        String userName = "Raul";

        GroupChat groupChat = new GroupChat("Test Group", "This is a test group", Collections.singletonList(this.user1), Collections.singletonList(this.user1), new Date());
        groupChat.setId(chatId);

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(groupChat));
        when(this.userServiceMock.getUserByUserName(userName)).thenReturn(Optional.empty());

        boolean result = this.chatService.addParticipantToGroupChat(chatId, adminName, userName);

        assertFalse(result);
    }

    @Test
    void addParticipantToGroupChat_ValidInputs_ReturnsTrue() {
        String chatId = "123";
        String adminName = "Carlos";
        String userName = "Raul";

        GroupChat groupChat = new GroupChat("Test Group", "This is a test group", Collections.singletonList(this.user1), Collections.singletonList(this.user1), new Date());
        groupChat.setId(chatId);

        when(this.chatRepositoryMock.findById(chatId)).thenReturn(Optional.of(groupChat));
        when(this.userServiceMock.getUserByUserName(userName)).thenReturn(Optional.of(this.user2));
        when(this.chatRepositoryMock.update(any(Chat.class))).thenReturn(true);

        boolean result = this.chatService.addParticipantToGroupChat(chatId, adminName, userName);

        assertTrue(result);
    }

}