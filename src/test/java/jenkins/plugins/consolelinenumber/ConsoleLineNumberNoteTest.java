/*
 * The MIT License
 * 
 * Copyright (c) 2011 Eric A. Smalling
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jenkins.plugins.consolelinenumber;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import hudson.MarkupText;
import hudson.console.ConsoleNote;
import hudson.tasks._ant.AntTargetNote;

import java.util.TimeZone;

import org.apache.commons.lang.SerializationUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Unit test for the {@link ConsoleLineNumberNote} class.
 */
public class ConsoleLineNumberNoteTest {

  private static TimeZone systemDefaultTimeZone;

  /**
   */
  @Test
  public void consoleLineNote() {
    assertThat(annotate("line", new ConsoleLineNumberNote("0")),
        is("<a name=\"0\"><b>0</b></a>  line"));
  }

  /**
   */
  @Test
  public void serialization() {
    assertThat(annotate("line", serialize(new ConsoleLineNumberNote("0"))),
        is("<a name=\"0\"><b>0</b></a>  line"));
  }

  /**
   */
  @Test
  public void lineNumberThenAntTargetNote() {
    assertThat(annotate("target:", new ConsoleLineNumberNote("0"), new AntTargetNote()),
        is("<a name=\"0\"><b>0</b></a>  <b class=ant-target>target</b>:"));
  }

  /**
   */

  @Test
  public void antTargetNoteThenLineNumber() {
    assertThat(annotate("target:", new AntTargetNote(), new ConsoleLineNumberNote("0")),
        is("<a name=\"0\"><b>0</b></a>  <b class=ant-target>target</b>:"));
  }

  @SuppressWarnings("unchecked")
  private String annotate(String text, ConsoleNote... notes) {
    Object context = new Object();
    MarkupText markupText = new MarkupText(text);
    for (ConsoleNote note : notes) {
      note.annotate(context, markupText, 0);
    }
    return markupText.toString(true);
  }

  private ConsoleLineNumberNote serialize(ConsoleLineNumberNote note) {
    return (ConsoleLineNumberNote) SerializationUtils.clone(note);
  }
}
