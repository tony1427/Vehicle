package com.walnutvw.vehicle.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.walnutvw.vehicle.entity.VehicleEntity
import com.walnutvw.vehicle.exception.BadActionException
import com.walnutvw.vehicle.exception.NotFoundException
import com.walnutvw.vehicle.model.VehicleRepresentation
import com.walnutvw.vehicle.repository.VehicleRepository
import com.walnutvw.vehicle.util.createRandomVehicle
import com.walnutvw.vehicle.util.createRandonVehicleMap
import com.walnutvw.vehicle.utils.ApplicationFactory
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.modelmapper.ModelMapper
import java.util.*

internal class VehicleServiceTest {

    @MockK
    private lateinit var vehicleRepository: VehicleRepository

//    @MockK
//    private lateinit var modelMapper: ModelMapper

//    @MockK
//    private lateinit var applicationFactory: ApplicationFactory

    @MockK
    private lateinit var vehicleServiceHelper: VehicleServiceHelper

    private lateinit var vehicleService: VehicleService


    @BeforeEach
    fun setUp(){
        MockKAnnotations.init(this, relaxUnitFun = true)
        vehicleService = VehicleService(
            vehicleRepository,
            ModelMapper(),
            ApplicationFactory(),
            vehicleServiceHelper,
            ObjectMapper()
        )
    }

    @Test
    fun `get vehicle by id should return a vehicle representation`() {
        val vehicle = createRandomVehicle()
        val uuid = UUID.randomUUID()

        every { vehicleRepository.findById(uuid) }.returns(Optional.of(vehicle.toEntity()))

        val result = vehicleService.getVehicle(uuid.toString())

        assertThat(result).isNotNull
        assertThat(result).isEqualTo(vehicle)
        verify(exactly = 1) { vehicleRepository.findById(uuid) }
    }

    @Test
    fun `get vehicle by id throws not found exception`() {
        val vehicle = createRandomVehicle()
        val uuid = UUID.randomUUID()

        every { vehicleRepository.findById(uuid) }.returns(Optional.empty())

        assertThrows(NotFoundException::class.java){ vehicleService.getVehicle(uuid.toString())}
        verify(exactly = 1) { vehicleRepository.findById(uuid) }
    }

    @Test
    fun `create vehicle`(){
        val uuid = UUID.randomUUID()
        val vehicle = createRandomVehicle()
        val vehicleEntity = vehicle.toEntity()
        vehicleEntity.id = uuid

        every { vehicleRepository.save(any<VehicleEntity>())}.returns(vehicleEntity)

        val createVehicle = vehicleService.createVehicle(vehicle)
        assertThat(createVehicle).isEqualTo(vehicle)

        val capturedEntity = slot<VehicleEntity>()
        verify (exactly = 1){ vehicleRepository.save(capture(capturedEntity)) }
        assertThat(capturedEntity.captured.make).isEqualTo(vehicleEntity.make)
        assertThat(capturedEntity.captured.model).isEqualTo(vehicleEntity.model)
        assertThat(capturedEntity.captured.vin).isEqualTo(vehicleEntity.vin)

    }

    @Test
    fun `update vehicle throws bad action exception`(){
        val uuid = UUID.randomUUID()
        val map = mapOf<String, String>()

        assertThrows(BadActionException::class.java){vehicleService.updateVehicle(uuid.toString(), map)}
    }

    @Test
    fun `update vehicle throws not found exception`(){
        val uuid = UUID.randomUUID()
        val vehicleMap = createRandonVehicleMap()

        every { vehicleRepository.findById(uuid) }.returns(Optional.empty())

        assertThrows(NotFoundException::class.java){vehicleService.updateVehicle(uuid.toString(), vehicleMap)}
        verify(exactly = 1) { vehicleRepository.findById(uuid) }
    }

    @Test
    fun `update vehicle throws bad action exception due to bad field`(){
        val uuid = UUID.randomUUID()
        val vehicleEntity = createRandomVehicle().toEntity()
        vehicleEntity.id = uuid
        val vehicleMap = createRandonVehicleMap().toMutableMap()
        vehicleMap["fakeField"] = "fakeField"

        every { vehicleRepository.findById(uuid) }.returns(Optional.of(vehicleEntity))
        every { vehicleServiceHelper.validateRequest(any(), any())}.returns(listOf("fakeField"))

        assertThrows(BadActionException::class.java,
            {vehicleService.updateVehicle(uuid.toString(), vehicleMap)},
            "Fields don't exist fakeField")
        verify(exactly = 1) { vehicleRepository.findById(uuid) }
    }

    @Test
    fun `update vehicle throws bad action exception due to bad fields`(){
        val uuid = UUID.randomUUID()
        val vehicleEntity = createRandomVehicle().toEntity()
        vehicleEntity.id = uuid
        val vehicleMap = createRandonVehicleMap().toMutableMap()
        vehicleMap["fakeField1"] = "fakeField1"
        vehicleMap["fakeField2"] = "fakeField2"

        every { vehicleRepository.findById(uuid) }.returns(Optional.of(vehicleEntity))
        every { vehicleServiceHelper.validateRequest(any(), any())}.returns(listOf("fakeField1", "fakeField2"))

        assertThrows(BadActionException::class.java,
            {vehicleService.updateVehicle(uuid.toString(), vehicleMap)},
            "Fields don't exist fakeField1, fakeField2")
        verify(exactly = 1) { vehicleRepository.findById(uuid) }
    }

}