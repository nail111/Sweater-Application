package com.sweater.controller;

import com.sweater.model.Message;
import com.sweater.model.User;
import com.sweater.repo.MessageRepo;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MessageRepo messageRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(Model model) {
        model.addAttribute("messages", messageRepo.findAll());
        return "main";
    }

    @SneakyThrows
    @PostMapping("/main")
    public String addMessage(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) String tag,
            @RequestParam(name = "filter", required = false, defaultValue = "") String filter,
            Model model,
            @RequestParam(name = "file", required = false) MultipartFile file
    ) {

        if ((text != null && !text.isBlank() && !text.isEmpty()) || (tag != null && !tag.isEmpty() && !tag.isBlank())) {
            Message message = new Message(text, tag, user);
            if (file != null && !file.getOriginalFilename().isEmpty()) {
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + file.getOriginalFilename();
                file.transferTo(new File(uploadPath + "/" + resultFilename));
                message.setFilename(resultFilename);
            }
            messageRepo.save(message);
            model.addAttribute("messages", messageRepo.findAll());
        }

        if (filter != null && !filter.isEmpty() && !filter.isBlank()) {
            model.addAttribute("messages", messageRepo.findByTag(filter));
        } else {
            model.addAttribute("messages", messageRepo.findAll());
        }

        return "main";
    }
}
