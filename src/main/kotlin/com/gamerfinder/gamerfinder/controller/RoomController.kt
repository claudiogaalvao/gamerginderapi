package com.gamerfinder.gamerfinder.controller

import com.gamerfinder.gamerfinder.dtos.input.CreateJoinRequestInput
import com.gamerfinder.gamerfinder.dtos.input.CreateRoomInput
import com.gamerfinder.gamerfinder.dtos.input.UpdateRoomInput
import com.gamerfinder.gamerfinder.dtos.output.CreateRoomOutput
import com.gamerfinder.gamerfinder.dtos.output.PendingJoinRequestOutput
import com.gamerfinder.gamerfinder.dtos.output.RoomOutput
import com.gamerfinder.gamerfinder.dtos.output.UpdateRoomOutput
import com.gamerfinder.gamerfinder.service.RoomService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder

@RestController
@RequestMapping("/rooms")
class RoomController(
    private val service: RoomService
) {

    @GetMapping("/{gameId}")
    fun getRooms(@PathVariable gameId: Int): List<RoomOutput> {
        return service.getRooms(gameId)
    }

    @PostMapping  // TODO requires authentication
    fun createRoom(
        @RequestParam gameId: Int,
        @RequestParam playerId: Int,
        @RequestBody @Valid input: CreateRoomInput,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<CreateRoomOutput> {
        // check if receiving gameId and playerId
        // check if receiving room with all fields
        val roomCreatedResponse = service.createRoom(
            gameId = gameId,
            playerId = playerId,
            input = input
        )
        val uri = uriBuilder
            .path("/rooms/${roomCreatedResponse.id}")
            .build()
            .toUri()
        return ResponseEntity.created(uri).body(roomCreatedResponse)
    }

    @PutMapping("/{roomId}")  // TODO requires authentication
    fun update(
        @PathVariable roomId: String,
        @RequestBody @Valid input: UpdateRoomInput
    ): ResponseEntity<UpdateRoomOutput> {
        val room = service.update(roomId, input)
        return ResponseEntity.ok(room)
    }

    @DeleteMapping("/{roomId}")  // TODO requires authentication
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable roomId: String
    ) {
        service.delete(roomId)
    }

    @PostMapping("/{roomId}/join") // TODO requires authentication
    fun requestToJoinRoom(
        @PathVariable roomId: String,
        @RequestBody input: CreateJoinRequestInput,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<Any> {
        val joinRequest = service.requestToJoinRoom(roomId, input)
        val uri = uriBuilder
            .path("/$roomId/requests/${joinRequest.id}")
            .build()
            .toUri()
        return ResponseEntity.created(uri).body(joinRequest)
    }

    @GetMapping("/{roomId}/pending-requests") // TODO requires authentication
    fun getJoinRequests(roomId: String): List<PendingJoinRequestOutput> {
        return service.getPendingJoinRequests(roomId)
    }

    @PutMapping("/{roomId}/requests/{requestId}/accept") // TODO requires authentication
    fun acceptJoinRequest(
        @PathVariable roomId: String,
        @PathVariable requestId: String
    ): ResponseEntity<Any> {
        service.acceptJoinRequest(roomId, requestId)
        return ResponseEntity.noContent().build()
    }

//    fun rejectJoinRequest(roomId: String, requestId: String): ResponseEntity<Any> {
//        service.rejectJoinRequest(roomId, requestId)
//        return ResponseEntity.noContent().build()
//    }

}