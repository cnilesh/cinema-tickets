Had to run my TicketServiceImplTest using the java arguments "--add-opens java.base/java.lang=ALL-UNNAMED"
--reference: https://stackoverflow.com/questions/41265266/how-to-solve-inaccessibleobjectexception-unable-to-make-member-accessible-m

The Rationale is to offload validation to a different service and have all validations there

Made the domain immutable but using a final class so that it cannot be inherited, removing setters and having set variables using constructor
