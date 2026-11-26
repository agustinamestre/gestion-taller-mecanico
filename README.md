# G.M.A. · Gestión y Mantenimiento Automotriz

Sistema web para la gestión integral de talleres mecánicos. Reemplaza el seguimiento "de memoria" y por chats por un único registro del taller: clientes, vehículos, presupuestos, órdenes de trabajo, facturas y alertas de mantenimiento preventivo.

Proyecto final de la Tecnicatura Universitaria en Programación (UTN FRSN).

## Funcionalidades

| Módulo | Qué permite |
|---|---|
| **Clientes y vehículos** | Un cliente con varios vehículos, con historial por cliente y por unidad. Baja y reactivación. Marcas y modelos como catálogo. |
| **Productos** | Repuestos y mano de obra con precio actual. |
| **Presupuestos** | Ítems separados de repuestos y mano de obra. Vencen a los 30 días. |
| **Órdenes de trabajo** | Ciclo de vida controlado: `INGRESADO → EN_REPARACION → FINALIZADO → ENTREGADO` (o `CANCELADO`). Registra fecha de finalización y de entrega. |
| **Facturas** | Seña y factura final por orden de trabajo, con numeración automática. No se entrega una orden con saldo pendiente. |
| **Alertas de service** | Aviso por tiempo (12 meses) o kilometraje (10.000 km), con enlace de WhatsApp con el mensaje armado. |
| **Panel de control** | Resumen general del taller. |
| **Reportes** *(solo administrador)* | Facturación y servicios por período, con gráficos. |
| **Usuarios y perfil** | Alta, baja y reactivación de usuarios (administrador). Cada usuario edita su perfil y contraseña. |

### Roles

- **Empleado:** opera el taller (todo lo anterior, salvo usuarios, reportes y la facturación del panel).
- **Administrador:** todo lo del empleado, más gestión de usuarios y reportes.

Los permisos se controlan en el servidor y también en la interfaz.

## Arquitectura

Aplicación cliente-servidor desacoplada:

```
Frontend (Angular)  ──  API REST + JWT  ──  Backend (Spring Boot)  ──  PostgreSQL
                                                   │
                                                   ├── SMTP (email)
                                                   └── Tareas programadas
```

### Backend: arquitectura limpia

El dominio no depende de Spring ni de JPA.

- **`domain`**: modelos con sus reglas de negocio, validadores, errores de negocio e interfaces de repositorio (puertos).
- **`application`**: casos de uso (interfaz + implementación, sin `@Service`), commands y resultados.
- **`infrastructure`**: todo lo que toca el exterior.
  - `rest`: controladores, DTOs y manejo de errores.
  - `service`: un service `@Transactional` por módulo, que delega en el caso de uso.
  - `persistence`: adapters que implementan los repositorios, entidades JPA y mappers.
  - `security`: Spring Security, JWT y refresh token rotativo.
  - `jobs`: tareas programadas.
  - `config`: creación de los casos de uso como beans.

### Frontend

Angular con componentes standalone y signals, organizado por módulos (`features/`): cada uno tiene su contenedor, sus componentes de listado, detalle y formulario, su service y sus modelos. Incluye guards por rol (`authGuard`, `adminGuard`) y un interceptor que agrega el token a cada pedido y lo renueva ante un 401.

## Stack tecnológico

| Capa | Tecnologías |
|---|---|
| Backend | Java 21, Spring Boot 4, Spring Data JPA, Spring Security, JWT (jjwt), MapStruct, SpringDoc (Swagger), JavaMail, Maven |
| Frontend | Angular 21, PrimeNG 21, Chart.js, RxJS |
| Base de datos | PostgreSQL |

## Puesta en marcha

### Requisitos

- JDK 21
- Node.js (compatible con Angular 21) y npm
- PostgreSQL

### 1. Base de datos

Crear una base vacía llamada `TallerMecanico`. Las tablas se generan solas al iniciar el backend.

### 2. Backend

Crear `backend/src/main/resources/application-dev.properties`. Este archivo no se versiona porque contiene credenciales:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/TallerMecanico
spring.datasource.username=<usuario>
spring.datasource.password=<contraseña>
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

jwt.secret=<clave larga y aleatoria>
jwt.expiration=60000
jwt.refresh.expiration-days=7

spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=<cuenta de correo>
spring.mail.password=<contraseña de aplicación>
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

Iniciar el servidor:

```bash
cd backend
./mvnw spring-boot:run
```

Al arrancar se carga el usuario administrador inicial `admin` (definido en `src/main/resources/db/data.sql`). Conviene cambiar su contraseña al primer ingreso.

| Recurso | URL |
|---|---|
| API | http://localhost:8090/gestion-taller |
| Swagger UI | http://localhost:8090/gestion-taller/swagger-ui.html |
| OpenAPI (JSON) | http://localhost:8090/gestion-taller/api-docs |

### 3. Frontend

```bash
cd frontend
npm install
npm start
```

La aplicación queda en http://localhost:4200. La URL del backend se configura en `frontend/src/environments/environment.ts`.

## Configuración

Valores en `application.properties` que se pueden ajustar:

| Propiedad | Por defecto | Descripción |
|---|---|---|
| `server.port` | `8090` | Puerto del backend |
| `cors.allowed-origins` | `http://localhost:4200` | Origen permitido del frontend |
| `alertas.service.meses` | `12` | Meses para generar la alerta de service |
| `alertas.service.km` | `10000` | Kilómetros para generar la alerta (se genera la que ocurra primero) |

## Tareas programadas

| Tarea | Horario | Qué hace |
|---|---|---|
| Alertas de service | Lunes 4:00 | Genera alertas por tiempo o kilometraje y avisa por email a los clientes que tienen correo. |
| Vencimiento de presupuestos | Todos los días 2:00 | Pasa a `VENCIDO` los presupuestos de más de 30 días. |
| Depuración de presupuestos | Todos los días 3:00 | Elimina los presupuestos vencidos o rechazados de más de 180 días. |

## Tests

```bash
# Backend
cd backend
./mvnw test

# Frontend
cd frontend
npm test
```

## Estructura del repositorio

```
backend/    API REST (Spring Boot)
  bruno/    colección de requests de la API
  scripts/  datos de demostración
frontend/   aplicación web (Angular)
docs/       documentación
```
