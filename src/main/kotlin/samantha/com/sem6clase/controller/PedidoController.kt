package samantha.com.sem6clase.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import samantha.com.sem6clase.model.pedido.Pedido
import samantha.com.sem6clase.service.PedidoService

@RestController
@RequestMapping("/pedido")
class PedidoController {
    @Autowired
    private lateinit var pedidoService: PedidoService

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun crear(@RequestBody items: List<Pedido>): List<Pedido> {
        return pedidoService.crear(items)
    }

    @PutMapping("/{numeroPedido}/enviar")
    fun enviar(@PathVariable numeroPedido: String): List<Pedido> {
        return pedidoService.enviar(numeroPedido)
    }

    @PutMapping("/{numeroPedido}/recibir")
    fun recibir(
        @PathVariable numeroPedido: String,
        @RequestParam codigo: String
    ): List<Pedido> {
        return pedidoService.recibir(numeroPedido, codigo)
    }
}