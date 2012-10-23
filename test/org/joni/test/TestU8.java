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

import org.joni.Option;
import org.joni.Syntax;

public class TestU8 extends Test {

    public int option() {
        return Option.DEFAULT;
    }

    public Syntax syntax() {
        return Syntax.DEFAULT;
    }

    public void test() {
        x2s("x{2}", "xx", 0, 2, Option.IGNORECASE);
        x2s("x{2}", "XX", 0, 2, Option.IGNORECASE);
        x2s("x{3}", "XxX", 0, 3, Option.IGNORECASE);
        ns("x{2}", "x", Option.IGNORECASE);
        ns("x{2}", "X", Option.IGNORECASE);

        x2s("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 0, 35, Option.IGNORECASE);
        x2s("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", 0, 35, Option.IGNORECASE);
        x2s("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAA", 0, 35, Option.IGNORECASE);

        // x2s(pat2, str2, 4, 4);
        // x2s(pat2, str2, 4, 4, Option.IGNORECASE);

        ns("(?i-mx:ak)a", "ema");

        x2s("(?i:!\\[CDAT)", "![CDAT", 0, 6);
        x2s("(?i:\\!\\[CDAa)", "\\![CDAa", 1, 7);
        x2s("(?i:\\!\\[CDAb)", "\\![CDAb", 1, 7);
    }

    public static void main(String[] args) throws Throwable {
        new TestU8().run();
    }
}
