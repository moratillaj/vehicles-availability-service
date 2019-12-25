package com.meep.vehicles.availability.repository;

import com.meep.vehicles.availability.model.PollingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PollingInfoRepository extends JpaRepository<PollingInfo, Long> {

    @Query(nativeQuery = true,
            value = "select polling_timestamp from polling_info order by polling_timestamp desc limit 2")
    List<PollingInfo> findLastPollingInfos();

}
