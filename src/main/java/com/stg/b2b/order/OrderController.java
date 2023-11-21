package com.stg.b2b.order;

import com.stg.b2b.exception.NotFoundException;
import com.stg.b2b.order.dto.OrderResponseDto;
import com.stg.b2b.order.dto.UpdateOrderDto;
import com.stg.b2b.upload.NotificationExcelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private NotificationExcelService notificationExcelService;
    @Autowired
    private OrderService orderService;
    /**
     * returns list of orders based on given status
     * @param status
     * @return ResponseEntity containing the order DTO.
     */
    @GetMapping("/{status}")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(@PathVariable Boolean status){
        logger.info("Getting all orders");
        List<OrderResponseDto> orders = orderService.getOrdersList(status);
        logger.info("Found {} orders",orders.size());
        return ResponseEntity.ok(orders);
    }
    @PostMapping("/sourcing")
    public ResponseEntity<List<OrderResponseDto>> getAllOrdersBasedOnSourcing(@RequestBody List<Integer> sourcingOrders){
        logger.info("Getting all orders based on sourcing");
        List<OrderResponseDto> orders = orderService.getOrdersListBasedOnSourcing(sourcingOrders);
        logger.info("Found {} orders",orders.size());
        return ResponseEntity.ok(orders);
    }

    /** returns list of orderNo that are not having any positions in the data
     *  it is currently being used to show all the order pdfs that are required to be uploaded
     no parameters required */
    @GetMapping("orderswopositions")
    public ResponseEntity<List<Integer>> ordersWoPositions(){
        return ResponseEntity.ok(orderService.ordersWoPositions());
    }

    /**
     * Updates the order based on the given fields and returns the updated order DTO.
     * @param updateOrderDto The DTO containing the updated order details.
     * @return ResponseEntity containing the updated order DTO.
     * @throws NotFoundException if the order is not found.
     */
    @PutMapping(value="update")
    public ResponseEntity<OrderResponseDto> updateOrderDetails(@RequestBody UpdateOrderDto updateOrderDto, Authentication auth) throws NotFoundException {
        logger.info("Updating order details for order ID: {}", updateOrderDto.getOrderNo());
        OrderResponseDto updatedOrder = this.orderService.updateOrderDetails(updateOrderDto, auth.getPrincipal().toString());
        logger.info("Updated order details for order ID: {}", updateOrderDto.getOrderNo());
        return ResponseEntity.ok(updatedOrder);
    }
}

