🎮 VideoGame Library – MVVM Edition

Aplicación Android desarrollada en Kotlin que simula una biblioteca de videojuegos en la “nube”, implementando arquitectura MVVM (Model-View-ViewModel), corrutinas y manejo reactivo de estado con LiveData.

🚀 Descripción del Proyecto

La aplicación permite:

Cargar una lista de videojuegos simulando conexión a un servidor remoto.

Mostrar un juego principal destacado.

Navegar entre los juegos cargados.

Visualizar la lista restante sin duplicar el juego principal.

Manejar estados de carga y errores de forma reactiva.

Mantener el estado al rotar la pantalla.

🏗 Arquitectura

El proyecto fue refactorizado desde MVC hacia MVVM, desacoplando la lógica de negocio de la interfaz.

🧠 Patrón MVVM Implementado
1️⃣ Model

VideoGame (data class)

GameRepository

Simula latencia de red (2–5 segundos)

Probabilidad de fallo aleatorio

Devuelve datos usando Result

2️⃣ ViewModel

Contiene la lógica de negocio

Expone estado mediante LiveData

games

currentGame

isLoading

errorMessage

Usa viewModelScope.launch para corrutinas

Sobrevive a rotación de pantalla

3️⃣ View (MainActivity)

Observa los LiveData

No accede directamente al Repository

Solo reacciona a cambios de estado

No contiene lógica de negocio

🔄 Flujo de Funcionamiento

Usuario presiona "Cargar juegos"

ViewModel:

Activa estado de carga

Llama al Repository

Recibe datos o error

Activity observa cambios:

Muestra ProgressBar

Actualiza UI automáticamente

Muestra Toast en caso de error

Rotación de pantalla:

El ViewModel conserva estado

La lista y el juego actual no se reinician

⚙️ Simulación de Backend

El repositorio simula:

Latencia aleatoria entre 2000ms y 5000ms

Probabilidad de fallo del 20%

Carga de múltiples juegos simultáneamente

🧪 Prueba de Persistencia

Mientras la aplicación está descargando juegos:

Rotar el dispositivo.

El ProgressBar permanece activo.

La descarga no se reinicia.

La lista aparece automáticamente cuando termina
