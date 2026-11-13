CREATE TABLE pedidos (
                         id BIGSERIAL PRIMARY KEY,
                         numero_pedido VARCHAR(36) NOT NULL,
                         nombre_cliente VARCHAR(100) NOT NULL,
                         nombre_producto VARCHAR(100) NOT NULL,
                         cantidad INT NOT NULL CHECK (cantidad > 0),
                         estado VARCHAR(20) NOT NULL,
                         codigo_verificacion VARCHAR(6) NOT NULL
);