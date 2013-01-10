/*
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.joni.test;

import org.joni.Config;
import org.joni.Matcher;
import org.joni.Option;
import org.joni.Regex;
import org.joni.Region;
import org.joni.Syntax;
import org.joni.exception.JOniException;

import java.nio.charset.Charset;

public abstract class Test {
    static final boolean VERBOSE = false;

    int nsucc;
    int nerror;
    int nfail;

    public abstract int option();
    public abstract Charset encoding();
    public abstract Charset testEncoding();
    public abstract Syntax syntax();

    protected String repr(char[] chars) {
        return new String(chars, 0, length(chars));
    }

    protected int length(char[] chars) {
        return chars.length;
    }

    public void xx(char[]pattern, char[]str, int from, int to, int mem, boolean not) {
        xx(pattern, str, from, to, mem, not, option());
    }

    public void xx(char[] pattern, char[] str, int from, int to, int mem, boolean not, int option) {
        Regex reg;

        try {
            reg = new Regex(pattern, 0, length(pattern), option, syntax());
        } catch (JOniException je) {
            Config.err.println("Pattern: " + repr(pattern) + " Str: " + repr(str));
            je.printStackTrace(Config.err);
            Config.err.println("ERROR: " + je.getMessage());
            nerror++;
            return;
        } catch (Exception e) {
            Config.err.println("Pattern: " + repr(pattern) + " Str: " + repr(str));
            e.printStackTrace(Config.err);
            Config.err.println("SEVERE ERROR: " + e.getMessage());
            nerror++;
            return;
        }

        Matcher m = reg.matcher(str, 0, length(str));
        Region region;

        int r = 0;
        try {
            r = m.search(0, length(str), Option.NONE);
            region = m.getEagerRegion();
        } catch (JOniException je) {
            Config.err.println("Pattern: " + repr(pattern) + " Str: " + repr(str));
            je.printStackTrace(Config.err);
            Config.err.println("ERROR: " + je.getMessage());
            nerror++;
            return;
        } catch (Exception e) {
            Config.err.println("Pattern: " + repr(pattern) + " Str: " + repr(str));
            e.printStackTrace(Config.err);
            Config.err.println("SEVERE ERROR: " + e.getMessage());
            nerror++;
            return;
        }

        if (r == -1) {
            if (not) {
                if (VERBOSE) Config.log.println("OK(N): /" + repr(pattern) + "/ '" + repr(str) + "'");
                nsucc++;
            } else {
                Config.log.println("FAIL: /" + repr(pattern) + "/ '" + repr(str) + "'");
                Thread.dumpStack();
                nfail++;
            }
        } else {
            if (not) {
                Config.log.println("FAIL(N): /" + repr(pattern) + "/ '" + repr(str) + "'");
                nfail++;
            } else {
                if (region.beg[mem] == from && region.end[mem] == to) {
                    if (VERBOSE)  Config.log.println("OK: /" + repr(pattern) + "/ '" +repr(str) + "'");
                    nsucc++;
                } else {
                    Config.log.println("FAIL: /" + repr(pattern) + "/ '" + repr(str) + "' " +
                            from + "-" + to + " : " + region.beg[mem] + "-" + region.end[mem]
                            );
                    nfail++;
                }
            }
        }
    }

    protected void x2(char[] pattern, char[] str, int from, int to) {
        xx(pattern, str, from, to, 0, false);
    }

    protected void x2(char[] pattern, char[] str, int from, int to, int option) {
        xx(pattern, str, from, to, 0, false, option);
    }

    protected void x3(char[] pattern, char[] str, int from, int to, int mem) {
        xx(pattern, str, from, to, mem, false);
    }

    protected void n(char[] pattern, char[] str) {
        xx(pattern, str, 0, 0, 0, true);
    }

    protected void n(char[] pattern, char[] str, int option) {
        xx(pattern, str, 0, 0, 0, true, option);
    }

    public void xxs(String pattern, String str, int from, int to, int mem, boolean not) {
        xxs(pattern, str, from, to, mem, not, option());
    }

    public void xxs(String pattern, String str, int from, int to, int mem, boolean not, int option) {
        // Re-encode string using the test's encoding and translate result indices.
        String pattern2 = new String(pattern.getBytes(testEncoding()), encoding());
        byte[] strBytes = str.getBytes(testEncoding());
        String str2 = new String(strBytes, encoding());
        int from2 = from == 0 ? 0 : new String(strBytes, 0, from, encoding()).length();
        int to2 = to == 0 ? 0 : new String(strBytes, 0, to, encoding()).length();
        xx(pattern2.toCharArray(), str2.toCharArray(), from2, to2, mem, not, option);
    }

    public void x2s(String pattern, String str, int from, int to) {
        x2s(pattern, str, from, to, option());
    }

    public void x2s(String pattern, String str, int from, int to, int option) {
        xxs(pattern, str, from, to, 0, false, option);
    }

    public void x3s(String pattern, String str, int from, int to, int mem) {
        x3s(pattern, str, from, to, mem, option());
    }

    public void x3s(String pattern, String str, int from, int to, int mem, int option) {
        xxs(pattern, str, from, to, mem, false, option);
    }

    public void ns(String pattern, String str) {
        ns(pattern, str, option());
    }

    public void ns(String pattern, String str, int option) {
        xxs(pattern, str, 0, 0, 0, true, option);
    }

    public void printResults() {
        Config.log.println("RESULT   SUCC: " + nsucc + ",  FAIL: " + nfail + ",  ERROR: " + nerror + " Test: " + getClass().getSimpleName());
    }

    public abstract void test();

    public final void run() {
        test();
        printResults();
    }

}
