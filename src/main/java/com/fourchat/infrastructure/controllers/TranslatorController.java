package com.fourchat.infrastructure.controllers;

import com.fourchat.application.TranslatorService;
import com.fourchat.infrastructure.controllers.dtos.TranslateDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TranslatorController {

    TranslatorService translatorService;

    @PostMapping("/translate")
    public String translate(@RequestBody TranslateDto translateDto) {
        try {
            return translatorService.translate(translateDto.text(), translateDto.sourceLanguage(), translateDto.targetLanguage());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
