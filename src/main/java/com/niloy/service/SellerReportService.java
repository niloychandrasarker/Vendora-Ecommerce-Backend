package com.niloy.service;

import com.niloy.modal.Seller;
import com.niloy.modal.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(SellerReport sellerReport);
}
