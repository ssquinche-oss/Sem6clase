package samantha.com.sem6clase.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import samantha.com.sem6clase.model.producto.Producto
import samantha.com.sem6clase.repository.producto.ProductoRepository

@Service
class ProductoService {
    @Autowired
    private lateinit var productoRepository: ProductoRepository

    fun crear(producto: Producto): Producto {
        if (producto.nombre.isBlank())
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del producto es obligatorio")
        if (producto.stock < 0)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "El stock no puede ser negativo")
        if (productoRepository.findByNombre(producto.nombre) != null)
            throw ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un producto con el nombre '${producto.nombre}'")
        producto.id = null
        return productoRepository.save(producto)
    }
}