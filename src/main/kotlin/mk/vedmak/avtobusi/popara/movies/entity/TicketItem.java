package mk.vedmak.avtobusi.popara.movies.entity;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node
public class TicketItem {

    /**
     * Neo4j internally generated id.
     */
    @Id
    @GeneratedValue
    private Long id;

    private final String product;

    private final Double netAmount;

    private final Long units;

    @Relationship("FOR_PRODUCT")
    private final Product relatedProduct;

    TicketItem(String product, Double netAmount, Long units, Product relatedProduct) {
        this.product = product;
        this.netAmount = netAmount;
        this.units = units;
        this.relatedProduct = relatedProduct;
    }

    public Long getId() {
        return id;
    }

    public String getProduct() {
        return product;
    }

    public Double getNetAmount() {
        return netAmount;
    }

    public Long getUnits() {
        return units;
    }

    public Product getRelatedProduct() {
        return relatedProduct;
    }

    @Override public String toString() {
        return "TicketItem{" +
            "product='" + product + '\'' +
            ", netAmount=" + netAmount +
            ", units=" + units +
            ", relatedProduct=" + relatedProduct +
            '}';
    }
}