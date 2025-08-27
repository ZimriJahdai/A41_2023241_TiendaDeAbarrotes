  -- drop database if exists TiendaAbarrotes;
create database TiendaAbarrotes;
use TiendaAbarrotes;


create table Producto (
    id bigint primary key auto_increment,
    codigo varchar(50) not null unique,
    nombre varchar(255) not null,
    precio double not null,
    cantidad int not null,
    fecha_creacion timestamp default current_timestamp,
    fecha_actualizacion timestamp default current_timestamp on update current_timestamp
);


create index idx_productos_codigo on Producto(codigo);
create index idx_productos_nombre on Producto(nombre);


create table historial_inventario (
    id bigint primary key auto_increment,
    producto_id bigint not null,
    tipo_movimiento varchar(20) not null,
    cantidad_anterior int not null,
    cantidad_nueva int not null,
    precio_anterior decimal(10,2),
    precio_nuevo decimal(10,2),
    fecha_movimiento timestamp default current_timestamp,
    descripcion varchar(500),
    foreign key (producto_id) references Producto(id)
);

create table ventas (
    id bigint primary key auto_increment,
    fecha_venta timestamp default current_timestamp,
    total_venta decimal(10,2) not null
);


create table detalles_venta (
    id bigint primary key auto_increment,
    venta_id bigint not null,
    producto_id bigint not null,
    cantidad int not null,
    precio_unitario decimal(10,2) not null,
    subtotal decimal(10,2) not null,
    foreign key (venta_id) references ventas(id),
    foreign key (producto_id) references Producto(id)
);


create view vw_inventario_actual as
select 
    p.codigo,
    p.nombre,
    p.precio,
    p.cantidad,
    (p.precio * p.cantidad) as valor_total,
    p.fecha_actualizacion
from producto p
order by p.nombre;


create view vw_productos_stock_bajo as
select 
    codigo,
    nombre,
    precio,
    cantidad
from producto
where cantidad < 10;


insert into Producto (codigo, nombre, precio, cantidad) values
('elec001', 'laptop dell xps 15', 2500.00, 15),
('elec002', 'monitor samsung 27" 4k', 450.00, 25),
('elec003', 'teclado mecánico razer', 120.00, 30),
('elec004', 'mouse logitech g pro', 80.00, 40),
('elec005', 'auriculares sony wh-1000xm4', 350.00, 20),
('elec006', 'tableta gráfica wacom', 300.00, 12),
('elec007', 'impresora hp laserjet', 200.00, 8),
('elec008', 'ssd samsung 1tb nvme', 150.00, 35),
('elec009', 'memoria ram 16gb ddr4', 90.00, 28),
('elec010', 'procesador intel i7-12700k', 420.00, 10),
('elec011', 'tarjeta gráfica rtx 4070', 650.00, 6),
('elec012', 'placa base asus rog', 280.00, 14),
('elec013', 'fuente alimentación 750w', 110.00, 18),
('elec014', 'cámara web logitech c920', 70.00, 22),
('elec015', 'altavoces creative 2.1', 55.00, 16);

select * from Producto;

insert into historial_inventario (producto_id, tipo_movimiento, cantidad_anterior, cantidad_nueva, precio_anterior, precio_nuevo, descripcion) values
(1, 'entrada_stock', 0, 15, 0.00, 2500.00, 'carga inicial de inventario'),
(2, 'entrada_stock', 0, 25, 0.00, 450.00, 'carga inicial de inventario'),
(3, 'entrada_stock', 0, 30, 0.00, 120.00, 'carga inicial de inventario'),
(4, 'entrada_stock', 0, 40, 0.00, 80.00, 'carga inicial de inventario'),
(5, 'entrada_stock', 0, 20, 0.00, 350.00, 'carga inicial de inventario');



delimiter $$
create procedure sp_actualizar_stock(
    in p_codigo varchar(50),
    in p_cantidad int,
    in p_tipo_movimiento varchar(20)
)
begin
    declare v_producto_id bigint;
    declare v_cantidad_actual int;
    declare v_nueva_cantidad int;
    
    select id, cantidad into v_producto_id, v_cantidad_actual 
    from productos where codigo = p_codigo;
    
    if v_producto_id is null then
        signal sqlstate '45000' set message_text = 'producto no encontrado';
    end if;
    
    if p_tipo_movimiento = 'entrada' then
        set v_nueva_cantidad = v_cantidad_actual + p_cantidad;
    elseif p_tipo_movimiento = 'salida' then
        if v_cantidad_actual < p_cantidad then
            signal sqlstate '45000' set message_text = 'stock insuficiente';
        end if;
        set v_nueva_cantidad = v_cantidad_actual - p_cantidad;
    else
        signal sqlstate '45000' set message_text = 'tipo de movimiento no válido';
    end if;
    
    update productos set cantidad = v_nueva_cantidad where id = v_producto_id;
    
    insert into historial_inventario (producto_id, tipo_movimiento, cantidad_anterior, cantidad_nueva, descripcion)
    values (v_producto_id, p_tipo_movimiento, v_cantidad_actual, v_nueva_cantidad, 
            concat('ajuste de inventario - ', p_tipo_movimiento, ' de ', p_cantidad, ' unidades'));
end $$
delimiter ;


delimiter $$
create procedure sp_registrar_venta(
    in p_codigo_producto varchar(50),
    in p_cantidad_vendida int
)
begin
    declare v_producto_id bigint;
    declare v_precio_unitario decimal(10,2);
    declare v_stock_actual int;
    declare v_venta_id bigint;
    declare v_subtotal decimal(10,2);
    
    select id, precio, cantidad into v_producto_id, v_precio_unitario, v_stock_actual
    from productos where codigo = p_codigo_producto;
    
    if v_producto_id is null then
        signal sqlstate '45000' set message_text = 'producto no encontrado';
    end if;
    
    if v_stock_actual < p_cantidad_vendida then
        signal sqlstate '45000' set message_text = 'stock insuficiente para la venta';
    end if;
    

    insert into ventas (total_venta) values (0);
    set v_venta_id = last_insert_id();
    

    set v_subtotal = v_precio_unitario * p_cantidad_vendida;
    
   
    insert into detalles_venta (venta_id, producto_id, cantidad, precio_unitario, subtotal)
    values (v_venta_id, v_producto_id, p_cantidad_vendida, v_precio_unitario, v_subtotal);
    
    
    update ventas set total_venta = v_subtotal where id = v_venta_id;
    
    
    call sp_actualizar_stock(p_codigo_producto, p_cantidad_vendida, 'salida');
    
    select v_venta_id as numero_venta, v_subtotal as total;
end $$
delimiter ;


delimiter $$
create function fn_valor_total_inventario() returns decimal(10,2)
deterministic
reads sql data
begin
    declare total decimal(10,2);
    select sum(precio * cantidad) into total from productos;
    return coalesce(total, 0);
end $$
delimiter ;


delimiter $$
create function fn_obtener_stock(p_codigo varchar(50)) returns int
deterministic
reads sql data
begin
    declare v_stock int;
    select cantidad into v_stock from productos where codigo = p_codigo;
    return coalesce(v_stock, 0);
end $$
delimiter ;
