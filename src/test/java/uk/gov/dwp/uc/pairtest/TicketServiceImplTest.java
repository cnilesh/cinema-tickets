package uk.gov.dwp.uc.pairtest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;
import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;
import uk.gov.dwp.uc.pairtest.validator.TicketValidator;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TicketServiceImplTest {
    @Mock
    private TicketPaymentService ticketPaymentService;

    @Mock
    private SeatReservationService seatReservationService;

    @Mock
    private TicketValidator ticketValidator;

    private TicketService ticketService;

    @Before
    public void setup() {
        ticketService = new TicketServiceImpl(ticketPaymentService, seatReservationService, ticketValidator);
    }

    @Test
    public void testPurchaseTicketsForHappyPath() throws Exception {
        when(ticketValidator.validate(anyLong(), any())).thenReturn(List.of());
        int totalPrice = Whitebox.invokeMethod(ticketService, "calculatePrice", getTicketTypeRequests()[0], getTicketTypeRequests()[1],
                getTicketTypeRequests()[2]);
        int noOfSeatsToBeReserved = Whitebox.invokeMethod(ticketService, "calculateSeatsToBeBooked", getTicketTypeRequests()[0], getTicketTypeRequests()[1],
                getTicketTypeRequests()[2]);
        assertEquals(60, totalPrice);
        assertEquals(4, noOfSeatsToBeReserved);
    }

    @Test(expected = InvalidPurchaseException.class)
    public void testPurchaseTicketWhenFailedValidation() {
        when(ticketValidator.validate(anyLong(), any())).thenReturn(List.of("No Adults"));
        ticketService.purchaseTickets(123L, getTicketTypeRequests()[0]);
    }

    private TicketTypeRequest[] getTicketTypeRequests() {
        TicketTypeRequest[] ticketTypeRequests = new TicketTypeRequest[3];
        TicketTypeRequest ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 2);
        ticketTypeRequests[0] = ticketTypeRequest;
        ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.CHILD, 2);
        ticketTypeRequests[1] = ticketTypeRequest;
        ticketTypeRequest = new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 5);
        ticketTypeRequests[2] = ticketTypeRequest;
        return ticketTypeRequests;
    }
}
