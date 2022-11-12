package com.walnutvw.vehicle.controller

import com.ninjasquad.springmockk.MockkBean
import com.walnutvw.vehicle.controller.ApplicationConstants.VEHICLES
import com.walnutvw.vehicle.exception.BadActionException
import com.walnutvw.vehicle.exception.NotFoundException
import com.walnutvw.vehicle.model.VehicleRepresentation
import com.walnutvw.vehicle.service.VehicleService
import com.walnutvw.vehicle.util.asJsonString
import com.walnutvw.vehicle.util.createRandomVehicle
import com.walnutvw.vehicle.util.createRandonVehicleMap

import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@ExtendWith(SpringExtension::class)
@WebMvcTest(VehicleController::class)
internal class VehicleControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockkBean
    private lateinit var vehicleService: VehicleService

    @Test
    fun `get vehicle by id`() {
        val vehicle = createRandomVehicle()
        every { vehicleService.getVehicle("someId") } returns vehicle

        mockMvc.perform(MockMvcRequestBuilders.get("/$VEHICLES/someId"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.vin").value(vehicle.vin))
            .andExpect(jsonPath("$.make").value(vehicle.make))
            .andExpect(jsonPath("$.model").value(vehicle.model))
            .andExpect(jsonPath("$.purchaseDate").value(vehicle.purchaseDate.toString()))
            .andReturn()
    }

    @Test
    fun `get vehicle by id returns not found exception`() {
        every { vehicleService.getVehicle("someId") } throws (NotFoundException())

        mockMvc.perform(MockMvcRequestBuilders.get("/$VEHICLES/someId"))
            .andExpect(status().isNotFound)
            .andReturn()
    }

    @Test
    fun `create vehicle should return created status`() {
        val vehicle = createRandomVehicle()
        every { vehicleService.createVehicle(vehicle) }
            .returns(VehicleRepresentation("vin", "make", "model", LocalDate.now()).apply { id = "someId" })

        val result: MockHttpServletResponse =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/$VEHICLES")
                    .content(asJsonString(vehicle))
                    .contentType(MediaType.APPLICATION_JSON)
            )
                .andExpect(status().isCreated)
                .andReturn()
                .response

        assertThat(result.getHeaders("Location")).contains("http://localhost/$VEHICLES/someId")
    }

    @Test
    fun `update an existing vehicle`() {
        val vehicle = createRandomVehicle()

        every { vehicleService.updateVehicle("someId", vehicle) }
            .returns(VehicleRepresentation("vin", "make", "model", LocalDate.now()).apply { id = "someId" })

        mockMvc.perform(
            MockMvcRequestBuilders.put("/$VEHICLES/someId")
                .content(asJsonString(vehicle)).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
    }

    @Test
    fun `update an existing vehicle returns not found `() {
        val vehicle = createRandomVehicle()

        every { vehicleService.updateVehicle("someId", vehicle) }.throws(NotFoundException())

        mockMvc.perform(
            MockMvcRequestBuilders.put("/$VEHICLES/someId")
                .content(asJsonString(vehicle)).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound)

            .andReturn()
    }

    @Test
    fun `update existing vehicle using Patch`() {
        val vehicle = createRandonVehicleMap()
        every { vehicleService.updateVehicle("someId", vehicle) }
            .returns (VehicleRepresentation
                ("vin", "make", "model", LocalDate.now()).apply { id = "someId" })

        mockMvc.perform (
            MockMvcRequestBuilders.patch("/$VEHICLES/someId")
                .content(asJsonString(vehicle)).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
    }

    @Test
    fun `update existing vehicle using Patch throws Bad Request`() {
        val vehicle = createRandonVehicleMap()
        every { vehicleService.updateVehicle("someId", vehicle) }
            .throws(BadActionException("Bad Stuff Occurred"))

        mockMvc.perform (
            MockMvcRequestBuilders.patch("/$VEHICLES/someId")
                .content(asJsonString(vehicle)).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest)
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
    }

    @Test
    fun deleteVehicles() {
    }
}