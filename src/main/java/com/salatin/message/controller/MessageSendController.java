package com.salatin.message.controller;

import com.salatin.message.model.dto.LogMessage;
import com.salatin.message.service.OrderLogMessageProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/sender")
@RequiredArgsConstructor
public class MessageSendController {
    private final OrderLogMessageProducer messageProducer;

    @Operation(
            summary = "Send log message",
            description = "Allows to customer, manager, or mechanic "
                    + "to send log messages to the order by id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sent successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Can't find an order with id")
    })
    @PatchMapping("/order/{orderId}/send-message")
    @PreAuthorize(value = "hasAnyRole('mechanic', 'manager', 'customer')")
    public Mono<Void> sendMessage(@PathVariable String orderId,
                                  @RequestBody LogMessage request,
                                  @AuthenticationPrincipal JwtAuthenticationToken token) {
        setLogMessageRole(token, request);

        messageProducer.send(orderId, request);
        return Mono.empty();
    }

    private void setLogMessageRole(JwtAuthenticationToken token, LogMessage logMessage) {
        logMessage.setFrom(getRole(token));
    }

    private String getRole(JwtAuthenticationToken token) {
        return token.getAuthorities().stream()
                .findFirst()
                .orElse(new SimpleGrantedAuthority("no role"))
                .getAuthority().substring(5);
    }
}
