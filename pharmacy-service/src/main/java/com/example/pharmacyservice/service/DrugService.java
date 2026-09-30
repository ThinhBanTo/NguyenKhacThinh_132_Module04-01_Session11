package com.example.pharmacyservice.service;

import com.example.pharmacyservice.entity.Drug;
import com.example.pharmacyservice.repo.DrugRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class DrugService {
    private final DrugRepository drugRepository;
    private final RedissonClient redissonClient;

    @Cacheable(value = "medicines", key = "#id")
    public Drug getDrugById(Long id) {
        System.out.println(
                ">>> DANG TRUY VAN DATABASE CHO DRUG ID = " + id
        );

        return drugRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Khong tim thay thuoc ID = " + id
                        )
                );
    }
    @Transactional
    @CacheEvict(value = "medicines", key = "#id")
    public Drug updateDrug(Long id, Drug request) {

        Drug drug = drugRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Khong tim thay thuoc ID = " + id
                        )
                );

        drug.setName(request.getName());
        drug.setPrice(request.getPrice());
        drug.setStock(request.getStock());

        if (request.getExpiryDate() != null) {
            drug.setExpiryDate(request.getExpiryDate());
        }

        if (request.getStatus() != null) {
            drug.setStatus(request.getStatus());
        }

        Drug saved = drugRepository.save(drug);

        System.out.println(
                ">>> UPDATE DRUG ID = " + id
                        + " -> XOA CACHE medicines::" + id
        );

        return saved;
    }
    public String sellMedicine(Long id) {

        String lockKey = "lock:medicine:" + id;

        RLock lock =
                redissonClient.getLock(lockKey);

        boolean locked = false;

        try {

            locked = lock.tryLock(
                    3,
                    5,
                    TimeUnit.SECONDS
            );

            if (!locked) {
                return "Sản phẩm đang được xử lý, vui lòng thử lại sau";
            }

            Drug drug = drugRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Không tìm thấy thuốc ID = " + id
                            )
                    );

            if (drug.getStock() == null || drug.getStock() <= 0) {

                System.out.println(
                        ">>> SẢN PHẨM ĐÃ HẾT HÀNG"
                );

                return "Sản phẩm đã hết hàng!";
            }

            Integer oldStock = drug.getStock();

            drug.setStock(oldStock - 1);

            drugRepository.saveAndFlush(drug);

            System.out.println(
                    ">>> BÁN THÀNH CÔNG"
                            + " | drugId = " + id
                            + " | stock cũ = " + oldStock
                            + " | stock mới = " + drug.getStock()
            );

            return "Thanh toán thành công thuốc: "
                    + drug.getName();

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            return "Không lấy được lock";

        } finally {

            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
