package uk.gov.dwp.uc.pairtest.validator;

import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TicketValidator {

    public List<String> validate(Long accountId, TicketTypeRequest... ticketTypeRequests) {
        List<String> errors = new ArrayList<>();
        errors.addAll(isValidAccountId(accountId));
        errors.addAll(validateTicketTypeRequest(ticketTypeRequests));
        return errors;
    }

    private List<String> isValidAccountId(Long accountId) {
        if (accountId <= 0) {
            return new ArrayList<>(List.of("Invalid Account Id, cannot be less than or equals to 0."));
        }
        return new ArrayList<>();
    }

    private List<String> validateTicketTypeRequest(TicketTypeRequest... ticketTypeRequests) {
        if(ticketTypeRequests == null || ticketTypeRequests.length == 0) {
            return new ArrayList<>(List.of("Invalid ticket type request, cannot be null or 0"));
        }
        int noOfTickets = Arrays.stream(ticketTypeRequests).map(TicketTypeRequest::getNoOfTickets).mapToInt(Integer::intValue)
                .sum();
        if(noOfTickets > 20 || noOfTickets <= 0) {
            return new ArrayList<>(List.of("Invalid ticket type request, cannot be more than 20 or less than or equals to 0."));
        }
        Set<TicketTypeRequest> adultTicketTypeRequest = Arrays.stream(ticketTypeRequests).filter(
                ticketTypeRequest -> ticketTypeRequest.getTicketType() == TicketTypeRequest.Type.ADULT
        ).filter(ticketTypeRequest -> ticketTypeRequest.getNoOfTickets() != 0).collect(Collectors.toSet());
        if (adultTicketTypeRequest.isEmpty()) {
            return List.of("There should atleast be 1 adult present in the tickets requested");
        }
        return new ArrayList<>();
    }
}
