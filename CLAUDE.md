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
  cualquier `NotFoundException` termina en un 500.
- Cada servicio implementa su interfaz en `service/interfaces/I*Service.java` e inyecta los
  repositorios por constructor (sin `@Autowired`).

## Estado actual

- `VentaService.createVenta` está **a medio escribir y no compila** (`.sucursal()` y `.detalle()`
  llamados sin argumentos en el builder). Es el punto donde quedó el trabajo.
- Falta la capa `controller` completa; los endpoints planificados están en `README.md`.
- `DetalleVenta` no tiene `@Builder` (las demás entidades sí) ni repositorio propio: se persiste
  vía la relación con `Venta`, cuyo `@OneToMany` hoy no tiene `cascade`.
- `README.md` y `BITACORA.md` describen MySQL, pero `application.properties` apunta a **H2 en
  memoria** (`jdbc:h2:mem:superdb;MODE=MySQL`). El driver de MySQL sigue en el `pom.xml`.

## Convenciones del repo

- `BITACORA.md` se actualiza al final de cada jornada con una entrada por día que resume los
  commits de ese día (hay una plantilla comentada al final del archivo). Mantener el formato.
- Al completar una etapa, actualizar también el checklist de "Estado del desarrollo" en `README.md`.
- Mensajes de commit cortos en inglés (ver `git log`).
