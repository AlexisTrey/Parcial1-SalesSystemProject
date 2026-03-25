# Sales System Project

El sistema permite gestionar información básica de personas, productos y movimientos contables desde una interfaz de consola.

---

## Autor

**Yulian Alexis Tobar Rios**

Código: 202222448

---

## Tecnologías utilizadas

* Java 21
* Maven
* Lombok
* Arquitectura MVP
* Persistencia en archivos CSV
* Internacionalización (i18n)
* Estructuras de datos personalizadas
* Librería externa de configuración (config-lib)

---

## Funcionalidades principales

El sistema permite administrar:

### Personas

* Registrar personas
* Listar personas
* Eliminar personas
* Exportar información

### Productos

* Registrar productos
* Listar productos
* Eliminar productos
* Exportar información

### Contabilidad

* Registrar movimientos contables
* Listar movimientos
* Exportar información

---

## Librería de configuración

El proyecto utiliza una librería externa llamada `config-lib` para la lectura de archivos `.properties`.

Esta librería permite:

- Leer configuraciones internas (dentro del JAR)
- Leer configuraciones externas (fuera del JAR)
- Sobrescribir configuraciones sin necesidad de recompilar

Gracias a esto, el sistema puede manejar archivos como:

- `config/config.properties`
- `i18n/messages_*.properties`

de forma flexible y reutilizable en otros proyectos.

---

## Requisitos

* Java 21 o superior
* Maven

---

## Compilar el proyecto

```bash
mvn clean compile
```

---

## Generar el JAR

```bash
mvn clean package
```

El archivo generado se encontrará en:

```
target/SalesSystemProject-1.0.jar
```

---

## Ejecutar el proyecto

```bash
java -jar target/SalesSystemProject-1.0.jar
```

---

## Dependencias

El proyecto utiliza las siguientes dependencias:

* **Lombok 1.18.42**
* **config-lib 1.0** 

---

## Versión

**1.0**
