package com.quickbite.fooddelivery.restaraunt.controller;

import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.quickbite.fooddelivery.restaraunt.dto.RestarauntDto;
import com.quickbite.fooddelivery.restaraunt.service.RestarauntService;

@RestController
@RequestMapping("/api/v1/restaraunts")
public class RestarauntController {

    private final RestarauntService restarauntService;

    RestarauntController(RestarauntService restarauntService) {
        this.restarauntService = restarauntService;
    }

    @PostMapping
    public ResponseEntity<String> createRestaraunt(@RequestBody RestarauntDto restarauntDto) {
        restarauntService.createRestaraunt(restarauntDto);
        return ResponseEntity.ok("Restaraunt added waiting for approval");
    }

    @GetMapping("{id}")
    public ResponseEntity<RestarauntDto> getRestaurant(@PathVariable UUID id) {
        return ResponseEntity.ok(restarauntService.getRestarauntById(id));
    }
}
