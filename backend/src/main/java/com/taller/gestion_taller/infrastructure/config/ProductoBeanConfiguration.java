package com.taller.gestion_taller.infrastructure.config;

import com.taller.gestion_taller.application.mapper.ProductoApplicationMapper;
import com.taller.gestion_taller.application.usecases.producto.ActualizarPrecioProducto;
import com.taller.gestion_taller.application.usecases.producto.ActualizarPrecioProductoUseCase;
import com.taller.gestion_taller.application.usecases.producto.ActualizarStockProducto;
import com.taller.gestion_taller.application.usecases.producto.ActualizarStockProductoUseCase;
import com.taller.gestion_taller.application.usecases.producto.BuscarProductoPorTipo;
import com.taller.gestion_taller.application.usecases.producto.BuscarProductoPorTipoUseCase;
import com.taller.gestion_taller.application.usecases.producto.DesactivarProducto;
import com.taller.gestion_taller.application.usecases.producto.DesactivarProductoUseCase;
import com.taller.gestion_taller.application.usecases.producto.ModificarProducto;
import com.taller.gestion_taller.application.usecases.producto.ModificarProductoUseCase;
import com.taller.gestion_taller.application.usecases.producto.ObtenerTiposProducto;
import com.taller.gestion_taller.application.usecases.producto.ObtenerTiposProductoUseCase;
import com.taller.gestion_taller.application.usecases.producto.RegistrarProducto;
import com.taller.gestion_taller.application.usecases.producto.RegistrarProductoUseCase;
import com.taller.gestion_taller.domain.repositories.ProductoRepository;
import com.taller.gestion_taller.domain.service.ProductoValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductoBeanConfiguration {

    @Bean
    public ProductoValidator productoValidator(ProductoRepository productoRepository) {
        return new ProductoValidator(productoRepository);
    }

    @Bean
    public RegistrarProducto registrarProductoUseCase(ProductoRepository productoRepository,
                                                      ProductoApplicationMapper productoApplicationMapper) {
        return new RegistrarProductoUseCase(productoRepository, productoApplicationMapper);
    }

    @Bean
    public BuscarProductoPorTipo buscarProductoPorTipoUseCase(ProductoRepository productoRepository) {
        return new BuscarProductoPorTipoUseCase(productoRepository);
    }

    @Bean
    public ModificarProducto modificarProductoUseCase(ProductoRepository productoRepository,
                                                      ProductoValidator productoValidator) {
        return new ModificarProductoUseCase(productoRepository, productoValidator);
    }

    @Bean
    public DesactivarProducto desactivarProductoUseCase(ProductoRepository productoRepository) {
        return new DesactivarProductoUseCase(productoRepository);
    }

    @Bean
    public ObtenerTiposProducto obtenerTiposProductoUseCase() {
        return new ObtenerTiposProductoUseCase();
    }

    @Bean
    public ActualizarPrecioProducto actualizarPrecioProductoUseCase(ProductoRepository productoRepository) {
        return new ActualizarPrecioProductoUseCase(productoRepository);
    }

    @Bean
    public ActualizarStockProducto actualizarStockProductoUseCase(ProductoRepository productoRepository) {
        return new ActualizarStockProductoUseCase(productoRepository);
    }
}
