package model

import com.example.videogamelibrary.R
import kotlin.random.Random

class GameRepository {

    /* Se crea lista privada para los juegos */
    private val games: List<VideoGame> = listOf(
        VideoGame("Zelda: Breath of the Wild", "Nintendo Switch", R.drawable.zelda_botw, 2017),
        VideoGame("God of War", "PlayStation 4", R.drawable.god_of_war, 2018),
        VideoGame("Hollow Knight", "PC / Switch", R.drawable.hollow_knight, 2017),
        VideoGame("Red Dead Redemption 2", "PS4 / Xbox One", R.drawable.red_dead_2, 2018),
        VideoGame("Minecraft", "Multiplataforma", R.drawable.minecraft, 2011)
    )

    fun getRandomGame(): VideoGame {
        return games[Random.nextInt(games.size)]
    }
}