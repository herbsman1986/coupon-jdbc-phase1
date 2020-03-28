package dao;

import entities.Coupon;

import java.util.List;

public interface CouponsDAO {

    void addCoupon(Coupon coupon);

    void updateCoupon(Coupon coupon);

    void deleteCoupon(int couponID);

    List<Coupon> getAllCoupons();

    Coupon getOneCoupon(int couponID);

    void addCouponPurchase(int customerID, int couponID);

    void deleteCouponPurchase(int customerID, int couponID);
}
