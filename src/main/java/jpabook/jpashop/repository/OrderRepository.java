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
        List<Order> resultList = null;

        if (orderSearch.getMemberName() == null && orderSearch.getOrderStatus() == null) {
            resultList =
                em.createQuery("SELECT o " +
                        "FROM Order o INNER JOIN o.member m ", Order.class)
                    .setMaxResults(1000)
                    .getResultList();
        } else if (orderSearch.getMemberName() != null && orderSearch.getOrderStatus() == null) {
            resultList = em.createQuery(
                    "SELECT o " +
                        "FROM Order o INNER JOIN o.member m " +
                        "ON m.name LIKE :name", Order.class)
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000) // 최대 1000 건
                .getResultList();
        } else if (orderSearch.getMemberName() == null && orderSearch.getOrderStatus() != null) {
            resultList = em.createQuery(
                    "SELECT o " +
                        "FROM Order o INNER JOIN o.member m " +
                        "ON o.status = :status", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setMaxResults(1000) // 최대 1000 건
                .getResultList();
        } else if (String.format(orderSearch.getMemberName()).isEmpty() == false &&
            String.format(orderSearch.getOrderStatus().toString()).isEmpty() == false
        ) {
            resultList = em.createQuery(
                    "SELECT o " +
                        "FROM Order o INNER JOIN o.member m " +
                        "ON o.status = :status " +
                        "AND m.name LIKE :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000) // 최대 1000 건
                .getResultList();
        }


        return resultList;
    }


}
