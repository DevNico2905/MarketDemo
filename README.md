# MarketDemo

API REST para la gestión de un supermercado (productos, sucursales y ventas), construida con **Java 17 + Spring Boot**.

Este proyecto es mi resolución de la prueba técnica que se plantea en el video
[**RESUELVO una PRUEBA TÉCNICA de JAVA SPRING BOOT de una empresa REAL en 3 horas**](https://www.youtube.com/watch?v=l-Bl45I6UEY)
del canal [TodoCode](https://youtube.com/TodoCode) — una prueba real para un puesto de *Java Dev Junior*
en una empresa de España. La implementación es propia; el video se usa como enunciado y guía.

> El seguimiento diario del desarrollo está en [`BITACORA.md`](BITACORA.md).

---

## El enunciado

Se pide desarrollar el back-end de un sistema de ventas para una cadena de supermercados que permita:

- Administrar el **catálogo de productos** (alta, consulta, modificación y baja).
- Administrar las **sucursales** de la cadena.
- **Registrar ventas**, indicando la sucursal donde se realizan y el detalle de productos vendidos
  (producto, cantidad y precio), calculando el **total** de la operación.
- Exponer todo mediante **endpoints REST**, devolviendo **DTOs** en lugar de las entidades del dominio.
- Persistir la información en una base de datos **relacional (MySQL)** mediante **JPA/Hibernate**.
- Manejar los errores de negocio (por ejemplo, sucursal o producto inexistente) con **excepciones propias**.

### Requisitos técnicos

| Requisito | Detalle |
| --- | --- |
| Lenguaje | Java 17 |
| Framework | Spring Boot (Spring Web MVC + Spring Data JPA) |
| Base de datos | MySQL |
| Arquitectura | En capas: `model` → `repository` → `service` → `controller`, con `dto` y `mapper` |
| Utilidades | Lombok (getters/setters, constructores, patrón Builder) |
| Build | Maven (con wrapper `mvnw`) |
| Pruebas | Colección de Postman contra los endpoints |

---

## Modelo de dominio

```
Sucursal 1 ──── * Venta 1 ──── * DetalleVenta * ──── 1 Producto
```

| Entidad | Campos | Relaciones |
| --- | --- | --- |
| `Producto` | `id`, `nombre`, `categoria`, `precio`, `cantidad` | — |
| `Sucursal` | `id`, `nombre`, `direccion` | — |
| `Venta` | `id`, `fecha`, `estado`, `total` | `@ManyToOne` → `Sucursal`, `@OneToMany` → `DetalleVenta` |
| `DetalleVenta` | `id`, `cantProd`, `precio` | `@ManyToOne` → `Venta`, `@ManyToOne` → `Producto` |

`DetalleVenta` es la tabla intermedia que resuelve el vínculo *muchos a muchos* entre `Venta` y `Producto`,
agregando los datos propios de la línea de venta (cantidad y precio al momento de la operación).

---

## API planificada

Base URL: `http://localhost:8080`

### Productos — `/api/productos`

| Método | Ruta | Descripción |
| --- | --- | --- |
| `GET` | `/api/productos` | Lista todos los productos |
| `POST` | `/api/productos` | Crea un producto |
| `PUT` | `/api/productos/{id}` | Actualiza un producto |
| `DELETE` | `/api/productos/{id}` | Elimina un producto |

```json
POST /api/productos
{
  "nombre": "Naranjas",
  "categoria": "Frutas y Verduras",
  "precio": 1500.0,
  "cantidad": 80
}
```

### Sucursales — `/api/sucursales`

| Método | Ruta | Descripción |
| --- | --- | --- |
| `GET` | `/api/sucursales` | Lista todas las sucursales |
| `POST` | `/api/sucursales` | Crea una sucursal |
| `PUT` | `/api/sucursales/{id}` | Actualiza una sucursal |
| `DELETE` | `/api/sucursales/{id}` | Elimina una sucursal |

```json
POST /api/sucursales
{
  "nombre": "Sucursal Centro",
  "direccion": "Calle 45 #12-34"
}
```

### Ventas — `/api/ventas`

| Método | Ruta | Descripción |
| --- | --- | --- |
| `GET` | `/api/ventas` | Lista todas las ventas con su detalle |
| `POST` | `/api/ventas` | Registra una venta con su detalle de productos |
| `PUT` | `/api/ventas/{id}` | Actualiza los datos de una venta |
| `DELETE` | `/api/ventas/{id}` | Elimina una venta |

```json
POST /api/ventas
{
  "fecha": "2026-08-25",
  "estado": "REGISTRADA",
  "idSucursal": 1,
  "detalle": [
    { "nombreProd": "Coca Cola 1.5L", "cantProd": 2, "precio": 1500.0 },
    { "nombreProd": "Naranjas",       "cantProd": 3, "precio": 1500.0 }
  ]
}
```

Al crear una venta se valida que exista la sucursal, que el detalle no venga vacío y que cada producto
del detalle exista; el **total** se calcula como la suma de `precio × cantProd` de cada línea.

---

## Estructura del proyecto

```
src/main/java/com/example/MarketDemo/
├── MarketDemoApplication.java
├── model/            # Entidades JPA          ✅
│   ├── Producto.java
│   ├── Sucursal.java
│   ├── Venta.java
│   └── DetalleVenta.java
├── dto/              # Objetos de transferencia   ⏳
├── mapper/           # Entidad ⇄ DTO              ⏳
├── repository/       # Spring Data JPA            ⏳
├── service/          # Lógica de negocio          ⏳
├── controller/       # Endpoints REST             ⏳
└── exception/        # Excepciones propias        ⏳
```

## Estado del desarrollo

- [x] Proyecto base generado con Spring Initializr
- [x] Entidades JPA y relaciones (`Producto`, `Sucursal`, `Venta`, `DetalleVenta`)
- [x] DTOs y clase `Mapper` (patrón Builder)
- [x] Capa `repository`
- [ ] Conexión a MySQL en `application.properties`
- [x] Capa `service` con la lógica de negocio
- [ ] Manejo de excepciones
- [ ] Capa `controller` con los endpoints REST
- [ ] Pruebas con Postman

---

## Cómo ejecutarlo

### Requisitos previos

- JDK 17 o superior
- MySQL en ejecución
- Maven (o el wrapper `./mvnw` incluido)

### 1. Crear la base de datos

```sql
CREATE DATABASE marketdemo;
```

### 2. Configurar `src/main/resources/application.properties`

```properties
spring.application.name=MarketDemo

spring.datasource.url=jdbc:mysql://localhost:3306/marketdemo
spring.datasource.username=root
spring.datasource.password=tu_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3. Levantar la aplicación

```bash
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

### Otros comandos útiles

```bash
./mvnw clean package   # compilar y empaquetar
./mvnw test            # ejecutar los tests
```

---

## Referencias

- 🎥 Video de la prueba técnica: <https://www.youtube.com/watch?v=l-Bl45I6UEY>
- 🎥 Parte 2: <https://youtu.be/aaTWiVD8mro>
- 💻 Repositorio de referencia de TodoCode: <https://github.com/todocodeacademy/pruebaTecnicaSupermercado>
- 📚 Documentación de Spring Boot: <https://docs.spring.io/spring-boot/index.html>
