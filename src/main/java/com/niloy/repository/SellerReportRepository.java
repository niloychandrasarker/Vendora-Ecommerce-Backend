package com.niloy.repository;

import com.niloy.modal.SellerReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerReportRepository extends JpaRepository<SellerReport, String> {
   SellerReport findBySellerId(Long sellerId);
}
