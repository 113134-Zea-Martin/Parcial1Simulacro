package ar.edu.utn.frc.tup.lciii;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;


class BattleShipMatchTest {

    @Test
    void testGetYesNoAnswer() throws Exception {
        Method method = BattleShipMatch.class.getDeclaredMethod("getYesNoAnswer", String.class);
        method.setAccessible(true);

        Boolean resultY = (Boolean) method.invoke(null, "y");
        assertTrue(resultY);

        Boolean resultN = (Boolean) method.invoke(null, "n");
        assertFalse(resultN);

        Boolean resultInvalid = (Boolean) method.invoke(null, "invalid");
        assertNull(resultInvalid);
    }


//    @Test
//    void testGetYesNoAnswer() {
//        // TODO: Probar este metodo privado
//        assertTrue(false);
//    }

//    @Test
//    void testGetYesNoAnswer() throws Exception {
//        // Obtén la clase que contiene el método
//        Class<?> clazz = Class.forName("ar.edu.utn.frc.tup.lciii.BattleShipMatch");
//
//        // Obtén el método privado
//        Method method = clazz.getDeclaredMethod("getYesNoAnswer", String.class);
//
//        // Hacer el método accesible
//        method.setAccessible(true);
//
//        // Crear una instancia de la clase
//        Object instance = clazz.getDeclaredConstructor().newInstance();
//
//        // Prueba con "y"
//        Boolean result = (Boolean) method.invoke(instance, "y");
//        assertTrue(result);
//
//        // Prueba con "Y"
//        result = (Boolean) method.invoke(instance, "Y");
//        assertTrue(result);
//
//        // Prueba con "n"
//        result = (Boolean) method.invoke(instance, "n");
//        assertFalse(result);
//
//        // Prueba con "N"
//        result = (Boolean) method.invoke(instance, "N");
//        assertFalse(result);
//
//        // Prueba con un valor no válido
//        result = (Boolean) method.invoke(instance, "invalid");
//        assertNull(result);
//    }


}