package dao;

import entities.Coupon;
import entities.Customer;;
import pool.ConnectionPool;

import java.sql.*;
import java.util.List;

public class CustomersDBDAO implements CustomersDAO {

    private ConnectionPool pool = ConnectionPool.getInstance();

    @Override
    public boolean isCustomerExists(String email, String password)  {

        boolean isExist = true;

        String sql = "SELECT * FROM coupon.customers WHERE EMAIL = ? AND PASSWORD = ?;";
        Connection connection = pool.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);

            ResultSet resultSet = pstmt.executeQuery();

            while (!resultSet.next()) {
                isExist = false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }

        return isExist;
    }

    @Override
    public void addCustomer(Customer customer) {

        String sql = "INSERT INTO coupon.customers (FIRST_NAME, LAST_NAME, EMAIL, PASSWORD) VALUES (? ,?, ?, ?);";
        Connection connection = pool.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getLastName());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getPassword());

            pstmt.executeUpdate();

            ResultSet resultSet = pstmt.getGeneratedKeys();

            if (resultSet.next()) {
                customer.setID(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
    }

    @Override
    public void updateCustomer(Customer customer) {

        String sql = "UPDATE coupon.customers SET FIRST_NAME = ?, LAST_NAME = ?, EMAIL = ?, PASSWORD = ?;";
        Connection connection = pool.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, customer.getFirstName());
            pstmt.setString(2, customer.getLastName());
            pstmt.setString(3, customer.getEmail());
            pstmt.setString(4, customer.getPassword());

            pstmt.executeUpdate();
            customer = new Customer(customer.getID(), customer.getFirstName(), customer.getLastName(), customer.getEmail(), customer.getPassword(), customer.getCoupons());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
    }

    @Override
    public void deleteCustomer(int customerID) {

        Customer customer = null;

        String sql = "DELETE FROM coupon.customers WHERE ID = ?;";
        Connection connection = pool.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, customerID);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
    }

    @Override
    public List<Customer> getAllCustomers() {

        List<Customer> customerList = null;

        String sql = "SELECT * FROM coupon.customers;";
        Connection connection = pool.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()){
                int id = resultSet.getInt(1);
                String firstName = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                String email = resultSet.getString(4);
                String password = resultSet.getString(5);
                List<Coupon> coupons = (List<Coupon>) resultSet.getArray(6);

                Customer customer = new Customer(id, firstName, lastName, email, password, coupons);
                customerList.add(customer);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }

        return customerList;
    }

    @Override
    public Customer getOneCustomer(int customerID) {

        Customer customer = null;

        String sql = "SELECT * FROM coupon.customers WHERE ID = ?;";
        Connection connection = pool.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, customerID);

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                String firstName = resultSet.getString(2);
                String lastName = resultSet.getString(3);
                String email = resultSet.getString(4);
                String password = resultSet.getString(5);
                List<Coupon> coupons = (List<Coupon>) resultSet.getArray(6);

                customer = new Customer(customerID, firstName, lastName, email, password, coupons);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }

        return customer;
    }

    public Customer getCustomerByName(String firstName, String lastName) {

        Customer customer = null;

        String sql = "SELECT * FROM coupon.customers WHERE FIRST_NAME = ? AND LAST_NAME = ?;";
        Connection connection = pool.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String email = resultSet.getString(4);
                String password = resultSet.getString(5);
                List<Coupon> coupons = (List<Coupon>) resultSet.getArray(6);

                customer = new Customer(id, firstName, lastName, email, password, coupons);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }

        return customer;
    }
}
