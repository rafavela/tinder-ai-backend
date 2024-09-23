package io.javabrains.tinder_ai_backend.conversations;

import io.javabrains.tinder_ai_backend.profiles.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        Conversation conversation = new Conversation(
                UUID.randomUUID().toString(),
                request.profileId,
                List.of(
                        new ChatMessage("hello", request.profileId, LocalDateTime.now())
                )
        );

        conversationRepository.save(conversation);
        return conversation;
    }

    public record CreateConversationRequest(String  profileId){}
}
