package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class ProductoTest {
    

    @Autowired
    private ProductoRepositorio repositorio;

    @Test
    @Rollback(false) //Rollback retrocede la consulta en la base de datos 
    @Order(1)
    public void testGuardarProducto(){
        Producto producto = new Producto("Televisor Samsung HD", 1300000);
        Producto productoGuardado = repositorio.save(producto);
        
        assertNotNull(productoGuardado);
    }

    @Test
    @Order(2)
    public void testBuscarProductoPorNombre(){
        String nombre = "Televisor Samsung HD";
        Producto producto = repositorio.findByNombre(nombre);

        assertThat(producto.getNombre()).isEqualTo(nombre);

    }

    //Metodo para buscar un producto por nombre No Existente haciendon referencia a si es nullo
    @Test
    @Order(3)
    public void testBuscarProductoNoExistente(){
        String nombre = "Telefono Samsung";
        Producto producto = repositorio.findByNombre(nombre);

        assertNull(producto);
    }

    @Test
    @Rollback(false)
    @Order(4)
    public void testActualizarProducto(){

        String nombreProducto = "Televisor Samsung 32";
        Producto producto = new Producto(nombreProducto, 1450000); //Valores actualizados
        producto.setId(1); // ID del producto a actualizar
        repositorio.save(producto); //Actualizar el objeto
        Producto productoActualizado = repositorio.findByNombre(nombreProducto);
        assertThat(productoActualizado.getNombre()).isEqualTo(nombreProducto); // Verificar si se ha actualizado
    }

    @Test
    @Order(5)
    public void testListarProductos(){
        List<Producto> productos = (List<Producto>) repositorio.findAll(); //Se crea una Lista para obtener todos los productos existentes
        for(Producto producto : productos){ System.out.println(producto);}
        assertThat(productos).size().isGreaterThan(0); //Confirmar que el tamaño lista de productos es mayor a 0.
    }

    @Test
    @Rollback(false)
    @Order(6)
    public void testEliminarProducto(){
        Integer id = 5;
        boolean existente = repositorio.findById(id).isPresent(); //verificar si existe el producto con el id proporcionado
        repositorio.deleteById(id);
        boolean noExistente = repositorio.findById(id).isPresent(); //verificar si elimino el producto con el id proporcionado True

        assertTrue(existente); //
        assertFalse(noExistente);
        
    }

}
