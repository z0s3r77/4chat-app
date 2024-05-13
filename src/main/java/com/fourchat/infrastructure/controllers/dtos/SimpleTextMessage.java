package com.fourchat.infrastructure.controllers.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
public class SimpleTextMessage {

    private String chatId;
    private String messageId;
    private String content;
    private Date date;

}
