package ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import model.GameRepository
import com.example.videogamelibrary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val repository = GameRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mostrarJuegoAleatorio()

        binding.btnNext.setOnClickListener {
            mostrarJuegoAleatorio()
        }
    }
    private fun mostrarJuegoAleatorio() {
        val game = repository.getRandomGame()
        binding.ivCover.setImageResource(game.imagenResId)
        binding.tvTitle.text = game.titulo
        binding.tvPlatform.text = game.plataforma
        binding.tvYear.text = "Año: ${game.anioLanzamiento}"
    }
}