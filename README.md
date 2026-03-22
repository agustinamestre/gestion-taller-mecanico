# 🛠️ Sistema de Gestión de Talleres Mecánicos

Sistema web integral diseñado para la administración de talleres mecánicos. El objetivo principal es centralizar la gestión de clientes, vehículos y órdenes de trabajo, incorporando un sistema de fidelización mediante recordatorios automáticos de servicios.

## 🏗️ Arquitectura: Clean Architecture
Este proyecto implementa los principios de **Arquitectura Limpia**, garantizando el desacoplamiento entre la lógica de negocio y las herramientas externas (Base de Datos, Frameworks, APIs).

### Estructura de Capas:
* **Domain:** Contiene el corazón del negocio (Entidades, Interfaces de Repositorio y Excepciones). Es Java puro, sin dependencias de Spring.
* **Application:** Implementa los casos de uso (ej. `RegistrarCliente`, `GenerarOrdenTrabajo`). Orquesta la lógica de negocio.
* **Infrastructure:** Adaptadores para el mundo exterior.
    * `persistence`: Implementación de JPA y PostgreSQL.
    * `rest`: Controladores de la API y DTOs.
    * `config`: Configuración de beans de Spring.

## 🚀 Stack Tecnológico
* **Java 17/21**
* **Spring Boot**
* **Spring Data JPA**
* **PostgreSQL**
* **Maven**

## 📋 MVP - Alcance del Proyecto
- **Gestión de Clientes:** Registro, edición y consulta.
- **Gestión de Vehículos:** Asociación uno a muchos con clientes.
- **Órdenes de Trabajo:** Flujo de estados (Ingresado → En Reparación → Listo → Entregado).
- **Facturación:** Presupuestos y facturación simple.
- **Fidelización:** Alertas automáticas de service por tiempo.
