import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import jakarta.persistence.*;
import entity.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.query.Query;

public class CustomerApp extends JFrame{
    private JPanel customerPanel;
    private JTextField txtName;
    private JLabel Phone;
    private JTextField txtPhone;
    private JLabel Email;
    private JTextField txtEmail;
    private JLabel AddressLabel;
    private JLabel Street;
    private JLabel City;
    private JLabel State;
    private JLabel Zip;
    private JTextField txtState;
    private JTextField txtZip;
    private JTextField txtStreet;
    private JTextField txtCity;
    private JButton addButton;
    private JButton searchButton;
    private JButton deleteButton;
    private JButton updateButton;

    public CustomerApp() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            try {
                transaction.begin();
                String name = txtName.getText();
                txtName.setText("");
                String phone = txtPhone.getText();
                txtPhone.setText("");
                String email = txtEmail.getText();
                txtEmail.setText("");
                String street = txtStreet.getText();
                txtStreet.setText("");
                String city = txtCity.getText();
                txtCity.setText("");
                String state = txtState.getText();
                txtState.setText("");
                String zip = txtZip.getText();
                txtZip.setText("");

                Customer customer = new Customer(name, phone, email);
                Address address = new Address(street, city, state, zip);

                customer.setAddressByAddressId(address);


                entityManager.persist(customer);
                transaction.commit();

            }finally {
                JOptionPane.showMessageDialog(customerPanel, "Customer successfully included!");

            }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = JOptionPane.showInputDialog(customerPanel, "Enter Customer Name", null);
                try {
                    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                    CriteriaQuery<Customer> cr = cb.createQuery(Customer.class);
                    Root<Customer> root = cr.from(Customer.class);
                    cr.select(root).where(root.get("name").in(name));

                    TypedQuery<Customer> query = entityManager.createQuery(cr);
                    List<Customer> results = query.getResultList();
                    Customer customer = results.get(0);

                    Address address = customer.getAddressByAddressId();

                    txtName.setText(customer.getName());
                    txtPhone.setText(customer.getPhone());
                    txtEmail.setText(customer.getEmail());
                    txtStreet.setText(address.getStreet());
                    txtState.setText(address.getState());
                    txtCity.setText(address.getCity());
                    txtZip.setText(address.getZipCode());
                }catch(Exception err)
                {
                    JOptionPane.showMessageDialog(customerPanel, "No records found!");
                }
            }
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                if(transaction.isActive()){
                    transaction.rollback();
                }
                entityManager.close();
                entityManagerFactory.close();
                System.exit(0);
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    transaction.begin();
                    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                    CriteriaQuery<Customer> cr = cb.createQuery(Customer.class);
                    Root<Customer> root = cr.from(Customer.class);
                    cr.select(root).where(root.get("name").in(txtName.getText()));

                    TypedQuery<Customer> query = entityManager.createQuery(cr);
                    List<Customer> results = query.getResultList();
                    Customer customer = results.get(0);
                    entityManager.remove(customer);
                    transaction.commit();

                    txtName.setText("");
                    txtPhone.setText("");
                    txtEmail.setText("");
                    txtStreet.setText("");
                    txtCity.setText("");
                    txtState.setText("");
                    txtZip.setText("");
                } catch (Exception err) {
                    JOptionPane.showMessageDialog(customerPanel, "User not in database!");
                }finally {
                    JOptionPane.showMessageDialog(customerPanel, "Customer successfully deleted!");

                }
            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    transaction.begin();
                    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                    CriteriaQuery<Customer> cr = cb.createQuery(Customer.class);
                    Root<Customer> root = cr.from(Customer.class);
                    cr.select(root).where(root.get("name").in(txtName.getText()));

                    TypedQuery<Customer> query = entityManager.createQuery(cr);
                    List<Customer> results = query.getResultList();
                    Customer customer = results.get(0);
                    Address address = customer.getAddressByAddressId();
                    String name = txtName.getText();
                    customer.setName(name);
                    txtName.setText("");
                    String phone = txtPhone.getText();
                    customer.setPhone(phone);
                    txtPhone.setText("");
                    String email = txtEmail.getText();
                    customer.setEmail(email);
                    txtEmail.setText("");
                    String street = txtStreet.getText();
                    address.setStreet(street);
                    txtStreet.setText("");
                    String city = txtCity.getText();
                    address.setCity(city);
                    txtCity.setText("");
                    String state = txtState.getText();
                    address.setState(state);
                    txtState.setText("");
                    String zip = txtZip.getText();
                    address.setZipCode(zip);
                    txtZip.setText("");

                    entityManager.merge(customer);
                    transaction.commit();

                } catch (Exception err) {
                    JOptionPane.showMessageDialog(customerPanel, "User not in database!");
                }finally {
                    JOptionPane.showMessageDialog(customerPanel, "Customer data successfully updated!");

                }
            }
        });
    }

    public static void main(String[] args)
    {
        CustomerApp demo = new CustomerApp();
        demo.setContentPane(demo.customerPanel);
        demo.setTitle("Customer");
        demo.setSize(700, 500);
        demo.setVisible(true);
    }
}
