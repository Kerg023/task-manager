# Task Manager

Aplicación CRUD para la gestión de tareas, desarrollada como prueba técnica de admisión para Supervisa S.A.

Permite registrar, visualizar, actualizar y eliminar tareas con seguimiento de prioridad, estado y fecha de vencimiento, incluyendo actualización en tiempo real de la lista mediante el patrón Observer.

## Tecnologías utilizadas

- **Java 21**
- **Spring Boot 3.5**
- **Spring Data JPA** (persistencia)
- **Thymeleaf** (motor de plantillas para la interfaz web)
- **SQLite** (base de datos ligera embebida)
- **Maven** (gestión de dependencias y build)
- **Server-Sent Events (SSE)** (actualización en tiempo real)

## Características principales

- CRUD completo de tareas (crear, leer, actualizar, eliminar).
- Campos de tarea: título, descripción, fecha de vencimiento, prioridad y estado.
- Título obligatorio, único y limitado a 150 caracteres.
- Descripción opcional, limitada a 1000 caracteres.
- Prioridad con valor por defecto "Media" y código de colores (alta/media/baja).
- Estado con valor por defecto "Pendiente", con seguimiento del flujo de trabajo.
- Filtros dinámicos por estado y por prioridad.
- Panel de estadísticas con conteo de tareas por estado.
- **Actualización en tiempo real**: la lista de tareas se actualiza automáticamente en todas las pestañas/usuarios conectados cuando se crea, edita o elimina una tarea, sin necesidad de recargar la página. Esto se logra mediante el **patrón de diseño Observer**, implementado con Server-Sent Events (SSE).
- Interfaz responsive, adaptada para dispositivos móviles.
- API REST independiente (`/api/tasks`) además de la interfaz web (`/tasks`).

## Arquitectura

El proyecto sigue una arquitectura por capas (estilo MVC):

```
Controller  →  Service  →  Repository  →  Base de datos (SQLite)
```

Adicionalmente, se implementó el **patrón Observer** para el sistema de notificaciones en tiempo real:

- `TaskObserver`: interfaz que define los eventos de creación, actualización y eliminación de tareas.
- `SseTaskObserver`: implementación concreta que gestiona las conexiones SSE de los clientes y transmite los eventos.
- `TaskService`: actúa como *Subject*, notificando a los observadores registrados cada vez que ocurre un cambio sobre una tarea.

## Estructura del proyecto

**src/main/java/com/kevin/taskmanager**

- **controller**
  - WebController.java — vistas Thymeleaf: /tasks, /tasks/new, /tasks/{id}/edit, /tasks/stream
  - TaskController.java — API REST: /api/tasks
- **model**
  - Task.java — entidad principal (título, descripción, fecha, prioridad, estado)
  - TaskStatus.java — enum: PENDING, IN_PROGRESS, COMPLETED
  - TaskPriority.java — enum: HIGH, MEDIUM, LOW
- **observer**
  - TaskObserver.java — interfaz del patrón Observer
  - SseTaskObserver.java — implementación con Server-Sent Events
- **repository**
  - TaskRepository.java — interfaz JpaRepository, incluye findByStatus, findByPriority, findByTitle
- **service**
  - TaskService.java — lógica de negocio y notificación a los observers
- TaskmanagerApplication.java — clase principal de arranque

**src/main/resources**

- static/css/style.css — estilos de la interfaz
- templates/tasks.html — vista principal: lista, filtros y estadísticas
- templates/task-form.html — formulario de creación/edición
- application.properties — configuración de puerto y base de datos

**Raíz del proyecto**

- taskmanager.db — base de datos SQLite (se genera automáticamente al ejecutar)
- pom.xml — configuración de Maven y dependencias

## Cómo ejecutar el proyecto

### Requisitos previos

- Java 21 o superior instalado.
- Maven instalado (o usar el wrapper incluido `mvnw` / `mvnw.cmd`).

### Pasos

1. Clona el repositorio:
   ```bash
   git clone <url-del-repositorio>
   cd task-manager
   ```

2. Ejecuta la aplicación:
   ```bash
   mvn spring-boot:run
   ```
   O usando el wrapper incluido:
   ```bash
   ./mvnw spring-boot:run
   ```

3. La aplicación quedará disponible en:
   ```
   http://localhost:8081/tasks
   ```

   > El puerto se configura en `src/main/resources/application.properties` mediante la propiedad `server.port`.

4. La base de datos SQLite (`taskmanager.db`) se crea automáticamente en la raíz del proyecto la primera vez que se ejecuta la aplicación.

### Endpoints disponibles

**Interfaz web (Thymeleaf):**
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/tasks` | Lista de tareas, con filtros opcionales por `status` y `priority` |
| GET | `/tasks/new` | Formulario para crear una nueva tarea |
| GET | `/tasks/{id}/edit` | Formulario para editar una tarea existente |
| POST | `/tasks` | Crea una nueva tarea |
| POST | `/tasks/{id}` | Actualiza una tarea existente |
| POST | `/tasks/{id}/delete` | Elimina una tarea |
| GET | `/tasks/stream` | Conexión SSE para actualizaciones en tiempo real |

**API REST:**
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/tasks` | Lista todas las tareas |
| GET | `/api/tasks/{id}` | Obtiene una tarea por ID |
| POST | `/api/tasks` | Crea una nueva tarea |
| PUT | `/api/tasks/{id}` | Actualiza una tarea existente |
| DELETE | `/api/tasks/{id}` | Elimina una tarea |
| PATCH | `/api/tasks/{id}/complete` | Marca una tarea como completada |
| GET | `/api/tasks/status/{status}` | Filtra tareas por estado |
| GET | `/api/tasks/priority/{priority}` | Filtra tareas por prioridad |

## Modelo de datos

```json
{
  "taskId": 1,
  "title": "Ejemplo de tarea",
  "description": "Descripción detallada de la tarea",
  "dueDate": "2025-06-15",
  "priority": "HIGH",
  "status": "PENDING",
  "originFramework": "thymeleaf",
  "userEmail": "kevinerg96@gmail.com"
}
```

- `priority`: valores aceptados `HIGH`, `MEDIUM`, `LOW`.
- `status`: valores aceptados `PENDING`, `IN_PROGRESS`, `COMPLETED`.
- `originFramework`: tecnología del lado del cliente que generó la tarea. En este proyecto, al ser una aplicación monolítica con renderizado del lado del servidor, el valor por defecto es `"thymeleaf"`.
- `userEmail`: correo del desarrollador/aspirante asociado a la tarea. Valor por defecto: `"kevinerg96@gmail.com"`.

## Validaciones

- **Título**: obligatorio, único, máximo 150 caracteres.
- **Descripción**: opcional, máximo 1000 caracteres.
- **Prioridad**: obligatoria, valor por defecto "Media".
- **Estado**: obligatorio, valor por defecto "Pendiente".

## Autor

Kevin Esteban Rodriguez Guijo— Ingeniero de sistemas en formación, Universidad de Ibagué.
Correo de contacto: kevinerg96@gmail.com

