# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Contexto

API REST de un supermercado (productos, sucursales, ventas) en Java 17 + Spring Boot 4.1.1.
Es la resolución de una prueba técnica (enunciado y estado del scope en `README.md`); el avance
diario se registra en `BITACORA.md`.

El código y la documentación están en **español**: identificadores, comentarios, mensajes de
excepción y docs. Mantener esa convención al escribir código nuevo.

## Comandos

```bash
./mvnw spring-boot:run            # levantar la API en http://localhost:8080
./mvnw clean package              # compilar y empaquetar
./mvnw test                       # todos los tests
./mvnw test -Dtest=NombreTest     # un test suelto
./mvnw test -Dtest=NombreTest#metodo
./mvnw -q compile                 # chequeo rápido de compilación
```

El único test es `MarketDemoApplicationTests.contextLoads` (`@SpringBootTest`), así que `./mvnw test`
levanta el contexto completo de Spring y falla mientras el `main` no compile: para iterar sobre código
a medio escribir conviene `./mvnw -q compile`.

**Spring Boot 4 renombró los starters**; al agregar dependencias usar los nombres que ya están en el
`pom.xml`: `spring-boot-starter-webmvc` (no `-web`) y, para tests, `spring-boot-starter-webmvc-test` /
`spring-boot-starter-data-jpa-test` (no `spring-boot-starter-test`). El `maven-compiler-plugin` declara
Lombok en `annotationProcessorPaths` a mano en las dos ejecuciones (`default-compile` y
`default-testCompile`); si se agrega otro procesador de anotaciones hay que sumarlo en ambas.

## Arquitectura

Capas en `com.example.MarketDemo`: `model` (entidades JPA) → `repository` (Spring Data) →
`service` (lógica de negocio, DTO in / DTO out) → `controller` (REST, aún no existe), con
`dto`, `mapper` y `exception` transversales.

Decisiones que atraviesan varios archivos:

- **Los servicios nunca exponen entidades.** Reciben y devuelven DTOs; construyen la entidad
  con el builder de Lombok y devuelven `Mapper.toDTO(...)` del resultado de `save`.
- **`Mapper`** (`mapper/Mapper.java`) es una clase de métodos `static` sobrecargados (`toDTO`),
  no un bean de Spring; no se inyecta, se llama directo. Solo mapea entidad → DTO; el sentido
  inverso lo hace cada servicio a mano con el builder.
- **DTOs inmutables:** campos `final`, `@Getter` + `@Builder` (sin `@Setter`). Por eso las
  actualizaciones cargan la entidad, le aplican los setters y la vuelven a guardar.
- **`Venta.total` está persistido**, pero `Mapper.toDTO(Venta)` lo **recalcula** desde el detalle
  (`Σ precio × cantProd`) e ignora el valor de la entidad. Si se cambia esa regla hay que
  tocar los dos lados.
- **`DetalleVenta` es la tabla intermedia** entre `Venta` y `Producto` y guarda `cantProd` y el
  `precio` del momento de la venta. Ojo con los nombres: la entidad usa `cantProd`, el DTO usa
  `cantidad` y expone `productName` en lugar del `Producto` completo, más un `subtotal` calculado.
- **Errores de negocio:** `NotFoundException` (`RuntimeException`) lanzada desde los servicios
  con `orElseThrow` / `existsById`. Todavía **no hay `@RestControllerAdvice`**, así que hoy
  cualquier `NotFoundException` termina en un 500. Las validaciones de entrada de
  `VentaService.createVenta` tiran `RuntimeException` pelada, no una excepción propia.
- Cada servicio implementa su interfaz en `service/interfaces/I*Service.java` e inyecta los
  repositorios por constructor (sin `@Autowired`).
- Las entidades nombran la tabla con `@Entity(name = "producto")` en minúscula, sin `@Table`. Ojo:
  eso fija el nombre de la *entidad*, no el de la tabla; la estrategia de nombres de Hibernate
  igual crea `detalle_venta` con columnas `cant_prod`, `venta_id`, `producto_id` (importa al
  escribir SQL nativo).
- **El detalle de una venta identifica al producto por nombre, no por id**, porque `DetalleVentaDTO`
  expone `productName`. De ahí `ProductoRepository.findByNombre`. Si el DTO llegara a exponer un id
  de producto, esa query sobra.
- **`Mapper.toDTO(Venta)` recorre la colección lazy `detalle`.** Dentro de un request HTTP funciona
  por el *open-in-view* que Spring Boot trae activado por defecto, pero fuera de una transacción
  (un test, un `@Scheduled`) tira `LazyInitializationException`. `createVenta` no sufre eso porque
  es `@Transactional`; `allVentas` sí depende del open-in-view.

## Estado actual

- La capa `service` está completa (`createVenta` incluido). Falta la capa `controller`; los endpoints
  planificados están en `README.md`.
- `DetalleVenta` **no tiene repositorio propio**: se persiste vía la relación con `Venta`, que lleva
  `@OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)`. Por eso
  `createVenta` engancha los dos lados (`detalle.venta = venta` y `venta.getDetalle().add(detalle)`)
  antes de un único `ventaRepo.save(venta)`.
- `README.md` y `BITACORA.md` describen MySQL, pero `application.properties` apunta a **H2 en
  memoria** (`jdbc:h2:mem:superdb;MODE=MySQL`, `ddl-auto=update`). El driver de MySQL sigue en el
  `pom.xml`. La consola web de H2 no está habilitada.
- `SucursalDTO` escribe su constructor a mano en vez de usar `@AllArgsConstructor` como los otros DTOs.
- El JDK instalado es 25, pero el `pom.xml` compila con `java.version=17`: no usar API posterior a 17.

## Convenciones del repo

- `BITACORA.md` se actualiza al final de cada jornada con una entrada por día que resume los
  commits de ese día (hay una plantilla comentada al final del archivo). Mantener el formato.
- Al completar una etapa, actualizar también el checklist de "Estado del desarrollo" en `README.md`.
- Mensajes de commit cortos en inglés (ver `git log`).
