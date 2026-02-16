package ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import model.GameRepository
import model.VideoGame

class GameViewModel : ViewModel() {
    private val repositoy = GameRepository()
    private val _games = MutableLiveData<List<VideoGame>>(emptyList())
    val games: LiveData<List<VideoGame>> = _games
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage
    private val _currentGame = MutableLiveData<VideoGame?>(null)
    val currentGame: LiveData<VideoGame?> = _currentGame
    private var currentIndex = 0

    val gamesForList = MediatorLiveData<List<VideoGame>>().apply {
        fun recompute(){
            val list = games.value.orEmpty()
            value = list.filterIndexed { index, _ -> index != currentIndex }
        }
        addSource(games){recompute()}
        addSource(currentGame){recompute()}
    }

    fun fetchGames()
    {
        //si ya esta cargado, no se reinicia
        if(_isLoading.value == true) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repositoy.fetchGames(count = 3)

            result.onSuccess { list ->
                _games.value = list
                currentIndex = 0
                _currentGame.value = list.firstOrNull()
            }.onFailure { ex -> _errorMessage.value = ex.message ?: "Error desconocido." }

            _isLoading.value = false
        }
    }

    fun nextGame(){
        val list = _games.value.orEmpty()
        if(list.isEmpty()) return

        currentIndex = (currentIndex + 1) % list.size
        _currentGame.value = list[currentIndex]
    }

    fun onMainButtonClick(){
        if(_games.value.isNullOrEmpty()) fetchGames() else nextGame()
    }

    fun clearError(){ _errorMessage.value = null }
}