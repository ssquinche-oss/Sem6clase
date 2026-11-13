package samantha.com.sem6clase.repository.pedido

import org.springframework.data.jpa.repository.JpaRepository
import samantha.com.sem6clase.model.pedido.EstadoPedido
import samantha.com.sem6clase.model.pedido.Pedido

interface PedidoRepository : JpaRepository<Pedido, Long> {
    fun existsByNombreClienteAndEstado(nombreCliente: String, estado: EstadoPedido): Boolean
    fun findByNumeroPedido(numeroPedido: String): List<Pedido>
}