IF OBJECT_ID('productos', 'U') IS NOT NULL DROP TABLE productos;

CREATE TABLE productos (
    id          BIGINT        IDENTITY(1,1) PRIMARY KEY,
    nombre      NVARCHAR(255) NOT NULL UNIQUE,
    descripcion NVARCHAR(500),
    precio      FLOAT         NOT NULL,
    stock       INT           NOT NULL
);
