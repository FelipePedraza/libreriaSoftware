# 📚 Sistema de Librería - Búsqueda y Calificación de Libros

Este proyecto implementa un sistema de librería con funcionalidades de búsqueda de libros y calificación de libros por parte de los usuarios.

## 🚀 Funcionalidades Implementadas

### 1. Búsqueda de Libros
- **Búsqueda básica**: Los usuarios pueden buscar libros por título o autor
- **Búsqueda avanzada**: Filtros por género y disponibilidad en stock
- **Búsqueda case-insensitive**: No importa si escribes en mayúsculas o minúsculas

### 2. Sistema de Calificaciones
- **Calificación de 1 a 5 estrellas**: Los usuarios pueden calificar cualquier libro
- **Actualización de calificaciones**: Si un usuario ya calificó un libro, puede actualizar su calificación
- **Cálculo automático**: El sistema calcula automáticamente el promedio de calificaciones
- **Historial de calificaciones**: Se mantiene un registro de todas las calificaciones

## 🛠️ Tecnologías Utilizadas

- **Java 21**
- **Spring Boot 3.5.5**
- **Spring Data JPA**
- **MariaDB**
- **Lombok**
- **Spring Validation**

## 📋 Requisitos Previos

1. **Java 21** instalado
2. **MariaDB** instalado y ejecutándose
3. **Base de datos** `libreria_db` creada

## 🗄️ Configuración de la Base de Datos

1. Instala MariaDB
2. Crea la base de datos:
```sql
CREATE DATABASE libreria_db;
```
3. Configura las credenciales en `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/libreria_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

## 🚀 Cómo Ejecutar

1. **Clona o descarga el proyecto**
2. **Configura la base de datos** (ver sección anterior)
3. **Ejecuta la aplicación**:
```bash
./gradlew bootRun
```
4. **Abre tu navegador** y ve a: `http://localhost:8080`

## 📡 API Endpoints

### Búsqueda de Libros
- `GET /api/books/search?searchTerm=java&genre=Programming&inStock=true` - Búsqueda avanzada
- `GET /api/books/simple-search?q=java` - Búsqueda simple
- `GET /api/books` - Obtener todos los libros
- `GET /api/books/{id}` - Obtener libro por ID

### Sistema de Calificaciones
- `POST /api/books/rate` - Calificar un libro
- `GET /api/books/{id}/ratings` - Obtener calificaciones de un libro
- `GET /api/books/ratings/user/{userId}` - Obtener calificaciones de un usuario

### Ejemplo de Calificación (JSON)
```json
{
  "bookId": 1,
  "userId": "usuario123",
  "rating": 5,
  "comment": "Excelente libro"
}
```

## 🎯 Ejemplos de Uso

### Búsqueda Simple
```
GET /api/books/simple-search?q=clean code
```

### Búsqueda Avanzada
```
GET /api/books/search?searchTerm=programming&genre=Programming&inStock=true
```

### Calificar un Libro
```bash
curl -X POST http://localhost:8080/api/books/rate \
  -H "Content-Type: application/json" \
  -d '{
    "bookId": 1,
    "userId": "juan123",
    "rating": 5,
    "comment": "Muy bueno"
  }'
```

## 📊 Estructura de la Base de Datos

### Tabla `books`
- `id` (PK)
- `title` - Título del libro
- `author` - Autor del libro
- `description` - Descripción
- `isbn` - ISBN único
- `publication_date` - Fecha de publicación
- `price` - Precio
- `stock_quantity` - Cantidad en stock
- `genre` - Género
- `publisher` - Editorial
- `average_rating` - Calificación promedio
- `total_ratings` - Total de calificaciones

### Tabla `ratings`
- `id` (PK)
- `book_id` (FK) - Referencia al libro
- `user_id` - ID del usuario
- `rating` - Calificación (1-5)
- `comment` - Comentario opcional
- `created_at` - Fecha de creación

## 🎨 Interfaz Web

El proyecto incluye una interfaz web simple en `http://localhost:8080` que permite:
- Buscar libros por título o autor
- Filtrar por género y disponibilidad
- Calificar libros
- Ver calificaciones existentes

## 🔧 Características Técnicas

- **Validación de datos**: Las calificaciones deben estar entre 1 y 5
- **Transacciones**: Operaciones de base de datos con transacciones
- **Logging**: Registro detallado de operaciones
- **CORS habilitado**: Para integración con frontend
- **Datos de prueba**: Se cargan automáticamente libros de ejemplo

## 📝 Notas Importantes

1. **No es necesario comprar el libro**: Los usuarios pueden calificar cualquier libro en el sistema
2. **Actualización de calificaciones**: Si un usuario califica el mismo libro dos veces, se actualiza la calificación anterior
3. **Cálculo automático**: El promedio de calificaciones se calcula automáticamente
4. **Búsqueda flexible**: La búsqueda funciona con palabras parciales en título o autor

## 🐛 Solución de Problemas

### Error de conexión a la base de datos
- Verifica que MariaDB esté ejecutándose
- Confirma las credenciales en `application.properties`
- Asegúrate de que la base de datos `libreria_db` existe

### Puerto ocupado
- Cambia el puerto en `application.properties`: `server.port=8081`

### No se cargan los datos de prueba
- Verifica que la base de datos esté vacía
- Los datos se cargan solo si no hay libros en la base de datos
