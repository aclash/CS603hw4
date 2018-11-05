package cs603.hw4;

import javax.persistence.*;
import static org.mockito.Mockito.*;

public class Main {
    public static void main(String[] args) {
        EntityManager em = mock(EntityManager.class);
        EntityTransaction transaction = mock(EntityTransaction.class);
        when(em.getTransaction()).thenReturn(transaction);
        Purchase purchase = new Purchase();
        purchase.setEntityManager(em);
        Customer customer = mock(Customer.class);
        when(customer.getLimit()).thenReturn(15);
        purchase.setCustomer(customer);

        //Mock query1
        Product spy_product1 = new Product();
        Product product1 = spy(spy_product1);
        //Product product1 = mock(Product.class);
        product1.setProductId(1);
        product1.setPrice(10);
        verify(product1).setProductId(argThat(id -> id > 0 && id < Integer.MAX_VALUE));
        verify(product1).setProductId(argThat(id -> id > 0 && id < Integer.MAX_VALUE));
        //when(product1.getProductId()).thenReturn(1);
        //when(product1.getPrice()).thenReturn(10);
        int cnt = 1;
        //Query query1 = mock(Query.class);
        Query spy_query1 = em.createNamedQuery("Product.findByProductId");
        Query query1 = spy(spy_query1);
        when(query1.setParameter("productId", 1)).thenReturn(query1);
        when(query1.getSingleResult()).thenReturn(product1);
        when(em.createNamedQuery("Product.findByProductId")).thenReturn(query1);
        purchase.add(product1, cnt);

        //will accept
        purchase.complete();

        //Mock query2
        Product product2 = mock(Product.class);
        product2.setProductId(2);
        //verify(product2).setProductId(argThat(id -> id > 0 && id < Integer.MAX_VALUE));
        when(product2.getProductId()).thenReturn(2);
        when(product2.getPrice()).thenReturn(20);
        cnt = 2;
        Query query2 = mock(Query.class);
        when(query2.setParameter("productId", 2)).thenReturn(query2);
        when(query2.getSingleResult()).thenReturn(product2);
        when(em.createNamedQuery("Product.findByProductId")).thenReturn(query2);
        purchase.add(product2, cnt);

        //will roll back
        purchase.complete();

        verify(transaction).begin();
        verify(transaction).commit();
        verify(transaction).rollback();
        System.out.println("**************************Test Succeed.****************************");

        //Mock invalid query
        Product spy_product3 = new Product();
        //Product product3 = mock(Product.class);
        Product product3 = spy(spy_product3);
        product3.setProductId(-1);
        verify(product3).setProductId(argThat(id -> id > 0 && id < Integer.MAX_VALUE));          //will warn invalid id
        //when(product3.getProductId()).thenReturn(-1);
        when(product3.getPrice()).thenReturn(30);
        cnt = 3;
        Query query3 = mock(Query.class);
        when(query3.setParameter("productId", -1)).thenReturn(query3);
        when(query3.getSingleResult()).thenReturn(product3);
        when(em.createNamedQuery("Product.findByProductId")).thenReturn(query3);
        purchase.add(product3, cnt);

    }
}
