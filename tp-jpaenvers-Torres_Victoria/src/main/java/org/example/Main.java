package org.example;

import org.example.entidades.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("example-unit");
        EntityManager entityManager = emf.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Categoria perecedero = Categoria.builder()
                    .denominacion("Perecedero")
                    .build();

            Categoria lacteo = Categoria.builder()
                    .denominacion("Lacteo")
                    .build();

            Categoria limpieza = Categoria.builder()
                    .denominacion("Limpieza")
                    .build();

            Articulo leche = Articulo.builder()
                    .cantidad(10)
                    .denominacion("leche")
                    .precio(1000)
                    .build();

            Articulo detergente = Articulo.builder()
                    .cantidad(12)
                    .denominacion("detergente")
                    .precio(500)
                    .build();

            leche.getCategorias().add(perecedero);
            leche.getCategorias().add(lacteo);
            detergente.getCategorias().add(limpieza);

            lacteo.getArticulos().add(leche);
            perecedero.getArticulos().add(leche);
            limpieza.getArticulos().add(detergente);

            Factura fac1 = Factura.builder()
                    .fecha("14/04/2024")
                    .numero(40)
                    .total(16000)
                    .build();

            Cliente cli1 = Cliente.builder()
                    .nombre("Victoria")
                    .apellido("Torres")
                    .dni(44878296)
                    .build();

            Domicilio domicilio = Domicilio.builder()
                    .nombreCalle("Rioja")
                    .numero(467)
                    .build();

            Cliente cli2 = Cliente.builder()
                    .nombre("Valentina")
                    .apellido("Artola")
                    .dni(44122610)
                    .build();

            Domicilio domicilio2 = Domicilio.builder()
                    .nombreCalle("Paso de los andes")
                    .numero(425)
                    .build();

            cli1.setDomicilio(domicilio);
            cli2.setDomicilio(domicilio2);

            fac1.setCliente(cli1);

            DetalleFactura detalleFactura = DetalleFactura.builder()
                    .cantidad(10)
                    .subTotal(10000)
                    .build();

            detalleFactura.setArticulo(leche);

            fac1.getDetalles().add(detalleFactura);

            DetalleFactura detalleFactura2 = DetalleFactura.builder()
                    .cantidad(12)
                    .subTotal(6000)
                    .build();

            detalleFactura2.setArticulo(detergente);

            fac1.getDetalles().add(detalleFactura2);

            entityManager.persist(fac1);
            entityManager.flush();
            entityManager.getTransaction().commit();

            // Actualizar la persona
            entityManager.getTransaction().begin();
            cli1.setDni(44912312);
            cli1.setApellido("Tapia");
            entityManager.merge(cli1);
            entityManager.getTransaction().commit();

            // Buscar la persona por ID
            Cliente clienteEncontrado = entityManager.find(Cliente.class, cli1.getId());

            System.out.println("Persona encontrada: " + clienteEncontrado);

            // Desconectar la entidad (estado Detached)
            entityManager.getTransaction().begin();
            entityManager.detach(cli2);
            entityManager.getTransaction().commit();

            System.out.println("Voy a eliminar persona que ya no está vinculada");
            // Eliminar la persona
            entityManager.getTransaction().begin();
            entityManager.remove(cli2);
            entityManager.getTransaction().commit();


            //System.out.println("Me tiene que dar error");
            //Buscar la persona por ID
            //Cliente clienteEncontrado2 = entityManager.find(Cliente.class, cli2.getId());
            //System.out.println("Persona encontrada desde la base de datos: " + clienteEncontrado2);

        }catch (Exception e){
            entityManager.getTransaction().rollback();
            System.out.println(e.getMessage());
            System.out.println("Error...");
        }

    }
}