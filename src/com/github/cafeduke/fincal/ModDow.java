package com.github.cafeduke.fincal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;

public class ModDow
{
    private List listBigMountain = new ArrayList<>();

    private List listBigValley = new ArrayList<>();

    private int val[] = null;

    public static final int SIG_VERTICAL_SIZE = 10;

    public static final int SIG_HORIZONTAL_SIZE = 2;

    public ModDow(int val[])
    {
        this.val = val;
    }

    public static void main(String arg[])
    {
        //                                  0     1    2    3    4    5    6    7    8    9    10   11   12   13   14   15   16   17   18   
        ModDow dow = new ModDow(new int[] { 100, 120, 131, 140, 135, 130, 110, 118, 128, 130, 123, 120, 125, 129, 131, 145, 150, 148, 145 });
        dow.scan();
    }

    /**
     * Up trend creates one side of the slope which cannot be called a mountain yet. However, uptread creates the other side of
     * a valley, thus completing and deepening a valley.
     * 
     */

    public void scan()
    {
        if (val.length < 3)
            return;

        Triplet mountain = new Triplet(), valley = new Triplet();
        boolean uptrend = (val[1] >= val[0]);
        mountain.ia = valley.ia = 0;

        for (int i = 2; i < val.length; ++i)
        {
            System.out.println("-------------------------------------------------------------------------");
            System.out.println("i=" + i + " val=" + val[i]);
            if (val[i] >= val[i - 1] && uptrend)
            {
                // Up trend is going on. 
                System.out.println("Up trend is going on");

                // Up trend of mountain is also deepening of valley 
                valley.ic = (valley.ib == -1) ? -1 : i;

            }
            else if (val[i] < val[i - 1] && !uptrend)
            {
                // Down trend is going on.
                System.out.println("Down trend is going on");

                // Down trend of valley is also heightening of mountain
                mountain.ic = (mountain.ib == -1) ? -1 : i;

            }
            else if (val[i] < val[i - 1])
            {
                /* Mountain to valley
                 * ------------------
                 * x
                 *     x
                 *          x             <------ val[i-1] 
                 *       x                <------ val[i]   
                 */
                uptrend = false;
                if (mountain.ib == -1)
                {
                    System.out.println("Trend changed. New peak registered");
                    mountain.ib = i - 1;
                    mountain.ic = i;
                }
                else if (val[i - 1] >= val[mountain.ib])
                {
                    // The new peak is taller than the stored mountain's peak
                    System.out.println("The new peak is taller than the stored mountain's peak. Mountain=" + mountain + " CurrPeak=" + val[i - 1]);
                    mountain.ib = i - 1;
                    mountain.ic = i;
                }
                else
                {
                    System.out.println("The new peak is shorter than the stored mountain's peak. Mountain=" + mountain + " CurrPeak=" + val[i - 1]);
                }

                if (valley.isSig())
                {
                    System.out.println("Significant valley found. Valley=" + valley + " PointsDiff=" + valley.getVerticalSize() + " DaysDiff=" + valley.getHorizontalSize());
                    mountain.ia = valley.ib;
                    mountain.ib = valley.ic;
                }
            }
            else
            {
                /* Valley to Mountain
                 * ------------------
                 *            x
                 *        x
                 *  x                   <------ val[i-1] 
                 *     x                <------ val[i]   
                 */
                uptrend = true;
                if (valley.ib == -1)
                {
                    System.out.println("Trend changed. New bottom registered");
                    valley.ib = i - 1;
                    valley.ic = i;
                }
                else if (val[i - 1] <= val[valley.ib])
                {
                    // The new bottom is deeper than the stored valley's bottom
                    System.out.println("The new bottom is deeper than the stored valley's bottom. Valley=" + valley + " CurrBottom=" + val[i - 1]);
                    valley.ib = i - 1;
                    valley.ic = i;
                }
                else
                {
                    System.out.println("The new bottom is shallow than the stored valley's bottom. Valley=" + valley + " CurrBottom=" + val[i - 1]);
                }

                if (mountain.isSig())
                {
                    System.out.println("Significant mountain found. Mountain=" + mountain + " PointsDiff=" + mountain.getVerticalSize() + " DaysDiff=" + mountain.getHorizontalSize());
                    valley.ia = mountain.ib;
                    valley.ib = mountain.ic;
                }
            }
            System.out.println("Mountain=" + mountain + " Valley=" + valley);
        }
    }

    public boolean isBigMountain(int a, int b, int c)
    {
        return false;
    }

    class Triplet
    {
        private int ia, ib, ic;

        public Triplet()
        {
            this(-1, -1, -1);
        }

        public Triplet(int ia, int ib, int ic)
        {
            this.ia = ia;
            this.ib = ib;
            this.ic = ic;
        }

        public boolean isEmpty()
        {
            return ia == -1 && ib == -1 && ic == -1;
        }

        public boolean isValid()
        {
            return ia != -1 && ib != -1 && ic != -1;
        }

        public boolean isSig()
        {
            return getVerticalSize() >= SIG_VERTICAL_SIZE && getHorizontalSize() >= SIG_HORIZONTAL_SIZE;
        }

        public int getVerticalSize()
        {
            if (!isValid())
                return 0;

            int a = val[ia], b = val[ib], c = val[ic];
            int height = 0;
            if (b > a)
            {
                // Mountain
                Validate.isTrue(c <= b, "Not a mountain. It is increasing grapth. Triplet=" + this);
                int mountainBase = (c > a) ? c : a;
                height = b - mountainBase;
            }
            else if (b < a)
            {
                // Valley
                Validate.isTrue(c >= b, "Not a valley. It is decreasing grapth. Triplet=" + this);
                int valleyBase = (c < a) ? c : a;
                height = valleyBase - b;
            }
            return height;
        }

        public int getHorizontalSize()
        {
            if (!isValid())
                return 0;

            int a = val[ia], b = val[ib], c = val[ic];
            int width = 0;
            int start, end, delta, base;
            if (b > a)
            {
                // Mountain
                Validate.isTrue(c <= b, "Not a mountain. It is increasing grapth. Triplet=" + this);

                /* c >=a                    c < a
                 * ---------------------------------------
                 * a                             a  
                 *    x                             x  
                 *       x                              x 
                 *            b                x 
                 *         c                c     
                 *                                
                 */

                if (c >= a)
                {
                    start = ic;
                    end = ia;
                    delta = -1;
                    base = c;
                }
                else
                {
                    start = ia;
                    end = ic;
                    delta = 1;
                    base = a;
                }

                for (int i = start; i != end; i += delta)
                {
                    if (val[i] < base)
                        break;
                    ++width;
                }
            }
            else if (b < a)
            {
                // Valley
                Validate.isTrue(c >= b, "Not a valley. It is decreasing grapth. Triplet=" + this);

                /* 
                 *   c >= a                      c < a
                 * ---------------------------------------------
                 *         a                                   a
                 *      x                              x
                 *   b                           b
                 *        x                         x
                 *               c                       c  
                 */
                if (c >= a)
                {
                    start = ia;
                    end = ic;
                    delta = 1;
                    base = a;
                }
                else
                {
                    start = ic;
                    end = ia;
                    delta = -1;
                    base = c;
                }
                for (int i = start; i != end; i += delta)
                {
                    if (val[i] > base)
                        break;
                    ++width;
                }
            }
            return width;
        }

        @Override
        public String toString()
        {
            return String.format("(%d,%d,%d)", ia, ib, ic);
        }
    }
}
