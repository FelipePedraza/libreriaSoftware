# 📚 Implementación Completa - Sistema de Librería

## ✅ Funcionalidades Implementadas

He creado un sistema completo de librería que implementa las dos funcionalidades solicitadas:

### 1. 🔍 Búsqueda de Libros
- **Búsqueda básica**: Los usuarios pueden buscar libros por título o autor
- **Búsqueda avanzada**: Filtros por género y disponibilidad en stock
- **Búsqueda case-insensitive**: No importa si escribes en mayúsculas o minúsculas

### 2. ⭐ Sistema de Calificaciones
- **Calificación de 1 a 5 estrellas**: Los usuarios pueden calificar cualquier libro
- **Actualización de calificaciones**: Si un usuario ya calificó un libro, puede actualizar su calificación
- **Cálculo automático**: El sistema calcula automáticamente el promedio de calificaciones
- **Historial de calificaciones**: Se mantiene un registro de todas las calificaciones

## 📁 Estructura de Archivos Creados

```
src/main/java/com/co/edu/uniquindio/libreria/
├── entity/
│   ├── Book.java                    # Entidad principal de libros
│   └── Rating.java                  # Entidad para calificaciones
├── repository/
│   ├── BookRepository.java          # Repositorio con funcionalidad de búsqueda
│   └── RatingRepository.java        # Repositorio para operaciones de calificación
├── dto/
│   ├── BookSearchRequest.java       # DTO para solicitudes de búsqueda
│   ├── BookResponse.java            # DTO para respuestas de libros
│   ├── RatingRequest.java           # DTO para solicitudes de calificación
│   └── RatingResponse.java          # DTO para respuestas de calificación
├── service/
│   └── BookService.java             # Lógica de negocio para búsqueda y calificación
├── controller/
│   └── BookController.java          # Controlador REST con endpoints
└── config/
    └── DataInitializer.java         # Inicializador de datos de prueba

src/main/resources/
├── application.properties           # Configuración de base de datos
└── static/
    └── index.html                   # Interfaz web para probar funcionalidades
```

## 🗄️ Modelo de Base de Datos

### Tabla `books`
```sql
CREATE TABLE books (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    description TEXT,
    isbn VARCHAR(255) UNIQUE,
    publication_date DATE,
    price DECIMAL(10,2),
    stock_quantity INT,
    genre VARCHAR(100),
    publisher VARCHAR(255),
    average_rating DECIMAL(3,2) DEFAULT 0.00,
    total_ratings INT DEFAULT 0
);
```

### Tabla `ratings`
```sql
CREATE TABLE ratings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    book_id BIGINT NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES books(id)
);
```

## 🚀 Endpoints de la API

### Búsqueda de Libros
- `GET /api/books/search?searchTerm=java&genre=Programming&inStock=true` - Búsqueda avanzada
- `GET /api/books/simple-search?q=java` - Búsqueda simple
- `GET /api/books` - Obtener todos los libros
- `GET /api/books/{id}` - Obtener libro por ID

### Sistema de Calificaciones
- `POST /api/books/rate` - Calificar un libro
- `GET /api/books/{id}/ratings` - Obtener calificaciones de un libro
- `GET /api/books/ratings/user/{userId}` - Obtener calificaciones de un usuario

## 📝 Ejemplos de Uso

### 1. Búsqueda Simple
```bash
curl "http://localhost:8080/api/books/simple-search?q=clean%20code"
```

### 2. Búsqueda Avanzada
```bash
curl "http://localhost:8080/api/books/search?searchTerm=programming&genre=Programming&inStock=true"
```

### 3. Calificar un Libro
```bash
curl -X POST http://localhost:8080/api/books/rate \
  -H "Content-Type: application/json" \
  -d '{
    "bookId": 1,
    "userId": "juan123",
    "rating": 5,
    "comment": "Excelente libro"
  }'
```

### 4. Ver Calificaciones de un Libro
```bash
curl "http://localhost:8080/api/books/1/ratings"
```

## 🎯 Características Técnicas Implementadas

### Búsqueda
- **Búsqueda por título y autor**: Query personalizada que busca en ambos campos
- **Filtros adicionales**: Por género y disponibilidad en stock
- **Case-insensitive**: No importa mayúsculas o minúsculas
- **Búsqueda parcial**: Encuentra coincidencias parciales

### Calificaciones
- **Validación de rango**: Solo acepta calificaciones de 1 a 5
- **Actualización automática**: Si un usuario califica el mismo libro dos veces, se actualiza
- **Cálculo de promedio**: Se calcula automáticamente el promedio de calificaciones
- **Contador de calificaciones**: Se mantiene el total de calificaciones por libro

### Arquitectura
- **Patrón MVC**: Separación clara de responsabilidades
- **DTOs**: Transferencia de datos estructurada
- **Transacciones**: Operaciones de base de datos seguras
- **Logging**: Registro detallado de operaciones
- **CORS habilitado**: Para integración con frontend

## 🛠️ Configuración Requerida

### 1. Base de Datos
```properties
# En application.properties
spring.datasource.url=jdbc:mariadb://localhost:3306/libreria_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

### 2. Crear Base de Datos
```sql
CREATE DATABASE libreria_db;
```

### 3. Dependencias Agregadas
```gradle
implementation 'org.springframework.boot:spring-boot-starter-validation'
```

## 🎨 Interfaz Web

Se incluye una interfaz web completa en `http://localhost:8080` que permite:
- ✅ Buscar libros por título o autor
- ✅ Filtrar por género y disponibilidad
- ✅ Calificar libros con comentarios
- ✅ Ver calificaciones existentes
- ✅ Interfaz responsive y moderna

## 🔧 Solución de Problemas

### Si hay errores de compilación:
1. Verifica que MariaDB esté ejecutándose
2. Confirma las credenciales de base de datos
3. Asegúrate de que la base de datos `libreria_db` existe
4. Ejecuta `./gradlew clean build`

### Si la aplicación no inicia:
1. Verifica que el puerto 8080 esté disponible
2. Cambia el puerto en `application.properties` si es necesario
3. Revisa los logs de la aplicación

## 📊 Datos de Prueba

El sistema incluye datos de prueba automáticos:
- **Clean Code** - Robert C. Martin (Programming)
- **The Pragmatic Programmer** - David Thomas (Programming)
- **1984** - George Orwell (Fiction)
- **To Kill a Mockingbird** - Harper Lee (Fiction)
- **Design Patterns** - Gang of Four (Programming)

## ✅ Funcionalidades Cumplidas

### ✅ Búsqueda Básica
- Los usuarios pueden buscar por palabra o frase
- La búsqueda funciona en campos de autor y título
- Búsqueda case-insensitive y parcial

### ✅ Sistema de Calificaciones
- Los usuarios pueden calificar libros de 1 a 5 estrellas
- No es necesario haber comprado el libro
- Se puede actualizar la calificación
- Se calcula automáticamente el promedio

## 🚀 Cómo Ejecutar

1. **Configura la base de datos** (ver sección anterior)
2. **Ejecuta la aplicación**:
   ```bash
   ./gradlew bootRun
   ```
3. **Abre tu navegador** en: `http://localhost:8080`
4. **Prueba las funcionalidades** usando la interfaz web o los endpoints de la API

## 📋 Próximos Pasos Sugeridos

1. **Autenticación de usuarios**: Implementar sistema de login
2. **Gestión de inventario**: CRUD completo para libros
3. **Reportes**: Estadísticas de calificaciones y ventas
4. **Búsqueda avanzada**: Filtros por precio, fecha, etc.
5. **API de paginación**: Para grandes cantidades de datos

¡El sistema está completamente implementado y listo para usar! 🎉
