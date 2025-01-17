package com.gamerfinder.gamerfinder.domain

import com.gamerfinder.gamerfinder.dtos.output.RoomOutput
import java.time.LocalDateTime

data class Room(
    val id: String,
    val gameId: Int,
    val playerHost: Player,
    val playersIdJoined: List<Int> = emptyList(),
    val description: String,
    val spots: Int,
    val mode: String,
    val ranks: List<String>,
    val createdAt: LocalDateTime,
)

fun Room.toOutput() = RoomOutput(
    id = id,
    playerHostName = playerHost.name,
    description = description,
    spots = spots,
    mode = mode,
    ranks = ranks,
)