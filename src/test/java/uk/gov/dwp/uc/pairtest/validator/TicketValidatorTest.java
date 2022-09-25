package uk.gov.dwp.uc.pairtest.validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TicketValidatorTest {

    private final TicketValidator ticketValidator = new TicketValidator();

    @Test
    public void testValidPath() {
        List<String> errors = ticketValidator.validate(123L, getTicketTypeRequests()[0]);
        assertEquals(0, errors.size());
    }

    @Test
    public void testIncorrectAccountId() {
        List<String> errors = ticketValidator.validate(0L, getTicketTypeRequests()[0]);
        assertEquals(1, errors.size());
        assertEquals("Invalid Account Id, cannot be less than or equals to 0.", errors.get(0));
    }

    @Test
    public void testBookingwithoutAdult() {
        List<String> errors = ticketValidator.validate(1L, getTicketTypeRequests()[1], getTicketTypeRequests()[2]);
        assertEquals(1, errors.size());
        assertEquals("There should atleast be 1 adult present in the tickets requested", errors.get(0));
    }

    @Test
    public void testWhenMoreThan20() {
        List<String> errors = ticketValidator.validate(1L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 19),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 2));
        assertEquals(1, errors.size());
        assertEquals("Invalid ticket type request, cannot be more than 20 or less than or equals to 0.", errors.get(0));
    }

    @Test
    public void testWhen0() {
        List<String> errors = ticketValidator.validate(1L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 0),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 0));
        assertEquals(1, errors.size());
        assertEquals("Invalid ticket type request, cannot be more than 20 or less than or equals to 0.", errors.get(0));
    }

    @Test
    public void testWhenAdultPresentButNoOfTickets0() {
        List<String> errors = ticketValidator.validate(1L, new TicketTypeRequest(TicketTypeRequest.Type.ADULT, 0),
                new TicketTypeRequest(TicketTypeRequest.Type.INFANT, 5));
        assertEquals(1, errors.size());
        assertEquals("There should atleast be 1 adult present in the tickets requested", errors.get(0));
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
