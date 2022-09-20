package com.github.cafeduke.fincal;

public class FinUtil
{
    /**
     * Return the future value for the <b>pmt</b> made at <b>r</b> rate of interest for <b>n</b> years.
     * 
     * @param pmt
     * @param r
     * @param n
     * @return
     */
    public static double getFutureValueGivenPmt(double pmt, double r, double n)
    {
        return pmt * ((Math.pow(1 + r, n) - 1) / r);
    }

    /**
     * Return the present value for <b>n</b> number of <b>pmt</b> payments made each time-period.
     * 
     * <br>
     * <b>Example</b> Paying pmt=100 every month at r=1.0833% credit card interest per month, compounded monthly
     * (13% per year) for n=6 months has present value 577.89. This means a one shot payment of 577.89 is equivalent to
     * this credit payment.
     * 
     * @param pmt Payment made per time-period.
     * @param r rate of interest per time-period.
     * @param n Number of time-periods.
     * @return Return the present value for <b>n</b> number of <b>pmt</b> payments made each time-period.
     */
    public static double getPresentValueGivenPmt(double pmt, double r, double n)
    {
        return pmt * (1 - (1 / Math.pow(1 + r, n)) / r);
    }

    /**
     * Return present value given future value
     * 
     * @param fv Future value of the cash flow
     * @param r rate of interest per time-period
     * @param n Number of time-periods
     */
    public static double getPresentValueGivenFutureValue(double fv, double r, double n)
    {
        return fv / Math.pow(1 + r, n);
    }

    /**
     * Return the present value given future value and payments made each time-period.
     * 
     * <p>
     * Here, the payments made will result in a future value (say fvPmt). The user wants a future value (say fv). Now, the difference
     * (fvPmt - fv) is the final future value. We now need to calculate the present value for this final future value.
     * 
     * <p>
     * <i>What does the present value provide?</i> User makes payment <b>pmt</b> every time-period for <b>n</b> time-periods in a investment
     * with rate of interest <b>r</b> hoping for a future value <b>fv</b>. To achieve this the user needs a initial investment (present value)
     * as returned by this function.
     * 
     * @param fv Future value expected
     * @param pmt Payment made per time-period
     * @param r Rate of interest
     * @param n Number of time-periods.
     * @return The present value (initial investment) required to achieve a future value with periodic payments.
     */
    public static double getPresentValueGivenFutureValueWithPmt(double fv, double pmt, double r, double n)
    {
        double deltaFV = fv - getFutureValueGivenPmt(pmt, r, n);
        return getPresentValueGivenFutureValue(deltaFV, r, n);
    }

    /**
     * Return the payments to be made per time-period for <b>n</b> time-periods at rate of interest <b>r</b> to reach a future value <b>fv</b>.
     * 
     * @param fv Future value expected
     * @param r Rate of interest
     * @param n Number of time-periods.
     * @return
     */
    public static double getPmtGivenFutureValue(double fv, double r, double n)
    {
        return fv / ((Math.pow(1 + r, n) - 1) / r);
    }

    /**
     * Return the payments to be made per time-period for <b>n</b> time-periods at rate of interest <b>r</b> for a loan of <b>pv</b>
     * 
     * @param pv Present value
     * @param r Rate of interest
     * @param n Number of time-periods.
     * @return The payment amount to be paid per time-period to clear the loan.
     */
    public static double getPmtGivenPresentValue(double pv, double r, double n)
    {
        return pv / ((1 - (1 / (Math.pow(1 + r, n)))) / r);
    }

    /**
     * Return the payments to be made per time-period for <b>n</b> time-periods at rate of interest <b>r</b> to attain a future value
     * of <b>fv</b> with an initial investment of <b>pv</b>
     * 
     * <p/>
     * <i>Example:</i> A user wants to reach a future value of 2 crore after 20 years by investing in nifty expecting 15% returns.
     * The user also does an initial investment of 5 lakhs. Annual payment required = 115348.67.
     * <p/>
     * <code> getPmtGivenFutureAndPresentValue(20000000, 500000, 15, 20) = 115348.67 </code>
     * 
     * @param fv
     * @param pv
     * @param r
     * @param n
     * @return
     */
    public static double getPmtGivenFutureAndPresentValue(double fv, double pv, double r, double n)
    {
        return (fv - (pv * Math.pow(1 + r, n))) / ((Math.pow(1 + r, n) - 1) / r);
    }

    /**
     * Return the next cash flow incrementing <b>fv</b> by a rate <b>r</b>
     * 
     * @param fv Future value
     * @param r rate of interest
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
     * @param r rate of interest
     */
    public static double getConstantPerpetuity(double pmt, double r)
    {
        return pmt / r;
    }

    /**
     * Return the value of a growing perpetuity.
     * 
     * @param pmt Annual payment growing at the rate of <b>g</b> to be received in perpetuity
     * @param r rate of interest | Rate of risk
     * @param g Rate of growth
     * @return
     */
    public static double getGrowingPerpetuity(double pmt, double r, double g)
    {
        return pmt / (r - g);
    }
}
