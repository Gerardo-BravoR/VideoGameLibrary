package model

import com.example.videogamelibrary.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
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

    suspend fun getRandomGame(): Result<VideoGame> = withContext(Dispatchers.IO)
    {
        //simula latencia: 2000 a 5000ms
        val ms = Random.nextLong(2000L, 5001L)
        delay(ms)

        //20% de probabilidad de fallar
        val falla = Random.nextInt(100) < 20
        if(falla)
        {
            Result.failure(Exception("Error de conexión: servidor saturado"))
        }
        else
        {
            val game = games[Random.nextInt(games.size)]
            Result.success(game)
        }
    }
}