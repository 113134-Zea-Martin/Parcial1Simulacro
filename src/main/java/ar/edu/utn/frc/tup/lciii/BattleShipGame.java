package ar.edu.utn.frc.tup.lciii;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Esta clase mantiene el estado de un juego.
 * <p>
 * Un juego es una partida de las muchas que puede jugar el Player
 * en una misma corrida del programa.
 */
public class BattleShipGame {

    /**
     * Expresion regular para validar entradas de posiciones
     */
    private static final String POSITION_INPUT_REGEX = "[0-9]{1} [0-9]{1}";

    /**
     * Numero de barcos requeridos para jugar
     */
    private static final Integer FLEET_SIZE = 2;

    /**
     * Scanner para capturar las entradas del usuario
     */
    private Scanner scanner = new Scanner(System.in);

    /**
     * Jugador asignado al usuario
     */
    private Player player;

    /**
     * Jugador asignado a la app
     */
    private Player appPlayer;

    /**
     * Tablero de la flota del jugador
     */
    private Board playerFleetBoard;

    /**
     * Tablero de marcación de la flota enemiga del jugador
     */
    private Board playerEnemyFleetBoard;

    /**
     * Tablero de la flota de la app
     */
    private Board appFleetBoard;

    /**
     * Tablero de marcación de la flota enemiga de la app
     */
    private Board appEnemyFleetBoard;

    /**
     * Lista de los disparos efectuados por el jugador
     */
    private List<Position> playerShots;

    /**
     * Lista de los disparos efectuados por la app
     */
    private List<Position> appShots;

    /**
     * Lista de los barcos del jugador
     */
    private List<Ship> playerShips;

    /**
     * Lista de los barcos de la app
     */
    private List<Ship> appShips;

    /**
     * Jugador que gano el juego
     */
    private Player winner;

    private Integer appPuntos;

    private Integer playerPuntos;
    // TODO: more attributes if necessary

    // TODO: getters & setters...

    // TODO: constructors if necessary...
    public BattleShipGame(Player player, Player appPlayer) {
        this.player = player;
        this.appPlayer = appPlayer;
        this.playerFleetBoard = new Board();
        this.playerEnemyFleetBoard = new Board();
        this.appFleetBoard = new Board();
        this.appEnemyFleetBoard = new Board();
        this.playerShots = new ArrayList<>();
        this.appShots = new ArrayList<>();
        this.playerShips = new ArrayList<>();
        this.appShips = new ArrayList<>();
        this.winner = null;
        this.playerFleetBoard.initBoardFleet();
        this.playerEnemyFleetBoard.initBoardEnemyFleet();
        this.appFleetBoard.initBoardFleet();
        this.appEnemyFleetBoard.initBoardEnemyFleet();
        this.appPuntos = 0;
        this.playerPuntos = 0;
    }

    public BattleShipGame() {
    }

    /**
     * Este metodo genera una lista de posiciones aleatoria para
     * la flota de barcos con la que jugará la App.
     * <p>
     * Este metodo valida que las posiciones de la cada barco de la flota es unica
     * y que se encuentra dentro de los margenes del tablero.
     * <p>
     * Por cada barco de la flota debe agregarlo en la lista "appShips"
     *
     * @see #getPlayerFleetPositions()
     * @see #generateAppShot()
     * @see #getRandomPosition()
     */
    public void generateAppFleetPositions() {
        do {
            Position position = this.getRandomPosition();
            if (isAvailablePosition(appShips, position)) {
                this.appShips.add(new Ship(position, ShipStatus.AFLOAT));
            }
        } while (this.appShips.size() < FLEET_SIZE);
        appFleetBoard.setShipPositions(appShips);
    }

    /**
     * Este metodo gestiona el pedido de posiciones de cada barco al jugador,
     * y los agrega en la lista "playerShips".
     * <p>
     * Se le pide al usuario por pantalla cada par de coordenadas como
     * dos Enteros separados por un espacio en blanco. Por cada coordenada que el usuario ingresa,
     * debe validarse que este dentro de los margenes del tablero y que NO haya colocado ya
     * otro barco en dicha posicion.
     * <p>
     * Cuando el usuario ha colocado todos los barcos (20 en total),
     * el metodo los posiciona en el tablero del usuario.
     *
     * @see #generateAppFleetPositions()
     * @see #getPlayerShot()
     */
    public void getPlayerFleetPositions() {
        int numeroBarco = 1;
        do {
            System.out.println("Donde quiere posicionar su barco" + numeroBarco + "?");
            Position position = this.getPosition();
            if (isAvailablePosition(playerShips, position)) {
                this.playerShips.add(new Ship(position, ShipStatus.AFLOAT));
                numeroBarco++;
            } else {
                System.out.println("Ya establesio un barco en esa posicion.!");
            }
        } while (this.playerShips.size() < FLEET_SIZE);
        playerFleetBoard.setShipPositions(playerShips);
        // TODO: Hacer un bucle para pedir las posiciones hasta alcanzar el limite.DONE
        // TODO: Mostrar un mensaje por pantalla pidiendo posicionar el barco.DONE
        // TODO: Usar el metodo this.getPosition() para pedir la posicion.DONE
        // TODO: Validar si la posicion esta disponible en la lista de barcos.DONE
        // TODO: Mostrar un mensaje de error comentando que ya establesio esa posicion.DONE
        // TODO: Si esta disponible, crear el barco y agregarlo a la lista de barcos.DONE
        // TODO: Al finalizar el bucle, setear en el board las posiciones de los barcos.DONE

    }

    /**
     * Este metodo gestiona la acción de disparar por parte del usuario.
     * Cuando el usuario estableció el disparo, debe agregarlo a la lista de
     * disparos realizados "playerShots" y cargarlo en su board de la flota enemiga "playerEnemyFleetBoard"
     * según haya derribado un barco o encontrado agua.
     * <p>
     * Si el disparo alcanza un barco enemigo, se debe cambiar el barco de dicha posicion a ShipStatus.SUNKEN
     * mediante el metodo de Ship "sinkShip()"
     * <p>
     * Se le pide al usuario por pantalla cada par de coordenadas como
     * dos Enteros separados por un espacio en blanco. Por cada coordenada que el usuario ingresa,
     * debe validarse que este dentro de los margenes del tablero.
     *
     * @see #getPlayerFleetPositions()
     */
    public void getPlayerShot() {
        Position position = null;
        do {
            System.out.println("Donde quiere disparar?");
            position = this.getPosition();
            if (isAvailableShot(playerShots, position)) {
                this.playerShots.add(position);
                if (this.impactEnemyShip(appShips, position)) {
                    playerEnemyFleetBoard.setShipOnBoard(position);
                    System.out.println("Ha impactado en un barco.!");
                } else {
                    playerEnemyFleetBoard.setWaterOnBoard(position);
                    System.out.println("Ha impactado en el agua.!");
                }
            } else {
                position = null;
                System.out.println("Ya disparó a esa posición.!" +
                        System.lineSeparator() + "Elija otra posicion...");
            }
        } while (position == null);
        // TODO: Preguntar si la posicion del disparo impacto un barco enemigo.DONE
        // TODO: Setear segun hubo un impacto o no, agua o un barco, en el tablero de marcacion de la flota enemiga.DONE
    }

    /**
     * Este metodo genera de manera aleatoria un disparo por parte de la app.
     * El metodo genera dos enteros entre 0 y 9 para definir las coordenadas
     * donde efectuará el disparo.
     * <p>
     * El metodo valida que el disparo no se haya hecho antes de cargarlo en
     * la lista de disparos de la app.
     * <p>
     * Cuando la app estableció el disparo, debe agregarlo a la lista de
     * disparos realizados "appShots" y cargarlo en su board de la flota enemiga "appEnemyFleetBoard"
     * según haya derribado un barco o encontrado agua.
     * <p>
     * Si el disparo alcanza un barco enemigo, se debe cambiar el barco de dicha posicion a ShipStatus.SUNKEN
     * mediante el metodo de "ship.sinkShip()"
     *
     * @see #generateAppFleetPositions()
     * @see #getRandomPosition()
     */
    public void generateAppShot() {
        Position randomShot = new Position();
        boolean aux = false;

        do {
            randomShot = this.getRandomPosition();
            if (isAvailablePosition(appShips, randomShot)) {
                aux = true;
                this.appShots.add(randomShot);
                if (this.impactEnemyShip(playerShips, randomShot)) {
                    appEnemyFleetBoard.setShipOnBoard(randomShot);
                    playerFleetBoard.setRedX(randomShot);
                    System.out.println("El enemigo impacto en tu barco.!");
                } else {
                    appEnemyFleetBoard.setWaterOnBoard(randomShot);
                    System.out.println("El enemigo impacto en el agua.!");
                }
            }
        } while (!aux);
        // TODO: Generar una posicion random para el disparo de la app usando el metodo getRandomPosition().DONE
        // TODO: Validar si aun no se uso esa posicion en la lista de disparos de la app.DONE
        // TODO: Si el disparo ya se hizo, volver a generar disparos hasta que salga alguno valido.DONE
        // TODO: Cuando el disparo no haya sido usado antes, agregarlo a la lista de disparos de la app.DONE
    }

    /**
     * Este metodo imprime por pantalla el estado del juego, que incluye
     * cuantos barcos tiene cada jugar a flote y cuantos hundidos
     *
     * @see Player
     * @see #playerShips
     * @see #appShips
     * @see #player
     * @see #appPlayer
     */
    public void printGameStatus() {
        System.out.println("Status del juego: ");
        System.out.println("Jugador: " + player.getPlayerName());
        int playerShipsAFLOAT = 0;
        int playerShipsSUNKEN = 0;
        int appShipsAFLOAT = 0;
        int appShipsSUNKEN = 0;
        for (Ship ship : playerShips) {
            if (ship.getShipStatus().equals(ShipStatus.AFLOAT)) {
                playerShipsAFLOAT++;
            } else {
                playerShipsSUNKEN++;
            }
        }

        for (Ship ship : appShips) {
            if (ship.getShipStatus().equals(ShipStatus.AFLOAT)) {
                appShipsAFLOAT++;
            } else {
                appShipsSUNKEN++;
            }
        }
        System.out.println("Barcos flotando: " + playerShipsAFLOAT + ".");
        System.out.println("Barcos hundidos: " + playerShipsSUNKEN + ".");
        System.out.println();
        System.out.println("Jugador: APP");
        System.out.println("Barcos flotando: " + appShipsAFLOAT + ".");
        System.out.println("Barcos hundidos: " + appShipsSUNKEN + ".");
        // TODO: Imprimir por pantalla el status del juego.DONE
        // TODO: Incluir barcos flotando y hundidos de cada jugador.DONE
    }

    /**
     * Este metodo dibuja los tableros del Player junto al titulo de cada uno.
     *
     * @see Board#drawBoard()
     */
    public void drawPlayerBoards() {
        System.out.println("TU FLOTA" + System.lineSeparator());
        playerFleetBoard.drawBoard();
        // TODO: Dibujar el tablero del usuario.DONE
        System.out.println("FLOTA ENEMIGA" + System.lineSeparator());
        // TODO: Dibujar el tablero de marcacion de la flota enemiga del usurio.DONE
        playerEnemyFleetBoard.drawBoard();
    }

    /**
     * Este metodo muestra un mensaje de finalizacion de la partida,
     * muestra el nombre del ganador, el puntaje obtenido en esta partida
     * y los puntajes acumulados a traves de las partidas jugadas.
     *
     * @see System#out
     */
    public void goodbyeMessage() {
        System.out.println("La partida ha finalizado.!");
        System.out.println("El jugador " + player.getPlayerName() + " ha ganado " + playerPuntos + " puntos. Ahora tiene un total de " + player.getScore() + ". Y tiene " +
                player.getGamesWon() + " juegos ganados.!");
        System.out.println("El jugador APP ha ganado " + appPuntos + " puntos. Ahora tiene un total de " + appPlayer.getScore() + ". Y tiene " +
                appPlayer.getGamesWon() + " juegos ganados.!");
        // TODO: Imprimir por pantalla un mensaje de despedida.DONE
    }

    /**
     * Este metodo calcula los puntos obtenidos por cada jugador en esta partida
     * y se los suma a los que ya traia de otras partidas.
     *
     * @see Player
     * @see #playerShips
     * @see #appShips
     * @see #player
     * @see #appPlayer
     */
    public void calculateScores() {


        for (Ship ship : playerShips) {
            if (ship.getShipStatus().equals(ShipStatus.SUNKEN)) {
                appPuntos++;
            }
        }
        appPlayer.setScore(appPlayer.getScore() + appPuntos);


        for (Ship ship : appShips) {
            if (ship.getShipStatus().equals(ShipStatus.SUNKEN)) {
                playerPuntos++;
            }
        }
        player.setScore(player.getScore() + playerPuntos);

        if (FLEET_SIZE == playerPuntos) {
            if (winner == player) {
                System.out.println("El ganador ha sido el juegador " + player.getPlayerName());
            } else {
                System.out.println("El ganador ha sido el jugador APP");
            }
        }
        // TODO: Se debe completar este metodo.DONE
        // TODO: Calcular los puntos obtenidos por cada jugador en este juego.DONE
        // TODO: Sumar los puntos al score de cada jugardor.DONE
        // TODO: Sumar la partida ganada al jugador que ganó.DONE
    }

    /**
     * Este metodo verifica si hubo un impacto con el disparo,
     * si el disparo impacto, hunde el barco y retorna true.
     * Si el disparo no impacto retorna false
     *
     * @param fleetEnemyShips Lista de barcos de la flota enemiga
     * @param shot
     * @return true si el disparo impacta, false si no lo hace.
     * @see Position#equals(Object)
     * @see Ship#sinkShip()
     */
    private Boolean impactEnemyShip(List<Ship> fleetEnemyShips, Position shot) {
        for (Ship ship : fleetEnemyShips) {
            if (shot.equals(ship.getPosition())) {
                ship.sinkShip();
                return true;
            }
        }
        // TODO: Recorrer la lista de Ships pasada por parametro y validar si existe un barco en la posicion pasada por parametro.DONE
        // TODO: Si existe un barco en esa posicion, hundir el barco el metodo sinkShip().DONE
        // TODO: Retornar true si se hundio un barco, o false si no se hizo.DONE

        // TODO: Remember to replace the return statement with the correct object.DONE
        return false;
    }

    /**
     * Este metodo define si el juego terminó.
     * El juego termina cuando uno de los dos jugadores (El player o la app)
     * a hundido todos los barcos del contrario.
     * <p>
     * Cuando el juego termina, este metodo setea en el atributo winner quien ganó.
     *
     * @return true si el juego terminó, false si aun no hay un ganador
     * @see #validateSunkenFleet(List)
     * @see #winner
     */
    public Boolean isFinish() {

        if (validateSunkenFleet(appShips)) {
            winner = player;
            player.setGamesWon(player.getGamesWon() + 1);
            return true;
        } else if (validateSunkenFleet(playerShips)) {
            winner = appPlayer;
            appPlayer.setGamesWon(appPlayer.getGamesWon() + 1);
            return true;
        }

        return false;

        // TODO: Validar si todos lo barcos de algun jugador fueron undidos.DONE
        // TODO: Si algun jugador ya perdio todos sus barcos, setear el ganador en winner.DONE
        // TODO: Retornar true si hubo un ganador, o false si no lo hubo.DONE

        // TODO: Remember to replace the return statement with the correct object.DONE
    }

    /**
     * Este metodo valida si aun queda algun barco a flote en la flota,
     * para determinar si toda la flota fue hundida.
     *
     * @param fleet
     * @return true si toda la flota fue hundida, flase si al menos queda un barco a flote.
     * @see Ship#getShipStatus()
     * @see ShipStatus
     */
    private Boolean validateSunkenFleet(List<Ship> fleet) {
        for (Ship ship : fleet) {
            if (ship.getShipStatus().equals(ShipStatus.AFLOAT)) {
                return false;
            }
        }
        // TODO: Recorrer la lista de barcos y validar si todo fueron undidos.DONE
        // TODO: Retornar true si todos fueron undidos o false si al menos queda un barco a flote.DONE

        // TODO: Remember to replace the return statement with the correct object.DONE
        return true;
    }

    /**
     * Este metodo gestiona la acción de pedir coordenads al usuario.
     * <p>
     * Se le pide al usuario por pantalla cada par de coordenadas como
     * dos Enteros separados por un espacio en blanco. Por cada coordenada que el usuario ingresa,
     * debe validarse que este dentro de los margenes del tablero.
     *
     * @return La posicion elegida por el usuario.
     * @see #isValidPositionInput(String)
     * @see Position#Position()
     * @see String#split(String)
     */
    private Position getPosition() {
        Position position = null;
        do {
            System.out.println("Ingrese una coordenada en un formato de dos numeros " +
                    "enteros entre 0 y 9 separados por un espacio en blanco.");

            String input = scanner.nextLine();

            if (isValidPositionInput(input)) {
                String[] parts = input.split(" ");
                int primerCoor = Integer.parseInt(parts[0]);
                int segundaCoor = Integer.parseInt(parts[1]);
                position = new Position(primerCoor, segundaCoor);
                //TODO: Separar los enteros de input en dos Integers y crear Position.DONE
            } else {
                System.out.println("Error: Las coordenadas deben ser dos números enteros " +
                        "separados por un espacio. Ambos deben estar entre 0 y 9.");
                // TODO: Mostrar un mensaje de error sobre como ingreso los datos.DONE
            }
        } while (position == null);
        return position;
    }

    /**
     * Este metodo valida que la entrada del usuario como String este en el formato establecido
     * de dos numeros enteros entre 0 y 9 separados por un espacio en blanco.
     *
     * @param input La entrada que se capturo del usuario
     * @return true si el formato es valido, false si no lo es
     * @see BattleShipMatch#getYesNoAnswer(String)
     * @see Pattern#compile(String)
     * @see Pattern#matcher(CharSequence)
     * @see Matcher#matches()
     */
    private Boolean isValidPositionInput(String input) {
        Pattern pattern = Pattern.compile(POSITION_INPUT_REGEX);
        if (pattern.matcher(input).matches()) {
            return true;
        } else {
            return false;
        }
        // TODO: Remember to replace the return statement with the correct object.DONE
        //return null;
    }

    /**
     * Este metodo retorna una posición random que puede ser usada
     * para representar una posicion de un barco o un disparo random de la app.
     *
     * @return la posición del disparo random.
     * @see Random
     * @see Random#nextInt(int)
     * @see Position
     */
    private Position getRandomPosition() {
        Random random = new Random();
        int posicionFila = random.nextInt(10);
        int posicionColumna = random.nextInt(10);
        // TODO: Generar 2 numeros random entre 0 y 9 para establecer la row y column.DONE
        // TODO: Crear la Posicion a partir de la row y column.DONE
        // TODO: Retornar la Position.DONE

        // TODO: Remember to replace the return statement with the correct object.DONE
        return new Position(posicionFila, posicionColumna);
    }

    /**
     * Este metodo valida que la posición nueva no exista en la lista de posiciones que ya fueron cargadas
     * para eso recibe por parametro la lista donde hará la busqueda y la posicion a buscar.
     * <p>
     * El metodo otorga un mecanismo de validacion de que un objeto del tipo Position
     * no existe en una lista del tipo List<Position>
     *
     * @param listShots la lista donde se hará la busqueda
     * @param position  La nueva posicion a buscar
     * @return true si la posición no existe en la lista, false si ya existe esa posicion.
     * @see List#contains(Object)
     * @see Position#equals(Object)
     */
    private Boolean isAvailableShot(List<Position> listShots, Position position) {
        for (Position shot : listShots) {
            if (shot.equals(position)) {
                return false;
            }
        }
        // TODO: Validar que la lista de posiciones NO tenga la posision indicada.DONE

        // TODO: Remember to replace the return statement with the correct object.DONE
        return true;
    }

    /**
     * Este metodo valida que la posición pasada por parametro no exista dentro de las
     * posiciones de los barcos de la lista "listToCheck".
     *
     * @param listToCheck la lista donde se hará la busqueda
     * @param position    La nueva posicion a buscar
     * @return true si la posición no existe en la lista, false si ya existe esa posicion.
     * @see List#contains(Object)
     * @see Ship#equals(Object)
     */
    private Boolean isAvailablePosition(List<Ship> listToCheck, Position position) {
        for (Ship ship : listToCheck) {
            if (ship.getPosition().equals(position)) {
                return false;
            }
        }
        // TODO: Validar que la lista de Ship NO tenga un Ship con la posision indicada.DONE

        // TODO: Remember to replace the return statement with the correct object.DONE
        return true;
    }

}
