CREATE TABLE productos (
                           id BIGSERIAL PRIMARY KEY,
                           nombre VARCHAR(100) NOT NULL UNIQUE,
                           descripcion VARCHAR(255),
                           stock INT NOT NULL CHECK (stock >= 0)
);