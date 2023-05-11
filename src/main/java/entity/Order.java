package entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name="order", schema = "ordering")
public class Order {
    @Id
    @Column(name = "number")
    private int number;
    @Basic
    @Column(name = "date")
    private Date date;
    @Basic
    @Column(name = "item")
    private String item;
    @Basic
    @Column(name = "price")
    private double price;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customerByCustomerId;

    public Order(Integer number, Date date, String item, double price)
    {
        this.number = number;
        this.date = date;
        this.item = item;
        this.price = price;
    }
    public Order(){}
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCustomerId() {
        return customerByCustomerId.getId();
    }

    public void setCustomerId(int customerId) {
        this.customerByCustomerId.setId(customerId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return number == order.number && Double.compare(order.price, price) == 0 && customerByCustomerId.getId() == order.customerByCustomerId.getId() && Objects.equals(date, order.date) && Objects.equals(item, order.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, date, item, price, customerByCustomerId.getId());
    }

    public Customer getCustomerByCustomerId() {
        return customerByCustomerId;
    }

    public void setCustomerByCustomerId(Customer customerByCustomerId) {
        this.customerByCustomerId = customerByCustomerId;
    }
}
