package mk.vedmak.avtobusi.popara.movies.entity;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.ZonedDateTime;
import java.util.List;

@Node
class Ticket {

    /**
     * Assigned id (an actual property and not the identy function).
     */
    @Id
    private final Long id;

    @Property("datetime")
    private final ZonedDateTime purchasedAt;

    @Relationship("HAS_TICKETITEM")
    private final List<TicketItem> items;

    Ticket(Long id, ZonedDateTime purchasedAt, List<TicketItem> items) {
        this.id = id;
        this.purchasedAt = purchasedAt;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public ZonedDateTime getPurchasedAt() {
        return purchasedAt;
    }

    public List<TicketItem> getItems() {
        return items;
    }

    @Override public String toString() {
        return "Ticket{" +
            "purchasedAt=" + purchasedAt +
            ", items=" + items +
            '}';
    }
}