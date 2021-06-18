package com.mysaving.discountsm;

import com.mysaving.discountsm.common.UUIDEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataRepository<T extends UUIDEntity> extends JpaRepository<T, UUID> {}
