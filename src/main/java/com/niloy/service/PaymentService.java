package com.niloy.service;

import com.niloy.modal.Order;
import com.niloy.modal.PaymentOrder;
import com.niloy.modal.User;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

import java.util.Set;

public interface PaymentService {
    PaymentOrder createOrder(User user, Set<Order> orders);
    PaymentOrder getPaymentOrderByIId(Long orderId) throws Exception;
    PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception;
    Boolean ProceedPaymentOrder(PaymentOrder paymentOrder,
                                String paymentId,
                                String paymentLinkId) throws RazorpayException;
    PaymentLink createRazorpayPaymentLink(User user, Long amount,
                                          Long orderId) throws RazorpayException;
    String createStripePaymentLink(User user,
                                   Long amount, Long orderId) throws StripeException;
}
