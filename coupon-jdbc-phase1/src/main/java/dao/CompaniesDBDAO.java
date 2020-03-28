package dao;

import entities.Company;
import entities.Coupon;
import pool.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompaniesDBDAO implements CompaniesDAO {

    private ConnectionPool pool = ConnectionPool.getInstance();

    @Override
    public boolean isCompanyExists(String email, String password) {

        boolean isExist = true;

        String sql = "SELECT * FROM coupon.companies WHERE EMAIL = ? AND PASSWORD = ?;";
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
    public void addCompany(Company company) {

        String sql = "INSERT INTO coupon.companies (NAME, EMAIL, PASSWORD) VALUES (? ,?, ?);";
        Connection connection = pool.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, company.getName());
            pstmt.setString(2, company.getEmail());
            pstmt.setString(3, company.getPassword());

            pstmt.executeUpdate();

            ResultSet resultSet = pstmt.getGeneratedKeys();

            if (resultSet.next()) {
                company.setID(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
    }

    @Override
    public void updateCompany(Company company) {

        String sql = "UPDATE coupon.companies SET NAME = ?, EMAIL = ?, PASSWORD = ?;";
        Connection connection = pool.getConnection();
//        Do I need to change the id too?

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, company.getName());
            pstmt.setString(2, company.getEmail());
            pstmt.setString(3, company.getPassword());

            pstmt.executeUpdate();
            company = new Company(company.getName(), company.getEmail(), company.getPassword(), company.getCoupons());

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
    }

    @Override
    public void deleteCompany(int companyID) {

        String sql = "DELETE FROM coupon.companies WHERE ID = ?;";
        Connection connection = pool.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, companyID);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }
    }

    @Override
    public List<Company> getAllCompanies() {

        Company company = null;
        List<Company> companyList = null;

        String sql = "SELECT * FROM coupon.companies;";
        Connection connection = pool.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();

            companyList = new ArrayList <>();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                String password = resultSet.getString(4);

                company = new Company(id, name, email, password);
                companyList.add(company);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }

        return companyList;
    }

    @Override
    public Company getOneCompany(int companyID) {

        Company company = null;

        String sql = "SELECT * FROM coupon.companies WHERE ID = ?;";
        Connection connection = pool.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, companyID);

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString(2);
                String email = resultSet.getString(3);
                String password = resultSet.getString(4);
                List<Coupon> coupons = (List<Coupon>) resultSet.getArray(5);

                company = new Company(companyID, name, email, password, coupons);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }

        return company;
    }

    public Company getCompanyByName(String companyName) {

        Company company = null;

        String sql = "SELECT * FROM coupon.companies WHERE NAME = ?;";
        Connection connection = pool.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, companyName);

            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String email = resultSet.getString(3);
                String password = resultSet.getString(4);
                company = new Company(id, companyName, email, password);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pool.returnConnection(connection);
        }

        return company;
    }
}