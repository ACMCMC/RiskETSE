# RiskETSE

## Aldán Creo, Hugo Gómez

Para lanzar el error 126, "Carta de equipamiento ya asignada", como la semántica no especifica, hemos decidido que se refiere a "ya asignada al conjunto de cartas de equipamiento del jugador", y no a "ya asignada al conjunto de cartas de equipamiento de algún jugador de la partida".

Al implementar la clase abstracta de las cartas de equipamiento, ya que cada una se refiere a un país, he modificado la declaración de la clase abstracta para que lo guarde también.

El comando `jugador` o `describir jugador [nombre]`, en el campo `numEjercitoRearme` muestra los ejércitos que le corresponderían a un jugador al inicio de su turno, no los ejércitos que le quedan por rearmar en el turno actual.

Una vez que se asigna una carta de equipamiento al jugador, ya no puede atacar. Lo único que puede hacer es rearmar, antes de acabar su turno.

Si los archivos que incluyen la información del mapa no existen o no se encuentran en el momento de la ejecución del programa, se crearán automáticamente.

Hemos añadido códigos de error con ID 0, cuando se producen errores de lectura de los archivos.

Los comandos optativos `repartir ejercitos` y `cambiar cartas todas` han sido implementados.