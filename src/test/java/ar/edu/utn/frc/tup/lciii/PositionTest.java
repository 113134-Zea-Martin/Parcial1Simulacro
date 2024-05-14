package ar.edu.utn.frc.tup.lciii;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    @Test
    void testEquals() {
        // TODO: Probar este metodo publico
        Position position1 = new Position(5, 7);
        Position position2 = new Position(5, 7);

        // Crear un objeto Position con diferentes coordenadas
        Position position3 = new Position(3, 4);

        // Probar que position1 es igual a position2
        assertTrue(position1.equals(position2));

        // Probar que position1 no es igual a position3
        assertFalse(position1.equals(position3));
    }
}