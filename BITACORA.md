# Bitácora del proyecto — MarketDemo

Registro diario de avances. Cada entrada se arma a partir de los commits del día.

## Sobre el proyecto

- **Stack:** Java 17 · Spring Boot 4.1.1 · Spring Data JPA · Spring WebMVC · MySQL · Lombok · Maven
- **Paquete base:** `com.example.MarketDemo`
- **Objetivo:** sistema de gestión para un market (productos, sucursales y ventas).

## Estado actual

- Proyecto Spring Boot inicializado con el wrapper de Maven.
- Capa de modelo (`model/`) con 4 entidades JPA y sus relaciones.
- Pendiente: repositorios, servicios, controladores REST y configuración de la conexión a MySQL
  (`application.properties` solo define `spring.application.name`).

### Modelo de datos

| Entidad | Campos | Relaciones |
| --- | --- | --- |
| `Producto` | `id`, `nombre`, `categoria`, `precio`, `cantidad` | — |
| `Sucursal` | `id`, `nombre`, `direccion` | — |
| `Venta` | `id`, `fecha`, `estado` | `@ManyToOne` → `Sucursal` |
| `DetalleVenta` | `id`, `cantProd`, `precio` | `@ManyToOne` → `Venta`, `@ManyToOne` → `Producto` |

Todas usan `@Id` con `GenerationType.IDENTITY` y Lombok (`@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`).

---

## Registro diario

### 2026-08-25

**Commits:** `c8f099b`, `bdad579`, `6032d66`

- **`c8f099b` — Initial commit:** estructura base del proyecto Spring Boot generada con Spring Initializr
  (pom.xml con JPA, WebMVC, MySQL connector y Lombok; wrapper de Maven; clase `MarketDemoApplication`).
- **`bdad579` — Entities created:** se crean las clases del paquete `model` y se define la entidad `Sucursal`
  (`id`, `nombre`, `direccion`) con anotaciones JPA y Lombok.
- **`6032d66` — Entities and rela created:** se completan `Producto`, `Venta` y `DetalleVenta`, y se establecen
  las relaciones `Venta → Sucursal`, `DetalleVenta → Venta` y `DetalleVenta → Producto` con `@ManyToOne`.

**Próximos pasos:** configurar la conexión a MySQL y crear los repositorios JPA.

<!--
Plantilla para nuevas entradas:

### AAAA-MM-DD

**Commits:** `hash`, `hash`

- **`hash` — mensaje del commit:** qué se hizo y por qué.

**Próximos pasos:** ...
-->
