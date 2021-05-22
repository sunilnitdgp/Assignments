package com.assignment.repository;

import com.assignment.model.Order;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public class OrdersRepository extends BaseRepository {

    private static final String[] ID_ARRAY_ORDERS = { "unique_order_id" };
    private static final String[] ID_ARRAY_ORDER_ITEMS = { "unique_ordered_item_id" };

    private static final String PLACE_ORDER = "INSERT INTO orders() VALUES ()";

    private static final String PLACE_ORDERED_ITEMS = "INSERT INTO ordered_items(unique_order_id, c_unique_id, " +
            "quantity, c_name) VALUES (:unique_order_id, :c_unique_id, :quantity, :c_name)";

    @Transactional
    public Long placeOrder(List<Order> orderList) {
        Long placedOrderId = null;
        if(CollectionUtils.isNotEmpty(orderList)) {
            Long orderId = saveAndGetPrimaryKey(PLACE_ORDER, Map.of(), ID_ARRAY_ORDERS);
            Map[] batchValues = orderList.parallelStream()
                    .map(order -> Map.of(UNIQUE_ORDER_ID, orderId, C_UNIQUE_ID, order.getC_unique_id(),
                            QUANTITY, order.getQuantity(), C_NAME, order.getC_name())).toArray(Map[]::new);
            placedOrderId = orderId;
            batchUpdate(PLACE_ORDERED_ITEMS, batchValues);
        }
        return placedOrderId;
    }
}
