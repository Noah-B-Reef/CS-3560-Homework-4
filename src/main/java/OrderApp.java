import entity.Address;
import entity.Customer;
import entity.Order;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class OrderApp extends JFrame{
    private JLabel Number;
    private JTextField txtNumber;
    private JTextField txtDate;
    private JLabel Date;
    private JLabel Customer;
    private JComboBox comboCustomer;
    private JLabel Item;
    private JLabel Price;
    // Add items to ComboBox

    private JComboBox comboItem;
    private JTextField txtPrice;
    private JButton searchButton;
    private JButton updateButton;
    private JButton addButton;
    private JButton deleteButton;
    private JPanel orderPanel;

    public OrderApp() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        // Add Customers to Combobox
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<entity.Customer> cr = cb.createQuery(Customer.class);
        Root<Customer> root = cr.from(Customer.class);
        cr.select(root);
        TypedQuery<Customer> query = entityManager.createQuery(cr);
        List<Customer> customers = query.getResultList();



        for (int i = 0; i < customers.toArray().length; i++)
        {
            comboCustomer.addItem(customers.get(i).getName());
        }

        // Add items to ComboBox
        String items[] = {"Caesar Salad", "Greek Salad" , "Cobb Salad"};

        for (int i = 0; i < items.length; i++)
        {
            comboItem.addItem(items[i]);
        }



        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                entityManager.close();
                entityManagerFactory.close();
                System.exit(0);
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    transaction.begin();
                    Integer number = Integer.parseInt(txtNumber.getText());
                    txtNumber.setText("");

                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                    java.util.Date date = sdf1.parse(txtDate.getText());
                    java.sql.Date dateSQL = new java.sql.Date(date.getTime());
                    txtDate.setText("");

                    String item = comboItem.getSelectedItem().toString();

                    Double price = Double.parseDouble(txtPrice.getText());
                    txtPrice.setText("");



                    Order order = new Order(number, dateSQL, item, price);



                    order.setCustomerByCustomerId(customers.get(comboCustomer.getSelectedIndex()));



                    entityManager.merge(order);
                    transaction.commit();

                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                } finally {
                    JOptionPane.showMessageDialog(orderPanel, "Order successfully included!");

                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String number = JOptionPane.showInputDialog(orderPanel, "Enter Order Number", null);
                try {
                    transaction.begin();
                    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                    CriteriaQuery<Order> cr = cb.createQuery(Order.class);
                    Root<Order> root = cr.from(Order.class);
                    cr.select(root).where(root.get("number").in(Integer.parseInt(number)));

                    TypedQuery<Order> query = entityManager.createQuery(cr);
                    List<Order> results = query.getResultList();
                    Order order = results.get(0);

                    Customer customer = order.getCustomerByCustomerId();

                    txtNumber.setText(String.valueOf(order.getNumber()));
                    txtDate.setText(order.getDate().toString());
                    comboCustomer.setSelectedItem(customer.getName());
                    comboItem.setSelectedItem(order.getItem());
                    txtPrice.setText(String.valueOf(order.getPrice()));

                }catch(Exception err)
                {
                    JOptionPane.showMessageDialog(orderPanel, "No records found!");
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    transaction.begin();
                    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                    CriteriaQuery<Order> cr = cb.createQuery(Order.class);
                    Root<Order> root = cr.from(Order.class);
                    cr.select(root).where(root.get("number").in(Integer.parseInt(txtNumber.getText())));

                    TypedQuery<Order> query = entityManager.createQuery(cr);
                    List<Order> results = query.getResultList();
                    Order order = results.get(0);

                    entityManager.remove(order);
                    transaction.commit();

                    txtNumber.setText("");
                    txtDate.setText("");
                    txtPrice.setText("");

                }catch(Exception err)
                {
                    JOptionPane.showMessageDialog(orderPanel, "Order successfully deleted!");
                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    transaction.begin();
                    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                    CriteriaQuery<Order> cr = cb.createQuery(Order.class);
                    Root<Order> root = cr.from(Order.class);
                    cr.select(root).where(root.get("number").in(Integer.parseInt(txtNumber.getText())));

                    TypedQuery<Order> query = entityManager.createQuery(cr);
                    List<Order> results = query.getResultList();
                    Order order = results.get(0);

                    Integer number = Integer.parseInt(txtNumber.getText());
                    txtNumber.setText("");

                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                    java.util.Date date = sdf1.parse(txtDate.getText());
                    java.sql.Date dateSQL = new java.sql.Date(date.getTime());
                    txtDate.setText("");

                    String item = comboItem.getSelectedItem().toString();

                    Double price = Double.parseDouble(txtPrice.getText());
                    txtPrice.setText("");


                    order.setNumber(number);
                    order.setDate(dateSQL);
                    order.setItem(item);
                    order.setPrice(price);
                    order.setCustomerByCustomerId(customers.get(comboCustomer.getSelectedIndex()));

                    entityManager.merge(order);
                    transaction.commit();

                    txtNumber.setText("");
                    txtDate.setText("");
                    txtPrice.setText("");

                }catch(Exception err)
                {
                    JOptionPane.showMessageDialog(orderPanel, "No Records!");
                }finally {
                    JOptionPane.showMessageDialog(orderPanel, "Order data successfully updated!");

                }
            }
        });
    }
    public static void main(String[] args)
    {
        OrderApp demo = new OrderApp();
        demo.setContentPane(demo.orderPanel);
        demo.setTitle("Order");
        demo.setSize(700, 500);
        demo.setVisible(true);
    }
}
