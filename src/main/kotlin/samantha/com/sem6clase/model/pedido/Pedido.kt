package samantha.com.sem6clase.model.pedido

import jakarta.persistence.*

@Entity
@Table(name = "pedidos")
class Pedido(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "numero_pedido", nullable = false)
    var numeroPedido: String = "",

    @Column(name = "nombre_cliente", nullable = false)
    var nombreCliente: String = "",

    @Column(name = "nombre_producto", nullable = false)
    var nombreProducto: String = "",

    @Column(nullable = false)
    var cantidad: Int = 0,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var estado: EstadoPedido = EstadoPedido.PENDIENTE,

    @Column(name = "codigo_verificacion", nullable = false)
    var codigoVerificacion: String = ""
)