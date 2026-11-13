package samantha.com.sem6clase.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import samantha.com.sem6clase.model.pedido.EstadoPedido
import samantha.com.sem6clase.model.pedido.Pedido
import samantha.com.sem6clase.repository.pedido.PedidoRepository
import samantha.com.sem6clase.repository.producto.ProductoRepository
import java.util.UUID
import kotlin.random.Random

@Service
class PedidoService {
    @Autowired
    private lateinit var pedidoRepository: PedidoRepository

    @Autowired
    private lateinit var productoRepository: ProductoRepository

    fun crear(items: List<Pedido>): List<Pedido> {
        if (items.isEmpty())
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "El pedido debe tener al menos un producto")

        val cliente = items.first().nombreCliente
        if (cliente.isBlank() || items.any { it.nombreCliente != cliente })
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Todos los productos deben tener el mismo nombre de cliente")
        if (items.any { it.nombreProducto.isBlank() || it.cantidad < 1 })
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cada producto necesita nombre y cantidad mayor a 0")
        if (items.map { it.nombreProducto }.distinct().size != items.size)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "No repitas el mismo producto en un pedido")

        if (pedidoRepository.existsByNombreClienteAndEstado(cliente, EstadoPedido.PENDIENTE))
            throw ResponseStatusException(HttpStatus.CONFLICT, "El cliente ya tiene un pedido pendiente")

        val lineas = items.map { item ->
            val producto = productoRepository.findByNombre(item.nombreProducto)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "El producto '${item.nombreProducto}' no existe")
            if (producto.stock < item.cantidad)
                throw ResponseStatusException(HttpStatus.CONFLICT, "Stock insuficiente para '${producto.nombre}' (disponible: ${producto.stock})")
            producto to item
        }

        lineas.forEach { (producto, item) -> producto.stock -= item.cantidad }
        productoRepository.saveAll(lineas.map { it.first })

        val numero = UUID.randomUUID().toString()
        val codigo = Random.nextInt(100000, 1000000).toString()
        items.forEach {
            it.id = null
            it.numeroPedido = numero
            it.estado = EstadoPedido.PENDIENTE
            it.codigoVerificacion = codigo
        }
        return pedidoRepository.saveAll(items)
    }

    fun enviar(numeroPedido: String): List<Pedido> {
        val pedidos = obtener(numeroPedido)
        if (pedidos.first().estado != EstadoPedido.PENDIENTE)
            throw ResponseStatusException(HttpStatus.CONFLICT, "Solo un pedido pendiente puede pasar a enviado")
        pedidos.forEach { it.estado = EstadoPedido.ENVIADO }
        return pedidoRepository.saveAll(pedidos)
    }

    fun recibir(numeroPedido: String, codigo: String): List<Pedido> {
        val pedidos = obtener(numeroPedido)
        if (pedidos.first().estado != EstadoPedido.ENVIADO)
            throw ResponseStatusException(HttpStatus.CONFLICT, "El pedido debe estar en estado enviado antes de marcarlo como recibido")
        if (pedidos.first().codigoVerificacion != codigo)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Código de verificación incorrecto")
        pedidos.forEach { it.estado = EstadoPedido.RECIBIDO }
        return pedidoRepository.saveAll(pedidos)
    }

    private fun obtener(numeroPedido: String): List<Pedido> =
        pedidoRepository.findByNumeroPedido(numeroPedido)
            .ifEmpty { throw ResponseStatusException(HttpStatus.NOT_FOUND, "El pedido no existe") }
}