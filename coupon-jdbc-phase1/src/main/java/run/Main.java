package run;

import dao.CompaniesDAO;
import dao.CompaniesDBDAO;
import pool.ConnectionPool;


public class Main {

    public static void main(String[] args) {

        ConnectionPool pool = ConnectionPool.getInstance();
//        Connection connection = pool.getConnection();
//        System.out.println(connection);

        CompaniesDAO dao = new CompaniesDBDAO();
//        List<Company> companyList = dao.getAllCompanies();
//        for (Company company : companyList) {
//            System.out.println(company);
//        }
//        Company company = new Company("FOX", "fox@gmail.com", "fgh", null);
//        dao.addCompany(company);

//        dao.deleteCompany(13);

//        System.out.println(((CompaniesDBDAO) dao).getCompanyByName("HOT"));

        System.out.println(dao.getOneCompany(1));

        pool.closeAllConnections();

    }
}

