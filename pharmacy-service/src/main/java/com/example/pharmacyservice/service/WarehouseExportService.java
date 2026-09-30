package com.example.pharmacyservice.service;

import com.example.pharmacyservice.dto.WarehouseExportRequest;
import com.example.pharmacyservice.entity.Drug;
import com.example.pharmacyservice.redis.PharmacyAlert;
import com.example.pharmacyservice.redis.RedisAlertPublisher;
import com.example.pharmacyservice.repo.DrugRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class WarehouseExportService {

    private final DrugRepository drugRepository;
    private final RedissonClient redissonClient;
    private final CacheManager cacheManager;
    private final RedisAlertPublisher redisAlertPublisher;

    public String exportWarehouse(
            Long drugId,
            WarehouseExportRequest request
    ) {

        String lockKey = "lock:medicine:" + drugId;

        RLock lock = redissonClient.getLock(lockKey);

        boolean locked = false;

        try {

            // Chờ lock tối đa 3 giây,
            // giữ lock tối đa 5 giây
            locked = lock.tryLock(
                    3,
                    5,
                    TimeUnit.SECONDS
            );

            if (!locked) {
                return "Sản phẩm đang được cập nhật, vui lòng thử lại sau";
            }

            Drug drug = drugRepository.findById(drugId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Không tìm thấy thuốc ID = " + drugId
                            )
                    );

            Integer quantityExport =
                    request.getQuantityStock();

            if (quantityExport == null || quantityExport <= 0) {
                return "Số lượng xuất kho không hợp lệ";
            }

            if (drug.getStock() == null
                    || drug.getStock() < quantityExport) {

                return "Không đủ số lượng thuốc trong kho";
            }

            Integer oldStock = drug.getStock();

            // 1. Trừ tồn kho
            drug.setStock(
                    oldStock - quantityExport
            );

            drugRepository.saveAndFlush(drug);

            // 2. Xóa cache của thuốc
            Cache cache =
                    cacheManager.getCache("medicines");

            if (cache != null) {
                cache.evict(drugId);
            }

            // 3. Gửi thông báo Pub/Sub
            String message =
                    "Đã xuất "
                            + quantityExport
                            + " hộp "
                            + drug.getName()
                            + " | Số xe tải: "
                            + request.getNumberTrucks()
                            + " | Điểm nhận: "
                            + request.getDestination();

            PharmacyAlert alert =
                    new PharmacyAlert(
                            "WAREHOUSE_EXPORT",
                            message
                    );

            redisAlertPublisher.publish(alert);

            System.out.println();
            System.out.println(
                    "===== XUAT KHO THANH CONG ====="
            );

            System.out.println(
                    "Thuoc       : " + drug.getName()
            );

            System.out.println(
                    "Stock cu    : " + oldStock
            );

            System.out.println(
                    "So luong xuat: " + quantityExport
            );

            System.out.println(
                    "Stock moi   : " + drug.getStock()
            );

            System.out.println(
                    "Da xoa cache medicines::" + drugId
            );

            System.out.println(
                    "Da gui thong bao toi bo phan van chuyen"
            );

            System.out.println(
                    "================================"
            );

            return "Đã hoàn thành xuất kho và thông báo đến đội vận chuyển";

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            return "Không thể lấy Distributed Lock";

        } finally {

            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}