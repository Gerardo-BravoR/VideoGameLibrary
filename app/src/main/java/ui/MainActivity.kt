package ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.videogamelibrary.databinding.ActivityMainBinding
import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: GameViewModel by viewModels()
    private val adapter = GameAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvGames.layoutManager = LinearLayoutManager(this)
        binding.rvGames.adapter = adapter

        binding.btnFetch.setOnClickListener {
            viewModel.onMainButtonClick()
        }

        viewModel.isLoading.observe(this) { loading ->
            binding.pbLoading.visibility = if (loading) View.VISIBLE else View.GONE
            binding.btnFetch.isEnabled = !loading
            binding.tvStatus.text = if (loading) "Descargando..." else "Listo"
            binding.tvStatus.setTextColor(Color.DKGRAY)
        }

        viewModel.gamesForList.observe(this) { list ->
            adapter.submitList(list)
        }

        viewModel.errorMessage.observe(this) { msg ->
            if (!msg.isNullOrBlank()) {
                binding.tvStatus.text = "Error de red..."
                binding.tvStatus.setTextColor(Color.RED)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                viewModel.clearError() // evita repetir toast al rotar
            }
        }

        viewModel.currentGame.observe(this) { game ->
            if (game != null) {
                binding.ivCover.setImageResource(game.imagenResId)
                binding.tvTitle.text = game.titulo
                binding.tvPlatform.text = game.plataforma
                binding.tvYear.text = "Año: ${game.anioLanzamiento}"
            }
        }
    }
}