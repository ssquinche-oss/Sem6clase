package samantha.com.sem6clase.model.producto

import jakarta.persistence.*

@Entity
@Table(name = "productos")
class Producto(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false, unique = true)
    var nombre: String = "",

    var descripcion: String? = null,

    @Column(nullable = false)
    var stock: Int = 0
)