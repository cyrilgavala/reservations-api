package sk.cyrilgavala.reservationsApi.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.cyrilgavala.reservationsApi.service.ReservationService;
import sk.cyrilgavala.reservationsApi.web.request.CreateReservationRequest;
import sk.cyrilgavala.reservationsApi.web.request.UpdateReservationRequest;
import sk.cyrilgavala.reservationsApi.web.response.ReservationResponse;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Tag(name = "Reservations.")
@RestController
@RequestMapping(value = "/api/reservation", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class ReservationRestController {

	private final ReservationService service;

	@Inject
	public ReservationRestController(ReservationService service) {
		this.service = service;
	}

	@Operation(summary = "Get reservations for particular user", description = "Get reservations for particular user when correct username provided.")
	@GetMapping("/{username}")
	public List<ReservationResponse> getAllReservationsForUser(@PathVariable @NotBlank String username) {
		return service.getAllReservationsForUsername(username);
	}

	@Operation(summary = "Get all reservations", description = "Get all reservations available.")
	@GetMapping
	public List<ReservationResponse> getAllReservations() {
		return service.getAllReservations();
	}

	@Operation(summary = "Create new reservation", description = "Create new reservation when valid information provided.")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ReservationResponse createReservation(@Valid @RequestBody CreateReservationRequest requestBody) {
		return service.createReservation(requestBody);
	}

	@Operation(summary = "Update reservation", description = "Update reservation when valid information provided.")
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ReservationResponse updateReservation(@Valid @RequestBody UpdateReservationRequest requestBody) {
		return service.updateReservation(requestBody);
	}

	@Operation(summary = "Delete reservation", description = "Delete reservation when valid UUID provided.")
	@DeleteMapping("/{reservationUuid}")
	public void deleteReservation(@PathVariable @NotBlank String reservationUuid) {
		service.deleteReservation(reservationUuid);
	}
}
