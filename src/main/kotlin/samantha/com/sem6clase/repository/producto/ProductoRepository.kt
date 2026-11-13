package samantha.com.sem6clase.repository.producto

import org.springframework.data.jpa.repository.JpaRepository
import samantha.com.sem6clase.model.producto.Producto

interface ProductoRepository : JpaRepository<Producto, Long> {
    fun findByNombre(nombre: String): Producto?
}