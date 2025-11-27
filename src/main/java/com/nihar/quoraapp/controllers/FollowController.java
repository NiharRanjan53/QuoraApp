package com.nihar.quoraapp.controllers;

import com.nihar.quoraapp.dto.FollowRequestDTO;
import com.nihar.quoraapp.dto.FollowResponseDTO;
import com.nihar.quoraapp.services.IFollowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FollowController {

    private final IFollowService followService;

    @PostMapping("/follow")
    public Mono<ResponseEntity<FollowResponseDTO>> follow(@Valid @RequestBody FollowRequestDTO requestDTO) {
        return followService.follow(requestDTO)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED).body(response));
    }

    @PostMapping("/unfollow")
    public Mono<ResponseEntity<Void>> unfollow(@Valid @RequestBody FollowRequestDTO requestDTO) {
        return followService.unfollow(requestDTO)
                .thenReturn(ResponseEntity.noContent().<Void>build());
    }
}


