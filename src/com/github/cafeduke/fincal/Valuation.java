package com.github.cafeduke.fincal;

import static com.github.cafeduke.util.Util.heading;
import static com.github.cafeduke.util.Util.hr;
import static com.github.cafeduke.util.Util.validateArgCSV;
import static com.github.cafeduke.util.Util.validateArgDouble;
import static java.util.Map.entry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Valuation
{
    private double firstCashFlow = 0;

    private double rate[] = null;

    private int year[] = null;

    private double rateFD = 0;

    private double rateRiskPP = 0;

    private double rateGGPP = 0;

    private double mcap = 0;

    private boolean marginOfSafety = true;

    private double price = 0;

    private int totalYear = 0;

    private List<Double> listPresentCF = new ArrayList<>();

    private List<Double> listFutureCF = new ArrayList<>();

    private static final String BR = System.getProperty("line.separator");

    private static final String P = BR + BR;

    public static void main(String arg[])
    {
        Valuation valuation = new Valuation();
        valuation.parseArg(arg);
        valuation.fillCashFlows();
        valuation.doVaulation();
    }

    public void doVaulation()
    {
        heading("Yearly Cash Flows");
        System.out.println();
        System.out.println(String.format("+ %-5s + %18s + %18s +", " ", " ", " ").replace(' ', '-'));
        System.out.println(String.format("| %-5s | %18s | %18s |", "Year", "FutureCF", "PresentCF"));
        System.out.println(String.format("+ %-5s + %18s + %18s +", " ", " ", " ").replace(' ', '-'));

        for (int i = 0; i < listPresentCF.size(); ++i)
            System.out.println(String.format("| %02d%3s | %18.2f | %18.2f |", i + 1, " ", listFutureCF.get(i), listPresentCF.get(i)));
        System.out.println(String.format("+ %-5s + %18s + %18s +", " ", " ", " ").replace(' ', '-'));

        double lastFutureCF = listFutureCF.get(listFutureCF.size() - 1);
        double pmtForPP = lastFutureCF * (1 + rateGGPP);

        double perpetualFutureCF = FinUtil.getGrowingPerpetuity(pmtForPP, rateRiskPP, rateGGPP);
        double perpetualPresentCF = FinUtil.getPresentValueGivenFutureValue(perpetualFutureCF, rateFD, totalYear);

        double sumPresentCF = listPresentCF.stream().mapToDouble(Double::doubleValue).sum();
        double myMCap = perpetualPresentCF + sumPresentCF;

        double myMCapSafe = (marginOfSafety) ? myMCap * 0.5 : myMCap;

        heading("Valuation after " + totalYear + " years");
        System.out.printf("%-60s = %15.2f%n", "FutureValue of CashFlow received  after " + totalYear + " years", lastFutureCF);
        System.out.println(String.format("%-60s = %15.2f", "FutureValue of growing perpetuity after " + totalYear + " years", perpetualFutureCF));
        hr();
        System.out.println(String.format("%-60s = %15.2f", "MCap of company after " + totalYear + " years", lastFutureCF + perpetualFutureCF));

        heading("Valuation today");
        System.out.println(String.format("%-60s = %15.2f", "Sum of all Present CashFlows", sumPresentCF));
        System.out.println(String.format("%-60s = %15.2f", "PresentValue of growing perpetuity", perpetualPresentCF));
        hr();
        System.out.println(String.format("%-60s = %15.2f", "PresentValue of all CashFlows", myMCap));
        hr();
        String mesg = (marginOfSafety) ? "MCAP of company today with 50% margin of safety" : "MCAP of company today without margin of safety";
        System.out.println(String.format("%-60s = %15.2f", mesg, myMCapSafe));

        if (mcap > 0 && price > 0)
        {
            System.out.println(String.format("%-60s = %15.2f", "MCap of the stock", mcap));
            hr();
            System.out.println(String.format("%-60s = %15.2f", "Current price", price));
            System.out.println(String.format("%-60s = %15.2f", "Fair price", (myMCapSafe * price) / mcap));
        }
    }

    private void fillCashFlows()
    {
        double currFutureCF = firstCashFlow, currPresentCF = 0;
        int sumYear = 0;
        for (int i = 0; i < rate.length; ++i)
        {
            double r = rate[i];
            double y = year[i];
            for (int j = 0; j < y; ++j)
            {
                ++sumYear;
                currFutureCF = FinUtil.getNextCashFlow(currFutureCF, r);
                currPresentCF = FinUtil.getPresentValueGivenFutureValue(currFutureCF, rateFD, sumYear);
                listFutureCF.add(currFutureCF);
                listPresentCF.add(currPresentCF);
            }
        }
        totalYear = sumYear;
    }

    private void parseArg(String arg[])
    {
        if (arg.length == 0)
            dieUsage();

        for (int i = 0; i < arg.length; ++i)
        {
            String currArg = arg[i];
            if (currArg.equals("--fcf"))
                firstCashFlow = validateArgDouble(arg, i++);
            else if (currArg.equals("--rate"))
                rate = Arrays.stream(validateArgCSV(arg, i++)).mapToDouble(Double::parseDouble).map((x) -> x * 0.01).toArray();
            else if (currArg.equals("--year"))
                year = Arrays.stream(validateArgCSV(arg, i++)).mapToInt(Integer::parseInt).toArray();
            else if (currArg.equals("--rate-fd"))
                rateFD = validateArgDouble(arg, i++) * 0.01;
            else if (currArg.equals("--rate-risk-pp") || currArg.equals("--rate-rpp"))
                rateRiskPP = validateArgDouble(arg, i++) * 0.01;
            else if (currArg.equals("--rate-global-growth-pp") || currArg.equals("--rate-ggpp"))
                rateGGPP = validateArgDouble(arg, i++) * 0.01;
            else if (currArg.equals("--mcap"))
                mcap = validateArgDouble(arg, i++);
            else if (currArg.equals("--price"))
                price = validateArgDouble(arg, i++);
            else if (currArg.equals("--ignore-margin-safety") || currArg.equals("--no-mos"))
                marginOfSafety = false;
            else if (currArg.equals("--help") || currArg.equals("-h"))
                dieUsage();
            else
                dieUsage("Error: Unknown option '" + currArg + "'");
        }
        validateArg();
    }

    private void validateArg()
    {
        Map<String, Double> mapValToArg = Map.ofEntries(entry("--fcf", firstCashFlow), entry("--rateFD", rateFD), entry("--rate-rpp", rateRiskPP));

        for (Map.Entry<String, Double> entry : mapValToArg.entrySet())
            if (entry.getValue() == 0)
                dieUsage("Argument " + entry.getKey() + " is mandatory.");

        if (rate == null)
            dieUsage("Argument --rate is mandatory");

        if (year == null)
            dieUsage("Argument --year is mandatory");
    }

    private void dieUsage()
    {
        dieUsage(null, 0);
    }

    private void dieUsage(String mesg)
    {
        dieUsage(mesg, 1);
    }

    private void dieUsage(String mesg, int status)
    {
        if (mesg != null)
            System.out.println(mesg);
        usage();
        System.exit(status);
    }

    private void usage()
    {
        StringBuilder builder = new StringBuilder("Usage : ").append(BR).append("  --fcf     <First cash flow>").append(P)

                .append("  --rate    <CSV having rate of growth>").append(P)

                .append("  --year    <CSV having number of years for each rate of growth>").append(P)

                .append("  --rate-fd <Returns from safe instrument like fixed deposit instead of investing in stock>").append(P)

                .append("  --rate-risk-pp|--rate-rpp <Rate of returns you expect for perpetuity considering the risk of investment>").append(BR).append("    Higher rates imply risky stock. We would want a risky stock to quickly grow as we are concerned about sustained growth.").append(P)

                .append("  --rate-global-growth-pp|--rate-ggpp <Growth used to discount perpetual cash flow>").append(BR).append("    The rate at which the world is expected to grow for perpetual calculations").append(P)

                .append("  [ --mcap <Marketcap of the stock> ]").append(P)

                .append("  [ --price <Current price of the stock> ]").append(P)

                .append("  [ --ignore-margin-safety|--no-mos ]").append(BR).append("    Default: false").append(BR).append("    Do not consider margin of safety. This is done when risk is already factored with higher rate-risk-pp>").append(BR);
        System.out.println(builder);
    }
}
