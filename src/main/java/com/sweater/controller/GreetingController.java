package com.sweater.controller;

import com.sweater.model.Message;
import com.sweater.repo.MessageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class GreetingController {

    private final MessageRepo messageRepo;

    @GetMapping("/greeting")
    public String greeting(
            @RequestParam(name = "name", required = false, defaultValue = "World") String name,
            Map<String, Object> model
    ) {
        model.put("name", name);
        return "greeting";
    }

    @GetMapping
    public String main(Map<String, Object> model) {
        model.put("messages", messageRepo.findAll());
        return "main";
    }

    @PostMapping
    public String addMessage(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) String tag,
            @RequestParam(name = "filter", required = false, defaultValue = "") String filter,
            Map<String, Object> model
    ) {

        if ((text != null && !text.isBlank() && !text.isEmpty()) || (tag != null && !tag.isEmpty() && !tag.isBlank())) {
            Message message = new Message(text, tag);
            messageRepo.save(message);
            model.put("messages", messageRepo.findAll());
        }

        if (filter != null && !filter.isEmpty() && !filter.isBlank()) {
            model.put("messages", messageRepo.findByTag(filter));
        } else {
            model.put("messages", messageRepo.findAll());
        }

        return "main";
    }
}
