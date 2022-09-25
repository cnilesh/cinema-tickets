package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.validator.TicketValidator;

import java.util.Arrays;
import java.util.List;


public class TicketServiceImpl implements TicketService {

    private final TicketPaymentService ticketPaymentService;
    private final SeatReservationService seatReservationService;
    private final TicketValidator ticketValidator;

    public TicketServiceImpl(TicketPaymentService ticketPaymentService, SeatReservationService seatReservationService, TicketValidator ticketValidator) {
        this.ticketPaymentService = ticketPaymentService;
        this.seatReservationService = seatReservationService;
        this.ticketValidator = ticketValidator;
    }

    /**
     * Should only have private methods other than the one below.
     */
    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
        List<String> errors = ticketValidator.validate(accountId, ticketTypeRequests);
        if(!errors.isEmpty()) {
            throw new InvalidPurchaseException(String.join(", ", errors));
        }
//        try {
            int totalPrice = calculatePrice(ticketTypeRequests);
            ticketPaymentService.makePayment(accountId, totalPrice);            // Assumption: Throws exception in case the user doesn't have enough balance
                                                                                // so seat booking is not done
            int seatsToBeBooked = calculateSeatsToBeBooked(ticketTypeRequests);
            seatReservationService.reserveSeat(accountId, seatsToBeBooked);     // Assumption: Throws SeatBookingException, so payment refunds
//        } catch (SeatBookingException e) {
//            ticketPaymentService.refundPayment(accountId, totalPrice);
//        }
    }

    private int calculatePrice(TicketTypeRequest... ticketTypeRequests) {
        return Arrays.stream(ticketTypeRequests).map(ticketTypeRequest -> ticketTypeRequest.getTicketType().getPrice() * ticketTypeRequest.getNoOfTickets())
                .mapToInt(Integer::intValue).sum();
    }

    private int calculateSeatsToBeBooked(TicketTypeRequest... ticketTypeRequests) {
        return Arrays.stream(ticketTypeRequests).map(ticketTypeRequest -> ticketTypeRequest.getTicketType().getSeatOccupied()
                * ticketTypeRequest.getNoOfTickets())
                .mapToInt(Integer::intValue).sum();
    }
}
