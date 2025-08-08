package com.flowintent.workspace.data.local

import com.flowintent.workspace.R
import com.flowintent.workspace.data.Task
import com.flowintent.workspace.data.SongRes

object LocalTaskDataProvider {

    val allSongs = listOf(
        Task(
            serialNo = 111111111111111111,
            name = "Ever Now",
            creator = "Gesafelstein",
            content = SongRes.SongContentRes(R.string.default_song_content)
        ),
    )

    fun get(id: Long): Task? {
        return allSongs.firstOrNull { it.serialNo == id }
    }

    val defaultSong = Task(
        serialNo = -1,
        name = "Unknown",
        content = SongRes.SongContentRes(R.string.default_song_content),
    )
}
