# Pruebas en métodos privados

En Java, los métodos privados no se pueden probar directamente desde un caso de prueba, ya que no son accesibles fuera de la clase en la que se definen. Sin embargo, hay algunas formas de probar métodos privados en Java que permiten acceder a los métodos privados desde el caso de prueba.

Java proporciona la API de reflexión para acceder a los miembros de una clase, incluidos los métodos privados. Para usar la reflexión para acceder a un método privado, puedes hacer lo siguiente:

1. Obtener la clase que contiene el método privado.
2. Obtener una referencia al método privado.
3. Establecer al método accesible.
4. Invocar al método privado.

## Reflexión

La reflexión es una técnica que permite a un programa inspeccionar y manipular objetos en tiempo de ejecución, incluso al no ser conocido su tipo en tiempo de compilación. Con reflexión, se puede examinar la estructura de una clase, usar una instancia de una clase, invocar un método en un objeto, etc.

Reflection se usa comúnmente en bibliotecas y frameworks de Java para aumentar la flexibilidad y extensibilidad. Por ejemplo, un framework de inyección de dependencias puede usar reflexión para crear objetos de clase y establecer sus dependencias en tiempo de ejecución o un framework de test puede usarla para facilitar el testing de métodos privados sin que esto implique modificar la definición de la clase en tiempo de desarrollo.

Si bien la reflexión puede ser útil en algunos casos, es importante tener en cuenta que el acceso a campos y métodos privados a través de la reflexión puede ser considerado una mala práctica de programación ya que rompe el encapsulamiento y puede generar problemas de mantenimiento y legibilidad en el código. Por lo tanto, es importante utilizar la reflexión con precaución y sólo cuando no haya otra alternativa para realizar una prueba unitaria.

## ReflectionSupport (JUnit)

La clase `ReflectionSupport` del paquete `org.junit.platform.commons.support` de JUnit es una utilidad que proporciona métodos para trabajar con reflexión en el contexto de pruebas unitarias. Esta clase es parte de la plataforma de JUnit y se puede utilizar en cualquier versión de JUnit que utilice la plataforma de JUnit 5.

## Ejemplos de Uso

### Invocar un Método Privado en una Clase

Supongamos que tienes una clase `MiClase` que tiene un método privado llamado `metodoPrivado()` que deseas invocar en una prueba. Puedes hacerlo de la siguiente manera:

```java
import org.junit.platform.commons.support.ReflectionSupport;

MiClase instancia = new MiClase();

ReflectionSupport.invokeMethod(
    ReflectionSupport.findMethod(MiClase.class, "metodoPrivado"),
    instancia
); 
```

En este ejemplo, utilizamos el método `invokeMethod` de ReflectionSupport 
para invocar el método privado metodoPrivado en la instancia de MiClase. Primero, 
utilizamos el método `findMethod` de ReflectionSupport para buscar el método 
privado metodoPrivado en la clase MiClase. Luego, pasamos el método y la instancia 
a invokeMethod para invocar el método.

...

### Obtener el Valor de un Campo Privado en una Clase

Supongamos que tienes una clase `MiClase` que tiene un campo privado llamado `campoPrivado` que deseas obtener en una prueba. Puedes hacerlo de la siguiente manera:

```java
import org.junit.platform.commons.util.ReflectionSupport;

// ...

Object instancia = new MiClase();
Object valor = ReflectionSupport.getFieldValue(
    ReflectionSupport.findField(MiClase.class, "campoPrivado"),
    instancia
);
```

En este ejemplo, utilizamos el método `getFieldValue` de ReflectionSupport 
para obtener el valor del campo privado campoPrivado en la instancia de MiClase. 
Primero, utilizamos el método `findField` de ReflectionSupport para buscar el campo 
privado campoPrivado en la clase MiClase. Luego, pasamos el campo y la instancia 
a `getFieldValue` para obtener el valor del campo.


## `findMethod()`
El método `findMethod` se utiliza para buscar un método en una clase utilizando su nombre y parámetros. Acepta dos argumentos:
- `Class<?> clazz`: La clase en la que se va a buscar el método.
- `String methodName`: El nombre del método que se desea buscar.

Opcionalmente, se pueden especificar los tipos de los parámetros del método utilizando un arreglo de objetos `Class<?>` como tercer argumento.

El método devuelve un objeto `Optional<Method>`, que puede estar presente o no, representando la opción de haber encontrado el método buscado o un opcional vacío si no se encuentra.

## `invokeMethod()`
El método `invokeMethod` se utiliza para invocar un método en una instancia de una clase. Acepta tres argumentos:
- `Object instance`: La instancia de la clase en la que se va a invocar el método.
- `Method method`: El objeto `Method` que representa el método que se va a invocar.
- `Object... args`: Los argumentos que se van a pasar al método (opcional).

El método devuelve un objeto `Object` que representa el resultado de la invocación del método. Si el método no devuelve ningún valor (es decir, su tipo de retorno es `void`), devuelve `null`.

## `findField()`
El método `findField` se utiliza para buscar un campo (o atributo) en una clase. Acepta dos argumentos:
- `Class<?> clazz`: La clase en la que se va a buscar el campo.
- `String fieldName`: El nombre del campo que se va a buscar.

El método devuelve un objeto `Optional<Field>` que representa el campo buscado. Si el campo no se encuentra, el objeto `Optional` está vacío.

# getFieldValue()

El método `getFieldValue()` se utiliza para obtener el valor de un campo (o atributo) de una instancia de una clase. El método acepta dos argumentos:

- **Object instance**: La instancia de la clase en la que se encuentra el campo.
- **Field field**: El objeto Field que representa el campo del que se desea obtener el valor.

El método devuelve un objeto de tipo Object que representa el valor del campo en la instancia de la clase especificada.

# setFieldValue()

El método `setFieldValue()` se utiliza para establecer el valor de un campo (o atributo) de una instancia de una clase. El método acepta tres argumentos:

- **Object instance**: La instancia de la clase en la que se encuentra el campo.
- **Field field**: El objeto Field que representa el campo del que se desea establecer el valor.
- **Object value**: El valor que se desea establecer en el campo.

El método no devuelve ningún valor.
