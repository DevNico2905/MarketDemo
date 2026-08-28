# Bitácora del proyecto — MarketDemo

Registro diario de avances. Cada entrada se arma a partir de los commits del día.

## Sobre el proyecto

- **Stack:** Java 17 · Spring Boot 4.1.1 · Spring Data JPA · Spring WebMVC · MySQL · Lombok · Maven
- **Paquete base:** `com.example.MarketDemo`
- **Objetivo:** sistema de gestión para un market (productos, sucursales y ventas).

## Estado actual

- Proyecto Spring Boot inicializado con el wrapper de Maven.
- Capa de modelo (`model/`) con 4 entidades JPA y sus relaciones.
- Capa de DTOs (`dto/`) completa: un DTO por entidad, todos inmutables (campos `final` + `@Getter`).
- Pendiente: repositorios, servicios, controladores REST, mappers entidad↔DTO y configuración de la
  conexión a MySQL (`application.properties` solo define `spring.application.name`).

### Modelo de datos

| Entidad | Campos | Relaciones |
| --- | --- | --- |
| `Producto` | `id`, `nombre`, `categoria`, `precio`, `cantidad` | — |
| `Sucursal` | `id`, `nombre`, `direccion` | — |
| `Venta` | `id`, `fecha`, `estado`, `total` | `@ManyToOne` → `Sucursal` |
| `DetalleVenta` | `id`, `cantProd`, `precio` | `@ManyToOne` → `Venta`, `@ManyToOne` → `Producto` |

Todas usan `@Id` con `GenerationType.IDENTITY` y Lombok (`@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`).

### DTOs

| DTO | Campos | Notas |
| --- | --- | --- |
| `ProductoDTO` | `id`, `nombre`, `categoria`, `precio`, `cantidad` | espejo plano de la entidad |
| `SucursalDTO` | `id`, `nombre`, `direccion` | constructor escrito a mano (sin `@AllArgsConstructor`) |
| `VentaDTO` | `id`, `fecha`, `estado`, `total`, `idSucursal`, `detalle` | aplana la relación a `idSucursal` y anida `List<DetalleVentaDTO>` |
| `DetalleVentaDTO` | `id`, `productName`, `cantidad`, `precio`, `subtotal` | trae el nombre del producto y agrega `subtotal` (campo calculado) |

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

### 2026-08-26

**Commits:** `aa36ae3`, `3031a20`

- **`aa36ae3` — Add README, progress log and MCP config:** se documenta el proyecto (README con stack, modelo
  de datos y guía de arranque), se crea esta bitácora y se suma `.mcp.json` con la configuración de MCP.
- **`3031a20` — Ignore macOS .DS_Store files:** se agrega la regla al `.gitignore` para que los archivos de
  metadatos de macOS no ensucien el repo.

**Próximos pasos:** empezar la capa de transporte (DTOs) antes de repositorios y servicios.

### 2026-08-27

**Commits:** `930c293`, `fa78eb5`, `9f0416b`, `1576a36`

- **`930c293` — Total atribute added:** se agrega el campo `total` (`Double`) a la entidad `Venta` para guardar
  el importe de la venta sin tener que recalcularlo desde los detalles en cada consulta.
- **`fa78eb5` — ProductoDTO and SucursalDTO done!:** primeros DTOs del paquete `dto`. `ProductoDTO` usa
  `@AllArgsConstructor` de Lombok; `SucursalDTO` define el constructor a mano. Ambos con campos `final`
  y solo `@Getter`, para que sean objetos de solo lectura.
- **`9f0416b` — VentaDTO done, mix of differents entities:** DTO compuesto que mezcla datos de varias entidades:
  aplana la relación con `Sucursal` a un `idSucursal` y anida la lista de detalles como `List<DetalleVentaDTO>`,
  evitando exponer las entidades JPA y sus ciclos de referencias.
- **`1576a36` — DetalleVentaDTO done:** cierra la capa de DTOs. En lugar del `Producto` completo expone
  `productName`, y suma `subtotal` como campo calculado (cantidad × precio).

**Próximos pasos:** mappers entidad↔DTO, repositorios JPA y la configuración de la conexión a MySQL.

<!--
Plantilla para nuevas entradas:

### AAAA-MM-DD

**Commits:** `hash`, `hash`

- **`hash` — mensaje del commit:** qué se hizo y por qué.

**Próximos pasos:** ...
-->
