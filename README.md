# RiskETSE

### Aldán Creo, Hugo Gómez / POO 2020-21

## Comentarios a la entrega

Para lanzar el error 126, "Carta de equipamiento ya asignada", como la semántica no especifica, hemos decidido que se refiere a "ya asignada al conjunto de cartas de equipamiento del jugador", y no a "ya asignada al conjunto de cartas de equipamiento de algún jugador de la partida".

Al implementar la clase abstracta de las cartas de equipamiento, ya que cada una se refiere a un país, hemos modificado la declaración de la clase abstracta para que lo guarde también.

El comando `jugador` o `describir jugador [nombre]`, en el campo `numEjercitoRearme` muestra los ejércitos que le corresponderían a un jugador al inicio de su turno, no los ejércitos que le quedan por rearmar en el turno actual (entendemos que es a eso a lo que se refiere la documentación).

Una vez que se asigna una carta de equipamiento al jugador, ya no puede atacar. Lo único que puede hacer es rearmar, antes de acabar su turno. Sólo se puede asignar una carta de equipamiento por turno.

Si los archivos que incluyen la información del mapa no existen o no se encuentran en el momento de la ejecución del programa, se crearán automáticamente.

El programa controla automáticamente las condiciones de victoria, pero como la documentación no especifica nada al respecto, cuando un jugador gana, la partida continúa igualmente.

Si un jugador alcanza las 6 cartas de equipamiento al inicio de su turno, se realiza una llamada implícita al comando `cambiar cartas todas`.

Hemos añadido algunas excepciones propias, que no se contemplan en la documentación original. Para eso, hemos creado un nuevo tipo de excepción dentro de la jerarquía especificada, que se llama ExcepcionPropia. Se correcponde con los siguientes códigos:

- "El archivo especificado no existe" (200)
- "No se ha podido leer del archivo" (201)
- "No se ha podido leer ni crear el archivo" (202)
- "El formato del archivo no es correcto" (203)
- "No se ha conquistado ningún país" (203). Se lanza cuando un jugador intenta ejecutar `asignar carta [idCarta]`, sin haber conquistado ningún país en su turno.
- "Se ha lanzado una cantidad de dados no permitida" (205). Se lanza cuando un jugador usa `atacar pais1 dados pais2 dados`, si la cantidad de los dados es incorrecta (por ejemplo, los dados "6x2x2x5x2").
- "El valor de los dados no es correcto" (206). Se lanza cuando un jugador usa `atacar pais1 dados pais2 dados`, si el valor de alguno de los dados es incorrecto (por ejemplo, los dados "6x9x2").
- "El formato de los dados no es correcto" (207). Se lanza cuando un jugador usa `atacar pais1 dados pais2 dados`, si el formato de alguno de los dados es incorrecto (por ejemplo, los dados "6xB6x2").

Los comandos optativos `repartir ejercitos` y `cambiar cartas todas` han sido implementados.

Hemos supuesto que `ver mapa` y sus comandos asociados se pueden ejecutar durante la fase de inicialización.

Hemos cambiado el comando `Jugador` por `jugador`, ya que entendemos que los comandos, como regla general, deben ir siempre en minúsculas. Pese a todo, por compatibilidad, mantenemos las dos posibles sintaxis.

Tras la fase inicial de reparto de ejércitos, hemos entendido que se inicia un turno estándar. Como eso implica un reparto de ejércitos, si el jugador introduce `repartir ejercitos`, después va a tener que introducir `repartir ejercitos [num] [pais]`, ya que acaba de comenzar su turno.

La codificación de los archivos puede dar problemas dependiendo de la plataforma. Si, por ejemplo, el comando `repartir ejercitos 2 Japón` lanzase el error 109 ("El país no existe"), hemos implementado una solución que interpreta el nombre sin acentos como si fuera igual al nombre con acento. Por eso, en ese caso, sería recomendable probar la combinación `repartir ejercitos 2 Japon`.

Los comandos de lectura de archivos realizan una asignación por línea, ya que es lo que se especifica (por ejemplo) en `asignar paises [archivo]`. Por eso, si intentamos ejecutar ese mismo comando sin tener asignadas las misiones, tendremos un código de error 118 ("Las misiones no están asignadas"), por cada línea del archivo en sí.