package io.javabrains.tinder_ai_backend.matches;

import io.javabrains.tinder_ai_backend.conversations.Conversation;
import io.javabrains.tinder_ai_backend.conversations.ConversationRepository;
import io.javabrains.tinder_ai_backend.profiles.Profile;
import io.javabrains.tinder_ai_backend.profiles.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class MatchController {

    private final  ProfileRepository profileRepository;
    private final  ConversationRepository conversationRepository;
    private final  MatchRepository matchRepository;

    public MatchController(
            ProfileRepository profileRepository,
            ConversationRepository conversationRepository,
            MatchRepository matchRepository
    ){
        this.profileRepository=profileRepository;
        this.conversationRepository=conversationRepository;
        this.matchRepository=matchRepository;
    }

    public record CreateMatchRequest(String profileId){}

    @PostMapping("/matches")
    public Match createNewMatch(@RequestBody CreateMatchRequest request){
        Profile profile =
                profileRepository
                        .findById(request.profileId)
                        .orElseThrow(
                            ()->new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Unable to find a profile with ID " + request.profileId()
                            )
                        );
        Conversation conversation = new Conversation(
                UUID.randomUUID().toString(),
                profile.id(),
                new ArrayList<>()
        );
        conversationRepository.save(conversation);
        Match match = new Match(UUID.randomUUID().toString(),profile,conversation.id());
        matchRepository.save(match);
        return match;
    }

    @GetMapping("/matches")
    public List<Match> getAllMatches(){
        return matchRepository.findAll();
    }

}
