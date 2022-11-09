package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    @PersistenceContext
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    private String sql = "";

    public List<Order> findAll(OrderSearch orderSearch) {
        String memberName = "'' OR 1=1";
        String status = "'' OR 1=1";

        if (String.format(orderSearch.getMemberName()).isEmpty() == false) {
            memberName = orderSearch.getMemberName();
        }
        if (String.format(orderSearch.getOrderStatus().toString()).isEmpty() == false) {
            status = orderSearch.getOrderStatus().toString();
        }

        List<Order> resultList = em.createQuery("SELECT o " +
                        "FROM Order o INNER JOIN o.member m " +
                        "ON o.status = :status " +
                        "AND m.name LIKE :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000) // 최대 1000 건
                .getResultList();

        return resultList;
    }


}
