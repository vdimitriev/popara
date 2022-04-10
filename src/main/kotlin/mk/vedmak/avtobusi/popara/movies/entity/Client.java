package mk.vedmak.avtobusi.popara.movies.entity;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node
class Client {

    /**
     * Assigned id (an actual property and not the identy function).
     * Assigned means that is assigned from the client side and in this
     * case, assigned with a generated value, orchestrated by SDN 6.
     */
    @Id
    @GeneratedValue(generatorRef = "clientSequenceGenerator")
    private Long id;

    @Relationship(direction = Relationship.Direction.OUTGOING)
    private final List<Ticket> purchasedTickets;

    Client(List<Ticket> purchasedTickets) {
        this.purchasedTickets = purchasedTickets;
    }

    Long getId() {
        return id;
    }

    public List<Ticket> getPurchasedTickets() {
        return purchasedTickets;
    }

    @Override public String toString() {
        return "Client{" +
            "purchasedTickets=" + purchasedTickets +
            '}';
    }
}