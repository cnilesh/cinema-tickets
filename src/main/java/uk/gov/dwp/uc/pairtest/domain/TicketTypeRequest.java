package uk.gov.dwp.uc.pairtest.domain;

/**
 * Immutable Object
 */
public final class TicketTypeRequest {

    private final int noOfTickets;
    private final Type type;

    public TicketTypeRequest(Type type, int noOfTickets) {
        this.type = type;
        this.noOfTickets = noOfTickets;
    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public Type getTicketType() {
        return type;
    }

    public enum Type {
        ADULT(20, 1), CHILD(10, 1) , INFANT(0, 0);
        private final int price;
        private final int seatOccupied;

        Type(int price, int seatOccupied) {
            this.price = price;
            this.seatOccupied = seatOccupied;
        }

        public int getPrice() {
            return price;
        }

        public int getSeatOccupied() {
            return seatOccupied;
        }
    }

}
