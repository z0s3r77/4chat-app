package com.fourchat.infrastructure.controllers.dtos;


import lombok.Builder;

@Builder
public record TranslateDto(String text, String sourceLanguage, String targetLanguage) {
}
