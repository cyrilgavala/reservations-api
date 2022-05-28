package sk.cyrilgavala.reservationsApi.web.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sk.cyrilgavala.reservationsApi.web.DateTimeAware;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreateReservationRequest implements DateTimeAware, Serializable {

	private static final long serialVersionUID = -44348945472726119L;

	@JsonProperty
	@NotBlank
	private String reservationFor;
	@JsonProperty
	@NotNull
	@JsonFormat(pattern = DATE_TIME_FORMAT, shape = JsonFormat.Shape.STRING)
	private LocalDateTime reservationFrom;
	@JsonProperty
	@NotNull
	@JsonFormat(pattern = DATE_TIME_FORMAT, shape = JsonFormat.Shape.STRING)
	private LocalDateTime reservationTo;
	@JsonProperty
	@NotNull
	@JsonFormat(pattern = DATE_TIME_FORMAT, shape = JsonFormat.Shape.STRING)
	private LocalDateTime createdAt;

}
