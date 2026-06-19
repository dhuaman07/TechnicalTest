# Backend Challenge — API de Productos

API REST reactiva para la gestión de productos, construida con **Spring Boot 3 + WebFlux**, **Java 21** y **SQL Server** vía R2DBC.

## Arquitectura

```
presentation/    → Controllers, DTOs, manejo global de excepciones
application/     → Command/Query handlers, validadores, buses
domain/          → Modelo de dominio, repositorio (interfaz), excepciones
infrastructure/  → Adaptador R2DBC, entidad persistencia
```

---

## Requisitos previos

| Herramienta | Versión mínima |
|-------------|---------------|
| Docker      | 20.x          |
| Docker Compose | v2.x       |
| Java (solo para ejecución local) | 21 |
| Maven (solo para ejecución local) | 3.9.x |

---

## Ejecución con Docker

### 1. Levantar todos los servicios

```bash
docker compose up --build
```

Esto levanta tres servicios en orden:
1. **sqlserver** — SQL Server 2022 Express en el puerto `1433`
2. **db-init** — crea la base de datos `TestDB` si no existe
3. **app** — construye y ejecuta la API (puerto `8081` → contenedor `8080`)

La API queda disponible en: **`http://localhost:8081`**

### 2. Solo reconstruir la aplicación (sin tocar la BD)

```bash
docker compose up --build app
```

### 3. Detener y eliminar contenedores

```bash
docker compose down
```

### 4. Detener y eliminar contenedores + volúmenes

```bash
docker compose down -v
```

### Variables de entorno configuradas en docker-compose

| Variable | Valor |
|----------|-------|
| `SPRING_R2DBC_URL` | `r2dbc:mssql://sqlserver:1433/TestDB?trustServerCertificate=true` |
| `SPRING_R2DBC_USERNAME` | `sa` |
| `SPRING_R2DBC_PASSWORD` | `Tawa832*` |

---

## Ejecución local (sin Docker)

Requiere una instancia de SQL Server accesible. Ajustar `src/main/resources/application.yml` con los datos de conexión y luego:

```bash
./mvnw spring-boot:run
```

La API queda disponible en: **`http://localhost:8080`**

---

## Tests y cobertura de código

### Ejecutar todos los tests

```bash
./mvnw test
```

### Ejecutar tests + generar reporte de cobertura JaCoCo

```bash
./mvnw verify
```

El reporte HTML se genera en:

```
target/site/jacoco/index.html
```

Abrirlo en el navegador:

```bash
open target/site/jacoco/index.html        # macOS
xdg-open target/site/jacoco/index.html   # Linux
```

### Umbral de cobertura configurado

JaCoCo verifica que el paquete `com.dhuaman.application` tenga al menos **70 % de cobertura por instrucción**. El build falla si no se alcanza ese umbral.

### Suites de tests incluidas

| Test | Descripción |
|------|-------------|
| `CreateProductoCommandHandlerTest` | Creación exitosa y conflicto por nombre duplicado |
| `CreateProductoCommandValidatorTest` | Validaciones de entrada al crear |
| `UpdateProductoCommandHandlerTest` | Actualización exitosa y producto no encontrado |
| `UpdateProductoCommandValidatorTest` | Validaciones de entrada al actualizar |
| `DeleteProductoCommandHandlerTest` | Eliminación exitosa y producto no encontrado |
| `DeleteProductoCommandValidatorTest` | Validación de ID al eliminar |
| `GetAllProductosQueryHandlerTest` | Listado de productos |
| `GetProductoByIdQueryHandlerTest` | Búsqueda por ID y not found |
| `ProductoControllerTest` | Tests de integración de los endpoints HTTP |

---

## Endpoints

**Base URL:** `http://localhost:8081/api/v1/productos` (Docker) o `http://localhost:8080/api/v1/productos` (local)

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/v1/productos` | Listar todos los productos |
| `GET` | `/api/v1/productos/{id}` | Obtener producto por ID |
| `POST` | `/api/v1/productos` | Crear producto |
| `PUT` | `/api/v1/productos/{id}` | Actualizar producto |
| `DELETE` | `/api/v1/productos/{id}` | Eliminar producto |

### Formato de respuesta

Todas las respuestas siguen la estructura:

```json
{
  "isSuccess": true,
  "message": "Mensaje descriptivo",
  "data": { },
  "errors": []
}
```

---

## Ejemplos de consumo con cURL

> Los ejemplos usan el puerto `8081` (Docker). Cambiar por `8080` para ejecución local.

### Crear producto

```bash
curl -X POST http://localhost:8081/api/v1/productos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Laptop Pro 15",
    "descripcion": "Laptop de alta gama con procesador Intel i9",
    "precio": 2999.99,
    "stock": 10
  }'
```

**Respuesta exitosa (201 Created):**

```json
{
  "isSuccess": true,
  "message": "Producto creado exitosamente",
  "data": {
    "id": 1,
    "nombre": "Laptop Pro 15",
    "descripcion": "Laptop de alta gama con procesador Intel i9",
    "precio": 2999.99,
    "stock": 10
  },
  "errors": []
}
```

---

### Listar todos los productos

```bash
curl http://localhost:8081/api/v1/productos
```

**Respuesta exitosa (200 OK):**

```json
{
  "isSuccess": true,
  "message": "Productos obtenidos exitosamente",
  "data": [
    {
      "id": 1,
      "nombre": "Laptop Pro 15",
      "descripcion": "Laptop de alta gama con procesador Intel i9",
      "precio": 2999.99,
      "stock": 10
    }
  ],
  "errors": []
}
```

---

### Obtener producto por ID

```bash
curl http://localhost:8081/api/v1/productos/1
```

**Respuesta exitosa (200 OK):**

```json
{
  "isSuccess": true,
  "message": "Producto obtenido exitosamente",
  "data": {
    "id": 1,
    "nombre": "Laptop Pro 15",
    "descripcion": "Laptop de alta gama con procesador Intel i9",
    "precio": 2999.99,
    "stock": 10
  },
  "errors": []
}
```

**Respuesta cuando no existe (404 Not Found):**

```json
{
  "isSuccess": false,
  "message": "Recurso no encontrado",
  "data": null,
  "errors": ["Producto con id 99 no encontrado"]
}
```

---

### Actualizar producto

```bash
curl -X PUT http://localhost:8081/api/v1/productos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Laptop Pro 15 v2",
    "descripcion": "Versión actualizada con 32GB RAM",
    "precio": 3299.99,
    "stock": 5
  }'
```

**Respuesta exitosa (200 OK):**

```json
{
  "isSuccess": true,
  "message": "Producto actualizado exitosamente",
  "data": {
    "id": 1,
    "nombre": "Laptop Pro 15 v2",
    "descripcion": "Versión actualizada con 32GB RAM",
    "precio": 3299.99,
    "stock": 5
  },
  "errors": []
}
```

---

### Eliminar producto

```bash
curl -X DELETE http://localhost:8081/api/v1/productos/1
```

**Respuesta exitosa (200 OK):**

```json
{
  "isSuccess": true,
  "message": "Producto eliminado exitosamente",
  "data": null,
  "errors": []
}
```

---

## Validaciones y errores

### Error de validación (400 Bad Request)

Al crear o actualizar sin los campos requeridos:

```bash
curl -X POST http://localhost:8081/api/v1/productos \
  -H "Content-Type: application/json" \
  -d '{"descripcion": "Sin nombre ni precio"}'
```

```json
{
  "isSuccess": false,
  "message": "Error de validación",
  "data": null,
  "errors": ["El nombre del producto es obligatorio"]
}
```

### Conflicto de nombre duplicado (409 Conflict)

```json
{
  "isSuccess": false,
  "message": "Conflicto de datos",
  "data": null,
  "errors": ["Ya existe un producto con el nombre 'Laptop Pro 15'"]
}
```

### Reglas de validación

| Campo | Regla |
|-------|-------|
| `nombre` | Obligatorio, no puede estar vacío, debe ser único |
| `precio` | Obligatorio, debe ser mayor a `0` |
| `stock` | Obligatorio, no puede ser negativo |
| `descripcion` | Opcional |
