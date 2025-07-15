package com.niloy.modal;

import com.niloy.domain.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {

    private String paymentId;
    private String razorpayPaymentLinkId;
    private String razorpayPamentLinkReferenceId;
    private String razorpayLinkStatus;
    private String razorpayPaymentId;
    private PaymentStatus status;

}
