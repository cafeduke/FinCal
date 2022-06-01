package com.github.cafeduke.fincal;

public class FinUtil
{
    /**
     * Return present value given future value
     * 
     * @param fv Future value of the cash flow
     * @param r Rate of return per time-period
     * @param n Number of time-periods
     */
    public static double getPresentValue(double fv, double r, double n)
    {
        return fv / Math.pow(1 + r, n);
    }

    /**
     * Return the next cash flow incrementing <b>fv</b> by a rate <b>r</b>
     * 
     * @param fv Future value
     * @param r Rate of return
     * @return The value of the next cash flow.
     */
    public static double getNextCashFlow(double fv, double r)
    {
        return fv * (1 + r);
    }

    /**
     * Return the value of a constant perpetuity.
     * 
     * @param pmt Annual payment to be received in perpetuity
     * @param r Rate of return
     */
    public static double getConstantPerpetuity(double pmt, double r)
    {
        return pmt / r;
    }

    /**
     * Return the value of a growing perpetuity.
     * 
     * @param pmt Annual payment growing at the rate of <b>g</b> to be received in perpetuity
     * @param r Rate of return | Rate of risk
     * @param g Rate of growth
     * @return
     */
    public static double getGrowingPerpetuity(double pmt, double r, double g)
    {
        return pmt / (r - g);
    }
}
