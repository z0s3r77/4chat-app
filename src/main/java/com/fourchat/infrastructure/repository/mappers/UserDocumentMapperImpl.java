package com.fourchat.infrastructure.repository.mappers;

import com.fourchat.domain.models.BasicUser;
import com.fourchat.domain.models.User;
import com.fourchat.infrastructure.repository.documents.UserDocument;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class UserDocumentMapperImpl implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public UserDocument toUserDocument(User user) {

        if (user instanceof BasicUser) {

            UserDocument userDocument = new UserDocument();
            userDocument.setId(user.getId());
            userDocument.setFirstName(((BasicUser) user).getFirstName());
            userDocument.setLastName(((BasicUser) user).getLastName());
            userDocument.setUserName(user.getUserName());
            userDocument.setEmail(user.getEmail());
            userDocument.setType("BasicUser");
            userDocument.setDescription(user.getDescription());
            userDocument.setContacts(user.getContacts());
            userDocument.setLinkedIn(((BasicUser) user).getLinkedIn());
            userDocument.setTwitter(((BasicUser) user).getTwitter());
            userDocument.setFacebook(((BasicUser) user).getFacebook());
            userDocument.setPhotoUrl(((BasicUser) user).getPhotoUrl());
            return userDocument;

        } else {
            return null;
        }
    }

    public User toUser(UserDocument userDocument) {

        if (userDocument.getType().equals("BasicUser")) {

            if (userDocument.getContacts() == null) {
                return new BasicUser(userDocument.getId(), userDocument.getUserName(), userDocument.getFirstName(), userDocument.getLastName(), userDocument.getEmail(), userDocument.getDescription(), new ArrayList<>(), userDocument.getLinkedIn(), userDocument.getTwitter(), userDocument.getFacebook(), userDocument.getPhotoUrl(), null);
            }

            return new BasicUser(userDocument.getId(), userDocument.getUserName(), userDocument.getFirstName(), userDocument.getLastName(), userDocument.getEmail(), userDocument.getDescription(), userDocument.getContacts(), userDocument.getLinkedIn(), userDocument.getTwitter(), userDocument.getFacebook(), userDocument.getPhotoUrl(), null);

        } else {
            return null;
        }
    }


}
