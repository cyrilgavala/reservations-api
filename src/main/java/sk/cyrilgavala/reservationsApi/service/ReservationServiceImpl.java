package sk.cyrilgavala.reservationsApi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sk.cyrilgavala.reservationsApi.exception.ReservationException;
import sk.cyrilgavala.reservationsApi.mapper.ReservationMapper;
import sk.cyrilgavala.reservationsApi.model.Reservation;
import sk.cyrilgavala.reservationsApi.repository.ReservationRepository;
import sk.cyrilgavala.reservationsApi.web.request.CreateReservationRequest;
import sk.cyrilgavala.reservationsApi.web.request.UpdateReservationRequest;
import sk.cyrilgavala.reservationsApi.web.response.ReservationResponse;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {
	private static final String RESERVATION_UNPROCESSABLE_START_DATE_IS_AFTER_END_DATE = "Reservation unprocessable: start date is after end date";
	private static final String RESERVATION_UNPROCESSABLE_COVERS_ANOTHER_RESERVATION = "Reservation unprocessable: covers another reservation";
	private final ReservationRepository repository;
	private final ReservationMapper mapper;

	@Inject
	public ReservationServiceImpl(ReservationRepository repository, ReservationMapper mapper) {
		this.repository = repository;
		this.mapper = mapper;
	}

	@Override
	public ReservationResponse createReservation(CreateReservationRequest request) {
		validateDates(request.getReservationFrom(), request.getReservationTo());
		Reservation mappedReservation = mapper.createRequestToModel(request);
		Reservation savedReservation = repository.insert(mappedReservation);
		return mapper.modelToResponse(savedReservation);
	}

	@Override
	public ReservationResponse updateReservation(UpdateReservationRequest request) {
		validateDates(request.getReservationFrom(), request.getReservationTo());
		Reservation loadedReservation = repository.findByUuid(request.getUuid());
		Reservation updatedReservation = mapper.updateRequestToModel(loadedReservation, request);
		Reservation savedReservation = repository.save(updatedReservation);
		return mapper.modelToResponse(savedReservation);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReservationResponse> getAllReservations() {
		return repository.findAll(Sort.by("reservationFrom"))
			.stream()
			.map(mapper::modelToResponse)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ReservationResponse> getAllReservationsForUsername(String username) {
		return repository.findAllByReservationFor(username, Sort.by("reservationFrom"))
			.stream()
			.map(mapper::modelToResponse)
			.collect(Collectors.toList());
	}

	@Override
	public void deleteReservation(String reservationUuid) {
		repository.deleteById(reservationUuid);
	}

	private void validateDates(LocalDateTime from, LocalDateTime to) {
		if (from.isAfter(to)) {
			log.error(RESERVATION_UNPROCESSABLE_START_DATE_IS_AFTER_END_DATE);
			throw new ReservationException(RESERVATION_UNPROCESSABLE_START_DATE_IS_AFTER_END_DATE);
		}
		List<Reservation> reservations = repository.findAllBetween(from, to);
		reservations.stream()
			.findFirst()
			.ifPresent(reservation -> {
				log.error(RESERVATION_UNPROCESSABLE_COVERS_ANOTHER_RESERVATION);
				throw new ReservationException(RESERVATION_UNPROCESSABLE_COVERS_ANOTHER_RESERVATION);
			});
	}
}
