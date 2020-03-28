package dao;

import entities.Coupon;
import pool.ConnectionPool;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class CouponsDBDAO implements CouponsDAO {

    private ConnectionPool pool = ConnectionPool.getInstance();

    @Override
    public void addCoupon(Coupon coupon) {

        String sql = "INSERT INTO coupon.coupons (TITLE, DESCRIPTION, START_DATE, END_DATE, AMOUNT, PRICE, IMAGE, COMPANY_ID, CATEGORY_ID) VALUES (? ,?, ?, ?, ?, ?, ?, ?, ?);";
        Connection connection = pool.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, coupon.getTitle());
            pstmt.setString(2, coupon.getDescription());
            pstmt.setDate(3, Date.valueOf(coupon.getStartDate()));
            pstmt.setDate(4, Date.valueOf(coupon.getEndDate()));
            pstmt.setInt(5, coupon.getAmount());
            pstmt.setDouble(6, coupon.getPrice());
            pstmt.setString(7, coupon.getImage());
            pstmt.setInt(8, coupon.getCompanyID());
            pstmt.setInt(9, coupon.getCategory());

            pstmt.executeUpdate();

            ResultSet resultSet = pstmt.getGeneratedKeys();

            if (resultSet.next()) {
                coupon.setID(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
    }

    @Override
    public void updateCoupon(Coupon coupon) {

        String sql = "UPDATE coupon.coupons SET TITLE = ?, DESCRIPTION = ?, START_DATE = ?, END_DATE = ?, AMOUNT = ?, PRICE = ?, IMAGE = ?, COMPANY_ID = ?, CATEGORY_ID = ?;";
        Connection connection = pool.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, coupon.getTitle());
            pstmt.setString(2, coupon.getDescription());
            pstmt.setDate(3, Date.valueOf(coupon.getStartDate()));
            pstmt.setDate(4, Date.valueOf(coupon.getEndDate()));
            pstmt.setInt(5, coupon.getAmount());
            pstmt.setDouble(6, coupon.getPrice());
            pstmt.setString(7, coupon.getImage());
            pstmt.setInt(8, coupon.getCompanyID());
            pstmt.setInt(9, coupon.getCategory());

            pstmt.executeUpdate();

            coupon = new Coupon(coupon.getID(), coupon.getCompanyID(), coupon.getCategory(),
                    coupon.getTitle(), coupon.getDescription(), coupon.getStartDate(),
                    coupon.getEndDate(), coupon.getAmount(), coupon.getPrice(), coupon.getImage());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
    }

    @Override
    public void deleteCoupon(int couponID) {

        String sql = "DELETE FROM coupon.coupons WHERE ID = ?;";
        Connection connection = pool.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, couponID);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
    }

    @Override
    public List<Coupon> getAllCoupons() {

        List<Coupon> couponList = null;

        String sql = "SELECT * FROM coupon.coupons;";
        Connection connection = pool.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()){
                int id = resultSet.getInt(1);
                String title = resultSet.getString(2);
                String description = resultSet.getString(3);
                LocalDate startDate = resultSet.getDate(4).toLocalDate();
                LocalDate endDate = resultSet.getDate(5).toLocalDate();
                int amount = resultSet.getInt(6);
                double price = resultSet.getDouble(7);
                String image = resultSet.getString(8);
                int companyID = resultSet.getInt(9);
                int categoryID = resultSet.getInt(10);

                Coupon coupon = new Coupon(id, companyID, categoryID, title, description, startDate, endDate, amount, price, image);
                couponList.add(coupon);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }

        return couponList;
    }

    @Override
    public Coupon getOneCoupon(int couponID) {

        Coupon coupon = null;

        String sql = "SELECT * FROM coupon.coupons WHERE ID = ?;";
        Connection connection = pool.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, couponID);

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                int companyID = resultSet.getInt(2);
                int categoryID = resultSet.getInt(3);
                String title = resultSet.getString(2);
                String description = resultSet.getString(3);
                LocalDate startDate = resultSet.getDate(4).toLocalDate();
                LocalDate endDate = resultSet.getDate(5).toLocalDate();
                int amount = resultSet.getInt(6);
                double price = resultSet.getDouble(7);
                String image = resultSet.getString(8);

                coupon = new Coupon(couponID, companyID, categoryID, title, description, startDate, endDate, amount, price, image);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }

        return coupon;
    }

    @Override
    public void addCouponPurchase(int customerID, int couponID) {

        Coupon coupon;

        String select = "SELECT * FROM coupon.coupons WHERE ID = ?;";
        String insert = "INSERT INTO CUSTOMERS_VS_COUPONS (CUSTOMER_ID, COUPON_ID) VALUES (?, ?);";
        Connection connection = pool.getConnection();

        try (PreparedStatement selectPstmt = connection.prepareStatement(select);
             PreparedStatement insertPstmt = connection.prepareStatement(insert)) {
            selectPstmt.setInt(1, couponID);

            ResultSet resultSet = selectPstmt.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString(2);
                String description = resultSet.getString(3);
                LocalDate startDate = resultSet.getDate(4).toLocalDate();
                LocalDate endDate = resultSet.getDate(5).toLocalDate();
                int amount = resultSet.getInt(6);
                double price = resultSet.getDouble(7);
                String image = resultSet.getString(8);
                int companyID = resultSet.getInt(9);
                int categoryID = resultSet.getInt(10);

                coupon = new Coupon(couponID, companyID, categoryID, title, description, startDate, endDate, amount, price, image);
            }

            insertPstmt.setInt(1, customerID);
            insertPstmt.setInt(2, couponID);

            insertPstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
    }

    @Override
    public void deleteCouponPurchase(int customerID, int couponID) {

        Coupon coupon = null;
    }

    public Coupon getCouponByTitle(String title) {

        Coupon coupon = null;

        String sql = "SELECT * FROM coupon.coupons WHERE TITLE = ?;";
        Connection connection = pool.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, title);

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                int companyID = resultSet.getInt(2);
                int categoryID = resultSet.getInt(3);
                String description = resultSet.getString(5);
                LocalDate startDate = resultSet.getDate(6).toLocalDate();
                LocalDate endDate = resultSet.getDate(7).toLocalDate();
                int amount = resultSet.getInt(8);
                double price = resultSet.getDouble(9);
                String image = resultSet.getString(10);

                coupon = new Coupon(id, companyID, categoryID, title, description, startDate, endDate, amount, price, image);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }

        return coupon;
    }
}
