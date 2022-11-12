package com.walnutvw.vehicle.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.UUID

@NoRepositoryBean
interface BaseRepository<T>: JpaRepository<T, UUID> {
}