package ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import model.GameRepository
import com.example.videogamelibrary.databinding.ActivityMainBinding
import android.graphics.Color
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val repository = GameRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Estado inicial
        setIdleState("Listo")

        binding.btnNext.setOnClickListener {
            lifecycleScope.launch {
                cargarSiguienteJuego()
            }
        }

        binding.btnBulk.setOnClickListener {
            lifecycleScope.launch {
                cargaMasiva3Juegos()
            }
        }
    }

    private suspend fun cargarSiguienteJuego() {
        setLoadingState("Conectando...")

        //barra de progreso simulada
        val progressJob = lifecycleScope.launch {
            binding.pbDownload.visibility = android.view.View.VISIBLE
            binding.pbDownload.progress = 0
            for (p in 0..100) {
                binding.pbDownload.progress = p
                delay(50)
            }
        }

        try {
            binding.tvStatus.text = "Descargando..."
            val result = repository.getRandomGame()

            // Cuando termina la descarga real, detenemos la barra simulada
            progressJob.cancel()
            binding.pbDownload.visibility = android.view.View.GONE

            result.onSuccess { game ->
                binding.ivCover.setImageResource(game.imagenResId)
                binding.tvTitle.text = game.titulo
                binding.tvPlatform.text = game.plataforma
                binding.tvYear.text = "Año: ${game.anioLanzamiento}"
                setIdleState("Listo ✅")
            }.onFailure { ex ->
                setErrorState("Error de red...")
                Toast.makeText(this, ex.message ?: "Error desconocido", Toast.LENGTH_SHORT).show()
            }
        } finally {
            binding.pbLoading.visibility = android.view.View.GONE
            binding.btnNext.isEnabled = true
            binding.btnBulk.isEnabled = true
        }
    }

    private suspend fun cargaMasiva3Juegos() {
        setLoadingState("Carga masiva: conectando...")

        //barra
        val progressJob = lifecycleScope.launch {
            binding.pbDownload.visibility = android.view.View.VISIBLE
            binding.pbDownload.progress = 0
            for (p in 0..100) {
                binding.pbDownload.progress = p
                delay(50)
            }
        }

        try {
            //si el servidor tarda más de 4s -> cancelar
            val results = withTimeout(4000L) {
                coroutineScope {
                    val jobs = List(3) {
                        async { repository.getRandomGame() }
                    }
                    jobs.awaitAll() // simultáneo
                }
            }

            progressJob.cancel()
            binding.pbDownload.visibility = android.view.View.GONE

            val ok = results.count { it.isSuccess }
            val fail = results.count { it.isFailure }

            // Muestra el primer éxito en pantalla
            results.firstOrNull { it.isSuccess }?.getOrNull()?.let { game ->
                binding.ivCover.setImageResource(game.imagenResId)
                binding.tvTitle.text = game.titulo
                binding.tvPlatform.text = game.plataforma
                binding.tvYear.text = "Año: ${game.anioLanzamiento}"
            }

            if (fail > 0) {
                setErrorState("Carga masiva: $ok OK / $fail fallos")
                Toast.makeText(this, "Algunos juegos fallaron por red", Toast.LENGTH_SHORT).show()
            } else {
                setIdleState("Carga masiva completa: $ok/3 ✅")
            }

        } catch (e: TimeoutCancellationException) {
            progressJob.cancel()
            binding.pbDownload.visibility = android.view.View.GONE

            setErrorState("Servidor fuera de servicio por lentitud. Inténtalo más tarde")
            Toast.makeText(this, "Servidor fuera de servicio por lentitud. Inténtalo más tarde", Toast.LENGTH_LONG).show()
        } finally {
            binding.pbLoading.visibility = android.view.View.GONE
            binding.btnNext.isEnabled = true
            binding.btnBulk.isEnabled = true
        }
    }

    private fun setLoadingState(message: String) {
        binding.pbLoading.visibility = android.view.View.VISIBLE
        binding.btnNext.isEnabled = false
        binding.btnBulk.isEnabled = false
        binding.tvStatus.text = message
        binding.tvStatus.setTextColor(Color.DKGRAY)
    }

    private fun setIdleState(message: String) {
        binding.pbLoading.visibility = android.view.View.GONE
        binding.tvStatus.text = message
        binding.tvStatus.setTextColor(Color.DKGRAY)
    }

    private fun setErrorState(message: String) {
        binding.pbLoading.visibility = android.view.View.GONE
        binding.tvStatus.text = message
        binding.tvStatus.setTextColor(Color.RED)
    }
}