# Bitácora del proyecto — MarketDemo

Registro diario de avances. Cada entrada se arma a partir de los commits del día.

## Sobre el proyecto

- **Stack:** Java 17 · Spring Boot 4.1.1 · Spring Data JPA · Spring WebMVC · H2 en memoria · Lombok · Maven
  (el connector de MySQL sigue en el `pom.xml` para migrar más adelante)
- **Paquete base:** `com.example.MarketDemo`
- **Objetivo:** sistema de gestión para un market (productos, sucursales y ventas).

## Estado actual

- Capa de modelo (`model/`) con 4 entidades JPA y sus relaciones.
- Capa de DTOs (`dto/`) completa: un DTO por entidad, todos inmutables (campos `final` + `@Getter`).
- `Mapper` con los `toDTO` de las cuatro entidades y capa `repository` con los tres `JpaRepository`.
- Capa `service` completa: CRUD de productos, sucursales y ventas, `createVenta` incluido.
- Base H2 en memoria configurada (`jdbc:h2:mem:superdb;MODE=MySQL`); la migración a MySQL queda pendiente.
- Pendiente: la capa `controller` con los endpoints REST y un `@RestControllerAdvice` que traduzca
  `NotFoundException` a un 404 (hoy cualquier error de negocio termina en 500).

### Modelo de datos

| Entidad | Campos | Relaciones |
| --- | --- | --- |
| `Producto` | `id`, `nombre`, `categoria`, `precio`, `cantidad` | — |
| `Sucursal` | `id`, `nombre`, `direccion` | — |
| `Venta` | `id`, `fecha`, `estado`, `total` | `@ManyToOne` → `Sucursal`, `@OneToMany` → `DetalleVenta` (con `cascade`) |
| `DetalleVenta` | `id`, `cantProd`, `precio` | `@ManyToOne` → `Venta`, `@ManyToOne` → `Producto` |

Todas usan `@Id` con `GenerationType.IDENTITY` y Lombok (`@Getter`, `@Setter`, `@NoArgsConstructor`,
`@AllArgsConstructor`, `@Builder`). `Producto.nombre` es único: es la clave con la que el detalle de una
venta identifica al producto.

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

### 2026-08-28

**Commits:** `65fde1e`, `d8685cf`

- **`65fde1e` — Repos created:** se crea la capa `repository` con las tres interfaces extendiendo
  `JpaRepository<T, Long>`, todavía sin métodos propios: Spring Data ya resuelve el CRUD básico.
- **`d8685cf` — Entities relation updated:** se completa el lado inverso de la relación en `Venta` con
  `@OneToMany(mappedBy = "venta")` sobre `List<DetalleVenta>`, inicializada en `ArrayList` para no
  tener que manejar la lista en `null`.

**Próximos pasos:** configurar la base de datos y arrancar la capa de servicios.

### 2026-08-29

**Commits:** `7028315`, `4f46492`, `b80a0e5`, `79ffa27`, `c0e06e5`, `5185460`, `deec89f`

- **`7028315` — H2 Database config:** se suma la dependencia de H2 y se configura una base en memoria
  (`jdbc:h2:mem:superdb;MODE=MySQL`) para poder levantar la app sin depender de un MySQL instalado.
  El connector de MySQL queda en el `pom.xml` para la migración posterior.
- **`4f46492` — Init Services classes and interfaces:** esqueleto de la capa `service`: las tres clases
  `@Service` y sus interfaces en `service/interfaces`.
- **`b80a0e5` — Services Interfaces ready:** se definen los contratos CRUD, siempre con DTOs de entrada
  y de salida, para que la capa de servicios nunca exponga entidades JPA.
- **`79ffa27` — @Builder Annotation added to the DTO'S:** los DTOs suman `@Builder`, necesario para
  poder construirlos desde el mapper teniendo los campos `final`.
- **`c0e06e5` — Sucursal and Producto to DTO Mapper, done!:** primeros métodos `toDTO` de `Mapper`,
  estáticos y sobrecargados, para los dos casos simples.
- **`5185460` — @Repository Annotation added:** se anotan los tres repositorios.
- **`deec89f` — Working on the services...:** primeras implementaciones de los servicios, con los
  repositorios inyectados por constructor.

**Próximos pasos:** cerrar el mapper de `Venta` y terminar los servicios.

### 2026-08-30

**Commits:** `766ebac`, `7d8b477`, `9d6624c`, `cb2d0fa`, `518e1a0`, `4a8ee0b`, `2cdc619`

- **`766ebac` — Working on the mapper** y **`7d8b477` — Mappers done!:** se completa `Mapper.toDTO(Venta)`,
  el caso complejo: arma la lista de `DetalleVentaDTO` con el nombre del producto y el `subtotal`, y
  calcula el `total` sumando los subtotales en vez de leer el campo persistido de la entidad.
- **`9d6624c` — @Builder Annotations added.:** `@Builder` en `Producto`, `Sucursal` y `Venta`, para que
  los servicios construyan las entidades desde el DTO sin pasar por los setters.
- **`cb2d0fa` — Still working on services:** avance sobre los tres servicios.
- **`518e1a0` — Exception added:** se crea `NotFoundException` (extiende `RuntimeException`) para los
  errores de negocio, en lugar de devolver `null` cuando no se encuentra un registro.
- **`4a8ee0b` — Product Service done!:** `ProductoService` cerrado, con `orElseThrow` y `existsById`
  para los casos de no encontrado.
- **`2cdc619` — Product and Venta Service done!:** pese al mensaje, lo que cierra es `SucursalService`;
  `VentaService` queda con el CRUD armado salvo `createVenta`, que queda a medio escribir y sin compilar.
  Se suma además `CLAUDE.md` con las guías del repo.

**Próximos pasos:** terminar `createVenta` y empezar la capa `controller`.

### 2026-08-31

**Commits:** `710ea77`, `f502c16`, `207ed33`, `48ee10a`, `e166b8c`, `a8a42f0`

- **`710ea77` — @Builder annotation added to DetalleVenta:** completa el patrón Builder en la última
  entidad que faltaba, para poder armar las líneas de una venta como el resto del modelo.
- **`f502c16` — Cascade on Venta detalle and findByNombre:** dos piezas que `createVenta` necesitaba.
  El `cascade = CascadeType.ALL, orphanRemoval = true` en `Venta.detalle` es imprescindible porque
  `DetalleVenta` no tiene repositorio propio: sin eso, guardar una venta no persistía ninguna línea.
  `findByNombre` hace falta porque `DetalleVentaDTO` identifica al producto por nombre y no por id.
- **`207ed33` — createVenta done!:** valida la sucursal y cada producto del detalle, arma las líneas
  enganchando los dos lados de la relación y guarda todo con un único `save`. El total se calcula en el
  servidor y se ignora el que manda el cliente, para que coincida con el que recalcula `Mapper.toDTO`.
  El precio de cada línea es el del momento de la venta; si no viene, se toma el actual del producto.
- **`48ee10a` — Docs: CLAUDE.md rewrite and README checklist:** `CLAUDE.md` al día con el estado real del
  código y con las trampas del proyecto (starters renombrados de Spring Boot 4, nombres de tabla en
  snake_case, el detalle lazy que se lee gracias al open-in-view). Se marcan DTOs/Mapper, `repository` y
  `service` en el checklist del README.
- **`e166b8c` — Fix: updateVenta no longer overwrites total:** el update persistía el total que mandaba
  el cliente, pero `Mapper.toDTO` lo recalcula desde el detalle y lo ignoraba: la BD quedaba con un valor
  y la API devolvía otro. Como el update no toca las líneas, el total directamente ya no se toca.
- **`a8a42f0` — Fix: unique product name:** nada impedía crear dos productos con el mismo nombre, y ahí
  `findByNombre` cortaba con `IncorrectResultSizeDataAccessException` y `createVenta` respondía un 500.
  Se agrega el unique en `Producto.nombre` y se valida en `ProductoService` (alta y renombrado) para que
  el duplicado sea un error de negocio y no una violación de integridad.

**Próximos pasos:** la capa `controller` con los endpoints REST y un `@RestControllerAdvice` que traduzca
`NotFoundException` a 404. Queda pendiente decidir si el `PUT /api/ventas/{id}` debe poder modificar el
detalle: hoy acepta el campo y lo ignora en silencio. También revisar el ejemplo de `POST /api/ventas` del
README, que usa `nombreProd`/`cantProd` mientras que `DetalleVentaDTO` expone `productName`/`cantidad`.

<!--
Plantilla para nuevas entradas:

### AAAA-MM-DD

**Commits:** `hash`, `hash`

- **`hash` — mensaje del commit:** qué se hizo y por qué.

**Próximos pasos:** ...
-->
