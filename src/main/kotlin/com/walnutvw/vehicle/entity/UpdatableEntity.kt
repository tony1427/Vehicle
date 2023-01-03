package com.walnutvw.vehicle.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.springframework.data.annotation.LastModifiedDate
import java.time.OffsetDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class UpdatableEntity {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id")
    var id: UUID? = null

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    val createdAt: OffsetDateTime = OffsetDateTime.now()

    @Column(name = "updated_at")
    @LastModifiedDate
    var updatedAt: OffsetDateTime = OffsetDateTime.now()


    override fun equals(other: Any?): Boolean {
        if(this === other) return true

        if(other !is UpdatableEntity) return false

        if(id != other.id) return false
        if(createdAt != other.createdAt) return false
        if(updatedAt != other.updatedAt) return false

        return true
    }

    override fun toString() = "Entity of type ${this.javaClass.name} with id: $id"

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + createdAt.hashCode()
        result = 31 * result + updatedAt.hashCode()
        return result
    }
}