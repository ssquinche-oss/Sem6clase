package samantha.com.sem6clase.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import samantha.com.sem6clase.model.producto.Producto
import samantha.com.sem6clase.service.ProductoService

@RestController
@RequestMapping("/producto")
class ProductoController {
    @Autowired
    private lateinit var productoService: ProductoService

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun crear(@RequestBody producto: Producto): Producto {
        return productoService.crear(producto)
    }
}