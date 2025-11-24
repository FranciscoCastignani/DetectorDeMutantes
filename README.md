# Mutantes API

API REST para la detección de secuencias de ADN mutante.

## 🚀 Tecnologías Utilizadas

* **Java 17:** Lenguaje principal.
* **Spring Boot:** Framework para el desarrollo de la API.
* **Gradle:** Herramienta de construcción y gestión de dependencias.
* **Docker:** Contenerización de la aplicación.
* **JUnit 5:** Librería para pruebas unitarias.

## 💻 Instrucciones de Ejecución

Para ejecutar la aplicación, esta se encuentra ya desplegada y lista para probarse en **Render**:

👉 **Base URL:** [https://mercadolibreprueba-mzd8.onrender.com/](https://mercadolibreprueba-mzd8.onrender.com/)

Para ejecutar la aplicación localmente, primero asegúrate de tener el **JDK 17** instalado. Dependiendo de tu sistema operativo, utiliza los siguientes comandos en la terminal dentro de la carpeta raíz del proyecto.

### En Windows (PowerShell o CMD)

No necesitas configurar permisos, simplemente ejecuta el script `gradlew`:

.\gradlew bootRun

En Linux o macOS

Es posible que primero necesites dar permisos de ejecución al script de Gradle:
# 1. Dar permisos de ejecución
chmod +x gradlew

# 2. Ejecutar la aplicación
./gradlew bootRun

Ejecución con Docker (Opcional)

Si prefieres no instalar Java localmente y usar el contenedor:

  1 - Construir la imagen:
  docker build -t mutantes-api .

  2 - Correr el contenedor:
  docker run -p 8080:8080 mutantes-api

La aplicación estará disponible en: http://localhost:8080
### Endpoint: /mutant
  **Ejemplo En local:**
  * **Método:** "POST"
  * **URL Relativa:** "/mutant"
  * **URL Local:** http://localhost:8080/mutant
  * **Body:**
    {
      "dna": ["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
    }
    
  * **RESPUESTA:** 200 OK (Es Mutante) o 403 Forbidden (Es Humano).

  **Ejemplo desde la API en Render:**
  * **Método:** "POST"
  * **URL Relativa:** "/mutant"
  * **URL Deploy:** https://mercadolibreprueba-mzd8.onrender.com/mutant
  * **Body:**
    {
      "dna": ["ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG"]
    }
    
  * **RESPUESTA:** 200 OK (Es Mutante) o 403 Forbidden (Es Humano).


### Endpoint: /stats
Devuelve un resumen de las verificaciones de ADN realizadas, indicando la cantidad de mutantes, humanos y el ratio de mutantes.

  **Ejemplo En local:**
  * **Método:** "GET"
  * **URL Relativa:** "/stats"
  * **URL Local:** http://localhost:8080/stats
  * **Body:**  No requiere cuerpo.
  * **RESPUESTA:**
    {
          "count_mutant_dna": 40,
          "count_human_dna": 100,
          "ratio": 0.4
      }

  **Ejemplo desde la API en Render:**
  * **Método:** "GET"
  * **URL Relativa:** "/stats"
  * **URL Deploy:** https://mercadolibreprueba-mzd8.onrender.com/stats
  * **Body:** No requiere cuerpo.
  * **RESPUESTA:**
    {
          "count_mutant_dna": 40,
          "count_human_dna": 100,
          "ratio": 0.4
      }
  
