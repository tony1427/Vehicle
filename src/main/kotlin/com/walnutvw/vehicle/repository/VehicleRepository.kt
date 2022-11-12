package com.walnutvw.vehicle.repository

import com.walnutvw.vehicle.entity.VehicleEntity
import org.springframework.stereotype.Repository

@Repository
interface VehicleRepository: BaseRepository<VehicleEntity> {
}