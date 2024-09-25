package io.javabrains.tinder_ai_backend.conversations;

import io.javabrains.tinder_ai_backend.profiles.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
public class ConversationController {

    private final ConversationRepository conversationRepository;
    
    private final ProfileRepository profileRepository;

    public ConversationController(ConversationRepository conversationRepository, ProfileRepository profileRepository){
        this.conversationRepository = conversationRepository;
        this.profileRepository=profileRepository;
    }

    @PostMapping("/conversations")
    public Conversation createNewConversation(@RequestBody CreateConversationRequest request){
        System.out.println("hello");
        profileRepository.findById(request.profileId)
                .orElseThrow(()->new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unable to find profile with Id "+request.profileId
                ));
        Conversation conversation = new Conversation(
                UUID.randomUUID().toString(),
                request.profileId,
                List.of(
//                        new ChatMessage("hello", request.profileId, LocalDateTime.now())
                )
        );

        conversationRepository.save(conversation);
        return conversation;
    }

    @GetMapping("conversations/{conversationId}")
    public Conversation getConversation(@PathVariable String conversationId){
        return conversationRepository.findById(conversationId)
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unable to find conversation with the ID "+ conversationId
                ));
    }


    @PostMapping("conversations/{conversationId}")
    public Conversation addMessageToConversation(
            @PathVariable String conversationId,
            @RequestBody ChatMessage chatMessage
    ){
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unable to find conversation with the ID "+ conversationId
                ));
        profileRepository.findById(chatMessage.authorId())
                .orElseThrow(()-> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unable to find a profile with ID "+chatMessage.authorId()
                ));
        ChatMessage chatMessageWithTime = new ChatMessage(
            chatMessage.messageText(),
            chatMessage.authorId(),
            LocalDateTime.now()
        );

        conversation.message().add(chatMessageWithTime);
        conversationRepository.save(conversation);
        return conversation;
    }

    public record CreateConversationRequest(String  profileId){}
}
