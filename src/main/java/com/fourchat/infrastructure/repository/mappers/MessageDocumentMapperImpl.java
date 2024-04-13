package com.fourchat.infrastructure.repository.mappers;


import com.fourchat.domain.models.Message;
import com.fourchat.domain.models.SystemTextMessage;
import com.fourchat.domain.models.TextMessage;
import com.fourchat.domain.models.User;
import com.fourchat.domain.ports.UserService;
import com.fourchat.infrastructure.repository.documents.MessageDocument;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class MessageDocumentMapperImpl {

	private final UserService userService;


	public MessageDocument toMessageDocument(Message message) {


		if (message instanceof TextMessage){

			MessageDocument messageDocument = new MessageDocument();
			messageDocument.setId(message.getId());
			messageDocument.setSenderId(message.getSender().getId());
			messageDocument.setContent(message.getContent());
			messageDocument.setCreationDate(message.getCreationDate());
			messageDocument.setType("textMessage");
			messageDocument.setUpdated(message.updated());

			return messageDocument;

		} else if (message instanceof SystemTextMessage){

			MessageDocument messageDocument = new MessageDocument();
			messageDocument.setId(message.getId());
			messageDocument.setSenderId(message.getSender().getId());
			messageDocument.setContent(message.getContent());
			messageDocument.setCreationDate(message.getCreationDate());
			messageDocument.setType("SystemTextMessage");
			messageDocument.setUpdated(message.updated());

			return messageDocument;

		} else {
			return null;
		}

	}

	public Message toMessage(MessageDocument messageDocument) {

		if (messageDocument.getType().equals("textMessage")){

			User sender = userService.getUserById(messageDocument.getSenderId());

			TextMessage textMessage = new TextMessage();
			textMessage.setId(messageDocument.getId());
			textMessage.setSender(sender);
			textMessage.setContent(messageDocument.getContent());
			textMessage.setCreationDate(messageDocument.getCreationDate());
			textMessage.setUpdated(messageDocument.isUpdated());

			return textMessage;

		}  else if (messageDocument.getType().equals("SystemTextMessage")){

			SystemTextMessage systemTextMessage = new SystemTextMessage(messageDocument.getContent(), messageDocument.getCreationDate());
			systemTextMessage.setId(messageDocument.getId());

			return systemTextMessage;
		} else {
			return null;
		}
	}



}
