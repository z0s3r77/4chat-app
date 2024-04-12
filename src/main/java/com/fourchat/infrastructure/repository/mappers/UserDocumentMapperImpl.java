package com.fourchat.infrastructure.repository.mappers;

import com.fourchat.domain.models.BasicUser;
import com.fourchat.domain.models.User;
import com.fourchat.infrastructure.repository.documents.UserDocument;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserDocumentMapperImpl {


    public UserDocument toUserDocument(User user) {

        if (user instanceof BasicUser){

            UserDocument userDocument = new UserDocument();
            userDocument.setId(user.getId());
            userDocument.setUserName(user.getUserName());
            userDocument.setEmail(user.getEmail());
            userDocument.setType("BasicUser");
            return userDocument;

        } else {
            return null;
        }
    }

    public User toUser(UserDocument userDocument) {

        if (userDocument.getType().equals("BasicUser")){

            BasicUser basicUser = new BasicUser(userDocument.getId(), userDocument.getUserName(), userDocument.getEmail());

            return basicUser;

        } else {
            return null;
        }
    }


}
